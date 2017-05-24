package org.iobserve.analysis.model;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.iobserve.planning.cloudprofile.CloudProfile;
import org.iobserve.planning.cloudprofile.cloudprofilePackage;

public class CloudProfileModelProvider extends AbstractModelProvider<CloudProfile> {

	public CloudProfileModelProvider(URI theUriModelInstance) {
		super(theUriModelInstance);
	}

	@Override
	protected EPackage getPackage() {
		return cloudprofilePackage.eINSTANCE;
	}

	@Override
	public void resetModel() {
		this.getModel().getCloudProviders().clear();
	}

}
