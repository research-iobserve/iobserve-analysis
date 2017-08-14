package org.iobserve.analysis.service.updater;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.common.record.ContainerAllocationEvent;
import org.iobserve.common.record.IAllocationRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;

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
    private final String systemId = "testSystemId";
    private final String outputPort = "9090";
    private final String outputHostname = "localhost";
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

    /**
     * -initializing of stage under test and events for input -http server that takes HTTP-Requests
     *
     * @throws IOException
     */
    @Before
    public void initServer() throws IOException {
        System.out.println("@Before am Anfang");

        this.changelogURL = new URL("http://" + this.outputHostname + ":" + this.outputPort + "/v1/systems/"
                + this.systemId + "/changelogs");

        this.allocationVisualizationStage = new AllocationVisualizationStage(this.changelogURL, this.systemId,
                this.mockedResourceContainerModelProvider);

        /** test event */
        this.allocationEvent = new ContainerAllocationEvent(AllocationVisualizationStageTest.URL);

        /** input events */
        this.inputEvents.add(this.allocationEvent);

        /** list of test resource container */
        this.testResourceContainer = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
        this.testResourceContainerList.add(this.testResourceContainer);

        // stubbing
        Mockito.when(this.mockedResourceContainerModelProvider.readOnlyComponentByName(ResourceContainer.class,
                AllocationVisualizationStageTest.SERVICE)).thenReturn(this.testResourceContainerList);

        System.out.println("@Before setup and start server");
        final HttpServer server = HttpServer.create(new InetSocketAddress(9090), 0);
        server.createContext("/v1/systems/testSystemId/changelogs", new TestHandler());
        server.start();
    }

    /**
     * Check information in HTTP request
     */
    @Test
    public void test() {
        System.out.println("@Test");
        // Sende container allocation event und überprüfe, ob das richtige drinsteht
        StageTester.test(this.allocationVisualizationStage).and().send(this.inputEvents)
                .to(this.allocationVisualizationStage.getInputPort()).start();

    }

}
