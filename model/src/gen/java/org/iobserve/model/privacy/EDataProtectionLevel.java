/**
 */
package org.iobserve.model.privacy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>EData Protection Level</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.iobserve.model.privacy.PrivacyPackage#getEDataProtectionLevel()
 * @model
 * @generated
 */
public enum EDataProtectionLevel implements Enumerator {
    /**
     * The '<em><b>ANONYMOUS</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ANONYMOUS_VALUE
     * @generated
     * @ordered
     */
    ANONYMOUS(0, "ANONYMOUS", "anonymous"),

    /**
     * The '<em><b>DEPERSONALIZED</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #DEPERSONALIZED_VALUE
     * @generated
     * @ordered
     */
    DEPERSONALIZED(1, "DEPERSONALIZED", "depersonalized"),

    /**
     * The '<em><b>PERSONAL</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #PERSONAL_VALUE
     * @generated
     * @ordered
     */
    PERSONAL(2, "PERSONAL", "personal");

    /**
     * The '<em><b>ANONYMOUS</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>ANONYMOUS</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #ANONYMOUS
     * @model literal="anonymous"
     * @generated
     * @ordered
     */
    public static final int ANONYMOUS_VALUE = 0;

    /**
     * The '<em><b>DEPERSONALIZED</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>DEPERSONALIZED</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #DEPERSONALIZED
     * @model literal="depersonalized"
     * @generated
     * @ordered
     */
    public static final int DEPERSONALIZED_VALUE = 1;

    /**
     * The '<em><b>PERSONAL</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>PERSONAL</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #PERSONAL
     * @model literal="personal"
     * @generated
     * @ordered
     */
    public static final int PERSONAL_VALUE = 2;

    /**
     * An array of all the '<em><b>EData Protection Level</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final EDataProtectionLevel[] VALUES_ARRAY =
        new EDataProtectionLevel[] {
            ANONYMOUS,
            DEPERSONALIZED,
            PERSONAL,
        };

    /**
     * A public read-only list of all the '<em><b>EData Protection Level</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<EDataProtectionLevel> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>EData Protection Level</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static EDataProtectionLevel get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            EDataProtectionLevel result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>EData Protection Level</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static EDataProtectionLevel getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            EDataProtectionLevel result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>EData Protection Level</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static EDataProtectionLevel get(int value) {
        switch (value) {
            case ANONYMOUS_VALUE: return ANONYMOUS;
            case DEPERSONALIZED_VALUE: return DEPERSONALIZED;
            case PERSONAL_VALUE: return PERSONAL;
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
    private EDataProtectionLevel(int value, String name, String literal) {
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
    
} //EDataProtectionLevel
