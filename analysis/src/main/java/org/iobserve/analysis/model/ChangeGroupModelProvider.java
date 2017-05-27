package org.iobserve.analysis.model;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.iobserve.planning.changegroup.ChangeGroup;
import org.iobserve.planning.changegroup.ChangegroupPackage;

public class ChangeGroupModelProvider extends AbstractModelProvider<ChangeGroup> {

	public ChangeGroupModelProvider(URI theUriModelInstance) {
		super(theUriModelInstance);
	}

	@Override
	protected EPackage getPackage() {
		return ChangegroupPackage.eINSTANCE;
	}

	@Override
	public void resetModel() {
		this.getModel().getActions().clear();
	}

}
