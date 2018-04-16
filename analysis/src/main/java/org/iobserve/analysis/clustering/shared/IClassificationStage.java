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
package org.iobserve.analysis.clustering.shared;

import kieker.common.configuration.Configuration;

import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.iobserve.analysis.behavior.models.basic.BehaviorModel;
import org.iobserve.analysis.session.data.UserSession;
import org.iobserve.stages.general.ConfigurationException;

/**
 * Generalises stages that aggregate user sessions based on a timer and generate behavior models for
 * them.
 *
 * @author Jannis Kuckei
 * @author Melf Lorenzen
 *
 */
public interface IClassificationStage {

    /**
     * Sets up the classification stage with respect to the configuration.
     * 
     * @param configuration
     *            the configuration object for the classification stage
     * @throws ConfigurationException
     *             if the configuration does not contain all necessary keys
     */
    void setupStage(final Configuration configuration) throws ConfigurationException;

    /**
     * get matching input port for the UserSession.
     *
     * @return input port
     */
    InputPort<UserSession> getSessionInputPort();

    /**
     * get matching input port for the timer signal.
     *
     * @return input port
     */
    InputPort<Long> getTimerInputPort();

    /**
     * get suitable output port.
     *
     * @return outputPort
     */
    OutputPort<BehaviorModel> getOutputPort();
}