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
import org.iobserve.analysis.deployment.DeploymentModelUpdater;
import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.analysis.model.builder.ResourceEnvironmentModelBuilder;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.analysis.test.data.ImplementationLevelData;
import org.iobserve.analysis.test.data.ModelLevelData;
import org.iobserve.common.record.ServletDeployedEvent;
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
 * Tests for {@link DeploymentModelUpdater} filter, in case the {@link ResourceContainer} does not
 * exist, yet.
 *
 * @author Josefine Wegert
 * @author Reiner Jung
 *
 */
@RunWith(PowerMockRunner.class)
// write all final classes here
@PrepareForTest(ResourceEnvironmentModelBuilder.class)
public class DeploymentNoResourceContainerTest {

    /** stage under test. */
    private DeploymentModelUpdater deploymentModelUpdater;

    /** mocks. */
    @Mock
    private static ModelProvider<org.palladiosimulator.pcm.system.System> mockedSystemModelGraphProvider;
    @Mock
    private static ModelProvider<ResourceEnvironment> mockedResourceEnvironmentModelGraphProvider;
    @Mock
    private static ModelProvider<Allocation> mockedAllocationModelGraphProvider;

    /** input events. */
    private static List<PCMDeployedEvent> inputEvents = new ArrayList<>();

    /** test resource container. */
    private static Optional<ResourceContainer> optTestNullResourceContainer;

    private static PCMDeployedEvent deploymentEvent;

    /**
     * Initialize test events and mocks necessary classes.
     */
    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void setup() {
        /** mocks for model graph provider */
        DeploymentNoResourceContainerTest.mockedSystemModelGraphProvider = Mockito.mock(ModelProvider.class);
        DeploymentNoResourceContainerTest.mockedResourceEnvironmentModelGraphProvider = Mockito
                .mock(ModelProvider.class);
        DeploymentNoResourceContainerTest.mockedAllocationModelGraphProvider = Mockito.mock(ModelProvider.class);

        /** input deployment event */
        DeploymentNoResourceContainerTest.deploymentEvent = ModelLevelData.createPCMDeployedEvent();
        DeploymentNoResourceContainerTest.inputEvents.add(DeploymentNoResourceContainerTest.deploymentEvent);

        /** optional test resource container without value */
        DeploymentNoResourceContainerTest.optTestNullResourceContainer = Optional.ofNullable(null);

    }

    /**
     * Define the test situation in which the needed {@link ResourceContainer} does not exist in the
     * given {@link ResourceEnvironment} model.
     */
    @Before
    public void stubMocksNoServletResourceContainer() {
        this.deploymentModelUpdater = new DeploymentModelUpdater(
                DeploymentNoResourceContainerTest.mockedAllocationModelGraphProvider,
                DeploymentNoResourceContainerTest.mockedSystemModelGraphProvider);

        /** mock for ResourceEnvironmentModelBuilder */
        // use PowerMockito for calling static methods of this final class
        PowerMockito.mockStatic(ResourceEnvironmentModelBuilder.class);

        Mockito.when(
                ResourceEnvironmentModelBuilder
                        .getResourceContainerByName(
                                DeploymentNoResourceContainerTest.mockedResourceEnvironmentModelGraphProvider
                                        .readOnlyRootComponent(ResourceEnvironment.class),
                                ImplementationLevelData.SERVICE))
                .thenReturn(DeploymentNoResourceContainerTest.optTestNullResourceContainer);
    }

    /**
     * Check whether a {@link ServletDeployedEvent} is forwarded, if the needed
     * {@link ResourceContainer} does not exist yet. A {@link ServletDeployedEvent} is defined as
     * input.
     */
    @Test
    public void checkDeploymentAfterAllocationNeeded() {
        final List<PCMDeployedEvent> deploymentEvents = new ArrayList<>();

        StageTester.test(this.deploymentModelUpdater).and().send(DeploymentNoResourceContainerTest.inputEvents)
                .to(this.deploymentModelUpdater.getInputPort()).and().receive(deploymentEvents)
                .from(this.deploymentModelUpdater.getDeployedNotifyOutputPort()).start();

        Assert.assertThat(deploymentEvents.get(0), Is.is(DeploymentNoResourceContainerTest.deploymentEvent));

    }

}
