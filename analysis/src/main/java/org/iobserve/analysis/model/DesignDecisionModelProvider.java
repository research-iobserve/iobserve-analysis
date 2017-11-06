package org.iobserve.analysis.model;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;

import de.uka.ipd.sdq.pcm.designdecision.DecisionSpace;
import de.uka.ipd.sdq.pcm.designdecision.designdecisionPackage;

/**
 * Model provider to provide a {@link DecisionSpace} model.
 *
 * @author Tobias Pöppke
 *
 */
public class DesignDecisionModelProvider extends AbstractModelProvider<DecisionSpace> {

	/**
	 * Create a new provider with the given model file
	 *
	 * @param theUriModelInstance
	 *            path to the model file
	 */
	public DesignDecisionModelProvider(URI theUriModelInstance) {
		super(theUriModelInstance);
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
