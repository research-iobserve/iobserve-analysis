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
package org.iobserve.model.test.provider.neo4j;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil.EqualityHelper;
import org.iobserve.model.DebugHelper;

/**
 * Modified {@link EqualityHelper} which handles proxies differently. In the neo4j database a proxy
 * node does not reference other nodes and only contains attributes and an uri. For this reason we
 * only check if these properties are equal here.
 *
 * @author Lars Bluemke
 *
 * @see EqualityHelper
 *
 * @since 0.0.2
 *
 */
public class CompareModelPartitions {

    /**
     * Create Neo4JEqualityHelper.
     */
    public CompareModelPartitions() {
        // empty default constructor
    }

    /**
     * Compare multiple partitions.
     *
     * @param expectedModels
     *            expected models
     * @param actualModels
     *            actual models
     * @return true on success
     */
    public boolean comparePartitions(final List<EObject> expectedModels, final List<EObject> actualModels,
            final EClass partitionrootClass) {
        boolean result = true;
        for (int i = 0; i < expectedModels.size(); i++) {
            result &= this.comparePartition(expectedModels.get(i), actualModels.get(i), partitionrootClass);
        }
        return result;
    }

    /**
     * Returns whether <code>eObject1</code> and <code>eObject2</code> are {@link EqualityHelper
     * equal} in the context of this helper instance. Note: With overwriting this method I modified
     * the proxy check. As proxy resolving isn't possible with the neo4j model db so far, only
     * attributes are relevant for the equality check in this version.
     *
     * @param expectedModel
     *            expected model
     * @param actualModel
     *            model
     * @param packageInfo
     *
     * @return whether <code>eObject1</code> and <code>eObject2</code> are equal.
     * @since 2.1.0
     */
    public boolean comparePartition(final EObject expectedModel, final EObject actualModel,
            final EClass partitionRootClass) {
        // If the first object is null, the second object must be null.
        //
        if (expectedModel == null) {
            return actualModel == null;
        }

        // We know the first object isn't null, so if the second one is, it can't be equal.
        //
        if (actualModel == null) {
            return false;
        }

        if (expectedModel.getClass() == actualModel.getClass()) {
            if (this.isProxy(partitionRootClass, expectedModel) && this.isProxy(partitionRootClass, actualModel)) {
                return true;
            } else {
                return this.compareAllReferences(expectedModel, actualModel, partitionRootClass);
            }
        } else {
            return false;
        }
    }

    private boolean isProxy(final EClass partitionRootClass, final EObject searchObject) {
        return this.checkClasses(partitionRootClass, searchObject.eClass());
    }

    private boolean checkClasses(final EClass partitionRootClass, final EClass searchClass) {
        if (partitionRootClass == searchClass) {
            return true;
        } else {
            for (final EReference reference : partitionRootClass.getEAllContainments()) {
                final EClassifier classifier = reference.getEType();
                if (classifier instanceof EClass) {
                    if (this.checkClasses((EClass) classifier, searchClass)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean compareAllReferences(final EObject expectedModel, final EObject actualModel,
            final EClass partitionrootClass) {
        boolean result = true;

        for (final EReference reference : expectedModel.eClass().getEAllReferences()) {
            if (reference.isContainment()) {
                if (reference.isMany()) {
                    result &= this.compareAllContainedObjects((EList<?>) expectedModel.eGet(reference),
                            (EList<?>) actualModel.eGet(reference), partitionrootClass);
                } else {
                    result &= this.comparePartition((EObject) expectedModel.eGet(reference),
                            (EObject) actualModel.eGet(reference), partitionrootClass);
                }
            } else {
                if (reference.isMany()) {
                    result &= this.compareAllReferencedObjects((EList<?>) expectedModel.eGet(reference),
                            (EList<?>) actualModel.eGet(reference));
                } else {
                    result &= this.compareSingleReferencedObject((EObject) expectedModel.eGet(reference),
                            (EObject) actualModel.eGet(reference));
                }
            }
        }

        result &= this.compareAttributes(expectedModel, actualModel);

        return result;

    }

    private boolean compareAttributes(final EObject eObject1, final EObject eObject2) {
        if (eObject1.eIsProxy() && eObject2.eIsProxy()) {
            return true;
        }

        boolean result = true;
        for (final EAttribute attribute : eObject1.eClass().getEAllAttributes()) {
            final Object expected = eObject1.eGet(attribute);
            final Object actual = eObject2.eGet(attribute);
            if (expected != null && actual != null) {
                result &= expected.equals(actual);
            } else if (expected == null && actual == null) {
                result &= true;
            } else {
                return false;
            }
        }
        return result;
    }

    private boolean compareAllContainedObjects(final EList<?> list1, final EList<?> list2,
            final EClass partitionrootClass) {
        DebugHelper.printList(this.getClass(), "expected", list1);
        DebugHelper.printList(this.getClass(), "actual", list2);

        if (list1.size() != list2.size()) {
            return false;
        }
        final boolean result = true;
        for (int i = 0; i < list1.size(); i++) {
            this.comparePartition((EObject) list1.get(i), (EObject) list2.get(i), partitionrootClass);
        }

        return result;
    }

    private boolean compareSingleReferencedObject(final EObject object1, final EObject object2) {
        if (object1 != null && object2 != null) {
            return this.compareAttributes(object1, object2);
        } else {
            return false;
        }
    }

    private boolean compareAllReferencedObjects(final EList<?> list1, final EList<?> list2) {
        final boolean result = true;
        for (int i = 0; i < list1.size(); i++) {
            this.compareSingleReferencedObject((EObject) list1.get(i), (EObject) list2.get(i));
        }

        return result;
    }

    public boolean compareObject(final EObject expectedModel, final EObject actualModel) {
        if (expectedModel == null && actualModel == null) {
            return true;
        } else if (expectedModel != null && actualModel != null) {
            return this.compareAttributes(expectedModel, actualModel);
        } else {
            return false;
        }
    }

}
