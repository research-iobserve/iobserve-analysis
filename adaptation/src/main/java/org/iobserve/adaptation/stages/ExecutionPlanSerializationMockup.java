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

import teetime.framework.AbstractProducerStage;

/**
 * A mockup of the {@link ExecutionPlanSerialization} stage. Instead of getting an execution plan as
 * an object, an already serialized execution plan file is set in the constructor and then passed to
 * the stage's output port.
 *
 * @author Lars Bluemke
 *
 */
public class ExecutionPlanSerializationMockup extends AbstractProducerStage<File> {

    private final File executionPlan;

    public ExecutionPlanSerializationMockup(final File executionPlanURI) {
        this.executionPlan = executionPlanURI;
    }

    @Override
    protected void execute() throws Exception {
        this.outputPort.send(this.executionPlan);
    }

}
