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
package org.iobserve.evaluation;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.model.snapshot.SnapshotBuilder;
import org.iobserve.stages.general.AbstractLinearComposition;

/**
 * TODO add description for stage.
 *
 * @author unknown
 *
 */
public class SystemEvaluation extends AbstractLinearComposition<URI, Boolean> {

    private static ModelComparer modelComparer;

    /**
     * Create a system evaluation stage.
     *
     * @param comparer
     *            model comparer
     */
    public SystemEvaluation(final ModelComparer comparer) {
        super(comparer.getInputPort(), comparer.getOutputPort());

        SystemEvaluation.modelComparer = comparer;
    }

    /**
     * enable evaluation.
     *
     * @param adaptationData
     *            set base data
     */
    public static void enableEvaluation(final AdaptationData adaptationData) {
        SystemEvaluation.modelComparer.setBaseAdaptationData(adaptationData);
        SnapshotBuilder.setEvaluationMode(true);
    }

    /**
     * disable evaluation.
     */
    public static void disableEvaluation() {
        SystemEvaluation.modelComparer.setBaseAdaptationData(null);
        SnapshotBuilder.setEvaluationMode(false);
    }

}
