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
package org.iobserve.planning;

import java.io.File;

import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.data.graph.GraphFactory;
import org.iobserve.analysis.data.graph.ModelGraph;
import org.iobserve.planning.data.PlanningData;

import teetime.stage.basic.AbstractTransformation;

/**
 * This class selects a created candidate and creates all required information for further
 * processing.
 *
 * @author Philipp Weimann
 */
public class CandidateProcessing extends AbstractTransformation<PlanningData, AdaptationData> {

    @Override
    protected void execute(final PlanningData element) throws Exception {
        CandidateGeneration.LOG.info("Candiate Processing");
        final AdaptationData adapdationData = element.getAdaptationData();

        final GraphFactory factory = new GraphFactory();
        final ModelGraph graph = factory
                .buildGraph(new InitializeModelProviders(new File(adapdationData.getReDeploymentURI().toFileString())));
        element.getAdaptationData().setReDeploymentGraph(graph);

        this.outputPort.send(element.getAdaptationData());
    }

}
