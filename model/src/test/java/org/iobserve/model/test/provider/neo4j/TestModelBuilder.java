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
package org.iobserve.model.test.provider.neo4j;

import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.allocation.AllocationFactory;
import org.palladiosimulator.pcm.core.CoreFactory;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
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
import org.palladiosimulator.pcm.resourceenvironment.CommunicationLinkResourceSpecification;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ProcessingResourceSpecification;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;
import org.palladiosimulator.pcm.resourcetype.CommunicationLinkResourceType;
import org.palladiosimulator.pcm.resourcetype.ProcessingResourceType;
import org.palladiosimulator.pcm.resourcetype.ResourcetypeFactory;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.system.SystemFactory;
import org.palladiosimulator.pcm.usagemodel.ClosedWorkload;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.Start;
import org.palladiosimulator.pcm.usagemodel.Stop;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;
import org.palladiosimulator.pcm.usagemodel.UsagemodelFactory;

/**
 * Class to provide programmatically built test models.
 *
 * @author Lars Bluemke
 *
 */
public class TestModelBuilder { // NOCS,NOPMD test

    // Repository components
    private final Repository repository = RepositoryFactory.eINSTANCE.createRepository();

    private final BasicComponent queryInputComponent = RepositoryFactory.eINSTANCE.createBasicComponent();
    private final BasicComponent queryProcessingComponent = RepositoryFactory.eINSTANCE.createBasicComponent();
    private final BasicComponent catalogSearchComponent = RepositoryFactory.eINSTANCE.createBasicComponent();
    private final BasicComponent paymentComponent = RepositoryFactory.eINSTANCE.createBasicComponent();
    private final CompositeComponent orderComponent = RepositoryFactory.eINSTANCE.createCompositeComponent();

    private final OperationProvidedRole providedInputOperation = RepositoryFactory.eINSTANCE
            .createOperationProvidedRole();
    private final OperationProvidedRole providedSearchOperation = RepositoryFactory.eINSTANCE
            .createOperationProvidedRole();
    private final OperationProvidedRole providedPayOperation = RepositoryFactory.eINSTANCE
            .createOperationProvidedRole();
    private final OperationRequiredRole requiredInputOperation = RepositoryFactory.eINSTANCE
            .createOperationRequiredRole();
    private final OperationRequiredRole requiredSearchOperation = RepositoryFactory.eINSTANCE
            .createOperationRequiredRole();
    private final OperationRequiredRole requiredPayOperation = RepositoryFactory.eINSTANCE
            .createOperationRequiredRole();

    private final OperationInterface queryInputInterface = RepositoryFactory.eINSTANCE.createOperationInterface();
    private final OperationInterface searchInterface = RepositoryFactory.eINSTANCE.createOperationInterface();
    private final OperationInterface payInterface = RepositoryFactory.eINSTANCE.createOperationInterface();

    private final OperationSignature getQuerySignature = RepositoryFactory.eINSTANCE.createOperationSignature();
    private final OperationSignature getPriceSignature = RepositoryFactory.eINSTANCE.createOperationSignature();
    private final OperationSignature withdrawSignature = RepositoryFactory.eINSTANCE.createOperationSignature();

    private final PrimitiveDataType intDataType = RepositoryFactory.eINSTANCE.createPrimitiveDataType();
    private final PrimitiveDataType stringDataType = RepositoryFactory.eINSTANCE.createPrimitiveDataType();

    // System components
    private final System system = SystemFactory.eINSTANCE.createSystem();

    private final AssemblyContext queryInputAssemblyContext = CompositionFactory.eINSTANCE.createAssemblyContext();
    private final AssemblyContext businessOrderAssemblyContext = CompositionFactory.eINSTANCE.createAssemblyContext();
    private final AssemblyContext privateOrderAssemblyContext = CompositionFactory.eINSTANCE.createAssemblyContext();
    private final AssemblyContext paymentAssemblyContext = CompositionFactory.eINSTANCE.createAssemblyContext();

