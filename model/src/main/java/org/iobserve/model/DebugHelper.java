/***************************************************************************
 * Copyright 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.model;

import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.iobserve.model.persistence.neo4j.EMFRelationshipType;
import org.iobserve.model.persistence.neo4j.ModelGraphFactory;
import org.iobserve.model.persistence.neo4j.ModelProviderUtil;
import org.iobserve.model.persistence.neo4j.ModelResource;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Reiner Jung
 *
 */
public final class DebugHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DebugHelper.class);

    private DebugHelper() {
        // helper class
    }

    /**
     * Print the complete partition of the given object.
     *
     * @param object
     *            root element of the partition
     */
    public static void printModelPartition(final EObject object) {
        DebugHelper.printModelPartition(object, "");
    }

    private static void printModelPartition(final EObject object, final String indent) {
        DebugHelper.LOGGER.debug(
                String.format("%sclass %s : %s", indent, object.hashCode(), object.getClass().getCanonicalName()));
        DebugHelper.printObjectAttributes(object, indent);
        DebugHelper.printObjectReferences(object, indent);
    }

    private static void printObjectReferences(final EObject object, final String indent) {
        for (final EReference reference : object.eClass().getEAllReferences()) {
            DebugHelper.LOGGER.debug(String.format("%s + %s : %s", indent, reference.getName(),
                    reference.getEReferenceType().getName()));
            if (reference.isContainment()) {
                DebugHelper.printContainmentReferenceContent(object.eGet(reference), indent);
            } else {
                DebugHelper.printReferences(object.eGet(reference), reference.isContainer(), indent);
            }
        }

    }

    private static void printReferences(final Object contents, final boolean container, final String indent) {
        final String refType = container ? "parent" : "ref";
        if (contents != null) {
            if (contents instanceof EList<?>) {
                for (final Object content : (EList<?>) contents) {
                    if (((EObject) content).eIsProxy()) {
                        DebugHelper.LOGGER.debug(String.format("%s\t%s proxy %s\n", indent, refType, content));
                    } else {
                        DebugHelper.LOGGER.debug(String.format("%s\t%s %s", indent, refType, content.hashCode()));
                    }
                }
            } else {
                DebugHelper.LOGGER.debug(String.format("%s\t%s %s", indent, refType, contents.hashCode()));
            }
        }
    }

    private static void printContainmentReferenceContent(final Object contents, final String indent) {
        if (contents != null) {
            if (contents instanceof EList<?>) {
                for (final Object content : (EList<?>) contents) {
                    DebugHelper.printModelPartition((EObject) content, indent + "\t");
                }
            } else {
                DebugHelper.printModelPartition((EObject) contents, indent + "\t");
            }
        }

    }

    private static void printObjectAttributes(final EObject object, final String indent) {
        for (final EAttribute attribute : object.eClass().getEAllAttributes()) {
            final Object value = object.eGet(attribute);
            DebugHelper.LOGGER.debug(String.format("%s - %s = %s : %s", indent, value, attribute.getName(),
                    attribute.getEType().getInstanceTypeName()));
        }
    }

    /**
     * List all {@link AllocationContext}s of an {@link Allocation} model.
     *
     * @param label
     *            prefix label
     * @param allocation
     *            allocation model
     */
    public static void listAllocations(final String label, final Allocation allocation) {
        int i = 1;
        for (final AllocationContext context : allocation.getAllocationContexts_Allocation()) {
            DebugHelper.LOGGER.debug(String.format("%s %d %s %s", label, i, context.getEntityName(),
                    context.getAssemblyContext_AllocationContext()));
            i++;
        }
    }

    /**
     * List all relationships.
     *
     * @param relationships
     *            the list of relationships
     */
    public static void printRelationshipList(final Iterable<Relationship> relationships) {
        for (final Relationship relationship : relationships) {
            DebugHelper.LOGGER.debug(String.format("rel %d %d->%d", relationship.getId(),
                    relationship.getStartNode().getId(), relationship.getEndNode().getId()));
            for (final Entry<String, Object> property : relationship.getAllProperties().entrySet()) {
                DebugHelper.LOGGER.debug(String.format("\t %s %s", property.getKey(), property.getValue()));
            }
        }
    }

    /**
     * Print an object's id and entityName if available.
     *
     * @param object
     *            the object
     */
    public static void printObjectIdAndName(final EObject object) {
        String entityName = "<none>";
        String id = "<none>";
        for (final EAttribute attribute : object.eClass().getEAllAttributes()) {
            if ("entityName".equals(attribute.getName())) {
                entityName = object.eGet(attribute).toString();
            }
            if ("id".equals(attribute.getName())) {
                id = object.eGet(attribute).toString();
            }
        }
        DebugHelper.LOGGER.debug(String.format("Object id: %s  name: %s", id, entityName));

    }

    /**
     * List all relationships in a graph model.
     *
     * @param resource
     *            the graph
     */
    public static void listAllRelationships(final ModelResource<?> resource) {
        for (final Relationship r : resource.getGraphDatabaseService().getAllRelationships()) {
            DebugHelper.LOGGER
                    .debug(String.format("\t%d -- (%d) --> %d", r.getStartNodeId(), r.getId(), r.getEndNodeId()));
        }
    }

    /**
     * Print a map.
     *
     * @param label
     *            header label
     * @param map
     *            map to be printed
     */
    public static void printMap(final String label, final Map<EObject, Node> map) {
        DebugHelper.LOGGER.debug(String.format("Map %s", label));
        for (final Entry<EObject, Node> entry : map.entrySet()) {
            DebugHelper.LOGGER.debug(String.format("\t%s = %d %s", entry.getKey(), entry.getValue().getId(),
                    String.valueOf(ModelGraphFactory.isProxyNode(entry.getValue()))));
        }
    }

    public static void printList(final String label, final EList<?> list) {
        DebugHelper.LOGGER.debug(String.format("List %s", label));
        for (final Object entry : list) {
            DebugHelper.LOGGER.debug(String.format("\t%s", entry));
        }
    }

    public static <T extends EObject> void printNodeModel(final Map<EObject, Node> objectNodeMap, final T object) {
        DebugHelper.LOGGER.debug(String.format(">> %s", object.toString()));
        DebugHelper.printNodeModel("", objectNodeMap.get(object));
    }

    private static <T extends EObject> void printNodeModel(final String indent, final Node node) {
        if (node == null) {
            DebugHelper.LOGGER.debug(String.format("%sno node for object", indent));
            return;
        }
        DebugHelper.LOGGER
                .debug(String.format("%s%s : %d", indent, node.getLabels().iterator().next().name(), node.getId()));
        for (final Entry<String, Object> entry : node.getAllProperties().entrySet()) {
            DebugHelper.LOGGER
                    .debug(String.format("%s - %s = %s", indent, entry.getKey(), entry.getValue().toString()));
        }
        for (final Relationship rel : node.getRelationships(Direction.OUTGOING)) {
            if (rel.isType(EMFRelationshipType.CONTAINS)) {
                DebugHelper.LOGGER.debug(String.format("%s + %s", indent, rel.getProperty(ModelProviderUtil.REF_NAME)));
                DebugHelper.printNodeModel(indent + "   \t", rel.getEndNode());
            } else {
                final Node endNode = rel.getEndNode();
                DebugHelper.LOGGER
                        .debug(String.format("%s + %s -> %s : %s", indent, rel.getProperty(ModelProviderUtil.REF_NAME),
                                endNode.getLabels().iterator().next().name(), endNode.getId()));
            }
        }
    }

}
