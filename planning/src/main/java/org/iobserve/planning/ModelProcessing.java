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

import teetime.stage.basic.AbstractTransformation;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.analysis.snapshot.SnapshotBuilder;
import org.iobserve.model.PCMModelHandler;
import org.iobserve.planning.data.PlanningData;

/**
 * Stage for processing the PCM model before the model is used in PerOpteryx for generating
 * adaptation candidates. This stage performs the grouping of allocation contexts into allocation
 * groups to reduce the available degrees of freedom for the design space exploration.
 *
 * @author Tobias Pöppke
 *
 */
public class ModelProcessing extends AbstractTransformation<URI, PlanningData> {
    public static final String PROCESSED_MODEL_FOLDER = "processedModel";

    private final URI perOpteryxDir;
    private final URI lqnsDir;

    /**
     * Creates a new model processing stage and fills the planning data with the given location of
     * the headless PerOpteryx version. The planning data is passed on through all planning stages.
     *
     * @param perOpteryxDir
     *            the location of the headless PerOpteryx executable
     * @param lqnsDir
     *            directory for layered queuing networks
     */
    public ModelProcessing(final URI perOpteryxDir, final URI lqnsDir) {
        this.perOpteryxDir = perOpteryxDir;
        this.lqnsDir = lqnsDir;
    }

    @Override
    protected void execute(final URI element) throws Exception {
        CandidateGeneration.LOG.info("Model Processing");

        final PlanningData planningData = new PlanningData();

        final AdaptationData adaptationData = new AdaptationData();
        adaptationData.setRuntimeModelURI(element);
        planningData.setAdaptationData(adaptationData);
        planningData.setPerOpteryxDir(this.perOpteryxDir);
        planningData.setOriginalModelDir(element);
        planningData.setLqnsDir(this.lqnsDir);

        final File directory = new File(adaptationData.getReDeploymentURI().toFileString());

        final PCMModelHandler models = new PCMModelHandler(directory);
        final SnapshotBuilder snapshotBuilder = new SnapshotBuilder(ModelProcessing.PROCESSED_MODEL_FOLDER, models);

        final URI snapshotLocation = snapshotBuilder.createSnapshot();
        planningData.setProcessedModelDir(snapshotLocation);

        final ModelTransformer modelTransformer = new ModelTransformer(planningData);
        modelTransformer.transformModel();

        this.outputPort.send(planningData);
    }

}
