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

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.BasicEObjectImpl;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Reiner Jung
 *
 */
public final class ModelGraphFactory {

    public static final String INTERNAL_ID = ":internalId";
    public static final String PROXY_OBJECT = ":proxyObject";

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelGraphFactory.class);

    private static final String EMF_URI = ":emfUri";

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
     *            db id
     * @return returns the created node
     */
    public static Node createNode(final GraphDatabaseService graphDatabaseService, final EObject storeableObject,
            final long id) {
        final Label typeName = Label.label(ModelGraphFactory.fqnClassName(storeableObject.eClass()));

        final Node node = graphDatabaseService.createNode(typeName);
        node.setProperty(ModelGraphFactory.INTERNAL_ID, id);

        /** set object uri. */
        ModelGraphFactory.setNodeObjectUri(node, storeableObject);

        node.setProperty(ModelGraphFactory.PROXY_OBJECT, false);

        /** attributes. */
        ModelGraphFactory.storeAttributes(node, storeableObject);

        ModelGraphFactory.LOGGER.debug(String.format("create node %s %d", storeableObject.toString(), node.getId()));

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
            final long id) {
        final Label typeName = Label.label(ModelGraphFactory.fqnClassName(storeableObject.eClass()));

        final Node node = graphDatabaseService.createNode(typeName);
        node.setProperty(ModelGraphFactory.INTERNAL_ID, id);

        ModelGraphFactory.setNodeObjectUri(node, storeableObject);

        node.setProperty(ModelGraphFactory.PROXY_OBJECT, true);

        ModelGraphFactory.LOGGER.debug(String.format("create proxy %s %d", storeableObject.toString(), node.getId()));

        return node;
    }

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
     * Create an object URI for a given object and set the corresponding node property.
     *
     * @param node
     *            the node
     * @param storeableObject
     *            the object
     */
    private static void setNodeObjectUri(final Node node, final EObject storeableObject) {
        final URI uri = ((BasicEObjectImpl) storeableObject).eProxyURI();
        if (uri == null) {
            node.setProperty(ModelGraphFactory.EMF_URI, ModelGraphFactory.createUri(storeableObject));
        } else {
            node.setProperty(ModelGraphFactory.EMF_URI, uri.toString());
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
     * @param sourceNode
     *            source node
     * @param targetNode
     *            target node
     * @param targetObject
     *            target object
     * @param reference
     *            corresponding reference
     * @param i
     *            position info for lists
     */
    public static void createRelationship(final Node sourceNode, final Node targetNode, final EObject targetObject,
            final EReference reference, final int i) {
        final Relationship relationship = sourceNode.createRelationshipTo(targetNode,
                ModelProviderUtil.getRelationshipType(reference, targetObject));
        relationship.setProperty(ModelProviderUtil.REF_NAME, reference.getName());
        relationship.setProperty(ModelProviderUtil.REF_POS, i);
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
                    final String nodeUri = node.getProperty(ModelGraphFactory.EMF_URI).toString();

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
        return URI.createURI(node.getProperty(ModelGraphFactory.EMF_URI).toString());
    }

    /**
     * Returns a URI based on the objects containing the passed object.
     *
     * @param object
     *            The component to compute a URI to
     * @return The URI
     */
    public static String createUri(final EObject object) {
        if (object.eContainer() != null) {
            return ModelGraphFactory.createUri(object.eContainer()) + "/" + ModelGraphFactory.createUriFragment(object);
        } else {
            return "neo4j://" + ModelGraphFactory.fqnClassName(object.eClass());
        }
    }

    private static String createUriFragment(final EObject object) {
        final EObject container = object.eContainer();
        for (final EReference reference : container.eClass().getEAllContainments()) {
            if (reference.isMany()) {
                final EList<?> manyReference = (EList<?>) container.eGet(reference);
                int i = 0;
                for (final Object element : manyReference) {
                    if (element.equals(object)) {
                        return reference.getName() + "[" + i + "]";
                    }
                    i++;
                }
            } else {
                if (container.eGet(reference).equals(object)) {
                    return reference.getName();
                }
            }
        }

        throw new InternalError("Object is contained but cannot be found in containing class.");
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
}
