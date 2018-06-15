/**
 */
package org.iobserve.model.test.storage;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Other Sub Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.test.storage.OtherSubType#isLabel <em>Label</em>}</li>
 *   <li>{@link org.iobserve.model.test.storage.OtherSubType#getOther <em>Other</em>}</li>
 * </ul>
 *
 * @see org.iobserve.model.test.storage.StoragePackage#getOtherSubType()
 * @model
 * @generated
 */
public interface OtherSubType extends Other {
    /**
     * Returns the value of the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Label</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Label</em>' attribute.
     * @see #setLabel(boolean)
     * @see org.iobserve.model.test.storage.StoragePackage#getOtherSubType_Label()
     * @model
     * @generated
     */
    boolean isLabel();

    /**
     * Sets the value of the '{@link org.iobserve.model.test.storage.OtherSubType#isLabel <em>Label</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Label</em>' attribute.
     * @see #isLabel()
     * @generated
     */
    void setLabel(boolean value);

    /**
     * Returns the value of the '<em><b>Other</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Other</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Other</em>' containment reference.
     * @see #setOther(Other)
     * @see org.iobserve.model.test.storage.StoragePackage#getOtherSubType_Other()
     * @model containment="true"
     * @generated
     */
    Other getOther();

    /**
     * Sets the value of the '{@link org.iobserve.model.test.storage.OtherSubType#getOther <em>Other</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Other</em>' containment reference.
     * @see #getOther()
     * @generated
     */
    void setOther(Other value);

} // OtherSubType
