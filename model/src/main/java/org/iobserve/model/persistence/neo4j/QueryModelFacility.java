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

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
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

/**
 * @author Reiner Jung
 *
 */
public class QueryModelFacility extends GenericModelFacility {

    private final Set<EFactory> factories;

    /**
     * Create a query model facility.
     *
     * @param modelResource
     *            parent object reference to the resource
     *
     * @param graphDatabaseService
     *            data base service
     * @param objectNodeMap
     *            object node map
     * @param factories
     *            factories of the metamodel or partition
     */
    public QueryModelFacility(final ModelResource modelResource, final GraphDatabaseService graphDatabaseService,
            final Map<EObject, Node> objectNodeMap, final Set<EFactory> factories) {
        super(modelResource, graphDatabaseService, objectNodeMap);
        this.factories = factories;
    }

    /**
     * Helper method for reading: Starting from a given node this method recursively reads all
     * contained successor nodes and instantiates the correspondent EObjects. Calls to this method
     * have to be performed from inside a {@link Transaction}.
     *
     * @param parentNode
     *            The node to start with
     * @param relationshipType
     *            this is either CONTAINS or REFERENCES
     * @param nodesToCreatedObjects
     *            Initially empty map of nodes to already created objects to make sure that objects
     *            are instantiated just once
     * @param <T>
     *            type definition
     * @return The root
     */
    @SuppressWarnings("unchecked")
    public <T extends EObject> T readNodes(final Node parentNode, final Map<Node, EObject> nodesToCreatedObjects,
            final EMFRelationshipType... relationshipType) {
        if (!nodesToCreatedObjects.containsKey(parentNode)) {
            final T parentObject = (T) ModelObjectFactory.createObject(parentNode, this.factories);

            // Already register unfinished components because there might be circles
            nodesToCreatedObjects.putIfAbsent(parentNode, parentObject);

            this.loadRelatedNodes(parentNode, relationshipType, nodesToCreatedObjects);

            return parentObject;
        } else {
            return (T) nodesToCreatedObjects.get(parentNode);
        }
    }

    /**
     * Load related nodes representing referenced objects.
     *
     * @param sourceNode
     *            the corresponding node to the component
     * @param nodesToCreatedObjects
     *            additional nodes
     */
    private void loadRelatedNodes(final Node sourceNode, final EMFRelationshipType[] relationshipType,
            final Map<Node, EObject> nodesToCreatedObjects) {
        for (final Relationship relationship : ModelProviderUtil
                .sortRelsByPosition(sourceNode.getRelationships(Direction.OUTGOING, relationshipType))) {
            final Node targetNode = relationship.getEndNode();

            if (!nodesToCreatedObjects.containsKey(targetNode)) {
                final EObject targetObject;
                if (ModelGraphFactory.isProxyNode(targetNode)) {
                    targetObject = ModelObjectFactory.createProxyObject(targetNode, this.factories);
                    nodesToCreatedObjects.put(targetNode, targetObject);
                } else {
                    targetObject = ModelObjectFactory.createObject(targetNode, this.factories);
                    nodesToCreatedObjects.put(targetNode, targetObject);
                    this.loadRelatedNodes(targetNode, relationshipType, nodesToCreatedObjects);
                }
            }
        }
    }

