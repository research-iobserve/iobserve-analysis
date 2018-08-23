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
package org.iobserve.model;

import org.iobserve.model.correspondence.AbstractEntry;
import org.iobserve.model.correspondence.CorrespondenceModel;
import org.iobserve.model.correspondence.OperationEntry;
import org.iobserve.model.correspondence.Part;
import org.palladiosimulator.pcm.repository.OperationSignature;

/**
 * @author Reiner Jung
 *
 */
public final class CorrespondenceUtility {

    private CorrespondenceUtility() {
        // utility class
    }

    public static Part findPart(final Class<?> clazz, final CorrespondenceModel correspondenceModel) {
        for (final Part part : correspondenceModel.getParts()) {
            if (part.getModelType().getClass() == clazz) {
                return part;
            }
        }

        return null;
    }

    public static OperationSignature findModelElementForOperation(final CorrespondenceModel correspondenceModel,
            final Class<?> clazz, final String classSignature, final String objectSignature) {
        final Part part = CorrespondenceUtility.findPart(clazz, correspondenceModel);
        return CorrespondenceUtility.findModelElementForOperation(part, classSignature, objectSignature);
    }

    public static OperationSignature findModelElementForOperation(final Part part, final String classSignature,
            final String objectSignature) {
        for (final AbstractEntry entry : part.getEntries()) {
            if (entry instanceof OperationEntry) {
                final OperationEntry operationEntry = (OperationEntry) entry;
                if (operationEntry.getImplementationId().equals(objectSignature)) {
                    return operationEntry.getOperation();
                }
            }
        }
        return null;
    }

}
