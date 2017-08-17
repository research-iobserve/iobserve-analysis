package org.iobserve.analysis.service.updater;

import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.core.Is;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.common.record.ContainerAllocationEvent;
import org.iobserve.common.record.IAllocationRecord;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;

import com.sun.net.httpserver.HttpServer;

import teetime.framework.test.StageTester;
import util.TestHandler;

/**
 * Tests for {@link AllocationVisualizationStage}.
 *
 * @author jweg
 *
 */

@RunWith(MockitoJUnitRunner.class)
public class AllocationVisualizationStageTest {

    /** stage under test */
    private AllocationVisualizationStage allocationVisualizationStage;

    /** test parameters for stage under test */
    @Mock
    private ModelProvider<ResourceContainer> mockedResourceContainerModelProvider;
    private static final String systemId = "testSystemId";
    private URL changelogURL;
    private final String outputPort = "9090";
    private static final String outputHostname = "localhost";

    /** input events */
    private final List<IAllocationRecord> inputEvents = new ArrayList<>();

    /** test event */
    private ContainerAllocationEvent allocationEvent;

    /** data for generating test container allocation event */
    private static final String SERVICE = "test-service";
    private static final String CONTEXT = "/path/test";
    private static final String URL = "http://" + AllocationVisualizationStageTest.SERVICE + '/'
            + AllocationVisualizationStageTest.CONTEXT;

    /** list of test resource container */
    private final List<ResourceContainer> testResourceContainerList = new ArrayList<>();
    private ResourceContainer testResourceContainer;

    /** handler for http requests */
    private TestHandler testHandler;

    /**
     * Initialize test data and setup test server.
     *
     * @throws Exception
     */
    @Before
    public void setupAndInitServer() throws Exception {

        this.changelogURL = new URL("http://" + AllocationVisualizationStageTest.outputHostname + ":" + this.outputPort
                + "/v1/systems/" + AllocationVisualizationStageTest.systemId + "/changelogs");

        this.allocationVisualizationStage = new AllocationVisualizationStage(this.changelogURL,
                AllocationVisualizationStageTest.systemId, this.mockedResourceContainerModelProvider);

        /** test event */
        this.allocationEvent = new ContainerAllocationEvent(AllocationVisualizationStageTest.URL);

        /** input events */
        this.inputEvents.add(this.allocationEvent);

        /** list of test resource container */
        this.testResourceContainer = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
        this.testResourceContainer.setEntityName("test_nodeName");
        this.testResourceContainer.setId("test_nodeId");
        this.testResourceContainerList.add(this.testResourceContainer);

        /** init test server */
        this.testHandler = new TestHandler();
        final HttpServer server = HttpServer.create(new InetSocketAddress(9090), 0);
        server.createContext("/v1/systems/testSystemId/changelogs", this.testHandler);
        server.start();

        // stubbing
        Mockito.when(this.mockedResourceContainerModelProvider.readOnlyComponentByName(ResourceContainer.class,
                AllocationVisualizationStageTest.SERVICE)).thenReturn(this.testResourceContainerList);

    }

    /**
     * Check whether the changelog for creating the nodegroup is written before the changelog for
     * creating the node (constraint on deployment visualization) and that the node is assigned to
     * the nodegroup.
     */
    @Test
    public void test() {
        StageTester.test(this.allocationVisualizationStage).and().send(this.inputEvents)
                .to(this.allocationVisualizationStage.getInputPort()).start();

        final JSONArray changelogs = new JSONArray(this.testHandler.getRequestBody());
        final JSONObject expectedNodegroup = new JSONObject(changelogs.getJSONObject(0).get("data").toString());
        final JSONObject expectedNode = new JSONObject(changelogs.getJSONObject(1).get("data").toString());

        Assert.assertThat(expectedNodegroup.get("type"), Is.is("nodeGroup"));
        Assert.assertThat(expectedNode.get("type"), Is.is("node"));
        Assert.assertEquals(expectedNodegroup.get("id"), expectedNode.get("nodeGroupId"));

        Assert.assertThat(expectedNodegroup.get("systemId"), Is.is(AllocationVisualizationStageTest.systemId));
        Assert.assertThat(expectedNode.get("systemId"), Is.is(AllocationVisualizationStageTest.systemId));

    }

}
