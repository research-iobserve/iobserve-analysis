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

import java.util.Map.Entry;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Reiner Jung
 *
 */
public final class DBDebugHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBDebugHelper.class);

    private DBDebugHelper() {
        // helper class
    }

    /**
     * Print the complete partition of the given object.
     *
     * @param object
     *            root element of the partition
     */
    public static void printModelPartition(final String header, final EObject object) {
        DBDebugHelper.LOGGER.debug(header);
        DBDebugHelper.printModelPartition(object, "");
    }

    private static void printModelPartition(final EObject object, final String indent) {
        DBDebugHelper.LOGGER.debug("{}class {} : {}", indent, object.hashCode(), object.getClass().getCanonicalName());
        DBDebugHelper.printAttributes(object, indent);
        for (final EReference reference : object.eClass().getEAllReferences()) {
            DBDebugHelper.LOGGER.debug("{} + {} : {}", indent, reference.getName(), reference.getEReferenceType());
            if (reference.isContainment()) {
                DBDebugHelper.printContainment(object.eGet(reference), indent);
            } else {
                DBDebugHelper.printReference(object.eGet(reference), indent);
            }
        }
    }

    private static void printReference(final Object contents, final String indent) {
        if (contents != null) {
            if (contents instanceof EList<?>) {
                for (final Object content : (EList<?>) contents) {
                    DBDebugHelper.LOGGER.debug("{}\tref {}", indent, content.hashCode());
                }
            } else {
                DBDebugHelper.LOGGER.debug("{}\tref {}", indent, contents.hashCode());
            }
        }
    }

    private static void printContainment(final Object contents, final String indent) {
        if (contents != null) {
            if (contents instanceof EList<?>) {
                for (final Object content : (EList<?>) contents) {
                    DBDebugHelper.printModelPartition((EObject) content, indent + "\t");
                }
            } else {
                DBDebugHelper.printModelPartition((EObject) contents, indent + "\t");
            }
        }
    }

    private static void printAttributes(final EObject object, final String indent) {
        for (final EAttribute attribute : object.eClass().getEAllAttributes()) {
            final Object value = object.eGet(attribute);
            if (value != null) {
                DBDebugHelper.LOGGER.debug("{} - {} = {} : {}", indent, value.toString(), attribute.getName(),
                        attribute.getEType().getInstanceTypeName());
            } else {
                DBDebugHelper.LOGGER.debug("{} - NULL = {} : {}", indent, attribute.getName(),
                        attribute.getEType().getInstanceTypeName());
            }
        }
    }

    public static void printResource(final String name, final GraphDatabaseService graphDatabaseService) {
        DBDebugHelper.LOGGER.debug("--- Model {} ---", name);
        try (Transaction tx = graphDatabaseService.beginTx()) {
            for (final Node node : graphDatabaseService.getAllNodes()) {
                DBDebugHelper.LOGGER.debug("Node {} {}", node.getId(), node.getLabels().iterator().next().name());
                for (final Entry<String, Object> p : node.getAllProperties().entrySet()) {
                    DBDebugHelper.LOGGER.debug("\t p {}={}", p.getKey(), p.getValue());
                }
                for (final Relationship r : node.getRelationships(Direction.OUTGOING)) {
                    DBDebugHelper.LOGGER.debug("\t r to {}", r.getEndNodeId());
                    for (final Entry<String, Object> p : r.getAllProperties().entrySet()) {
                        DBDebugHelper.LOGGER.debug("\t\t p {}={}", p.getKey(), p.getValue());
                    }
                }
            }
            tx.success();
        }
    }
}
