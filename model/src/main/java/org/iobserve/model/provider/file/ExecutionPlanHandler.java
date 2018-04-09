package org.iobserve.model.provider.file;

import org.eclipse.emf.ecore.EPackage;
import org.iobserve.adaptation.executionplan.ExecutionPlan;
import org.iobserve.adaptation.executionplan.ExecutionplanPackage;

public class ExecutionPlanHandler extends AbstractModelHandler<ExecutionPlan> {

    @Override
    protected EPackage getPackage() {
        // TODO Auto-generated method stub
        return ExecutionplanPackage.eINSTANCE;
    }

}
