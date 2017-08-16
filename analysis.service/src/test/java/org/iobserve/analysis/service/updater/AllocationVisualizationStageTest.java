package org.iobserve.analysis.service.updater;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.common.record.ContainerAllocationEvent;
import org.iobserve.common.record.IAllocationRecord;
import org.iobserve.models.Changelog;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;

import teetime.framework.test.StageTester;
import util.TestHandler;

/**
 * Tests for AllocationVisualizationStage.
 *
 * @author jweg
 *
 */

@RunWith(MockitoJUnitRunner.class)
public class AllocationVisualizationStageTest {

    private AllocationVisualizationStage allocationVisualizationStage;
    @Mock
    private ModelProvider<ResourceContainer> mockedResourceContainerModelProvider;
    private static final String systemId = "testSystemId";
    private final String outputPort = "9090";
    private static final String outputHostname = "localhost";
    private URL changelogURL;

    /** list of test resource container */
    private final List<ResourceContainer> testResourceContainerList = new ArrayList<>();
    private ResourceContainer testResourceContainer;

    /** test event */
    private ContainerAllocationEvent allocationEvent;

    /** data for generating test container allocation event */
    private static final String SERVICE = "test-service";
    private static final String CONTEXT = "/path/test";
    private static final String URL = "http://" + AllocationVisualizationStageTest.SERVICE + '/'
            + AllocationVisualizationStageTest.CONTEXT;
    // Erstelle create changelog für node und nodegroup (wie er sein soll)

    /** input events */
    private final List<IAllocationRecord> inputEvents = new ArrayList<>();

    private TestHandler testHandler;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Changelog changelogRequest;

    // /** data for mocking SendHttpRequest */
    // private static final String TEST_RESPONSE = "test\nresponse";
    // private static final String nodeId = "test_nodeId";
    // private static final String nodegroupId = "test_nodegroupId";
    // private final String NODE_DATA = Json.createObjectBuilder().add("type", "node")
    // .add("id", AllocationVisualizationStageTest.nodeId)
    // .add("systemId", AllocationVisualizationStageTest.systemId)
    // .add("nodeGroupId", AllocationVisualizationStageTest.nodegroupId)
    // .add("hostname", AllocationVisualizationStageTest.outputHostname).build();

    // private static final JsonObject NODEGROUP_DATA = Json.createObjectBuilder().add("type",
    // "nodeGroup")
    // .add("id", AllocationVisualizationStageTest.nodegroupId)
    // .add("systemId", AllocationVisualizationStageTest.systemId).add("name",
    // "nodeGroupName").build();
    // private static final String PROTOCOL = "http";
    //
    /**
     * -initializing of stage under test and events for input -http server that takes HTTP-Requests
     *
     * @throws Exception
     */
    @Before
    public void initServer() throws Exception {

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

        // stubbing
        Mockito.when(this.mockedResourceContainerModelProvider.readOnlyComponentByName(ResourceContainer.class,
                AllocationVisualizationStageTest.SERVICE)).thenReturn(this.testResourceContainerList);
        this.testHandler = new TestHandler();

        final HttpServer server = HttpServer.create(new InetSocketAddress(9090), 0);
        server.createContext("/v1/systems/testSystemId/changelogs", this.testHandler);
        server.start();

    }

    /**
     * Check information in HTTP request
     *
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    @Test
    public void test() throws JsonParseException, JsonMappingException, IOException {

        // Sende container allocation event und überprüfe, ob das richtige drinsteht
        StageTester.test(this.allocationVisualizationStage).and().send(this.inputEvents)
                .to(this.allocationVisualizationStage.getInputPort()).start();

        this.changelogRequest = this.objectMapper.readValue(this.testHandler.getRequestBody(), Changelog.class);

        System.out.printf("jsonObject changelogRequest:%s\n", this.changelogRequest.toString());

        // Assert.assertArrayEquals(expecteds, this.testHandler.getRequestBody());

    }

}
