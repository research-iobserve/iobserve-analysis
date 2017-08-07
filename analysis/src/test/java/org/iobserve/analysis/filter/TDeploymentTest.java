package org.iobserve.analysis.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hamcrest.core.Is;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelBuilder;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelProvider;
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
import org.mockito.Mockito;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;
import org.palladiosimulator.pcm.resourceenvironment.impl.ResourceenvironmentFactoryImpl;

import teetime.framework.test.StageTester;

/**
 * Tests for TDeployment filter.
 *
 * @author jweg
 *
 */
public class TDeploymentTest {
    /** mocks */
    private static AllocationModelProvider mockedAllocationModelProvider;
    private static SystemModelProvider mockedSystemModelProvider;
    private static ResourceEnvironmentModelProvider mockedResourceEnvironmentModelProvider;
    private static ModelProvider<Allocation> mockedAllocationModelGraphProvider;
    private static ICorrespondence mockedCorrespondence;
    private static ResourceEnvironmentModelBuilder mockedResourceEnvironmentModelBuilder;

    /** data for generating test events */
    private static final long DEPLOY_TIME = 1;
    private static final String SERVICE = "test-service";
    private static final String CONTEXT = "/path/test";
    private static final String DEPLOYMENT_ID = "service-01";
    private static final String URL = "http://" + TDeploymentTest.SERVICE + '/' + TDeploymentTest.CONTEXT;

    /** test events */
    private static ServletDeployedEvent servletDeploymentEvent;
    private static EJBDeployedEvent ejbDeploymentEvent;
    private static ContainerAllocationEvent allocationEvent;

    /***/
    private static TDeployment tDeployment;
    private final List<IDeploymentRecord> inputEvents = new ArrayList<>();

    /**
     * Initialize test events and stage under test (TDeployment) and therefore mock necessary
     * classes.
     */
    @BeforeClass
    public static void initializeTDeploymentAndMock() {
        /** test events */
        TDeploymentTest.servletDeploymentEvent = new ServletDeployedEvent(TDeploymentTest.DEPLOY_TIME,
                TDeploymentTest.SERVICE, TDeploymentTest.CONTEXT, TDeploymentTest.DEPLOYMENT_ID);
        TDeploymentTest.ejbDeploymentEvent = new EJBDeployedEvent(TDeploymentTest.DEPLOY_TIME, TDeploymentTest.SERVICE,
                TDeploymentTest.CONTEXT, TDeploymentTest.DEPLOYMENT_ID);
        TDeploymentTest.allocationEvent = new ContainerAllocationEvent(TDeploymentTest.URL);

        /** mocks for old model provider */
        TDeploymentTest.mockedAllocationModelProvider = Mockito.mock(AllocationModelProvider.class);
        TDeploymentTest.mockedSystemModelProvider = Mockito.mock(SystemModelProvider.class);
        TDeploymentTest.mockedResourceEnvironmentModelProvider = Mockito.mock(ResourceEnvironmentModelProvider.class);

        /** mocks for new graph provider */
        TDeploymentTest.mockedAllocationModelGraphProvider = Mockito.mock(ModelProvider.class);

        /** mock for correspondence model */
        TDeploymentTest.mockedCorrespondence = Mockito.mock(ICorrespondence.class);

        /** mock for model builder */
        TDeploymentTest.mockedResourceEnvironmentModelBuilder = Mockito.mock(ResourceEnvironmentModelBuilder.class);

        TDeploymentTest.tDeployment = new TDeployment(TDeploymentTest.mockedCorrespondence,
                TDeploymentTest.mockedAllocationModelProvider, TDeploymentTest.mockedAllocationModelGraphProvider,
                TDeploymentTest.mockedSystemModelProvider, TDeploymentTest.mockedResourceEnvironmentModelProvider);

    }

