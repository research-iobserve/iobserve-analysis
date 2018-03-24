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
package org.iobserve.adaptation.droolsstages;

import java.util.List;

import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.planning.systemadaptation.Action;

import teetime.framework.Configuration;
import teetime.framework.test.StageTester;
import teetime.stage.CollectorSink;
import teetime.stage.InitialElementProducer;

/**
 * Test configuration for {@link ComposedActionComputationTest}. As the output of that stage
 * contains EObjects we could not use teetime's {@link StageTester} api because the ids of actual
 * and expected Objects always differed.
 *
 * @author Lars Bluemke
 *
 */
public class ComposedActionComputationTestConfig extends Configuration {

    public ComposedActionComputationTestConfig(final InitialElementProducer<AdaptationData> producer,
            final ComposedActionComputation composedActionComputation, final CollectorSink<List<Action>> collector) {
        this.connectPorts(producer.getOutputPort(), composedActionComputation.getInputPort());
        this.connectPorts(composedActionComputation.getOutputPort(), collector.getInputPort());
    }
}
