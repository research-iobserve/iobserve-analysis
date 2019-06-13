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
package org.iobserve.service.behavior.analysis.test.clustering;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import teetime.framework.test.StageTester;

import org.iobserve.service.behavior.analysis.clustering.Clustering;
import org.iobserve.service.behavior.analysis.clustering.ClusteringCompositeStage;
import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;
import org.iobserve.service.behavior.analysis.model.BehaviorModelNode;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Lars Jürgensen
 *
 */
public class ClusteringCompositeStageTest {

    private final ClusteringCompositeStage clustering = new ClusteringCompositeStage(true, 9999);
    private List<BehaviorModelGED> models = new ArrayList<>();

    @Before
    public void setup() {
        this.models = new ArrayList<>();

        for (int i = 0; i < 10000; i++) {
            final BehaviorModelGED newModel = new BehaviorModelGED();

            final Map<String, BehaviorModelNode> nodes = newModel.getNodes();

            final Random rand = new Random();
            final int nodeAmount = rand.nextInt(30);
            for (int j = 0; j < nodeAmount; j++) {
                nodes.put(Integer.toString(j), new BehaviorModelNode(Integer.toString(j)));
            }
            this.models.add(newModel);
        }
    }

    @Test
    public void clusteringTest() {
        final List<Clustering> solutions = new ArrayList<>();
        System.out.println(this.models);
        StageTester.test(this.clustering).and().send(this.models).to(this.clustering.getModelInputPort()).and()
                .send(new ArrayList<Long>()).to(this.clustering.getTimerInputPort()).and().receive(solutions)
                .from(this.clustering.getOutputPort()).start();

        if (solutions.size() > 0) {
            for (final Clustering solution : solutions) {
                // System.out.println("new solution");

                for (final Set<BehaviorModelGED> cluster : solution.getClusters()) {
                    // System.out.println("new cluster");

                    for (final BehaviorModelGED model : cluster) {

                        // System.out.println(model.getNodes().size());
                    }
                }
            }
        }
        System.out.println("finished");

    }
}
