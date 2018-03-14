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
import org.iobserve.model.provider.neo4j.ModelProvider;
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
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Tests for {@link UndeploymentModelUpdater} filter, in case the {@link ResourceContainer} does not
 * exist.
 *
 * @author Josefine Wegert
 * @author Reiner Jung
 *
 */
@RunWith(PowerMockRunner.class) // NOCS test
// write all final classes here
@PrepareForTest({ ResourceEnvironmentModelFactory.class, AllocationModelFactory.class })
public class UndeploymentNoResourceContainerTest {

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
    private static List<PCMUndeployedEvent> inputEvents = new ArrayList<>();

    /** test resource container with null value. */
    private static Optional<ResourceContainer> optTestNullResourceContainer;

    /** stage under test. */
    private UndeploymentModelUpdater undeploymentUpdater;

    /**
     * Initialize test events and mock necessary classes.
     */
    @BeforeClass
    public static void setUp() {

        /** input deployment event */
        UndeploymentNoResourceContainerTest.inputEvents.add(ModelLevelData.PCM_UNDEPLOYED_EVENT);

        /** optional test resource container with null value */
        UndeploymentNoResourceContainerTest.optTestNullResourceContainer = Optional.ofNullable(null);

    }

    /**
     * Define the test situation in which the needed {@link ResourceContainer} and
     * {@link AssemblyContext} do not exist in the given {@link ResourceEnvironment} model. No
     * unemployment should take place.
     */
    @SuppressWarnings("unchecked")
    @Before
    public void stubMocksResourceContainer() {

        /** mock for ModelBuilder */
        // use PowerMockito for calling static methods of these final classes
        PowerMockito.mockStatic(ResourceEnvironmentModelFactory.class);
        PowerMockito.mockStatic(AllocationModelFactory.class);

        /** mocks for model graph provider */
        UndeploymentNoResourceContainerTest.mockedSystemModelGraphProvider = Mockito.mock(ModelProvider.class);
        UndeploymentNoResourceContainerTest.mockedResourceEnvironmentModelGraphProvider = Mockito
                .mock(ModelProvider.class);
        UndeploymentNoResourceContainerTest.mockedAllocationModelGraphProvider = Mockito.mock(ModelProvider.class);

        /** mock for correspondence model */
        UndeploymentNoResourceContainerTest.mockedCorrespondence = Mockito.mock(ICorrespondence.class);

        this.undeploymentUpdater = new UndeploymentModelUpdater(
                UndeploymentNoResourceContainerTest.mockedAllocationModelGraphProvider,
                UndeploymentNoResourceContainerTest.mockedSystemModelGraphProvider);

        /** get models */
        Mockito.when(UndeploymentNoResourceContainerTest.mockedCorrespondence
                .getCorrespondent(ImplementationLevelDataFactory.CONTEXT))
                .thenReturn(Optional.of(CorrespondenceModelDataFactory.CORRESPONDENT));

        Mockito.when(UndeploymentNoResourceContainerTest.mockedResourceEnvironmentModelGraphProvider
                .readOnlyRootComponent(ResourceEnvironment.class))
                .thenReturn(ResourceEnvironmentDataFactory.RESOURCE_ENVIRONMENT);

        Mockito.when(UndeploymentNoResourceContainerTest.mockedSystemModelGraphProvider
                .readOnlyRootComponent(org.palladiosimulator.pcm.system.System.class))
                .thenReturn(SystemDataFactory.SYSTEM);

        /** get part of models */
        Mockito.when(ResourceEnvironmentModelFactory.getResourceContainerByName(
                ResourceEnvironmentDataFactory.RESOURCE_ENVIRONMENT, ImplementationLevelDataFactory.SERVICE))
                .thenReturn(UndeploymentNoResourceContainerTest.optTestNullResourceContainer);

    }

    /**
     * Check whether no event is triggered, when the needed {@link ResourceContainer} does not
     * exist. A {@link ServletUndeployedEvent} is defined as input.
     */
    @Test
    public void checkNoUndeployment() {
        final List<PCMUndeployedEvent> undeploymentEvents = new ArrayList<>();

        StageTester.test(this.undeploymentUpdater).and().send(UndeploymentNoResourceContainerTest.inputEvents)
                .to(this.undeploymentUpdater.getInputPort()).and().receive(undeploymentEvents)
                .from(this.undeploymentUpdater.getOutputPort()).start();

        Assert.assertThat(undeploymentEvents.size(), Is.is(0));
    }

}
