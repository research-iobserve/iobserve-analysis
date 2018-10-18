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
package org.iobserve.adaptation.stages.tests;

import teetime.framework.Configuration;
import teetime.framework.test.StageTester;
import teetime.stage.CollectorSink;
import teetime.stage.InitialElementProducer;

import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.adaptation.stages.ComposedActionComputation;
import org.iobserve.adaptation.stages.ComposedActionFactoryInitialization;
import org.iobserve.planning.systemadaptation.SystemAdaptation;

/**
 * Test configuration for {@link ComposedActionComputationTest}. As the output of that stage
 * contains EObjects we could not use teetime's {@link StageTester} api because the ids of actual
 * and expected Objects always differed.
 *
 * @author Lars Bluemke
 *
 */
public class ComposedActionComputationTestConfig extends Configuration {

    /**
     * Create configuration for composed actions.
     *
     * @param producer
     *            event producer
     * @param actionFactoryInitializer
     *            action factory
     * @param composedActionComputation
     *            stage to process composed actions
     * @param collector
     *            result sink
     */

    public ComposedActionComputationTestConfig(final InitialElementProducer<AdaptationData> producer,
            final ComposedActionFactoryInitialization actionFactoryInitializer,
            final ComposedActionComputation composedActionComputation,
            final CollectorSink<SystemAdaptation> collector) {
        this.connectPorts(producer.getOutputPort(), actionFactoryInitializer.getInputPort());
        this.connectPorts(actionFactoryInitializer.getOutputPort(), composedActionComputation.getInputPort());
        this.connectPorts(composedActionComputation.getOutputPort(), collector.getInputPort());
    }
}
