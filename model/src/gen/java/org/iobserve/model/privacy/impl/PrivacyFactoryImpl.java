/**
 */
package org.iobserve.model.privacy.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.iobserve.model.privacy.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class PrivacyFactoryImpl extends EFactoryImpl implements PrivacyFactory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static PrivacyFactory init() {
        try {
            PrivacyFactory thePrivacyFactory = (PrivacyFactory)EPackage.Registry.INSTANCE.getEFactory(PrivacyPackage.eNS_URI);
            if (thePrivacyFactory != null) {
                return thePrivacyFactory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new PrivacyFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PrivacyFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            case PrivacyPackage.DATA_PROTECTION_MODEL: return createDataProtectionModel();
            case PrivacyPackage.RETURN_TYPE_DATA_PROTECTION: return createReturnTypeDataProtection();
            case PrivacyPackage.GEO_LOCATION: return createGeoLocation();
            case PrivacyPackage.PARAMETER_DATA_PROTECTION: return createParameterDataProtection();
            case PrivacyPackage.ENCAPSULATED_DATA_SOURCE: return createEncapsulatedDataSource();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object createFromString(EDataType eDataType, String initialValue) {
        switch (eDataType.getClassifierID()) {
            case PrivacyPackage.EDATA_PROTECTION_LEVEL:
                return createEDataProtectionLevelFromString(eDataType, initialValue);
            case PrivacyPackage.EISO_CODE:
                return createEISOCodeFromString(eDataType, initialValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String convertToString(EDataType eDataType, Object instanceValue) {
        switch (eDataType.getClassifierID()) {
            case PrivacyPackage.EDATA_PROTECTION_LEVEL:
                return convertEDataProtectionLevelToString(eDataType, instanceValue);
            case PrivacyPackage.EISO_CODE:
                return convertEISOCodeToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DataProtectionModel createDataProtectionModel() {
        DataProtectionModelImpl dataProtectionModel = new DataProtectionModelImpl();
        return dataProtectionModel;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ReturnTypeDataProtection createReturnTypeDataProtection() {
        ReturnTypeDataProtectionImpl returnTypeDataProtection = new ReturnTypeDataProtectionImpl();
        return returnTypeDataProtection;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GeoLocation createGeoLocation() {
        GeoLocationImpl geoLocation = new GeoLocationImpl();
        return geoLocation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ParameterDataProtection createParameterDataProtection() {
        ParameterDataProtectionImpl parameterDataProtection = new ParameterDataProtectionImpl();
        return parameterDataProtection;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EncapsulatedDataSource createEncapsulatedDataSource() {
        EncapsulatedDataSourceImpl encapsulatedDataSource = new EncapsulatedDataSourceImpl();
        return encapsulatedDataSource;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataProtectionLevel createEDataProtectionLevelFromString(EDataType eDataType, String initialValue) {
        EDataProtectionLevel result = EDataProtectionLevel.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertEDataProtectionLevelToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EISOCode createEISOCodeFromString(EDataType eDataType, String initialValue) {
        EISOCode result = EISOCode.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertEISOCodeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PrivacyPackage getPrivacyPackage() {
        return (PrivacyPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static PrivacyPackage getPackage() {
        return PrivacyPackage.eINSTANCE;
    }

} //PrivacyFactoryImpl
