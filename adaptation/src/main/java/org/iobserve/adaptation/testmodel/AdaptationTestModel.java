/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.adaptation.testmodel;

import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.allocation.AllocationFactory;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.CompositionFactory;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationRequiredRole;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryFactory;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.system.SystemFactory;

/**
 * A set of very basic PCM models containing:
 * <ul>
 * <li>a repository model containing components compA,comp_B and comp_b2 (with B and b2 equivalent)
 * <li>a system model containing assembly contexts acxtA and acxt_b as well as an assembly connector
 * between a and b aconn_ab
 * <li>a resource environment model containing resource containers rc1 and rc2 and a linking
 * resource lrRc1Rc2
 * </ul>
 * Additionally there are methods to replicate, migrate, change repository components and allocate
 * resource containers.
 *
 * @author Lars Bluemke
 *
 */
public class AdaptationTestModel {
    // Repository components
    private final Repository repository = RepositoryFactory.eINSTANCE.createRepository();
    private final BasicComponent compA = RepositoryFactory.eINSTANCE.createBasicComponent();
    private final BasicComponent compBx = RepositoryFactory.eINSTANCE.createBasicComponent();
    private final BasicComponent compBy = RepositoryFactory.eINSTANCE.createBasicComponent();
    private final OperationProvidedRole providedRole = RepositoryFactory.eINSTANCE.createOperationProvidedRole();
    private final OperationRequiredRole requiredRole = RepositoryFactory.eINSTANCE.createOperationRequiredRole();

    // System components
    private final System system = SystemFactory.eINSTANCE.createSystem();
    private final AssemblyContext acxtA = CompositionFactory.eINSTANCE.createAssemblyContext();
    private final AssemblyContext acxtB = CompositionFactory.eINSTANCE.createAssemblyContext();
    private final AssemblyConnector aconnAB = CompositionFactory.eINSTANCE.createAssemblyConnector();

    // Resource environment components
    private final ResourceEnvironment resEnvironment = ResourceenvironmentFactory.eINSTANCE.createResourceEnvironment();
    private final ResourceContainer rc1 = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
    private final ResourceContainer rc2 = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
    private final ResourceContainer rc3 = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
    private final LinkingResource lrRc1Rc2 = ResourceenvironmentFactory.eINSTANCE.createLinkingResource();
    private final LinkingResource lrRc2Rc3 = ResourceenvironmentFactory.eINSTANCE.createLinkingResource();

    // Allocation components
    private final Allocation allocation = AllocationFactory.eINSTANCE.createAllocation();
    private final AllocationContext alcxtArc1 = AllocationFactory.eINSTANCE.createAllocationContext();
    private final AllocationContext alcxtBRc1 = AllocationFactory.eINSTANCE.createAllocationContext();
    private final AllocationContext alcxtBRc2 = AllocationFactory.eINSTANCE.createAllocationContext();

    /**
     * Creates a new TestModelBuilder and initializes the test models.
     */
    public AdaptationTestModel() {
        this.createReposiory();
        this.createSystem();
        this.createResourceEnvironment();
        this.createAllocation();
    }

    private void createReposiory() {
        // Repository
        this.repository.setEntityName("repository");
        this.repository.getComponents__Repository().add(this.compA);
        this.repository.getComponents__Repository().add(this.compBx);
        this.repository.getComponents__Repository().add(this.compBy);

        // Components
        this.compA.setEntityName("compA");
        this.compA.getRequiredRoles_InterfaceRequiringEntity().add(this.requiredRole);
        this.compA.setRepository__RepositoryComponent(this.repository);
        this.compBx.setEntityName("compBx");
        this.compBx.getProvidedRoles_InterfaceProvidingEntity().add(this.providedRole);
        this.compBx.setRepository__RepositoryComponent(this.repository);
        this.compBy.setEntityName("compBy");
        this.compBy.getProvidedRoles_InterfaceProvidingEntity().add(this.providedRole);
        this.compBy.setRepository__RepositoryComponent(this.repository);

        // Roles
        this.providedRole.setEntityName("providedRole");
        this.requiredRole.setEntityName("requiredRole");
    }

