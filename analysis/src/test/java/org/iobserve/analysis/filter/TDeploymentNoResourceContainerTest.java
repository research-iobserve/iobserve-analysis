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
import org.iobserve.analysis.model.ResourceEnvironmentModelBuilder;
import org.iobserve.analysis.model.correspondence.Correspondent;
import org.iobserve.analysis.model.correspondence.CorrespondentFactory;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.common.record.ContainerAllocationEvent;
import org.iobserve.common.record.EJBDeployedEvent;
import org.iobserve.common.record.IAllocationRecord;
import org.iobserve.common.record.IDeploymentRecord;
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
 * Tests for {@link TDeployment} filter, in case the {@link ResourceContainer} does not exist, yet.
 *
 * @author jweg
 *
 */
@RunWith(PowerMockRunner.class)
// write all final classes here
@PrepareForTest(ResourceEnvironmentModelBuilder.class)
public class TDeploymentNoResourceContainerTest {

    /** stage under test. */
    private TDeployment tDeployment;

    /** mocks. */
    @Mock
    private static ModelProvider<org.palladiosimulator.pcm.system.System> mockedSystemModelGraphProvider;
    @Mock
    private static ModelProvider<ResourceEnvironment> mockedResourceEnvironmentModelGraphProvider;
    @Mock
    private static ModelProvider<Allocation> mockedAllocationModelGraphProvider;
    @Mock
    private static ICorrespondence mockedCorrespondence;

    /** data for generating test events. */
    private static final long DEPLOY_TIME = 1;
    private static final String SERVICE = "test-service";
    private static final String CONTEXT = "/path/test";
    private static final String DEPLOYMENT_ID = "service-01";
    private static final String URL = "http://" + TDeploymentNoResourceContainerTest.SERVICE + '/'
            + TDeploymentNoResourceContainerTest.CONTEXT;

    /** test events. */
    private static ServletDeployedEvent servletDeploymentEvent;
    private static EJBDeployedEvent ejbDeploymentEvent;
    private static ContainerAllocationEvent allocationEvent;

    /** input events. */
    private static List<IDeploymentRecord> inputServletEvents = new ArrayList<>();
    private static List<IDeploymentRecord> inputEJBEvents = new ArrayList<>();

    /** test correspondent. */
    private static Correspondent testCorrespondent;
    private static Optional<Correspondent> optTestCorrespondent;

    /** test resource container. */
    private static Optional<ResourceContainer> optTestNullResourceContainer;

    /**
     * Initialize test events and mocks necessary classes.
     */
    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void setup() {
        /** test events */
        TDeploymentNoResourceContainerTest.servletDeploymentEvent = new ServletDeployedEvent(
                TDeploymentNoResourceContainerTest.DEPLOY_TIME, TDeploymentNoResourceContainerTest.SERVICE,
                TDeploymentNoResourceContainerTest.CONTEXT, TDeploymentNoResourceContainerTest.DEPLOYMENT_ID);
        TDeploymentNoResourceContainerTest.ejbDeploymentEvent = new EJBDeployedEvent(
                TDeploymentNoResourceContainerTest.DEPLOY_TIME, TDeploymentNoResourceContainerTest.SERVICE,
                TDeploymentNoResourceContainerTest.CONTEXT, TDeploymentNoResourceContainerTest.DEPLOYMENT_ID);
        TDeploymentNoResourceContainerTest.allocationEvent = new ContainerAllocationEvent(
                TDeploymentNoResourceContainerTest.URL);

        /** mocks for model graph provider */
        TDeploymentNoResourceContainerTest.mockedSystemModelGraphProvider = Mockito.mock(ModelProvider.class);
        TDeploymentNoResourceContainerTest.mockedResourceEnvironmentModelGraphProvider = Mockito
                .mock(ModelProvider.class);
        TDeploymentNoResourceContainerTest.mockedAllocationModelGraphProvider = Mockito.mock(ModelProvider.class);

        /** mock for correspondence model */
        TDeploymentNoResourceContainerTest.mockedCorrespondence = Mockito.mock(ICorrespondence.class);

        /** input deployment event */
        TDeploymentNoResourceContainerTest.inputServletEvents
                .add(TDeploymentNoResourceContainerTest.servletDeploymentEvent);
        TDeploymentNoResourceContainerTest.inputEJBEvents.add(TDeploymentNoResourceContainerTest.ejbDeploymentEvent);

        /** test correspondent */
        TDeploymentNoResourceContainerTest.testCorrespondent = CorrespondentFactory.newInstance("test.org.pcm.entity",
                "testPcmEntityId", "testPcmOperationName", "testPcmOperationId");
        TDeploymentNoResourceContainerTest.optTestCorrespondent = Optional
                .of(TDeploymentNoResourceContainerTest.testCorrespondent);

        /** optional test resource container without value */
        TDeploymentNoResourceContainerTest.optTestNullResourceContainer = Optional.ofNullable(null);

    }

