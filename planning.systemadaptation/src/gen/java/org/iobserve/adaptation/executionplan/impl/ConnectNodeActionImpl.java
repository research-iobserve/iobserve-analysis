/**
 */
package org.iobserve.adaptation.executionplan.impl;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.iobserve.adaptation.executionplan.ConnectNodeAction;
import org.iobserve.adaptation.executionplan.ExecutionplanPackage;

import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Connect Node Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.adaptation.executionplan.impl.ConnectNodeActionImpl#getTargetConnectors <em>Target Connectors</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ConnectNodeActionImpl extends ResourceContainerActionImpl implements ConnectNodeAction {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ConnectNodeActionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ExecutionplanPackage.Literals.CONNECT_NODE_ACTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    public EList<LinkingResource> getTargetConnectors() {
        return (EList<LinkingResource>)eGet(ExecutionplanPackage.Literals.CONNECT_NODE_ACTION__TARGET_CONNECTORS, true);
    }

} //ConnectNodeActionImpl
