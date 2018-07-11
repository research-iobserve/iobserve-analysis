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
package org.iobserve.model.test.data;

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

/**
 *
 * @author Reiner Jung
 *
 */
public final class DebugHelper {

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
        System.err.printf("%sclass %s : %s\n", indent, object.hashCode(), object.getClass().getCanonicalName()); // NOPMD
        DebugHelper.printObjectAttributes(object, indent);
        DebugHelper.printObjectReferences(object, indent);
    }

    private static void printObjectReferences(final EObject object, final String indent) {
        for (final EReference reference : object.eClass().getEAllReferences()) {
            System.err.printf("%s + %s : %s\n", indent, reference.getName(), reference.getEReferenceType().getName()); // NOPMD
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
                    System.err.printf("%s\t%s %s\n", indent, refType, content.hashCode()); // NOPMD
                }
            } else {
                System.err.printf("%s\t%s %s\n", indent, refType, contents.hashCode()); // NOPMD
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
            System.err.printf("%s - %s = %s : %s\n", indent, value, attribute.getName(), // NOPMD
                    attribute.getEType().getInstanceTypeName());
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
            java.lang.System.err.printf("%s %d %s %s\n", label, i, context.getEntityName(), // NOPMD
                    context.getAssemblyContext_AllocationContext());
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
            System.err.println(String.format("rel %d %d->%d", relationship.getId(), // NOPMD
                    relationship.getStartNode().getId(), relationship.getEndNode().getId()));
            for (final Entry<String, Object> property : relationship.getAllProperties().entrySet()) {
                System.err.println(String.format("\t %s %s", property.getKey(), property.getValue())); // NOPMD
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
        System.err.println(String.format("Object id: %s  name: %s", id, entityName)); // NOPMD

    }

    /**
     * List all relationships in a graph model.
     *
     * @param resource
     *            the graph
     */
    public static void listAllRelationships(final ModelResource resource) {
        for (final Relationship r : resource.getGraphDatabaseService().getAllRelationships()) {
            System.err.println("\t" + r.getStartNodeId() + " -- (" + r.getId() + ") --> " + r.getEndNodeId()); // NOPMD
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
        System.err.println("Map " + label);
        for (final Entry<EObject, Node> entry : map.entrySet()) {
            System.err.println("\t" + entry.getKey() + " = " + entry.getValue().getId() + " "
                    + ModelGraphFactory.isProxyNode(entry.getValue()));
        }
    }

    public static void printList(final String label, final EList<?> list) {
        System.err.println("List " + label);
        for (final Object entry : list) {
            System.err.println("\t" + entry);
        }
    }

    public static <T extends EObject> void printNodeModel(final Map<EObject, Node> objectNodeMap, final T object) {
        System.err.println(">> " + object);
        DebugHelper.printNodeModel("", objectNodeMap.get(object));
    }

    private static <T extends EObject> void printNodeModel(final String indent, final Node node) {
        if (node == null) {
            System.err.println(indent + "no node for object");
            return;
        }
        System.err.println(indent + node.getLabels().iterator().next().name() + " : " + node.getId());
        for (final Entry<String, Object> entry : node.getAllProperties().entrySet()) {
            System.err.println(indent + " - " + entry.getKey() + " = " + entry.getValue());
        }
        for (final Relationship rel : node.getRelationships(Direction.OUTGOING)) {
            if (rel.isType(EMFRelationshipType.CONTAINS)) {
                System.err.println(indent + " + " + rel.getProperty(ModelProviderUtil.REF_NAME));
                DebugHelper.printNodeModel(indent + "   \t", rel.getEndNode());
            } else {
                final Node endNode = rel.getEndNode();
                System.err.println(indent + " + " + rel.getProperty(ModelProviderUtil.REF_NAME) + " -> "
                        + endNode.getLabels().iterator().next().name() + " : " + endNode.getId());
            }
        }
    }

}
