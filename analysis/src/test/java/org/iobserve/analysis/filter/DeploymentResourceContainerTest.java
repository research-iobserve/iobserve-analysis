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

import org.iobserve.analysis.deployment.DeploymentModelUpdater;
import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
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
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
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
@RunWith(PowerMockRunner.class) // NOCS
// write all final classes here
@PrepareForTest({ ResourceEnvironmentModelFactory.class, AllocationModelFactory.class, SystemModelFactory.class })
public class DeploymentResourceContainerTest {

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
    private DeploymentModelUpdater deploymentModelUpdater;

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
    }

    /**
     * Define the test situation in which the needed {@link ResourceContainer} exists in the given
     * {@link ResourceEnvironment} model.
     */
    @Before
    public void stubMocksResourceContainer() {

        /** mock for ModelBuilder */
        // use PowerMockito for calling static methods of these final classes
        PowerMockito.mockStatic(ResourceEnvironmentModelFactory.class);
        PowerMockito.mockStatic(AllocationModelFactory.class);
        PowerMockito.mockStatic(SystemModelFactory.class);

        this.deploymentModelUpdater = new DeploymentModelUpdater(
                DeploymentResourceContainerTest.mockedAllocationModelGraphProvider,
                DeploymentResourceContainerTest.mockedSystemModelGraphProvider);

        /** get models */
        Mockito.when(DeploymentResourceContainerTest.mockedCorrespondence
                .getCorrespondent(ImplementationLevelDataFactory.CONTEXT))
                .thenReturn(Optional.of(CorrespondenceModelDataFactory.CORRESPONDENT));

        Mockito.when(DeploymentResourceContainerTest.mockedAllocationModelGraphProvider
                .readOnlyRootComponent(Allocation.class)).thenReturn(AllocationDataFactory.ALLOCATION);

        Mockito.when(DeploymentResourceContainerTest.mockedResourceEnvironmentModelGraphProvider
                .readOnlyRootComponent(ResourceEnvironment.class))
                .thenReturn(ResourceEnvironmentDataFactory.RESOURCE_ENVIRONMENT);

        Mockito.when(DeploymentResourceContainerTest.mockedSystemModelGraphProvider
                .readOnlyRootComponent(org.palladiosimulator.pcm.system.System.class))
                .thenReturn(SystemDataFactory.SYSTEM);

        /** get part of models */

        Mockito.when(SystemModelFactory.getAssemblyContextByName(SystemDataFactory.SYSTEM,
                AssemblyContextDataFactory.ASSEMBLY_CONTEXT_NAME))
                .thenReturn(Optional.of(AssemblyContextDataFactory.ASSEMBLY_CONTEXT));

        Mockito.when(ResourceEnvironmentModelFactory.getResourceContainerByName(
                ResourceEnvironmentDataFactory.RESOURCE_ENVIRONMENT, ImplementationLevelDataFactory.SERVICE))
                .thenReturn(Optional.of(ResourceEnvironmentDataFactory.RESOURCE_CONTAINER));

        /** interaction with graphs */
        Mockito.when(SystemModelFactory.createAssemblyContextsIfAbsent(SystemDataFactory.SYSTEM,
                ImplementationLevelDataFactory.SERVICE)).thenReturn(AssemblyContextDataFactory.ASSEMBLY_CONTEXT);

        Mockito.doNothing().when(DeploymentResourceContainerTest.mockedSystemModelGraphProvider)
                .updateComponent(org.palladiosimulator.pcm.system.System.class, SystemDataFactory.SYSTEM);

        PowerMockito.doNothing().when(AllocationModelFactory.class);
        AllocationModelFactory.addAllocationContext(AllocationDataFactory.ALLOCATION,
                ResourceEnvironmentDataFactory.RESOURCE_CONTAINER, AssemblyContextDataFactory.ASSEMBLY_CONTEXT);

        Mockito.doNothing().when(DeploymentResourceContainerTest.mockedAllocationModelGraphProvider)
                .updateComponent(Allocation.class, AllocationDataFactory.ALLOCATION);
    }

    /**
     * Tests whether a new deployment is generated for an already existing entity.
     */
    @Test
    public void checkNoDeploymentNeeded() {

        /** input deployment event */
        final PCMDeployedEvent deploymentEvent = ModelLevelData.PCM_DEPLOYED_EVENT;
        final List<PCMDeployedEvent> inputEvents = new ArrayList<>();
        inputEvents.add(deploymentEvent);

        final List<PCMDeployedEvent> outputEvents = new ArrayList<>();

        StageTester.test(this.deploymentModelUpdater).and().send(inputEvents)
                .to(this.deploymentModelUpdater.getInputPort()).start();

        Assert.assertThat(this.deploymentModelUpdater.getDeployedNotifyOutputPort(), StageTester.producesNothing());

        Assert.assertEquals("There should be no notification for errorous data.", outputEvents.size(), 0);
    }

}
