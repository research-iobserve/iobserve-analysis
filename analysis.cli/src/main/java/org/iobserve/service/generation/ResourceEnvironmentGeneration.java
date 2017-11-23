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
package org.iobserve.service.generation;

import java.util.concurrent.ThreadLocalRandom;

import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;
import org.palladiosimulator.pcm.resourceenvironmentprivacy.ResourceContainerPrivacy;
import org.palladiosimulator.pcm.resourceenvironmentprivacy.ResourceenvironmentPrivacyFactory;

import com.neovisionaries.i18n.CountryCode;

public class ResourceEnvironmentGeneration {

    private static final ResourceenvironmentFactory RES_ENV_FACTORY = ResourceenvironmentFactory.eINSTANCE;
    private static final ResourceenvironmentPrivacyFactory RES_ENV_PRIVACY_FACTORY = ResourceenvironmentPrivacyFactory.eINSTANCE;
    private final ResourceEnvironment resourceEnvironment;
    private final CountryCode[] countryCodes = CountryCode.values();

    public ResourceEnvironmentGeneration(final String modelName) {
        this.resourceEnvironment = ResourceEnvironmentGeneration.RES_ENV_FACTORY.createResourceEnvironment();
        this.resourceEnvironment.setEntityName(modelName);
    }

    public ResourceEnvironmentGeneration(final ResourceEnvironment resEnvModel) {
        this.resourceEnvironment = resEnvModel;
    }

    public ResourceEnvironment craeteResourceEnvironment(final int resourceContainerCount) {
        for (int i = 0; i < resourceContainerCount; i++) {
            final String prefix = Integer.toString(i);
            final ResourceContainerPrivacy resContainer = this.createResourceContainer(prefix);
            this.resourceEnvironment.getResourceContainer_ResourceEnvironment().add(resContainer);
        }
        return this.resourceEnvironment;
    }

    public void addResourceContainers(final int resourceContainerCount, final String postPrefix) {
        for (int i = 0; i < resourceContainerCount; i++) {
            final String prefix = Integer.toString(i);
            final ResourceContainerPrivacy resContainer = this.createResourceContainer(prefix + "_" + postPrefix);
            this.resourceEnvironment.getResourceContainer_ResourceEnvironment().add(resContainer);
        }
    }

    private ResourceContainerPrivacy createResourceContainer(final String prefix) {
        final ResourceContainerPrivacy resContainer = ResourceEnvironmentGeneration.RES_ENV_PRIVACY_FACTORY
                .createResourceContainerPrivacy();
        resContainer.setEntityName(prefix + "_ResCon");

        final int randGeoLocation = ThreadLocalRandom.current().nextInt(this.countryCodes.length);
        resContainer.setGeolocation(this.countryCodes[randGeoLocation].getNumeric());

        return resContainer;
    }

}
