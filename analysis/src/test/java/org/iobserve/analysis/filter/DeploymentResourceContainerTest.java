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
package org.iobserve.analysis.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import teetime.framework.test.StageTester;

import org.hamcrest.core.Is;
import org.iobserve.analysis.deployment.DeploymentModelUpdater;
import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.analysis.model.builder.AllocationModelBuilder;
import org.iobserve.analysis.model.builder.ResourceEnvironmentModelBuilder;
import org.iobserve.analysis.model.builder.SystemModelBuilder;
import org.iobserve.analysis.model.correspondence.Correspondent;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.analysis.test.data.CorrespondenceModelData;
import org.iobserve.analysis.test.data.ImplementationLevelData;
import org.iobserve.analysis.test.data.ModelLevelData;
import org.iobserve.common.record.ServletDeployedEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationFactory;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.CompositionFactory;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;
import org.palladiosimulator.pcm.system.SystemFactory;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Tests for {@link DeploymentModelUpdater} filter, in case the {@link ResourceContainer} exists
 * already.
 *
 * @author Josefine Wegert
 * @author Reiner Jung
 *
 */
@RunWith(PowerMockRunner.class)
// write all final classes here
@PrepareForTest({ ResourceEnvironmentModelBuilder.class, AllocationModelBuilder.class, SystemModelBuilder.class })
public class DeploymentResourceContainerTest {

    /** stage under test. */
    private DeploymentModelUpdater tDeployment;

    /** mocks. */
    @Mock
    private static ModelProvider<org.palladiosimulator.pcm.system.System> mockedSystemModelGraphProvider;
    @Mock
    private static ModelProvider<ResourceEnvironment> mockedResourceEnvironmentModelGraphProvider;
    @Mock
    private static ModelProvider<Allocation> mockedAllocationModelGraphProvider;
    @Mock
    private static ICorrespondence mockedCorrespondence;

    /** test events. */
    private static PCMDeployedEvent deploymentEvent = ModelLevelData.createPCMDeployedEvent();

    /** input events. */
    private static List<PCMDeployedEvent> inputEvents = new ArrayList<>();

    /** test correspondent. */
    // private static Correspondent testCorrespondent;
    private static Optional<Correspondent> optTestCorrespondent;

    /** test allocation. */
    private static Allocation testAllocation;

    /** test resource environment. */
    private static ResourceEnvironment testResourceEnvironment;

    /** test system. */
    private static org.palladiosimulator.pcm.system.System testSystem;

    /** test resource container. */
    private static ResourceContainer testResourceContainer;
    private static Optional<ResourceContainer> optTestResourceContainer;

    /** test assembly context. */
    private static AssemblyContext testAssemblyContext;
    private static Optional<AssemblyContext> optTestAssemblyContext;

    /**
     * Initialize test events and mock necessary classes.
     */
    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void setup() {

        /** mocks for model graph provider */
        DeploymentResourceContainerTest.mockedSystemModelGraphProvider = Mockito.mock(ModelProvider.class);
        DeploymentResourceContainerTest.mockedResourceEnvironmentModelGraphProvider = Mockito.mock(ModelProvider.class);
        DeploymentResourceContainerTest.mockedAllocationModelGraphProvider = Mockito.mock(ModelProvider.class);

        /** mock for correspondence model */
        DeploymentResourceContainerTest.mockedCorrespondence = Mockito.mock(ICorrespondence.class);

        /** input deployment event */
        DeploymentResourceContainerTest.inputEvents.add(DeploymentResourceContainerTest.deploymentEvent);

        /** test correspondent */
        DeploymentResourceContainerTest.optTestCorrespondent = Optional
                .of(CorrespondenceModelData.createCorrespondent());

        /** test allocation */
        DeploymentResourceContainerTest.testAllocation = AllocationFactory.eINSTANCE.createAllocation();

        /** test resourceEnvironment */
        DeploymentResourceContainerTest.testResourceEnvironment = ResourceenvironmentFactory.eINSTANCE
                .createResourceEnvironment();

        /** test system */
        DeploymentResourceContainerTest.testSystem = SystemFactory.eINSTANCE.createSystem();

        /** optional test resource container with value */
        DeploymentResourceContainerTest.testResourceContainer = ResourceenvironmentFactory.eINSTANCE
                .createResourceContainer();
        DeploymentResourceContainerTest.testResourceContainer.setEntityName("TestResourceContainer");
        DeploymentResourceContainerTest.testResourceContainer.setId("_resourcecontainer_test_id");
        DeploymentResourceContainerTest.optTestResourceContainer = Optional
                .of(DeploymentResourceContainerTest.testResourceContainer);

        /** optional test assembly context */
        DeploymentResourceContainerTest.testAssemblyContext = CompositionFactory.eINSTANCE.createAssemblyContext();
        DeploymentResourceContainerTest.testAssemblyContext.setEntityName("TestAssemblyContext");
        DeploymentResourceContainerTest.testAssemblyContext.setId("_assemblycontext_test_id");
        DeploymentResourceContainerTest.optTestAssemblyContext = Optional
                .of(DeploymentResourceContainerTest.testAssemblyContext);

    }

