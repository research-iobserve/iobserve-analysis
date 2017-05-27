package org.iobserve.analysis.model;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;

import de.uka.ipd.sdq.pcm.designdecision.DecisionSpace;
import de.uka.ipd.sdq.pcm.designdecision.designdecisionPackage;

public class DesignDecisionModelProvider extends AbstractModelProvider<DecisionSpace> {

	public DesignDecisionModelProvider(URI theUriModelInstance) {
		super(theUriModelInstance);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected EPackage getPackage() {
		return designdecisionPackage.eINSTANCE;
	}

	@Override
	public void resetModel() {
		this.getModel().getDegreesOfFreedom().clear();
	}

}
