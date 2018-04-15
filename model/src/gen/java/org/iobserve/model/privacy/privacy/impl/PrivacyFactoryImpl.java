/**
 */
package org.iobserve.model.privacy.privacy.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.iobserve.model.privacy.privacy.*;

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
            case PrivacyPackage.PRIVACY_MODEL: return createPrivacyModel();
            case PrivacyPackage.GEO_LOCATION: return createGeoLocation();
            case PrivacyPackage.PARAMETER_PRIVACY: return createParameterPrivacy();
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
            case PrivacyPackage.EDATA_PRIVACY_LEVEL:
                return createEDataPrivacyLevelFromString(eDataType, initialValue);
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
            case PrivacyPackage.EDATA_PRIVACY_LEVEL:
                return convertEDataPrivacyLevelToString(eDataType, instanceValue);
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
    public PrivacyModel createPrivacyModel() {
        PrivacyModelImpl privacyModel = new PrivacyModelImpl();
        return privacyModel;
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
    public ParameterPrivacy createParameterPrivacy() {
        ParameterPrivacyImpl parameterPrivacy = new ParameterPrivacyImpl();
        return parameterPrivacy;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataPrivacyLevel createEDataPrivacyLevelFromString(EDataType eDataType, String initialValue) {
        EDataPrivacyLevel result = EDataPrivacyLevel.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertEDataPrivacyLevelToString(EDataType eDataType, Object instanceValue) {
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