    private void createSystem() {
        // System
        this.system.setEntityName("system");
        this.system.getAssemblyContexts__ComposedStructure().add(this.acxtA);
        this.system.getAssemblyContexts__ComposedStructure().add(this.acxtB);
        this.system.getConnectors__ComposedStructure().add(this.aconnAB);

        // Assembly contexts
        this.acxtA.setEntityName("acxtA");
        this.acxtA.setEncapsulatedComponent__AssemblyContext(this.compA);

        this.acxtB.setEntityName("acxtB");
        this.acxtB.setEncapsulatedComponent__AssemblyContext(this.compBx);

        // Assembly connectors
        this.aconnAB.setEntityName("aconnAB");
        this.aconnAB.setProvidedRole_AssemblyConnector(this.providedRole);
        this.aconnAB.setRequiredRole_AssemblyConnector(this.requiredRole);
        this.aconnAB.setProvidingAssemblyContext_AssemblyConnector(this.acxtB);
        this.aconnAB.setRequiringAssemblyContext_AssemblyConnector(this.acxtA);
    }

    private void createResourceEnvironment() {
        // Resource environment
        this.resEnvironment.setEntityName("resEnvironment");
        this.resEnvironment.getResourceContainer_ResourceEnvironment().add(this.rc1);
        this.resEnvironment.getResourceContainer_ResourceEnvironment().add(this.rc2);
        this.resEnvironment.getLinkingResources__ResourceEnvironment().add(this.lrRc1Rc2);
        // We don't add rc3 and lrRc2Rc3 yet!

        // Resource container
        this.rc1.setEntityName("rc1");
        this.rc1.setResourceEnvironment_ResourceContainer(this.resEnvironment);

        this.rc2.setEntityName("rc2");
        this.rc2.setResourceEnvironment_ResourceContainer(this.resEnvironment);

        this.rc3.setEntityName("rc3");

        // Linking resource
        this.lrRc1Rc2.setEntityName("lrRc1Rc2");
        this.lrRc1Rc2.setResourceEnvironment_LinkingResource(this.resEnvironment);
        this.lrRc1Rc2.getConnectedResourceContainers_LinkingResource().add(this.rc1);
        this.lrRc1Rc2.getConnectedResourceContainers_LinkingResource().add(this.rc2);

        this.lrRc2Rc3.setEntityName("lrRc2Rc3");
        this.lrRc2Rc3.getConnectedResourceContainers_LinkingResource().add(this.rc2);
        this.lrRc2Rc3.getConnectedResourceContainers_LinkingResource().add(this.rc3);
    }

    private void createAllocation() {
        // Allocation
        this.allocation.setEntityName("allocation");
        this.allocation.setSystem_Allocation(this.system);
        this.allocation.setTargetResourceEnvironment_Allocation(this.resEnvironment);
        this.allocation.getAllocationContexts_Allocation().add(this.alcxtArc1);
        this.allocation.getAllocationContexts_Allocation().add(this.alcxtBRc1);
        // We don't add alcxtBRc2 yet!

        // Allocation contexts
        this.alcxtArc1.setEntityName("alcxtArc1");
        this.alcxtArc1.setAssemblyContext_AllocationContext(this.acxtA);
        this.alcxtArc1.setResourceContainer_AllocationContext(this.rc1);
        this.alcxtArc1.setAllocation_AllocationContext(this.allocation);

        this.alcxtBRc1.setEntityName("alcxt_Brc1");
        this.alcxtBRc1.setAssemblyContext_AllocationContext(this.acxtB);
        this.alcxtBRc1.setResourceContainer_AllocationContext(this.rc1);
        this.alcxtBRc1.setAllocation_AllocationContext(this.allocation);

        this.alcxtBRc2.setEntityName("alcxt_Brc2");
        this.alcxtBRc2.setAssemblyContext_AllocationContext(this.acxtB);
        this.alcxtBRc2.setResourceContainer_AllocationContext(this.rc2);
    }

    public Repository getRepository() {
        return this.repository;
    }

    public System getSystem() {
        return this.system;
    }

    public ResourceEnvironment getResEnvironment() {
        return this.resEnvironment;
    }

    public Allocation getAllocation() {
        return this.allocation;
    }

    public BasicComponent getCompA() {
        return this.compA;
    }

    public BasicComponent getCompBx() {
        return this.compBx;
    }

    public BasicComponent getCompBy() {
        return this.compBy;
    }

    public OperationProvidedRole getProvidedRole() {
        return this.providedRole;
    }

    public OperationRequiredRole getRequiredRole() {
        return this.requiredRole;
    }

    public AssemblyContext getAcxtA() {
        return this.acxtA;
    }

