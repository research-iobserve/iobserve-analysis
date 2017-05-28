package org.iobserve.analysis.model;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;

import de.uka.ipd.sdq.pcm.cost.CostRepository;
import de.uka.ipd.sdq.pcm.cost.costPackage;

/**
 * Model provider to provide a {@link CostRepository} model.
 *
 * @author Tobias Pöppke
 *
 */
public class CostModelProvider extends AbstractModelProvider<CostRepository> {

	/**
	 * Create a new provider with the given model file
	 *
	 * @param theUriModelInstance
	 *            path to the model file
	 */
	public CostModelProvider(URI theUriModelInstance) {
		super(theUriModelInstance);
	}

	@Override
	protected EPackage getPackage() {
		return costPackage.eINSTANCE;
	}

	@Override
	public void resetModel() {
		this.getModel().getCost().clear();
	}

}
