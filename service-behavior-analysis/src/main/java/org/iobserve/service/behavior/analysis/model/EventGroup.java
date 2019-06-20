/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.service.behavior.analysis.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iobserve.stages.general.data.PayloadAwareEntryCallEvent;

/**
 * The edge between two nodes of a BehaviorModel.
 *
 * @author Lars Jürgensen
 *
 */
public class EventGroup {

    private final String[] parameters;
    private final List<PayloadAwareEntryCallEvent> events;

    public EventGroup(final String[] parameters) {
        this.parameters = parameters;
        this.events = new ArrayList<>();
    }

    public String[] getParameters() {
        return this.parameters;
    }

    public List<PayloadAwareEntryCallEvent> getEvents() {
        return this.events;
    }

    public boolean hasSameParameters(final PayloadAwareEntryCallEvent event) {
        return Arrays.equals(event.getParameters(), this.parameters);
    }

    public boolean hasSameParameters(final EventGroup eventGroup) {
        return Arrays.equals(eventGroup.getParameters(), this.parameters);
    }

}
