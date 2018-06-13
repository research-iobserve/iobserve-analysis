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
package org.iobserve.model.provider.file;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.palladiosimulator.pcm.cloud.pcmcloud.cloudprofile.CloudProfile;
import org.palladiosimulator.pcm.cloud.pcmcloud.cloudprofile.CloudprofilePackage;

/**
 * Model provider to provide a {@link CloudProfile} model.
 *
 * @author Tobias Pöppke
 * @author Reiner Jung - refactoring & api change
 */
public class CloudProfileModelHandler extends AbstractModelHandler<CloudProfile> {

    public static final String SUFFIX = "cloudprofile";

    /**
     * Create a new provider with the given model file.
     * 
     * @param resourceSet
     *            set the resource set for the resource
     */
    public CloudProfileModelHandler(final ResourceSet resourceSet) {
        super(resourceSet);
    }

    @Override
    protected EPackage getPackage() {
        return CloudprofilePackage.eINSTANCE;
    }

    @Override
    protected String getSuffix() {
        return CloudProfileModelHandler.SUFFIX;
    }

}
