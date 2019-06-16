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
package org.iobserve.service.behavior.analysis.clustering;

import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;

import mtree.DistanceFunction;

/**
 *
 * @author Lars Jürgensen
 *
 */
public class OpticsData {

    public static final int UNDEFINED = -1;

    private static final OPTICSDataGED GED = new OPTICSDataGED();

    private double reachabilityDistance = OpticsData.UNDEFINED;

    private double coreDistance = OpticsData.UNDEFINED;
    private boolean visited = false;

    private final BehaviorModelGED data;

    public OpticsData(final BehaviorModelGED data) {
        this.data = data;
    }

    public double getCoreDistance() {
        return this.coreDistance;

    }

    public double distanceTo(final OpticsData model) {

        return OpticsData.GED.calculate(this, model);
    }

    public void setCoreDistance(final double coreDistance) {
        this.coreDistance = coreDistance;
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

    public void reset() {
        this.reachabilityDistance = -1;

        this.coreDistance = -1;
        this.visited = false;
    }

    public static OPTICSDataGED getDistanceFunction() {
        return OpticsData.GED;
    }

    private static class OPTICSDataGED implements DistanceFunction<OpticsData> {

        private final DistanceFunction<BehaviorModelGED> distanceFunction = new GraphEditDistance();

        @Override
        public double calculate(final OpticsData model1, final OpticsData model2) {
            return this.distanceFunction.calculate(model1.data, model2.data);
        }
    }
}
