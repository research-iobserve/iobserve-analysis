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
package org.iobserve.adaptation.stages.transformations;

import teetime.stage.basic.AbstractTransformation;

import org.iobserve.adaptation.stages.signal.AllActionsSentSignal;
import org.iobserve.planning.systemadaptation.ComposedAction;
import org.iobserve.planning.systemadaptation.SystemAdaptation;

/**
 * Sends the actions included in the system adaptation model to its output port.
 *
 * @author Lars Bluemke
 *
 */
public class SystemAdaptationModel2ComposedActions extends AbstractTransformation<SystemAdaptation, ComposedAction> {

    @Override
    protected void execute(final SystemAdaptation systemAdaptationModel) throws Exception {

        for (final ComposedAction composedAction : systemAdaptationModel.getActions()) {
            this.outputPort.send(composedAction);
        }

        this.outputPort.sendSignal(new AllActionsSentSignal());
    }

}
