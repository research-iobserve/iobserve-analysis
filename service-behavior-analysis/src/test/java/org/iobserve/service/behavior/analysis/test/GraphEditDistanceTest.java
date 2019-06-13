package org.iobserve.service.behavior.analysis.test;

import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;

public class GraphEditDistanceTest {

    private BehaviorModelGED model1;
    private BehaviorModelGED model2;

    private final BehaviorModelGED[] models = new BehaviorModelGED[10];

//    @Before
//    public void setupTestData() {
//
//        final Map<String, BehaviorModelNode> map = new HashMap<>();
//
//        final BehaviorModelNode node1 = new BehaviorModelNode("1");
//        map.put("1", node1);
//
//        final BehaviorModelNode node2 = new BehaviorModelNode("2");
//        node2.addEdge(null, node1);
//        map.put("2", node2);
//
//        final BehaviorModelNode node3 = new BehaviorModelNode("3");
//        map.put("3", node3);
//
//        final BehaviorModelNode node4 = new BehaviorModelNode("4");
//        node4.addEdge(null, node3);
//        map.put("4", node4);
//
//        final BehaviorModelNode node5 = new BehaviorModelNode("4");
//        node5.addEdge(null, node4);
//        node3.addEdge(null, node5);
//
//        map.put("5", node5);
//
//        this.model1 = new BehaviorModelGED(map);
//        this.model2 = new BehaviorModelGED(new HashMap<>());
//
//        for (int i = 0; i < 10; i++) {
//            final Map<String, BehaviorModelNode> nodes = new HashMap<>();
//
//            for (int j = 0; j < i; j++) {
//                nodes.put(Integer.toString(j), new BehaviorModelNode(Integer.toString(j)));
//            }
//
//            this.models[i] = new BehaviorModelGED(nodes);
//        }
//    }

//    @Test
//    public void calculateTest() {
//        final GraphEditDistance ged = new GraphEditDistance();
//        double result = ged.calculate(this.model1, this.model2);
//        Assert.assertTrue(result > 0);
//
//        result = ged.calculate(this.model1, this.model1);
//        Assert.assertTrue(result == 0);
//
//        result = ged.calculate(this.model2, this.model2);
//        Assert.assertTrue(result == 0);
//
//    }
//
//    @Test
//    public void mTreeTest() {
//        final MTree<BehaviorModelGED> tree = new MTree<>(new GraphEditDistance(), null);
//        for (int i = 0; i < 10; i++) {
//            tree.add(this.models[i]);
//
//        }
//        final Iterator<MTree<BehaviorModelGED>.ResultItem> results = tree.getNearestByLimit(this.models[5], 4)
//                .iterator();
//
//        while (results.hasNext()) {
//            System.out.println(results.next().data.getNodes().size());
//
//        }
//    }

}
