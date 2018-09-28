/**
 */
package org.iobserve.model.correspondence;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>EService Technology</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.iobserve.model.correspondence.CorrespondencePackage#getEServiceTechnology()
 * @model
 * @generated
 */
public enum EServiceTechnology implements Enumerator {
    /**
     * The '<em><b>SERVLET</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SERVLET_VALUE
     * @generated
     * @ordered
     */
    SERVLET(0, "SERVLET", "SERVLET"),

    /**
     * The '<em><b>EJB</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #EJB_VALUE
     * @generated
     * @ordered
     */
    EJB(1, "EJB", "EJB"),

    /**
     * The '<em><b>ASPECT J</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ASPECT_J_VALUE
     * @generated
     * @ordered
     */
    ASPECT_J(2, "ASPECT_J", "ASPECT_J");

    /**
     * The '<em><b>SERVLET</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>SERVLET</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #SERVLET
     * @model
     * @generated
     * @ordered
     */
    public static final int SERVLET_VALUE = 0;

    /**
     * The '<em><b>EJB</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>EJB</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #EJB
     * @model
     * @generated
     * @ordered
     */
    public static final int EJB_VALUE = 1;

    /**
     * The '<em><b>ASPECT J</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>ASPECT J</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #ASPECT_J
     * @model
     * @generated
     * @ordered
     */
    public static final int ASPECT_J_VALUE = 2;

    /**
     * An array of all the '<em><b>EService Technology</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final EServiceTechnology[] VALUES_ARRAY =
        new EServiceTechnology[] {
            SERVLET,
            EJB,
            ASPECT_J,
        };

    /**
     * A public read-only list of all the '<em><b>EService Technology</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<EServiceTechnology> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>EService Technology</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static EServiceTechnology get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            EServiceTechnology result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>EService Technology</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static EServiceTechnology getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            EServiceTechnology result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>EService Technology</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static EServiceTechnology get(int value) {
        switch (value) {
            case SERVLET_VALUE: return SERVLET;
            case EJB_VALUE: return EJB;
            case ASPECT_J_VALUE: return ASPECT_J;
        }
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final int value;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final String name;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final String literal;

    /**
     * Only this class can construct instances.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EServiceTechnology(int value, String name, String literal) {
        this.value = value;
        this.name = name;
        this.literal = literal;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public int getValue() {
      return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getName() {
      return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getLiteral() {
      return literal;
    }

    /**
     * Returns the literal value of the enumerator, which is its string representation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        return literal;
    }
    
} //EServiceTechnology