    /**
     * Resolve all references for all objects stored in the map.
     *
     * @param nodeObjectMap
     *            node object map
     */
    public void resolveReferences(final Map<Node, EObject> nodeObjectMap) {
        for (final Entry<Node, EObject> entry : nodeObjectMap.entrySet()) {
            final EObject object = entry.getValue(); // TODO only non proxy objects
            // if ()
            for (final EReference reference : object.eClass().getEAllReferences()) {
                final Stream<Relationship> sortedRelationships = StreamSupport
                        .stream(entry.getKey().getRelationships(Direction.OUTGOING).spliterator(), false)
                        .filter(r -> r.getProperty(ModelProviderUtil.REF_NAME).equals(reference.getName()))
                        .sorted(new Comparator<Relationship>() {

                            @Override
                            public int compare(final Relationship a, final Relationship b) {
                                return (Integer) a.getProperty(ModelProviderUtil.REF_POS)
                                        - (Integer) b.getProperty(ModelProviderUtil.REF_POS);
                            }

                        });
                sortedRelationships // TODO here we would also need referenced objects.
                        .forEach(r -> this.updateReference(object, reference, nodeObjectMap.get(r.getEndNode())));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void updateReference(final EObject sourceObject, final EReference reference, final EObject targetObject) {
        if (reference.isMany()) {
            ((EList<EObject>) sourceObject.eGet(reference)).add(targetObject);
        } else {
            if (reference.isChangeable()) {
                sourceObject.eSet(reference, targetObject);
            }
        }
    }

    /**
     * Recursively returns all containments and data types of a given component.
     *
     * @param object
     *            The component to start with
     * @param containmentsAndDatatypes
     *            Initially empty set of all containments and data types
     * @return The passed containmentsAndDatatypes set now filled with containments and data types
     */
    private Set<EObject> getAllContainmentsByObject(final EObject object, final Set<EObject> containmentsAndDatatypes) {
        if (!containmentsAndDatatypes.contains(object)) {
            containmentsAndDatatypes.add(object);

            for (final EReference reference : object.eClass().getEAllReferences()) {
                final Object referencedEntity = object.eGet(reference);
                if (reference.isMany()) {
                    this.checkMultipleReferences(reference, (EList<?>) referencedEntity, containmentsAndDatatypes);
                } else {
                    this.checkSingleReferences(reference, (EObject) referencedEntity, containmentsAndDatatypes);
                }
            }
        }

        return containmentsAndDatatypes;
    }

    private void checkMultipleReferences(final EReference reference, final EList<?> referencedEntities,
            final Set<EObject> containmentsAndDatatypes) {
        for (final Object referencedElement : referencedEntities) {
            if (reference.isContainment() || ModelProviderUtil.isDatatype(reference, referencedElement)) {
                this.getAllContainmentsByObject((EObject) referencedElement, containmentsAndDatatypes);
            }
        }
    }

    private void checkSingleReferences(final EReference reference, final EObject referencedEntity,
            final Set<EObject> containmentsAndDatatypes) {
        if ((referencedEntity != null)
                && (reference.isContainment() || ModelProviderUtil.isDatatype(reference, referencedEntity))) {
            this.getAllContainmentsByObject(referencedEntity, containmentsAndDatatypes);
        }
    }

    /**
     * Helper method for reading: Recursively returns all containments and data types of a given
     * node.
     *
     * @param node
     *            The node to start with
     * @param nodes
     *            Initially empty set of all containments and data types
     * @return The passed containmentsAndDatatypes set now filled with containments and data types
     */
    public Set<Node> getAllContainmentNodes(final Node node, final Set<Node> nodes) {
        if (!nodes.contains(node)) {
            nodes.add(node);

            for (final Relationship rel : node.getRelationships(Direction.OUTGOING, EMFRelationshipType.CONTAINS,
                    EMFRelationshipType.IS_TYPE)) {
                this.getAllContainmentNodes(rel.getEndNode(), nodes);
            }
        }

        return nodes;
    }

    /**
     * Read the object of the given and all contained objects.
     *
     * @param node
     *            the object identifying node
     *
     * @param <T>
     *            type of the root node
     * @return the object for the node
     */
    @SuppressWarnings("unchecked")
    public <T> T readContainedNodes(final Node node) {
        return (T) this.readNodes(node, new HashMap<Node, EObject>(), EMFRelationshipType.CONTAINS);
    }

    /**
     * Find the corresponding node.
     *
     * @param uri
     *            node uri
     * @return the corresponding node
     * @throws DBException
     *             on unresolvable uri
     */
    public Node getNodeByUri(final URI uri) throws DBException {
        final Label rootLabel = Label.label(uri.authority());
        final ResourceIterator<Node> nodes = this.graphDatabaseService.findNodes(rootLabel);

        if (nodes.hasNext()) {
            Node node = nodes.next();
            if (uri.segments().length > 0) {
                for (final String segment : uri.segments()) {
                    final String segmentName;
                    final long segmentIndex;
                    final int index = segment.indexOf('[');
                    if (index == -1) {
                        segmentName = segment;
                        segmentIndex = 0;
                    } else {
                        segmentName = segment.substring(0, index);
                        segmentIndex = Long.parseLong(segment.substring(index + 1, segment.length() - 1));
                    }
                    boolean next = false;
                    for (final Relationship relationship : node.getRelationships(Direction.OUTGOING)) {
                        if (segmentName.equals(relationship.getProperty(ModelProviderUtil.REF_NAME))
                                && (segmentIndex == (Integer) relationship.getProperty(ModelProviderUtil.REF_POS))) {
                            node = relationship.getEndNode();
                            next = true;
                            break;
                        }
                    }
                    if (!next) {
                        throw new DBException("Reference not found " + segment);
                    }
                }
            }

            return node;
        } else {
            throw new DBException("URI does not specify a node " + uri.toString());
        }
    }

}
