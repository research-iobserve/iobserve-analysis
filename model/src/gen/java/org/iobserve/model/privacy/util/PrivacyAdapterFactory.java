/**
 */
package org.iobserve.model.privacy.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.iobserve.model.privacy.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.iobserve.model.privacy.PrivacyPackage
 * @generated
 */
public class PrivacyAdapterFactory extends AdapterFactoryImpl {
    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static PrivacyPackage modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PrivacyAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = PrivacyPackage.eINSTANCE;
        }
    }

    /**
     * Returns whether this factory is applicable for the type of the object.
     * <!-- begin-user-doc -->
     * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
     * <!-- end-user-doc -->
     * @return whether this factory is applicable for the type of the object.
     * @generated
     */
    @Override
    public boolean isFactoryForType(Object object) {
        if (object == modelPackage) {
            return true;
        }
        if (object instanceof EObject) {
            return ((EObject)object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
     * The switch that delegates to the <code>createXXX</code> methods.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected PrivacySwitch<Adapter> modelSwitch =
        new PrivacySwitch<Adapter>() {
            @Override
            public Adapter caseDataProtectionModel(DataProtectionModel object) {
                return createDataProtectionModelAdapter();
            }
            @Override
            public Adapter caseReturnTypeDataProtection(ReturnTypeDataProtection object) {
                return createReturnTypeDataProtectionAdapter();
            }
            @Override
            public Adapter caseIDataProtectionAnnotation(IDataProtectionAnnotation object) {
                return createIDataProtectionAnnotationAdapter();
            }
            @Override
            public Adapter caseGeoLocation(GeoLocation object) {
                return createGeoLocationAdapter();
            }
            @Override
            public Adapter caseParameterDataProtection(ParameterDataProtection object) {
                return createParameterDataProtectionAdapter();
            }
            @Override
            public Adapter caseEncapsulatedDataSource(EncapsulatedDataSource object) {
                return createEncapsulatedDataSourceAdapter();
            }
            @Override
            public Adapter defaultCase(EObject object) {
                return createEObjectAdapter();
            }
        };

    /**
     * Creates an adapter for the <code>target</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param target the object to adapt.
     * @return the adapter for the <code>target</code>.
     * @generated
     */
    @Override
    public Adapter createAdapter(Notifier target) {
        return modelSwitch.doSwitch((EObject)target);
    }


    /**
     * Creates a new adapter for an object of class '{@link org.iobserve.model.privacy.DataProtectionModel <em>Data Protection Model</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.iobserve.model.privacy.DataProtectionModel
     * @generated
     */
    public Adapter createDataProtectionModelAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.iobserve.model.privacy.ReturnTypeDataProtection <em>Return Type Data Protection</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.iobserve.model.privacy.ReturnTypeDataProtection
     * @generated
     */
    public Adapter createReturnTypeDataProtectionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.iobserve.model.privacy.IDataProtectionAnnotation <em>IData Protection Annotation</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.iobserve.model.privacy.IDataProtectionAnnotation
     * @generated
     */
    public Adapter createIDataProtectionAnnotationAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.iobserve.model.privacy.GeoLocation <em>Geo Location</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.iobserve.model.privacy.GeoLocation
     * @generated
     */
    public Adapter createGeoLocationAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.iobserve.model.privacy.ParameterDataProtection <em>Parameter Data Protection</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.iobserve.model.privacy.ParameterDataProtection
     * @generated
     */
    public Adapter createParameterDataProtectionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.iobserve.model.privacy.EncapsulatedDataSource <em>Encapsulated Data Source</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.iobserve.model.privacy.EncapsulatedDataSource
     * @generated
     */
    public Adapter createEncapsulatedDataSourceAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for the default case.
     * <!-- begin-user-doc -->
     * This default implementation returns null.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @generated
     */
    public Adapter createEObjectAdapter() {
        return null;
    }

} //PrivacyAdapterFactory