    private final AssemblyConnector businessQueryInputConnector = CompositionFactory.eINSTANCE
            .createAssemblyConnector();
    private final AssemblyConnector privateQueryInputConnector = CompositionFactory.eINSTANCE.createAssemblyConnector();
    private final AssemblyConnector businessPayConnector = CompositionFactory.eINSTANCE.createAssemblyConnector();
    private final AssemblyConnector privatePayConnector = CompositionFactory.eINSTANCE.createAssemblyConnector();

    // Resource environment components
    private final ResourceEnvironment resourceEnvironment = ResourceenvironmentFactory.eINSTANCE
            .createResourceEnvironment();
    private final ResourceContainer client1 = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
    private final ResourceContainer client2 = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
    private final ResourceContainer orderServer = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
    private final ResourceContainer paymentServer = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();

    private final ProcessingResourceSpecification client1Specification = ResourceenvironmentFactory.eINSTANCE
            .createProcessingResourceSpecification();
    private final ProcessingResourceSpecification client2Specification = ResourceenvironmentFactory.eINSTANCE
            .createProcessingResourceSpecification();
    private final ProcessingResourceSpecification orderServerSpecification = ResourceenvironmentFactory.eINSTANCE
            .createProcessingResourceSpecification();
    private final ProcessingResourceSpecification paymentServerSpecification = ResourceenvironmentFactory.eINSTANCE
            .createProcessingResourceSpecification();

    private final ProcessingResourceType client1Type = ResourcetypeFactory.eINSTANCE.createProcessingResourceType();
    private final ProcessingResourceType client2Type = ResourcetypeFactory.eINSTANCE.createProcessingResourceType();
    private final ProcessingResourceType orderServerType = ResourcetypeFactory.eINSTANCE.createProcessingResourceType();
    private final ProcessingResourceType paymentServerType = ResourcetypeFactory.eINSTANCE
            .createProcessingResourceType();

    private final LinkingResource lan1 = ResourceenvironmentFactory.eINSTANCE.createLinkingResource();
    private final CommunicationLinkResourceSpecification lan1Specification = ResourceenvironmentFactory.eINSTANCE
            .createCommunicationLinkResourceSpecification();
    private final CommunicationLinkResourceType lan1Type = ResourcetypeFactory.eINSTANCE
            .createCommunicationLinkResourceType();

    // Allocation components
    private final Allocation allocation = AllocationFactory.eINSTANCE.createAllocation();
    private final AllocationContext client1AllocationContext = AllocationFactory.eINSTANCE.createAllocationContext();
    private final AllocationContext client2AllocationContext = AllocationFactory.eINSTANCE.createAllocationContext();
    private final AllocationContext businessOrderServerAllocationContext = AllocationFactory.eINSTANCE
            .createAllocationContext();
    private final AllocationContext privateOrderServerAllocationContext = AllocationFactory.eINSTANCE
            .createAllocationContext();
    private final AllocationContext paymentServerAllocationContext = AllocationFactory.eINSTANCE
            .createAllocationContext();

    // Usage model components
    private final UsageModel usageModel = UsagemodelFactory.eINSTANCE.createUsageModel();
    private final UsageScenario usageScenarioGroup0 = UsagemodelFactory.eINSTANCE.createUsageScenario();

    private final ScenarioBehaviour buyBookScenarioBehaviour = UsagemodelFactory.eINSTANCE.createScenarioBehaviour();
    private final Start startScenario = UsagemodelFactory.eINSTANCE.createStart();
    private final EntryLevelSystemCall getQueryCall = UsagemodelFactory.eINSTANCE.createEntryLevelSystemCall();
    private final EntryLevelSystemCall getPriceCall = UsagemodelFactory.eINSTANCE.createEntryLevelSystemCall();
    private final EntryLevelSystemCall withdrawCall = UsagemodelFactory.eINSTANCE.createEntryLevelSystemCall();
    private final Stop stopScenario = UsagemodelFactory.eINSTANCE.createStop();

    private final ClosedWorkload closedWorkload = UsagemodelFactory.eINSTANCE.createClosedWorkload();
    private final PCMRandomVariable thinkTime = CoreFactory.eINSTANCE.createPCMRandomVariable();

