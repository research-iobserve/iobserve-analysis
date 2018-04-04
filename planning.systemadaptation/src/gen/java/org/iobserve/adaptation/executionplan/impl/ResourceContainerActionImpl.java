/**
 */
package org.iobserve.adaptation.executionplan.impl;

import org.eclipse.emf.ecore.EClass;
import org.iobserve.adaptation.executionplan.ExecutionplanPackage;
import org.iobserve.adaptation.executionplan.ResourceContainerAction;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Resource Container
 * Action</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.adaptation.executionplan.impl.ResourceContainerActionImpl#getTargetResourceContainer <em>Target Resource Container</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ResourceContainerActionImpl extends ActionImpl implements ResourceContainerAction {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected ResourceContainerActionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ExecutionplanPackage.Literals.RESOURCE_CONTAINER_ACTION;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ResourceContainer getTargetResourceContainer() {
        return (ResourceContainer)eGet(ExecutionplanPackage.Literals.RESOURCE_CONTAINER_ACTION__TARGET_RESOURCE_CONTAINER, true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setTargetResourceContainer(ResourceContainer newTargetResourceContainer) {
        eSet(ExecutionplanPackage.Literals.RESOURCE_CONTAINER_ACTION__TARGET_RESOURCE_CONTAINER, newTargetResourceContainer);
    }

} // ResourceContainerActionImpl
