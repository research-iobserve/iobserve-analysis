/**
 */
package org.iobserve.model.test.storage;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.test.storage.Root#getName <em>Name</em>}</li>
 *   <li>{@link org.iobserve.model.test.storage.Root#getLabels <em>Labels</em>}</li>
 *   <li>{@link org.iobserve.model.test.storage.Root#getFixed <em>Fixed</em>}</li>
 *   <li>{@link org.iobserve.model.test.storage.Root#getEnumerate <em>Enumerate</em>}</li>
 *   <li>{@link org.iobserve.model.test.storage.Root#getOthers <em>Others</em>}</li>
 *   <li>{@link org.iobserve.model.test.storage.Root#getIfaceOthers <em>Iface Others</em>}</li>
 * </ul>
 *
 * @see org.iobserve.model.test.storage.StoragePackage#getRoot()
 * @model
 * @generated
 */
public interface Root extends EObject {
    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(String)
     * @see org.iobserve.model.test.storage.StoragePackage#getRoot_Name()
     * @model
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link org.iobserve.model.test.storage.Root#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

    /**
     * Returns the value of the '<em><b>Labels</b></em>' attribute list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Labels</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Labels</em>' attribute list.
     * @see org.iobserve.model.test.storage.StoragePackage#getRoot_Labels()
     * @model
     * @generated
     */
    EList<String> getLabels();

    /**
     * Returns the value of the '<em><b>Fixed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Fixed</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Fixed</em>' attribute.
     * @see #setFixed(String)
     * @see org.iobserve.model.test.storage.StoragePackage#getRoot_Fixed()
     * @model required="true"
     * @generated
     */
    String getFixed();

    /**
     * Sets the value of the '{@link org.iobserve.model.test.storage.Root#getFixed <em>Fixed</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Fixed</em>' attribute.
     * @see #getFixed()
     * @generated
     */
    void setFixed(String value);

    /**
     * Returns the value of the '<em><b>Enumerate</b></em>' attribute.
     * The literals are from the enumeration {@link org.iobserve.model.test.storage.EnumValueExample}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Enumerate</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Enumerate</em>' attribute.
     * @see org.iobserve.model.test.storage.EnumValueExample
     * @see #setEnumerate(EnumValueExample)
     * @see org.iobserve.model.test.storage.StoragePackage#getRoot_Enumerate()
     * @model
     * @generated
     */
    EnumValueExample getEnumerate();

    /**
     * Sets the value of the '{@link org.iobserve.model.test.storage.Root#getEnumerate <em>Enumerate</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Enumerate</em>' attribute.
     * @see org.iobserve.model.test.storage.EnumValueExample
     * @see #getEnumerate()
     * @generated
     */
    void setEnumerate(EnumValueExample value);

    /**
     * Returns the value of the '<em><b>Others</b></em>' containment reference list.
     * The list contents are of type {@link org.iobserve.model.test.storage.Other}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Others</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Others</em>' containment reference list.
     * @see org.iobserve.model.test.storage.StoragePackage#getRoot_Others()
     * @model containment="true"
     * @generated
     */
    EList<Other> getOthers();

    /**
     * Returns the value of the '<em><b>Iface Others</b></em>' containment reference list.
     * The list contents are of type {@link org.iobserve.model.test.storage.OtherInterface}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Iface Others</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Iface Others</em>' containment reference list.
     * @see org.iobserve.model.test.storage.StoragePackage#getRoot_IfaceOthers()
     * @model containment="true"
     * @generated
     */
    EList<OtherInterface> getIfaceOthers();

} // Root
