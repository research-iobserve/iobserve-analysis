/**
 */
package org.iobserve.model.privacy;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IData Protection Annotation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.privacy.IDataProtectionAnnotation#getLevel <em>Level</em>}</li>
 * </ul>
 *
 * @see org.iobserve.model.privacy.PrivacyPackage#getIDataProtectionAnnotation()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface IDataProtectionAnnotation extends EObject {
    /**
     * Returns the value of the '<em><b>Level</b></em>' attribute.
     * The literals are from the enumeration {@link org.iobserve.model.privacy.EDataProtectionLevel}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Level</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Level</em>' attribute.
     * @see org.iobserve.model.privacy.EDataProtectionLevel
     * @see #setLevel(EDataProtectionLevel)
     * @see org.iobserve.model.privacy.PrivacyPackage#getIDataProtectionAnnotation_Level()
     * @model unique="false" required="true"
     * @generated
     */
    EDataProtectionLevel getLevel();

    /**
     * Sets the value of the '{@link org.iobserve.model.privacy.IDataProtectionAnnotation#getLevel <em>Level</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Level</em>' attribute.
     * @see org.iobserve.model.privacy.EDataProtectionLevel
     * @see #getLevel()
     * @generated
     */
    void setLevel(EDataProtectionLevel value);

} // IDataProtectionAnnotation