    /**
     * Define the test situation in which the needed resource container does not exist in the given
     * resource environment model. A servletDeploymentEvent is defined as input.
     */
    @Before
    public void stubMocksNoServletResourceContainer() {

        this.inputEvents.add(TDeploymentTest.servletDeploymentEvent);

        /** test correspondent */
        // TODO sinnvolle Werte
        final Correspondent testCorrespondent = CorrespondentFactory.newInstance("test.org.pcm.entity", "pcmEntityId",
                "pcmOperationName", "pcmOperationId");
        final Optional<Correspondent> optTestCorrespondent = Optional.of(testCorrespondent);

        Mockito.when(TDeploymentTest.mockedCorrespondence.getCorrespondent(TDeploymentTest.CONTEXT))
                .thenReturn(optTestCorrespondent);

        final Optional<ResourceContainer> optTestResourceContainer = Optional.ofNullable(null);
        Mockito.when(ResourceEnvironmentModelBuilder.getResourceContainerByName(
                TDeploymentTest.mockedResourceEnvironmentModelProvider.getModel(), TDeploymentTest.SERVICE))
                .thenReturn(optTestResourceContainer);
    }

    /**
     * Check whether an allocation event is triggered, if the needed resource container does not
     * exist yet.
     */
    @Test
    public void checkServletAllocationNeeded() {
        final List<IAllocationRecord> allocationEvents = new ArrayList<>();

        StageTester.test(TDeploymentTest.tDeployment).and().send(this.inputEvents)
                .to(TDeploymentTest.tDeployment.getInputPort()).and().receive(allocationEvents)
                .from(TDeploymentTest.tDeployment.getAllocationOutputPort()).start();

        Assert.assertThat(allocationEvents, Is.is(TDeploymentTest.allocationEvent));
        // weiterer Testfall?
        // Assert.assertThat("=deploymentEvent im deploymentOutputPort",
        // Is.is(TDeploymentTest.servletDeploymentEvent));
    }

    /**
     * Define the test situation in which the needed resource container does exist in the given
     * resource environment model. A servletDeploymentEvent is defined as input.
     */
    @Before
    public void stubMocksResourceContainer() {

        /** input events */
        this.inputEvents.add(TDeploymentTest.servletDeploymentEvent);

        /** test correspondent */
        // TODO sinnvolle Werte
        final Correspondent testCorrespondent = CorrespondentFactory.newInstance("test.org.pcm.entity", "pcmEntityId",
                "pcmOperationName", "pcmOperationId");
        final Optional<Correspondent> optTestCorrespondent = Optional.of(testCorrespondent);

        Mockito.when(TDeploymentTest.mockedCorrespondence.getCorrespondent(TDeploymentTest.CONTEXT))
                .thenReturn(optTestCorrespondent);

        /** test resource container */
        final ResourceContainer testResourceContainer = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
        testResourceContainer.setEntityName("TestResourceContainer");
        testResourceContainer.setId("_resourcecontainer_test_id");
        final Optional<ResourceContainer> optTestResourceContainer = Optional.of(testResourceContainer);

        new ResourceenvironmentFactoryImpl();
        Mockito.when(ResourceEnvironmentModelBuilder.getResourceContainerByName(
                TDeploymentTest.mockedResourceEnvironmentModelProvider.getModel(), TDeploymentTest.SERVICE))
                .thenReturn(optTestResourceContainer);
    }

    /**
     * Check whether a deployment event is triggered, if the needed resource container does exist.
     */
    @Test
    public void checkNoServletAllocationNeeded() {
        final List<IDeploymentRecord> deploymentEvents = new ArrayList<>();

        StageTester.test(TDeploymentTest.tDeployment).and().send(this.inputEvents)
                .to(TDeploymentTest.tDeployment.getInputPort()).and().receive(deploymentEvents)
                .from(TDeploymentTest.tDeployment.getDeploymentFinishedOutputPort()).start();

        Assert.assertThat(deploymentEvents, Is.is(TDeploymentTest.servletDeploymentEvent));
    }

}
