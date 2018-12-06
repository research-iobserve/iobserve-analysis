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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.BasicEObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * @author Reiner Jung
 *
 */
public final class ModelNeo4JUtil {

    private ModelNeo4JUtil() {
        // do not instantiate utility class
    }

    public synchronized static void resolveAll(final ResourceSet resourceSet,
            final ModelResource<?>... modelResources) {
        final EList<Resource> resources = resourceSet.getResources();

        for (int i = 0; i < resources.size(); i++) {
            final Resource resource = resources.get(i);
            final TreeIterator<EObject> elements = resource.getAllContents();
            while (elements.hasNext()) {
                final EObject object = elements.next();
                ModelNeo4JUtil.resolve(object, modelResources);
            }
        }
    }

    private static void resolve(final EObject object, final ModelResource<?>[] modelResources) {
        for (final EReference reference : object.eClass().getEAllReferences()) {
            if (reference.isMany()) {
                @SuppressWarnings("unchecked")
                final EList<EObject> manyReference = (EList<EObject>) object.eGet(reference);
                for (final EObject referencedObject : manyReference) {
                    ModelNeo4JUtil.resolveReference(modelResources, referencedObject);
                }
            } else {
                final EObject singleReference = (EObject) object.eGet(reference);
                ModelNeo4JUtil.resolveReference(modelResources, singleReference);
            }
        }
    }

    private static void resolveReference(final ModelResource<?>[] modelResources, final EObject referencedObject) {
        for (final ModelResource<?> modelResource : modelResources) {
            if (referencedObject != null) {
                if (referencedObject.eIsProxy() && modelResource.isTypeManagedByResource(referencedObject)) {
                    try {
                        modelResource.resolve(referencedObject);
                    } catch (final DBException | InvocationException e) {
                        final URI classUri = ((BasicEObjectImpl) referencedObject).eProxyURI();
                        System.err.println("ERROR " + classUri + " " + e.getLocalizedMessage());
                    }
                }
            }
        }
    }

    /**
     * print model.
     *
     * @param <T>
     *            object root element type
     * @param model
     *            model
     */
    public static <T extends EObject> void printModel(final T model) {
        System.out.println("Print Model");
        ModelNeo4JUtil.printModel(model, "");
    }

    @SuppressWarnings("unchecked")
    private static <T extends EObject> void printModel(final T model, final String prefix) {
        System.out.printf("%sObject %s\n", prefix, model.eClass().getName());
        for (final EAttribute attribute : model.eClass().getEAllAttributes()) {
            System.out.printf("%s   %s = %s\n", prefix, attribute.getName(), model.eGet(attribute));
        }
        for (final EReference reference : model.eClass().getEAllReferences()) {
            final Object referencedObject = model.eGet(reference);
            if (referencedObject != null) {
                if (referencedObject instanceof EObject) {
                    ModelNeo4JUtil.printObject((EObject) referencedObject, reference, prefix);
                } else if (referencedObject instanceof EList) {
                    final EList<EObject> referencedList = (EList<EObject>) referencedObject;
                    if (reference.isContainment()) {
                        System.out.printf("%s   %s => [\n", prefix, reference.getName());
                        for (final EObject object : referencedList) {
                            if (object.eIsProxy()) {
                                System.out.printf("%s\n", ((BasicEObjectImpl) object).eProxyURI());
                            } else {
                                ModelNeo4JUtil.printModel(object, prefix + "   ");
                            }
                        }
                    } else {
                        System.out.printf("%s   %s --> [\n", prefix, reference.getName());
                        for (final EObject object : referencedList) {
                            if (object.eIsProxy()) {
                                System.out.printf("%s\n", ((BasicEObjectImpl) object).eProxyURI());
                            } else {
                                ModelNeo4JUtil.printAttributes(object, prefix + "   ");
                            }
                        }
                    }
                    System.out.printf("%s]\n", prefix);
                }

            } else {
                if (reference.isContainment()) {
                    System.out.printf("%s   %s => null\n", prefix, reference.getName());
                } else {
                    System.out.printf("%s   %s --> null\n", prefix, reference.getName());
                }
            }
        }
    }

    private static void printObject(final EObject referencedObject, final EReference reference, final String prefix) {
        if (referencedObject.eIsProxy()) {
            System.out.printf("%s   %s => %s", prefix, reference.getName(),
                    ((BasicEObjectImpl) referencedObject).eProxyURI());
        } else {
            if (reference.isContainment()) {
                System.out.printf("%s   %s => \n", prefix, reference.getName());
                ModelNeo4JUtil.printModel(referencedObject, prefix + "   ");
            } else {
                System.out.printf("%s   %s --> \n", prefix, reference.getName());
                ModelNeo4JUtil.printAttributes(referencedObject, prefix + "   ");
            }
        }
    }

    private static <T extends EObject> void printAttributes(final T model, final String prefix) {
        System.out.printf("%sObject %s\n", prefix, model.eClass().getName());
        for (final EAttribute attribute : model.eClass().getEAllAttributes()) {
            System.out.printf("%s   %s = %s\n", prefix, attribute.getName(), model.eGet(attribute));
        }
    }

}
