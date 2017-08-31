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
package org.iobserve.analysis.modelneo4j.test;

import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.CompositionFactory;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.CompositeComponent;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationRequiredRole;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.PrimitiveDataType;
import org.palladiosimulator.pcm.repository.PrimitiveTypeEnum;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryFactory;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.system.SystemFactory;

/**
 * Class to provide programmatically built test models.
 *
 * @author Lars Bluemke
 *
 */
public class TestModelBuilder {

    // Repository components
    private final Repository repository = RepositoryFactory.eINSTANCE.createRepository();

    private final BasicComponent queryInputComp = RepositoryFactory.eINSTANCE.createBasicComponent();
    private final BasicComponent catalogSearchComp = RepositoryFactory.eINSTANCE.createBasicComponent();
    private final BasicComponent paymentComp = RepositoryFactory.eINSTANCE.createBasicComponent();
    private final CompositeComponent orderComp = RepositoryFactory.eINSTANCE.createCompositeComponent();

    private final OperationProvidedRole providedSearchOperation = RepositoryFactory.eINSTANCE
            .createOperationProvidedRole();
    private final OperationProvidedRole providedPayOperation = RepositoryFactory.eINSTANCE
            .createOperationProvidedRole();

    private final OperationRequiredRole requiredSearchOperation = RepositoryFactory.eINSTANCE
            .createOperationRequiredRole();
    private final OperationRequiredRole requiredPayOperation = RepositoryFactory.eINSTANCE
            .createOperationRequiredRole();

    private final OperationInterface searchInterface = RepositoryFactory.eINSTANCE.createOperationInterface();
    private final OperationInterface payInterface = RepositoryFactory.eINSTANCE.createOperationInterface();

    private final OperationSignature getPriceSig = RepositoryFactory.eINSTANCE.createOperationSignature();
    private final OperationSignature withdrawSig = RepositoryFactory.eINSTANCE.createOperationSignature();

    private final PrimitiveDataType intDataType = RepositoryFactory.eINSTANCE.createPrimitiveDataType();

    // System components
    private final System system = SystemFactory.eINSTANCE.createSystem();

    private final AssemblyContext businessOrderContext = CompositionFactory.eINSTANCE.createAssemblyContext();
    private final AssemblyContext privateOrderContext = CompositionFactory.eINSTANCE.createAssemblyContext();
    private final AssemblyContext paymentContext = CompositionFactory.eINSTANCE.createAssemblyContext();

    private final AssemblyConnector businessPayConnector = CompositionFactory.eINSTANCE.createAssemblyConnector();
    private final AssemblyConnector privatePayConnector = CompositionFactory.eINSTANCE.createAssemblyConnector();

    public TestModelBuilder() {
        this.createReposiory();
        this.createSystem();
    }

