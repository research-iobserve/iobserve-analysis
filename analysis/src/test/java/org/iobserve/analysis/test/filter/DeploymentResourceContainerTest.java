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
package org.iobserve.analysis.test.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import teetime.framework.test.StageTester;

import org.iobserve.analysis.deployment.DeploymentModelUpdater;
import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.analysis.test.data.ModelLevelDataFactory;
import org.iobserve.common.record.ISOCountryCode;
import org.iobserve.model.correspondence.CorrespondenceModel;
import org.iobserve.model.factory.ResourceEnvironmentModelFactory;
import org.iobserve.model.factory.SystemModelFactory;
import org.iobserve.model.persistence.DBException;
import org.iobserve.model.persistence.neo4j.Neo4JModelResource;
import org.iobserve.model.persistence.neo4j.NodeLookupException;
import org.iobserve.model.test.data.AllocationDataFactory;
import org.iobserve.model.test.data.ImplementationLevelDataFactory;
import org.iobserve.model.test.data.RepositoryModelDataFactory;
import org.iobserve.model.test.data.ResourceEnvironmentDataFactory;
import org.iobserve.model.test.data.SystemDataFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.allocation.AllocationFactory;
import org.palladiosimulator.pcm.allocation.AllocationPackage;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;
import org.palladiosimulator.pcm.system.System;
import org.powermock.api.mockito.PowerMockito;

/**
 * Tests for {@link DeploymentModelUpdater} filter, in case the {@link ResourceContainer} exists
 * already.
 *
 * @author Josefine Wegert
 * @author Reiner Jung
 *
 */
// @RunWith(PowerMockRunner.class) // NOCS
// write all final classes here
// @PrepareForTest({ ResourceEnvironmentModelFactory.class, AllocationModelFactory.class,
// SystemModelFactory.class })
public class DeploymentResourceContainerTest {

    /** mocks. */
    @Mock
    private static Neo4JModelResource<ResourceEnvironment> mockedResourceEnvironmentModelGraphProvider;
    @Mock
    private static Neo4JModelResource<Allocation> mockedAllocationModelGraphProvider;
    @Mock
    private static Neo4JModelResource<CorrespondenceModel> mockedCorrespondence;

    private static ResourceEnvironment resourceEnvironment = ResourceEnvironmentDataFactory.createResourceEnvironment();
    private static Repository repository = RepositoryModelDataFactory.createBookstoreRepositoryModel();
    private static System system = SystemDataFactory.createSystem(DeploymentResourceContainerTest.repository);
    private static Allocation allocation = AllocationDataFactory.createAllocation(
            DeploymentResourceContainerTest.system, DeploymentResourceContainerTest.resourceEnvironment);

    /** stage under test. */
    private DeploymentModelUpdater deploymentModelUpdater;

    /**
     * Initialize test events and mock necessary classes.
     */
    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void setUp() {

        /** mocks for model graph provider */
        DeploymentResourceContainerTest.mockedResourceEnvironmentModelGraphProvider = Mockito.mock(Neo4JModelResource.class);
        DeploymentResourceContainerTest.mockedAllocationModelGraphProvider = Mockito.mock(Neo4JModelResource.class);

        /** mock for correspondence model */
        DeploymentResourceContainerTest.mockedCorrespondence = Mockito.mock(Neo4JModelResource.class);
    }

