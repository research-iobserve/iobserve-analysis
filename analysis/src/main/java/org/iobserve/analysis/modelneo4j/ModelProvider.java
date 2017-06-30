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
package org.iobserve.analysis.modelneo4j;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.BasicEObjectImpl;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.PrimitiveDataType;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

/**
 * Provides methods to access the pcm models stored in a neo4j graph database. Enables partial
 * access, to access only parts of a pcm component model, i.e. a submodel.
 *
 * @author Lars Bluemke
 *
 * @param <T>
 *            Type of the model's or submodel's root component
 */
public class ModelProvider<T extends EObject> implements IModelProvider<T> {

    private static final String EMF_URI = "emfUri";
    private static final String ENTITY_NAME = "entityName";
    private static final String ID = "id";
    private static final String REF_NAME = "refName";
    private static final String TYPE = "type";

    private static final String ACCESSIBLE = "accessible";
    private static final String DELETE = "delete";
    private static final String VISITED = "visited";

    private final Graph graph;

    /**
     * Creates a new model provider.
     *
     * @param graph
     *            The neo4j graph database
     */
    public ModelProvider(final Graph graph) {
        this.graph = graph;
    }

    /**
     * Deletes all nodes and relationships in the {@link #graph}.
     */
    public void clearGraph() {
        try (Transaction tx = this.graph.getGraphDatabaseService().beginTx()) {
            this.graph.getGraphDatabaseService().execute("MATCH (n) DETACH DELETE (n)");
            tx.success();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.iobserve.analysis.modelneo4j.IModelProvider#createComponent(T)
     */
    @Override
    public Node createComponent(final T component) {
        final Node node;
        try (Transaction tx = this.graph.getGraphDatabaseService().beginTx()) {
            final HashSet<EObject> containmentsAndDatatypes = this.getAllContainmentsAndDatatypes(component,
                    new HashSet<>());
            node = this.createNodes(component, containmentsAndDatatypes, new HashMap<>());
            tx.success();
        }
        return node;
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
    private HashSet<EObject> getAllContainmentsAndDatatypes(final EObject component,
            final HashSet<EObject> containmentsAndDatatypes) {
        if (!containmentsAndDatatypes.contains(component)) {
            containmentsAndDatatypes.add(component);

            for (final EReference ref : component.eClass().getEAllReferences()) {

                final Object refObject = component.eGet(ref);

                if (refObject instanceof EList<?>) {
                    for (final Object o : (EList<?>) component.eGet(ref)) {
                        if (ref.isContainment() || (ModelProviderUtil.isDatatype(ref, o))) {
                            this.getAllContainmentsAndDatatypes((EObject) o, containmentsAndDatatypes);
                        }
                    }

                } else {
                    if ((refObject != null)
                            && (ref.isContainment() || (ModelProviderUtil.isDatatype(ref, refObject)))) {
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
     * @param component
     *            Component to save
     * @param containmentsAndDatatypes
     *            Set of EObjects contained in the root preferably created by
     *            {@link #getAllContainmentsAndDatatypes(EObject, HashSet)}
     * @param objectsToCreatedNodes
     *            Initially empty map of EObjects to already created correspondent nodes to make
     *            sure nodes are written just once
     * @return Root node of the component's graph
     */
    private Node createNodes(final EObject component, final HashSet<EObject> containmentsAndDatatypes,
            final HashMap<EObject, Node> objectsToCreatedNodes) {
        // Create a label representing the type of the component
        final Label label = Label.label(ModelProviderUtil.getTypeName(component.eClass()));
        Node node = null;

        // Check if node has already been created
        final EAttribute idAttr = component.eClass().getEIDAttribute();
        if (idAttr != null) {
            node = this.graph.getGraphDatabaseService().findNode(label, ModelProvider.ID, component.eGet(idAttr));
        } else if (component instanceof PrimitiveDataType) {
            node = this.graph.getGraphDatabaseService().findNode(label, ModelProvider.TYPE,
                    ((PrimitiveDataType) component).getType().name());
        } else if ((component instanceof UsageModel) || (component instanceof ResourceEnvironment)
                || (component instanceof Allocation)) {
            final ResourceIterator<Node> nodes = this.graph.getGraphDatabaseService()
                    .findNodes(Label.label(component.eClass().getName()));
            if (nodes.hasNext()) {
                node = nodes.next();
            }
        } else {
            // For components that cannot be found in the graph (e.g. due to missing id) but have
            // been created in this recursion
            node = objectsToCreatedNodes.get(component);
        }

        // If there is no node yet, create one
        if (node == null) {

            node = this.graph.getGraphDatabaseService().createNode(label);
            objectsToCreatedNodes.put(component, node);

            // Create a URI to enable proxy resolving
            final URI uri = ((BasicEObjectImpl) component).eProxyURI();
            if (uri == null) {
                node.setProperty(ModelProvider.EMF_URI, ModelProviderUtil.getUriString(component));
            } else {
                node.setProperty(ModelProvider.EMF_URI, uri.toString());
            }

            // Save attributes as node properties
            for (final EAttribute attr : component.eClass().getEAllAttributes()) {
                final Object value = component.eGet(attr);
                if (value != null) {
                    node.setProperty(attr.getName(), value.toString());
                }
            }

            // Outgoing references are only stored for containments and data types of the root,
            // otherwise we just store the blank node as a proxy
            if (containmentsAndDatatypes.contains(component)) {

                for (final EReference ref : component.eClass().getEAllReferences()) {
                    final Object refReprensation = component.eGet(ref);

                    // 0..* refs are represented as a list and 1 refs are represented directly
                    if (refReprensation instanceof EList<?>) {

                        for (final Object o : (EList<?>) component.eGet(ref)) {
                            final Node refNode = this.createNodes((EObject) o, containmentsAndDatatypes,
                                    objectsToCreatedNodes);
                            final Relationship rel = node.createRelationshipTo(refNode,
                                    ModelProviderUtil.getRelationshipType(ref, o));
                            rel.setProperty(ModelProvider.REF_NAME, ref.getName());
                        }
                    } else {
                        if (refReprensation != null) {
                            final Node refNode = this.createNodes((EObject) refReprensation, containmentsAndDatatypes,
                                    objectsToCreatedNodes);
                            final Relationship rel = node.createRelationshipTo(refNode,
                                    ModelProviderUtil.getRelationshipType(ref, refReprensation));
                            rel.setProperty(ModelProvider.REF_NAME, ref.getName());
                        }
                    }
                }
            }
        }

        return node;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.iobserve.analysis.modelneo4j.IModelProvider#readComponentById(java.lang.Class,
     * java.lang.String)
     */
    @Override
    @SuppressWarnings("unchecked")
    public T readComponentById(final Class<T> clazz, final String id) {
        final Label label = Label.label(clazz.getSimpleName());
        Node node;
        EObject component;

        try (Transaction tx = this.graph.getGraphDatabaseService().beginTx()) {
            node = this.graph.getGraphDatabaseService().findNode(label, ModelProvider.ID, id);
            final HashSet<Node> containmentsAndDatatypes = this.getAllContainmentsAndDatatypes(node,
                    new HashSet<Node>());
            component = this.readComponent(node, containmentsAndDatatypes, new HashMap<Node, EObject>());
            tx.success();
        }

        return (T) component;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.iobserve.analysis.modelneo4j.IModelProvider#readComponentByName(java.lang.Class,
     * java.lang.String)
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<T> readComponentByName(final Class<T> clazz, final String entityName) {
        final Label label = Label.label(clazz.getSimpleName());
        final List<T> nodes = new LinkedList<>();

        try (Transaction tx = this.graph.getGraphDatabaseService().beginTx()) {
            final ResourceIterator<Node> nodesIter = this.graph.getGraphDatabaseService().findNodes(label,
                    ModelProvider.ENTITY_NAME, entityName);

            while (nodesIter.hasNext()) {
                final Node node = nodesIter.next();
                final HashSet<Node> containmentsAndDatatypes = this.getAllContainmentsAndDatatypes(node,
                        new HashSet<Node>());
                final EObject component = this.readComponent(node, containmentsAndDatatypes,
                        new HashMap<Node, EObject>());
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
    private HashSet<Node> getAllContainmentsAndDatatypes(final Node node,
            final HashSet<Node> containmentsAndDatatypes) {

        if (!containmentsAndDatatypes.contains(node)) {
            containmentsAndDatatypes.add(node);

            for (final Relationship rel : node.getRelationships(Direction.OUTGOING, PcmRelationshipType.CONTAINS,
                    PcmRelationshipType.IS_TYPE)) {
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
    @SuppressWarnings("unchecked")
    private EObject readComponent(final Node node, final HashSet<Node> containmentsAndDatatypes,
            final HashMap<Node, EObject> nodesToCreatedObjects) {
        EObject component;

        if (!nodesToCreatedObjects.containsKey(node)) {
            // Get node's data type label and instantiate a new empty object of this data type
            final Label label = ModelProviderUtil.getFirstLabel(node.getLabels());
            component = ModelProviderUtil.instantiateEObject(label.name());

            // Set Uri
            final URI uri = URI.createURI(node.getProperty(ModelProvider.EMF_URI).toString());
            ((BasicEObjectImpl) component).eSetProxyURI(uri);

            // Load attribute values from the node
            final Iterator<Map.Entry<String, Object>> i = node.getAllProperties().entrySet().iterator();
            while (i.hasNext()) {
                final Entry<String, Object> property = i.next();
                final EAttribute attr = (EAttribute) component.eClass().getEStructuralFeature(property.getKey());

                // attr == null for the emfUri property stored in the graph
                if (attr != null) {
                    final Object value = ModelProviderUtil.instantiateAttribute(
                            attr.getEAttributeType().getInstanceClass(), property.getValue().toString());

                    if (value != null) {
                        component.eSet(attr, value);
                    }
                }
            }

            // Already register unfinished components because there might be circles
            nodesToCreatedObjects.putIfAbsent(node, component);

            // Load related nodes representing referenced components
            for (final Relationship rel : node.getRelationships(Direction.OUTGOING)) {
                final Node endNode = rel.getEndNode();
                final String relName = (String) rel.getProperty(ModelProvider.REF_NAME);
                final EReference ref = (EReference) component.eClass().getEStructuralFeature(relName);
                Object refReprensation = component.eGet(ref);

                // For partial reading: Only load containments and data types of the root
                if (containmentsAndDatatypes.contains(endNode)) {

                    if (refReprensation instanceof EList<?>) {
                        final EObject endComponent = this.readComponent(endNode, containmentsAndDatatypes,
                                nodesToCreatedObjects);
                        ((EList<EObject>) refReprensation).add(endComponent);
                    } else {
                        refReprensation = this.readComponent(endNode, containmentsAndDatatypes, nodesToCreatedObjects);
                        component.eSet(ref, refReprensation);
                    }

                } else {
                    // Create proxy EObject here...
                    final Label endLabel = ModelProviderUtil.getFirstLabel(endNode.getLabels());
                    final EObject endComponent = ModelProviderUtil.instantiateEObject(endLabel.name());

                    if (endComponent != null) {
                        final URI endUri = URI.createURI(endNode.getProperty(ModelProvider.EMF_URI).toString());
                        ((BasicEObjectImpl) endComponent).eSetProxyURI(endUri);

                        // Load attribute values from the node
                        final Iterator<Map.Entry<String, Object>> i2 = endNode.getAllProperties().entrySet().iterator();
                        while (i2.hasNext()) {
                            final Entry<String, Object> property = i2.next();
                            final EAttribute attr = (EAttribute) endComponent.eClass()
                                    .getEStructuralFeature(property.getKey());

                            // attr == null for the emfUri property stored in the graph
                            if (attr != null) {
                                final Object value = ModelProviderUtil.instantiateAttribute(
                                        attr.getEAttributeType().getInstanceClass(), property.getValue().toString());

                                if (value != null) {
                                    endComponent.eSet(attr, value);
                                }
                            }
                        }

                        if (refReprensation instanceof EList<?>) {
                            ((EList<EObject>) refReprensation).add(endComponent);
                        } else {
                            component.eSet(ref, endComponent);
                        }
                    }

                }
            }
        } else {
            component = nodesToCreatedObjects.get(node);
        }
        return component;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.iobserve.analysis.modelneo4j.IModelProvider#readComponentByType(java.lang.Class)
     */
    @Override
    public List<String> readComponentByType(final Class<T> clazz) {
        try (Transaction tx = this.graph.getGraphDatabaseService().beginTx()) {
            final ResourceIterator<Node> nodes = this.graph.getGraphDatabaseService()
                    .findNodes(Label.label(clazz.getSimpleName()));
            final LinkedList<String> ids = new LinkedList<>();

            while (nodes.hasNext()) {
                final Node n = nodes.next();
                ids.add(n.getProperty(ModelProvider.ID).toString());
            }

            tx.success();
            return ids;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.iobserve.analysis.modelneo4j.IModelProvider#readRootComponent(java.lang.Class)
     */
    @Override
    @SuppressWarnings("unchecked")
    public T readRootComponent(final Class<T> clazz) {
        EObject component = null;
        try (Transaction tx = this.graph.getGraphDatabaseService().beginTx()) {
            if (clazz.equals(Repository.class) || clazz.equals(org.palladiosimulator.pcm.system.System.class)
                    || clazz.equals(Allocation.class) || clazz.equals(UsageModel.class)
                    || clazz.equals(ResourceEnvironment.class)) {
                final ResourceIterator<Node> nodes = this.graph.getGraphDatabaseService()
                        .findNodes(Label.label(clazz.getSimpleName()));
                if (nodes.hasNext()) {
                    final Node node = nodes.next();
                    final HashSet<Node> containmentsAndDatatypes = this.getAllContainmentsAndDatatypes(node,
                            new HashSet<Node>());
                    component = this.readComponent(node, containmentsAndDatatypes, new HashMap<Node, EObject>());
                }
            } else {
                System.err.println(
                        "Passed type has to be one of Repository, System, Allocation, ResourceEnvironment or UsageModel!");
            }

            tx.success();
        }
        return (T) component;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.iobserve.analysis.modelneo4j.IModelProvider#updateComponent(java.lang.Class, T)
     */
    @Override
    public void updateComponent(final Class<T> clazz, final T component) {
        final EAttribute idAttr = component.eClass().getEIDAttribute();
        if (idAttr != null) {
            this.deleteComponentAndDatatypes(clazz, component.eGet(idAttr).toString());
            this.createComponent(component);
        } else {
            System.err.println("Updated component needs to have an id");
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.iobserve.analysis.modelneo4j.IModelProvider#deleteComponent(java.lang.Class,
     * java.lang.String)
     */
    @Override
    public void deleteComponent(final Class<T> clazz, final String id) {
        final Label label = Label.label(clazz.getSimpleName());
        Node node;

        try (Transaction tx = this.graph.getGraphDatabaseService().beginTx()) {
            node = this.graph.getGraphDatabaseService().findNode(label, ModelProvider.ID, id);
            this.deleteComponent(node);
            tx.success();
        }
    }

    /**
     * Helper method for deleting: Starting with a given node this method recursively traverses down
     * through all nodes accessible via {@link PcmRelationshipType#CONTAINS} edges and then deletes
     * them from bottom to the top.
     *
     * @param node
     *            The node to start with
     */
    private void deleteComponent(final Node node) {

        for (final Relationship rel : node.getRelationships(Direction.OUTGOING, PcmRelationshipType.CONTAINS)) {
            this.deleteComponent(rel.getEndNode());
        }

        for (final Relationship rel : node.getRelationships()) {
            rel.delete();
        }

        node.delete();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.iobserve.analysis.modelneo4j.IModelProvider#deleteComponentAndDatatypes(java.lang.Class,
     * java.lang.String)
     */
    @Override
    public void deleteComponentAndDatatypes(final Class<T> clazz, final String id) {
        final Label label = Label.label(clazz.getSimpleName());
        Node node;

        try (Transaction tx = this.graph.getGraphDatabaseService().beginTx()) {
            node = this.graph.getGraphDatabaseService().findNode(label, ModelProvider.ID, id);
            this.markAccessibleNodes(node);
            this.markDeletableNodes(node, true);
            this.deleteNodes(node);
            tx.success();
        }

    }

    /**
     * Helper method for deleting: Starting from a given node this method recursively marks all
     * nodes accessible via {@link PcmRelationshipType#CONTAINS} or
     * {@link PcmRelationshipType#IS_TYPE} edges. Calls to this method have to be performed from
     * inside a {@link Transaction}.
     *
     * @param node
     *            The node to start with
     */
    private void markAccessibleNodes(final Node node) {
        node.setProperty(ModelProvider.DELETE, true);

        for (final Relationship rel : node.getRelationships(Direction.OUTGOING, PcmRelationshipType.CONTAINS,
                PcmRelationshipType.IS_TYPE)) {
            if (!rel.hasProperty(ModelProvider.ACCESSIBLE)) {
                rel.setProperty(ModelProvider.ACCESSIBLE, true);
                this.markAccessibleNodes(rel.getEndNode());
            }
        }

        return;
    }

    /**
     * Helper method for deleting: Starting from a given node this method recursively marks all
     * accessible nodes marked with {@link #markAccessibleNodes(Node)} which can be deleted.
     * Starting from one node all contained nodes can be deleted as well. Nodes with incoming
     * {@link PcmRelationshipType#IS_TYPE} edges may only be deleted if they are not referenced from
     * outside the accessible nodes and have no predecessor which is referenced from outside the
     * accessible nodes via an {@link PcmRelationshipType#IS_TYPE} edge. Calls to this method have
     * to be performed from inside a {@link Transaction}.
     *
     * @param node
     *            The node to start with
     * @param reallyDeletePred
     *            Flag if predecessor may be deleted
     */
    private void markDeletableNodes(final Node node, final boolean reallyDeletePred) {

        boolean reallyDelete = reallyDeletePred;

        // Check if there are incoming IS_TYPE relations from outside
        for (final Relationship rel : node.getRelationships(Direction.INCOMING, PcmRelationshipType.IS_TYPE)) {
            if (!rel.hasProperty(ModelProvider.ACCESSIBLE)) {
                reallyDelete = false;
            }
        }

        // Remove delete property if node must not be deleted
        if (node.hasProperty(ModelProvider.DELETE) && !reallyDelete) {
            node.removeProperty(ModelProvider.DELETE);
        }

        // Recursively check successors and mark already visited edges to prevent call circles
        for (final Relationship rel : node.getRelationships(Direction.OUTGOING, PcmRelationshipType.CONTAINS,
                PcmRelationshipType.IS_TYPE)) {
            if (!rel.hasProperty(ModelProvider.VISITED)) {
                rel.setProperty(ModelProvider.VISITED, true);
                this.markDeletableNodes(rel.getEndNode(), reallyDelete);
            }
        }

        // Remove edge marks when returned from successor node's calls
        for (final Relationship rel : node.getRelationships(Direction.OUTGOING, PcmRelationshipType.CONTAINS,
                PcmRelationshipType.IS_TYPE)) {
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
    private void deleteNodes(final Node node) {

        // Recursively go to the lowest node and mark already visited edges to prevent call circles
        for (final Relationship rel : node.getRelationships(Direction.OUTGOING, PcmRelationshipType.CONTAINS,
                PcmRelationshipType.IS_TYPE)) {

            try {
                if (!rel.hasProperty(ModelProvider.VISITED)) {
                    rel.setProperty(ModelProvider.VISITED, true);
                    this.deleteNodes(rel.getEndNode());
                }
            } catch (final NotFoundException e) {
                // relation has already been deleted on another path
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
                for (final Relationship rel : node.getRelationships(Direction.OUTGOING, PcmRelationshipType.CONTAINS,
                        PcmRelationshipType.IS_TYPE)) {
                    rel.removeProperty(ModelProvider.VISITED);
                }
            }
        } catch (final NotFoundException e) {
            // node has already been deleted on another path
        }

        return;
    }

    /**
     * Returns the provider's graph.
     *
     * @return The graph
     */
    public Graph getGraph() {
        return this.graph;
    }
}
