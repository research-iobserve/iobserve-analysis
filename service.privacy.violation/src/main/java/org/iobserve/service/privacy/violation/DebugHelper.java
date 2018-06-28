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
package org.iobserve.service.privacy.violation;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
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
     * Print the complete partition of the given object
     *
     * @param object
     *            root element of the partition
     */
    public static void printModelPartition(final EObject object) {
        DebugHelper.printModelPartition(object, "");
    }

    private static void printModelPartition(final EObject object, final String indent) {
        DebugHelper.LOGGER.debug("{}class {} : {}", indent, object.hashCode(), object.getClass().getCanonicalName());
        for (final EAttribute attribute : object.eClass().getEAllAttributes()) {
            final Object value = object.eGet(attribute);
            DebugHelper.LOGGER.debug("{} - {} = {} : {}", indent, value.toString(), attribute.getName(),
                    attribute.getEType().getInstanceTypeName());
        }
        for (final EReference reference : object.eClass().getEAllReferences()) {
            DebugHelper.LOGGER.debug("{} + {} : {}", indent, reference.getName(), reference.getEReferenceType());
            if (reference.isContainment()) {
                for (final EObject content : reference.eContents()) {
                    DebugHelper.printModelPartition(content, indent + "\t");
                }
            } else {
                for (final EObject content : reference.eContents()) {
                    DebugHelper.LOGGER.debug("{} - {}", content.hashCode());
                }
            }
        }
    }
}
