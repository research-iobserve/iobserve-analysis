/**
 */
package org.iobserve.adaptation.executionplan.impl;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.iobserve.adaptation.executionplan.DisconnectNodeAction;
import org.iobserve.adaptation.executionplan.ExecutionplanPackage;

import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Disconnect Node Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.adaptation.executionplan.impl.DisconnectNodeActionImpl#getTargetConnectors <em>Target Connectors</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DisconnectNodeActionImpl extends ResourceContainerActionImpl implements DisconnectNodeAction {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DisconnectNodeActionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ExecutionplanPackage.Literals.DISCONNECT_NODE_ACTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    public EList<LinkingResource> getTargetConnectors() {
        return (EList<LinkingResource>)eGet(ExecutionplanPackage.Literals.DISCONNECT_NODE_ACTION__TARGET_CONNECTORS, true);
    }

} //DisconnectNodeActionImpl
