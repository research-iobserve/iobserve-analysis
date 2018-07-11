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
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.BasicEObjectImpl;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;

/**
 * @author Reiner Jung
 *
 */
public final class ModelObjectFactory {

    /** factory constructor, prevent instantiation. */
    private ModelObjectFactory() {
        // nothing to do here
    }

    /**
     * Create a proxy object from node.
     *
     * @param node
     *            the node
     * @param packages
     *            collection of factories
     * @return returns the proxy object
     */
    public static EObject createProxyObject(final Node node, final List<EPackage> packages) {
        final Label label = ModelObjectFactory.getEMFTypeLabel(node.getLabels());
        final EObject object = ModelObjectFactory.instantiateEObject(packages, label.name());
        ((BasicEObjectImpl) object).eSetProxyURI(ModelGraphFactory.getUri(node));

        return object;
    }

    /**
     * Create an object from node.
     *
     * @param node
     *            the node
     * @param factories
     *            collection of factories
     * @return returns the object
     */
    public static EObject createObject(final Node node, final List<EPackage> packages) {
        final Label label = ModelObjectFactory.getEMFTypeLabel(node.getLabels());
        final EObject object = ModelObjectFactory.instantiateEObject(packages, label.name());

        ModelObjectFactory.loadAttributes(object, node.getAllProperties(), packages);

        return object;
    }

    /**
     * Load attribute values from the node.
     *
     * @param object
     *            component where the attributes are attached to
     * @param properties
     *            the attributes to scan for values
     * @param factories
     *            factories for object instantiation
     */
    private static <T extends EObject> void loadAttributes(final T object, final Map<String, Object> properties,
            final List<EPackage> packages) {
        for (final Entry<String, Object> property : properties.entrySet()) {
            final EAttribute attr = (EAttribute) object.eClass().getEStructuralFeature(property.getKey());

            // attr == null for the emfUri property stored in the graph
            if (attr != null) {
                if (attr.isMany()) {
                    ModelObjectFactory.createManyValuesAttribute(object, attr, property, packages);
                } else {
                    object.eSet(attr, ModelObjectFactory.convertValue(attr.getEAttributeType(),
                            property.getValue().toString(), packages));
                }

            }
        }
    }

    private static void createManyValuesAttribute(final EObject component, final EAttribute attr,
            final Entry<String, Object> property, final List<EPackage> packages) {
        @SuppressWarnings("unchecked")
        final List<Object> attribute = (List<Object>) component.eGet(attr);

        final String valueString = property.getValue().toString();

        final String[] values = valueString.substring(1, valueString.length() - 1).split(", ");
        for (final String value : values) {
            final Object convertedValue = ModelObjectFactory.convertValue(attr.getEAttributeType(), value, packages);

            attribute.add(convertedValue);
        }
    }

    private static Object convertValue(final EDataType type, final String input, final List<EPackage> packages) {
        Object value = ModelProviderUtil.instantiateAttribute(type, input);

        if (value == null) {
            // TODO ending up here is strange, check whether this is necessary
            for (final EPackage ePackage : packages) {
                value = ePackage.getEFactoryInstance().createFromString(type, input);

                if (value != null) {
                    return value;
                }
            }

            throw new InternalError("Type " + type.getInstanceClassName() + " is not supported.");
        } else {
            return value;
        }
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
    private static <T> T instantiateEObject(final List<EPackage> packages, final String fqnClassName) {

        final int separationPoint = fqnClassName.lastIndexOf('.');
        final String className = fqnClassName.substring(separationPoint + 1);

        for (final EPackage ePackage : packages) {
            final EClass eClass = (EClass) ePackage.getEClassifier(className);
            if (eClass != null) {
                return (T) ePackage.getEFactoryInstance().create(eClass);
            }
        }

        return null;
    }

    /**
     * Returns the first of several labels.
     *
     * @param labels
     *            Several labels
     * @return The first label
     */
    private static Label getEMFTypeLabel(final Iterable<Label> labels) {
        if (labels.iterator().hasNext()) {
            return labels.iterator().next();
        } else {
            return null;
        }
    }

}