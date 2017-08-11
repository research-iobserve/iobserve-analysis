package org.iobserve.analysis.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hamcrest.core.Is;
import org.iobserve.analysis.model.AllocationModelBuilder;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelBuilder;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelBuilder;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.analysis.model.correspondence.Correspondent;
import org.iobserve.analysis.model.correspondence.CorrespondentFactory;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.common.record.EJBUndeployedEvent;
import org.iobserve.common.record.IUndeploymentRecord;
import org.iobserve.common.record.ServletUndeployedEvent;
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
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import teetime.framework.test.StageTester;

/**
 * Tests for TUndeployment filter, in case the resource container does not.
 *
 * @author jweg
 *
 */
@RunWith(PowerMockRunner.class)
// write all final classes here
@PrepareForTest({ ResourceEnvironmentModelBuilder.class, AllocationModelBuilder.class, SystemModelBuilder.class,
        AllocationModelProvider.class, SystemModelProvider.class, ResourceEnvironmentModelProvider.class })
public class TUndeploymentNoResourceContainerTest {

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
    private static final long UNDEPLOY_TIME = 1;
    private static final String SERVICE = "test-service";
    private static final String CONTEXT = "/path/test";
    private static final String UNDEPLOYMENT_ID = "service-01";

    /** test events */
    private static ServletUndeployedEvent servletUndeploymentEvent;
    private static EJBUndeployedEvent ejbUndeploymentEvent;

    /***/
    private TUndeployment tUndeployment;
    private static List<IUndeploymentRecord> inputServletEvents = new ArrayList<>();
    private static List<IUndeploymentRecord> inputEJBEvents = new ArrayList<>();

    /** test correspondent */
    private static Correspondent testCorrespondent;
    private static Optional<Correspondent> optTestCorrespondent;

    /** test resource environment */
    private static ResourceEnvironment testResourceEnvironment;

    /** test resource container with null value */
    private static Optional<ResourceContainer> optTestNullResourceContainer;

    /**
     * Initialize test events and stage under test (TDeployment) and therefore mock necessary
     * classes.
     */
    @BeforeClass
    public static void initializeTDeploymentAndMock() {
        /** test events */
        TUndeploymentNoResourceContainerTest.servletUndeploymentEvent = new ServletUndeployedEvent(
                TUndeploymentNoResourceContainerTest.UNDEPLOY_TIME, TUndeploymentNoResourceContainerTest.SERVICE,
                TUndeploymentNoResourceContainerTest.CONTEXT, TUndeploymentNoResourceContainerTest.UNDEPLOYMENT_ID);
        TUndeploymentNoResourceContainerTest.ejbUndeploymentEvent = new EJBUndeployedEvent(
                TUndeploymentNoResourceContainerTest.UNDEPLOY_TIME, TUndeploymentNoResourceContainerTest.SERVICE,
                TUndeploymentNoResourceContainerTest.CONTEXT, TUndeploymentNoResourceContainerTest.UNDEPLOYMENT_ID);

        /** input deployment event */
        TUndeploymentNoResourceContainerTest.inputServletEvents
                .add(TUndeploymentNoResourceContainerTest.servletUndeploymentEvent);
        TUndeploymentNoResourceContainerTest.inputEJBEvents
                .add(TUndeploymentNoResourceContainerTest.ejbUndeploymentEvent);

        /** test correspondent */
        TUndeploymentNoResourceContainerTest.testCorrespondent = CorrespondentFactory.newInstance("test.org.pcm.entity",
                "testPcmEntityId", "testPcmOperationName", "testPcmOperationId");
        TUndeploymentNoResourceContainerTest.optTestCorrespondent = Optional
                .of(TUndeploymentNoResourceContainerTest.testCorrespondent);

        /** test resourceEnvironment */
        TUndeploymentNoResourceContainerTest.testResourceEnvironment = ResourceenvironmentFactory.eINSTANCE
                .createResourceEnvironment();

        /** optional test resource container with null value */
        TUndeploymentNoResourceContainerTest.optTestNullResourceContainer = Optional.ofNullable(null);

    }

