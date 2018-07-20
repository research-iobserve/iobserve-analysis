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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
     * @param caller
     *            caller class
     * @param header
     *            title label
     * @param object
     *            root element of the partition
     */
    public static void printModelPartition(final Class<?> caller, final String header, final EObject object) {
        DBDebugHelper.LOGGER.debug(header);
        DBDebugHelper.printModelPartition(caller, object, "");
    }

    private static void printModelPartition(final Class<?> caller, final EObject object, final String indent) {
        DBDebugHelper.LOGGER.debug("{} {}class {} : {}", caller, indent, object.hashCode(),
                object.getClass().getCanonicalName());
        DBDebugHelper.printAttributes(caller, object, indent);
        for (final EReference reference : object.eClass().getEAllReferences()) {
            DBDebugHelper.LOGGER.debug("{} {} + {} : {}", caller, indent, reference.getName(),
                    reference.getEReferenceType());
            if (reference.isContainment()) {
                DBDebugHelper.printContainment(caller, object.eGet(reference), indent);
            } else {
                DBDebugHelper.printReference(caller, object.eGet(reference), indent);
            }
        }
    }

    private static void printAttributes(final Class<?> caller, final EObject object, final String indent) {
        for (final EAttribute attribute : object.eClass().getEAllAttributes()) {
            final Object value = object.eGet(attribute);
            if (value != null) {
                DBDebugHelper.LOGGER.debug("{} {} - {} = {} : {}", caller, indent, value.toString(),
                        attribute.getName(), attribute.getEType().getInstanceTypeName());
            } else {
                DBDebugHelper.LOGGER.debug("{} {} - NULL = {} : {}", caller, indent, attribute.getName(),
                        attribute.getEType().getInstanceTypeName());
            }
        }
    }

    private static void printContainment(final Class<?> caller, final Object contents, final String indent) {
        if (contents != null) {
            if (contents instanceof EList<?>) {
                for (final Object content : (EList<?>) contents) {
                    DBDebugHelper.printModelPartition(caller, (EObject) content, indent + "\t");
                }
            } else {
                DBDebugHelper.printModelPartition(caller, (EObject) contents, indent + "\t");
            }
        }
    }

    private static void printReference(final Class<?> caller, final Object contents, final String indent) {
        if (contents != null) {
            if (contents instanceof EList<?>) {
                for (final Object content : (EList<?>) contents) {
                    DBDebugHelper.LOGGER.debug("{} {}\tref {}", caller, indent, content.hashCode());
                }
            } else {
                DBDebugHelper.LOGGER.debug("{} {}\tref {}", caller, indent, contents.hashCode());
            }
        }
    }

    public static void printResource(final Class<?> caller, final String name,
            final GraphDatabaseService graphDatabaseService) {
        DBDebugHelper.LOGGER.debug("{} --- Print Resource {} ---", caller, name);
        try (Transaction tx = graphDatabaseService.beginTx()) {
            for (final Node node : graphDatabaseService.getAllNodes()) {
                DBDebugHelper.LOGGER.debug("{} Node {} {}", caller, node.getId(),
                        node.getLabels().iterator().next().name());
                for (final Entry<String, Object> p : node.getAllProperties().entrySet()) {
                    DBDebugHelper.LOGGER.debug("{} \t p {}={}", caller, p.getKey(), p.getValue());
                }
                for (final Relationship r : node.getRelationships(Direction.OUTGOING)) {
                    DBDebugHelper.LOGGER.debug("{} \t r to {}", caller, r.getEndNodeId());
                    for (final Entry<String, Object> p : r.getAllProperties().entrySet()) {
                        DBDebugHelper.LOGGER.debug("{} \t\t p {}={}", caller, p.getKey(), p.getValue());
                    }
                }
            }
            tx.success();
        }
    }

    /**
     * Save a given model to file.
     *
     * @param name
     *            name of the model
     * @param rootElement
     *            root element
     * @param modelCount
     *            version of the model
     */
    public static void saveModel(final String name, final EObject rootElement, final int modelCount) {
        final File file = new File(String.format("%s-%010d", name, modelCount));
        try {
            final BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            DBDebugHelper.printToFileModel(writer, rootElement, "");
            writer.flush();
            writer.close();
        } catch (final IOException ex) {
            DBDebugHelper.LOGGER.error("Cannot write file {}", file.getAbsolutePath());
        }
    }

    private static void printToFileModel(final BufferedWriter writer, final EObject object, final String indent)
            throws IOException {
        writer.write(
                String.format("%sclass %s : %s\n", indent, object.hashCode(), object.getClass().getCanonicalName()));
        DBDebugHelper.printToFileAttributes(writer, object, indent);
        for (final EReference reference : object.eClass().getEAllReferences()) {
            writer.write(String.format("%s + %s : %s\n", indent, reference.getName(),
                    reference.getEReferenceType().toString()));
            if (reference.isContainment()) {
                DBDebugHelper.printToFileContainment(writer, object.eGet(reference), indent);
            } else {
                DBDebugHelper.printToFileReference(writer, object.eGet(reference), indent);
            }
        }

    }

    private static void printToFileAttributes(final BufferedWriter writer, final EObject object, final String indent)
            throws IOException {
        for (final EAttribute attribute : object.eClass().getEAllAttributes()) {
            final Object value = object.eGet(attribute);
            if (value != null) {
                writer.write(String.format("%s - %s = %s : %s\n", indent, value.toString(), attribute.getName(),
                        attribute.getEType().getInstanceTypeName()));
            } else {
                writer.write(String.format("%s - NULL = %s : %s\n", indent, attribute.getName(),
                        attribute.getEType().getInstanceTypeName()));
            }
        }
    }

    private static void printToFileContainment(final BufferedWriter writer, final Object contents, final String indent)
            throws IOException {
        if (contents != null) {
            if (contents instanceof EList<?>) {
                for (final Object content : (EList<?>) contents) {
                    DBDebugHelper.printToFileModel(writer, (EObject) content, indent + "     ");
                }
            } else {
                DBDebugHelper.printToFileModel(writer, (EObject) contents, indent + "     ");
            }
        }
    }

    private static void printToFileReference(final BufferedWriter writer, final Object contents, final String indent)
            throws IOException {
        if (contents != null) {
            if (contents instanceof EList<?>) {
                for (final Object content : (EList<?>) contents) {
                    writer.write(String.format("%s     ref %s\n", indent, content.hashCode()));
                }
            } else {
                writer.write(String.format("%s     ref %s\n", indent, contents.hashCode()));
            }
        }
    }
}
