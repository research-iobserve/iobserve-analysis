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
package org.iobserve.adaptation.droolsstages;

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
 * <li>a repository model containing components comp_a,comp_b1 and comp_b2 (with b1 and b2
 * equivalent)
 * <li>a system model containing assembly contexts acxt_a and acxt_b as well as an assembly
 * connector between a and b aconn_ab
 * <li>a resource environment model containing resource containers rc_1 and rc_2 and a linking
 * resource lr_rc1rc2
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
    private final BasicComponent comp_a = RepositoryFactory.eINSTANCE.createBasicComponent();
    private final BasicComponent comp_b1 = RepositoryFactory.eINSTANCE.createBasicComponent();
    private final BasicComponent comp_b2 = RepositoryFactory.eINSTANCE.createBasicComponent();
    private final OperationProvidedRole providedRole = RepositoryFactory.eINSTANCE.createOperationProvidedRole();
    private final OperationRequiredRole requiredRole = RepositoryFactory.eINSTANCE.createOperationRequiredRole();

    // System components
    private final System system = SystemFactory.eINSTANCE.createSystem();
    private final AssemblyContext acxt_a = CompositionFactory.eINSTANCE.createAssemblyContext();
    private final AssemblyContext acxt_b = CompositionFactory.eINSTANCE.createAssemblyContext();
    private final AssemblyConnector aconn_ab = CompositionFactory.eINSTANCE.createAssemblyConnector();

    // Resource environment components
    private final ResourceEnvironment resEnvironment = ResourceenvironmentFactory.eINSTANCE.createResourceEnvironment();
    private final ResourceContainer rc_1 = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
    private final ResourceContainer rc_2 = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
    private final ResourceContainer rc_3 = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
    private final LinkingResource lr_rc1rc2 = ResourceenvironmentFactory.eINSTANCE.createLinkingResource();
    private final LinkingResource lr_rc2rc3 = ResourceenvironmentFactory.eINSTANCE.createLinkingResource();

    // Allocation components
    private final Allocation allocation = AllocationFactory.eINSTANCE.createAllocation();
    private final AllocationContext alcxt_arc1 = AllocationFactory.eINSTANCE.createAllocationContext();
    private final AllocationContext alcxt_brc1 = AllocationFactory.eINSTANCE.createAllocationContext();
    private final AllocationContext alcxt_brc2 = AllocationFactory.eINSTANCE.createAllocationContext();

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
        this.repository.getComponents__Repository().add(this.comp_a);
        this.repository.getComponents__Repository().add(this.comp_b1);
        this.repository.getComponents__Repository().add(this.comp_b2);

        // Components
        this.comp_a.setEntityName("comp_a");
        this.comp_a.getRequiredRoles_InterfaceRequiringEntity().add(this.requiredRole);
        this.comp_a.setRepository__RepositoryComponent(this.repository);
        this.comp_b1.setEntityName("comp_b1");
        this.comp_b1.getProvidedRoles_InterfaceProvidingEntity().add(this.providedRole);
        this.comp_b1.setRepository__RepositoryComponent(this.repository);
        this.comp_b2.setEntityName("comp_b2");
        this.comp_b2.getProvidedRoles_InterfaceProvidingEntity().add(this.providedRole);
        this.comp_b2.setRepository__RepositoryComponent(this.repository);

        // Roles
        this.providedRole.setEntityName("providedRole");
        this.requiredRole.setEntityName("requiredRole");
    }

    private void createSystem() {
        // System
        this.system.setEntityName("system");
        this.system.getAssemblyContexts__ComposedStructure().add(this.acxt_a);
        this.system.getAssemblyContexts__ComposedStructure().add(this.acxt_b);
        this.system.getConnectors__ComposedStructure().add(this.aconn_ab);

        // Assembly contexts
        this.acxt_a.setEntityName("acxt_a");
        this.acxt_a.setEncapsulatedComponent__AssemblyContext(this.comp_a);
        this.acxt_b.setEntityName("acxt_b");
        this.acxt_b.setEncapsulatedComponent__AssemblyContext(this.comp_b1);

        // Assembly connectors
        this.aconn_ab.setEntityName("aconn_ab");
        this.aconn_ab.setProvidedRole_AssemblyConnector(this.providedRole);
        this.aconn_ab.setRequiredRole_AssemblyConnector(this.requiredRole);
        this.aconn_ab.setProvidingAssemblyContext_AssemblyConnector(this.acxt_b);
        this.aconn_ab.setRequiringAssemblyContext_AssemblyConnector(this.acxt_a);
    }

    private void createResourceEnvironment() {
        // Resource environment
        this.resEnvironment.setEntityName("resEnvironment");
        this.resEnvironment.getResourceContainer_ResourceEnvironment().add(this.rc_1);
        this.resEnvironment.getResourceContainer_ResourceEnvironment().add(this.rc_2);
        this.resEnvironment.getLinkingResources__ResourceEnvironment().add(this.lr_rc1rc2);
        // We don't add rc_3 and lr_rc2rc3yet!

        // Resource container
        this.rc_1.setEntityName("rc_1");
        this.rc_1.setResourceEnvironment_ResourceContainer(this.resEnvironment);

        this.rc_2.setEntityName("rc_2");
        this.rc_2.setResourceEnvironment_ResourceContainer(this.resEnvironment);

        this.rc_3.setEntityName("rc_3");
        this.rc_3.setResourceEnvironment_ResourceContainer(this.resEnvironment);

        // Linking resource
        this.lr_rc1rc2.setEntityName("lr_rc1rc2");
        this.lr_rc1rc2.setResourceEnvironment_LinkingResource(this.resEnvironment);
        this.lr_rc1rc2.getConnectedResourceContainers_LinkingResource().add(this.rc_1);
        this.lr_rc1rc2.getConnectedResourceContainers_LinkingResource().add(this.rc_2);

        this.lr_rc2rc3.setEntityName("lr_rc2rc3");
        this.lr_rc2rc3.setResourceEnvironment_LinkingResource(this.resEnvironment);
        this.lr_rc2rc3.getConnectedResourceContainers_LinkingResource().add(this.rc_2);
        this.lr_rc2rc3.getConnectedResourceContainers_LinkingResource().add(this.rc_3);
    }

    private void createAllocation() {
        // Allocation
        this.allocation.setEntityName("allocation");
        this.allocation.setSystem_Allocation(this.system);
        this.allocation.setTargetResourceEnvironment_Allocation(this.resEnvironment);
        this.allocation.getAllocationContexts_Allocation().add(this.alcxt_arc1);
        this.allocation.getAllocationContexts_Allocation().add(this.alcxt_brc1);
        // We don't add alcxt_brc2 yet!

        // Allocation contexts
        this.alcxt_arc1.setEntityName("alcxt_rc1");
        this.alcxt_arc1.setAssemblyContext_AllocationContext(this.acxt_a);
        this.alcxt_arc1.setResourceContainer_AllocationContext(this.rc_1);
        this.alcxt_arc1.setAllocation_AllocationContext(this.allocation);

        this.alcxt_brc1.setEntityName("alcxt_brc1");
        this.alcxt_brc1.setAssemblyContext_AllocationContext(this.acxt_b);
        this.alcxt_brc1.setResourceContainer_AllocationContext(this.rc_1);
        this.alcxt_brc1.setAllocation_AllocationContext(this.allocation);

        this.alcxt_brc2.setEntityName("alcxt_brc2");
        this.alcxt_brc2.setAssemblyContext_AllocationContext(this.acxt_b);
        this.alcxt_brc2.setResourceContainer_AllocationContext(this.rc_2);
        this.alcxt_brc2.setAllocation_AllocationContext(this.allocation);
    }

    public Repository getRepository() {
        return this.repository;
    }

    public System getSystem() {
        return this.system;
    }

    public ResourceEnvironment getResourceEnvironment() {
        return this.resEnvironment;
    }

    public Allocation getAllocation() {
        return this.allocation;
    }

    public void replicateCompBToRc2() {
        this.allocation.getAllocationContexts_Allocation().add(this.alcxt_brc2);
    }

    public void dereplicateCompBfromRc2() {
        this.allocation.getAllocationContexts_Allocation().remove(this.alcxt_brc2);
    }

    public void migrateCompBToRc2() {
        this.replicateCompBToRc2();
        this.allocation.getAllocationContexts_Allocation().remove(this.alcxt_brc1);
    }

    public void changeRepositoryCompB1ToCompB2() {
        this.acxt_b.setEncapsulatedComponent__AssemblyContext(this.comp_b2);
    }

    public void allocateResourceContainerR3() {
        this.resEnvironment.getResourceContainer_ResourceEnvironment().add(this.rc_3);
        this.resEnvironment.getLinkingResources__ResourceEnvironment().add(this.lr_rc2rc3);
    }

    public void deallocateResourceContainerR3() {
        this.resEnvironment.getResourceContainer_ResourceEnvironment().remove(this.rc_3);
        this.resEnvironment.getLinkingResources__ResourceEnvironment().remove(this.lr_rc2rc3);
    }
}
