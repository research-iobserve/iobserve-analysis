package org.iobserve.analysis.model;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsagemodelPackage;

public final class UsageModelProvider extends AbstractModelProvider<UsageModel> {

	// ********************************************************************
	// * INITIALIZATION
	// ********************************************************************

	public UsageModelProvider(final URI uriUsageModel, final ModelProviderPlatform thePlatform) {
		super(uriUsageModel, thePlatform);
	}
	
	@Override
	public EPackage getPackage() {
		return UsagemodelPackage.eINSTANCE;
	}
	
	@Override
	protected void loadModel() {
		super.loadModel();
	}
	

}
