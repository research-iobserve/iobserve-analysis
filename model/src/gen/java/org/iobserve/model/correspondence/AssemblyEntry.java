/**
 */
package org.iobserve.model.correspondence;

import org.palladiosimulator.pcm.core.composition.AssemblyContext;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Assembly Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.correspondence.AssemblyEntry#getAssembly <em>Assembly</em>}</li>
 * </ul>
 *
 * @see org.iobserve.model.correspondence.CorrespondencePackage#getAssemblyEntry()
 * @model
 * @generated
 */
public interface AssemblyEntry extends AbstractEntry {
    /**
     * Returns the value of the '<em><b>Assembly</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Assembly</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Assembly</em>' reference.
     * @see #setAssembly(AssemblyContext)
     * @see org.iobserve.model.correspondence.CorrespondencePackage#getAssemblyEntry_Assembly()
     * @model required="true"
     * @generated
     */
    AssemblyContext getAssembly();

    /**
     * Sets the value of the '{@link org.iobserve.model.correspondence.AssemblyEntry#getAssembly <em>Assembly</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Assembly</em>' reference.
     * @see #getAssembly()
     * @generated
     */
    void setAssembly(AssemblyContext value);

} // AssemblyEntry
