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
package org.iobserve.analysis.test.service.updater;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import teetime.framework.test.StageTester;

import org.hamcrest.core.Is;
import org.iobserve.analysis.service.updater.AllocationVisualizationStage;
import org.iobserve.analysis.test.service.suites.VisualizationHttpTestServer;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;

/**
 * Tests for {@link AllocationVisualizationStage}.
 *
 * @author Josefine Wegert
 *
 */

// @RunWith(MockitoJUnitRunner.class) // NOCS
public class AllocationVisualizationStageTest {

    private static final String SYSTEM_ID = "test_systemId";
    private static final String OUTPUT_PORT = "9090";
    private static final String OUTPUT_HOSTNAME = "localhost";

    /** data for generating test container allocation event. */
    private static final String SERVICE = "test-service";

    /** stage under test. */
    private AllocationVisualizationStage allocationVisualizationStage;

    /** test parameters for stage under test. */
    @Mock
    private ModelProvider<ResourceContainer> mockedResourceContainerModelProvider;

    /** input events. */
    private final List<ResourceContainer> inputEvents = new ArrayList<>();

    /** test event. */

    /** list of test resource container. */
    private final List<ResourceContainer> testResourceContainerList = new ArrayList<>();

    /**
     * Initialize test data and stub necessary method calls.
     *
     * @throws MalformedURLException
     *             if the creation of the URL fails.
     *
     */
    // @Before
    public void setupAndInitServer() throws MalformedURLException {
        final URL changelogURL = new URL("http://" + AllocationVisualizationStageTest.OUTPUT_HOSTNAME + ":"
                + AllocationVisualizationStageTest.OUTPUT_PORT + "/v1/systems/"
                + AllocationVisualizationStageTest.SYSTEM_ID + "/changelogs");

        this.allocationVisualizationStage = new AllocationVisualizationStage(changelogURL,
                AllocationVisualizationStageTest.SYSTEM_ID);

        /** list of test resource container */
        final ResourceContainer testResourceContainer = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
        testResourceContainer.setEntityName("test_nodeName");
        testResourceContainer.setId("test_nodeId");
        this.testResourceContainerList.add(testResourceContainer);

        /** input events */
        this.inputEvents.add(testResourceContainer);

        // stubbing
        Mockito.when(this.mockedResourceContainerModelProvider.readOnlyComponentByName(ResourceContainer.class,
                AllocationVisualizationStageTest.SERVICE)).thenReturn(this.testResourceContainerList);

    }

    /**
     * Check whether the changelog for creating the nodegroup is written before the changelog for
     * creating the node (constraint on deployment visualization) and that the node is assigned to
     * the nodegroup.
     *
     */
    // TODO fix test
    // @Test(timeout = 3000)
    public void test() {

        StageTester.test(this.allocationVisualizationStage).and().send(this.inputEvents)
                .to(this.allocationVisualizationStage.getInputPort()).start();

        final JSONArray changelogs = new JSONArray(VisualizationHttpTestServer.getRequestBody());
        final JSONObject expectedNodegroup = new JSONObject(changelogs.getJSONObject(0).get("data").toString());
        final JSONObject expectedNode = new JSONObject(changelogs.getJSONObject(1).get("data").toString());

        Assert.assertThat(expectedNodegroup.get("type"), Is.is("nodeGroup"));
        Assert.assertThat(expectedNode.get("type"), Is.is("node"));
        Assert.assertEquals(expectedNodegroup.get("id"), expectedNode.get("nodeGroupId"));

        Assert.assertThat(expectedNodegroup.get("systemId"), Is.is(AllocationVisualizationStageTest.SYSTEM_ID));
        Assert.assertThat(expectedNode.get("systemId"), Is.is(AllocationVisualizationStageTest.SYSTEM_ID));

    }

}
