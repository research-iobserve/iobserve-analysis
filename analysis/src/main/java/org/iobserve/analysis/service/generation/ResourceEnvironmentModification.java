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
package org.iobserve.analysis.service.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.eclipse.emf.common.util.EList;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ToDo .
 *
 * @author unknown
 *
 */
public class ResourceEnvironmentModification {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelModificationFactory.class);

    private final ResourceEnvironment resourceEnvironment;

    public ResourceEnvironmentModification(final ResourceEnvironment resourceEnvironment) {
        this.resourceEnvironment = resourceEnvironment;
    }

    /**
     *
     * @param terminations
     * @return
     */
    public List<ResourceContainer> modifyResEnvTerminate(final int terminations) {

        final List<ResourceContainer> removedResourceContainers = new ArrayList<>();
        final EList<ResourceContainer> resourceContainers = this.resourceEnvironment
                .getResourceContainer_ResourceEnvironment();

        for (int i = 0; i < terminations; i++) {
            final int randomIndex = ThreadLocalRandom.current().nextInt(resourceContainers.size());
            final ResourceContainer removedResCon = resourceContainers.remove(randomIndex);
            removedResourceContainers.add(removedResCon);

            if (ResourceEnvironmentModification.LOGGER.isInfoEnabled()) {
                ResourceEnvironmentModification.LOGGER
                        .info("REMOVING: \tResourceContainer: \t" + removedResCon.getId());
            }
        }

        return removedResourceContainers;
    }

    /**
     *
     * @param acquires
     */
    public void modifyResEnvAcquire(final int acquires) {
        final ResourceEnvironmentGeneration resEnvGen = new ResourceEnvironmentGeneration(this.resourceEnvironment);
        resEnvGen.addResourceContainers(acquires, "mod");
    }

}
