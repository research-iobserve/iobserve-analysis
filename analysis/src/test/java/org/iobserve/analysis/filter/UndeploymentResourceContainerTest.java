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
import org.iobserve.common.record.ServletUndeployedEvent;
import org.iobserve.model.correspondence.ICorrespondence;
import org.iobserve.model.factory.AllocationModelFactory;
import org.iobserve.model.factory.ResourceEnvironmentModelFactory;
import org.iobserve.model.factory.SystemModelFactory;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.iobserve.model.test.data.AllocationDataFactory;
import org.iobserve.model.test.data.AssemblyContextDataFactory;
import org.iobserve.model.test.data.CorrespondenceModelDataFactory;
import org.iobserve.model.test.data.ImplementationLevelDataFactory;
import org.iobserve.model.test.data.ModelLevelData;
import org.iobserve.model.test.data.ResourceEnvironmentDataFactory;
import org.iobserve.model.test.data.SystemDataFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
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
@RunWith(PowerMockRunner.class) // NOCS test
// write all final classes here
@PrepareForTest({ ResourceEnvironmentModelFactory.class, AllocationModelFactory.class, SystemModelFactory.class })
public class UndeploymentResourceContainerTest {

    /** mocks. */
    @Mock
    private static ModelProvider<org.palladiosimulator.pcm.system.System> mockedSystemModelGraphProvider;
    @Mock
    private static ModelProvider<ResourceEnvironment> mockedResourceEnvironmentModelGraphProvider;
    @Mock
    private static ModelProvider<Allocation> mockedAllocationModelGraphProvider;
    @Mock
    private static ICorrespondence mockedCorrespondence;

    /** stage under test. */
    private UndeploymentModelUpdater undeploymentUpdater;

    /**
     * Define the test situation in which the needed {@link ResourceContainer} and
     * {@link AssemblyContext} exist in the given {@link ResourceEnvironment} model.
     */
    @SuppressWarnings("unchecked")
    @Before
    public void stubMocksResourceContainer() {

        /** mock for ModelBuilder */
        // use PowerMockito for calling static methods of these final classes
        PowerMockito.mockStatic(ResourceEnvironmentModelFactory.class);
        PowerMockito.mockStatic(AllocationModelFactory.class);
        PowerMockito.mockStatic(SystemModelFactory.class);

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
                .getCorrespondent(ImplementationLevelDataFactory.CONTEXT))
                .thenReturn(Optional.of(CorrespondenceModelDataFactory.CORRESPONDENT));

        Mockito.when(UndeploymentResourceContainerTest.mockedResourceEnvironmentModelGraphProvider
                .readOnlyRootComponent(ResourceEnvironment.class))
                .thenReturn(ResourceEnvironmentDataFactory.RESOURCE_ENVIRONMENT);

        Mockito.when(UndeploymentResourceContainerTest.mockedSystemModelGraphProvider
                .readOnlyRootComponent(org.palladiosimulator.pcm.system.System.class))
                .thenReturn(SystemDataFactory.SYSTEM);

        Mockito.when(UndeploymentResourceContainerTest.mockedAllocationModelGraphProvider
                .readOnlyRootComponent(Allocation.class)).thenReturn(AllocationDataFactory.ALLOCATION);

        /** get part of models */
        Mockito.when(ResourceEnvironmentModelFactory.getResourceContainerByName(
                ResourceEnvironmentDataFactory.RESOURCE_ENVIRONMENT, ImplementationLevelDataFactory.SERVICE))
                .thenReturn(Optional.of(ResourceEnvironmentDataFactory.RESOURCE_CONTAINER));

        Mockito.when(SystemModelFactory.getAssemblyContextByName(SystemDataFactory.SYSTEM,
                AssemblyContextDataFactory.ASSEMBLY_CONTEXT_NAME))
                .thenReturn(Optional.of(AssemblyContextDataFactory.ASSEMBLY_CONTEXT));

        /** interaction with graphs */
        PowerMockito.doNothing().when(AllocationModelFactory.class);
        AllocationModelFactory.removeAllocationContext(AllocationDataFactory.ALLOCATION,
                ResourceEnvironmentDataFactory.RESOURCE_CONTAINER, AssemblyContextDataFactory.ASSEMBLY_CONTEXT);

        Mockito.doNothing().when(UndeploymentResourceContainerTest.mockedAllocationModelGraphProvider)
                .updateComponent(Allocation.class, AllocationDataFactory.ALLOCATION);
    }

    /**
     * Check whether a {@link ServletUndeployedEvent} is triggered, when the needed
     * {@link ResourceContainer} exists. A {@link ServletUndeployedEvent} is defined as input.
     */
    @Test
    public void checkUndeployment() {
        final List<PCMUndeployedEvent> inputEvents = new ArrayList<>();
        inputEvents.add(ModelLevelData.PCM_UNDEPLOYED_EVENT);

        final List<PCMUndeployedEvent> undeploymentEvents = new ArrayList<>();

        StageTester.test(this.undeploymentUpdater).and().send(inputEvents).to(this.undeploymentUpdater.getInputPort())
                .and().receive(undeploymentEvents).from(this.undeploymentUpdater.getOutputPort()).start();

        Assert.assertThat(undeploymentEvents.get(0), Is.is(ModelLevelData.PCM_UNDEPLOYED_EVENT));
    }

}
