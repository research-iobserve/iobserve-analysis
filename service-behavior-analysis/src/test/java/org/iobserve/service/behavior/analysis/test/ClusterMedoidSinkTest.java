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
package org.iobserve.service.behavior.analysis.test;

import java.util.ArrayList;
import java.util.List;

import teetime.framework.test.StageTester;

import org.iobserve.service.behavior.analysis.ClusterMedoidSink;
import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;
import org.iobserve.service.behavior.analysis.model.generation.UserSessionToModelConverter;
import org.iobserve.stages.general.data.EntryCallEvent;
import org.iobserve.stages.general.data.PayloadAwareEntryCallEvent;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Lars Jürgensen
 *
 */
public class ClusterMedoidSinkTest {

    private ClusterMedoidSink sink;
    private BehaviorModelGED model;

    @Before
    public void setup() {
        // TODO is there a better way to test sink stages?
        this.sink = new ClusterMedoidSink("/home/lars/Desktop/SinkTest");
        this.model = this.createModel();
    }

    @Test
    public void noException() {
        System.out.println(this.model.getNodes());
        System.out.println(this.model.getEdges());

        StageTester.test(this.sink).and().send(this.model).to(this.sink.getInputPort()).and().start();
    }

    private BehaviorModelGED createModel() {

        final List<EntryCallEvent> events = new ArrayList<>();

        final long entryTime = 0;
        final long exitTime = 0;
        final String operationSignature = "/jpetstore/actions/Catalog.action";
        final String classSignature = "/jpetstore/actions/Catalog.action";
        final String sessionId = "";
        final String hostname = "";
        final String[] parameters = {};
        final String[] values = {};
        final int requestType = 0;

        final PayloadAwareEntryCallEvent event = new PayloadAwareEntryCallEvent(entryTime, exitTime, operationSignature,
                classSignature, sessionId, hostname, parameters, values, requestType);

        events.add(event);
        events.add(event);

        final BehaviorModelGED newModel = UserSessionToModelConverter.eventsToModel(events);

        return newModel;
    }

}
