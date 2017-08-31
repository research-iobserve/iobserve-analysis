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
import org.iobserve.common.record.EJBUndeployedEvent;
import org.iobserve.common.record.ServletDeployedEvent;
import org.iobserve.common.record.ServletUndeployedEvent;
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

@RunWith(MockitoJUnitRunner.class)
public class UndeploymentVisualizationStageTest {

    /** stage under test */
    private UndeploymentVisualizationStage undeploymentVisualizationStage;

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
    private ModelProvider<org.palladiosimulator.pcm.system.System> mockedSystemModelGraphProvider;
    @Mock
    private ICorrespondence mockedCorrespondenceModel;

    /** input events */
    private final List<ServletUndeployedEvent> inputServletEvents = new ArrayList<>();
    private final List<EJBUndeployedEvent> inputEJBEvents = new ArrayList<>();

    /** test event */
    private ServletUndeployedEvent servletEvent;
    private EJBUndeployedEvent ejbEvent;

    /** data for generating test events */
    private static final long DEPLOY_TIME = 1;
    private static final String SERVICE = "test-service";
    private static final String CONTEXT = "/path/test";
    private static final String DEPLOYMENT_ID = "service-01";
    private static final String URL = "http://" + UndeploymentVisualizationStageTest.SERVICE + '/'
            + UndeploymentVisualizationStageTest.CONTEXT;

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

        this.undeploymentVisualizationStage = new UndeploymentVisualizationStage(this.changelogURL, this.systemId,
                this.mockedResourceContainerModelProvider, this.mockedAssemblyContextModelProvider,
                this.mockedSystemModelGraphProvider, this.mockedCorrespondenceModel);

        /** test events */
        this.servletEvent = new ServletUndeployedEvent(UndeploymentVisualizationStageTest.DEPLOY_TIME,
                UndeploymentVisualizationStageTest.SERVICE, UndeploymentVisualizationStageTest.CONTEXT,
                UndeploymentVisualizationStageTest.DEPLOYMENT_ID);
        this.ejbEvent = new EJBUndeployedEvent(UndeploymentVisualizationStageTest.DEPLOY_TIME,
                UndeploymentVisualizationStageTest.SERVICE, UndeploymentVisualizationStageTest.CONTEXT,
                UndeploymentVisualizationStageTest.DEPLOYMENT_ID);

        /** input events */
        this.inputServletEvents.add(this.servletEvent);
        this.inputEJBEvents.add(this.ejbEvent);

        /** test correspondent */
        UndeploymentVisualizationStageTest.testCorrespondent = CorrespondentFactory.newInstance("test.org.pcm.entity",
                "testPcmEntityId", "testPcmOperationName", "testPcmOperationId");
        UndeploymentVisualizationStageTest.optTestCorrespondent = Optional
                .of(UndeploymentVisualizationStageTest.testCorrespondent);

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
                UndeploymentVisualizationStageTest.SERVICE)).thenReturn(this.testResourceContainers);

        Mockito.when(this.mockedCorrespondenceModel.getCorrespondent(UndeploymentVisualizationStageTest.CONTEXT))
                .thenReturn(UndeploymentVisualizationStageTest.optTestCorrespondent);

        final String asmContextName = UndeploymentVisualizationStageTest.testCorrespondent.getPcmEntityName() + "_"
                + UndeploymentVisualizationStageTest.SERVICE;
        Mockito.when(
                this.mockedAssemblyContextModelProvider.readOnlyComponentByName(AssemblyContext.class, asmContextName))
                .thenReturn(this.testAssemblyContexts);

    }

    /**
     * Check the changelog for deleting a serviceInstance. A {@link ServletDeployedEvent} is defined
     * as input.
     *
     * @throws IOException
     */
    @Test
    public void testServlet() throws IOException {

        StageTester.test(this.undeploymentVisualizationStage).and().send(this.inputServletEvents)
                .to(this.undeploymentVisualizationStage.getInputPort()).start();

        final JSONArray changelogs = new JSONArray(TestHandler.getRequestBody());
        final JSONObject expectedServiceInstance = new JSONObject(changelogs.getJSONObject(0).get("data").toString());

        Assert.assertThat(changelogs.getJSONObject(0).get("operation"), Is.is("DELETE"));
        Assert.assertThat(expectedServiceInstance.getString("type"), Is.is("serviceInstance"));
    }

    /**
     * Check the changelog for deleting a serviceInstance. A {@link EJBDeployedEvent} is defined as
     * input.
     *
     * @throws IOException
     */
    @Test
    public void testEJB() throws IOException {

        StageTester.test(this.undeploymentVisualizationStage).and().send(this.inputEJBEvents)
                .to(this.undeploymentVisualizationStage.getInputPort()).start();

        final JSONArray changelogs = new JSONArray(TestHandler.getRequestBody());
        final JSONObject expectedServiceInstance = new JSONObject(changelogs.getJSONObject(0).get("data").toString());

        Assert.assertThat(changelogs.getJSONObject(0).get("operation"), Is.is("DELETE"));
        Assert.assertThat(expectedServiceInstance.getString("type"), Is.is("serviceInstance"));
    }

}
