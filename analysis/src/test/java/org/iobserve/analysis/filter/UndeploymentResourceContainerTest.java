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
import org.iobserve.analysis.deployment.UndeploymentModelUpdater;
import org.iobserve.analysis.deployment.data.PCMUndeployedEvent;
import org.iobserve.analysis.model.builder.AllocationModelBuilder;
import org.iobserve.analysis.model.builder.ResourceEnvironmentModelBuilder;
import org.iobserve.analysis.model.builder.SystemModelBuilder;
import org.iobserve.analysis.model.correspondence.Correspondent;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.analysis.test.data.CorrespondenceModelData;
import org.iobserve.analysis.test.data.ImplementationLevelData;
import org.iobserve.analysis.test.data.ModelLevelData;
import org.iobserve.analysis.test.data.ResourceEnvironmentData;
import org.iobserve.common.record.ServletUndeployedEvent;
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
 * Tests for {@link UndeploymentModelUpdater} filter, in case the {@link ResourceContainer} exists
 * already.
 *
 * @author Josefine Wegert
 * @author Reiner Jung
 *
 */
@RunWith(PowerMockRunner.class)
// write all final classes here
@PrepareForTest({ ResourceEnvironmentModelBuilder.class, AllocationModelBuilder.class, SystemModelBuilder.class })
public class UndeploymentResourceContainerTest {

    /** stage under test. */
    private UndeploymentModelUpdater undeploymentUpdater;

    /** mocks. */
    @Mock
    private static ModelProvider<org.palladiosimulator.pcm.system.System> mockedSystemModelGraphProvider;
    @Mock
    private static ModelProvider<ResourceEnvironment> mockedResourceEnvironmentModelGraphProvider;
    @Mock
    private static ModelProvider<Allocation> mockedAllocationModelGraphProvider;
    @Mock
    private static ICorrespondence mockedCorrespondence;

    /** input events. */
    private static PCMUndeployedEvent undeployedEvent;
    private static List<PCMUndeployedEvent> inputEvents = new ArrayList<>();

    /** test correspondent. */
    private static Optional<Correspondent> optTestCorrespondent;

    /** test allocation. */
    private static Allocation testAllocation;

    /** test resource environment. */
    private static ResourceEnvironment testResourceEnvironment;

    /** test system. */
    private static org.palladiosimulator.pcm.system.System testSystem;

    /** test resource container. */
    private static Optional<ResourceContainer> optTestResourceContainer;

    /** test assembly context. */
    private static AssemblyContext testAssemblyContext;
    private static Optional<AssemblyContext> optTestAssemblyContext;

    /**
     * Initialize test events and mock necessary classes.
     */
    @BeforeClass
    public static void setup() {

        /** input deployment event */
        UndeploymentResourceContainerTest.undeployedEvent = ModelLevelData.createPCMUndeployedEvent();
        UndeploymentResourceContainerTest.inputEvents.add(UndeploymentResourceContainerTest.undeployedEvent);

        /** test correspondent */
        UndeploymentResourceContainerTest.optTestCorrespondent = Optional
                .of(CorrespondenceModelData.createCorrespondent());

        /** test allocation */
        UndeploymentResourceContainerTest.testAllocation = AllocationFactory.eINSTANCE.createAllocation();

        /** test resourceEnvironment */
        UndeploymentResourceContainerTest.testResourceEnvironment = ResourceenvironmentFactory.eINSTANCE
                .createResourceEnvironment();

        /** test system */
        UndeploymentResourceContainerTest.testSystem = SystemFactory.eINSTANCE.createSystem();

        UndeploymentResourceContainerTest.optTestResourceContainer = ResourceEnvironmentData.createResourceContainer();

        /** optional test assembly context */
        UndeploymentResourceContainerTest.testAssemblyContext = CompositionFactory.eINSTANCE.createAssemblyContext();
        UndeploymentResourceContainerTest.testAssemblyContext.setEntityName("TestAssemblyContext");
        UndeploymentResourceContainerTest.testAssemblyContext.setId("_assemblycontext_test_id");
        UndeploymentResourceContainerTest.optTestAssemblyContext = Optional
                .of(UndeploymentResourceContainerTest.testAssemblyContext);

    }

