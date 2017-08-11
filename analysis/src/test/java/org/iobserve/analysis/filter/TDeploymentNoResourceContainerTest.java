package org.iobserve.analysis.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hamcrest.core.Is;
import org.iobserve.analysis.model.AllocationModelBuilder;
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
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import teetime.framework.test.StageTester;

/**
 * Tests for TDeployment filter, in case the resource container does not exist, yet.
 *
 * @author jweg
 *
 */
@RunWith(PowerMockRunner.class)
// write all final classes here
@PrepareForTest({ ResourceEnvironmentModelBuilder.class, AllocationModelBuilder.class, AllocationModelProvider.class,
        SystemModelProvider.class, ResourceEnvironmentModelProvider.class })
public class TDeploymentNoResourceContainerTest {

    /** mocks */
    @Mock
    private static ModelProvider<org.palladiosimulator.pcm.system.System> mockedSystemModelGraphProvider;
    @Mock
    private static ModelProvider<ResourceEnvironment> mockedResourceEnvironmentModelGraphProvider;
    @Mock
    private static ModelProvider<Allocation> mockedAllocationModelGraphProvider;
    @Mock
    private static ICorrespondence mockedCorrespondence;

    /** data for generating test events */
    private static final long DEPLOY_TIME = 1;
    private static final String SERVICE = "test-service";
    private static final String CONTEXT = "/path/test";
    private static final String DEPLOYMENT_ID = "service-01";
    private static final String URL = "http://" + TDeploymentNoResourceContainerTest.SERVICE + '/'
            + TDeploymentNoResourceContainerTest.CONTEXT;

    /** test events */
    private static ServletDeployedEvent servletDeploymentEvent;
    private static EJBDeployedEvent ejbDeploymentEvent;
    private static ContainerAllocationEvent allocationEvent;

    /***/
    private TDeployment tDeployment;
    private static List<IDeploymentRecord> inputServletEvents = new ArrayList<>();
    private static List<IDeploymentRecord> inputEJBEvents = new ArrayList<>();

    /** test correspondent */
    private static Correspondent testCorrespondent;
    private static Optional<Correspondent> optTestCorrespondent;

    /** test resource containers */
    private static Optional<ResourceContainer> optTestNullResourceContainer;

    /**
     * Initialize test events and stage under test (TDeployment) and therefore mock necessary
     * classes.
     */
    @BeforeClass
    public static void initializeTDeploymentAndMock() {
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
     * Define the test situation in which the needed resource container does not exist in the given
     * resource environment model.
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
     * Check whether an allocation event is triggered, if the needed resource container does not
     * exist yet. A servletDeploymentEvent is defined as input.
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
     * Check whether an deployment event is forwarded, if the needed resource container does not
     * exist yet. A servletDeploymentEvent is defined as input.
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
     * Check whether an allocation event is triggered, if the needed resource container does not
     * exist yet. A EJBDeploymentEvent is defined as input.
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
     * Check whether an deployment event is forwarded, if the needed resource container does not
     * exist yet. A EJBDeploymentEvent is defined as input.
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
