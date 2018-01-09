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
import org.iobserve.analysis.model.correspondence.CorrespondentFactory;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.analysis.test.data.ImplementationLevelData;
import org.iobserve.analysis.test.data.ModelLevelData;
import org.iobserve.common.record.ServletUndeployedEvent;
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
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;
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
@RunWith(PowerMockRunner.class)
// write all final classes here
@PrepareForTest({ ResourceEnvironmentModelBuilder.class, AllocationModelBuilder.class, SystemModelBuilder.class })
public class UndeploymentNoResourceContainerTest {

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
    private static Correspondent testCorrespondent;
    private static Optional<Correspondent> optTestCorrespondent;

    /** test resource environment. */
    private static ResourceEnvironment testResourceEnvironment;

    /** test resource container with null value. */
    private static Optional<ResourceContainer> optTestNullResourceContainer;

    /**
     * Initialize test events and mock necessary classes.
     */
    @BeforeClass
    public static void setup() {

        /** input deployment event */
        UndeploymentNoResourceContainerTest.undeployedEvent = ModelLevelData.createPCMUndeployedEvent();
        UndeploymentNoResourceContainerTest.inputEvents.add(UndeploymentNoResourceContainerTest.undeployedEvent);

        /** test correspondent */
        UndeploymentNoResourceContainerTest.testCorrespondent = CorrespondentFactory.newInstance("test.org.pcm.entity",
                "testPcmEntityId", "testPcmOperationName", "testPcmOperationId");
        UndeploymentNoResourceContainerTest.optTestCorrespondent = Optional
                .of(UndeploymentNoResourceContainerTest.testCorrespondent);

        /** test resourceEnvironment */
        UndeploymentNoResourceContainerTest.testResourceEnvironment = ResourceenvironmentFactory.eINSTANCE
                .createResourceEnvironment();

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
        PowerMockito.mockStatic(ResourceEnvironmentModelBuilder.class);
        PowerMockito.mockStatic(AllocationModelBuilder.class);
        PowerMockito.mockStatic(SystemModelBuilder.class);

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
                .getCorrespondent(ImplementationLevelData.CONTEXT))
                .thenReturn(UndeploymentNoResourceContainerTest.optTestCorrespondent);

        Mockito.when(UndeploymentNoResourceContainerTest.mockedResourceEnvironmentModelGraphProvider
                .readOnlyRootComponent(ResourceEnvironment.class))
                .thenReturn(UndeploymentNoResourceContainerTest.testResourceEnvironment);

        /** get part of models */
        Mockito.when(ResourceEnvironmentModelBuilder.getResourceContainerByName(
                UndeploymentNoResourceContainerTest.testResourceEnvironment, ImplementationLevelData.SERVICE))
                .thenReturn(UndeploymentNoResourceContainerTest.optTestNullResourceContainer);

    }

    /**
     * Check whether no event is triggered, when the needed {@link ResourceContainer} does not
     * exist. A {@link ServletUndeployedEvent} is defined as input.
     */
    @Test
    public void checkNoAllocationNeeded() {
        final List<PCMUndeployedEvent> undeploymentEvents = new ArrayList<>();

        StageTester.test(this.undeploymentUpdater).and().send(UndeploymentNoResourceContainerTest.inputEvents)
                .to(this.undeploymentUpdater.getInputPort()).and().receive(undeploymentEvents)
                .from(this.undeploymentUpdater.getOutputPort()).start();

        Assert.assertThat(undeploymentEvents.size(), Is.is(0));
    }

}
