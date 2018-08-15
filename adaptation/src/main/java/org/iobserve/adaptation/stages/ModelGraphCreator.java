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
package org.iobserve.adaptation.stages;

import java.io.File;

import teetime.stage.basic.AbstractFilter;

import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.adaptation.data.graph.HostComponentAllocationGraph;
import org.iobserve.adaptation.data.graph.HostComponentAllocationGraphFactory;
import org.iobserve.adaptation.data.graph.ModelGraphRevision;
import org.iobserve.model.ModelImporter;

/**
 * Receives AdaptationData, creates a ModelGraph and adds it to the AdaptationData.
 *
 * @author Lars Bluemke
 *
 */
public class ModelGraphCreator extends AbstractFilter<AdaptationData> {

    @Override
    protected void execute(final AdaptationData adaptationData) throws Exception {
        final HostComponentAllocationGraphFactory runtimeFactory = new HostComponentAllocationGraphFactory();
        final HostComponentAllocationGraphFactory redeploymentFactory = new HostComponentAllocationGraphFactory();

        final File runtimeModelDir = adaptationData.getRuntimeModelDir();
        final File redeploymentModelDir = adaptationData.getReDeploymentModelDir();

        final HostComponentAllocationGraph runtimeModelGraph = runtimeFactory
                .buildGraph(new ModelImporter(runtimeModelDir), ModelGraphRevision.RUNTIME);
        final HostComponentAllocationGraph redeploymentModelGraph = redeploymentFactory
                .buildGraph(new ModelImporter(redeploymentModelDir), ModelGraphRevision.REDEPLOYMENT);

        adaptationData.setRuntimeGraph(runtimeModelGraph);
        adaptationData.setReDeploymentGraph(redeploymentModelGraph);

        this.getOutputPort().send(adaptationData);
    }

}
