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
package org.iobserve.service.behavior.analysis;

import java.util.Iterator;

import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;

import mtree.DistanceFunction;
import mtree.MTree;

/**
 *
 * @author Lars Jürgensen
 *
 */
public class OpticsData {

    public static final int UNDEFINED = -2;
    public static final int NOT_INITIALIZED = -1;

    private OPTICS optics;

    private double reachabilityDistance = OpticsData.UNDEFINED;

    private double coreDistance = OpticsData.NOT_INITIALIZED;
    private boolean visited = false;

    private final OPTICSDataGED ged = new OPTICSDataGED();
    private final BehaviorModelGED data;

    public OpticsData(final BehaviorModelGED data) {
        this.data = data;
    }

    public double getCoreDistance() {
        if (this.coreDistance == OpticsData.NOT_INITIALIZED) {
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
                this.coreDistance = OpticsData.UNDEFINED;
            } else {

                this.coreDistance = this.ged.calculate(this, last);

            }

        }
        return this.coreDistance;

    }

    public double distanceTo(final OpticsData model) {

        return this.ged.calculate(this, model);
    }

    public void setCoreDistance(final double core_distance) {
        this.coreDistance = core_distance;
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

    public void setReachabilityDistance(final double reachabilityDistance) {
        this.reachabilityDistance = reachabilityDistance;
    }

    public void setOptics(final OPTICS optics) {
        this.optics = optics;
    }

    public void reset() {
        this.reachabilityDistance = -1;

        this.coreDistance = -1;
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
