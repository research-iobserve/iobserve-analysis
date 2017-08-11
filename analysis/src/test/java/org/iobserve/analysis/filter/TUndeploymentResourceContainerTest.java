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
import org.palladiosimulator.pcm.allocation.AllocationFactory;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.CompositionFactory;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;
import org.palladiosimulator.pcm.system.SystemFactory;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import teetime.framework.test.StageTester;

/**
 * Tests for TUndeployment filter, in case the resource container exists already.
 *
 * @author jweg
 *
 */
@RunWith(PowerMockRunner.class)
// write all final classes here
@PrepareForTest({ ResourceEnvironmentModelBuilder.class, AllocationModelBuilder.class, SystemModelBuilder.class,
        AllocationModelProvider.class, SystemModelProvider.class, ResourceEnvironmentModelProvider.class })
public class TUndeploymentResourceContainerTest {

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
    private static final String URL = "http://" + TUndeploymentResourceContainerTest.SERVICE + '/'
            + TUndeploymentResourceContainerTest.CONTEXT;

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

    /** test allocation */
    private static Allocation testAllocation;

    /** test resource environment */
    private static ResourceEnvironment testResourceEnvironment;

    /** test system */
    private static org.palladiosimulator.pcm.system.System testSystem;

    /** test resource container */
    private static ResourceContainer testResourceContainer;
    private static Optional<ResourceContainer> optTestResourceContainer;

    /** test assembly context */
    private static AssemblyContext testAssemblyContext;
    private static Optional<AssemblyContext> optTestAssemblyContext;

    /**
     * Initialize test events and stage under test (TDeployment) and therefore mock necessary
     * classes.
     */
    @BeforeClass
    public static void initializeTDeploymentAndMock() {
        /** test events */
        TUndeploymentResourceContainerTest.servletUndeploymentEvent = new ServletUndeployedEvent(
                TUndeploymentResourceContainerTest.UNDEPLOY_TIME, TUndeploymentResourceContainerTest.SERVICE,
                TUndeploymentResourceContainerTest.CONTEXT, TUndeploymentResourceContainerTest.UNDEPLOYMENT_ID);
        TUndeploymentResourceContainerTest.ejbUndeploymentEvent = new EJBUndeployedEvent(
                TUndeploymentResourceContainerTest.UNDEPLOY_TIME, TUndeploymentResourceContainerTest.SERVICE,
                TUndeploymentResourceContainerTest.CONTEXT, TUndeploymentResourceContainerTest.UNDEPLOYMENT_ID);

        /** input deployment event */
        TUndeploymentResourceContainerTest.inputServletEvents
                .add(TUndeploymentResourceContainerTest.servletUndeploymentEvent);
        TUndeploymentResourceContainerTest.inputEJBEvents.add(TUndeploymentResourceContainerTest.ejbUndeploymentEvent);

        /** test correspondent */
        TUndeploymentResourceContainerTest.testCorrespondent = CorrespondentFactory.newInstance("test.org.pcm.entity",
                "testPcmEntityId", "testPcmOperationName", "testPcmOperationId");
        TUndeploymentResourceContainerTest.optTestCorrespondent = Optional
                .of(TUndeploymentResourceContainerTest.testCorrespondent);

        /** test allocation */
        TUndeploymentResourceContainerTest.testAllocation = AllocationFactory.eINSTANCE.createAllocation();

        /** test resourceEnvironment */
        TUndeploymentResourceContainerTest.testResourceEnvironment = ResourceenvironmentFactory.eINSTANCE
                .createResourceEnvironment();

        /** test system */
        TUndeploymentResourceContainerTest.testSystem = SystemFactory.eINSTANCE.createSystem();

        /** optional test resource container with value */
        TUndeploymentResourceContainerTest.testResourceContainer = ResourceenvironmentFactory.eINSTANCE
                .createResourceContainer();
        TUndeploymentResourceContainerTest.testResourceContainer.setEntityName("TestResourceContainer");
        TUndeploymentResourceContainerTest.testResourceContainer.setId("_resourcecontainer_test_id");
        TUndeploymentResourceContainerTest.optTestResourceContainer = Optional
                .of(TUndeploymentResourceContainerTest.testResourceContainer);

        /** optional test assembly context */
        TUndeploymentResourceContainerTest.testAssemblyContext = CompositionFactory.eINSTANCE.createAssemblyContext();
        TUndeploymentResourceContainerTest.testAssemblyContext.setEntityName("TestAssemblyContext");
        TUndeploymentResourceContainerTest.testAssemblyContext.setId("_assemblycontext_test_id");
        TUndeploymentResourceContainerTest.optTestAssemblyContext = Optional
                .of(TUndeploymentResourceContainerTest.testAssemblyContext);

    }

