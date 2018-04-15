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
import org.iobserve.common.record.ContainerAllocationEvent;
import org.iobserve.common.record.IAllocationEvent;
import org.iobserve.model.factory.ResourceEnvironmentModelFactory;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.iobserve.model.test.data.ImplementationLevelDataFactory;
import org.iobserve.model.test.data.ResourceEnvironmentDataFactory;
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
 * Tests for {@link TAllocation} filter, when the {@link ResourceContainer} already exists.
 *
 * @author jweg
 *
 */
@RunWith(PowerMockRunner.class) // NOCS
// write all final classes here
@PrepareForTest(ResourceEnvironmentModelFactory.class)
public class AllocationResourceContainerTest {

    /** mocks. */
    private static ModelProvider<ResourceEnvironment, ResourceEnvironment> mockedResourceEnvironmentModelGraphProvider;

    /** stage under test. */
    private AllocationStage allocationStage;

    /**
     * Initialize test data.
     */
    @BeforeClass
    public static void setUp() {

        /** input allocation event */
    }

    /**
     * Define the test situation in which a {@link ContainerAllocationEvent} is defined as input and
     * the specified {@link ResourceContainer} does exist in the {@link ResourceEnvironment}.
     */
    @SuppressWarnings("unchecked")
    @Before
    public void stubMocksResourceContainer() {

        /** mock for ResourceEnvironmentModelBuilder */
        // use PowerMockito for calling static methods of this final class
        PowerMockito.mockStatic(ResourceEnvironmentModelFactory.class);
        /** mock for new graph provider */
        AllocationResourceContainerTest.mockedResourceEnvironmentModelGraphProvider = Mockito.mock(ModelProvider.class);

        this.allocationStage = new AllocationStage(
                AllocationResourceContainerTest.mockedResourceEnvironmentModelGraphProvider);

        Mockito.when(AllocationResourceContainerTest.mockedResourceEnvironmentModelGraphProvider
                .readOnlyRootComponent(ResourceEnvironment.class))
                .thenReturn(ResourceEnvironmentDataFactory.RESOURCE_ENVIRONMENT);

        Mockito.when(
                ResourceEnvironmentModelFactory.getResourceContainerByName(
                        AllocationResourceContainerTest.mockedResourceEnvironmentModelGraphProvider
                                .readOnlyRootComponent(ResourceEnvironment.class),
                        ImplementationLevelDataFactory.SERVICE))
                .thenReturn(Optional.of(ResourceEnvironmentDataFactory.RESOURCE_CONTAINER));
    }

    /**
     * Check that no {@link ContainerAllocationEvent} is forwarded, if the
     * {@link ResourceEnvironment} is not updated.
     */
    @Test
    public void checkNoAllocationUpdate() {

        final List<IAllocationEvent> inputEvents = new ArrayList<>();
        inputEvents.add(ImplementationLevelDataFactory.CONTAINER_ALLOCATION_EVENT);

        final List<ResourceContainer> allocationFinishedEvents = new ArrayList<>();
        final List<IAllocationEvent> noAllocationEvents = new ArrayList<>();

        StageTester.test(this.allocationStage).and().send(inputEvents).to(this.allocationStage.getInputPort()).and()
                .receive(allocationFinishedEvents).from(this.allocationStage.getAllocationNotifyOutputPort()).and()
                .receive(noAllocationEvents).from(this.allocationStage.getAllocationOutputPort()).start();

        Assert.assertThat(noAllocationEvents.size(), Is.is(0));
        Assert.assertThat(allocationFinishedEvents.size(), Is.is(1));
    }

}
