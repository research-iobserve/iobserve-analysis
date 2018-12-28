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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.iobserve.model.persistence.DBException;
import org.iobserve.model.persistence.IModelResource;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotInTransactionException;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.TransactionFailureException;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Reiner Jung
 *
 * @param <R>
 *            type of the root class
 */
public class Neo4JModelResource<R extends EObject> implements IModelResource<R> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Neo4JModelResource.class);

    private final File graphDirectory;
    private final GraphDatabaseService graphDatabaseService;
    private final Set<EFactory> factories = new HashSet<>();

    private final StoreModelFacility<R> storeModelFacility;
    private final UpdateModelFacility<R> updateModelFacility;
    private final QueryModelFacility<R> queryModelFacility;
    private final DeleteModelFacility<R> deleteModelFacility;

    /**
     * Creates a new {@link GraphDatabaseService} at the given location.
     *
     * @param ePackage
     *            ePackage for the particular metamodel
     * @param graphDirectory
     *            Directory where the graph database shall be located
     */
    public Neo4JModelResource(final EPackage ePackage, final File graphDirectory) {
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

        this.storeModelFacility = new StoreModelFacility<>(this, this.graphDatabaseService);
        this.queryModelFacility = new QueryModelFacility<>(this, this.graphDatabaseService, this.factories);
        this.updateModelFacility = new UpdateModelFacility<>(this, this.graphDatabaseService);
        this.deleteModelFacility = new DeleteModelFacility<>(this, this.graphDatabaseService);

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
    @Override
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
     * @throws DBException
     *             on db errors
     */
    @Override
    public void storeModelPartition(final R rootElement) throws DBException {
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
     * @throws DBException
     *             on db errors
     */
    @Override
    public <T extends EObject> void updatePartition(final T object) throws NodeLookupException, DBException {
        ModelProviderSynchronizer.getLock(this);

        try (Transaction tx = this.graphDatabaseService.beginTx()) {
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
     * @throws DBException
     *             on db errors
     */
    @Override
    public <T> void deleteObject(final EObject object) throws DBException {
        ModelProviderSynchronizer.getLock(this);

        try (Transaction tx = this.graphDatabaseService.beginTx()) {
            this.deleteModelFacility.deleteObjectRecursively(object);
            tx.success();
        }

        ModelProviderSynchronizer.releaseLock(this);
    }

    /**
     * Delete an object by id and data type.
     *
     * @param clazz
     *            data type
     * @param eClass
     *            eClass corresponding to clazz
     * @param id
     *            the id
     * @param <T>
     *            type definition
     */
    private <T> void deleteObjectByIdAndDatatype(final Class<T> clazz, final EClass eClass, final String id) {
        ModelProviderSynchronizer.getLock(this);

        try (Transaction tx = this.graphDatabaseService.beginTx()) {
            final Label label = Label.label(ModelGraphFactory.fqnClassName(eClass));

            final Node node = this.graphDatabaseService.findNode(label, ModelGraphFactory.EMF_INTERNAL_ID, id);
            this.deleteModelFacility.deleteObjectNodesRecursively(node);

            tx.success();
        }

        ModelProviderSynchronizer.releaseLock(this);
    }

    /**
     * Find an object by id and set an exclusive lock to the database.
     *
     * @param clazz
     *            type
     * @param eClass
     *            eClass corresponding to clazz
     * @param id
     *            id of the object
     *
     * @param <T>
     *            type definition
     * @return returns the object identified by the id and locks the database
     * @throws DBException
     *             when a relationship is not backed by a EReference
     */
    @Override
    public <T extends EObject> T findAndLockObjectById(final Class<T> clazz, final EClass eClass, final String id)
            throws DBException {
        ModelProviderSynchronizer.getLock(this);
        return this.findObjectByTypeAndId(clazz, eClass, id);
    }

    /**
     * Reads a specified component from the provider's graph without locking it for other providers.
     *
     * @param clazz
     *            Data type of component to be read
     * @param eClass
     *            eClass corresponding to clazz
     * @param id
     *            Id of component to be read
     * @param <T>
     *            type definition
     * @return The read object
     * @throws DBException
     *             when a relationship is not backed by a EReference
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends EObject> T findObjectByTypeAndId(final Class<T> clazz, final EClass eClass, final String id)
            throws DBException {
        final Label label = Label.label(ModelGraphFactory.fqnClassName(eClass));

        try (Transaction tx = this.graphDatabaseService.beginTx()) {
            final Node node = this.graphDatabaseService.findNode(label, ModelGraphFactory.EMF_INTERNAL_ID, id);
            if (node != null) {

                final Map<Node, EObject> nodeObjectMap = new HashMap<>();

                final EObject object = this.queryModelFacility.readContainedNodesRecursively(node, nodeObjectMap);
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
     * @param eClass
     *            eClass corresponding to clazz
     * @param name
     *            the property name
     * @param value
     *            the value
     * @param <T>
     *            type definition
     * @return list of objects with the given type and attribute value
     * @throws DBException
     *             when a relationship is not backed by a EReference
     */
    private <T extends EObject> List<T> collectAllObjectsByTypeAndName(final Class<T> clazz, final EClass eClass,
            final String name, final String value) throws DBException {
        ModelProviderSynchronizer.getLock(this);
        return this.findObjectsByTypeAndProperty(clazz, eClass, name, value);
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
     * @throws DBException
     *             when a relationship is not backed by a EReference
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends EObject> List<T> findObjectsByTypeAndProperty(final Class<T> clazz, final EClass eClass,
            final String key, final String value) throws DBException {
        final Label label = Label.label(ModelGraphFactory.fqnClassName(eClass));
        final List<T> nodes = new LinkedList<>();

        try (Transaction tx = this.graphDatabaseService.beginTx()) {
            final ResourceIterator<Node> nodesIter = this.graphDatabaseService.findNodes(label, key, value);

            while (nodesIter.hasNext()) {
                final Node node = nodesIter.next();
                Neo4JModelResource.LOGGER.debug("Node id {} type {}", node.getId(),
                        node.getLabels().iterator().next().name());
                for (final Entry<String, Object> v : node.getAllProperties().entrySet()) {
                    Neo4JModelResource.LOGGER.debug("property {}={}", v.getKey(), v.getValue());
                }

                final Map<Node, EObject> nodeObjectMap = new HashMap<>();

                final EObject object = this.queryModelFacility.readContainedNodesRecursively(node, nodeObjectMap);
                this.queryModelFacility.resolveReferences(nodeObjectMap);

                nodes.add((T) object);

            }
            tx.success();
        }

        return nodes;
    }

    /**
     * Collect all objects of a given type.
     *
     * @param clazz
     *            the type of the objects
     * @param eClass
     *            eClass corresponding to clazz
     *
     * @param <T>
     *            type of the objects
     *
     * @return returns a list of objects
     *
     * @throws DBException
     *             when the search for a matching reference to a relationship
     */
    @Override
    public <T extends EObject> List<T> collectAllObjectsByType(final Class<T> clazz, final EClass eClass)
            throws DBException {
        final Label label = Label.label(ModelGraphFactory.fqnClassName(eClass));
        final List<T> nodes = new LinkedList<>();

        try (Transaction tx = this.graphDatabaseService.beginTx()) {
            final ResourceIterator<Node> nodesIter = this.graphDatabaseService.findNodes(label);

            while (nodesIter.hasNext()) {
                final Node node = nodesIter.next();

                nodes.add(this.queryModelFacility.readContainedNodes(node));
            }
            tx.success();
        } catch (final NotInTransactionException e) {
            Neo4JModelResource.LOGGER.debug("Access failed", e);
        }

        return nodes;
    }

    /**
     * Collects objects with refer to an object specified by class and id from the provider's graph
     * without locking it for other providers.
     *
     * @param clazz
     *            Data type of the referenced object
     * @param eClass
     *            eClass corresponding to clazz
     * @param property
     *            Id of the referenced object
     * @return List of all objects which reference the specified object
     * @throws DBException
     *             when a relationship is not backed by a EReference
     */
    @Override
    public List<EObject> collectReferencingObjectsByTypeAndProperty(final Class<?> clazz, final EClass eClass,
            final String property) throws DBException {
        final List<EObject> referencingObjects = new LinkedList<>();

        try (Transaction tx = this.graphDatabaseService.beginTx()) {
            final Label label = Label.label(ModelGraphFactory.fqnClassName(eClass));

            final Node node = this.graphDatabaseService.findNode(label, ModelGraphFactory.EMF_INTERNAL_ID, property);
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
     * @param eClass
     *            eclass corresponding to clazz
     * @return returns the object
     * @throws DBException
     *             when a relationship is not backed by a EReference
     */
    @Override
    public R getAndLockModelRootNode(final Class<R> clazz, final EClass eClass) throws DBException {
        ModelProviderSynchronizer.getLock(this);
        return this.getModelRootNode(clazz, eClass);
    }

    /**
     * Reads the models (partition) root node and all contained classes without locking the graph
     * for other providers.
     *
     * @param clazz
     *            Data type of the root object
     * @param eClass
     *            eclass type corresponding to clazz
     * @return The read object
     * @throws DBException
     */
    @Override
    public R getModelRootNode(final Class<R> clazz, final EClass eClass) throws DBException {
        R object = null;
        try (Transaction tx = this.graphDatabaseService.beginTx()) {

            final ResourceIterator<Node> nodes = this.graphDatabaseService
                    .findNodes(Label.label(ModelGraphFactory.fqnClassName(eClass)));

            if (nodes.hasNext()) {
                final Node node = nodes.next();

                final Map<Node, EObject> nodeObjectMap = new HashMap<>();

                object = this.queryModelFacility.readContainedNodesRecursively(node, nodeObjectMap);
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
     * Check if the given type is supported by this resources factories.
     *
     * @param <T>
     *            class of the proxy object
     *
     * @param proxyObject
     *            the proxy object
     * @return returns true on success
     */
    @Override
    public <T extends EObject> boolean isTypeManagedByResource(final T proxyObject) {
        for (final EFactory factory : this.factories) {
            try {
                final EObject instance = factory.create(proxyObject.eClass());
                if (instance != null) {
                    return true;
                }
            } catch (final IllegalArgumentException e) {

            }
        }

        return false;
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
     * @return returns the resolved objects
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends EObject> T resolve(final T proxyObject) throws InvocationException, DBException {
        if (proxyObject == null) {
            throw new InvocationException("Object reference is null, cannot resolve.");
        }
        if (proxyObject.eIsProxy()) {
            try (Transaction tx = this.graphDatabaseService.beginTx()) {
                final Node realNode = ModelGraphFactory.findNode(this.graphDatabaseService, proxyObject);

                if (realNode == null) {
                    throw new DBException(String.format("Object '%s' has no representing node.",
                            ModelGraphFactory.getIdentification(proxyObject)));
                }

                final Map<Node, EObject> nodesToCreatedObjects = new HashMap<>();
                this.queryModelFacility.readContainedNodesRecursively(realNode, nodesToCreatedObjects);
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
            } catch (final TransactionFailureException ex) {
                Neo4JModelResource.LOGGER.error("Cannot create transaction to resolve data.");
            }
            return proxyObject;
        } else {
            return proxyObject;
        }
    }

}