    /**
     * Creates a new TestModelBuilder and initializes the test models.
     */
    public TestModelBuilder() {
        this.createReposiory();
        this.createSystem();
        this.createResourceEnvironment();
        this.createAllocation();
        this.createUsageModel();
    }

    private void createReposiory() {
        // Repository
        this.repository.setEntityName("MyBookstore");
        this.repository.getComponents__Repository().add(this.queryInputComponent);
        this.repository.getComponents__Repository().add(this.orderComponent);
        this.repository.getComponents__Repository().add(this.paymentComponent);
        this.repository.getComponents__Repository().add(this.catalogSearchComponent);
        this.repository.getComponents__Repository().add(this.queryProcessingComponent);
        this.repository.getDataTypes__Repository().add(this.intDataType);
        this.repository.getInterfaces__Repository().add(this.queryInputInterface);
        this.repository.getInterfaces__Repository().add(this.payInterface);
        this.repository.getInterfaces__Repository().add(this.searchInterface);

        // Components
        this.queryInputComponent.setEntityName("org.mybookstore.queryInputComponent");
        this.queryInputComponent.getRequiredRoles_InterfaceRequiringEntity().add(this.requiredInputOperation);
        this.queryInputComponent.setRepository__RepositoryComponent(this.repository);

        this.queryProcessingComponent.setEntityName("org.mybookstore.orderComponent.queryProcessingComponent");
        this.queryProcessingComponent.getRequiredRoles_InterfaceRequiringEntity().add(this.requiredSearchOperation);
        this.queryProcessingComponent.setRepository__RepositoryComponent(this.repository);

        this.catalogSearchComponent.setEntityName("org.mybookstore.orderComponent.catologSearchComponent");
        this.catalogSearchComponent.getProvidedRoles_InterfaceProvidingEntity().add(this.providedSearchOperation);
        this.catalogSearchComponent.setRepository__RepositoryComponent(this.repository);

        this.paymentComponent.setEntityName("org.mybookstore.paymentComponent");
        this.paymentComponent.getProvidedRoles_InterfaceProvidingEntity().add(this.providedPayOperation);
        this.paymentComponent.setRepository__RepositoryComponent(this.repository);

        this.orderComponent.setEntityName("org.mybookstore.orderComponent");
        this.orderComponent.getRequiredRoles_InterfaceRequiringEntity().add(this.requiredPayOperation);
        this.orderComponent.getProvidedRoles_InterfaceProvidingEntity().add(this.providedInputOperation);
        this.orderComponent.setRepository__RepositoryComponent(this.repository);

        // Roles
        this.providedInputOperation.setEntityName("queryStringInput");
        this.providedInputOperation.setProvidedInterface__OperationProvidedRole(this.queryInputInterface);

        this.providedPayOperation.setEntityName("creditCardPayment");
        this.providedPayOperation.setProvidedInterface__OperationProvidedRole(this.payInterface);

        this.providedSearchOperation.setEntityName("catalogSearch");
        this.providedSearchOperation.setProvidedInterface__OperationProvidedRole(this.searchInterface);

        this.requiredInputOperation.setEntityName("input");
        this.requiredInputOperation.setRequiredInterface__OperationRequiredRole(this.queryInputInterface);

        this.requiredPayOperation.setEntityName("payment");
        this.requiredPayOperation.setRequiredInterface__OperationRequiredRole(this.payInterface);

        this.requiredSearchOperation.setEntityName("search");
        this.requiredSearchOperation.setRequiredInterface__OperationRequiredRole(this.searchInterface);

        // Interfaces
        this.queryInputInterface.setEntityName("IQueryInput");
        this.queryInputInterface.getSignatures__OperationInterface().add(this.getQuerySignature);
        this.queryInputInterface.setRepository__Interface(this.repository);

        this.searchInterface.setEntityName("ISearch");
        this.searchInterface.getSignatures__OperationInterface().add(this.getPriceSignature);
        this.searchInterface.setRepository__Interface(this.repository);

        this.payInterface.setEntityName("IPay");
        this.payInterface.getSignatures__OperationInterface().add(this.withdrawSignature);
        this.payInterface.setRepository__Interface(this.repository);

        // Signatures
        this.getQuerySignature.setEntityName("getQuery");
        this.getQuerySignature.setReturnType__OperationSignature(this.stringDataType);
        this.getQuerySignature.setInterface__OperationSignature(this.queryInputInterface);

        this.getPriceSignature.setEntityName("getPrice");
        this.getPriceSignature.setReturnType__OperationSignature(this.intDataType);
        this.getPriceSignature.setInterface__OperationSignature(this.searchInterface);

        this.withdrawSignature.setEntityName("withdraw");
        this.withdrawSignature.setReturnType__OperationSignature(this.intDataType);
        this.withdrawSignature.setInterface__OperationSignature(this.payInterface);

        // Data type
        this.intDataType.setType(PrimitiveTypeEnum.INT);
        this.intDataType.setRepository__DataType(this.repository);

        this.stringDataType.setType(PrimitiveTypeEnum.STRING);
        this.stringDataType.setRepository__DataType(this.repository);

    }

