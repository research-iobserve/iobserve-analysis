/**
 */
package org.iobserve.model.privacy;

import org.palladiosimulator.pcm.repository.Parameter;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Parameter Data Protection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.privacy.ParameterDataProtection#getParameter <em>Parameter</em>}</li>
 * </ul>
 *
 * @see org.iobserve.model.privacy.PrivacyPackage#getParameterDataProtection()
 * @model
 * @generated
 */
public interface ParameterDataProtection extends IDataProtectionAnnotation {
    /**
     * Returns the value of the '<em><b>Parameter</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Parameter</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Parameter</em>' reference.
     * @see #setParameter(Parameter)
     * @see org.iobserve.model.privacy.PrivacyPackage#getParameterDataProtection_Parameter()
     * @model required="true"
     * @generated
     */
    Parameter getParameter();

    /**
     * Sets the value of the '{@link org.iobserve.model.privacy.ParameterDataProtection#getParameter <em>Parameter</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Parameter</em>' reference.
     * @see #getParameter()
     * @generated
     */
    void setParameter(Parameter value);

} // ParameterDataProtection
