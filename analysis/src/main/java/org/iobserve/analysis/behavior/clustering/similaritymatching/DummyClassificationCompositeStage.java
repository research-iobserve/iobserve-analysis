/***************************************************************************
 * Copyright 2018 iObserve Project (https://www.iobserve-devops.net)
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

import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.iobserve.analysis.behavior.filter.IClassificationStage;
import org.iobserve.analysis.behavior.models.extended.BehaviorModel;
import org.iobserve.analysis.session.data.UserSession;

/**
 *
 * @author Reiner Jung
 *
 */
public class DummyClassificationCompositeStage extends CompositeStage implements IClassificationStage {

    private final DummyClassificationStage dummyClassificationStage;

    /**
     * Create a dummy stage.
     *
     * @param configuration
     *            configuration parameters
     * @throws ConfigurationException
     *             on configuration errors
     */
    public DummyClassificationCompositeStage(final Configuration configuration) throws ConfigurationException {
        this.dummyClassificationStage = new DummyClassificationStage();
    }

    @Override
    public InputPort<UserSession> getSessionInputPort() {
        return this.dummyClassificationStage.getSessionInputPort();
    }

    @Override
    public InputPort<Long> getTimerInputPort() {
        return this.dummyClassificationStage.getTimerInputPort();
    }

    @Override
    public OutputPort<BehaviorModel> getOutputPort() {
        return this.dummyClassificationStage.getOutputPort();
    }

}
