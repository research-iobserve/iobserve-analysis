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
package org.iobserve.analysis.test.cli;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.palladiosimulator.pcm.repository.RepositoryFactory;
import org.palladiosimulator.pcm.repository.RepositoryPackage;

/**
 * @author reiner
 *
 */
public class DynamicallyCreateEClassMain {

    /**
     * @param args
     */
    public static void main(final String[] args) {
        System.out.println("Number of types " + RepositoryPackage.eINSTANCE.getEClassifiers().size());
        for (final EClassifier eClassifier : RepositoryPackage.eINSTANCE.getEClassifiers()) {
            if (eClassifier instanceof EClass) {
                final EClass eClass = (EClass) eClassifier;

                if (eClass.isAbstract()) {
                    System.out.print("abstract ");
                }
                if (eClass.isInterface()) {
                    System.out.print("interface ");
                }

                System.out.println("type " + DynamicallyCreateEClassMain.getFullQualifiedClassifierName(eClassifier));

                if (!eClass.isAbstract() && !eClass.isInterface()) {
                    DynamicallyCreateEClassMain.instantiateObject(eClass);
                    DynamicallyCreateEClassMain.listReferences(eClass);
                } else {
                    DynamicallyCreateEClassMain.listAttributes(eClass);
                    DynamicallyCreateEClassMain.listReferences(eClass);
                }
            }
        }

    }

    /**
     * List all attributes of a given eClass.
     *
     * @param eClass
     */
    private static void listAttributes(final EClass eClass) {
        /** loop over attributes. */
        for (final EAttribute attr : eClass.getEAllAttributes()) {
            System.out.println("\tattribute " + attr.getName() + " : " + attr.getEAttributeType().getName());
        }
    }

    /**
     * Instantiate an eClass.
     *
     * @param eClass
     */
    private static void instantiateObject(final EClass eClass) {
        /** instantiation test. */
        final EObject obj = RepositoryFactory.eINSTANCE.create(eClass);
        /** loop over attributes. */
        for (final EAttribute attr : eClass.getEAllAttributes()) {
            System.out.println("\tattribute " + attr.getName() + " : " + attr.getEAttributeType().getName());
            final Class<?> clazz = attr.getEAttributeType().getInstanceClass();
            if (clazz == String.class) {
                obj.eSet(attr, "Example String");
            } else if (clazz == Integer.class) {
                obj.eSet(attr, 10);
            }

        }

        System.out.println("  object type " + obj.getClass());
    }

    /**
     * List all references of an eClass.
     *
     * @param eClass
     */
    private static void listReferences(final EClass eClass) {
        for (final EReference reference : eClass.getEAllReferences()) {
            System.out.println("\treference " + reference.getName() + " : "
                    + DynamicallyCreateEClassMain.getFullQualifiedClassifierName(reference.getEType()));
        }
    }

    /**
     * Compute fully qualified name of a classifier.
     *
     * @param eClassifier
     *            the classifier
     * @return Return the FQN
     */
    private static String getFullQualifiedClassifierName(final EClassifier eClassifier) {
        return DynamicallyCreateEClassMain.getFullyQualifiedPackageName(eClassifier.getEPackage()) + "."
                + eClassifier.getName();
    }

    /**
     * Compute the fully qualified name of a package.
     *
     * @param ePackage
     * @return
     */
    private static String getFullyQualifiedPackageName(final EPackage ePackage) {
        if (ePackage.getESuperPackage() != null) {
            return DynamicallyCreateEClassMain.getFullyQualifiedPackageName(ePackage.getESuperPackage()) + "."
                    + ePackage.getName();
        } else {
            return ePackage.getName();
        }
    }

}
