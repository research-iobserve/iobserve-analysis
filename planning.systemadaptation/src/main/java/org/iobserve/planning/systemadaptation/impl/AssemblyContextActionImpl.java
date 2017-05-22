/**
 */
package org.iobserve.planning.systemadaptation.impl;

import org.eclipse.emf.ecore.EClass;

import org.iobserve.planning.systemadaptation.AssemblyContextAction;
import org.iobserve.planning.systemadaptation.systemadaptationPackage;

import org.palladiosimulator.pcm.core.composition.AssemblyContext;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Assembly Context Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.systemadaptation.impl.AssemblyContextActionImpl#getSourceAssemblyContext <em>Source Assembly Context</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AssemblyContextActionImpl extends ActionImpl implements AssemblyContextAction {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AssemblyContextActionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return systemadaptationPackage.Literals.ASSEMBLY_CONTEXT_ACTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AssemblyContext getSourceAssemblyContext() {
		return (AssemblyContext)eGet(systemadaptationPackage.Literals.ASSEMBLY_CONTEXT_ACTION__SOURCE_ASSEMBLY_CONTEXT, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSourceAssemblyContext(AssemblyContext newSourceAssemblyContext) {
		eSet(systemadaptationPackage.Literals.ASSEMBLY_CONTEXT_ACTION__SOURCE_ASSEMBLY_CONTEXT, newSourceAssemblyContext);
	}

} //AssemblyContextActionImpl
