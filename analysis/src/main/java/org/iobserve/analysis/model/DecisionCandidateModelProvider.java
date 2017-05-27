package org.iobserve.analysis.model;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;

import de.uka.ipd.sdq.pcm.designdecision.Candidates;
import de.uka.ipd.sdq.pcm.designdecision.designdecisionPackage;

public class DecisionCandidateModelProvider extends AbstractModelProvider<Candidates> {

	public DecisionCandidateModelProvider(URI theUriModelInstance) {
		super(theUriModelInstance);
	}

	@Override
	protected EPackage getPackage() {
		return designdecisionPackage.eINSTANCE;
	}

	@Override
	public void resetModel() {
		this.getModel().getCandidate().clear();
	}

}
