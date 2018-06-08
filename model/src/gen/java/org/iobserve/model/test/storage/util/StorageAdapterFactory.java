/**
 */
package org.iobserve.model.test.storage.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.iobserve.model.test.storage.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.iobserve.model.test.storage.StoragePackage
 * @generated
 */
public class StorageAdapterFactory extends AdapterFactoryImpl {
    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static StoragePackage modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public StorageAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = StoragePackage.eINSTANCE;
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
    protected StorageSwitch<Adapter> modelSwitch =
        new StorageSwitch<Adapter>() {
            @Override
            public Adapter caseRoot(Root object) {
                return createRootAdapter();
            }
            @Override
            public Adapter caseOther(Other object) {
                return createOtherAdapter();
            }
            @Override
            public Adapter caseOtherSubType(OtherSubType object) {
                return createOtherSubTypeAdapter();
            }
            @Override
            public Adapter caseOtherInterface(OtherInterface object) {
                return createOtherInterfaceAdapter();
            }
            @Override
            public Adapter caseSpecialA(SpecialA object) {
                return createSpecialAAdapter();
            }
            @Override
            public Adapter caseSpecialB(SpecialB object) {
                return createSpecialBAdapter();
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
     * Creates a new adapter for an object of class '{@link org.iobserve.model.test.storage.Root <em>Root</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.iobserve.model.test.storage.Root
     * @generated
     */
    public Adapter createRootAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.iobserve.model.test.storage.Other <em>Other</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.iobserve.model.test.storage.Other
     * @generated
     */
    public Adapter createOtherAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.iobserve.model.test.storage.OtherSubType <em>Other Sub Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.iobserve.model.test.storage.OtherSubType
     * @generated
     */
    public Adapter createOtherSubTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.iobserve.model.test.storage.OtherInterface <em>Other Interface</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.iobserve.model.test.storage.OtherInterface
     * @generated
     */
    public Adapter createOtherInterfaceAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.iobserve.model.test.storage.SpecialA <em>Special A</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.iobserve.model.test.storage.SpecialA
     * @generated
     */
    public Adapter createSpecialAAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.iobserve.model.test.storage.SpecialB <em>Special B</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.iobserve.model.test.storage.SpecialB
     * @generated
     */
    public Adapter createSpecialBAdapter() {
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

} //StorageAdapterFactory