    private void createSystem() {
        // System
        this.system.setEntityName("MyBookstore");
        this.system.getAssemblyContexts__ComposedStructure().add(this.queryInputAssemblyContext);
        this.system.getAssemblyContexts__ComposedStructure().add(this.businessOrderAssemblyContext);
        this.system.getAssemblyContexts__ComposedStructure().add(this.paymentAssemblyContext);
        this.system.getAssemblyContexts__ComposedStructure().add(this.privateOrderAssemblyContext);
        this.system.getConnectors__ComposedStructure().add(this.businessQueryInputConnector);
        this.system.getConnectors__ComposedStructure().add(this.privateQueryInputConnector);
        this.system.getConnectors__ComposedStructure().add(this.businessPayConnector);
        this.system.getConnectors__ComposedStructure().add(this.privatePayConnector);

        // Assembly contexts
        this.queryInputAssemblyContext.setEntityName("queryInputContext_" + this.queryInputComponent.getEntityName());
        this.businessOrderAssemblyContext.setEntityName("businessOrderContext_" + this.orderComponent.getEntityName());
        this.privateOrderAssemblyContext.setEntityName("privateOrderContext_" + this.orderComponent.getEntityName());
        this.paymentAssemblyContext.setEntityName("paymentContext_" + this.paymentComponent.getEntityName());

        // Assembly connectors
        this.businessQueryInputConnector.setEntityName("businessQueryInput");
        this.businessQueryInputConnector.setProvidedRole_AssemblyConnector(this.providedInputOperation);
        this.businessQueryInputConnector.setRequiredRole_AssemblyConnector(this.requiredInputOperation);
        this.businessQueryInputConnector
                .setProvidingAssemblyContext_AssemblyConnector(this.businessOrderAssemblyContext);
        this.businessQueryInputConnector.setRequiringAssemblyContext_AssemblyConnector(this.queryInputAssemblyContext);

        this.privateQueryInputConnector.setEntityName("privateQueryInput");
        this.privateQueryInputConnector.setProvidedRole_AssemblyConnector(this.providedInputOperation);
        this.privateQueryInputConnector.setRequiredRole_AssemblyConnector(this.requiredInputOperation);
        this.privateQueryInputConnector.setProvidingAssemblyContext_AssemblyConnector(this.privateOrderAssemblyContext);
        this.privateQueryInputConnector.setRequiringAssemblyContext_AssemblyConnector(this.queryInputAssemblyContext);

        this.businessPayConnector.setEntityName("businessPayment");
        this.businessPayConnector.setProvidedRole_AssemblyConnector(this.providedPayOperation);
        this.businessPayConnector.setRequiredRole_AssemblyConnector(this.requiredPayOperation);
        this.businessPayConnector.setProvidingAssemblyContext_AssemblyConnector(this.paymentAssemblyContext);
        this.businessPayConnector.setRequiringAssemblyContext_AssemblyConnector(this.businessOrderAssemblyContext);

        this.privatePayConnector.setEntityName("privatePayment");
        this.privatePayConnector.setProvidedRole_AssemblyConnector(this.providedPayOperation);
        this.privatePayConnector.setRequiredRole_AssemblyConnector(this.requiredPayOperation);
        this.privatePayConnector.setProvidingAssemblyContext_AssemblyConnector(this.paymentAssemblyContext);
        this.privatePayConnector.setRequiringAssemblyContext_AssemblyConnector(this.privateOrderAssemblyContext);

    }

