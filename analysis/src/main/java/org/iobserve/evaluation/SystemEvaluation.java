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
import org.iobserve.analysis.snapshot.SnapshotBuilder;
import org.iobserve.analysis.utils.AbstractLinearComposition;

/**
 * TODO add description.
 *
 * @author unknown
 *
 */
public class SystemEvaluation extends AbstractLinearComposition<URI, Boolean> {

    private static ModelComparer modelComparer;

    public SystemEvaluation(final ModelComparer comparer) {
        super(comparer.getInputPort(), comparer.getOutputPort());

        SystemEvaluation.modelComparer = comparer;
    }

    public static void enableEvaluation(final AdaptationData adaptationData) {
        SystemEvaluation.modelComparer.setBaseData(adaptationData);
        SnapshotBuilder.setEvaluationMode(true);
    }

    public static void disableEvaluation() {
        SystemEvaluation.modelComparer.setBaseData(null);
        SnapshotBuilder.setEvaluationMode(false);
    }

}
