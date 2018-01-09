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
import org.iobserve.analysis.model.builder.ResourceEnvironmentModelBuilder;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.common.record.ContainerAllocationEvent;
import org.iobserve.common.record.IAllocationEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Tests for {@link TAllocation} filter, in case the {@link ResourceContainer} does not exist yet.
 *
 * @author jweg
 *
 */
@RunWith(PowerMockRunner.class)
// write all final classes here
@PrepareForTest(ResourceEnvironmentModelBuilder.class)
public class AllocationNoResourceContainerTest {

    /** stage under test. */
    private AllocationStage tAllocation;

    /** mocks. */
    private static ModelProvider<ResourceEnvironment> mockedResourceEnvironmentModelGraphProvider;

    /** test event. */
    private static ContainerAllocationEvent allocationEvent;

    /** input events. */
    private static List<IAllocationEvent> inputEvents = new ArrayList<>();

    /** data for generating test container allocation event. */
    private static final String SERVICE = "test-service";
    private static final String CONTEXT = "/path/test";
    private static final String URL = "http://" + AllocationNoResourceContainerTest.SERVICE + '/'
            + AllocationNoResourceContainerTest.CONTEXT;

    /** test resource containers. */
    private static Optional<ResourceContainer> optTestNullResourceContainer;
    private static ResourceContainer testResourceContainer;

    private static ResourceEnvironment testResourceEnvironment;

    /**
     * Initialize test data.
     */
    @BeforeClass
    public static void setup() {

        /** test event */
        AllocationNoResourceContainerTest.allocationEvent = new ContainerAllocationEvent(
                AllocationNoResourceContainerTest.URL);

        /** input allocation event */
        AllocationNoResourceContainerTest.inputEvents.add(AllocationNoResourceContainerTest.allocationEvent);

        /** optional test resource container without value */
        AllocationNoResourceContainerTest.optTestNullResourceContainer = Optional.ofNullable(null);
        // /** test resource container with value */
        AllocationNoResourceContainerTest.testResourceContainer = ResourceenvironmentFactory.eINSTANCE
                .createResourceContainer();
        AllocationNoResourceContainerTest.testResourceContainer.setEntityName("TestResourceContainer");
        AllocationNoResourceContainerTest.testResourceContainer.setId("_resourcecontainer_test_id");

        /** test resource environment */
        AllocationNoResourceContainerTest.testResourceEnvironment = ResourceenvironmentFactory.eINSTANCE
                .createResourceEnvironment();

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
        PowerMockito.mockStatic(ResourceEnvironmentModelBuilder.class);
        /** mock for new graph provider */
        AllocationNoResourceContainerTest.mockedResourceEnvironmentModelGraphProvider = Mockito
                .mock(ModelProvider.class);

        this.tAllocation = new AllocationStage(
                AllocationNoResourceContainerTest.mockedResourceEnvironmentModelGraphProvider);

        Mockito.when(AllocationNoResourceContainerTest.mockedResourceEnvironmentModelGraphProvider
                .readOnlyRootComponent(ResourceEnvironment.class))
                .thenReturn(AllocationNoResourceContainerTest.testResourceEnvironment);

        Mockito.when(ResourceEnvironmentModelBuilder.getResourceContainerByName(
                AllocationNoResourceContainerTest.testResourceEnvironment, AllocationNoResourceContainerTest.SERVICE))
                .thenReturn(AllocationNoResourceContainerTest.optTestNullResourceContainer);

        Mockito.when(ResourceEnvironmentModelBuilder.createResourceContainer(
                AllocationNoResourceContainerTest.testResourceEnvironment, AllocationNoResourceContainerTest.SERVICE))
                .thenReturn(AllocationNoResourceContainerTest.testResourceContainer);

        Mockito.doNothing().when(AllocationNoResourceContainerTest.mockedResourceEnvironmentModelGraphProvider)
                .updateComponent(ResourceEnvironment.class, AllocationNoResourceContainerTest.testResourceEnvironment);
    }

    /**
     * Check whether the {@link ContainerAllocationEvent} is forwarded to the next stage after the
     * allocation is finished.
     */
    @Test
    public void checkAllocationFinished() {

        final List<ResourceContainer> allocationFinishedEvents = new ArrayList<>();

        StageTester.test(this.tAllocation).and().send(AllocationNoResourceContainerTest.inputEvents)
                .to(this.tAllocation.getInputPort()).and().receive(allocationFinishedEvents)
                .from(this.tAllocation.getAllocationNotifyOutputPort()).start();

        Assert.assertThat(allocationFinishedEvents.get(0), Is.is(AllocationNoResourceContainerTest.allocationEvent));
    }

    /**
     * Check whether the {@link ContainerAllocationEvent} is forwarded to the next stage after the
     * {@link ResourceEnvironment} is updated.
     */
    @Test
    public void checkAllocationUpdate() {

        final List<IAllocationEvent> allocationEvents = new ArrayList<>();

        StageTester.test(this.tAllocation).and().send(AllocationNoResourceContainerTest.inputEvents)
                .to(this.tAllocation.getInputPort()).and().receive(allocationEvents)
                .from(this.tAllocation.getAllocationOutputPort()).start();

        Assert.assertThat(allocationEvents.get(0), Is.is(AllocationNoResourceContainerTest.allocationEvent));

    }

}