    private void createResourceEnvironment() {
        // Resource environment
        this.resourceEnvironment.setEntityName("defaultResourceEnvironment");
        this.resourceEnvironment.getResourceContainer_ResourceEnvironment().add(this.client1);
        this.resourceEnvironment.getResourceContainer_ResourceEnvironment().add(this.client2);
        this.resourceEnvironment.getResourceContainer_ResourceEnvironment().add(this.orderServer);
        this.resourceEnvironment.getResourceContainer_ResourceEnvironment().add(this.paymentServer);
        this.resourceEnvironment.getLinkingResources__ResourceEnvironment().add(this.lan1);

        // Resource container
        this.client1.setEntityName("user0815's MacBook");
        this.client1.setResourceEnvironment_ResourceContainer(this.resourceEnvironment);
        this.client1.getActiveResourceSpecifications_ResourceContainer().add(this.client1Specification);

        this.client2.setEntityName("user0816's ThinkPad");
        this.client2.setResourceEnvironment_ResourceContainer(this.resourceEnvironment);
        this.client2.getActiveResourceSpecifications_ResourceContainer().add(this.client2Specification);

        this.orderServer.setEntityName("orderServer");
        this.orderServer.setResourceEnvironment_ResourceContainer(this.resourceEnvironment);
        this.orderServer.getActiveResourceSpecifications_ResourceContainer().add(this.orderServerSpecification);

        this.paymentServer.setEntityName("paymentServer");
        this.paymentServer.setResourceEnvironment_ResourceContainer(this.resourceEnvironment);
        this.paymentServer.getActiveResourceSpecifications_ResourceContainer().add(this.paymentServerSpecification);

        // Resource container specifications
        this.client1Specification.setActiveResourceType_ActiveResourceSpecification(this.client1Type);
        this.client1Specification.setMTTF(42);
        this.client1Specification.setMTTR(42);

        this.client2Specification.setActiveResourceType_ActiveResourceSpecification(this.client2Type);
        this.client2Specification.setMTTF(42);
        this.client2Specification.setMTTR(42);

        this.orderServerSpecification.setActiveResourceType_ActiveResourceSpecification(this.orderServerType);
        this.orderServerSpecification.setMTTF(42);
        this.orderServerSpecification.setMTTR(42);

        this.paymentServerSpecification.setActiveResourceType_ActiveResourceSpecification(this.paymentServerType);
        this.paymentServerSpecification.setMTTF(42);
        this.paymentServerSpecification.setMTTR(42);

        // Resource container types
        this.client1Type.setEntityName("MacBook");
        this.client2Type.setEntityName("ThinkPad");
        this.orderServerType.setEntityName("Cisco Business Server");
        this.paymentServerType.setEntityName("Lenovo Security Server");

        // Linking resource
        this.lan1.setEntityName("lan-1");
        this.lan1.setResourceEnvironment_LinkingResource(this.resourceEnvironment);
        this.lan1.setCommunicationLinkResourceSpecifications_LinkingResource(this.lan1Specification);
        this.lan1.getConnectedResourceContainers_LinkingResource().add(this.orderServer);
        this.lan1.getConnectedResourceContainers_LinkingResource().add(this.paymentServer);

        // Linking resource specification
        this.lan1Specification.setCommunicationLinkResourceType_CommunicationLinkResourceSpecification(this.lan1Type);
        this.lan1Specification.setFailureProbability(0.01);

        // Linking resource type
        this.lan1Type.setEntityName("Cat.7 LAN");

    }

