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
import org.iobserve.analysis.deployment.AllocationStage;
import org.iobserve.analysis.test.data.ImplementationLevelData;
import org.iobserve.analysis.test.data.ResourceEnvironmentData;
import org.iobserve.common.record.ContainerAllocationEvent;
import org.iobserve.common.record.IAllocationEvent;
import org.iobserve.model.factory.ResourceEnvironmentModelFactory;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Tests for {@link AllocationStage} filter, in case the {@link ResourceContainer} does not exist
 * yet.
 *
 * @author Josefine Wegert
 * @author Reiner Jung
 *
 */
@RunWith(PowerMockRunner.class)
// write all final classes here
@PrepareForTest(ResourceEnvironmentModelFactory.class)
public class AllocationNoResourceContainerTest {

    /** stage under test. */
    private AllocationStage allocationStage;

    /** mocks. */
    private static ModelProvider<ResourceEnvironment> mockedResourceEnvironmentModelGraphProvider;

    /** test resource containers. */
    private static Optional<ResourceContainer> optTestNullResourceContainer;

    /**
     * Initialize test data.
     */
    @BeforeClass
    public static void setup() {
        /** optional test resource container without value */
        AllocationNoResourceContainerTest.optTestNullResourceContainer = Optional.ofNullable(null);
    }

    /**
     * Define the test situation in which a {@link ContainerAllocationEvent} is defined as input and
     * the specified {@link ResourceContainer} does not exist in the {@link ResourceEnvironment}.
     */
    @SuppressWarnings("unchecked")
    @Before
    public void stubMocksNoResourceContainer() {

        /** mock for ResourceEnvironmentModelBuilder */
        // use PowerMockito for calling static methods of this final class
        PowerMockito.mockStatic(ResourceEnvironmentModelFactory.class);
        /** mock for new graph provider */
        AllocationNoResourceContainerTest.mockedResourceEnvironmentModelGraphProvider = Mockito
                .mock(ModelProvider.class);

        this.allocationStage = new AllocationStage(
                AllocationNoResourceContainerTest.mockedResourceEnvironmentModelGraphProvider);

        Mockito.when(AllocationNoResourceContainerTest.mockedResourceEnvironmentModelGraphProvider
                .readOnlyRootComponent(ResourceEnvironment.class))
                .thenReturn(ResourceEnvironmentData.RESOURCE_ENVIRONMENT);

        Mockito.when(ResourceEnvironmentModelFactory.getResourceContainerByName(
                ResourceEnvironmentData.RESOURCE_ENVIRONMENT, ImplementationLevelData.SERVICE))
                .thenReturn(AllocationNoResourceContainerTest.optTestNullResourceContainer);

        Mockito.when(ResourceEnvironmentModelFactory
                .createResourceContainer(ResourceEnvironmentData.RESOURCE_ENVIRONMENT, ImplementationLevelData.SERVICE))
                .thenReturn(ResourceEnvironmentData.RESOURCE_CONTAINER);

        Mockito.doNothing().when(AllocationNoResourceContainerTest.mockedResourceEnvironmentModelGraphProvider)
                .updateComponent(ResourceEnvironment.class, ResourceEnvironmentData.RESOURCE_ENVIRONMENT);
    }

    /**
     * Check whether the {@link ContainerAllocationEvent} is forwarded to the next stage after the
     * allocation is finished.
     */
    @Test
    public void checkAllocationFinished() {
        final List<IAllocationEvent> inputEvents = new ArrayList<>();
        inputEvents.add(ImplementationLevelData.CONTAINER_ALLOCATION_EVENT);

        final List<ResourceContainer> allocationFinishedEvents = new ArrayList<>();
        final List<IAllocationEvent> allocationEvents = new ArrayList<>();

        StageTester.test(this.allocationStage).and().send(inputEvents).to(this.allocationStage.getInputPort()).and()
                .receive(allocationFinishedEvents).from(this.allocationStage.getAllocationNotifyOutputPort()).and()
                .receive(allocationEvents).from(this.allocationStage.getAllocationOutputPort()).start();

        Assert.assertEquals("Wrong number of ResourceContainers relayed", 1, allocationFinishedEvents.size());
        Assert.assertEquals("Wrong number of create operations", 1, allocationEvents.size());

        Assert.assertThat(allocationFinishedEvents.get(0), Is.is(ResourceEnvironmentData.RESOURCE_CONTAINER));
        Assert.assertThat(allocationEvents.get(0), Is.is(ImplementationLevelData.CONTAINER_ALLOCATION_EVENT));
    }

}
