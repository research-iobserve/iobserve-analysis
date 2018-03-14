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
package org.iobserve.evaluation;

import java.io.File;

import teetime.stage.basic.AbstractTransformation;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.analysis.data.graph.GraphFactory;
import org.iobserve.analysis.data.graph.ModelGraph;
import org.iobserve.model.PCMModelHandler;

/**
 *
 * TODO add description.
 *
 * @author unknown.
 *
 */
public class ModelComparer extends AbstractTransformation<URI, Boolean> {

    private AdaptationData baseAdaptationData;

    /**
     * Create a model comparer stage.
     */
    public ModelComparer() {
        // empty default constructor
    }

    @Override
    protected void execute(final URI element) throws Exception {

        boolean equalGraphs = false;
        if (this.baseAdaptationData != null) {
            // TODO finish
            final PCMModelHandler modelProviders = new PCMModelHandler(new File(element.toFileString()));
            final GraphFactory graphFactory = new GraphFactory();
            final ModelGraph runtimeGraph = graphFactory.buildGraph(modelProviders);

            if (runtimeGraph.equals(this.baseAdaptationData.getReDeploymentGraph())
                    && this.baseAdaptationData.getReDeploymentGraph().equals(runtimeGraph)) {
                equalGraphs = true;
                SystemEvaluation.disableEvaluation();
            }
        }
        this.outputPort.send(Boolean.valueOf(equalGraphs));
    }

    public void setBaseAdaptationData(final AdaptationData baseAdaptationData) {
        this.baseAdaptationData = baseAdaptationData;
    }

}