    private void createAllocation() {
        // Allocation
        this.allocation.setEntityName("defaultAllocation");
        this.allocation.setSystem_Allocation(this.system);
        this.allocation.setTargetResourceEnvironment_Allocation(this.resourceEnvironment);
        this.allocation.getAllocationContexts_Allocation().add(this.client1AllocationContext);
        this.allocation.getAllocationContexts_Allocation().add(this.client2AllocationContext);
        this.allocation.getAllocationContexts_Allocation().add(this.businessOrderServerAllocationContext);
        this.allocation.getAllocationContexts_Allocation().add(this.privateOrderServerAllocationContext);
        this.allocation.getAllocationContexts_Allocation().add(this.paymentServerAllocationContext);

        // Allocation contexts
        this.client1AllocationContext.setEntityName("Allocation1_" + this.queryInputAssemblyContext.getEntityName());
        this.client1AllocationContext.setAssemblyContext_AllocationContext(this.queryInputAssemblyContext);
        this.client1AllocationContext.setResourceContainer_AllocationContext(this.client1);
        this.client1AllocationContext.setAllocation_AllocationContext(this.allocation);

        this.client2AllocationContext.setEntityName("Allocation2_" + this.queryInputAssemblyContext.getEntityName());
        this.client2AllocationContext.setAssemblyContext_AllocationContext(this.queryInputAssemblyContext);
        this.client2AllocationContext.setResourceContainer_AllocationContext(this.client2);
        this.client2AllocationContext.setAllocation_AllocationContext(this.allocation);

        this.businessOrderServerAllocationContext
                .setEntityName("Allocation1_" + this.businessOrderAssemblyContext.getEntityName());
        this.businessOrderServerAllocationContext
                .setAssemblyContext_AllocationContext(this.businessOrderAssemblyContext);
        this.businessOrderServerAllocationContext.setResourceContainer_AllocationContext(this.orderServer);
        this.businessOrderServerAllocationContext.setAllocation_AllocationContext(this.allocation);

        this.privateOrderServerAllocationContext
                .setEntityName("Allocation2_" + this.privateOrderAssemblyContext.getEntityName());
        this.privateOrderServerAllocationContext.setAssemblyContext_AllocationContext(this.privateOrderAssemblyContext);
        this.privateOrderServerAllocationContext.setResourceContainer_AllocationContext(this.orderServer);
        this.privateOrderServerAllocationContext.setAllocation_AllocationContext(this.allocation);

        this.paymentServerAllocationContext.setEntityName("Allocation_" + this.paymentAssemblyContext.getEntityName());
        this.paymentServerAllocationContext.setAssemblyContext_AllocationContext(this.paymentAssemblyContext);
        this.paymentServerAllocationContext.setResourceContainer_AllocationContext(this.paymentServer);
        this.paymentServerAllocationContext.setAllocation_AllocationContext(this.allocation);
    }

