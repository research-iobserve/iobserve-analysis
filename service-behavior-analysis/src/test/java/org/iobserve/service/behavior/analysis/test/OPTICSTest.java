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

public class OPTICSTest {

    private OPTICS optics;
    private MTree<OpticsData> mtree;
    private List<OpticsData> models;

    @Before
    public void setupTestData() {
        this.models = new ArrayList<>();
        this.mtree = new MTree<>(new OpticsData.OPTICSDataGED(), null);

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
        for (final OpticsData model : this.models) {
            model.setOptics(this.optics);
        }
    }

    @Test
    public void opticsClusteringTest() {
        final List<OpticsData> resultList = this.optics.calculate();

        for (final OpticsData model : resultList) {
            System.out.println(model + " has " + model.getReachabilityDistance());
        }

        final int model_amount = resultList.size();

        if (model_amount < 50) {
            System.out.println("-------- ALERT ---- : " + model_amount);
        }

    }
}
