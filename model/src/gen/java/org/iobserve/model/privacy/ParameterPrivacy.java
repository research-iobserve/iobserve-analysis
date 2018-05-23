/**
 */
package org.iobserve.model.privacy;

import org.palladiosimulator.pcm.repository.Parameter;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Parameter Privacy</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.privacy.ParameterPrivacy#getParameter <em>Parameter</em>}</li>
 * </ul>
 *
 * @see org.iobserve.model.privacy.PrivacyPackage#getParameterPrivacy()
 * @model
 * @generated
 */
public interface ParameterPrivacy extends IPrivacyAnnotation {
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
     * @see org.iobserve.model.privacy.PrivacyPackage#getParameterPrivacy_Parameter()
     * @model required="true"
     * @generated
     */
    Parameter getParameter();

    /**
     * Sets the value of the '{@link org.iobserve.model.privacy.ParameterPrivacy#getParameter <em>Parameter</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Parameter</em>' reference.
     * @see #getParameter()
     * @generated
     */
    void setParameter(Parameter value);

} // ParameterPrivacy
