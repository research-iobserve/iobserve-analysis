/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.model.provider.neo4j;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.BasicEObjectImpl;
import org.iobserve.model.privacy.PrivacyModel;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.palladiosimulator.pcm.repository.PrimitiveDataType;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides methods to access the pcm models stored in a neo4j graph database. Enables partial
 * access, to access only parts of a pcm component model, i.e. a submodel.
 *
 * @author Lars Bluemke
 *
 * @param <T>
 *            Type of the model's or submodel's root component
 * @since 0.0.2
 */
public class ModelProvider<T extends EObject> implements IModelProvider<T> {

    /** TODO these should be moved elsewhere. */
    public static final String PCM_ENTITY_NAME = "entityName";
    public static final String PCM_ID = "id";
    public static final String IMPLEMENTATION_ID = "implementationId";

    protected static final String EMF_URI = "emfUri";
    protected static final String REF_POS = "refPos";

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelProvider.class);

    private static final String REF_NAME = "refName";
    private static final String TYPE = "type";

    private static final String ACCESSIBLE = "accessible";
    private static final String DELETE = "delete";
    private static final String VISITED = "visited";

    private final ModelGraph graph;
    private final String nameLabel;
    private final String idLabel;

    /**
     * Creates a new model provider.
     *
     * @param graph
     *            The neo4j graph database
     * @param nameLabel
     *            label used for the name property, e.g., entityName in PCM (can be null if no names
     *            are present)
     * @param idLabel
     *            label used for tne id property, e.g., id in PCM (can be null if no ids are
     *            present)
     */
    public ModelProvider(final ModelGraph graph, final String nameLabel, final String idLabel) {
        this.graph = graph;
        this.nameLabel = nameLabel;
        this.idLabel = idLabel;
    }

    /**
     * Deletes all nodes and relationships in the graph database.
     */
    public void clearGraph() {
        ModelProviderSynchronizer.getLock(this);

        try (Transaction tx = this.graph.getGraphDatabaseService().beginTx()) {
            this.graph.getGraphDatabaseService().execute("MATCH (n) DETACH DELETE (n)");
            tx.success();
        }

        ModelProviderSynchronizer.releaseLock(this);
    }

    /**
     * Clones and returns a new version from the current newest version of the model graph. If there
     * is none yet an empty graph is returned.
     *
     * @param factory
     *            metamodel factory class
     *
     * @return The cloned graph
     */
    public ModelGraph cloneNewGraphVersion(final EFactory factory) {
        final File baseDirectory = this.graph.getGraphDirectory().getParentFile().getParentFile();
        final ModelGraphLoader graphLoader = new ModelGraphLoader(baseDirectory);

        return graphLoader.cloneNewModelGraphVersion(factory);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.iobserve.model.provider.neo4j#createComponent(T)
     */
    @Override
    public void storeModelPartition(final T rootElement) {
        ModelProviderSynchronizer.getLock(this);

        try (Transaction tx = this.graph.getGraphDatabaseService().beginTx()) {
            final Set<EObject> containmentsAndDatatypes = this.getAllContainmentsAndDatatypes(rootElement,
                    new HashSet<>());

            this.createNodes(rootElement, containmentsAndDatatypes, new HashMap<>());
            tx.success();
        }

        ModelProviderSynchronizer.releaseLock(this);
    }

    /**
     * Helper method for writing: Recursively returns all containments and data types of a given
     * component.
     *
     * @param component
     *            The component to start with
     * @param containmentsAndDatatypes
     *            Initially empty set of all containments and data types
     * @return The passed containmentsAndDatatypes set now filled with containments and data types
     */
    private Set<EObject> getAllContainmentsAndDatatypes(final EObject component,
            final Set<EObject> containmentsAndDatatypes) {
        if (!containmentsAndDatatypes.contains(component)) {
            containmentsAndDatatypes.add(component);

            for (final EReference ref : component.eClass().getEAllReferences()) {
                final Object refObject = component.eGet(ref);

                if (refObject instanceof EList<?>) {
                    for (final Object o : (EList<?>) component.eGet(ref)) {
                        if (ref.isContainment() || ModelProviderUtil.isDatatype(ref, o)) {
                            this.getAllContainmentsAndDatatypes((EObject) o, containmentsAndDatatypes);
                        }
                    }

                } else {
                    if (refObject != null && (ref.isContainment() || ModelProviderUtil.isDatatype(ref, refObject))) {
                        this.getAllContainmentsAndDatatypes((EObject) refObject, containmentsAndDatatypes);
                    }
                }
            }
        }

        return containmentsAndDatatypes;
    }

    /**
     * Helper method for writing: Writes the given component into the provider's {@link #graph}
     * recursively. Calls to this method have to be performed from inside a {@link Transaction}.
     *
     * @param storeableObject
     *            Component to save
     * @param containmentsAndDatatypes
     *            Set of EObjects contained in the root preferably created by
     *            {@link #getAllContainmentsAndDatatypes(EObject, HashSet)}
     * @param objectsToCreatedNodes
     *            Initially empty map of EObjects to already created correspondent nodes to make
     *            sure nodes are written just once
     * @return Root node of the component's graph
     */
    private Node createNodes(final EObject storeableObject, final Set<EObject> containmentsAndDatatypes,
            final Map<EObject, Node> objectsToCreatedNodes) {
        // Create a label representing the type of the component
        final Label label = Label.label(storeableObject.eClass().getInstanceTypeName());
        Node node = null;

        // TODO unnecessary complicated
        // Check if node has already been created
        final EAttribute idAttr = storeableObject.eClass().getEIDAttribute();
        if (idAttr != null) {
            node = this.graph.getGraphDatabaseService().findNode(label, this.idLabel, storeableObject.eGet(idAttr));
        } else if (storeableObject instanceof PrimitiveDataType) {
            node = this.graph.getGraphDatabaseService().findNode(label, ModelProvider.TYPE,
                    ((PrimitiveDataType) storeableObject).getType().name());
        } else if (storeableObject instanceof UsageModel || storeableObject instanceof ResourceEnvironment) {
            final ResourceIterator<Node> nodes = this.graph.getGraphDatabaseService()
                    .findNodes(Label.label(storeableObject.eClass().getName()));
            if (nodes.hasNext()) {
                node = nodes.next();
            }
        } else {
            // For components that cannot be found in the graph (e.g. due to missing id) but have
            // been created in this recursion
            node = objectsToCreatedNodes.get(storeableObject);
        }

        // If there is no node yet, create one
        if (node == null) {
            node = this.createNewNodes(label, storeableObject, containmentsAndDatatypes, objectsToCreatedNodes);
        }

        return node;
    }

    /**
     * Create a new node.
     *
     * @param typeName
     *            type name label for the new node
     * @param storeableObject
     *            the corresponding object
     * @param containmentsAndDatatypes
     *            set of containments
     * @param objectsToCreatedNodes
     *            map of objects and nodes used for lookup
     * @return returns the created node
     */
    private Node createNewNodes(final Label typeName, final EObject storeableObject,
            final Set<EObject> containmentsAndDatatypes, final Map<EObject, Node> objectsToCreatedNodes) {
        /** create node. */
        final Node node = this.graph.getGraphDatabaseService().createNode(typeName);
        objectsToCreatedNodes.put(storeableObject, node);

        // Create a URI to enable proxy resolving
        final URI uri = ((BasicEObjectImpl) storeableObject).eProxyURI();
        if (uri == null) {
            node.setProperty(ModelProvider.EMF_URI, ModelProviderUtil.getUriString(storeableObject));
        } else {
            node.setProperty(ModelProvider.EMF_URI, uri.toString());
        }

        /** attributes. */
        this.storeAttributes(node, storeableObject);

        /** references. */
        // Outgoing references are only stored for containments and data types of the root,
        // otherwise we just store the blank node as a proxy
        if (containmentsAndDatatypes.contains(storeableObject)) {
            this.storeReferences(node, storeableObject, containmentsAndDatatypes, objectsToCreatedNodes);
        }

        return node;
    }

    /**
     * Add all object attributes to the node as properties.
     *
     * @param node
     *            the node
     * @param storeableObject
     *            the object having attributes
     */
    private void storeAttributes(final Node node, final EObject storeableObject) {
        for (final EAttribute attr : storeableObject.eClass().getEAllAttributes()) {
            final Object value = storeableObject.eGet(attr);
            if (value != null) {
                node.setProperty(attr.getName(), value.toString());
            }
        }
    }

    /**
     * Store references in the database as relationships.
     *
     * @param node
     *            the source node
     * @param storeableObject
     *            the related object
     * @param containmentsAndDatatypes
     *            set of containments
     * @param objectsToCreatedNodes
     *            map of objects and nodes used for lookup
     */
    private void storeReferences(final Node node, final EObject storeableObject,
            final Set<EObject> containmentsAndDatatypes, final Map<EObject, Node> objectsToCreatedNodes) {
        for (final EReference reference : storeableObject.eClass().getEAllReferences()) {
            final Object referenceInstance = storeableObject.eGet(reference);

            if (referenceInstance != null) {
                /** 0..* refs are represented as a list and 1 refs are represented directly. */
                if (referenceInstance instanceof EList<?>) {
                    final EList<?> listReferenceInstance = (EList<?>) referenceInstance;
                    for (int i = 0; i < listReferenceInstance.size(); i++) {
                        final Object targetObject = listReferenceInstance.get(i);
                        final Node targetObjectNode = this.createNodes((EObject) targetObject, containmentsAndDatatypes,
                                objectsToCreatedNodes);
                        final Relationship relationship = node.createRelationshipTo(targetObjectNode,
                                ModelProviderUtil.getRelationshipType(reference, targetObject));
                        relationship.setProperty(ModelProvider.REF_NAME, reference.getName());
                        relationship.setProperty(ModelProvider.REF_POS, i);
                    }
                } else {
                    final Node targetObjectNode = this.createNodes((EObject) referenceInstance,
                            containmentsAndDatatypes, objectsToCreatedNodes);
                    final Relationship relationship = node.createRelationshipTo(targetObjectNode,
                            ModelProviderUtil.getRelationshipType(reference, referenceInstance));
                    relationship.setProperty(ModelProvider.REF_NAME, reference.getName());
                    relationship.setProperty(ModelProvider.REF_POS, 0);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.iobserve.model.provider.neo4j#readComponentById(java.lang.Class, java.lang.String)
     */
    @Override
    public T findAndLockObjectById(final Class<T> clazz, final String id) {
        ModelProviderSynchronizer.getLock(this);
        return this.getObjectByTypeAndId(clazz, id);
    }

    /**
     * Reads a specified component from the provider's graph without locking it for other providers.
     *
     * @param clazz
     *            Data type of component to be read
     * @param id
     *            Id of component to be read
     * @return The read component
     */
    @Override
    @SuppressWarnings("unchecked")
    public T getObjectByTypeAndId(final Class<T> clazz, final String id) {
        final Label label = Label.label(clazz.getCanonicalName());

        try (Transaction tx = this.graph.getGraphDatabaseService().beginTx()) {
            final Node node = this.graph.getGraphDatabaseService().findNode(label, this.idLabel, id);
            if (node != null) {
                final Set<Node> containmentsAndDatatypes = this.getAllContainmentsAndDatatypes(node,
                        new HashSet<Node>());
                return (T) this.readNodes(node, containmentsAndDatatypes, new HashMap<Node, EObject>());
            }
            tx.success();
        }

        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.iobserve.model.provider.neo4j#readComponentByName(java.lang.Class, java.lang.String)
     */
    @Override
    public List<T> collectAllObjectsByTypeAndName(final Class<T> clazz, final String entityName) {
        ModelProviderSynchronizer.getLock(this);
        return this.getObjectsByTypeAndName(clazz, entityName);
    }

    /**
     * Reads components from the provider's graph by their entityName without locking it for other
     * providers. Note that not all components in the PCM models have an entityName and that an
     * entityName doesn't need to be unique. If multiple components of the specified type have the
     * specified name, the returned list contains all of them.
     *
     * @param clazz
     *            Data type of component(s) to be read
     * @param entityName
     *            EntityName of the component(s) to be read
     * @return List of the read component(s)
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<T> getObjectsByTypeAndName(final Class<T> clazz, final String entityName) {
        final Label label = Label.label(clazz.getCanonicalName());
        final List<T> nodes = new LinkedList<>();

        try (Transaction tx = this.graph.getGraphDatabaseService().beginTx()) {
            final ResourceIterator<Node> nodesIter = this.graph.getGraphDatabaseService().findNodes(label,
                    this.nameLabel, entityName);

            while (nodesIter.hasNext()) {
                final Node node = nodesIter.next();
                final Set<Node> containmentsAndDatatypes = this.getAllContainmentsAndDatatypes(node,
                        new HashSet<Node>());
                final EObject component = this.readNodes(node, containmentsAndDatatypes, new HashMap<Node, EObject>());
                nodes.add((T) component);

            }
            tx.success();
        }

        return nodes;
    }

    /**
     * Helper method for reading: Recursively returns all containments and data types of a given
     * node.
     *
     * @param node
     *            The node to start with
     * @param containmentsAndDatatypes
     *            Initially empty set of all containments and data types
     * @return The passed containmentsAndDatatypes set now filled with containments and data types
     */
    private Set<Node> getAllContainmentsAndDatatypes(final Node node, final Set<Node> containmentsAndDatatypes) {
        if (!containmentsAndDatatypes.contains(node)) {
            containmentsAndDatatypes.add(node);

            for (final Relationship rel : node.getRelationships(Direction.OUTGOING, EMFRelationshipType.CONTAINS,
                    EMFRelationshipType.IS_TYPE)) {
                this.getAllContainmentsAndDatatypes(rel.getEndNode(), containmentsAndDatatypes);
            }
        }

        return containmentsAndDatatypes;
    }

    /**
     * Helper method for reading: Starting from a given node this method recursively reads all
     * contained successor nodes and instantiates the correspondent EObjects. Calls to this method
     * have to be performed from inside a {@link Transaction}.
     *
     * @param node
     *            The node to start with
     * @param containmentsAndDatatypes
     *            Set of all containments and data types of the root node
     * @param nodesToCreatedObjects
     *            Initially empty map of nodes to already created objects to make sure that objects
     *            are instantiated just once
     * @return The root
     */
    private EObject readNodes(final Node node, final Set<Node> containmentsAndDatatypes,
            final Map<Node, EObject> nodesToCreatedObjects) {
        final EObject object;

        if (!nodesToCreatedObjects.containsKey(node)) {
            // Get node's data type label and instantiate a new empty object of this data type
            final Label label = ModelProviderUtil.getFirstLabel(node.getLabels());
            object = ModelProviderUtil.instantiateEObject(this.graph.getEFactories(), label.name());

            this.loadAttributes(object, node.getAllProperties());

            // Already register unfinished components because there might be circles
            nodesToCreatedObjects.putIfAbsent(node, object);

            this.loadRelatedNodes(object, node, containmentsAndDatatypes, nodesToCreatedObjects);
        } else {
            object = nodesToCreatedObjects.get(node);
        }

        return object;
    }

    /**
     * Load related nodes representing referenced components.
     *
     * @param component
     *            component the other components are related to
     * @param node
     *            the corresponding node to the component
     * @param containmentsAndDatatypes
     *            datatypes
     * @param nodesToCreatedObjects
     *            additional nodes
     */
    @SuppressWarnings("unchecked")
    private void loadRelatedNodes(final EObject component, final Node node, final Set<Node> containmentsAndDatatypes,
            final Map<Node, EObject> nodesToCreatedObjects) {
        for (final Relationship rel : ModelProviderUtil.sortRelsByPosition(node.getRelationships(Direction.OUTGOING))) {
            final Node endNode = rel.getEndNode();
            final String relName = (String) rel.getProperty(ModelProvider.REF_NAME);
            final EReference ref = (EReference) component.eClass().getEStructuralFeature(relName);
            Object refReprensation = component.eGet(ref);

            // For partial reading: Only load containments and data types of the root
            if (containmentsAndDatatypes.contains(endNode)) {

                if (refReprensation instanceof EList<?>) {
                    final EObject endComponent = this.readNodes(endNode, containmentsAndDatatypes,
                            nodesToCreatedObjects);
                    ((EList<EObject>) refReprensation).add(endComponent);

                } else {
                    refReprensation = this.readNodes(endNode, containmentsAndDatatypes, nodesToCreatedObjects);
                    component.eSet(ref, refReprensation);
                }

            } else {
                // Create proxy EObject here...
                final Label endLabel = ModelProviderUtil.getFirstLabel(endNode.getLabels());
                final EObject endComponent = ModelProviderUtil.instantiateEObject(this.graph.getEFactories(),
                        endLabel.name());

                if (endComponent != null) {
                    final URI endUri = URI.createURI(endNode.getProperty(ModelProvider.EMF_URI).toString());
                    ((BasicEObjectImpl) endComponent).eSetProxyURI(endUri);
                    // TODO here to set the unique identifier

                    // Load attribute values from the node
                    final Iterator<Map.Entry<String, Object>> i2 = endNode.getAllProperties().entrySet().iterator();
                    while (i2.hasNext()) {
                        final Entry<String, Object> property = i2.next();
                        final EAttribute attr = (EAttribute) endComponent.eClass()
                                .getEStructuralFeature(property.getKey());

                        // attr == null for the emfUri property stored in the graph
                        if (attr != null) {
                            final Object value = ModelProviderUtil.instantiateAttribute(attr.getEAttributeType(),
                                    property.getValue().toString());

                            if (value != null) {
                                endComponent.eSet(attr, value);
                            }
                        }
                    }

                    if (refReprensation instanceof EList<?>) {
                        ((EList<EObject>) refReprensation).add(endComponent);
                    } else {
                        if (ref.isChangeable()) { // TODO what to do with unchangeable elements
                            component.eSet(ref, endComponent);
                        }
                    }
                }
            }
        }

    }

    /**
     * Load attribute values from the node.
     *
     * @param object
     *            component where the attributes are attached to
     * @param properties
     *            the attributes to scan for values
     */
    private void loadAttributes(final EObject object, final Map<String, Object> properties) {
        for (final Entry<String, Object> property : properties.entrySet()) {
            final EAttribute attr = (EAttribute) object.eClass().getEStructuralFeature(property.getKey());

            // attr == null for the emfUri property stored in the graph
            if (attr != null) {
                if (attr.isMany()) {
                    this.createManyValuesAttribute(object, attr, property);
                } else {
                    this.createSingleValueAttribute(object, attr, property);
                }

            }
        }
    }

    private void createManyValuesAttribute(final EObject component, final EAttribute attr,
            final Entry<String, Object> property) {
        @SuppressWarnings("unchecked")
        final List<Object> attribute = (List<Object>) component.eGet(attr);

        final String valueString = property.getValue().toString();

        final String[] values = valueString.substring(1, valueString.length() - 1).split(", ");
        for (final String value : values) {
            final Object convertedValue = this.convertValue(attr.getEAttributeType(), value);

            attribute.add(convertedValue);
        }
    }

    private void createSingleValueAttribute(final EObject component, final EAttribute attr,
            final Entry<String, Object> property) {
        component.eSet(attr, this.convertValue(attr.getEAttributeType(), property.getValue().toString()));
    }

    private Object convertValue(final EDataType type, final String input) {
        Object value = ModelProviderUtil.instantiateAttribute(type, input);

        if (value == null) {
            for (final EFactory factory : this.graph.getEFactories()) {
                value = factory.createFromString(type, input);

                if (value != null) {
                    return value;
                }
            }

            throw new InternalError("Type " + type.getInstanceClassName() + " is not supported.");
        } else {
            return value;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.iobserve.model.provider.neo4j#collectAllObjectIdsByType(java.lang.Class)
     */
    @Override
    public List<String> collectAllObjectIdsByType(final Class<T> clazz) {
        try (Transaction tx = this.graph.getGraphDatabaseService().beginTx()) {
            final ResourceIterator<Node> nodes = this.graph.getGraphDatabaseService()
                    .findNodes(Label.label(clazz.getCanonicalName()));

            final List<String> ids = new LinkedList<>();

            while (nodes.hasNext()) {
                final Node n = nodes.next();
                ids.add(n.getProperty(this.idLabel).toString());
            }

            tx.success();
            return ids;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.iobserve.model.provider.neo4j#collectAllObjectsByType(java.lang.Class)
     */
    @Override
    public List<T> collectAllObjectsByType(final Class<T> clazz) {
        final Label label = Label.label(clazz.getCanonicalName());
        final List<T> nodes = new LinkedList<>();

        try (Transaction tx = this.graph.getGraphDatabaseService().beginTx()) {
            final ResourceIterator<Node> nodesIter = this.graph.getGraphDatabaseService().findNodes(label);

            while (nodesIter.hasNext()) {
                final Node node = nodesIter.next();
                final Set<Node> containmentsAndDatatypes = this.getAllContainmentsAndDatatypes(node,
                        new HashSet<Node>());
                final EObject object = this.readNodes(node, containmentsAndDatatypes, new HashMap<Node, EObject>());
                nodes.add((T) object);
            }
            tx.success();
        }

        return nodes;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.iobserve.model.provider.neo4j#readRootComponent(java.lang.Class)
     */
    @Override
    public T getAndLockModelRootNode(final Class<T> clazz) {
        ModelProviderSynchronizer.getLock(this);
        return this.getModelRootNode(clazz);
    }

    /**
     * Reads the models (partition) root node and all contained classes without locking the graph
     * for other providers.
     *
     * @param clazz
     *            Data type of the root object
     * @return The read object
     */
    @Override
    @SuppressWarnings("unchecked")
    public T getModelRootNode(final Class<T> clazz) {
        EObject object = null;
        try (Transaction tx = this.graph.getGraphDatabaseService().beginTx()) {

            final ResourceIterator<Node> nodes = this.graph.getGraphDatabaseService()
                    .findNodes(Label.label(clazz.getCanonicalName()));

            if (nodes.hasNext()) {
                final Node node = nodes.next();

                final Set<Node> containmentsAndDatatypes = this.getAllContainmentsAndDatatypes(node,
                        new HashSet<Node>());
                object = this.readNodes(node, containmentsAndDatatypes, new HashMap<Node, EObject>());
            }

            tx.success();
        }

        return (T) object;
    }

    /**
     * Reads the containing component of the specified component from the provider's graph.
     *
     * @param clazz
     *            Data type of the contained component
     * @param id
     *            Id of the contained component
     * @return The containing component
     */
    public EObject readContainingComponentById(final Class<?> clazz, final String id) {
        ModelProviderSynchronizer.getLock(this);
        return this.readOnlyContainingComponentById(clazz, id);
    }

    /**
     * Reads the containing component of the specified component from the provider's graph without
     * locking it for other providers.
     *
     * @param clazz
     *            Data type of the contained component
     * @param id
     *            Id of the contained component
     * @return The containing component
     */
    public EObject readOnlyContainingComponentById(final Class<?> clazz, final String id) {
        final Label label = Label.label(clazz.getCanonicalName());

        EObject component = null;

        try (Transaction tx = this.graph.getGraphDatabaseService().beginTx()) {
            final Node node = this.graph.getGraphDatabaseService().findNode(label, this.idLabel, id);
            final Iterator<Relationship> inRels = node
                    .getRelationships(Direction.INCOMING, EMFRelationshipType.CONTAINS).iterator();

            if (inRels.hasNext()) {
                final Node endNode = inRels.next().getStartNode();
                final Set<Node> containmentsAndDatatypes = this.getAllContainmentsAndDatatypes(endNode,
                        new HashSet<Node>());
                component = this.readNodes(endNode, containmentsAndDatatypes, new HashMap<Node, EObject>());
            }

            tx.success();
        }

        return component;
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
    public List<EObject> readReferencingComponentsById(final Class<?> clazz, final String id) {
        ModelProviderSynchronizer.getLock(this);
        return this.collectReferencingObjectsByTypeAndId(clazz, id);
    }

    /**
     * Reads components referencing to the specified component from the provider's graph without
     * locking it for other providers.
     *
     * @param clazz
     *            Data type of the referenced component
     * @param id
     *            Id of the referenced component
     * @return The referencing components
     */
    @Override
    public List<EObject> collectReferencingObjectsByTypeAndId(final Class<?> clazz, final String id) {
        final List<EObject> referencingComponents = new LinkedList<>();
        final Label label = Label.label(clazz.getCanonicalName());

        try (Transaction tx = this.graph.getGraphDatabaseService().beginTx()) {
            final Node node = this.graph.getGraphDatabaseService().findNode(label, this.idLabel, id);
            if (node != null) {
                for (final Relationship inRel : node.getRelationships(Direction.INCOMING,
                        EMFRelationshipType.REFERENCES)) {
                    final Node startNode = inRel.getStartNode();
                    final Set<Node> containmentsAndDatatypes = this.getAllContainmentsAndDatatypes(startNode,
                            new HashSet<Node>());
                    final EObject component = this.readNodes(startNode, containmentsAndDatatypes,
                            new HashMap<Node, EObject>());
                    referencingComponents.add(component);
                }
            }

            tx.success();
        }

        return referencingComponents;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.iobserve.model.provider.neo4j#updateComponent(java.lang.Class, T)
     */
    @Override
    public void updateObject(final Class<T> clazz, final T object) {
        ModelProviderSynchronizer.getLock(this);

        final EAttribute idAttr = object.eClass().getEIDAttribute();
        final Label label = Label.label(clazz.getCanonicalName());

        final Set<EObject> containmentsAndDatatypes;
        final Node node;

        if (idAttr != null) {
            try (Transaction tx = this.graph.getGraphDatabaseService().beginTx()) {
                node = this.graph.getGraphDatabaseService().findNode(label, this.idLabel, object.eGet(idAttr));
                containmentsAndDatatypes = this.getAllContainmentsAndDatatypes(object, new HashSet<>());
                this.updateNodes(object, node, containmentsAndDatatypes, new HashSet<EObject>());
                tx.success();
            }
        } else if (object instanceof ResourceEnvironment || object instanceof UsageModel
                || object instanceof PrivacyModel) {
            try (Transaction tx = this.graph.getGraphDatabaseService().beginTx()) {
                final ResourceIterator<Node> nodes = this.graph.getGraphDatabaseService()
                        .findNodes(Label.label(clazz.getCanonicalName()));
                if (nodes.hasNext()) {
                    node = nodes.next();
                    containmentsAndDatatypes = this.getAllContainmentsAndDatatypes(object, new HashSet<>());
                    this.updateNodes(object, node, containmentsAndDatatypes, new HashSet<EObject>());
                }
                tx.success();
            }
        } else {
            ModelProvider.LOGGER.warn(
                    "Updated component needs to have an id or be of type Allocation, Repository, ResourceEnvironment, System or UsageModel");
        }

        ModelProviderSynchronizer.releaseLock(this);
    }

    private Node updateNodes(final EObject component, final Node node, final Set<EObject> containmentsAndDatatypes,
            final Set<EObject> updatedComponents) {

        if (!updatedComponents.contains(component)) {
            updatedComponents.add(component);

            // Update node properties
            final Map<String, Object> nodeProperties = node.getAllProperties();

            for (final EAttribute attr : component.eClass().getEAllAttributes()) {
                final String key = attr.getName();
                final Object value = component.eGet(attr);
                if (value != null) {
                    node.setProperty(key, value.toString());
                    nodeProperties.remove(key);
                }
            }

            // Remove possibly removed properties
            final Iterator<Entry<String, Object>> iter = nodeProperties.entrySet().iterator();
            while (iter.hasNext()) {
                node.removeProperty(iter.next().getKey());
            }

            // Create a URI to enable proxy resolving
            final URI uri = ((BasicEObjectImpl) component).eProxyURI();
            if (uri == null) {
                node.setProperty(ModelProvider.EMF_URI, ModelProviderUtil.getUriString(component));
            } else {
                node.setProperty(ModelProvider.EMF_URI, uri.toString());
            }

            // Outgoing references are only stored for containments and data types of the root,
            // otherwise we just store the blank node as a proxy
            if (containmentsAndDatatypes.contains(component)) {

                // Create list of node's outgoing relationships
                final List<Relationship> outRels = new LinkedList<>();
                node.getRelationships(Direction.OUTGOING).forEach(outRels::add);

                for (final EReference ref : component.eClass().getEAllReferences()) {
                    final Object refReprensation = component.eGet(ref);

                    // 0..* refs are represented as a list and 1 refs are represented directly
                    if (refReprensation instanceof EList<?>) {

                        final EList<?> refs = (EList<?>) component.eGet(ref);
                        for (int i = 0; i < refs.size(); i++) {
                            final Object o = refs.get(i);

                            // Find node matching o
                            Node endNode = ModelProviderUtil
                                    .findMatchingNode(ModelProviderUtil.getUriString((EObject) o), outRels);

                            if (endNode == null) {
                                // Create a non existing node
                                endNode = this.createNodes((EObject) o, containmentsAndDatatypes, new HashMap<>());
                                final Relationship rel = node.createRelationshipTo(endNode,
                                        ModelProviderUtil.getRelationshipType(ref, o));
                                rel.setProperty(ModelProvider.REF_NAME, ref.getName());
                                rel.setProperty(ModelProvider.REF_POS, i);

                            }

                            this.updateNodes((EObject) o, endNode, containmentsAndDatatypes, updatedComponents);
                        }
                    } else {
                        if (refReprensation != null) {
                            // Find node matching refRepresentation
                            Node endNode = ModelProviderUtil.findMatchingNode(
                                    ModelProviderUtil.getUriString((EObject) refReprensation), outRels);

                            if (endNode == null) {
                                // Create a non existing node
                                endNode = this.createNodes((EObject) refReprensation, containmentsAndDatatypes,
                                        new HashMap<>());
                                final Relationship rel = node.createRelationshipTo(endNode,
                                        ModelProviderUtil.getRelationshipType(ref, refReprensation));
                                rel.setProperty(ModelProvider.REF_NAME, ref.getName());
                                rel.setProperty(ModelProvider.REF_POS, 0);
                            }

                            this.updateNodes((EObject) refReprensation, endNode, containmentsAndDatatypes,
                                    updatedComponents);
                        }
                    }
                }

                // Delete nodes that are not referenced anymore
                for (final Relationship r : outRels) {
                    try {
                        final Node endNode = r.getEndNode();
                        r.delete();
                        this.deleteComponentAndDatatypeNodes(endNode, false);
                    } catch (final NotFoundException e) {
                        // relation has already been deleted
                    }
                }
            }
        }

        return node;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.iobserve.model.provider.neo4j#deleteComponent(java.lang.Class, java.lang.String)
     */
    @Override
    public void deleteObjectByTypeAndId(final Class<T> clazz, final String id) {
        ModelProviderSynchronizer.getLock(this);

        final Label label = Label.label(clazz.getCanonicalName());

        try (Transaction tx = this.graph.getGraphDatabaseService().beginTx()) {
            final Node node = this.graph.getGraphDatabaseService().findNode(label, this.idLabel, id);
            if (node != null) {
                this.deleteComponentNodes(node);
            }
            tx.success();
        }

        ModelProviderSynchronizer.releaseLock(this);
    }

    /**
     * Helper method for deleting: Starting with a given node this method recursively traverses down
     * through all nodes accessible via {@link EMFRelationshipType#CONTAINS} edges and then deletes
     * them from bottom to the top.
     *
     * @param node
     *            The node to start with
     */
    private void deleteComponentNodes(final Node node) {
        for (final Relationship rel : node.getRelationships(Direction.OUTGOING, EMFRelationshipType.CONTAINS)) {
            this.deleteComponentNodes(rel.getEndNode());
        }

        for (final Relationship rel : node.getRelationships()) {
            rel.delete();
        }

        node.delete();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.iobserve.model.provider.neo4j#deleteComponentAndDatatypes(Class, String, boolean)
     */
    @Override
    public void deleteObjectByIdAndDatatypes(final Class<T> clazz, final String id, final boolean forceDelete) {
        ModelProviderSynchronizer.getLock(this);

        final Label label = Label.label(clazz.getCanonicalName());

        final Node node;

        try (Transaction tx = this.graph.getGraphDatabaseService().beginTx()) {
            node = this.graph.getGraphDatabaseService().findNode(label, this.idLabel, id);
            this.deleteComponentAndDatatypeNodes(node, forceDelete);
            tx.success();
        }

        ModelProviderSynchronizer.releaseLock(this);
    }

    /**
     * Helper method for deleting: Combines {@link #markAccessibleNodes(Node)},
     * {@link #markDeletableNodes(Node, boolean)} and {@link #deleteMarkedNodes(Node)} in one
     * method.
     *
     * @param node
     *            The node to start with
     * @param forceDelete
     *            Force method to delete the specified node even if it is referenced with an is_type
     *            or contains edge
     */
    private void deleteComponentAndDatatypeNodes(final Node node, final boolean forceDelete) {
        this.markAccessibleNodes(node);
        this.markDeletableNodes(node, true, forceDelete);
        this.deleteMarkedNodes(node);
        this.deleteNonReferencedNodes();
    }

    /**
     * Helper method for deleting: Starting from a given node this method recursively marks all
     * nodes accessible via {@link EMFRelationshipType#CONTAINS} or
     * {@link EMFRelationshipType#IS_TYPE} edges. Calls to this method have to be performed from
     * inside a {@link Transaction}.
     *
     * @param node
     *            The node to start with
     */
    private void markAccessibleNodes(final Node node) {
        node.setProperty(ModelProvider.DELETE, true);

        for (final Relationship rel : node.getRelationships(Direction.OUTGOING, EMFRelationshipType.CONTAINS,
                EMFRelationshipType.IS_TYPE)) {
            if (!rel.hasProperty(ModelProvider.ACCESSIBLE)) {
                rel.setProperty(ModelProvider.ACCESSIBLE, true);
                this.markAccessibleNodes(rel.getEndNode());
            }
        }
    }

    /**
     * Helper method for deleting: Starting from a given node this method recursively marks all
     * accessible nodes marked with {@link #markAccessibleNodes(Node)} which can be deleted.
     * Starting from one node all contained nodes can be deleted as well. Nodes with incoming
     * {@link EMFRelationshipType#IS_TYPE} edges may only be deleted if they are not referenced from
     * outside the accessible nodes and have no predecessor which is referenced from outside the
     * accessible nodes via an {@link EMFRelationshipType#IS_TYPE} edge. Calls to this method have
     * to be performed from inside a {@link Transaction}.
     *
     * @param node
     *            The node to start with
     * @param reallyDeletePred
     *            Flag if predecessor may be deleted
     */
    private void markDeletableNodes(final Node node, final boolean reallyDeletePred, final boolean forceDelete) {
        boolean reallyDelete = reallyDeletePred;

        // Check if there are incoming IS_TYPE relations from outside
        for (final Relationship rel : node.getRelationships(Direction.INCOMING, EMFRelationshipType.CONTAINS,
                EMFRelationshipType.IS_TYPE)) {
            if (!rel.hasProperty(ModelProvider.ACCESSIBLE) && !forceDelete) {
                reallyDelete = false;
            }
        }

        // Remove delete property if node must not be deleted
        if (node.hasProperty(ModelProvider.DELETE) && !reallyDelete) {
            node.removeProperty(ModelProvider.DELETE);
        }

        // Recursively check successors and mark already visited edges to prevent call circles
        for (final Relationship rel : node.getRelationships(Direction.OUTGOING, EMFRelationshipType.CONTAINS,
                EMFRelationshipType.IS_TYPE)) {
            if (!rel.hasProperty(ModelProvider.VISITED)) {
                rel.setProperty(ModelProvider.VISITED, true);
                this.markDeletableNodes(rel.getEndNode(), reallyDelete, false);
            }
        }

        // Remove edge marks when returned from successor node's calls
        for (final Relationship rel : node.getRelationships(Direction.OUTGOING, EMFRelationshipType.CONTAINS,
                EMFRelationshipType.IS_TYPE)) {
            rel.removeProperty(ModelProvider.VISITED);
        }
    }

    /**
     * Helper method for deleting: Starting from a given node this method recursively traverses down
     * through all accessible nodes marked with {@link #markAccessibleNodes(Node)} and then deletes
     * all nodes marked with a delete flag by {@link #markDeletableNodes(Node)} from bottom to the
     * top. Calls to this method have to be performed from inside a {@link Transaction}.
     *
     * @param node
     *            The node to start with
     */
    private void deleteMarkedNodes(final Node node) {
        // Recursively go to the lowest node and mark already visited edges to prevent call circles
        for (final Relationship rel : node.getRelationships(Direction.OUTGOING, EMFRelationshipType.CONTAINS,
                EMFRelationshipType.IS_TYPE)) {

            try {
                if (!rel.hasProperty(ModelProvider.VISITED)) {
                    rel.setProperty(ModelProvider.VISITED, true);
                    this.deleteMarkedNodes(rel.getEndNode());
                }
            } catch (final NotFoundException e) {
                // relation has already been deleted on another path
                e.printStackTrace();
            }
        }

        try {
            if (node.hasProperty(ModelProvider.DELETE)) {
                // Delete node and its relationships
                for (final Relationship rel : node.getRelationships()) {
                    rel.delete();
                }
                node.delete();

            } else {
                // Only remove visited mark
                for (final Relationship rel : node.getRelationships(Direction.OUTGOING, EMFRelationshipType.CONTAINS,
                        EMFRelationshipType.IS_TYPE)) {
                    rel.removeProperty(ModelProvider.VISITED);
                }
            }
        } catch (final NotFoundException e) {
            // node has already been deleted on another path
            e.printStackTrace();
        }
    }

    /**
     * Helper method for deleting: This method acts as a kind of garbage collector and deletes nodes
     * from the graph which are not connected to any other node.
     */
    private void deleteNonReferencedNodes() {
        final ResourceIterator<Node> nodesIter = this.graph.getGraphDatabaseService().getAllNodes().iterator();
        while (nodesIter.hasNext()) {
            final Node node = nodesIter.next();

            if (!node.getRelationships().iterator().hasNext()) {
                node.delete();
            }
        }
    }

    /**
     * Returns the provider's graph.
     *
     * @return The graph
     */
    @Override
    public ModelGraph getGraph() {
        return this.graph;
    }
}
