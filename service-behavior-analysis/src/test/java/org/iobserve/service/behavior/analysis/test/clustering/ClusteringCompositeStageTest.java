package org.iobserve.service.behavior.analysis.test.clustering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import teetime.framework.test.StageTester;

import org.iobserve.service.behavior.analysis.Clustering;
import org.iobserve.service.behavior.analysis.clustering.ClusteringCompositeStage;
import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;
import org.iobserve.service.behavior.analysis.model.BehaviorModelNode;
import org.junit.Before;
import org.junit.Test;

public class ClusteringCompositeStageTest {

    private final ClusteringCompositeStage clustering = new ClusteringCompositeStage();
    private List<BehaviorModelGED> models = new ArrayList<>();

    @Before
    public void setup() {
        this.models = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            final Map<String, BehaviorModelNode> nodes = new HashMap<>();

            final Random rand = new Random();
            final int nodeAmount = rand.nextInt(100);
            for (int j = 0; j < nodeAmount; j++) {
                nodes.put(Integer.toString(j), new BehaviorModelNode(Integer.toString(j)));
            }
        }
    }

    @Test
    public void clusteringTest() {
        final List<Clustering> solutions = new ArrayList<>();
        StageTester.test(this.clustering).and().send(this.models).to(this.clustering.getModelInputPort()).and()
                .receive(solutions).from(this.clustering.getOutputPort()).start();

        for (final Set<BehaviorModelGED> cluster : solutions.get(0).getClusters()) {
            for (final BehaviorModelGED model : cluster) {

                System.out.println(model.getNodes().size());
            }
        }
    }
}
