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
    private final AssemblyContext acxt_b11 = CompositionFactory.eINSTANCE.createAssemblyContext();
    private final AssemblyContext acxt_b12 = CompositionFactory.eINSTANCE.createAssemblyContext();
    private final AssemblyContext acxt_b2 = CompositionFactory.eINSTANCE.createAssemblyContext();

    private final AssemblyConnector aconn_ab11 = CompositionFactory.eINSTANCE.createAssemblyConnector();
    private final AssemblyConnector aconn_ab12 = CompositionFactory.eINSTANCE.createAssemblyConnector();
    private final AssemblyConnector aconn_ab2 = CompositionFactory.eINSTANCE.createAssemblyConnector();

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
    private final AllocationContext alcxt_b11rc1 = AllocationFactory.eINSTANCE.createAllocationContext();
    private final AllocationContext alcxt_b12rc2 = AllocationFactory.eINSTANCE.createAllocationContext();
    private final AllocationContext alcxt_b2rc1 = AllocationFactory.eINSTANCE.createAllocationContext();

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
        this.system.getAssemblyContexts__ComposedStructure().add(this.acxt_b11);
        this.system.getConnectors__ComposedStructure().add(this.aconn_ab11);
        // We don't add acxt_b12, acxt_b2 and aconn_ab12, aconn_ab2 yet!

        // Assembly contexts
        this.acxt_a.setEntityName("acxt_a");
        this.acxt_a.setEncapsulatedComponent__AssemblyContext(this.comp_a);

        this.acxt_b11.setEntityName("acxt_b11");
        this.acxt_b11.setEncapsulatedComponent__AssemblyContext(this.comp_b1);

        this.acxt_b12.setEntityName("acxt_b12");
        this.acxt_b12.setEncapsulatedComponent__AssemblyContext(this.comp_b1);

        this.acxt_b2.setEntityName("acxt_b2");
        this.acxt_b2.setEncapsulatedComponent__AssemblyContext(this.comp_b2);

        // Assembly connectors
        this.aconn_ab11.setEntityName("aconn_ab11");
        this.aconn_ab11.setProvidedRole_AssemblyConnector(this.providedRole);
        this.aconn_ab11.setRequiredRole_AssemblyConnector(this.requiredRole);
        this.aconn_ab11.setProvidingAssemblyContext_AssemblyConnector(this.acxt_b11);
        this.aconn_ab11.setRequiringAssemblyContext_AssemblyConnector(this.acxt_a);

        this.aconn_ab12.setEntityName("aconn_ab12");
        this.aconn_ab12.setProvidedRole_AssemblyConnector(this.providedRole);
        this.aconn_ab12.setRequiredRole_AssemblyConnector(this.requiredRole);
        this.aconn_ab12.setProvidingAssemblyContext_AssemblyConnector(this.acxt_b12);
        this.aconn_ab12.setRequiringAssemblyContext_AssemblyConnector(this.acxt_a);

        this.aconn_ab2.setEntityName("aconn_ab2");
        this.aconn_ab2.setProvidedRole_AssemblyConnector(this.providedRole);
        this.aconn_ab2.setRequiredRole_AssemblyConnector(this.requiredRole);
        this.aconn_ab2.setProvidingAssemblyContext_AssemblyConnector(this.acxt_b2);
        this.aconn_ab2.setRequiringAssemblyContext_AssemblyConnector(this.acxt_a);
    }

    private void createResourceEnvironment() {
        // Resource environment
        this.resEnvironment.setEntityName("resEnvironment");
        this.resEnvironment.getResourceContainer_ResourceEnvironment().add(this.rc_1);
        this.resEnvironment.getResourceContainer_ResourceEnvironment().add(this.rc_2);
        this.resEnvironment.getLinkingResources__ResourceEnvironment().add(this.lr_rc1rc2);
        // We don't add rc_3 and lr_rc2rc3 yet!

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
        this.allocation.getAllocationContexts_Allocation().add(this.alcxt_b11rc1);
        // We don't add alcxt_b12rc2 and alcxt_b2rc1 yet!

        // Allocation contexts
        this.alcxt_arc1.setEntityName("alcxt_arc1");
        this.alcxt_arc1.setAssemblyContext_AllocationContext(this.acxt_a);
        this.alcxt_arc1.setResourceContainer_AllocationContext(this.rc_1);
        this.alcxt_arc1.setAllocation_AllocationContext(this.allocation);

        this.alcxt_b11rc1.setEntityName("alcxt_b11rc1");
        this.alcxt_b11rc1.setAssemblyContext_AllocationContext(this.acxt_b11);
        this.alcxt_b11rc1.setResourceContainer_AllocationContext(this.rc_1);
        this.alcxt_b11rc1.setAllocation_AllocationContext(this.allocation);

        this.alcxt_b12rc2.setEntityName("alcxt_b12rc2");
        this.alcxt_b12rc2.setAssemblyContext_AllocationContext(this.acxt_b12);
        this.alcxt_b12rc2.setResourceContainer_AllocationContext(this.rc_2);
        this.alcxt_b12rc2.setAllocation_AllocationContext(this.allocation);

        this.alcxt_b2rc1.setEntityName("alcxt_b2rc1");
        this.alcxt_b2rc1.setAssemblyContext_AllocationContext(this.acxt_b2);
        this.alcxt_b2rc1.setResourceContainer_AllocationContext(this.rc_1);
        this.alcxt_b2rc1.setAllocation_AllocationContext(this.allocation);
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

    public BasicComponent getComp_a() {
        return this.comp_a;
    }

    public BasicComponent getComp_b1() {
        return this.comp_b1;
    }

    public BasicComponent getComp_b2() {
        return this.comp_b2;
    }

    public OperationProvidedRole getProvidedRole() {
        return this.providedRole;
    }

    public OperationRequiredRole getRequiredRole() {
        return this.requiredRole;
    }

    public AssemblyContext getAcxt_a() {
        return this.acxt_a;
    }

    public AssemblyContext getAcxt_b11() {
        return this.acxt_b11;
    }

    public AssemblyContext getAcxt_b12() {
        return this.acxt_b12;
    }

    public AssemblyContext getAcxt_b2() {
        return this.acxt_b2;
    }

    public AssemblyConnector getAconn_ab11() {
        return this.aconn_ab11;
    }

    public AssemblyConnector getAconn_ab12() {
        return this.aconn_ab12;
    }

    public AssemblyConnector getAconn_ab2() {
        return this.aconn_ab2;
    }

    public ResourceContainer getRc_1() {
        return this.rc_1;
    }

    public ResourceContainer getRc_2() {
        return this.rc_2;
    }

    public ResourceContainer getRc_3() {
        return this.rc_3;
    }

    public LinkingResource getLr_rc1rc2() {
        return this.lr_rc1rc2;
    }

    public LinkingResource getLr_rc2rc3() {
        return this.lr_rc2rc3;
    }

    public AllocationContext getAlcxt_arc1() {
        return this.alcxt_arc1;
    }

    public AllocationContext getAlcxt_b11rc1() {
        return this.alcxt_b11rc1;
    }

    public AllocationContext getAlcxt_b12rc2() {
        return this.alcxt_b12rc2;
    }

    public AllocationContext getAlcxt_b2rc1() {
        return this.alcxt_b2rc1;
    }

    /**
     * Replicate component instance b11 to rc2 (creating b12 instance there)
     */
    public void replicateCompB11ToRc2() {
        this.system.getAssemblyContexts__ComposedStructure().add(this.acxt_b12);
        this.system.getConnectors__ComposedStructure().add(this.aconn_ab12);
        this.allocation.getAllocationContexts_Allocation().add(this.alcxt_b12rc2);
    }

    /**
     * Dereplicate component instance b12 from rc2
     */
    public void dereplicateCompB12fromRc2() {
        this.system.getAssemblyContexts__ComposedStructure().remove(this.acxt_b12);
        this.system.getConnectors__ComposedStructure().remove(this.aconn_ab12);
        this.allocation.getAllocationContexts_Allocation().remove(this.alcxt_b12rc2);
    }

    /**
     * Migrate component instance b from rc1 (where it's called b11) to rc2 (where it's called b12)
     */
    public void migrateCompB1ToRc2() {
        this.replicateCompB11ToRc2();
        this.system.getAssemblyContexts__ComposedStructure().remove(this.acxt_b11);
        this.system.getConnectors__ComposedStructure().remove(this.aconn_ab11);
        this.allocation.getAllocationContexts_Allocation().remove(this.alcxt_b11rc1);
    }

    /**
     * Replace component instance of b1 with equivalent of b2
     */
    public void changeRepositoryCompB1ToCompB2() {
        this.system.getAssemblyContexts__ComposedStructure().remove(this.acxt_b11);
        this.system.getConnectors__ComposedStructure().remove(this.aconn_ab11);
        this.allocation.getAllocationContexts_Allocation().remove(this.alcxt_b11rc1);

        this.system.getAssemblyContexts__ComposedStructure().add(this.acxt_b2);
        this.system.getConnectors__ComposedStructure().add(this.aconn_ab2);
        this.allocation.getAllocationContexts_Allocation().add(this.alcxt_b2rc1);
    }

    /**
     * Allocate resource container rc3
     */
    public void allocateResourceContainerR3() {
        this.resEnvironment.getResourceContainer_ResourceEnvironment().add(this.rc_3);
        this.resEnvironment.getLinkingResources__ResourceEnvironment().add(this.lr_rc2rc3);
    }

    /**
     * Deallocate resource container rc3
     */
    public void deallocateResourceContainerR3() {
        this.resEnvironment.getResourceContainer_ResourceEnvironment().remove(this.rc_3);
        this.resEnvironment.getLinkingResources__ResourceEnvironment().remove(this.lr_rc2rc3);
    }

    /**
     * Create a deep copy of the test models where all components still have the same id. (Using
     * clone() still referenced the same components which was a problem during testing.)
     * 
     * @return
     */
    public Object getCopyWithSameIds() {
        final AdaptationTestModel modelCopy = new AdaptationTestModel();

        // Repository components
        modelCopy.getRepository().setId(this.repository.getId());
        modelCopy.getComp_a().setId(this.comp_a.getId());
        modelCopy.getComp_b1().setId(this.comp_b1.getId());
        modelCopy.getComp_b2().setId(this.comp_b2.getId());
        modelCopy.getProvidedRole().setId(this.providedRole.getId());
        modelCopy.getRequiredRole().setId(this.requiredRole.getId());

        // System components
        modelCopy.getSystem().setId(this.system.getId());
        modelCopy.getAcxt_a().setId(this.acxt_a.getId());
        modelCopy.getAcxt_b11().setId(this.acxt_b11.getId());
        modelCopy.getAcxt_b12().setId(this.acxt_b12.getId());
        modelCopy.getAcxt_b2().setId(this.acxt_b2.getId());
        modelCopy.getAconn_ab11().setId(this.aconn_ab11.getId());
        modelCopy.getAconn_ab12().setId(this.aconn_ab12.getId());
        modelCopy.getAconn_ab2().setId(this.aconn_ab2.getId());

        // Resource environment components
        modelCopy.getRc_1().setId(this.rc_1.getId());
        modelCopy.getRc_2().setId(this.rc_2.getId());
        modelCopy.getRc_3().setId(this.rc_3.getId());
        modelCopy.getLr_rc1rc2().setId(this.lr_rc1rc2.getId());
        modelCopy.getLr_rc2rc3().setId(this.lr_rc2rc3.getId());

        // Allocation components
        modelCopy.getAllocation().setId(this.allocation.getId());
        modelCopy.getAlcxt_arc1().setId(this.alcxt_arc1.getId());
        modelCopy.getAlcxt_b11rc1().setId(this.alcxt_b11rc1.getId());
        modelCopy.getAlcxt_b12rc2().setId(this.alcxt_b12rc2.getId());
        modelCopy.getAlcxt_b2rc1().setId(this.alcxt_b2rc1.getId());

        return modelCopy;
    }
}