    private void createUsageModel() {
        // Usage model
        this.usageModel.getUsageScenario_UsageModel().add(this.usageScenarioGroup0);

        // Usage scenario
        this.usageScenarioGroup0.setEntityName("Usage scenario of user group 0");
        this.usageScenarioGroup0.setScenarioBehaviour_UsageScenario(this.buyBookScenarioBehaviour);
        this.usageScenarioGroup0.setWorkload_UsageScenario(this.closedWorkload);

        // Scenario behavior
        this.buyBookScenarioBehaviour.setEntityName("Buy a book");
        this.buyBookScenarioBehaviour.setUsageScenario_SenarioBehaviour(this.usageScenarioGroup0);
        this.buyBookScenarioBehaviour.getActions_ScenarioBehaviour().add(this.startScenario);
        this.buyBookScenarioBehaviour.getActions_ScenarioBehaviour().add(this.getQueryCall);
        this.buyBookScenarioBehaviour.getActions_ScenarioBehaviour().add(this.getPriceCall);
        this.buyBookScenarioBehaviour.getActions_ScenarioBehaviour().add(this.withdrawCall);
        this.buyBookScenarioBehaviour.getActions_ScenarioBehaviour().add(this.stopScenario);

        // Start, stop and entry level system calls
        this.startScenario.setEntityName("startScenario");
        this.startScenario.setScenarioBehaviour_AbstractUserAction(this.buyBookScenarioBehaviour);
        this.startScenario.setSuccessor(this.getQueryCall);

        this.getQueryCall.setEntityName("getQueryCall");
        this.getQueryCall.setScenarioBehaviour_AbstractUserAction(this.buyBookScenarioBehaviour);
        this.getQueryCall.setOperationSignature__EntryLevelSystemCall(this.getQuerySignature);
        this.getQueryCall.setProvidedRole_EntryLevelSystemCall(this.providedInputOperation);
        this.getQueryCall.setPredecessor(this.startScenario);
        this.getQueryCall.setSuccessor(this.getPriceCall);

        this.getPriceCall.setEntityName("getPriceCall");
        this.getPriceCall.setScenarioBehaviour_AbstractUserAction(this.buyBookScenarioBehaviour);
        this.getPriceCall.setOperationSignature__EntryLevelSystemCall(this.getPriceSignature);
        this.getPriceCall.setProvidedRole_EntryLevelSystemCall(this.providedSearchOperation);
        this.getPriceCall.setPredecessor(this.getQueryCall);
        this.getPriceCall.setSuccessor(this.withdrawCall);

        this.withdrawCall.setEntityName("withdrawCall");
        this.withdrawCall.setScenarioBehaviour_AbstractUserAction(this.buyBookScenarioBehaviour);
        this.withdrawCall.setOperationSignature__EntryLevelSystemCall(this.withdrawSignature);
        this.withdrawCall.setProvidedRole_EntryLevelSystemCall(this.providedPayOperation);
        this.withdrawCall.setPredecessor(this.getPriceCall);
        this.withdrawCall.setSuccessor(this.stopScenario);

        this.stopScenario.setEntityName("stopScenario");
        this.stopScenario.setScenarioBehaviour_AbstractUserAction(this.buyBookScenarioBehaviour);
        this.stopScenario.setPredecessor(this.withdrawCall);

        // Closed workload
        this.closedWorkload.setPopulation(2);
        this.closedWorkload.setThinkTime_ClosedWorkload(this.thinkTime);
        this.closedWorkload.setUsageScenario_Workload(this.usageScenarioGroup0);

        // Think time
        this.thinkTime.setSpecification("5.0");

    }

    public Repository getRepository() {
        return this.repository;
    }

    public System getSystem() {
        return this.system;
    }

    public ResourceEnvironment getResourceEnvironment() {
        return this.resourceEnvironment;
    }

    public Allocation getAllocation() {
        return this.allocation;
    }

    public UsageModel getUsageModel() {
        return this.usageModel;
    }

    public BasicComponent getQueryInputComponent() {
        return this.queryInputComponent;
    }

    public BasicComponent getQueryProcessingComponent() {
        return this.queryProcessingComponent;
    }

    public BasicComponent getCatalogSearchComponent() {
        return this.catalogSearchComponent;
    }

    public BasicComponent getPaymentComponent() {
        return this.paymentComponent;
    }

    public CompositeComponent getOrderComponent() {
        return this.orderComponent;
    }

    public OperationProvidedRole getProvidedInputOperation() {
        return this.providedInputOperation;
    }

    public OperationProvidedRole getProvidedSearchOperation() {
        return this.providedSearchOperation;
    }

    public OperationProvidedRole getProvidedPayOperation() {
        return this.providedPayOperation;
    }

    public OperationRequiredRole getRequiredInputOperation() {
        return this.requiredInputOperation;
    }

    public OperationRequiredRole getRequiredSearchOperation() {
        return this.requiredSearchOperation;
    }

    public OperationRequiredRole getRequiredPayOperation() {
        return this.requiredPayOperation;
    }

    public OperationInterface getQueryInputInterface() {
        return this.queryInputInterface;
    }

    public OperationInterface getSearchInterface() {
        return this.searchInterface;
    }

    public OperationInterface getPayInterface() {
        return this.payInterface;
    }