    /**
     * Define the test situation in which the needed {@link ResourceContainer} and
     * {@link AssemblyContext} exist in the given {@link ResourceEnvironment} model.
     */
    @SuppressWarnings("unchecked")
    @Before
    public void stubMocksResourceContainer() {

        /** mock for ModelBuilder */
        // use PowerMockito for calling static methods of these final classes
        PowerMockito.mockStatic(ResourceEnvironmentModelBuilder.class);
        PowerMockito.mockStatic(AllocationModelBuilder.class);
        PowerMockito.mockStatic(SystemModelBuilder.class);

        /** mocks for model graph provider */
        UndeploymentResourceContainerTest.mockedSystemModelGraphProvider = Mockito.mock(ModelProvider.class);
        UndeploymentResourceContainerTest.mockedResourceEnvironmentModelGraphProvider = Mockito
                .mock(ModelProvider.class);
        UndeploymentResourceContainerTest.mockedAllocationModelGraphProvider = Mockito.mock(ModelProvider.class);

        this.undeploymentUpdater = new UndeploymentModelUpdater(
                UndeploymentResourceContainerTest.mockedAllocationModelGraphProvider,
                UndeploymentResourceContainerTest.mockedSystemModelGraphProvider);

        /** mock for correspondence model */
        UndeploymentResourceContainerTest.mockedCorrespondence = Mockito.mock(ICorrespondence.class);

        /** get models */
        Mockito.when(UndeploymentResourceContainerTest.mockedCorrespondence
                .getCorrespondent(ImplementationLevelData.CONTEXT))
                .thenReturn(UndeploymentResourceContainerTest.optTestCorrespondent);

        Mockito.when(UndeploymentResourceContainerTest.mockedResourceEnvironmentModelGraphProvider
                .readOnlyRootComponent(ResourceEnvironment.class))
                .thenReturn(UndeploymentResourceContainerTest.testResourceEnvironment);

        Mockito.when(UndeploymentResourceContainerTest.mockedSystemModelGraphProvider
                .readOnlyRootComponent(org.palladiosimulator.pcm.system.System.class))
                .thenReturn(UndeploymentResourceContainerTest.testSystem);

        Mockito.when(UndeploymentResourceContainerTest.mockedAllocationModelGraphProvider
                .readOnlyRootComponent(Allocation.class)).thenReturn(UndeploymentResourceContainerTest.testAllocation);

        /** get part of models */
        Mockito.when(ResourceEnvironmentModelBuilder.getResourceContainerByName(
                UndeploymentResourceContainerTest.testResourceEnvironment, ImplementationLevelData.SERVICE))
                .thenReturn(UndeploymentResourceContainerTest.optTestResourceContainer);

        final String asmCtxName = CorrespondenceModelData.PCM_ENTITY_NAME + "_" + ImplementationLevelData.SERVICE;
        Mockito.when(
                SystemModelBuilder.getAssemblyContextByName(UndeploymentResourceContainerTest.testSystem, asmCtxName))
                .thenReturn(UndeploymentResourceContainerTest.optTestAssemblyContext);

        /** interaction with graphs */
        PowerMockito.doNothing().when(AllocationModelBuilder.class);
        AllocationModelBuilder.removeAllocationContext(UndeploymentResourceContainerTest.testAllocation,
                UndeploymentResourceContainerTest.optTestResourceContainer.get(),
                UndeploymentResourceContainerTest.testAssemblyContext);

        Mockito.doNothing().when(UndeploymentResourceContainerTest.mockedAllocationModelGraphProvider)
                .updateComponent(Allocation.class, UndeploymentResourceContainerTest.testAllocation);
    }

    /**
     * Check whether a {@link ServletUndeployedEvent} is triggered, when the needed
     * {@link ResourceContainer} exists. A {@link ServletUndeployedEvent} is defined as input.
     */
    @Test
    public void checkNoAllocationNeeded() {
        final List<PCMUndeployedEvent> undeploymentEvents = new ArrayList<>();

        StageTester.test(this.undeploymentUpdater).and().send(UndeploymentResourceContainerTest.inputEvents)
                .to(this.undeploymentUpdater.getInputPort()).and().receive(undeploymentEvents)
                .from(this.undeploymentUpdater.getOutputPort()).start();

        Assert.assertThat(undeploymentEvents.get(0), Is.is(UndeploymentResourceContainerTest.undeployedEvent));
    }

}
