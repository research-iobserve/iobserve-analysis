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

import org.iobserve.adaptation.executionplan.ExecutionPlan;
import org.iobserve.adaptation.stages.AtomicActionComputation;
import org.iobserve.planning.systemadaptation.SystemAdaptation;

/**
 * Test configuration for {@link AtomicActionComputationTest}. As the output of that stage contains
 * EObjects we could not use teetime's {@link StageTester} api because the ids of actual and
 * expected Objects always differed.
 *
 * @author Lars Bluemke
 *
 */
public class AtomicActionComputationTestConfig extends Configuration {

    public AtomicActionComputationTestConfig(final InitialElementProducer<SystemAdaptation> producer,
            final AtomicActionComputation atomicActionComputation, final CollectorSink<ExecutionPlan> collector) {
        this.connectPorts(producer.getOutputPort(), atomicActionComputation.getInputPort());
        this.connectPorts(atomicActionComputation.getOutputPort(), collector.getInputPort());
    }

}