    public OperationSignature getGetQuerySignature() {
        return this.getQuerySignature;
    }

    public OperationSignature getGetPriceSignature() {
        return this.getPriceSignature;
    }

    public OperationSignature getWithdrawSignature() {
        return this.withdrawSignature;
    }

    public PrimitiveDataType getIntDataType() {
        return this.intDataType;
    }

    public PrimitiveDataType getStringDataType() {
        return this.stringDataType;
    }

    public AssemblyContext getQueryInputAssemblyContext() {
        return this.queryInputAssemblyContext;
    }

    public AssemblyContext getBusinessOrderAssemblyContext() {
        return this.businessOrderAssemblyContext;
    }

    public AssemblyContext getPrivateOrderAssemblyContext() {
        return this.privateOrderAssemblyContext;
    }

    public AssemblyContext getPaymentAssemblyContext() {
        return this.paymentAssemblyContext;
    }

    public AssemblyConnector getBusinessQueryInputConnector() {
        return this.businessQueryInputConnector;
    }

    public AssemblyConnector getPrivateQueryInputConnector() {
        return this.privateQueryInputConnector;
    }

    public AssemblyConnector getBusinessPayConnector() {
        return this.businessPayConnector;
    }

    public AssemblyConnector getPrivatePayConnector() {
        return this.privatePayConnector;
    }

    public ResourceContainer getClient1() {
        return this.client1;
    }

    public ResourceContainer getClient2() {
        return this.client2;
    }

    public ResourceContainer getOrderServer() {
        return this.orderServer;
    }

    public ResourceContainer getPaymentServer() {
        return this.paymentServer;
    }

    public ProcessingResourceSpecification getClient1Specification() {
        return this.client1Specification;
    }

    public ProcessingResourceSpecification getClient2Specification() {
        return this.client2Specification;
    }

    public ProcessingResourceSpecification getOrderServerSpecification() {
        return this.orderServerSpecification;
    }

    public ProcessingResourceSpecification getPaymentServerSpecification() {
        return this.paymentServerSpecification;
    }

    public ProcessingResourceType getClient1Type() {
        return this.client1Type;
    }

    public ProcessingResourceType getClient2Type() {
        return this.client2Type;
    }

    public ProcessingResourceType getOrderServerType() {
        return this.orderServerType;
    }

    public ProcessingResourceType getPaymentServerType() {
        return this.paymentServerType;
    }

    public LinkingResource getLan1() {
        return this.lan1;
    }

    public CommunicationLinkResourceSpecification getLan1Specification() {
        return this.lan1Specification;
    }

    public CommunicationLinkResourceType getLan1Type() {
        return this.lan1Type;
    }

    public AllocationContext getClient1AllocationContext() {
        return this.client1AllocationContext;
    }

    public AllocationContext getClient2AllocationContext() {
        return this.client2AllocationContext;
    }

    public AllocationContext getBusinessOrderServerAllocationContext() {
        return this.businessOrderServerAllocationContext;
    }

    public AllocationContext getPrivateOrderServerAllocationContext() {
        return this.privateOrderServerAllocationContext;
    }

    public AllocationContext getPaymentServerAllocationContext() {
        return this.paymentServerAllocationContext;
    }

    public UsageScenario getUsageScenarioGroup0() {
        return this.usageScenarioGroup0;
    }

    public ScenarioBehaviour getBuyBookScenarioBehaviour() {
        return this.buyBookScenarioBehaviour;
    }

    public Start getStartScenario() {
        return this.startScenario;
    }

    public EntryLevelSystemCall getGetQueryCall() {
        return this.getQueryCall;
    }

    public EntryLevelSystemCall getGetPriceCall() {
        return this.getPriceCall;
    }

    public EntryLevelSystemCall getWithdrawCall() {
        return this.withdrawCall;
    }

    public Stop getStopScenario() {
        return this.stopScenario;
    }

    public ClosedWorkload getClosedWorkload() {
        return this.closedWorkload;
    }

    public PCMRandomVariable getThinkTime() {
        return this.thinkTime;
    }

}