    /**
     * Define the test situation in which the needed resource container and assembly context do not
     * exist in the given resource environment model. No unemployment should take place.
     */
    @Before
    public void stubMocksResourceContainer() {

        /** mock for ModelBuilder */
        // use PowerMockito for calling static methods of these final classes
        PowerMockito.mockStatic(ResourceEnvironmentModelBuilder.class);
        PowerMockito.mockStatic(AllocationModelBuilder.class);
        PowerMockito.mockStatic(SystemModelBuilder.class);

        /** mocks for model graph provider */
        TUndeploymentNoResourceContainerTest.mockedSystemModelGraphProvider = Mockito.mock(ModelProvider.class);
        TUndeploymentNoResourceContainerTest.mockedResourceEnvironmentModelGraphProvider = Mockito
                .mock(ModelProvider.class);
        TUndeploymentNoResourceContainerTest.mockedAllocationModelGraphProvider = Mockito.mock(ModelProvider.class);

        /** mock for correspondence model */
        TUndeploymentNoResourceContainerTest.mockedCorrespondence = Mockito.mock(ICorrespondence.class);

        this.tUndeployment = new TUndeployment(TUndeploymentNoResourceContainerTest.mockedCorrespondence,
                TUndeploymentNoResourceContainerTest.mockedAllocationModelGraphProvider,
                TUndeploymentNoResourceContainerTest.mockedSystemModelGraphProvider,
                TUndeploymentNoResourceContainerTest.mockedResourceEnvironmentModelGraphProvider);

        /** get models */
        Mockito.when(TUndeploymentNoResourceContainerTest.mockedCorrespondence
                .getCorrespondent(TUndeploymentNoResourceContainerTest.CONTEXT))
                .thenReturn(TUndeploymentNoResourceContainerTest.optTestCorrespondent);

        Mockito.when(TUndeploymentNoResourceContainerTest.mockedResourceEnvironmentModelGraphProvider
                .readOnlyRootComponent(ResourceEnvironment.class))
                .thenReturn(TUndeploymentNoResourceContainerTest.testResourceEnvironment);

        /** get part of models */
        Mockito.when(ResourceEnvironmentModelBuilder.getResourceContainerByName(
                TUndeploymentNoResourceContainerTest.testResourceEnvironment,
                TUndeploymentNoResourceContainerTest.SERVICE))
                .thenReturn(TUndeploymentNoResourceContainerTest.optTestNullResourceContainer);

    }

    /**
     * Check whether no event is triggered, when there is no needed resource container. A
     * servletUndeploymentEvent is defined as input.
     */
    @Test
    public void checkNoServletAllocationNeeded() {
        final List<IUndeploymentRecord> undeploymentEvents = new ArrayList<>();

        StageTester.test(this.tUndeployment).and().send(TUndeploymentNoResourceContainerTest.inputServletEvents)
                .to(this.tUndeployment.getInputPort()).and().receive(undeploymentEvents)
                .from(this.tUndeployment.getVisualizationOutputPort()).start();

        Assert.assertThat(undeploymentEvents.size(), Is.is(0));
    }

    /**
     * Check whether no event is triggered, when the needed resource container does not exist. An
     * ejbUndeploymentEvent is defined as input.
     */
    @Test
    public void checkNoEjbAllocationNeeded() {
        final List<IUndeploymentRecord> undeploymentEvents = new ArrayList<>();

        StageTester.test(this.tUndeployment).and().send(TUndeploymentNoResourceContainerTest.inputEJBEvents)
                .to(this.tUndeployment.getInputPort()).and().receive(undeploymentEvents)
                .from(this.tUndeployment.getVisualizationOutputPort()).start();

        Assert.assertThat(undeploymentEvents.size(), Is.is(0));
    }

}
