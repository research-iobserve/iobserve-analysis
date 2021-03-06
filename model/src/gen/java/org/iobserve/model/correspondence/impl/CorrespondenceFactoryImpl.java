/**
 */
package org.iobserve.model.correspondence.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.iobserve.model.correspondence.*;
import org.iobserve.model.correspondence.AllocationEntry;
import org.iobserve.model.correspondence.AssemblyEntry;
import org.iobserve.model.correspondence.ComponentEntry;
import org.iobserve.model.correspondence.CorrespondenceFactory;
import org.iobserve.model.correspondence.CorrespondenceModel;
import org.iobserve.model.correspondence.CorrespondencePackage;
import org.iobserve.model.correspondence.OperationEntry;
import org.iobserve.model.correspondence.Part;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class CorrespondenceFactoryImpl extends EFactoryImpl implements CorrespondenceFactory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static CorrespondenceFactory init() {
        try {
            CorrespondenceFactory theCorrespondenceFactory = (CorrespondenceFactory)EPackage.Registry.INSTANCE.getEFactory(CorrespondencePackage.eNS_URI);
            if (theCorrespondenceFactory != null) {
                return theCorrespondenceFactory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new CorrespondenceFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CorrespondenceFactoryImpl() {
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
            case CorrespondencePackage.CORRESPONDENCE_MODEL: return createCorrespondenceModel();
            case CorrespondencePackage.DATA_TYPE_ENTRY: return createDataTypeEntry();
            case CorrespondencePackage.PART: return createPart();
            case CorrespondencePackage.COMPONENT_ENTRY: return createComponentEntry();
            case CorrespondencePackage.ALLOCATION_ENTRY: return createAllocationEntry();
            case CorrespondencePackage.ASSEMBLY_ENTRY: return createAssemblyEntry();
            case CorrespondencePackage.OPERATION_ENTRY: return createOperationEntry();
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
            case CorrespondencePackage.ESERVICE_TECHNOLOGY:
                return createEServiceTechnologyFromString(eDataType, initialValue);
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
            case CorrespondencePackage.ESERVICE_TECHNOLOGY:
                return convertEServiceTechnologyToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CorrespondenceModel createCorrespondenceModel() {
        CorrespondenceModelImpl correspondenceModel = new CorrespondenceModelImpl();
        return correspondenceModel;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DataTypeEntry createDataTypeEntry() {
        DataTypeEntryImpl dataTypeEntry = new DataTypeEntryImpl();
        return dataTypeEntry;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Part createPart() {
        PartImpl part = new PartImpl();
        return part;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ComponentEntry createComponentEntry() {
        ComponentEntryImpl componentEntry = new ComponentEntryImpl();
        return componentEntry;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AllocationEntry createAllocationEntry() {
        AllocationEntryImpl allocationEntry = new AllocationEntryImpl();
        return allocationEntry;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AssemblyEntry createAssemblyEntry() {
        AssemblyEntryImpl assemblyEntry = new AssemblyEntryImpl();
        return assemblyEntry;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OperationEntry createOperationEntry() {
        OperationEntryImpl operationEntry = new OperationEntryImpl();
        return operationEntry;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EServiceTechnology createEServiceTechnologyFromString(EDataType eDataType, String initialValue) {
        EServiceTechnology result = EServiceTechnology.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertEServiceTechnologyToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CorrespondencePackage getCorrespondencePackage() {
        return (CorrespondencePackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static CorrespondencePackage getPackage() {
        return CorrespondencePackage.eINSTANCE;
    }

} //CorrespondenceFactoryImpl
