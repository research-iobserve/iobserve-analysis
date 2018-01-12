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
package org.iobserve.analysis.model;

import org.palladiosimulator.pcm.resourcetype.CommunicationLinkResourceType;
import org.palladiosimulator.pcm.resourcetype.ProcessingResourceType;
import org.palladiosimulator.pcm.resourcetype.ResourceRepository;
import org.palladiosimulator.pcm.resourcetype.SchedulingPolicy;

/**
 * Convenient access to Palladio's {@link ResourceRepository}.
 *
 * @author Fabian Keller
 */
public interface IPalladioResourceRepository {

    ProcessingResourceType cpu();

    ProcessingResourceType hdd();

    ProcessingResourceType delay();

    CommunicationLinkResourceType lan();

    SchedulingPolicy policyProcessorSharing();

    SchedulingPolicy policyFCFS();

    SchedulingPolicy policyDelay();

    /**
     * Hello stranger,
     * <p>
     * until there is a proper DI framework built into this project, we'll need to rely on this
     * singleton to not have to pass around the {@link IPalladioResourceRepository} to all builders.
     * Unfortunately, some deeply nested builders require the resource repository and bubbling up
     * the dependency leads to more technical debt than this singleton.
     * <p>
     * Hit <kbd>Alt+F7</kbd> to find the builders requiring the {@link IPalladioResourceRepository}.
     * <p>
     * Have a nice day, anyways!
     */
    public enum INSTANCE {
        ONE;
        private static IPalladioResourceRepository resources;

        /**
         * Technical Debt - to be replaced by DI framework.
         *
         * @param instance
         *            an resource instance
         */
        public static void initResources(final IPalladioResourceRepository instance) {
            INSTANCE.resources = instance;
        }

        /**
         * Technical Debt - to be replaced by DI framework.
         *
         * @return a resource repository
         */
        public static IPalladioResourceRepository resources() {
            if (INSTANCE.resources == null) {
                throw new RuntimeException("Palladio resource repository singleton has not been initialized!");
            }
            return INSTANCE.resources;
        }
    }
}