    private void createReposiory() {
        // Repository
        this.repository.setEntityName("MyBookstore");
        this.repository.getComponents__Repository().add(this.orderComp);
        this.repository.getComponents__Repository().add(this.paymentComp);
        this.repository.getComponents__Repository().add(this.catalogSearchComp);
        this.repository.getComponents__Repository().add(this.queryInputComp);
        this.repository.getDataTypes__Repository().add(this.intDataType);
        this.repository.getInterfaces__Repository().add(this.payInterface);
        this.repository.getInterfaces__Repository().add(this.searchInterface);

        // Components
        this.queryInputComp.setEntityName("org.mybookstore.orderComponent.queryInputComponent");
        this.queryInputComp.getRequiredRoles_InterfaceRequiringEntity().add(this.requiredSearchOperation);
        this.queryInputComp.setRepository__RepositoryComponent(this.repository);

        this.catalogSearchComp.setEntityName("org.mybookstore.orderComponent.catologSearchComponent");
        this.catalogSearchComp.getProvidedRoles_InterfaceProvidingEntity().add(this.providedSearchOperation);
        this.catalogSearchComp.setRepository__RepositoryComponent(this.repository);

        this.paymentComp.setEntityName("org.mybookstore.paymentComponent");
        this.paymentComp.getProvidedRoles_InterfaceProvidingEntity().add(this.providedPayOperation);
        this.paymentComp.setRepository__RepositoryComponent(this.repository);

        this.orderComp.setEntityName("org.mybookstore.orderComponent");
        this.orderComp.getRequiredRoles_InterfaceRequiringEntity().add(this.requiredPayOperation);
        this.orderComp.setRepository__RepositoryComponent(this.repository);

        // Roles
        this.providedPayOperation.setEntityName("creditCardPayment");
        this.providedPayOperation.setProvidedInterface__OperationProvidedRole(this.payInterface);

        this.providedSearchOperation.setEntityName("catalogSearch");
        this.providedSearchOperation.setProvidedInterface__OperationProvidedRole(this.searchInterface);

        this.requiredPayOperation.setEntityName("payment");
        this.requiredPayOperation.setRequiredInterface__OperationRequiredRole(this.payInterface);

        this.requiredSearchOperation.setEntityName("search");
        this.requiredSearchOperation.setRequiredInterface__OperationRequiredRole(this.searchInterface);

        // Interfaces
        this.searchInterface.setEntityName("ISearch");
        this.searchInterface.getSignatures__OperationInterface().add(this.getPriceSig);
        this.searchInterface.setRepository__Interface(this.repository);

        this.payInterface.setEntityName("IPay");
        this.payInterface.getSignatures__OperationInterface().add(this.withdrawSig);
        this.payInterface.setRepository__Interface(this.repository);

        // Signatures
        this.getPriceSig.setEntityName("getPrice");
        this.getPriceSig.setReturnType__OperationSignature(this.intDataType);
        this.getPriceSig.setInterface__OperationSignature(this.searchInterface);

        this.withdrawSig.setEntityName("withdraw");
        this.withdrawSig.setReturnType__OperationSignature(this.intDataType);
        this.withdrawSig.setInterface__OperationSignature(this.payInterface);

        // Data type
        this.intDataType.setType(PrimitiveTypeEnum.INT);
        this.intDataType.setRepository__DataType(this.repository);

    }

    public void createSystem() {
        // System
        this.system.setEntityName("MyBookstore");
        this.system.getAssemblyContexts__ComposedStructure().add(this.businessOrderContext);
        this.system.getAssemblyContexts__ComposedStructure().add(this.paymentContext);
        this.system.getAssemblyContexts__ComposedStructure().add(this.privateOrderContext);
        this.system.getConnectors__ComposedStructure().add(this.businessPayConnector);
        this.system.getConnectors__ComposedStructure().add(this.privatePayConnector);

        // Assembly contexts
        this.businessOrderContext.setEntityName("businessOrderContext_" + this.orderComp.getEntityName());
        this.privateOrderContext.setEntityName("privateOrderContext_" + this.orderComp.getEntityName());
        this.paymentContext.setEntityName("paymentContext_" + this.paymentComp.getEntityName());

        // Assembly connectors
        this.businessPayConnector.setEntityName("businessPayment");
        this.businessPayConnector.setProvidedRole_AssemblyConnector(this.providedPayOperation);
        this.businessPayConnector.setRequiredRole_AssemblyConnector(this.requiredPayOperation);
        this.businessPayConnector.setProvidingAssemblyContext_AssemblyConnector(this.paymentContext);
        this.businessPayConnector.setRequiringAssemblyContext_AssemblyConnector(this.businessOrderContext);

        this.privatePayConnector.setEntityName("privatePayment");
        this.privatePayConnector.setProvidedRole_AssemblyConnector(this.providedPayOperation);
        this.privatePayConnector.setRequiredRole_AssemblyConnector(this.requiredPayOperation);
        this.privatePayConnector.setProvidingAssemblyContext_AssemblyConnector(this.paymentContext);
        this.privatePayConnector.setRequiringAssemblyContext_AssemblyConnector(this.privateOrderContext);

    }

    public Repository getRepository() {
        return this.repository;
    }

    public System getSystem() {
        return this.system;
    }
}
