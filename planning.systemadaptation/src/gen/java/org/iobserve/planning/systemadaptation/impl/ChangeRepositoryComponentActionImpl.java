/**
 */
package org.iobserve.planning.systemadaptation.impl;

import org.eclipse.emf.ecore.EClass;

import org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction;
import org.iobserve.planning.systemadaptation.SystemadaptationPackage;

import org.palladiosimulator.pcm.allocation.AllocationContext;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Change Repository Component Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.systemadaptation.impl.ChangeRepositoryComponentActionImpl#getSourceAllocationContext <em>Source Allocation Context</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ChangeRepositoryComponentActionImpl extends AssemblyContextActionImpl implements ChangeRepositoryComponentAction {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ChangeRepositoryComponentActionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SystemadaptationPackage.Literals.CHANGE_REPOSITORY_COMPONENT_ACTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AllocationContext getSourceAllocationContext() {
        return (AllocationContext)eGet(SystemadaptationPackage.Literals.CHANGE_REPOSITORY_COMPONENT_ACTION__SOURCE_ALLOCATION_CONTEXT, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSourceAllocationContext(AllocationContext newSourceAllocationContext) {
        eSet(SystemadaptationPackage.Literals.CHANGE_REPOSITORY_COMPONENT_ACTION__SOURCE_ALLOCATION_CONTEXT, newSourceAllocationContext);
    }

} //ChangeRepositoryComponentActionImpl
