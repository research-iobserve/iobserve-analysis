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
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.BasicEObjectImpl;
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
     * @param caller
     *            caller class
     * @param title
     *            title of the partition
     * @param object
     *            root element of the partition
     */
    public static void printModelPartition(final Class<?> caller, final String title, final EObject object) {
        DebugHelper.LOGGER.debug("{} model partition {}", caller, title);
        if (object != null) {
            DebugHelper.printModelPartition(caller, object, "");
        } else {
            DebugHelper.LOGGER.debug("{} Object in NULL", caller);
        }
    }

    private static void printModelPartition(final Class<?> caller, final EObject object, final String indent) {
        DebugHelper.LOGGER.debug(String.format("%s %sclass %s : %s", caller, indent, object.hashCode(),
                object.getClass().getCanonicalName()));
        DebugHelper.printObjectAttributes(caller, object, indent);
        DebugHelper.printObjectReferences(caller, object, indent);
    }

    private static void printObjectAttributes(final Class<?> caller, final EObject object, final String indent) {
        for (final EAttribute attribute : object.eClass().getEAllAttributes()) {
            final Object value = object.eGet(attribute);
            DebugHelper.LOGGER.debug(String.format("%s %s - %s = %s : %s", caller, indent, value, attribute.getName(),
                    attribute.getEType().getInstanceTypeName()));
        }
    }

    private static void printObjectReferences(final Class<?> caller, final EObject object, final String indent) {
        for (final EReference reference : object.eClass().getEAllReferences()) {
            DebugHelper.LOGGER.debug(String.format("%s %s + %s : %s", caller, indent, reference.getName(),
                    reference.getEReferenceType().getName()));
            if (reference.isContainment()) {
                DebugHelper.printContainmentReferenceContent(caller, object.eGet(reference), indent);
            } else {
                DebugHelper.printReferences(caller, object.eGet(reference), reference.isContainer(), indent);
            }
        }

    }

    private static void printReferences(final Class<?> caller, final Object contents, final boolean container,
            final String indent) {
        final String refType = container ? "parent" : "ref";
        if (contents != null) {
            if (contents instanceof EList<?>) {
                for (final Object content : (EList<?>) contents) {
                    if (((EObject) content).eIsProxy()) {
                        DebugHelper.LOGGER
                                .debug(String.format("%s %s\t%s proxy %s\n", caller, indent, refType, content));
                    } else {
                        DebugHelper.LOGGER
                                .debug(String.format("%s %s\t%s %s", caller, indent, refType, content.hashCode()));
                    }
                }
            } else {
                DebugHelper.LOGGER.debug(String.format("%s %s\t%s %s", caller, indent, refType, contents.hashCode()));
            }
        }
    }

    private static void printContainmentReferenceContent(final Class<?> caller, final Object contents,
            final String indent) {
        if (contents != null) {
            if (contents instanceof EList<?>) {
                for (final Object content : (EList<?>) contents) {
                    DebugHelper.printModelPartition(caller, (EObject) content, indent + "\t");
                }
            } else {
                DebugHelper.printModelPartition(caller, (EObject) contents, indent + "\t");
            }
        }

    }

    /**
     * List all {@link AllocationContext}s of an {@link Allocation} model.
     *
     * @param caller
     *            caller class
     * @param label
     *            prefix label
     * @param allocation
     *            allocation model
     */
    public static void listAllocations(final Class<?> caller, final String label, final Allocation allocation) {
        int i = 1;
        for (final AllocationContext context : allocation.getAllocationContexts_Allocation()) {
            DebugHelper.LOGGER.debug(String.format("%s %s %d %s %s", caller, label, i, context.getEntityName(),
                    context.getAssemblyContext_AllocationContext()));
            i++;
        }
    }

    /**
     * List all relationships.
     *
     * @param caller
     *            caller class
     * @param relationships
     *            the list of relationships
     */
    public static void printRelationshipList(final Class<?> caller, final Iterable<Relationship> relationships) {
        for (final Relationship relationship : relationships) {
            DebugHelper.LOGGER.debug(String.format("%s rel %d %d->%d", caller, relationship.getId(),
                    relationship.getStartNode().getId(), relationship.getEndNode().getId()));
            for (final Entry<String, Object> property : relationship.getAllProperties().entrySet()) {
                DebugHelper.LOGGER.debug(String.format("%s \t %s %s", caller, property.getKey(), property.getValue()));
            }
        }
    }

    /**
     * Print an object's id and entityName if available.
     *
     * @param caller
     *            caller class
     * @param object
     *            the object
     */
    public static void printObjectIdAndName(final Class<?> caller, final EObject object) {
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
        DebugHelper.LOGGER.debug(String.format("%s Object id: %s  name: %s", caller, id, entityName));

    }

    /**
     * Get the proxy uri.
     *
     * @param object
     *            the object for which the proxy uri is determined.
     * @return returns the object's proxy uri
     */
    public static String getProxyURI(final EObject object) {
        final URI uri = ((BasicEObjectImpl) object).eProxyURI();
        if (uri != null) {
            return uri.toString();
        } else {
            return "no URI";
        }
    }

    /**
     * List all relationships in a graph model.
     *
     * @param caller
     *            caller class
     * @param resource
     *            the graph
     */
    public static void listAllRelationships(final Class<?> caller, final ModelResource<?> resource) {
        for (final Relationship r : resource.getGraphDatabaseService().getAllRelationships()) {
            DebugHelper.LOGGER.debug(
                    String.format("%s \t%d -- (%d) --> %d", caller, r.getStartNodeId(), r.getId(), r.getEndNodeId()));
        }
    }

    /**
     * Print a map.
     *
     * @param caller
     *            caller class
     * @param label
     *            header label
     * @param map
     *            map to be printed
     */
    public static void printMap(final Class<?> caller, final String label, final Map<EObject, Node> map) {
        DebugHelper.LOGGER.debug(String.format("%s Print Map %s", caller, label));
        for (final Entry<EObject, Node> entry : map.entrySet()) {
            DebugHelper.LOGGER.debug(String.format("%s \t%s = %d %s", caller, entry.getKey(), entry.getValue().getId(),
                    String.valueOf(ModelGraphFactory.isProxyNode(entry.getValue()))));
        }
    }

    /**
     * Print list of elements.
     *
     * @param caller
     *            caller class
     * @param label
     *            label of the list
     * @param list
     *            the list itself
     */
    public static void printList(final Class<?> caller, final String label, final EList<?> list) {
        DebugHelper.LOGGER.debug(String.format("%s Print List %s", caller, label));
        for (final Object entry : list) {
            DebugHelper.LOGGER.debug(String.format("%s \t%s", caller, entry));
        }
    }

    /**
     * print the node model.
     *
     * @param caller
     *            caller class
     * @param objectNodeMap
     *            map of objects to nodes
     * @param object
     *            root object
     * @param <T>
     *            type parameter
     */
    public static <T extends EObject> void printNodeModel(final Class<?> caller, final Map<EObject, Node> objectNodeMap,
            final T object) {
        DebugHelper.LOGGER.debug(String.format("%s Print Node Model %s", caller, object.toString()));
        DebugHelper.printNodeModel(caller, "", objectNodeMap.get(object));
    }

    private static <T extends EObject> void printNodeModel(final Class<?> caller, final String indent,
            final Node node) {
        if (node == null) {
            DebugHelper.LOGGER.debug(String.format("%s %sno node for object", caller, indent));
            return;
        }
        DebugHelper.LOGGER.debug(
                String.format("%s %s%s : %d", caller, indent, node.getLabels().iterator().next().name(), node.getId()));
        for (final Entry<String, Object> entry : node.getAllProperties().entrySet()) {
            DebugHelper.LOGGER.debug(
                    String.format("%s %s - %s = %s", caller, indent, entry.getKey(), entry.getValue().toString()));
        }
        for (final Relationship rel : node.getRelationships(Direction.OUTGOING)) {
            if (rel.isType(EMFRelationshipType.CONTAINS)) {
                DebugHelper.LOGGER.debug(
                        String.format("%s %s + %s", caller, indent, rel.getProperty(ModelProviderUtil.REF_NAME)));
                DebugHelper.printNodeModel(caller, indent + "   \t", rel.getEndNode());
            } else {
                final Node endNode = rel.getEndNode();
                DebugHelper.LOGGER.debug(String.format("%s %s + %s -> %s : %s", caller, indent,
                        rel.getProperty(ModelProviderUtil.REF_NAME), endNode.getLabels().iterator().next().name(),
                        endNode.getId()));
            }
        }
    }

}
