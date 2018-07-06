/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.model.persistence.neo4j;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

/**
 * @author Reiner Jung
 *
 */
public class ModelResource {

    static final String INTERNAL_ID = ":internalId";
    static final String PROXY_OBJECT = ":proxyObject";

    private final File graphDirectory;
    private final GraphDatabaseService graphDatabaseService;
    private final List<EFactory> eFactories = new ArrayList<>();
    private final List<EClass> classes = new ArrayList<>();

    private final Map<EObject, Node> objectNodeMap = new ConcurrentHashMap<>();
    private final StoreModelFacility storeModelFacility;
    private final UpdateModelFacility updateModelFacility;
    private final QueryModelFacility queryModelFacility;
    private final DeleteModelFacility deleteModelFacility;

    /**
     * Creates a new {@link GraphDatabaseService} at the given location.
     *
     * @param factory
     *            factory for the particular metamodel
     * @param graphDirectory
     *            Directory where the graph database shall be located
     */
    public ModelResource(final EFactory factory, final File graphDirectory) {
        this.graphDirectory = graphDirectory.getAbsoluteFile();
        if (!this.graphDirectory.exists()) {
            if (!this.graphDirectory.mkdirs()) {
                throw new InternalError("Cannot create directories for path " + this.graphDirectory);
            }
            if (!this.graphDirectory.isDirectory()) {
                throw new InternalError("Path is not a directory " + this.graphDirectory);
            }
        }

        this.graphDatabaseService = new GraphDatabaseFactory().newEmbeddedDatabase(this.graphDirectory);

        this.storeModelFacility = new StoreModelFacility(this.graphDatabaseService, this.objectNodeMap);
        this.queryModelFacility = new QueryModelFacility(this.graphDatabaseService, this.objectNodeMap,
                this.eFactories);
        this.updateModelFacility = new UpdateModelFacility(this.graphDatabaseService, this.objectNodeMap);
        this.deleteModelFacility = new DeleteModelFacility(this.graphDatabaseService, this.objectNodeMap);

        this.registerShutdownHook(this.graphDatabaseService);
        this.eFactories.add(factory);
        if (factory.getEPackage() != null) {
            for (final EClassifier classifier : factory.getEPackage().getEClassifiers()) {
                if (classifier instanceof EClass) {
                    this.checkClassForContainmentReferences((EClass) classifier);
                }
            }
        }
    }

