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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import teetime.framework.test.StageTester;

import org.hamcrest.core.Is;
import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.analysis.model.correspondence.Correspondent;
import org.iobserve.analysis.model.correspondence.CorrespondentFactory;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.modelneo4j.ModelProvider;
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

import util.TestHandler;

/**
 * Tests for {@link DeploymentVisualizationStage}.
 *
 * @author Josefine Wegert
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class DeploymentVisualizationStageTest {

    /** stage under test. */
    private DeploymentVisualizationStage deploymentVisualizationStage;

    /** test parameters for stage under test. */
    private URL changelogURL;
    private static final String OUTPUT_PORT = "9090";
    private static final String OUTPUT_HOSTNAME = "localhost";
    private static final String SYSTEM_ID = "test_systemId";

    /** data for generating test events. */
    private static final long DEPLOY_TIME = 1;
    private static final String SERVICE = "test-service";
    private static final String CONTEXT = "path.test";
    private static final String DEPLOYMENT_ID = "service-01";

    @Mock
    private ModelProvider<ResourceContainer> mockedResourceContainerModelProvider;
    @Mock
    private ModelProvider<AssemblyContext> mockedAssemblyContextModelProvider;
    @Mock
    private ICorrespondence mockedCorrespondenceModel;

    /** input events. */
    private final List<PCMDeployedEvent> inputEvents = new ArrayList<>();

    /** test event. */
    private PCMDeployedEvent deployedEvent;

    /** test correspondent. */
    private static Correspondent testCorrespondent;
    private static Optional<Correspondent> optTestCorrespondent;

    /** test resource container. */
    private final List<ResourceContainer> testResourceContainers = new ArrayList<>();
    private final String testNodeId = "test_nodeId";
    private ResourceContainer testResourceContainer;

    /** test assembly context. */
    private final List<AssemblyContext> testAssemblyContexts = new ArrayList<>();
    private AssemblyContext testAssemblyContext;

    /**
     * Initialize test data and stub necessary method calls.
     *
     * @throws MalformedURLException
     *             if the creation of the URL fails.
     *
     */
    @Before
    public void setup() throws MalformedURLException {

        this.changelogURL = new URL("http://" + DeploymentVisualizationStageTest.OUTPUT_HOSTNAME + ":"
                + DeploymentVisualizationStageTest.OUTPUT_PORT + "/v1/systems/"
                + DeploymentVisualizationStageTest.SYSTEM_ID + "/changelogs");

        /** test correspondent */
        DeploymentVisualizationStageTest.testCorrespondent = CorrespondentFactory.newInstance("test.org.pcm.entity",
                "testPcmEntityId", "testPcmOperationName", "testPcmOperationId");
        DeploymentVisualizationStageTest.optTestCorrespondent = Optional
                .of(DeploymentVisualizationStageTest.testCorrespondent);

        /** test resource container */
        this.testResourceContainer = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
        this.testResourceContainer.setId(this.testNodeId);
        this.testResourceContainers.add(this.testResourceContainer);

        /** test events */
        final String urlContext = DeploymentVisualizationStageTest.CONTEXT.replaceAll("\\.", "/");
        final String url = "http://" + DeploymentVisualizationStageTest.SERVICE + '/' + urlContext;
        this.deployedEvent = new PCMDeployedEvent(DeploymentVisualizationStageTest.SERVICE,
                DeploymentVisualizationStageTest.testCorrespondent, url, (short) 0);

        this.deployedEvent.setResourceContainer(this.testResourceContainer);

        /** input events */
        this.inputEvents.add(this.deployedEvent);

        /** test assembly context */
        final String asmContextName = DeploymentVisualizationStageTest.testCorrespondent.getPcmEntityName() + "_"
                + DeploymentVisualizationStageTest.SERVICE;

        this.testAssemblyContext = CompositionFactory.eINSTANCE.createAssemblyContext();
        this.testAssemblyContext.setId(DeploymentVisualizationStageTest.SYSTEM_ID);
        this.testAssemblyContext.setEntityName(asmContextName);
        this.testAssemblyContexts.add(this.testAssemblyContext);

        // stubbing
        Mockito.when(this.mockedResourceContainerModelProvider.readOnlyComponentByName(ResourceContainer.class,
                DeploymentVisualizationStageTest.SERVICE)).thenReturn(this.testResourceContainers);

        Mockito.when(this.mockedCorrespondenceModel.getCorrespondent(DeploymentVisualizationStageTest.CONTEXT))
                .thenReturn(DeploymentVisualizationStageTest.optTestCorrespondent);

        Mockito.when(
                this.mockedAssemblyContextModelProvider.readOnlyComponentByName(AssemblyContext.class, asmContextName))
                .thenReturn(this.testAssemblyContexts);

        this.deploymentVisualizationStage = new DeploymentVisualizationStage(this.changelogURL,
                DeploymentVisualizationStageTest.SYSTEM_ID, this.mockedResourceContainerModelProvider,
                this.mockedAssemblyContextModelProvider);
    }

    /**
     * Check whether the changelog for creating the service is written before the changelog for
     * creating the serviceInstance (constraint on deployment visualization). A
     * {@link ServletDeployedEvent} is defined as input.
     *
     */
    @Test
    public void checkServletChangelog() {
        StageTester.test(this.deploymentVisualizationStage).and().send(this.inputEvents)
                .to(this.deploymentVisualizationStage.getInputPort()).start();

        final JSONArray changelogs = new JSONArray(TestHandler.getRequestBody());
        final JSONObject expectedService = new JSONObject(changelogs.getJSONObject(0).get("data").toString());
        final JSONObject expectedServiceInstance = new JSONObject(changelogs.getJSONObject(1).get("data").toString());

        Assert.assertThat(expectedService.get("type"), Is.is("service"));
        Assert.assertThat(expectedServiceInstance.get("type"), Is.is("serviceInstance"));
        Assert.assertEquals(expectedService.get("id"), expectedServiceInstance.get("serviceId"));

        Assert.assertThat(expectedService.get("systemId"), Is.is(DeploymentVisualizationStageTest.SYSTEM_ID));
        Assert.assertThat(expectedServiceInstance.get("systemId"), Is.is(DeploymentVisualizationStageTest.SYSTEM_ID));
    }

}
