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
