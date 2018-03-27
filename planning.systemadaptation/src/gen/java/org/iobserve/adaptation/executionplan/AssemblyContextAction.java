/**
 */
package org.iobserve.adaptation.executionplan;

import org.palladiosimulator.pcm.core.composition.AssemblyContext;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Assembly Context Action</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.adaptation.executionplan.AssemblyContextAction#getSourceAssemblyContext <em>Source Assembly Context</em>}</li>
 * </ul>
 *
 * @see org.iobserve.adaptation.executionplan.ExecutionplanPackage#getAssemblyContextAction()
 * @model
 * @generated
 */
public interface AssemblyContextAction extends Action {
    /**
     * Returns the value of the '<em><b>Source Assembly Context</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Source Assembly Context</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Source Assembly Context</em>' reference.
     * @see #setSourceAssemblyContext(AssemblyContext)
     * @see org.iobserve.adaptation.executionplan.ExecutionplanPackage#getAssemblyContextAction_SourceAssemblyContext()
     * @model
     * @generated
     */
    AssemblyContext getSourceAssemblyContext();

    /**
     * Sets the value of the '{@link org.iobserve.adaptation.executionplan.AssemblyContextAction#getSourceAssemblyContext <em>Source Assembly Context</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Source Assembly Context</em>' reference.
     * @see #getSourceAssemblyContext()
     * @generated
     */
    void setSourceAssemblyContext(AssemblyContext value);

} // AssemblyContextAction
