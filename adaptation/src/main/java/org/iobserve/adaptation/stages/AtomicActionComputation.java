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

import org.iobserve.adaptation.executionplan.ExecutionPlan;
import org.iobserve.planning.systemadaptation.SystemAdaptation;

import teetime.stage.basic.AbstractTransformation;

/**
 * Receives a {@link SystemAdaptation} model containing a list of composed adaptation actions and
 * computes the required atomic adaptation actions - the execution plan - which is passed to the
 * output port.
 *
 * @author Lars Bluemke
 *
 */
public class AtomicActionComputation extends AbstractTransformation<SystemAdaptation, ExecutionPlan> {

    @Override
    protected void execute(final SystemAdaptation systemAdaptationModel) throws Exception {
        // TODO Auto-generated method stub

    }

}
