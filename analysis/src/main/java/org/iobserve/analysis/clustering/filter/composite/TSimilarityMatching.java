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
package org.iobserve.analysis.clustering.filter.composite;

import org.iobserve.analysis.clustering.filter.IParameterMetricStrategy;
import org.iobserve.analysis.clustering.filter.IStructureMetricStrategy;
import org.iobserve.analysis.clustering.filter.TGroupingStage;
import org.iobserve.analysis.clustering.filter.TModelGeneration;
import org.iobserve.analysis.clustering.filter.TSessionToModel;
import org.iobserve.analysis.clustering.filter.TVectorization;
import org.iobserve.analysis.session.data.UserSession;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;

public class TSimilarityMatching extends CompositeStage {
    private final InputPort<UserSession> sessionInputPort;
    private final InputPort<Long> timerInputPort;

    public TSimilarityMatching(final IStructureMetricStrategy structureMetric,
            final IParameterMetricStrategy parameterMetric, final double similarityRadius) {
        /** Create individual stages */
        final TSessionToModel sessionToModel = new TSessionToModel();
        final TVectorization vectorization = new TVectorization(structureMetric, parameterMetric);
        final TGroupingStage groupingStage = new TGroupingStage(similarityRadius);
        final TModelGeneration modelGeneration = new TModelGeneration();

        /** Connect ports */
        this.sessionInputPort = sessionToModel.getInputPort();
        this.timerInputPort = vectorization.getTimerInputPort();
        this.connectPorts(sessionToModel.getOutputPort(), vectorization.getModelInputPort());
        this.connectPorts(vectorization.getOutputPort(), groupingStage.getInputPort());
        // this.connectPorts(groupingStage.getOutputPort(), modelGeneration.get);
    }

    public InputPort<UserSession> getSessionInputPort() {
        return this.sessionInputPort;
    }

    public InputPort<UserSession> getTimerInputPort() {
        return this.timerInputPort;
    }
}
