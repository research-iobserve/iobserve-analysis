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

/**
 * Class to provide programmatically built test models.
 *
 * @author Lars Bluemke
 *
 */
public class TestModelBuilder {

    public static Repository createReposiory() {
        // Instantiate components, interfaces etc.
        final Repository repo = RepositoryFactory.eINSTANCE.createRepository();

        final BasicComponent queryInputComp = RepositoryFactory.eINSTANCE.createBasicComponent();
        final BasicComponent catalogSearchComp = RepositoryFactory.eINSTANCE.createBasicComponent();
        final BasicComponent creditCardBillingComp = RepositoryFactory.eINSTANCE.createBasicComponent();
        final CompositeComponent orderComp = RepositoryFactory.eINSTANCE.createCompositeComponent();

        final OperationProvidedRole providedSearchOperation = RepositoryFactory.eINSTANCE.createOperationProvidedRole();
        final OperationProvidedRole providedPayOperation = RepositoryFactory.eINSTANCE.createOperationProvidedRole();

        final OperationRequiredRole requiredSearchOperation = RepositoryFactory.eINSTANCE.createOperationRequiredRole();
        final OperationRequiredRole requiredPayOperation = RepositoryFactory.eINSTANCE.createOperationRequiredRole();

        final OperationInterface searchInterface = RepositoryFactory.eINSTANCE.createOperationInterface();
        final OperationInterface payInterface = RepositoryFactory.eINSTANCE.createOperationInterface();

        final OperationSignature getPriceSig = RepositoryFactory.eINSTANCE.createOperationSignature();
        final OperationSignature withdrawSig = RepositoryFactory.eINSTANCE.createOperationSignature();

        final PrimitiveDataType intDataType = RepositoryFactory.eINSTANCE.createPrimitiveDataType();

        // Repository
        repo.setEntityName("MyBookstore");
        repo.getComponents__Repository().add(orderComp);
        repo.getComponents__Repository().add(creditCardBillingComp);
        repo.getComponents__Repository().add(catalogSearchComp);
        repo.getComponents__Repository().add(queryInputComp);
        repo.getDataTypes__Repository().add(intDataType);
        repo.getInterfaces__Repository().add(payInterface);
        repo.getInterfaces__Repository().add(searchInterface);

        // Components
        queryInputComp.setEntityName("org.mybookstore.orderComponent.queryInputComponent");
        queryInputComp.getRequiredRoles_InterfaceRequiringEntity().add(requiredSearchOperation);
        queryInputComp.setRepository__RepositoryComponent(repo);

        catalogSearchComp.setEntityName("org.mybookstore.orderComponent.catologSearchComponent");
        catalogSearchComp.getProvidedRoles_InterfaceProvidingEntity().add(providedSearchOperation);
        catalogSearchComp.setRepository__RepositoryComponent(repo);

        creditCardBillingComp.setEntityName("org.mybookstore.paymentComponent");
        creditCardBillingComp.getProvidedRoles_InterfaceProvidingEntity().add(providedPayOperation);
        creditCardBillingComp.setRepository__RepositoryComponent(repo);

        orderComp.setEntityName("org.mybookstore.orderComponent");
        orderComp.getRequiredRoles_InterfaceRequiringEntity().add(requiredPayOperation);
        orderComp.setRepository__RepositoryComponent(repo);

        // Roles
        providedPayOperation.setEntityName("creditCardPayment");
        providedPayOperation.setProvidedInterface__OperationProvidedRole(payInterface);

        providedSearchOperation.setEntityName("catalogSearch");
        providedSearchOperation.setProvidedInterface__OperationProvidedRole(searchInterface);

        requiredPayOperation.setEntityName("payment");
        requiredPayOperation.setRequiredInterface__OperationRequiredRole(payInterface);

        requiredSearchOperation.setEntityName("search");
        requiredSearchOperation.setRequiredInterface__OperationRequiredRole(searchInterface);

        // Interfaces
        searchInterface.setEntityName("ISearch");
        searchInterface.getSignatures__OperationInterface().add(getPriceSig);
        searchInterface.setRepository__Interface(repo);

        payInterface.setEntityName("IPay");
        payInterface.getSignatures__OperationInterface().add(withdrawSig);
        payInterface.setRepository__Interface(repo);

        // Signatures
        getPriceSig.setEntityName("getPrice");
        getPriceSig.setReturnType__OperationSignature(intDataType);
        getPriceSig.setInterface__OperationSignature(searchInterface);

        withdrawSig.setEntityName("withdraw");
        withdrawSig.setReturnType__OperationSignature(intDataType);
        withdrawSig.setInterface__OperationSignature(payInterface);

        // Data type
        intDataType.setType(PrimitiveTypeEnum.INT);
        intDataType.setRepository__DataType(repo);

        return repo;
    }
}