    /**
     * Define the test situation in which the needed {@link ResourceContainer} exists in the given
     * {@link ResourceEnvironment} model.
     *
     * @throws NodeLookupException
     * @throws DBException
     */
    @Before
    public void stubMocksResourceContainer() throws NodeLookupException, DBException {

        /** mock for ModelBuilder */
        // use PowerMockito for calling static methods of these final classes
        PowerMockito.mockStatic(ResourceEnvironmentModelFactory.class);
        PowerMockito.mockStatic(SystemModelFactory.class);

        this.deploymentModelUpdater = new DeploymentModelUpdater(DeploymentResourceContainerTest.mockedCorrespondence,
                DeploymentResourceContainerTest.mockedAllocationModelGraphProvider);

        /** get models */
        // this makes no sense anymore
        // Mockito.when(DeploymentResourceContainerTest.mockedCorrespondence
        // .getCorrespondent(ImplementationLevelDataFactory.CONTEXT))
        // .thenReturn(Optional.of(CorrespondenceModelDataFactory.CORRESPONDENT));

        Mockito.when(DeploymentResourceContainerTest.mockedAllocationModelGraphProvider
                .getModelRootNode(Allocation.class, AllocationPackage.Literals.ALLOCATION))
                .thenReturn(DeploymentResourceContainerTest.allocation);

        Mockito.when(DeploymentResourceContainerTest.mockedResourceEnvironmentModelGraphProvider
                .getModelRootNode(ResourceEnvironment.class, ResourceenvironmentPackage.Literals.RESOURCE_ENVIRONMENT))
                .thenReturn(DeploymentResourceContainerTest.resourceEnvironment);

        // TODO fix this
        // Mockito.when(DeploymentResourceContainerTest.allocationContextModelGraphProvider
        // .readOnlyRootComponent(AllocationContext.class)).thenReturn(AllocationDataFactory.ALLOCATION);

        /** get part of models */

        Mockito.when(SystemModelFactory.getAssemblyContextByName(DeploymentResourceContainerTest.system,
                DeploymentResourceContainerTest.system.getAssemblyContexts__ComposedStructure().get(0).getEntityName()))
                .thenReturn(Optional
                        .of(DeploymentResourceContainerTest.system.getAssemblyContexts__ComposedStructure().get(0)));

        Mockito.when(ResourceEnvironmentModelFactory.getResourceContainerByName(
                DeploymentResourceContainerTest.resourceEnvironment, ImplementationLevelDataFactory.SERVICE))
                .thenReturn(Optional.of(DeploymentResourceContainerTest.resourceEnvironment
                        .getResourceContainer_ResourceEnvironment().get(0)));

        /** interaction with graphs */
        Mockito.when(SystemModelFactory.createAssemblyContextsIfAbsent(DeploymentResourceContainerTest.system,
                ImplementationLevelDataFactory.SERVICE))
                .thenReturn(DeploymentResourceContainerTest.system.getAssemblyContexts__ComposedStructure().get(0));

        this.addAllocationContext(DeploymentResourceContainerTest.allocation,
                DeploymentResourceContainerTest.resourceEnvironment.getResourceContainer_ResourceEnvironment().get(0),
                DeploymentResourceContainerTest.system.getAssemblyContexts__ComposedStructure().get(0));

        Mockito.doNothing().when(DeploymentResourceContainerTest.mockedAllocationModelGraphProvider)
                .updatePartition(DeploymentResourceContainerTest.allocation);
    }

    /**
     * Add an {@link AllocationContext} for the given {@link ResourceContainer} and
     * {@link AssemblyContext} if they are absent to this model. No check for duplication is done!
     *
     * @param model
     *            allocation model
     * @param resContainer
     *            container
     * @param asmCtx
     *            assembly context
     */
    private void addAllocationContext(final Allocation model, final ResourceContainer resContainer,
            final AssemblyContext asmCtx) {
        final AllocationFactory factory = AllocationFactory.eINSTANCE;
        final AllocationContext allocationCtx = factory.createAllocationContext();
        allocationCtx.setEntityName(asmCtx.getEntityName());
        allocationCtx.setAssemblyContext_AllocationContext(asmCtx);
        allocationCtx.setResourceContainer_AllocationContext(resContainer);
        model.getAllocationContexts_Allocation().add(allocationCtx);
    }

    /**
     * Tests whether a new deployment is generated for an already existing entity.
     */
    // @Test
    public void checkNoDeploymentNeeded() {

        /** input deployment event */
        final PCMDeployedEvent deploymentEvent = ModelLevelDataFactory.createPCMDeployedEvent(
                ISOCountryCode.EVIL_EMPIRE,
                DeploymentResourceContainerTest.system.getAssemblyContexts__ComposedStructure().get(0));
        final List<PCMDeployedEvent> inputEvents = new ArrayList<>();
        inputEvents.add(deploymentEvent);

        final List<PCMDeployedEvent> outputEvents = new ArrayList<>();

        StageTester.test(this.deploymentModelUpdater).and().send(inputEvents)
                .to(this.deploymentModelUpdater.getInputPort()).start();

        Assert.assertThat(this.deploymentModelUpdater.getDeployedNotifyOutputPort(), StageTester.producesNothing());

        Assert.assertEquals("There should be no notification for errorous data.", outputEvents.size(), 0);
    }

}
