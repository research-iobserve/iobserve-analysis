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
import java.util.Set;

import teetime.framework.test.StageTester;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.iobserve.service.behavior.analysis.clustering.Clustering;
import org.iobserve.service.behavior.analysis.clustering.ExtractDBScanClusters;
import org.iobserve.service.behavior.analysis.clustering.OpticsData;
import org.iobserve.service.behavior.analysis.clustering.OpticsStage;
import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;
import org.iobserve.service.behavior.analysis.test.TestHelper;
import org.junit.Before;
import org.junit.Test;

import mtree.MTree;

/**
 * Only tested with identical models. The evaluation showed it is able to cluster more complex data
 *
 * @author Lars Jürgensen
 *
 */
public class OpticsStageTest {

    private MTree<OpticsData> mTree;
    private List<OpticsData> models;

    @Before
    public void setup() {
        final List<BehaviorModelGED> behaviorModels = new ArrayList<>();

        behaviorModels.add(TestHelper.createBehaviorModelA());
        behaviorModels.add(TestHelper.createBehaviorModelA());
        behaviorModels.add(TestHelper.createBehaviorModelA());
        behaviorModels.add(TestHelper.createBehaviorModelA());
        behaviorModels.add(TestHelper.createBehaviorModelA());

        behaviorModels.add(TestHelper.createBehaviorModelB());
        behaviorModels.add(TestHelper.createBehaviorModelB());
        behaviorModels.add(TestHelper.createBehaviorModelB());
        behaviorModels.add(TestHelper.createBehaviorModelB());
        behaviorModels.add(TestHelper.createBehaviorModelB());

        behaviorModels.add(TestHelper.createBehaviorModelC());
        behaviorModels.add(TestHelper.createBehaviorModelC());
        behaviorModels.add(TestHelper.createBehaviorModelC());
        behaviorModels.add(TestHelper.createBehaviorModelC());
        behaviorModels.add(TestHelper.createBehaviorModelC());

        behaviorModels.add(TestHelper.createBehaviorModelD());
        behaviorModels.add(TestHelper.createBehaviorModelD());
        behaviorModels.add(TestHelper.createBehaviorModelD());
        behaviorModels.add(TestHelper.createBehaviorModelD());
        behaviorModels.add(TestHelper.createBehaviorModelD());

        // noise
        behaviorModels.add(TestHelper.createBehaviorModelE());
        behaviorModels.add(TestHelper.createBehaviorModelE());

        this.models = new ArrayList<>();

        for (final BehaviorModelGED model : behaviorModels) {
            this.models.add(new OpticsData(model));
        }

        this.mTree = TestHelper.generateMTree(this.models);
    }

    @Test
    public void test() {

        final List<OpticsData> opticsPlot = OpticsStageTest.runClusterStage(this.models, this.mTree);
        // check, that no models are lost
        MatcherAssert.assertThat(opticsPlot.size(), Matchers.is(22));

        final Clustering<BehaviorModelGED> clustering = OpticsStageTest.runExtractionStage(opticsPlot);

        // two noise objects
        MatcherAssert.assertThat(clustering.getNoise().size(), Matchers.is(2));

        // four clusters
        MatcherAssert.assertThat(clustering.getClusters().size(), Matchers.is(4));

        // every cluster contain 5 elements
        for (final Set<BehaviorModelGED> cluster : clustering.getClusters()) {
            MatcherAssert.assertThat(cluster.size(), Matchers.is(5));
        }
    }

    public static List<OpticsData> runClusterStage(final List<OpticsData> models, final MTree<OpticsData> mTree) {
        // prepare input
        final List<List<OpticsData>> modelsInputList = new ArrayList<>();
        modelsInputList.add(models);

        // prepare input
        final List<MTree<OpticsData>> mTreeInputList = new ArrayList<>();
        mTreeInputList.add(mTree);

        // these are the clustering arguments
        final OpticsStage optics = new OpticsStage(0, 4);

        final List<List<OpticsData>> solutions = new ArrayList<>();

        StageTester.test(optics).and().send(modelsInputList).to(optics.getModelsInputPort()).and().send(mTreeInputList)
                .to(optics.getmTreeInputPort()).and().receive(solutions).from(optics.getOutputPort()).start();

        return solutions.get(0);
    }

    public static Clustering<BehaviorModelGED> runExtractionStage(final List<OpticsData> models) {
        // prepare input
        final List<List<OpticsData>> inputList = new ArrayList<>();
        inputList.add(models);

        // clustering distance = 0 => only identical objects should be in a cluster
        final ExtractDBScanClusters extraction = new ExtractDBScanClusters(0);

        final List<Clustering<BehaviorModelGED>> solutions = new ArrayList<>();

        StageTester.test(extraction).and().send(inputList).to(extraction.getInputPort()).and().receive(solutions)
                .from(extraction.getOutputPort()).start();

        return solutions.get(0);
    }
}
