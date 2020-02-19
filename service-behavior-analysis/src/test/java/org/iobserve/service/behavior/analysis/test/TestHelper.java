/***************************************************************************
 * Copyright (C) 2019 iObserve Project (https://www.iobserve-devops.net)
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

import java.util.List;

import org.iobserve.service.behavior.analysis.clustering.GraphEditDistance;
import org.iobserve.service.behavior.analysis.clustering.OpticsData;
import org.iobserve.service.behavior.analysis.model.BehaviorModelEdge;
import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;
import org.iobserve.service.behavior.analysis.model.BehaviorModelNode;
import org.iobserve.stages.general.data.PayloadAwareEntryCallEvent;

import mtree.MTree;

/**
 *
 * @author Lars Jürgensen
 *
 */
public final class TestHelper {

    private TestHelper() {
    };

    public static PayloadAwareEntryCallEvent createEvent(final int time, final String name, final String[] parameters,
            final String[] values) {
        return new PayloadAwareEntryCallEvent(time, time + 1, name, name, "session", "host", parameters, values, 0);

    }

    public static BehaviorModelGED createBehaviorModelA() {

        final BehaviorModelGED model = new BehaviorModelGED();

        TestHelper.addNode(model, "Init");
        TestHelper.addNode(model, "A");
        TestHelper.addNode(model, "B");
        TestHelper.addNode(model, "C");

        final String[] parameters = {};
        final String[] values = {};

        final BehaviorModelEdge edge1 = TestHelper.addEdge(model, "Init", "A");

        edge1.addEvent(TestHelper.createEvent(0, "A", parameters, values));

        final BehaviorModelEdge edge2 = TestHelper.addEdge(model, "A", "B");
        edge2.addEvent(TestHelper.createEvent(1, "A", parameters, values));

        final BehaviorModelEdge edge3 = TestHelper.addEdge(model, "B", "C");
        edge3.addEvent(TestHelper.createEvent(2, "C", parameters, values));

        return model;

    }

    // only difference to A: different values
    public static BehaviorModelGED createBehaviorModelB() {

        final BehaviorModelGED model = new BehaviorModelGED();

        TestHelper.addNode(model, "Init");
        TestHelper.addNode(model, "A");
        TestHelper.addNode(model, "B");
        TestHelper.addNode(model, "C");

        final String[] parameters = {};
        final String[] values = { "different value" };

        final BehaviorModelEdge edge1 = TestHelper.addEdge(model, "Init", "A");

        edge1.addEvent(TestHelper.createEvent(0, "A", parameters, values));

        final BehaviorModelEdge edge2 = TestHelper.addEdge(model, "A", "B");
        edge2.addEvent(TestHelper.createEvent(1, "A", parameters, values));

        final BehaviorModelEdge edge3 = TestHelper.addEdge(model, "B", "C");
        edge3.addEvent(TestHelper.createEvent(2, "C", parameters, values));

        return model;
    }

    // only difference to A: different parameterNames
    public static BehaviorModelGED createBehaviorModelC() {

        final BehaviorModelGED model = new BehaviorModelGED();

        TestHelper.addNode(model, "Init");
        TestHelper.addNode(model, "A");
        TestHelper.addNode(model, "B");
        TestHelper.addNode(model, "C");

        final String[] parameters = { "different parameters" };
        final String[] values = {};

        final BehaviorModelEdge edge1 = TestHelper.addEdge(model, "Init", "A");

        edge1.addEvent(TestHelper.createEvent(0, "A", parameters, values));

        final BehaviorModelEdge edge2 = TestHelper.addEdge(model, "A", "B");
        edge2.addEvent(TestHelper.createEvent(1, "A", parameters, values));

        final BehaviorModelEdge edge3 = TestHelper.addEdge(model, "B", "C");
        edge3.addEvent(TestHelper.createEvent(2, "C", parameters, values));

        return model;
    }

    // only difference to A: additional edge
    public static BehaviorModelGED createBehaviorModelD() {

        final BehaviorModelGED model = new BehaviorModelGED();

        TestHelper.addNode(model, "Init");
        TestHelper.addNode(model, "A");
        TestHelper.addNode(model, "B");
        TestHelper.addNode(model, "C");

        final String[] parameters = {};
        final String[] values = {};

        final BehaviorModelEdge edge1 = TestHelper.addEdge(model, "Init", "A");

        edge1.addEvent(TestHelper.createEvent(0, "A", parameters, values));

        final BehaviorModelEdge edge2 = TestHelper.addEdge(model, "A", "B");
        edge2.addEvent(TestHelper.createEvent(1, "A", parameters, values));

        final BehaviorModelEdge edge3 = TestHelper.addEdge(model, "B", "C");
        edge3.addEvent(TestHelper.createEvent(2, "C", parameters, values));

        final BehaviorModelEdge edge4 = TestHelper.addEdge(model, "C", "B");
        edge3.addEvent(TestHelper.createEvent(3, "B", parameters, values));

        return model;

    }

    // only difference to A: additional node
    public static BehaviorModelGED createBehaviorModelE() {

        final BehaviorModelGED model = new BehaviorModelGED();

        TestHelper.addNode(model, "Init");
        TestHelper.addNode(model, "A");
        TestHelper.addNode(model, "B");
        TestHelper.addNode(model, "C");
        TestHelper.addNode(model, "D"); // unconnected, but sould still increase distance

        final String[] parameters = {};
        final String[] values = {};

        final BehaviorModelEdge edge1 = TestHelper.addEdge(model, "Init", "A");

        edge1.addEvent(TestHelper.createEvent(0, "A", parameters, values));

        final BehaviorModelEdge edge2 = TestHelper.addEdge(model, "A", "B");
        edge2.addEvent(TestHelper.createEvent(1, "A", parameters, values));

        final BehaviorModelEdge edge3 = TestHelper.addEdge(model, "B", "C");
        edge3.addEvent(TestHelper.createEvent(2, "C", parameters, values));

        return model;
    }

    public static void addNode(final BehaviorModelGED model, final String nodeName) {
        final BehaviorModelNode node = new BehaviorModelNode(nodeName);
        model.getNodes().put(nodeName, node);
    }

    public static BehaviorModelEdge addEdge(final BehaviorModelGED model, final String sourceName,
            final String targetName) {
        final BehaviorModelNode source = model.getNodes().get(sourceName);
        final BehaviorModelNode target = model.getNodes().get(targetName);

        final BehaviorModelEdge edge = new BehaviorModelEdge(source, target);
        source.getOutgoingEdges().put(target, edge);
        source.getIngoingEdges().put(source, edge);
        model.getEdges().add(edge);
        return edge;
    }

    public static MTree<OpticsData> generateMTree(final List<OpticsData> models) {

        final GraphEditDistance ged = new GraphEditDistance();
        final MTree<OpticsData> mtree = new MTree<>(20, 40, OpticsData.getDistanceFunction(), null);

        for (final OpticsData model : models) {
            mtree.add(model);
        }

        return mtree;
    }

}
