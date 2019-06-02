package org.iobserve.service.behavior.analysis;

import java.util.Iterator;

import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;

import mtree.DistanceFunction;
import mtree.MTree;

public class OpticsData {
    private OPTICS optics;

    public static final int Undefined = -2;
    public static final int Not_Initialized = -1;

    private double reachabilityDistance = OpticsData.Undefined;

    private double core_distance = OpticsData.Not_Initialized;
    private boolean visited = false;

    private final OPTICSDataGED ged = new OPTICSDataGED();
    private final BehaviorModelGED data;

    public OpticsData(final BehaviorModelGED data) {
        this.data = data;
    }

    // TODO maybe we have to look for one more result, because mtree starts with the object itself
    public double getCoreDistance() {
        if (this.core_distance == OpticsData.Not_Initialized) {
            final Iterator<MTree<OpticsData>.ResultItem> results = this.optics.getMtree()
                    .getNearest(this, this.optics.getMaxDistance(), this.optics.getMinPTs()).iterator();

            int resultAmount = 0;
            OpticsData last = null;

            while (results.hasNext()) {
                resultAmount++;
                last = results.next().data;
            }
            // System.out.println("getMaxDistance: " + this.optics.getMaxDistance());

            // System.out.println("result amount: " + resultAmount);
            if (resultAmount < this.optics.getMinPTs()) {
                this.core_distance = OpticsData.Undefined;

            }

            else {

                this.core_distance = this.ged.calculate(this, last);

            }

        }
        return this.core_distance;

    }

    public double distanceTo(final OpticsData model) {

        return this.ged.calculate(this, model);
    }

    public void setCore_distance(final double core_distance) {
        this.core_distance = core_distance;
    }

    public double getReachabilityDistance() {
        return this.reachabilityDistance;
    }

    public boolean isVisited() {
        return this.visited;
    }

    public BehaviorModelGED getData() {
        return this.data;
    }

    public void setVisited(final boolean visited) {
        this.visited = visited;
    }

    public void setReachabilityDistance(final double reachability_distance) {
        this.reachabilityDistance = reachability_distance;
    }

    public void setOptics(final OPTICS optics) {
        this.optics = optics;
    }

    public void reset() {
        this.reachabilityDistance = -1;

        this.core_distance = -1;
        this.visited = false;
    }

    @Override
    public String toString() {
        return "{" + this.data.getNodes().size() + "}";
    }

    public static class OPTICSDataGED implements DistanceFunction<OpticsData> {

        private final DistanceFunction<BehaviorModelGED> distanceFunction = new GraphEditDistance();

        @Override
        public double calculate(final OpticsData model1, final OpticsData model2) {
            return this.distanceFunction.calculate(model1.data, model2.data);
        }
    }
}
