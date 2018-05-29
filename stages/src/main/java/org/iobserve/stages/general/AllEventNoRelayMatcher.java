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
package org.iobserve.stages.general;

import teetime.framework.OutputPort;

/**
 * This matcher accepts all events and does not relay them to any output port.
 *
 * @author Reiner Jung
 *
 */
public class AllEventNoRelayMatcher implements IEventMatcher<Object> {

    @Override
    public void setOutputPort(final OutputPort<Object> outputPort) {
        // nothing to be done here
    }

    @Override
    public <R> boolean matchEvent(final R event) {
        return true;
    }

    @Override
    public IEventMatcher<? extends Object> getNextMatcher() {
        return null;
    }

    @Override
    public OutputPort<Object> getOutputPort() {
        return null;
    }

    @Override
    public void setNextMatcher(final IEventMatcher<? extends Object> leaveEventMatcher) throws ConfigurationException {
        throw new ConfigurationException(
                "The DefaultMatcher accepts all events. Therefore, defining submatchers has no effect.");
    }

}
