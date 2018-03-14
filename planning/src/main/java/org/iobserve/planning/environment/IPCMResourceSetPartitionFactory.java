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
package org.iobserve.planning.environment;

import org.eclipse.emf.common.util.URI;
import org.palladiosimulator.analyzer.workflow.blackboard.PCMResourceSetPartition;
import org.palladiosimulator.analyzer.workflow.configurations.AbstractPCMWorkflowRunConfiguration;

/**
 * Creates a {@link PCMResourceSetPartition} where Palladio default models are already loaded.
 *
 * @author Fabian Keller
 */
public interface IPCMResourceSetPartitionFactory {

    /**
     * Creates a {@link PCMResourceSetPartition} that can be used in headless mode.
     *
     * @return returns a pcm resource set partition
     */
    PCMResourceSetPartition create();

    /**
     * {@link PCMResourceSetPartition}s do not by default hold references to the default models
     * shipped with Palladio. They are somehow automatically registered when loading the model.
     * However, we need to load them manually and this class shows how this can be done. While this
     * is not nice and future proof, it works for the moment. This should be replaced by some proper
     * loading in the near future.
     */
    class DefaultFactory implements IPCMResourceSetPartitionFactory { // NOCS

        private static final String[] TOLOAD = { "pathmap://PCM_MODELS/Palladio.resourcetype" };

        @Override
        public PCMResourceSetPartition create() {
            final PCMResourceSetPartition rsp = new PCMResourceSetPartition();
            // final ResourceSet rs = new ResourceSetImpl();
            rsp.getResourceSet().setURIConverter(PalladioEclipseEnvironment.INSTANCE.getUriConverter());
            rsp.initialiseResourceSetEPackages(AbstractPCMWorkflowRunConfiguration.PCM_EPACKAGES);

            // load default models
            for (final String model : DefaultFactory.TOLOAD) {
                rsp.loadModel(URI.createURI(model));
            }

            rsp.resolveAllProxies();
            return rsp;
        }
    }
}
