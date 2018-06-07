/***************************************************************************
 * Copyright (C) 2015 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.model.provider.file;

import org.eclipse.emf.ecore.EPackage;
import org.iobserve.model.privacy.PrivacyModel;
import org.iobserve.model.privacy.PrivacyPackage;
import org.palladiosimulator.pcm.system.System;

/**
 * Model provider to provide {@link System} model.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 * @author Reiner Jung - refactoring & api change
 */
public final class PrivacyModelHandler extends AbstractModelHandler<PrivacyModel> {

    public static final String SUFFIX = "privacy";

    /**
     * Create an empty model provider.
     */
    public PrivacyModelHandler() {
        super();
    }

    @Override
    protected EPackage getPackage() {
        return PrivacyPackage.eINSTANCE;
    }

    @Override
    protected String getSuffix() {
        return PrivacyModelHandler.SUFFIX;
    }
}
