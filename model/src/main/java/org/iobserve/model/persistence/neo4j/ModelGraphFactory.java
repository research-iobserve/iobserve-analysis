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

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Reiner Jung
 *
 */
public final class ModelGraphFactory {

    public static final String PROXY_OBJECT = ":proxyObject";

    public static final String EMF_INTERNAL_ID = ":dbId";

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelGraphFactory.class);

    /** Utility class, prevent instantiation. */
    private ModelGraphFactory() {
        // nothing to do here
    }

    /**
     * Create a single node without resolved references.
     *
     * @param graphDatabaseService
     *            the graph database handle
     * @param storeableObject
     *            the object to be placed as proxy
     * @param id
     *            db id string
     * @return returns the created node
     */
    public static Node createNode(final GraphDatabaseService graphDatabaseService, final EObject storeableObject,
            final String id) {
        final Label typeName = Label.label(ModelGraphFactory.fqnClassName(storeableObject.eClass()));

        final Node node = graphDatabaseService.createNode(typeName);
        node.setProperty(ModelGraphFactory.EMF_INTERNAL_ID, id);
        node.setProperty(ModelGraphFactory.PROXY_OBJECT, false);

        /** attributes. */
        ModelGraphFactory.storeAttributes(node, storeableObject);

        return node;
    }

    /**
     * Create proxy node.
     *
     * @param graphDatabaseService
     *            database service
     * @param storeableObject
     *            the object to be placed as proxy
     * @param id
     *            db id
     * @return returns the corresponding node
     */
    public static Node createProxyNode(final GraphDatabaseService graphDatabaseService, final EObject storeableObject,
            final String id) {
        final Label typeName = Label.label(ModelGraphFactory.fqnClassName(storeableObject.eClass()));

        final Node node = graphDatabaseService.createNode(typeName);
        node.setProperty(ModelGraphFactory.EMF_INTERNAL_ID, id);

        node.setProperty(ModelGraphFactory.PROXY_OBJECT, true);

        return node;
    }

    /**
     * Create the full qualified name of a class.
     *
     * @param eClass
     *            eclass
     * @return retuns the fqn string
     */
    public static String fqnClassName(final EClass eClass) {
        return ModelGraphFactory.fqnPackageName(eClass.getEPackage()) + "." + eClass.getName();
    }

    private static String fqnPackageName(final EPackage ePackage) {
        if (ePackage.getESuperPackage() != null) {
            return ModelGraphFactory.fqnPackageName(ePackage.getESuperPackage()) + "." + ePackage.getName();
        } else {
            return ePackage.getName();
        }
    }

    /**
     * Add all object attributes to the node as properties.
     *
     * @param node
     *            the node
     * @param storeableObject
     *            the object having attributes
     */
    private static void storeAttributes(final Node node, final EObject storeableObject) {
        for (final EAttribute attr : storeableObject.eClass().getEAllAttributes()) {
            final Object value = storeableObject.eGet(attr);
            if (value != null) {
                node.setProperty(attr.getName(), value.toString());
            }
        }
    }

    /**
     * Create a single reference.
     *
     * @param graphDatabaseService
     *            database service
     * @param source
     *            source object
     * @param target
     *            target object
     * @param reference
     *            corresponding reference
     * @param i
     *            position info for lists
     * @throws DBException
     *             on db errors
     */
    public static void createRelationship(final GraphDatabaseService graphDatabaseService, final EObject source,
            final EObject target, final EReference reference, final long i) throws DBException {
        final Node sourceNode = ModelGraphFactory.findNode(graphDatabaseService, source);
        final Node targetNode = ModelGraphFactory.findNode(graphDatabaseService, target);
        ModelGraphFactory.createRelationship(graphDatabaseService, sourceNode, targetNode, reference, i);
    }

    /**
     * Create a single reference.
     *
     * @param graphDatabaseService
     *            database service
     * @param source
     *            source node
     * @param target
     *            target node
     * @param reference
     *            corresponding reference
     * @param i
     *            position info for lists
     * @throws DBException
     *             on db errors
     */
    public static void createRelationship(final GraphDatabaseService graphDatabaseService, final Node source,
            final Node target, final EReference reference, final long i) throws DBException {
        final Relationship relationship = source.createRelationshipTo(target,
                ModelGraphFactory.getRelationshipType(reference));
        relationship.setProperty(ModelProviderUtil.REF_NAME, reference.getName());
        relationship.setProperty(ModelProviderUtil.REF_POS, i);
    }

    private static RelationshipType getRelationshipType(final EReference reference) {
        if (reference.isContainment()) {
            return EMFRelationshipType.CONTAINS;
        } else {
            return EMFRelationshipType.REFERENCES;
        }
    }

    /**
     * Update the position information of a relationship.
     *
     * @param relationship
     *            the relationship
     * @param position
     *            the position
     */
    public static void updateRelationship(final Relationship relationship, final int position) {
        relationship.setProperty(ModelProviderUtil.REF_POS, position);
    }

    /**
     * Find a relationship which terminates at the targetNode.
     *
     * @param relationships
     *            all relationships to search in
     * @param targetNode
     *            the target node
     * @return the relationship or null
     */
    public static Relationship findRelationshipByEndNode(final Iterable<Relationship> relationships,
            final Node targetNode) {
        for (final Relationship relationship : relationships) {
            if (targetNode.getId() == relationship.getEndNodeId()) {
                return relationship;
            }
        }
        return null;
    }

    /**
     * Based on a certain object-URI and a list of references to nodes which possibly represent that
     * component, this method returns the node which actually represents the component or null if
     * there is none in the list. Relationships to matching nodes are removed from the list, so this
     * method can also be used to reduce a list of references to those references which link to
     * nodes whose component does not exist anymore.
     *
     * @param uri
     *            The object-URI
     * @param relationships
     *            The relationships to possibly matching nodes
     * @return The node representing the component or null if there is none
     */
    public static Node findMatchingNode(final String uri, final List<Relationship> relationships) {
        if (uri != null) {
            for (final Relationship relationship : relationships) {
                final Node node = relationship.getEndNode();
                try {
                    final String nodeUri = node.getProperty(ModelGraphFactory.EMF_INTERNAL_ID).toString();

                    if (uri.equals(nodeUri)) {
                        relationships.remove(relationship);
                        return node;
                    }
                } catch (final NotFoundException e) {
                    ModelGraphFactory.LOGGER.error(
                            "Tried to delete a relationship which has already been removed. id {} and exception {}",
                            relationship.getId(), e);
                }
            }
        }

        return null;
    }

    /**
     * Get the EMF URI of the given node.
     *
     * @param node
     *            the node
     * @return returns the URI
     */
    public static URI getUri(final Node node) {
        return URI.createURI(node.getProperty(ModelGraphFactory.EMF_INTERNAL_ID).toString());
    }

    /**
     * Tests whether a given node is a proxy node.
     *
     * @param node
     *            the node
     * @return returns true if the node is a proxy node
     */
    public static boolean isProxyNode(final Node node) {
        return (Boolean) node.getProperty(ModelGraphFactory.PROXY_OBJECT);
    }

    /**
     * Tests whether a given object is represented by a proxy node.
     *
     * @param graphDatabaseService
     *            database service
     * @param object
     *            the object to check
     *
     * @return returns true if the node is a proxy node
     *
     * @throws DBException
     *             on missing node in database
     */
    public static boolean isProxyNode(final GraphDatabaseService graphDatabaseService, final EObject object)
            throws DBException {
        final Node node = ModelGraphFactory.findNode(graphDatabaseService, object);
        if (node != null) {
            return ModelGraphFactory.isProxyNode(node);
        } else {
            throw new DBException(String.format(
                    "Object of type '%s' with internal id %d has no database representation.",
                    ModelGraphFactory.fqnClassName(object.eClass()), ModelGraphFactory.getIdentification(object)));
        }

    }

    /**
     * Find the node representing the object.
     *
     * @param graphDatabaseService
     *            database service
     * @param object
     *            the object to look for
     * @return returns the corresponding node
     * @throws DBException
     *             if the object is not persistent in the database.
     */
    public static Node findNode(final GraphDatabaseService graphDatabaseService, final EObject object)
            throws DBException {
        System.err.println("findNode");
        final String typeName = ModelGraphFactory.fqnClassName(object.eClass());
        final String internalId = ModelGraphFactory.getIdentification(object);

        if (internalId != null) {
            final ResourceIterator<Node> iter = graphDatabaseService.findNodes(Label.label(typeName));
            while (iter.hasNext()) {
                final Node node = iter.next();
                System.err.println("findNode Nodes " + node.getProperty(ModelGraphFactory.EMF_INTERNAL_ID));
            }

            return graphDatabaseService.findNode(Label.label(typeName), ModelGraphFactory.EMF_INTERNAL_ID, internalId);
        } else {
            throw new DBException(String
                    .format("Object of type '%s' has no internal id which is required for a node test.", typeName));
        }
    }

    public static String getIdentification(final EObject object) {
        String internalId = EcoreUtil.getIdentification(object).toLowerCase(); // NOPMD

        if (object.eIsProxy()) {
            internalId = internalId.replaceAll("^.*\\{(.*\\{.*\\})\\}.*$", "$1");
        }

        return internalId;
    }

    /**
     * Delete nodes from a partition which do no longer represent an object.
     *
     * @param root
     *            root node.
     * @param objects
     *            objects belonging to the same partition
     * @throws DBException
     *             on various database exceptions
     */
    public static void deleteNodePartitionRecursively(final Node root, final List<EObject> objects) throws DBException {
        for (final Relationship relationship : root.getRelationships(EMFRelationshipType.CONTAINS,
                Direction.OUTGOING)) {
            final Node targetNode = relationship.getEndNode();
            ModelGraphFactory.deleteNodePartitionRecursively(targetNode, objects);

            if (!ModelGraphFactory.isProxyNode(targetNode)) {
                final EObject targetObject = ModelGraphFactory.findObject(objects,
                        (String) targetNode.getProperty(ModelGraphFactory.EMF_INTERNAL_ID));
                if (targetObject == null) {
                    ModelGraphFactory.deleteLinkedProxyNodes(targetNode);
                    targetNode.delete();
                }
            }
        }
    }

    /**
     * Delete proxy nodes connected to the sourceNode, iff they have only the relationship to the
     * sourceNode. Else delete the relationship.
     *
     * @param sourceNode
     *            source node
     * @throws DBException
     *             on db errors
     */
    private static void deleteLinkedProxyNodes(final Node sourceNode) throws DBException {
        for (final Relationship relationship : sourceNode.getRelationships(Direction.OUTGOING)) {
            final Node targetNode = relationship.getEndNode();
            if (ModelGraphFactory.isProxyNode(targetNode)) {
                final Iterator<Relationship> proxyRelationships = targetNode.getRelationships(Direction.INCOMING)
                        .iterator();
                /** check for more than one incoming. */
                if (proxyRelationships.hasNext()) {
                    proxyRelationships.next();
                    if (proxyRelationships.hasNext()) {
                        /** two incoming, remove just the relationship. */
                        relationship.delete();
                    } else {
                        /** one relationship; delete relationship and node. */
                        relationship.delete();
                        targetNode.delete();
                    }
                } else {
                    throw new DBException(
                            "There is an relationship form a -> b, but b has no incoming relationship. Consistency error.");
                }
            }
        }
    }

    private static EObject findObject(final List<EObject> objects, final String id) {
        for (final EObject object : objects) {
            final String objectId = ModelGraphFactory.getIdentification(object);
            if (objectId.equals(id)) {
                return object;
            }
        }
        return null;
    }

    /**
     * Find the corresponding reference in EMF to a relationship (instance) in the neo4j graph.
     *
     * @param sourceObject
     *            source object having the reference
     * @param relationship
     *            the relationship
     * @return returns the correct EReference
     * @throws DBException
     *             when the relationship is not matched by a reference, which must not happen
     */
    public static EReference findReference(final EObject sourceObject, final Relationship relationship)
            throws DBException {
        final String relationshipName = (String) relationship.getProperty(ModelProviderUtil.REF_NAME);
        for (final EReference reference : sourceObject.eClass().getEAllReferences()) {
            if (reference.getName().equals(relationshipName)) {
                return reference;
            }
        }

        throw new DBException(String.format("Relationship '%s' does not correspond to a reference in class '%s'",
                relationshipName, sourceObject.eClass().getName()));
    }

}
