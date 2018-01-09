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
 * Tests for {@link TAllocation} filter, when the {@link ResourceContainer} already exists.
 *
 * @author jweg
 *
 */
@RunWith(PowerMockRunner.class)
// write all final classes here
@PrepareForTest(ResourceEnvironmentModelBuilder.class)
public class AllocationResourceContainerTest {

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
    private static final String URL = "http://" + AllocationResourceContainerTest.SERVICE + '/'
            + AllocationResourceContainerTest.CONTEXT;

    /** test resource containers. */
    private static ResourceContainer testResourceContainer;
    private static Optional<ResourceContainer> optTestResourceContainer;

    private static ResourceEnvironment testResourceEnvironment;

    /**
     * Initialize test data.
     */
    @BeforeClass
    public static void setup() {

        /** test event */
        AllocationResourceContainerTest.allocationEvent = new ContainerAllocationEvent(
                AllocationResourceContainerTest.URL);

        /** input allocation event */
        AllocationResourceContainerTest.inputEvents.add(AllocationResourceContainerTest.allocationEvent);

        /** optional test resource container with value */
        AllocationResourceContainerTest.testResourceContainer = ResourceenvironmentFactory.eINSTANCE
                .createResourceContainer();
        AllocationResourceContainerTest.testResourceContainer.setEntityName("TestResourceContainer");
        AllocationResourceContainerTest.testResourceContainer.setId("_resourcecontainer_test_id");
        AllocationResourceContainerTest.optTestResourceContainer = Optional
                .of(AllocationResourceContainerTest.testResourceContainer);
        /** test resource environment */
        AllocationResourceContainerTest.testResourceEnvironment = ResourceenvironmentFactory.eINSTANCE
                .createResourceEnvironment();

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
        PowerMockito.mockStatic(ResourceEnvironmentModelBuilder.class);
        /** mock for new graph provider */
        AllocationResourceContainerTest.mockedResourceEnvironmentModelGraphProvider = Mockito
                .mock(ModelProvider.class);

        this.tAllocation = new AllocationStage(
                AllocationResourceContainerTest.mockedResourceEnvironmentModelGraphProvider);

        Mockito.when(AllocationResourceContainerTest.mockedResourceEnvironmentModelGraphProvider
                .readOnlyRootComponent(ResourceEnvironment.class))
                .thenReturn(AllocationResourceContainerTest.testResourceEnvironment);

        Mockito.when(
                ResourceEnvironmentModelBuilder.getResourceContainerByName(
                        AllocationResourceContainerTest.mockedResourceEnvironmentModelGraphProvider
                                .readOnlyRootComponent(ResourceEnvironment.class),
                        AllocationResourceContainerTest.SERVICE))
                .thenReturn(AllocationResourceContainerTest.optTestResourceContainer);
    }

    /**
     * Check that no {@link ContainerAllocationEvent} is forwarded, if the
     * {@link ResourceEnvironment} is not updated.
     */
    @Test
    public void checkNoAllocationUpdate() {

        final List<IAllocationEvent> noAllocationEvents = new ArrayList<>();

        StageTester.test(this.tAllocation).and().send(AllocationResourceContainerTest.inputEvents)
                .to(this.tAllocation.getInputPort()).and().receive(noAllocationEvents)
                .from(this.tAllocation.getAllocationOutputPort()).start();

        Assert.assertThat(noAllocationEvents.size(), Is.is(0));
    }

}
