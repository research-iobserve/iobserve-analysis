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
package org.iobserve.analysis.behavior.clustering.similaritymatching;

import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.iobserve.analysis.behavior.models.extended.BehaviorModel;
import org.iobserve.analysis.session.data.UserSession;

/**
 * Generalizes stages that aggregate user sessions based on a timer and generate behavior models for
 * them.
 *
 * @author Jannis Kuckei
 *
 */
public interface IClassificationStage {

    /**
     * Input port for user sessions.
     *
     * @return returns the proper port
     */
    InputPort<UserSession> getSessionInputPort();

    /**
     * Timer input port.
     *
     * @return returns the timer input port
     */
    InputPort<Long> getTimerInputPort();

    /**
     * Array of model outputs port.
     *
     * @return returns model output port
     */
    OutputPort<BehaviorModel[]> getOutputPort();
}
