package org.iobserve.analysis.model;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;

import de.uka.ipd.sdq.pcm.cost.CostRepository;
import de.uka.ipd.sdq.pcm.cost.costPackage;

public class CostModelProvider extends AbstractModelProvider<CostRepository> {

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