    public AssemblyContext getAcxtB() {
        return this.acxtB;
    }

    public AssemblyConnector getAconnAB() {
        return this.aconnAB;
    }

    public ResourceContainer getRc1() {
        return this.rc1;
    }

    public ResourceContainer getRc2() {
        return this.rc2;
    }

    public ResourceContainer getRc3() {
        return this.rc3;
    }

    public LinkingResource getLrRc1Rc2() {
        return this.lrRc1Rc2;
    }

    public LinkingResource getLrRc2Rc3() {
        return this.lrRc2Rc3;
    }

    public AllocationContext getAlcxtArc1() {
        return this.alcxtArc1;
    }

    public AllocationContext getAlcxtBRc1() {
        return this.alcxtBRc1;
    }

    public AllocationContext getAlcxtBRc2() {
        return this.alcxtBRc2;
    }

    /**
     * Replicate component instance B to RC2 (creating B2 instance there).
     */
    public void replicateCompBToRc2() {
        this.allocation.getAllocationContexts_Allocation().add(this.alcxtBRc2);
        this.alcxtBRc2.setAllocation_AllocationContext(this.allocation);
    }

    /**
     * Dereplicate component instance B2 from RC2.
     */
    public void dereplicateCompB2fromRc2() {
        this.allocation.getAllocationContexts_Allocation().remove(this.alcxtBRc2);
    }

    /**
     * Migrate allocation context B from RC1 to RC2.
     */
    public void migrateCompBToRc2() {
        this.alcxtBRc1.setEntityName("alcxt_b1rc2");
        this.alcxtBRc1.setResourceContainer_AllocationContext(this.rc2);
    }

    /**
     * Replace component instance of Bx with equivalent of By.
     */
    public void changeRepositoryCompBxToCompBy() {
        this.acxtB.setEncapsulatedComponent__AssemblyContext(this.compBy);
    }

    /**
     * Allocate resource container RC3.
     */
    public void allocateResourceContainerR3() {
        this.resEnvironment.getResourceContainer_ResourceEnvironment().add(this.rc3);
        this.rc3.setResourceEnvironment_ResourceContainer(this.resEnvironment);
        this.resEnvironment.getLinkingResources__ResourceEnvironment().add(this.lrRc2Rc3);
        this.lrRc2Rc3.setResourceEnvironment_LinkingResource(this.resEnvironment);
    }

    /**
     * Deallocate resource container RC3.
     */
    public void deallocateResourceContainerR3() {
        this.resEnvironment.getResourceContainer_ResourceEnvironment().remove(this.rc3);
        this.resEnvironment.getLinkingResources__ResourceEnvironment().remove(this.lrRc2Rc3);
    }

    /**
     * Create a deep copy of the test models where all components still have the same id. (Using
     * clone() still referenced the same components which was a problem during testing.)
     *
     * @return a copy of the model
     */
    public Object getCopyWithSameIds() {
        final AdaptationTestModel modelCopy = new AdaptationTestModel();

        // Repository components
        modelCopy.getRepository().setId(this.repository.getId());
        modelCopy.getCompA().setId(this.compA.getId());
        modelCopy.getCompBx().setId(this.compBx.getId());
        modelCopy.getCompBy().setId(this.compBy.getId());
        modelCopy.getProvidedRole().setId(this.providedRole.getId());
        modelCopy.getRequiredRole().setId(this.requiredRole.getId());

        // System components
        modelCopy.getSystem().setId(this.system.getId());
        modelCopy.getAcxtA().setId(this.acxtA.getId());
        modelCopy.getAcxtB().setId(this.acxtB.getId());
        modelCopy.getAconnAB().setId(this.aconnAB.getId());

        // Resource environment components
        modelCopy.getRc1().setId(this.rc1.getId());
        modelCopy.getRc2().setId(this.rc2.getId());
        modelCopy.getRc3().setId(this.rc3.getId());
        modelCopy.getLrRc1Rc2().setId(this.lrRc1Rc2.getId());
        modelCopy.getLrRc2Rc3().setId(this.lrRc2Rc3.getId());

        // Allocation components
        modelCopy.getAllocation().setId(this.allocation.getId());
        modelCopy.getAlcxtArc1().setId(this.alcxtArc1.getId());
        modelCopy.getAlcxtBRc1().setId(this.alcxtBRc1.getId());

        return modelCopy;
    }
}
