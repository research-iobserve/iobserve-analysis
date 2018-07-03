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

import java.util.Map.Entry;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.iobserve.model.persistence.neo4j.ModelGraph;
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
        System.err.printf("%sclass %s : %s\n", indent, object.hashCode(), object.getClass().getCanonicalName());
        for (final EAttribute attribute : object.eClass().getEAllAttributes()) {
            final Object value = object.eGet(attribute);
            System.err.printf("%s - %s = %s : %s\n", indent, value, attribute.getName(),
                    attribute.getEType().getInstanceTypeName());
        }
        for (final EReference reference : object.eClass().getEAllReferences()) {
            System.err.printf("%s + %s : %s\n", indent, reference.getName(), reference.getEReferenceType().getName());
            if (reference.isContainment()) {
                final Object contents = object.eGet(reference);
                if (contents != null) {
                    if (contents instanceof EList<?>) {
                        for (final Object content : (EList<?>) contents) {
                            DebugHelper.printModelPartition((EObject) content, indent + "\t");
                        }
                    } else {
                        DebugHelper.printModelPartition((EObject) contents, indent + "\t");
                    }
                }
            } else {
                final String refType = reference.isContainer() ? "parent" : "ref";

                final Object contents = object.eGet(reference);
                if (contents != null) {
                    if (contents instanceof EList<?>) {
                        for (final Object content : (EList<?>) contents) {
                            System.err.printf("%s\t%s %s\n", indent, refType, content.hashCode());
                        }
                    } else {
                        System.err.printf("%s\t%s %s\n", indent, refType, contents.hashCode());
                    }
                }
            }
        }
    }

    public static void listAllocations(final String label, final Allocation allocation) {
        int i = 1;
        for (final AllocationContext context : allocation.getAllocationContexts_Allocation()) {
            java.lang.System.err.println(label + " " + i + " " + context.getEntityName() + " "
                    + context.getAssemblyContext_AllocationContext());
            i++;
        }
    }

    public static void printList(final Iterable<Relationship> relationships) {
        for (final Relationship relationship : relationships) {
            System.err.println(String.format("rel %d %d->%d", relationship.getId(), relationship.getStartNode().getId(),
                    relationship.getEndNode().getId()));
            for (final Entry<String, Object> property : relationship.getAllProperties().entrySet()) {
                System.err.println(String.format("\t %s %s", property.getKey(), property.getValue()));
            }
        }
    }

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
        System.err.println(String.format("Object id: %s  name: %s", id, entityName));

    }

    public static void listAllRelationships(final ModelGraph graph) {
        for (final Relationship r : graph.getGraphDatabaseService().getAllRelationships()) {
            System.err.println("\t" + r.getStartNodeId() + " -- (" + r.getId() + ") --> " + r.getEndNodeId());
        }
    }

}
