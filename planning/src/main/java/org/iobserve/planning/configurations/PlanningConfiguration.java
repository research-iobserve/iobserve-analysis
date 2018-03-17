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
package org.iobserve.planning.configurations;

import teetime.framework.Configuration;

import org.eclipse.emf.common.util.URI;
import org.iobserve.planning.CandidateGeneration;
import org.iobserve.planning.CandidateProcessing;
import org.iobserve.planning.ModelOptimization;
import org.iobserve.planning.ModelProcessing;

/**
 * Configuration for the stages of the planning service.
 *
 * @author Lars Bluemke
 *
 */
public class PlanningConfiguration extends Configuration {

    public PlanningConfiguration(final URI perOpteryxHeadless, final URI lqnsDir) {

        if (perOpteryxHeadless != null && lqnsDir != null) {
            // create filters for snapshot planning, evaluation and adaptation
            final CandidateGeneration candidateGenerator = new CandidateGeneration(
                    new ModelProcessing(perOpteryxHeadless, lqnsDir), new ModelOptimization(),
                    new CandidateProcessing());
        }

        // TODO for lbl: Implement a way to pass data to the following stages
        // Path Planning => Adaptation
        // this.connectPorts(candidateGenerator.getOutputPort(), systemAdaptor.getInputPort());
    }
}
