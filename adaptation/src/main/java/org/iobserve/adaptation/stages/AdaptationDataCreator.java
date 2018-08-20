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

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.iobserve.adaptation.data.AdaptationData;

/**
 * Receives directories of the runtime model as well as the redeployment model and initializes the
 * adaptation data records needed by the adaptation and the legacy version of the execution.
 *
 * @author Lars Bluemke
 *
 */
public class AdaptationDataCreator extends CompositeStage {

    private final ModelCollector modelCollector;
    private final ModelGraphCreator modelGraphCreator;

    /**
     * adaptation data creator.
     */
    public AdaptationDataCreator() {
        this.modelCollector = new ModelCollector();
        this.modelGraphCreator = new ModelGraphCreator();

        this.connectPorts(this.modelCollector.getOutputPort(), this.modelGraphCreator.getInputPort());
        this.modelCollector.declareActive();
    }

    public InputPort<File> getRuntimeModelInputPort() {
        return this.modelCollector.getRuntimeModelInputPort();
    }

    public InputPort<File> getRedeploymentModelInputPort() {
        return this.modelCollector.getRedeploymentModelInputPort();
    }

    public OutputPort<AdaptationData> getOutputPort() {
        return this.modelGraphCreator.getOutputPort();
    }

}
