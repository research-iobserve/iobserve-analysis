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
import java.util.Map;
import java.util.Random;

import org.iobserve.service.behavior.analysis.clustering.OPTICS;
import org.iobserve.service.behavior.analysis.clustering.OpticsData;
import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;
import org.iobserve.service.behavior.analysis.model.BehaviorModelNode;
import org.junit.Before;
import org.junit.Test;

import mtree.MTree;

/**
 *
 * @author Lars Jürgensen
 *
 */
public class OPTICSTest {

    private OPTICS optics;
    private MTree<OpticsData> mtree;
    private List<OpticsData> models;

    @Before
    public void setupTestData() {
        this.models = new ArrayList<>();
        this.mtree = new MTree<>(OpticsData.getDistanceFunction(), null);

        for (int i = 0; i < 50; i++) {
            final BehaviorModelGED model = new BehaviorModelGED();

            final Map<String, BehaviorModelNode> nodes = model.getNodes();

            final Random rand = new Random();
            final int nodeAmount = rand.nextInt(100);
            for (int j = 0; j < nodeAmount; j++) {
                nodes.put(Integer.toString(j), new BehaviorModelNode(Integer.toString(j)));
            }

            final OpticsData newModel = new OpticsData(model);
            this.models.add(newModel);
            this.mtree.add(newModel);

        }
        this.optics = new OPTICS(this.mtree, 50, 5, this.models);

    }

    @Test
    public void opticsClusteringTest() {
        final List<OpticsData> resultList = this.optics.calculate();

        for (final OpticsData model : resultList) {
            System.out.println(model + " has " + model.getReachabilityDistance());
        }

    }
}