    /**
     * Define the test situation in which the needed {@link ResourceContainer} exists in the given
     * {@link ResourceEnvironment} model.
     */
    @Before
    public void stubMocksResourceContainer() {

        /** mock for ModelBuilder */
        // use PowerMockito for calling static methods of these final classes
        PowerMockito.mockStatic(ResourceEnvironmentModelBuilder.class);
        PowerMockito.mockStatic(AllocationModelBuilder.class);
        PowerMockito.mockStatic(SystemModelBuilder.class);

        this.tDeployment = new DeploymentModelUpdater(
                DeploymentResourceContainerTest.mockedAllocationModelGraphProvider,
                DeploymentResourceContainerTest.mockedSystemModelGraphProvider);

        /** get models */
        Mockito.when(
                DeploymentResourceContainerTest.mockedCorrespondence.getCorrespondent(ImplementationLevelData.CONTEXT))
                .thenReturn(DeploymentResourceContainerTest.optTestCorrespondent);

        Mockito.when(DeploymentResourceContainerTest.mockedAllocationModelGraphProvider
                .readOnlyRootComponent(Allocation.class)).thenReturn(DeploymentResourceContainerTest.testAllocation);

        Mockito.when(DeploymentResourceContainerTest.mockedResourceEnvironmentModelGraphProvider
                .readOnlyRootComponent(ResourceEnvironment.class))
                .thenReturn(DeploymentResourceContainerTest.testResourceEnvironment);

        Mockito.when(DeploymentResourceContainerTest.mockedSystemModelGraphProvider
                .readOnlyRootComponent(org.palladiosimulator.pcm.system.System.class))
                .thenReturn(DeploymentResourceContainerTest.testSystem);

        /** get part of models */
        final String asmCtxName = CorrespondenceModelData.PCM_ENTITY_NAME + "_" + ImplementationLevelData.SERVICE;

        Mockito.when(
                SystemModelBuilder.getAssemblyContextByName(DeploymentResourceContainerTest.testSystem, asmCtxName))
                .thenReturn(DeploymentResourceContainerTest.optTestAssemblyContext);

        Mockito.when(ResourceEnvironmentModelBuilder.getResourceContainerByName(
                DeploymentResourceContainerTest.testResourceEnvironment, ImplementationLevelData.SERVICE))
                .thenReturn(DeploymentResourceContainerTest.optTestResourceContainer);

        /** interaction with graphs */
        Mockito.when(SystemModelBuilder.createAssemblyContextsIfAbsent(DeploymentResourceContainerTest.testSystem,
                ImplementationLevelData.SERVICE)).thenReturn(DeploymentResourceContainerTest.testAssemblyContext);

        Mockito.doNothing().when(DeploymentResourceContainerTest.mockedSystemModelGraphProvider).updateComponent(
                org.palladiosimulator.pcm.system.System.class, DeploymentResourceContainerTest.testSystem);

        PowerMockito.doNothing().when(AllocationModelBuilder.class);
        if (AllocationModelBuilder.isAllocationPresent(DeploymentResourceContainerTest.testAllocation,
                DeploymentResourceContainerTest.testResourceContainer,
                DeploymentResourceContainerTest.testAssemblyContext)) {
            AllocationModelBuilder.addAllocationContext(DeploymentResourceContainerTest.testAllocation,
                    DeploymentResourceContainerTest.testResourceContainer,
                    DeploymentResourceContainerTest.testAssemblyContext);
        }

        Mockito.doNothing().when(DeploymentResourceContainerTest.mockedAllocationModelGraphProvider)
                .updateComponent(Allocation.class, DeploymentResourceContainerTest.testAllocation);
    }

    /**
     * Check whether a {@link ServletDeployedEvent} is triggered, when the needed
     * {@link ResourceContainer} exists. A {@link ServletDeployedEvent} is defined as input.
     */
    @Test
    public void checkNoAllocationNeeded() {
        final List<PCMDeployedEvent> deploymentEvents = new ArrayList<>();

        StageTester.test(this.tDeployment).and().send(DeploymentResourceContainerTest.inputEvents)
                .to(this.tDeployment.getInputPort()).and().receive(deploymentEvents)
                .from(this.tDeployment.getDeployedNotifyOutputPort()).start();

        Assert.assertThat(deploymentEvents.get(0), Is.is(DeploymentResourceContainerTest.deploymentEvent));
    }

}
