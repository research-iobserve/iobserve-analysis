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
package org.iobserve.adaptation;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.analysis.data.graph.GraphFactory;
import org.iobserve.analysis.data.graph.ModelGraph;
import org.iobserve.model.PCMModelHandler;

import teetime.stage.basic.AbstractTransformation;

/**
 * This class selects a created candidate and creates all required information for further
 * processing.
 *
 * @author Philipp Weimann
 */
public class CandidateProcessing extends AbstractTransformation<File, AdaptationData> {

    @Override
    protected void execute(final File modelDirectory) throws Exception {
        final AdaptationData adaptationData = new AdaptationData();
        adaptationData.setRuntimeModelURI(URI.createFileURI(modelDirectory.getAbsolutePath()));

        final GraphFactory factory = new GraphFactory();
        final File directory = new File(adaptationData.getReDeploymentURI().toFileString());
        final ModelGraph graph = factory.buildGraph(new PCMModelHandler(directory));
        adaptationData.setReDeploymentGraph(graph);

        this.outputPort.send(adaptationData);
    }

}