    /**
     * Define the test situation in which the needed resource container and assembly context exist
     * in the given resource environment model.
     */
    @Before
    public void stubMocksResourceContainer() {

        /** mock for ModelBuilder */
        // use PowerMockito for calling static methods of these final classes
        PowerMockito.mockStatic(ResourceEnvironmentModelBuilder.class);
        PowerMockito.mockStatic(AllocationModelBuilder.class);
        PowerMockito.mockStatic(SystemModelBuilder.class);

        /** mocks for model graph provider */
        TUndeploymentResourceContainerTest.mockedSystemModelGraphProvider = Mockito.mock(ModelProvider.class);
        TUndeploymentResourceContainerTest.mockedResourceEnvironmentModelGraphProvider = Mockito
                .mock(ModelProvider.class);
        TUndeploymentResourceContainerTest.mockedAllocationModelGraphProvider = Mockito.mock(ModelProvider.class);

        /** mock for correspondence model */
        TUndeploymentResourceContainerTest.mockedCorrespondence = Mockito.mock(ICorrespondence.class);

        this.tUndeployment = new TUndeployment(TUndeploymentResourceContainerTest.mockedCorrespondence,
                TUndeploymentResourceContainerTest.mockedAllocationModelGraphProvider,
                TUndeploymentResourceContainerTest.mockedSystemModelGraphProvider,
                TUndeploymentResourceContainerTest.mockedResourceEnvironmentModelGraphProvider);

        /** get models */
        Mockito.when(TUndeploymentResourceContainerTest.mockedCorrespondence
                .getCorrespondent(TUndeploymentResourceContainerTest.CONTEXT))
                .thenReturn(TUndeploymentResourceContainerTest.optTestCorrespondent);

        Mockito.when(TUndeploymentResourceContainerTest.mockedResourceEnvironmentModelGraphProvider
                .readOnlyRootComponent(ResourceEnvironment.class))
                .thenReturn(TUndeploymentResourceContainerTest.testResourceEnvironment);

        Mockito.when(TUndeploymentResourceContainerTest.mockedSystemModelGraphProvider
                .readOnlyRootComponent(org.palladiosimulator.pcm.system.System.class))
                .thenReturn(TUndeploymentResourceContainerTest.testSystem);

        Mockito.when(TUndeploymentResourceContainerTest.mockedAllocationModelGraphProvider
                .readOnlyRootComponent(Allocation.class)).thenReturn(TUndeploymentResourceContainerTest.testAllocation);

        /** get part of models */
        Mockito.when(ResourceEnvironmentModelBuilder.getResourceContainerByName(
                TUndeploymentResourceContainerTest.testResourceEnvironment, TUndeploymentResourceContainerTest.SERVICE))
                .thenReturn(TUndeploymentResourceContainerTest.optTestResourceContainer);

        // error: this throws nullPointer in TUndeployment
        Mockito.when(SystemModelBuilder.getAssemblyContextByName(TUndeploymentResourceContainerTest.testSystem,
                TUndeploymentResourceContainerTest.SERVICE))
                .thenReturn(TUndeploymentResourceContainerTest.optTestAssemblyContext);

        /** interaction with graphs */
        PowerMockito.doNothing().when(AllocationModelBuilder.class);
        AllocationModelBuilder.removeAllocationContext(TUndeploymentResourceContainerTest.testAllocation,
                TUndeploymentResourceContainerTest.testResourceContainer,
                TUndeploymentResourceContainerTest.testAssemblyContext);

        Mockito.doNothing().when(TUndeploymentResourceContainerTest.mockedAllocationModelGraphProvider)
                .updateComponent(Allocation.class, TUndeploymentResourceContainerTest.testAllocation);
    }

    /**
     * Check whether an undeployment event is triggered, when the needed resource container exists.
     * A servletUndeploymentEvent is defined as input.
     */
    @Test
    public void checkNoServletAllocationNeeded() {
        final List<IUndeploymentRecord> undeploymentEvents = new ArrayList<>();

        StageTester.test(this.tUndeployment).and().send(TUndeploymentResourceContainerTest.inputServletEvents)
                .to(this.tUndeployment.getInputPort()).and().receive(undeploymentEvents)
                .from(this.tUndeployment.getVisualizationOutputPort()).start();

        Assert.assertThat(undeploymentEvents.get(0),
                Is.is(TUndeploymentResourceContainerTest.servletUndeploymentEvent));
    }

    /**
     * Check whether a undeployment event is triggered, when the needed resource container exists.
     * An ejbUndeploymentEvent is defined as input.
     */
    @Test
    public void checkNoEjbAllocationNeeded() {
        final List<IUndeploymentRecord> undeploymentEvents = new ArrayList<>();

        StageTester.test(this.tUndeployment).and().send(TUndeploymentResourceContainerTest.inputEJBEvents)
                .to(this.tUndeployment.getInputPort()).and().receive(undeploymentEvents)
                .from(this.tUndeployment.getVisualizationOutputPort()).start();

        Assert.assertThat(undeploymentEvents.get(0), Is.is(TUndeploymentResourceContainerTest.ejbUndeploymentEvent));
    }

}
