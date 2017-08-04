package org.iobserve.analysis.filter;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.core.Is;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelBuilder;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelProvider;
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
import org.mockito.Mockito;
import org.palladiosimulator.pcm.allocation.Allocation;

import teetime.framework.test.StageTester;

public class TDeploymentTest {
    /***/
    private static AllocationModelProvider mockedAllocationModelProvider;
    private static SystemModelProvider mockedSystemModelProvider;
    private static ResourceEnvironmentModelProvider mockedResourceEnvironmentModelProvider;
    private static ModelProvider<Allocation> mockedAllocationModelGraphProvider;
    private static ICorrespondence mockedCorrespondence;

    /** data for generating test events */
    private static final long DEPLOY_TIME = 1;
    private static final String SERVICE = "test-service";
    private static final String CONTEXT = "/path/test";
    private static final String DEPLOYMENT_ID = "service-01";
    private static final String URL = "http://" + TDeploymentTest.SERVICE + '/' + TDeploymentTest.CONTEXT;

    /** Test events */
    private ServletDeployedEvent servletDeploymentEvent;
    private EJBDeployedEvent ejbDeploymentEvent;
    private ContainerAllocationEvent allocationEvent;

    /***/
    private static TDeployment tDeployment;
    private final List<IDeploymentRecord> inputEvents = new ArrayList<>();
    private final List<IAllocationRecord> allocationEvents = new ArrayList<>();
    private final List<IDeploymentRecord> deploymentEvents = new ArrayList<>();

    /**
     *
     */
    @BeforeClass
    public static void initializeTDeploymentAndMock() {
        /** mocks old model provider */
        TDeploymentTest.mockedAllocationModelProvider = Mockito.mock(AllocationModelProvider.class);
        TDeploymentTest.mockedSystemModelProvider = Mockito.mock(SystemModelProvider.class);
        TDeploymentTest.mockedResourceEnvironmentModelProvider = Mockito.mock(ResourceEnvironmentModelProvider.class);

        // /** mocks model builder */
        // TDeploymentTest.mockedResourceEnvironmentModelBuilder =
        // Mockito.mock(ResourceEnvironmentModelBuilder.class);

        /** mocks new graph provider */
        TDeploymentTest.mockedAllocationModelGraphProvider = Mockito.mock(ModelProvider.class);

        /** mocks model */
        TDeploymentTest.mockedCorrespondence = Mockito.mock(ICorrespondence.class);

        TDeploymentTest.tDeployment = new TDeployment(TDeploymentTest.mockedCorrespondence,
                TDeploymentTest.mockedAllocationModelProvider, TDeploymentTest.mockedAllocationModelGraphProvider,
                TDeploymentTest.mockedSystemModelProvider, TDeploymentTest.mockedResourceEnvironmentModelProvider);

    }

    // beide Fälle doppelt: für EJB und Servlet
    // 1. Fall: Es gibt den ResourceContainer auf dem deployed werden soll nicht im allocationModel
    // -> this.deploymentOutputPort.send(event); und this.allocationOutputPort.send(new
    // ContainerAllocationEvent(url));
    /**
     *
     */
    @Before
    public void stubMocksNoServletResourceContainer() {
        /** test events */
        this.servletDeploymentEvent = new ServletDeployedEvent(TDeploymentTest.DEPLOY_TIME, TDeploymentTest.SERVICE,
                TDeploymentTest.CONTEXT, TDeploymentTest.DEPLOYMENT_ID);
        this.ejbDeploymentEvent = new EJBDeployedEvent(TDeploymentTest.DEPLOY_TIME, TDeploymentTest.SERVICE,
                TDeploymentTest.CONTEXT, TDeploymentTest.DEPLOYMENT_ID);
        this.allocationEvent = new ContainerAllocationEvent(TDeploymentTest.URL);
        /** input events */
        this.inputEvents.add(this.servletDeploymentEvent);

        /** expected resulting events */
        this.allocationEvents.add(this.allocationEvent);
        // servlet- und ejbDeployment doppelt, weil sie an zwei OutputPorts anliegen?
        this.deploymentEvents.add(this.servletDeploymentEvent);

        // TODO this.correspondence.getCorrespondent(context): has to be mocked, too
        // when(mockedCorrespondence.getCorrespondent(CONTEXT)).thenReturn();
        // Warum geht "TDeploymentTest.mockedResourceEnvironmentModelBuilder" nicht?
        Mockito.when(ResourceEnvironmentModelBuilder.getResourceContainerByName(
                TDeploymentTest.mockedResourceEnvironmentModelProvider.getModel(), TDeploymentTest.SERVICE))
                .thenReturn(null);
    }

    @Test
    public void checkServletAllocationNeeded() {
        StageTester.test(TDeploymentTest.tDeployment).and().send(this.inputEvents)
                .to(TDeploymentTest.tDeployment.getInputPort()).and().receive(this.allocationEvents)
                .from(TDeploymentTest.tDeployment.getAllocationOutputPort()).start();

        Assert.assertThat("=allocationEvent im AllocationOutputPort", Is.is(this.allocationEvent));
        Assert.assertThat("=deploymentEvent im deploymentOutputPort", Is.is(this.servletDeploymentEvent));
    }

    // 2. Fall: Es gibt den ResourceContainer auf den deployed werden soll im allocationModel ->
    // this.deploymentFinishedOutputPort.send(event);
    @Before
    public void stubMocksResourceContainer() {
        // Mockito.when(ResourceEnvironmentModelBuilder.getResourceContainerByName(
        // TDeploymentTest.mockedResourceEnvironmentModelProvider.getModel(),
        // TDeploymentTest.SERVICE))
        // .thenReturn("new ResourceContainer");
    }

    @Test
    public void checkNoServletAllocationNeeded() {
        StageTester.test(TDeploymentTest.tDeployment).and().send(this.inputEvents)
                .to(TDeploymentTest.tDeployment.getInputPort()).and().receive(this.deploymentEvents)// .getOutputElements()
                .from(TDeploymentTest.tDeployment.getDeploymentFinishedOutputPort()).start();

        Assert.assertThat("=deploymentEvent im DeploymentFinishedOutputPort", Is.is(this.servletDeploymentEvent));
    }

}
