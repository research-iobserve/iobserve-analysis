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
    private final Repository repo = RepositoryFactory.eINSTANCE.createRepository();

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

    public TestModelBuilder() {
        this.createReposiory();
        this.createSystem();
    }

    private void createReposiory() {
        // Repository
        this.repo.setEntityName("MyBookstore");
        this.repo.getComponents__Repository().add(this.orderComp);
        this.repo.getComponents__Repository().add(this.paymentComp);
        this.repo.getComponents__Repository().add(this.catalogSearchComp);
        this.repo.getComponents__Repository().add(this.queryInputComp);
        this.repo.getDataTypes__Repository().add(this.intDataType);
        this.repo.getInterfaces__Repository().add(this.payInterface);
        this.repo.getInterfaces__Repository().add(this.searchInterface);

        // Components
        this.queryInputComp.setEntityName("org.mybookstore.orderComponent.queryInputComponent");
        this.queryInputComp.getRequiredRoles_InterfaceRequiringEntity().add(this.requiredSearchOperation);
        this.queryInputComp.setRepository__RepositoryComponent(this.repo);

        this.catalogSearchComp.setEntityName("org.mybookstore.orderComponent.catologSearchComponent");
        this.catalogSearchComp.getProvidedRoles_InterfaceProvidingEntity().add(this.providedSearchOperation);
        this.catalogSearchComp.setRepository__RepositoryComponent(this.repo);

        this.paymentComp.setEntityName("org.mybookstore.paymentComponent");
        this.paymentComp.getProvidedRoles_InterfaceProvidingEntity().add(this.providedPayOperation);
        this.paymentComp.setRepository__RepositoryComponent(this.repo);

        this.orderComp.setEntityName("org.mybookstore.orderComponent");
        this.orderComp.getRequiredRoles_InterfaceRequiringEntity().add(this.requiredPayOperation);
        this.orderComp.setRepository__RepositoryComponent(this.repo);

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
        this.searchInterface.setRepository__Interface(this.repo);

        this.payInterface.setEntityName("IPay");
        this.payInterface.getSignatures__OperationInterface().add(this.withdrawSig);
        this.payInterface.setRepository__Interface(this.repo);

        // Signatures
        this.getPriceSig.setEntityName("getPrice");
        this.getPriceSig.setReturnType__OperationSignature(this.intDataType);
        this.getPriceSig.setInterface__OperationSignature(this.searchInterface);

        this.withdrawSig.setEntityName("withdraw");
        this.withdrawSig.setReturnType__OperationSignature(this.intDataType);
        this.withdrawSig.setInterface__OperationSignature(this.payInterface);

        // Data type
        this.intDataType.setType(PrimitiveTypeEnum.INT);
        this.intDataType.setRepository__DataType(this.repo);

    }

    public System createSystem() {
        final System system = SystemFactory.eINSTANCE.createSystem();

        // final AssemblyContext context;
        // final context.ge
        //
        // system.getAssemblyContexts__ComposedStructure();
        // system.ge

        return system;
    }

    public Repository getRepository() {
        return this.repo;
    }
}
