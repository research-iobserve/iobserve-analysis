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
package org.iobserve.analysis.clustering.filter.similaritymatching;

import org.iobserve.analysis.clustering.behaviormodels.BehaviorModel;
import org.iobserve.analysis.clustering.behaviormodels.UserSessionToBehaviorModelConverter;
import org.iobserve.analysis.session.data.UserSession;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

public class TSessionToModel extends AbstractConsumerStage<UserSession> {
    private final OutputPort<BehaviorModel> outputPort = this.createOutputPort();

    @Override
    public void execute(final UserSession session) {
        this.outputPort.send(UserSessionToBehaviorModelConverter.convert(session));
    }

    public OutputPort<BehaviorModel> getOutputPort() {
        return this.outputPort;
    }
}