    /**
     * Registers a shutdown hook for the Neo4j instance so that it shuts down when the VM exits
     * (even if you "Ctrl-C" the running application).
     *
     * @param graph
     *            The Neo4j graph instance
     */
    private void registerShutdownHook(final GraphDatabaseService graph) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graph.shutdown();
            }
        });
    }

    public GraphDatabaseService getGraphDatabaseService() {
        return this.graphDatabaseService;
    }

    public List<EFactory> getEFactories() {
        return this.eFactories;
    }

    public File getGraphDirectory() {
        return this.graphDirectory;
    }

    /**
     * Deletes all nodes and relationships in the graph database.
     */
    public void clearResource() {
        ModelProviderSynchronizer.getLock(this);

        try (Transaction tx = this.getGraphDatabaseService().beginTx()) {
            this.getGraphDatabaseService().execute("MATCH (n) DETACH DELETE (n)");
            tx.success();
        }

        ModelProviderSynchronizer.releaseLock(this);
    }

    /**
     * Store the given element and all contained elements.
     *
     * @param rootElement
     *            root element to be stored
     */
    public void storeModelPartition(final EObject rootElement) {
        ModelProviderSynchronizer.getLock(this);

        try (Transaction tx = this.graphDatabaseService.beginTx()) {
            this.storeModelFacility.createNodes(rootElement);
            tx.success();
        }

        ModelProviderSynchronizer.releaseLock(this);
    }

    /**
     * Update an partition.
     *
     * @param clazz
     *            type of the object
     * @param object
     *            partition root
     * @param <T>
     *            type
     */
    public <T extends EObject> void updatePartition(final Class<T> clazz, final T object) {
        try (Transaction tx = this.graphDatabaseService.beginTx()) {
            ModelProviderSynchronizer.getLock(this);

            final Set<EObject> containedObjects = this.queryModelFacility.getAllContainmentsByObject(object,
                    new HashSet<>());

            this.updateModelFacility.updateNodes(containedObjects);

            tx.success();
        }

        ModelProviderSynchronizer.releaseLock(this);
    }

    /**
     * Delete an object by id and type.
     *
     * @param clazz
     *            type of the object
     * @param id
     *            unique id of the object
     * @param <T>
     *            type of the object
     */
    public <T> void deleteObjectByTypeAndId(final Class<T> clazz, final String id) {
        ModelProviderSynchronizer.getLock(this);

        final Label label = Label.label(clazz.getCanonicalName());

        try (Transaction tx = this.graphDatabaseService.beginTx()) {
            final Node node = this.graphDatabaseService.findNode(label, ModelResource.INTERNAL_ID, id);
            if (node != null) {
                this.deletePartitionRecursively(node);
            }
            tx.success();
        }

        ModelProviderSynchronizer.releaseLock(this);
    }

    /**
     * Starting with a given node this method recursively traverses down through all nodes
     * accessible via {@link EMFRelationshipType#CONTAINS} edges and then deletes them from bottom
     * to the top.
     *
     * @param node
     *            The node to start with
     */
    private void deletePartitionRecursively(final Node node) {
        for (final Relationship rel : node.getRelationships(Direction.OUTGOING, EMFRelationshipType.CONTAINS)) {
            this.deletePartitionRecursively(rel.getEndNode());
        }

        for (final Relationship rel : node.getRelationships()) {
            rel.delete();
        }

        node.delete();
    }

    /**
     * Delete an object by id and data type.
     *
     * @param clazz
     *            data type
     * @param id
     *            the id
     * @param forceDelete
     *            force fully deletion
     * @param <T>
     *            type definition
     */
    public <T> void deleteObjectByIdAndDatatype(final Class<T> clazz, final String id, final boolean forceDelete) {
        try (Transaction tx = this.graphDatabaseService.beginTx()) {
            ModelProviderSynchronizer.getLock(this);

            final Label label = Label.label(clazz.getCanonicalName());

            final Node node = this.graphDatabaseService.findNode(label, ModelResource.INTERNAL_ID, id);
            this.deleteModelFacility.deleteComponentAndDatatypeNodes(node, forceDelete);

            tx.success();
            ModelProviderSynchronizer.releaseLock(this);
        }
    }

    /**
     * Find an object by id and set an exclusive lock to the database.
     *
     * @param clazz
     *            type
     * @param id
     *            id of the object
     *
     * @param <T>
     *            type definition
     * @return returns the object identified by the id and locks the database
     */
    public <T extends EObject> T findAndLockObjectById(final Class<T> clazz, final String id) {
        ModelProviderSynchronizer.getLock(this);
        return this.findObjectByTypeAndId(clazz, id);
    }

    /**
     * Reads a specified component from the provider's graph without locking it for other providers.
     *
     * @param clazz
     *            Data type of component to be read
     * @param id
     *            Id of component to be read
     * @param <T>
     *            type definition
     * @return The read object
     */
    public <T extends EObject> T findObjectByTypeAndId(final Class<T> clazz, final String id) {
        final Label label = Label.label(clazz.getCanonicalName());

        try (Transaction tx = this.graphDatabaseService.beginTx()) {
            final Node node = this.graphDatabaseService.findNode(label, ModelResource.INTERNAL_ID, id);
            if (node != null) {
                final Set<Node> nodesForContainmentsAndDatatypes = this.queryModelFacility.getAllContainmentNodes(node,
                        new HashSet<Node>());
                return this.queryModelFacility.readNodes(node, nodesForContainmentsAndDatatypes,
                        new HashMap<Node, EObject>());
            }
            tx.success();
        }

        return null;
    }

    /**
     * Collect all objects with a specific property value and type.
     *
     * @param clazz
     *            the type of the object
     * @param name
     *            the property name
     * @param value
     *            the value
     * @param <T>
     *            type definition
     * @return list of objects with the given type and attribute value
     */
    public <T extends EObject> List<T> collectAllObjectsByTypeAndName(final Class<T> clazz, final String name,
            final String value) {
        ModelProviderSynchronizer.getLock(this);
        return this.findObjectsByTypeAndName(clazz, name, value);
    }

    /**
     * Reads components from the provider's graph by their entityName without locking it for other
     * providers. Note that not all components in the PCM models have an entityName and that an
     * entityName doesn't need to be unique. If multiple components of the specified type have the
     * specified name, the returned list contains all of them.
     *
     * @param clazz
     *            Data type of component(s) to be read
     * @param key
     *            key name
     * @param value
     *            value of the component(s) to be read
     * @param <T>
     *            type definition
     * @return List of the read component(s)
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> findObjectsByTypeAndName(final Class<T> clazz, final String key, final String value) {
        final Label label = Label.label(clazz.getCanonicalName());
        final List<T> nodes = new LinkedList<>();

        try (Transaction tx = this.graphDatabaseService.beginTx()) {
            final ResourceIterator<Node> nodesIter = this.graphDatabaseService.findNodes(label, key, value);

            while (nodesIter.hasNext()) {
                final Node node = nodesIter.next();
                final Set<Node> containmentsAndDatatypes = this.queryModelFacility.getAllContainmentNodes(node,
                        new HashSet<Node>());
                final EObject object = this.queryModelFacility.readNodes(node, containmentsAndDatatypes,
                        new HashMap<Node, EObject>());
                nodes.add((T) object);

            }
            tx.success();
        }

        return nodes;
    }

    public List<String> collectAllObjectIdsByType(final Class<?> clazz) {
        try (Transaction tx = this.graphDatabaseService.beginTx()) {
            final ResourceIterator<Node> nodes = this.graphDatabaseService
                    .findNodes(Label.label(clazz.getCanonicalName()));

            final List<String> ids = new LinkedList<>();

            while (nodes.hasNext()) {
                final Node n = nodes.next();
                ids.add(n.getProperty(ModelResource.INTERNAL_ID).toString());
            }

            tx.success();
            return ids;
        }
    }

    /**
     * Collect all objects of a given type.
     *
     * @param clazz
     *            the type of the objects
     *
     * @param <T>
     *            type of the objects
     *
     * @return returns a list of objects
     */
    public <T> List<T> collectAllObjectsByType(final Class<T> clazz) {
        final Label label = Label.label(clazz.getCanonicalName());
        final List<T> nodes = new LinkedList<>();

        try (Transaction tx = this.graphDatabaseService.beginTx()) {
            final ResourceIterator<Node> nodesIter = this.graphDatabaseService.findNodes(label);

            while (nodesIter.hasNext()) {
                final Node node = nodesIter.next();

                nodes.add(this.queryModelFacility.readContainedNodes(node));
            }
            tx.success();
        }

        return nodes;
    }

    /**
     * Reads the containing object of the specified object from the provider's graph.
     *
     * @param clazz
     *            Data type of the contained object
     * @param id
     *            Id of the contained object
     *
     * @param <T>
     *            type of the object
     *
     * @return The containing object
     */
    public <T> T readContainingObjectById(final Class<T> clazz, final String id) {
        ModelProviderSynchronizer.getLock(this);
        return this.readOnlyContainingObjectById(clazz, id);
    }

    /**
     * Reads the containing object of the specified object from the provider's graph without locking
     * it for other providers.
     *
     * @param clazz
     *            Data type of the contained object
     * @param id
     *            Id of the contained object
     *
     * @param <T>
     *            type of the object
     *
     * @return The containing object
     */
    public <T> T readOnlyContainingObjectById(final Class<T> clazz, final String id) {
        final Label label = Label.label(clazz.getCanonicalName());

        T object = null;

        try (Transaction tx = this.graphDatabaseService.beginTx()) {
            final Node node = this.graphDatabaseService.findNode(label, ModelResource.INTERNAL_ID, id);
            final Iterator<Relationship> inRels = node
                    .getRelationships(Direction.INCOMING, EMFRelationshipType.CONTAINS).iterator();

            if (inRels.hasNext()) {
                object = this.queryModelFacility.readContainedNodes(inRels.next().getStartNode());
            }

            tx.success();
        }

        return object;
    }

    /**
     * Reads components referencing to the specified component from the provider's graph.
     *
     * @param clazz
     *            Data type of the referenced component
     * @param id
     *            Id of the referenced component
     * @return The referencing components
     */
    public List<EObject> readReferencingObjectsById(final Class<?> clazz, final String id) {
        ModelProviderSynchronizer.getLock(this);
        return this.collectReferencingObjectsByTypeAndId(clazz, id);
    }

    /**
     * Collects objects with refer to an object specified by class and id from the provider's graph
     * without locking it for other providers.
     *
     * @param clazz
     *            Data type of the referenced object
     * @param id
     *            Id of the referenced object
     * @return List of all objects which reference the specified object
     */
    public List<EObject> collectReferencingObjectsByTypeAndId(final Class<?> clazz, final String id) {
        final List<EObject> referencingObjects = new LinkedList<>();
        final Label label = Label.label(clazz.getCanonicalName());

        try (Transaction tx = this.graphDatabaseService.beginTx()) {
            final Node node = this.graphDatabaseService.findNode(label, ModelResource.INTERNAL_ID, id);
            if (node != null) {
                for (final Relationship inRel : node.getRelationships(Direction.INCOMING,
                        EMFRelationshipType.REFERENCES)) {
                    referencingObjects.add(this.queryModelFacility.readContainedNodes(inRel.getStartNode()));
                }
            }

            tx.success();
        }

        return referencingObjects;
    }

    /**
     * Read the root node of a model and lock the resource.
     *
     * @param clazz
     *            type of the root class
     *
     * @param <T>
     *            type definition
     * @return returns the object
     */
    public <T extends EObject> T getAndLockModelRootNode(final Class<T> clazz) {
        ModelProviderSynchronizer.getLock(this);
        return this.getModelRootNode(clazz);
    }

    /**
     * Reads the models (partition) root node and all contained classes without locking the graph
     * for other providers.
     *
     * @param clazz
     *            Data type of the root object
     * @param <T>
     *            type definition
     * @return The read object
     */
    public <T extends EObject> T getModelRootNode(final Class<T> clazz) {
        T object = null;
        try (Transaction tx = this.graphDatabaseService.beginTx()) {

            final ResourceIterator<Node> nodes = this.graphDatabaseService
                    .findNodes(Label.label(clazz.getCanonicalName()));

            if (nodes.hasNext()) {
                final Node node = nodes.next();

                final Set<Node> containmentNodes = this.queryModelFacility.getAllContainmentNodes(node,
                        new HashSet<Node>());
                object = this.queryModelFacility.readNodes(node, containmentNodes, new HashMap<Node, EObject>());
            }

            tx.success();
        }

        return object;
    }

    private void checkClassForContainmentReferences(final EClass clazz) {
        if (!this.classes.contains(clazz)) {
            this.classes.add(clazz);
            for (final EReference reference : clazz.getEAllReferences()) {
                final EClass type = reference.getEReferenceType();
                this.addFactoryToCollection(type);
                if (reference.isContainment()) {
                    this.checkClassForContainmentReferences(type);
                }
            }
        }
    }

    private void addFactoryToCollection(final EClass clazz) {
        final EFactory subFactory = clazz.getEPackage().getEFactoryInstance();
        if (!this.eFactories.contains(subFactory)) {
            this.eFactories.add(subFactory);
        }
    }

}
