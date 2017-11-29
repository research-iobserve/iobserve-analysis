/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.planning.systemadaptation.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.iobserve.planning.systemadaptation.AcquireAction;
import org.iobserve.planning.systemadaptation.Action;
import org.iobserve.planning.systemadaptation.AllocateAction;
import org.iobserve.planning.systemadaptation.AssemblyContextAction;
import org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction;
import org.iobserve.planning.systemadaptation.DeallocateAction;
import org.iobserve.planning.systemadaptation.MigrateAction;
import org.iobserve.planning.systemadaptation.ReplicateAction;
import org.iobserve.planning.systemadaptation.ResourceContainerAction;
import org.iobserve.planning.systemadaptation.SystemAdaptation;
import org.iobserve.planning.systemadaptation.TerminateAction;
import org.iobserve.planning.systemadaptation.systemadaptationPackage;

/**
 * <!-- begin-user-doc --> The <b>Adapter Factory</b> for the model. It provides an adapter
 * <code>createXXX</code> method for each class of the model. <!-- end-user-doc -->
 * 
 * @see org.iobserve.planning.systemadaptation.systemadaptationPackage
 * @generated
 */
public class systemadaptationAdapterFactory extends AdapterFactoryImpl {
    /**
     * The cached model package. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected static systemadaptationPackage modelPackage;

    /**
     * Creates an instance of the adapter factory. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public systemadaptationAdapterFactory() {
        if (systemadaptationAdapterFactory.modelPackage == null) {
            systemadaptationAdapterFactory.modelPackage = systemadaptationPackage.eINSTANCE;
        }
    }

    /**
     * Returns whether this factory is applicable for the type of the object. <!-- begin-user-doc
     * --> This implementation returns <code>true</code> if the object is either the model's package
     * or is an instance object of the model. <!-- end-user-doc -->
     * 
     * @return whether this factory is applicable for the type of the object.
     * @generated
     */
    @Override
    public boolean isFactoryForType(final Object object) {
        if (object == systemadaptationAdapterFactory.modelPackage) {
            return true;
        }
        if (object instanceof EObject) {
            return ((EObject) object).eClass().getEPackage() == systemadaptationAdapterFactory.modelPackage;
        }
        return false;
    }

    /**
     * The switch that delegates to the <code>createXXX</code> methods. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    protected systemadaptationSwitch<Adapter> modelSwitch = new systemadaptationSwitch<Adapter>() {
        @Override
        public Adapter caseSystemAdaptation(final SystemAdaptation object) {
            return systemadaptationAdapterFactory.this.createSystemAdaptationAdapter();
        }

        @Override
        public Adapter caseAction(final Action object) {
            return systemadaptationAdapterFactory.this.createActionAdapter();
        }

        @Override
        public Adapter caseAssemblyContextAction(final AssemblyContextAction object) {
            return systemadaptationAdapterFactory.this.createAssemblyContextActionAdapter();
        }

        @Override
        public Adapter caseResourceContainerAction(final ResourceContainerAction object) {
            return systemadaptationAdapterFactory.this.createResourceContainerActionAdapter();
        }

        @Override
        public Adapter caseChangeRepositoryComponentAction(final ChangeRepositoryComponentAction object) {
            return systemadaptationAdapterFactory.this.createChangeRepositoryComponentActionAdapter();
        }

        @Override
        public Adapter caseAllocateAction(final AllocateAction object) {
            return systemadaptationAdapterFactory.this.createAllocateActionAdapter();
        }

        @Override
        public Adapter caseDeallocateAction(final DeallocateAction object) {
            return systemadaptationAdapterFactory.this.createDeallocateActionAdapter();
        }

        @Override
        public Adapter caseMigrateAction(final MigrateAction object) {
            return systemadaptationAdapterFactory.this.createMigrateActionAdapter();
        }

        @Override
        public Adapter caseAcquireAction(final AcquireAction object) {
            return systemadaptationAdapterFactory.this.createAcquireActionAdapter();
        }

        @Override
        public Adapter caseTerminateAction(final TerminateAction object) {
            return systemadaptationAdapterFactory.this.createTerminateActionAdapter();
        }

        @Override
        public Adapter caseReplicateAction(final ReplicateAction object) {
            return systemadaptationAdapterFactory.this.createReplicateActionAdapter();
        }

        @Override
        public Adapter defaultCase(final EObject object) {
            return systemadaptationAdapterFactory.this.createEObjectAdapter();
        }
    };

    /**
     * Creates an adapter for the <code>target</code>. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param target
     *            the object to adapt.
     * @return the adapter for the <code>target</code>.
     * @generated
     */
    @Override
    public Adapter createAdapter(final Notifier target) {
        return this.modelSwitch.doSwitch((EObject) target);
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.iobserve.planning.systemadaptation.SystemAdaptation <em>System Adaptation</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.iobserve.planning.systemadaptation.SystemAdaptation
     * @generated
     */
    public Adapter createSystemAdaptationAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.iobserve.planning.systemadaptation.Action <em>Action</em>}'. <!-- begin-user-doc
     * --> This default implementation returns null so that we can easily ignore cases; it's useful
     * to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.iobserve.planning.systemadaptation.Action
     * @generated
     */
    public Adapter createActionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.iobserve.planning.systemadaptation.AssemblyContextAction <em>Assembly Context
     * Action</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we
     * can easily ignore cases; it's useful to ignore a case when inheritance will catch all the
     * cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.iobserve.planning.systemadaptation.AssemblyContextAction
     * @generated
     */
    public Adapter createAssemblyContextActionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.iobserve.planning.systemadaptation.ResourceContainerAction <em>Resource Container
     * Action</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we
     * can easily ignore cases; it's useful to ignore a case when inheritance will catch all the
     * cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.iobserve.planning.systemadaptation.ResourceContainerAction
     * @generated
     */
    public Adapter createResourceContainerActionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction <em>Change
     * Repository Component Action</em>}'. <!-- begin-user-doc --> This default implementation
     * returns null so that we can easily ignore cases; it's useful to ignore a case when
     * inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction
     * @generated
     */
    public Adapter createChangeRepositoryComponentActionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.iobserve.planning.systemadaptation.AllocateAction <em>Allocate Action</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.iobserve.planning.systemadaptation.AllocateAction
     * @generated
     */
    public Adapter createAllocateActionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.iobserve.planning.systemadaptation.DeallocateAction <em>Deallocate Action</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.iobserve.planning.systemadaptation.DeallocateAction
     * @generated
     */
    public Adapter createDeallocateActionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.iobserve.planning.systemadaptation.MigrateAction <em>Migrate Action</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.iobserve.planning.systemadaptation.MigrateAction
     * @generated
     */
    public Adapter createMigrateActionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.iobserve.planning.systemadaptation.AcquireAction <em>Acquire Action</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.iobserve.planning.systemadaptation.AcquireAction
     * @generated
     */
    public Adapter createAcquireActionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.iobserve.planning.systemadaptation.TerminateAction <em>Terminate Action</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.iobserve.planning.systemadaptation.TerminateAction
     * @generated
     */
    public Adapter createTerminateActionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.iobserve.planning.systemadaptation.ReplicateAction <em>Replicate Action</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.iobserve.planning.systemadaptation.ReplicateAction
     * @generated
     */
    public Adapter createReplicateActionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for the default case. <!-- begin-user-doc --> This default
     * implementation returns null. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @generated
     */
    public Adapter createEObjectAdapter() {
        return null;
    }

} // systemadaptationAdapterFactory
