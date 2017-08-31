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
package org.iobserve.analysis.service.updater;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hamcrest.core.Is;
import org.iobserve.analysis.model.correspondence.Correspondent;
import org.iobserve.analysis.model.correspondence.CorrespondentFactory;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.common.record.EJBDeployedEvent;
import org.iobserve.common.record.ServletDeployedEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.CompositionFactory;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;

import teetime.framework.test.StageTester;
import util.TestHandler;

/**
 * Tests for {@link DeploymentVisualizationStage}
 *
 * @author jweg
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class DeploymentVisualizationStageTest {

    /** stage under test */
    private DeploymentVisualizationStage deploymentVisualizationStage;

    /** test parameters for stage under test */
    private URL changelogURL;
    private final String outputPort = "9090";
    private final String outputHostname = "localhost";
    private final String systemId = "test_systemId";
    @Mock
    private ModelProvider<ResourceContainer> mockedResourceContainerModelProvider;
    @Mock
    private ModelProvider<AssemblyContext> mockedAssemblyContextModelProvider;
    @Mock
    private ICorrespondence mockedCorrespondenceModel;

    /** input events */
    private final List<ServletDeployedEvent> inputServletEvents = new ArrayList<>();
    private final List<EJBDeployedEvent> inputEJBEvents = new ArrayList<>();

    /** test event */
    private ServletDeployedEvent servletEvent;
    private EJBDeployedEvent ejbEvent;

    /** data for generating test events */
    private static final long DEPLOY_TIME = 1;
    private static final String SERVICE = "test-service";
    private static final String CONTEXT = "/path/test";
    private static final String DEPLOYMENT_ID = "service-01";
    private static final String URL = "http://" + DeploymentVisualizationStageTest.SERVICE + '/'
            + DeploymentVisualizationStageTest.CONTEXT;

    /** test correspondent */
    private static Correspondent testCorrespondent;
    private static Optional<Correspondent> optTestCorrespondent;

    /** test resource container */
    private final List<ResourceContainer> testResourceContainers = new ArrayList<>();
    private final String testNodeId = "test_nodeId";
    private ResourceContainer testResourceContainer;

    /** test assembly context */
    private final List<AssemblyContext> testAssemblyContexts = new ArrayList<>();
    private AssemblyContext testAssemblyContext;

    /**
     * Initialize test data and stub necessary method calls.
     *
     * @throws IOException
     */
    @Before
    public void setup() throws IOException {

        this.changelogURL = new URL("http://" + this.outputHostname + ":" + this.outputPort + "/v1/systems/"
                + this.systemId + "/changelogs");

        this.deploymentVisualizationStage = new DeploymentVisualizationStage(this.changelogURL, this.systemId,
                this.mockedResourceContainerModelProvider, this.mockedAssemblyContextModelProvider,
                this.mockedCorrespondenceModel);

        /** test events */
        this.servletEvent = new ServletDeployedEvent(DeploymentVisualizationStageTest.DEPLOY_TIME,
                DeploymentVisualizationStageTest.SERVICE, DeploymentVisualizationStageTest.CONTEXT,
                DeploymentVisualizationStageTest.DEPLOYMENT_ID);
        this.ejbEvent = new EJBDeployedEvent(DeploymentVisualizationStageTest.DEPLOY_TIME,
                DeploymentVisualizationStageTest.SERVICE, DeploymentVisualizationStageTest.CONTEXT,
                DeploymentVisualizationStageTest.DEPLOYMENT_ID);

        /** input events */
        this.inputServletEvents.add(this.servletEvent);
        this.inputEJBEvents.add(this.ejbEvent);

        /** test correspondent */
        DeploymentVisualizationStageTest.testCorrespondent = CorrespondentFactory.newInstance("test.org.pcm.entity",
                "testPcmEntityId", "testPcmOperationName", "testPcmOperationId");
        DeploymentVisualizationStageTest.optTestCorrespondent = Optional
                .of(DeploymentVisualizationStageTest.testCorrespondent);

        /** test resource container */
        this.testResourceContainer = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
        this.testResourceContainer.setId(this.testNodeId);
        this.testResourceContainers.add(this.testResourceContainer);

        /** test assembly context */
        this.testAssemblyContext = CompositionFactory.eINSTANCE.createAssemblyContext();
        this.testAssemblyContext.setId("test_serviceId");
        this.testAssemblyContext.setEntityName("test_serviceName");
        this.testAssemblyContexts.add(this.testAssemblyContext);

        // stubbing
        Mockito.when(this.mockedResourceContainerModelProvider.readOnlyComponentByName(ResourceContainer.class,
                DeploymentVisualizationStageTest.SERVICE)).thenReturn(this.testResourceContainers);

        Mockito.when(this.mockedCorrespondenceModel.getCorrespondent(DeploymentVisualizationStageTest.CONTEXT))
                .thenReturn(DeploymentVisualizationStageTest.optTestCorrespondent);

        final String asmContextName = DeploymentVisualizationStageTest.testCorrespondent.getPcmEntityName() + "_"
                + DeploymentVisualizationStageTest.SERVICE;
        Mockito.when(
                this.mockedAssemblyContextModelProvider.readOnlyComponentByName(AssemblyContext.class, asmContextName))
                .thenReturn(this.testAssemblyContexts);

    }

    /**
     * Check whether the changelog for creating the service is written before the changelog for
     * creating the serviceInstance (constraint on deployment visualization). A
     * {@link ServletDeployedEvent} is defined as input.
     */
    @Test
    public void checkServletChangelog() {
        StageTester.test(this.deploymentVisualizationStage).and().send(this.inputServletEvents)
                .to(this.deploymentVisualizationStage.getInputPort()).start();

        final JSONArray changelogs = new JSONArray(TestHandler.getRequestBody());
        final JSONObject expectedService = new JSONObject(changelogs.getJSONObject(0).get("data").toString());
        final JSONObject expectedServiceInstance = new JSONObject(changelogs.getJSONObject(1).get("data").toString());

        Assert.assertThat(expectedService.get("type"), Is.is("service"));
        Assert.assertThat(expectedServiceInstance.get("type"), Is.is("serviceInstance"));
        Assert.assertEquals(expectedService.get("id"), expectedServiceInstance.get("serviceId"));

        Assert.assertThat(expectedService.get("systemId"), Is.is(this.systemId));
        Assert.assertThat(expectedServiceInstance.get("systemId"), Is.is(this.systemId));
    }

    /**
     * Check whether the changelog for creating the service is written before the changelog for
     * creating the serviceInstance (constraint on deployment visualization). A
     * {@link EJBDeployedEvent} is defined as input.
     */
    @Test
    public void checkEJBChangelog() {
        StageTester.test(this.deploymentVisualizationStage).and().send(this.inputEJBEvents)
                .to(this.deploymentVisualizationStage.getInputPort()).start();

        final JSONArray changelogs = new JSONArray(TestHandler.getRequestBody());
        final JSONObject expectedService = new JSONObject(changelogs.getJSONObject(0).get("data").toString());
        final JSONObject expectedServiceInstance = new JSONObject(changelogs.getJSONObject(1).get("data").toString());

        Assert.assertThat(expectedService.get("type"), Is.is("service"));
        Assert.assertThat(expectedServiceInstance.get("type"), Is.is("serviceInstance"));
        Assert.assertEquals(expectedService.get("id"), expectedServiceInstance.get("serviceId"));

        Assert.assertThat(expectedService.get("systemId"), Is.is(this.systemId));
        Assert.assertThat(expectedServiceInstance.get("systemId"), Is.is(this.systemId));
    }
}
