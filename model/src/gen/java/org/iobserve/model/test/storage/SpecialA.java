/**
 */
package org.iobserve.model.test.storage;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Special A</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.test.storage.SpecialA#getRelate <em>Relate</em>}</li>
 * </ul>
 *
 * @see org.iobserve.model.test.storage.StoragePackage#getSpecialA()
 * @model
 * @generated
 */
public interface SpecialA extends OtherInterface {

    /**
     * Returns the value of the '<em><b>Relate</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Relate</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Relate</em>' reference.
     * @see #setRelate(Other)
     * @see org.iobserve.model.test.storage.StoragePackage#getSpecialA_Relate()
     * @model
     * @generated
     */
    Other getRelate();

    /**
     * Sets the value of the '{@link org.iobserve.model.test.storage.SpecialA#getRelate <em>Relate</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Relate</em>' reference.
     * @see #getRelate()
     * @generated
     */
    void setRelate(Other value);
} // SpecialA
