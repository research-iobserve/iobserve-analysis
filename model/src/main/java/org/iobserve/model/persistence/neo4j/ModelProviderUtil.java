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
package org.iobserve.model.persistence.neo4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.plexus.util.FileUtils;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.palladiosimulator.pcm.parameter.VariableCharacterisationType;
import org.palladiosimulator.pcm.repository.ComponentType;
import org.palladiosimulator.pcm.repository.DataType;
import org.palladiosimulator.pcm.repository.ParameterModifier;
import org.palladiosimulator.pcm.repository.PrimitiveTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides different utilities for the {@link ModelProvider}.
 *
 * @author Lars Bluemke
 *
 */
public final class ModelProviderUtil {

    public static final String PCM_ENTITY_NAME = "entityName";
    public static final String PCM_ID = "id";
    public static final String IMPLEMENTATION_ID = "implementationId";

    public static final String REF_NAME = ":refName";

    public static final String REF_POS = ":refPos";

    public static final String EMF_URI = ":emfUri";

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelProviderUtil.class);
    private static final String VERSION_PREFIX = "v_";

    private ModelProviderUtil() {
        // private utility class
    }

    /**
     * Returns a URI based on the components containing the passed component.
     *
     * @param object
     *            The component to compute a URI to
     * @return The URI
     */
    public static String getUriString(final EObject object) {
        EObject comp = object;
        EObject container;
        String label;
        String uri = "";
        EAttribute idAttr;

        do {
            container = comp.eContainer();
            label = comp.eClass().getInstanceTypeName();

            idAttr = comp.eClass().getEIDAttribute();

            if (uri.isEmpty()) {
                if (idAttr != null) {
                    uri = label + "#" + comp.eGet(idAttr);
                } else {
                    uri = label;
                }
            } else {
                if (idAttr != null) {
                    uri = label + "#" + comp.eGet(idAttr) + "." + uri;
                } else {
                    uri = label + "." + uri;
                }
            }

            comp = container;

        } while (container != null);

        return uri;
    }

    /**
     * Returns the first of several labels.
     *
     * @param labels
     *            Several labels
     * @return The first label
     */
    public static Label getFirstLabel(final Iterable<Label> labels) {
        if (labels.iterator().hasNext()) {
            return labels.iterator().next();
        } else {
            return null;
        }
    }

    /**
     * Initializes the newest version of a model graph with the given model. Overwrites a potential
     * existing graph in the database directory of this loader.
     *
     * @param factory
     *            the factory for the particular metamodel (partition)
     * @param model
     *            the model to use for initialization
     * @param nameLabel
     *            label for the name attribute in the DB and model
     * @param idLabel
     *            label for the id attribute in the DB and model
     * @param baseDirectory
     *            base directory for the database
     *
     * @param <V>
     *            the type of the root element
     */
    public <V extends EObject> void initializeModelGraph(final EFactory factory, final V model, final String nameLabel,
            final String idLabel, final File baseDirectory) {
        final ModelResource resource = ModelProviderUtil.createModelResource(factory, baseDirectory);
        resource.clearResource();
        resource.storeModelPartition(model);
        resource.getGraphDatabaseService().shutdown();
    }

    /**
     * Helper method for getting graphs: Returns the newest version of the model graph.
     *
     * @param factory
     *            the factory for the particular metamodel (partition)
     * @param baseDirectory
     *            base directory for the model database
     * @return The model graph
     */
    public static ModelResource createModelResource(final EFactory factory, final File baseDirectory) {
        final String graphTypeDirName = ModelProviderUtil.fullyQualifiedPackageName(factory.getEPackage());
        final File graphTypeDir = new File(baseDirectory, graphTypeDirName);
        int maxVersionNumber = ModelProviderUtil.getLastVersionNumber(graphTypeDir.listFiles());

        if (maxVersionNumber == -1) { // no previous version exists.
            maxVersionNumber = 0;
        }

        final File newGraphDir = ModelProviderUtil.createResourceDatabaseFile(graphTypeDir, graphTypeDirName,
                maxVersionNumber);

        return new ModelResource(factory, newGraphDir);
    }

    /**
     * Returns a {@link #PcmRelationshipType} for a given reference and the referenced object.
     *
     * @param ref
     *            The reference
     * @param refObj
     *            The referenced object
     * @return The proper relationship type
     */
    public static RelationshipType getRelationshipType(final EReference ref, final Object refObj) {

        if (ref.isContainment()) {
            return EMFRelationshipType.CONTAINS;
        } else if (ModelProviderUtil.isDatatype(ref, refObj)) {
            return EMFRelationshipType.IS_TYPE;
        } else {
            return EMFRelationshipType.REFERENCES;
        }
    }

    /**
     * Checks whether a referenced object is the referencer's data type.
     *
     * @param reference
     *            The reference
     * @param referenceObject
     *            The referenced object
     * @return True, if the referenced object is the referencer's data type, false otherwise
     */
    public static boolean isDatatype(final EReference reference, final Object referenceObject) {
        return referenceObject instanceof DataType && !(reference.getName().equals("parentType_CompositeDataType")
                || reference.getName().equals("compositeDataType_InnerDeclaration"));
    }

    /**
     * Clones and returns a new version from the current newest version of the model graph. If there
     * is none yet an empty graph is returned.
     *
     * @param factory
     *            metamodel factory class
     * @param resource
     *            resource to be cloned
     *
     * @return The cloned graph
     */
    public static ModelResource createNewModelResourceVersion(final EFactory factory, final ModelResource resource) {
        final File baseDirectory = resource.getGraphDirectory().getParentFile().getParentFile();

        return ModelProviderUtil.cloneNewModelGraphVersion(factory, baseDirectory);
    }

    /**
     * Helper method for cloning: Clones and returns a new version from the current newest version
     * of the model graph.
     *
     * @param factory
     *            the factory for the particular metamodel (partition)
     * @param <T>
     *            graph type
     * @return The the model graph
     */
    private static <T extends EObject> ModelResource cloneNewModelGraphVersion(final EFactory factory,
            final File baseDirectory) {
        final String resourceRootTypeName = ModelProviderUtil.fullyQualifiedPackageName(factory.getEPackage());
        final File resourceBaseDirectory = new File(baseDirectory, resourceRootTypeName);
        final int maxVersionNumber = ModelProviderUtil.getLastVersionNumber(resourceBaseDirectory.listFiles());

        final File newGraphDir = ModelProviderUtil.createResourceDatabaseFile(resourceBaseDirectory,
                resourceRootTypeName, maxVersionNumber + 1);

        // Copy old graph files
        if (maxVersionNumber >= 0) {
            final File currentGraphDir = ModelProviderUtil.createResourceDatabaseFile(resourceBaseDirectory,
                    resourceRootTypeName, maxVersionNumber);

            try {
                FileUtils.copyDirectory(currentGraphDir, newGraphDir);
            } catch (final IOException e) {
                ModelProviderUtil.LOGGER.error("Could not copy old graph version.");
            }
        } else {
            throw new InternalError("No such model available for cloning.");
        }

        return new ModelResource(factory, newGraphDir);
    }

    /**
     * Instantiates attributes of pcm model components.
     *
     * @param type
     *            The attribute's data type
     * @param value
     *            The attribute's string value from the property of the neo4j graph
     * @return The attribute's value in the proper data type
     */
    public static Object instantiateAttribute(final EDataType type, final String value) {

        final Class<?> clazz = type.getInstanceClass();

        if (clazz == String.class) {
            return value;
        } else if (clazz == boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (clazz == char.class) {
            return value.charAt(0);
        } else if (clazz == byte.class) {
            return Byte.parseByte(value);
        } else if (clazz == short.class) { // NOPMD short supported by Kieker
            return Short.parseShort(value);
        } else if (clazz == int.class) {
            return Integer.parseInt(value);
        } else if (clazz == long.class) {
            return Long.parseLong(value);
        } else if (clazz == float.class) {
            return Float.parseFloat(value);
        } else if (clazz == double.class) {
            return Double.parseDouble(value);
        } else if (clazz == ParameterModifier.class) {
            if (value.equals(ParameterModifier.NONE.toString())) {
                return ParameterModifier.NONE;
            } else if (value.equals(ParameterModifier.IN.toString())) {
                return ParameterModifier.IN;
            } else if (value.equals(ParameterModifier.OUT.toString())) {
                return ParameterModifier.OUT;
            } else if (value.equals(ParameterModifier.INOUT.toString())) {
                return ParameterModifier.INOUT;
            }
        } else if (clazz == ComponentType.class) {
            if (value.equals(ComponentType.BUSINESS_COMPONENT.toString())) {
                return ComponentType.BUSINESS_COMPONENT;
            } else if (value.equals(ComponentType.INFRASTRUCTURE_COMPONENT.toString())) {
                return ComponentType.INFRASTRUCTURE_COMPONENT;
            }
        } else if (clazz == PrimitiveTypeEnum.class) {
            if (value.equals(PrimitiveTypeEnum.INT.toString())) {
                return PrimitiveTypeEnum.INT;
            } else if (value.equals(PrimitiveTypeEnum.STRING.toString())) {
                return PrimitiveTypeEnum.STRING;
            } else if (value.equals(PrimitiveTypeEnum.BOOL.toString())) {
                return PrimitiveTypeEnum.BOOL;
            } else if (value.equals(PrimitiveTypeEnum.DOUBLE.toString())) {
                return PrimitiveTypeEnum.DOUBLE;
            } else if (value.equals(PrimitiveTypeEnum.CHAR.toString())) {
                return PrimitiveTypeEnum.CHAR;
            } else if (value.equals(PrimitiveTypeEnum.BYTE.toString())) {
                return PrimitiveTypeEnum.BYTE;
            } else if (value.equals(PrimitiveTypeEnum.LONG.toString())) {
                return PrimitiveTypeEnum.LONG;
            }
        } else if (clazz == VariableCharacterisationType.class) {
            if (value.equals(VariableCharacterisationType.STRUCTURE.toString())) {
                return VariableCharacterisationType.STRUCTURE;
            } else if (value.equals(VariableCharacterisationType.NUMBER_OF_ELEMENTS.toString())) {
                return VariableCharacterisationType.NUMBER_OF_ELEMENTS;
            } else if (value.equals(VariableCharacterisationType.VALUE.toString())) {
                return VariableCharacterisationType.VALUE;
            } else if (value.equals(VariableCharacterisationType.BYTESIZE.toString())) {
                return VariableCharacterisationType.BYTESIZE;
            } else if (value.equals(VariableCharacterisationType.TYPE.toString())) {
                return VariableCharacterisationType.TYPE;
            }
        }

        return null;
    }

    /**
     * Instantiates a pcm model object from a given type name.
     *
     * @param factories
     *            factories for this particular metamodel
     * @param fqnClassName
     *            The data type name
     * @return New object of the given data type
     */
    @SuppressWarnings("unchecked")
    public static <T> T instantiateEObject(final List<EFactory> factories, final String fqnClassName) {

        final int separationPoint = fqnClassName.lastIndexOf('.');
        final String className = fqnClassName.substring(separationPoint + 1);

        for (final EFactory factory : factories) {
            final EPackage ePackage = factory.getEPackage();
            final EClass eClass = (EClass) ePackage.getEClassifier(className);
            if (eClass != null) {
                return (T) factory.create(eClass);
            }
        }
        return null;

    }

    /**
     * Sorts an Iterable of relationships by their position properties.
     *
     * @param relationships
     *            The relationships to be sorted
     * @return The sorted relationships
     */
    public static Iterable<Relationship> sortRelsByPosition(final Iterable<Relationship> relationships) {
        if (relationships == null) {
            return Collections.emptyList();
        }

        final List<Relationship> sortedRels = new ArrayList<>();
        relationships.forEach(sortedRels::add);

        Collections.sort(sortedRels, new RelationshipComparator());

        return sortedRels;
    }

    private static String fullyQualifiedPackageName(final EPackage ePackage) {
        if (ePackage == null) {
            return "default";
        } else if (ePackage.getESuperPackage() != null) {
            return ModelProviderUtil.fullyQualifiedPackageName(ePackage.getESuperPackage()) + "." + ePackage.getName();
        } else {
            return ePackage.getName();
        }
    }

    private static File createResourceDatabaseFile(final File graphTypeDir, final String graphTypeDirName,
            final int versionNumber) {
        return new File(graphTypeDir, graphTypeDirName + ModelProviderUtil.VERSION_PREFIX + versionNumber);
    }

    /**
     * Returns the highest version number from all passed graph database directories.
     *
     * @param files
     *            The graph database directories
     * @return The highest version number
     */
    private static int getLastVersionNumber(final File[] files) {
        if (files != null) {
            int max = 0;

            for (final File file : files) {
                final int version = ModelProviderUtil.getVersionNumber(file);

                if (max < version) {
                    max = version;
                }
            }
            return max;
        } else {
            return -1;
        }

    }

    /**
     * Returns the version number of a given graph database directory.
     *
     * @param file
     *            The graph database directory
     * @return The version number
     */
    private static int getVersionNumber(final File file) {
        final int versionNumberIndex = file.getName().lastIndexOf(ModelProviderUtil.VERSION_PREFIX);

        if (versionNumberIndex == -1) {
            throw new InternalError(
                    "Missing version number in " + file + "  Every database path must have a version number.");
        } else {
            return Integer
                    .valueOf(file.getName().substring(versionNumberIndex + ModelProviderUtil.VERSION_PREFIX.length()));
        }
    }
}
