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
package org.iobserve.legacyadaptation;

import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.stages.general.AbstractLinearComposition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class encapsulates the system adaption filter in the teetime framework. It contains the
 * sub-stages: compare/action generation, execution planning and actual execution.
 *
 * @author Philipp Weimann
 */
public class SystemAdaptation extends AbstractLinearComposition<AdaptationData, AdaptationData> {

    protected static final Logger LOG = LoggerFactory.getLogger(SystemAdaptation.class);

    /**
     * This class encapsulates the major system adaption filter stage.
     *
     * @param comparer
     *            computes action which need to be done for migration/adaption
     * @param planner
     *            orders the actions into a executable sequence
     */
    public SystemAdaptation(final AdaptationCalculation comparer, final AdaptationPlanning planner) {
        super(comparer.getInputPort(), planner.getOutputPort());

        this.connectPorts(comparer.getOutputPort(), planner.getInputPort());
    }

}