    /**
     * Define the test situation in which the needed {@link ResourceContainer} does not exist in the
     * given {@link ResourceEnvironment} model.
     */
    @Before
    public void stubMocksNoServletResourceContainer() {
        this.tDeployment = new TDeployment(TDeploymentNoResourceContainerTest.mockedCorrespondence,
                TDeploymentNoResourceContainerTest.mockedAllocationModelGraphProvider,
                TDeploymentNoResourceContainerTest.mockedSystemModelGraphProvider,
                TDeploymentNoResourceContainerTest.mockedResourceEnvironmentModelGraphProvider);

        Mockito.when(TDeploymentNoResourceContainerTest.mockedCorrespondence
                .getCorrespondent(TDeploymentNoResourceContainerTest.CONTEXT))
                .thenReturn(TDeploymentNoResourceContainerTest.optTestCorrespondent);

        /** mock for ResourceEnvironmentModelBuilder */
        // use PowerMockito for calling static methods of this final class
        PowerMockito.mockStatic(ResourceEnvironmentModelBuilder.class);

        Mockito.when(
                ResourceEnvironmentModelBuilder.getResourceContainerByName(
                        TDeploymentNoResourceContainerTest.mockedResourceEnvironmentModelGraphProvider
                                .readOnlyRootComponent(ResourceEnvironment.class),
                        TDeploymentNoResourceContainerTest.SERVICE))
                .thenReturn(TDeploymentNoResourceContainerTest.optTestNullResourceContainer);
    }

    /**
     * Check whether an {@link ContainerAllocationEvent} is triggered, if the needed
     * {@link ResourceContainer} does not exist yet. A {@link ServletDeployedEvent} is defined as
     * input.
     */
    @Test
    public void checkServletAllocationNeeded() {
        final List<IAllocationRecord> allocationEvents = new ArrayList<>();

        StageTester.test(this.tDeployment).and().send(TDeploymentNoResourceContainerTest.inputServletEvents)
                .to(this.tDeployment.getInputPort()).and().receive(allocationEvents)
                .from(this.tDeployment.getAllocationOutputPort()).start();

        Assert.assertThat(allocationEvents.get(0), Is.is(TDeploymentNoResourceContainerTest.allocationEvent));
    }

    /**
     * Check whether a {@link ServletDeployedEvent} is forwarded, if the needed
     * {@link ResourceContainer} does not exist yet. A {@link ServletDeployedEvent} is defined as
     * input.
     */
    @Test
    public void checkServletDeploymentAfterAllocationNeeded() {
        final List<IDeploymentRecord> deploymentEvents = new ArrayList<>();

        StageTester.test(this.tDeployment).and().send(TDeploymentNoResourceContainerTest.inputServletEvents)
                .to(this.tDeployment.getInputPort()).and().receive(deploymentEvents)
                .from(this.tDeployment.getDeploymentOutputPort()).start();

        Assert.assertThat(deploymentEvents.get(0), Is.is(TDeploymentNoResourceContainerTest.servletDeploymentEvent));

    }

    /**
     * Check whether an {@link ContainerAllocationEvent} is triggered, if the needed
     * {@link ResourceContainer} does not exist yet. A {@link EJBDeployedEvent} is defined as input.
     */
    @Test
    public void checkEjbAllocationNeeded() {
        final List<IAllocationRecord> allocationEvents = new ArrayList<>();

        StageTester.test(this.tDeployment).and().send(TDeploymentNoResourceContainerTest.inputEJBEvents)
                .to(this.tDeployment.getInputPort()).and().receive(allocationEvents)
                .from(this.tDeployment.getAllocationOutputPort()).start();

        Assert.assertThat(allocationEvents.get(0), Is.is(TDeploymentNoResourceContainerTest.allocationEvent));
    }

    /**
     * Check whether an {@link EJBDeployedEvent} is forwarded, if the needed
     * {@link ResourceContainer} does not exist yet. A {@link EJBDeployedEvent} is defined as input.
     */
    @Test
    public void checkEjbDeploymentAfterAllocationNeeded() {
        final List<IDeploymentRecord> deploymentEvents = new ArrayList<>();

        StageTester.test(this.tDeployment).and().send(TDeploymentNoResourceContainerTest.inputEJBEvents)
                .to(this.tDeployment.getInputPort()).and().receive(deploymentEvents)
                .from(this.tDeployment.getDeploymentOutputPort()).start();

        Assert.assertThat(deploymentEvents.get(0), Is.is(TDeploymentNoResourceContainerTest.ejbDeploymentEvent));

    }
}
