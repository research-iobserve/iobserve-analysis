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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.BasicEObjectImpl;
import org.iobserve.model.test.data.DebugHelper;
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

    private final File graphDirectory;
    private final GraphDatabaseService graphDatabaseService;
    private final Set<EFactory> factories = new HashSet<>();

    private final Map<EObject, Node> objectNodeMap = new ConcurrentHashMap<>();
    private final StoreModelFacility storeModelFacility;
    private final UpdateModelFacility updateModelFacility;
    private final QueryModelFacility queryModelFacility;
    private final DeleteModelFacility deleteModelFacility;

    private long objectId;

    /**
     * Creates a new {@link GraphDatabaseService} at the given location.
     *
     * @param prefix
     *            java code package prefix
     * @param ePackage
     *            ePackage for the particular metamodel
     * @param graphDirectory
     *            Directory where the graph database shall be located
     */
    public ModelResource(final EPackage ePackage, final File graphDirectory) {
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

        this.storeModelFacility = new StoreModelFacility(this, this.graphDatabaseService, this.objectNodeMap);
        this.queryModelFacility = new QueryModelFacility(this, this.graphDatabaseService, this.objectNodeMap,
                this.factories);
        this.updateModelFacility = new UpdateModelFacility(this, this.graphDatabaseService, this.objectNodeMap);
        this.deleteModelFacility = new DeleteModelFacility(this, this.graphDatabaseService, this.objectNodeMap);

        this.registerShutdownHook(this.graphDatabaseService);
        if (ePackage != null) {
            this.checkPackage(ePackage);
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
            this.storeModelFacility.storeAllNodesAndReferences(rootElement);
            tx.success();
        }

        ModelProviderSynchronizer.releaseLock(this);
    }

    /**
     * Update an partition.
     *
     * @param object
     *            partition root
     * @param <T>
     *            type
     * @throws NodeLookupException
     *             on node lookup errors
     */
    public <T extends EObject> void updatePartition(final T object) throws NodeLookupException {

        try (Transaction tx = this.graphDatabaseService.beginTx()) {
            DebugHelper.printMap("ObjectNodeMap", this.objectNodeMap);

            ModelProviderSynchronizer.getLock(this);

            DebugHelper.printNodeModel(this.objectNodeMap, object);

            this.updateModelFacility.updatePartition(object);

            tx.success();
        }

        ModelProviderSynchronizer.releaseLock(this);
    }

    /**
     * Delete an object by id and type.
     *
     * @param object
     *            the object
     * @param <T>
     *            type of the object
     */
    public <T> void deleteObject(final EObject object) {
        ModelProviderSynchronizer.getLock(this);

        try (Transaction tx = this.graphDatabaseService.beginTx()) {
            this.deleteModelFacility.deleteObject(object);
            tx.success();
        }

        ModelProviderSynchronizer.releaseLock(this);
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
    public <T> void deleteObjectByIdAndDatatype(final Class<T> clazz, final EClass eClass, final Long id,
            final boolean forceDelete) {
        try (Transaction tx = this.graphDatabaseService.beginTx()) {
            ModelProviderSynchronizer.getLock(this);

            final Label label = Label.label(ModelGraphFactory.fqnClassName(eClass));

            final Node node = this.graphDatabaseService.findNode(label, ModelGraphFactory.INTERNAL_ID, id);
            this.deleteModelFacility.deleteObjectNodesRecursively(node, forceDelete);

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
    public <T extends EObject> T findAndLockObjectById(final Class<T> clazz, final EClass eClass, final Long id) {
        ModelProviderSynchronizer.getLock(this);
        return this.findObjectByTypeAndId(clazz, eClass, id);
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
    @SuppressWarnings("unchecked")
    public <T extends EObject> T findObjectByTypeAndId(final Class<T> clazz, final EClass eClass, final Long id) {
        final Label label = Label.label(ModelGraphFactory.fqnClassName(eClass));

        try (Transaction tx = this.graphDatabaseService.beginTx()) {
            final Node node = this.graphDatabaseService.findNode(label, ModelGraphFactory.INTERNAL_ID, id);
            if (node != null) {

                final Map<Node, EObject> nodeObjectMap = new HashMap<>();

                final EObject object = this.queryModelFacility.readNodes(node, nodeObjectMap,
                        EMFRelationshipType.REFERENCES, EMFRelationshipType.CONTAINS);
                this.queryModelFacility.resolveReferences(nodeObjectMap);

                return (T) object;
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
    public <T extends EObject> List<T> collectAllObjectsByTypeAndName(final Class<T> clazz, final EClass eClass,
            final String name, final String value) {
        ModelProviderSynchronizer.getLock(this);
        return this.findObjectsByTypeAndName(clazz, eClass, name, value);
    }

    /**
     * Reads components from the provider's graph by their entityName without locking it for other
     * providers. Note that not all components in the PCM models have an entityName and that an
     * entityName doesn't need to be unique. If multiple components of the specified type have the
     * specified name, the returned list contains all of them.
     *
     * @param clazz
     *            Data type of component(s) to be read
     * @param eClass
     *            EMF class
     * @param key
     *            key name
     * @param value
     *            value of the component(s) to be read
     * @param <T>
     *            type definition
     * @return List of the read component(s)
     */
    @SuppressWarnings("unchecked")
    public <T extends EObject> List<T> findObjectsByTypeAndName(final Class<T> clazz, final EClass eClass,
            final String key, final String value) {
        final Label label = Label.label(ModelGraphFactory.fqnClassName(eClass));
        final List<T> nodes = new LinkedList<>();

        try (Transaction tx = this.graphDatabaseService.beginTx()) {
            final ResourceIterator<Node> nodesIter = this.graphDatabaseService.findNodes(label, key, value);

            while (nodesIter.hasNext()) {
                final Node node = nodesIter.next();

                final Map<Node, EObject> nodeObjectMap = new HashMap<>();

                final EObject object = this.queryModelFacility.readNodes(node, nodeObjectMap,
                        EMFRelationshipType.REFERENCES, EMFRelationshipType.CONTAINS);
                this.queryModelFacility.resolveReferences(nodeObjectMap);

                nodes.add((T) object);

            }
            tx.success();
        }

        return nodes;
    }

    /**
     * Collect all object ids of objects of the given type.
     *
     * @param clazz
     *            the objects' type
     * @return returns a list.
     */
    public List<Long> collectAllObjectIdsByType(final Class<?> clazz, final EClass eClass) {
        try (Transaction tx = this.graphDatabaseService.beginTx()) {
            final ResourceIterator<Node> nodes = this.graphDatabaseService
                    .findNodes(Label.label(ModelGraphFactory.fqnClassName(eClass)));

            final List<Long> ids = new LinkedList<>();

            while (nodes.hasNext()) {
                final Node n = nodes.next();
                ids.add((Long) n.getProperty(ModelGraphFactory.INTERNAL_ID));
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
    public <T> List<T> collectAllObjectsByType(final Class<T> clazz, final EClass eClass) {
        final Label label = Label.label(ModelGraphFactory.fqnClassName(eClass));
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
    public <T> T findAndLockContainingObjectById(final Class<T> clazz, final EClass eClass, final Long id) {
        ModelProviderSynchronizer.getLock(this);
        return this.findContainingObjectById(clazz, eClass, id);
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
    public <T> T findContainingObjectById(final Class<T> clazz, final EClass eClass, final Long id) {
        final Label label = Label.label(ModelGraphFactory.fqnClassName(eClass));

        T object = null;

        try (Transaction tx = this.graphDatabaseService.beginTx()) {
            final Node node = this.graphDatabaseService.findNode(label, ModelGraphFactory.INTERNAL_ID, id);
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
    public List<EObject> readReferencingObjectsById(final Class<?> clazz, final EClass eClass, final Long id) {
        ModelProviderSynchronizer.getLock(this);
        return this.collectReferencingObjectsByTypeAndId(clazz, eClass, id);
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
    public List<EObject> collectReferencingObjectsByTypeAndId(final Class<?> clazz, final EClass eClass,
            final Long id) {
        final List<EObject> referencingObjects = new LinkedList<>();

        try (Transaction tx = this.graphDatabaseService.beginTx()) {
            final Label label = Label.label(ModelGraphFactory.fqnClassName(eClass));

            final Node node = this.graphDatabaseService.findNode(label, ModelGraphFactory.INTERNAL_ID, id);
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
    public <T extends EObject> T getAndLockModelRootNode(final Class<T> clazz, final EClass eClass) {
        ModelProviderSynchronizer.getLock(this);
        return this.getModelRootNode(clazz, eClass);
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
    public <T extends EObject> T getModelRootNode(final Class<T> clazz, final EClass eClass) {
        T object = null;
        try (Transaction tx = this.graphDatabaseService.beginTx()) {

            final ResourceIterator<Node> nodes = this.graphDatabaseService
                    .findNodes(Label.label(ModelGraphFactory.fqnClassName(eClass)));

            if (nodes.hasNext()) {
                final Node node = nodes.next();

                final Map<Node, EObject> nodeObjectMap = new HashMap<>();

                object = this.queryModelFacility.readNodes(node, nodeObjectMap, EMFRelationshipType.REFERENCES,
                        EMFRelationshipType.CONTAINS);
                this.queryModelFacility.resolveReferences(nodeObjectMap);
            }

            tx.success();
        }

        return object;
    }

    private void checkPackage(final EPackage ePackage) {
        if (!this.factories.contains(ePackage.getEFactoryInstance())) {
            this.factories.add(ePackage.getEFactoryInstance());
            for (final EClassifier classifier : ePackage.getEClassifiers()) {
                if (classifier instanceof EClass) {
                    final EClass clazz = (EClass) classifier;
                    for (final EReference reference : clazz.getEAllReferences()) {
                        this.checkPackage(reference.getEReferenceType().getEPackage());
                    }
                }
            }
        }
    }

    /**
     * Id generator to be used in all sub-parts of the model resource.
     *
     * @return the next valid id
     */
    long getNextId() { // NOPMD should be package private
        return this.objectId++;
    }

    /**
     * Resolve proxy object.
     *
     * @param proxyObject
     *            resolve proxy object
     * @param <T>
     *            type
     * @throws InvocationException
     *             when called with a null object
     * @throws DBException
     *             on database issues
     *
     * @return returns the resulved objects
     */
    @SuppressWarnings("unchecked")
    public <T extends EObject> T resolve(final T proxyObject) throws InvocationException, DBException {
        if (proxyObject == null) {
            throw new InvocationException("Object reference is null, cannot resolve.");
        }
        if (proxyObject.eIsProxy()) {
            try (Transaction tx = this.graphDatabaseService.beginTx()) {
                final Node realNode = this.queryModelFacility
                        .getNodeByUri(((BasicEObjectImpl) proxyObject).eProxyURI());

                final Map<Node, EObject> nodesToCreatedObjects = new HashMap<>();
                this.queryModelFacility.readNodes(realNode, nodesToCreatedObjects, EMFRelationshipType.REFERENCES,
                        EMFRelationshipType.CONTAINS);
                this.queryModelFacility.resolveReferences(nodesToCreatedObjects);
                final EObject realObject = nodesToCreatedObjects.get(realNode);

                for (final EAttribute attribute : proxyObject.eClass().getEAllAttributes()) {
                    proxyObject.eSet(attribute, realObject.eGet(attribute));
                }

                for (final EReference reference : proxyObject.eClass().getEAllReferences()) {
                    if (reference.isMany()) {
                        final EList<EObject> manyReference = (EList<EObject>) realObject.eGet(reference);
                        ((EList<EObject>) proxyObject.eGet(reference)).addAll(manyReference);
                    } else {
                        final EObject singleReference = (EObject) realObject.eGet(reference);
                        ((EObject) proxyObject).eSet(reference, singleReference);
                    }
                }

                tx.success();
            }
            return proxyObject;
        } else {
            return proxyObject;
        }
    }

    /**
     * Get the internal database id for the given object.
     *
     * @param object
     *            the object
     * @return returns the id or -1 if no node exists in the database
     */
    public long getInternalId(final EObject object) {
        try (Transaction tx = this.graphDatabaseService.beginTx()) {
            final Node node = this.objectNodeMap.get(object);
            final long result;
            if (node != null) {
                result = (long) node.getProperty(ModelGraphFactory.INTERNAL_ID);
            } else {
                result = -1;
            }
            tx.success();
            return result;
        }
    }

}
