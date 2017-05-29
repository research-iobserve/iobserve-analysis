package org.iobserve.analysis.model;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.iobserve.planning.cloudprofile.CloudProfile;
import org.iobserve.planning.cloudprofile.cloudprofilePackage;

/**
 * Model provider to provide a {@link CloudProfile} model.
 *
 * @author Tobias Pöppke
 *
 */
public class CloudProfileModelProvider extends AbstractModelProvider<CloudProfile> {

	/**
	 * Create a new provider with the given model file
	 *
	 * @param theUriModelInstance
	 *            path to the model file
	 */
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
