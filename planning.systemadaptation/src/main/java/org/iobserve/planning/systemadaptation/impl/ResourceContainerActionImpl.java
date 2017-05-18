/**
 */
package org.iobserve.planning.systemadaptation.impl;

import org.eclipse.emf.ecore.EClass;

import org.iobserve.planning.systemadaptation.ResourceContainerAction;
import org.iobserve.planning.systemadaptation.systemadaptationPackage;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Resource Container Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.systemadaptation.impl.ResourceContainerActionImpl#getSourceResourceContainer <em>Source Resource Container</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ResourceContainerActionImpl extends ActionImpl implements ResourceContainerAction {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ResourceContainerActionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return systemadaptationPackage.Literals.RESOURCE_CONTAINER_ACTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResourceContainer getSourceResourceContainer() {
		return (ResourceContainer)eGet(systemadaptationPackage.Literals.RESOURCE_CONTAINER_ACTION__SOURCE_RESOURCE_CONTAINER, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSourceResourceContainer(ResourceContainer newSourceResourceContainer) {
		eSet(systemadaptationPackage.Literals.RESOURCE_CONTAINER_ACTION__SOURCE_RESOURCE_CONTAINER, newSourceResourceContainer);
	}

} //ResourceContainerActionImpl
