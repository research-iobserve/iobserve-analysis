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

import org.hamcrest.core.Is;
import org.iobserve.analysis.deployment.AllocationStage;
import org.iobserve.common.record.ContainerAllocationEvent;
import org.iobserve.common.record.IAllocationEvent;
import org.iobserve.model.factory.ResourceEnvironmentModelFactory;
import org.iobserve.model.persistence.neo4j.ModelProvider;
import org.iobserve.model.test.data.ImplementationLevelDataFactory;
import org.iobserve.model.test.data.ResourceEnvironmentDataFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mockito.Mockito;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.powermock.api.mockito.PowerMockito;

/**
 * Tests for {@link AllocationStage} filter, in case the {@link ResourceContainer} does not exist
 * yet.
 *
 * @author Josefine Wegert
 * @author Reiner Jung
 *
 */
// @RunWith(PowerMockRunner.class) // NOCS test
// write all final classes here
// @PrepareForTest(ResourceEnvironmentModelFactory.class)
public class AllocationNoResourceContainerTest {

    /** mocks. */
    private static ModelProvider<ResourceEnvironment> mockedResourceEnvironmentModelGraphProvider;

    /** test resource containers. */
    private static Optional<ResourceContainer> optTestNullResourceContainer;
    private static ResourceEnvironment resourceEnvironment = ResourceEnvironmentDataFactory.createResourceEnvironment();

    /** stage under test. */
    private AllocationStage allocationStage;

    /**
     * Initialize test data.
     */
    @BeforeClass
    public static void setUp() {
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
                .getModelRootNode(ResourceEnvironment.class))
                .thenReturn(AllocationNoResourceContainerTest.resourceEnvironment);

        Mockito.when(ResourceEnvironmentModelFactory.getResourceContainerByName(
                AllocationNoResourceContainerTest.resourceEnvironment, ImplementationLevelDataFactory.SERVICE))
                .thenReturn(AllocationNoResourceContainerTest.optTestNullResourceContainer);

        Mockito.when(ResourceEnvironmentModelFactory.createResourceContainer(
                AllocationNoResourceContainerTest.resourceEnvironment, ImplementationLevelDataFactory.SERVICE))
                .thenReturn(AllocationNoResourceContainerTest.resourceEnvironment
                        .getResourceContainer_ResourceEnvironment().get(0));

        Mockito.doNothing().when(AllocationNoResourceContainerTest.mockedResourceEnvironmentModelGraphProvider)
                .updatePartition(ResourceEnvironment.class, AllocationNoResourceContainerTest.resourceEnvironment);
    }

    /**
     * Check whether the {@link ContainerAllocationEvent} is forwarded to the next stage after the
     * allocation is finished.
     */
    // @Test
    public void checkAllocationFinished() {
        final List<IAllocationEvent> inputEvents = new ArrayList<>();
        inputEvents.add(ImplementationLevelDataFactory.CONTAINER_ALLOCATION_EVENT);

        final List<ResourceContainer> allocationFinishedEvents = new ArrayList<>();
        final List<IAllocationEvent> allocationEvents = new ArrayList<>();

        StageTester.test(this.allocationStage).and().send(inputEvents).to(this.allocationStage.getInputPort()).and()
                .receive(allocationFinishedEvents).from(this.allocationStage.getAllocationNotifyOutputPort()).and()
                .receive(allocationEvents).from(this.allocationStage.getAllocationOutputPort()).start();

        Assert.assertEquals("Wrong number of ResourceContainers relayed", 1, allocationFinishedEvents.size());
        Assert.assertEquals("Wrong number of create operations", 1, allocationEvents.size());

        Assert.assertThat(allocationFinishedEvents.get(0), Is.is(AllocationNoResourceContainerTest.resourceEnvironment
                .getResourceContainer_ResourceEnvironment().get(0)));
        Assert.assertThat(allocationEvents.get(0), Is.is(ImplementationLevelDataFactory.CONTAINER_ALLOCATION_EVENT));
    }

}
