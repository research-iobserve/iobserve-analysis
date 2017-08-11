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
import org.iobserve.common.record.EJBDeployedEvent;
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
 * Tests for TDeployment filter, in case the resource container exists already.
 *
 * @author jweg
 *
 */
@RunWith(PowerMockRunner.class)
// write all final classes here
@PrepareForTest({ ResourceEnvironmentModelBuilder.class, AllocationModelBuilder.class, SystemModelBuilder.class,
        AllocationModelProvider.class, SystemModelProvider.class, ResourceEnvironmentModelProvider.class })
public class TDeploymentResourceContainerTest {

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
    private static final String URL = "http://" + TDeploymentResourceContainerTest.SERVICE + '/'
            + TDeploymentResourceContainerTest.CONTEXT;

    /** test events */
    private static ServletDeployedEvent servletDeploymentEvent;
    private static EJBDeployedEvent ejbDeploymentEvent;

    /***/
    private TDeployment tDeployment;
    private static List<IDeploymentRecord> inputServletEvents = new ArrayList<>();
    private static List<IDeploymentRecord> inputEJBEvents = new ArrayList<>();

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
        TDeploymentResourceContainerTest.servletDeploymentEvent = new ServletDeployedEvent(
                TDeploymentResourceContainerTest.DEPLOY_TIME, TDeploymentResourceContainerTest.SERVICE,
                TDeploymentResourceContainerTest.CONTEXT, TDeploymentResourceContainerTest.DEPLOYMENT_ID);
        TDeploymentResourceContainerTest.ejbDeploymentEvent = new EJBDeployedEvent(
                TDeploymentResourceContainerTest.DEPLOY_TIME, TDeploymentResourceContainerTest.SERVICE,
                TDeploymentResourceContainerTest.CONTEXT, TDeploymentResourceContainerTest.DEPLOYMENT_ID);

        /** mocks for model graph provider */
        TDeploymentResourceContainerTest.mockedSystemModelGraphProvider = Mockito.mock(ModelProvider.class);
        TDeploymentResourceContainerTest.mockedResourceEnvironmentModelGraphProvider = Mockito
                .mock(ModelProvider.class);
        TDeploymentResourceContainerTest.mockedAllocationModelGraphProvider = Mockito.mock(ModelProvider.class);

        /** mock for correspondence model */
        TDeploymentResourceContainerTest.mockedCorrespondence = Mockito.mock(ICorrespondence.class);

        /** input deployment event */
        TDeploymentResourceContainerTest.inputServletEvents
                .add(TDeploymentResourceContainerTest.servletDeploymentEvent);
        TDeploymentResourceContainerTest.inputEJBEvents.add(TDeploymentResourceContainerTest.ejbDeploymentEvent);

        /** test correspondent */
        TDeploymentResourceContainerTest.testCorrespondent = CorrespondentFactory.newInstance("test.org.pcm.entity",
                "testPcmEntityId", "testPcmOperationName", "testPcmOperationId");
        TDeploymentResourceContainerTest.optTestCorrespondent = Optional
                .of(TDeploymentResourceContainerTest.testCorrespondent);

        /** test allocation */
        TDeploymentResourceContainerTest.testAllocation = AllocationFactory.eINSTANCE.createAllocation();

        /** test resourceEnvironment */
        TDeploymentResourceContainerTest.testResourceEnvironment = ResourceenvironmentFactory.eINSTANCE
                .createResourceEnvironment();

        /** test system */
        TDeploymentResourceContainerTest.testSystem = SystemFactory.eINSTANCE.createSystem();

        /** optional test resource container with value */
        TDeploymentResourceContainerTest.testResourceContainer = ResourceenvironmentFactory.eINSTANCE
                .createResourceContainer();
        TDeploymentResourceContainerTest.testResourceContainer.setEntityName("TestResourceContainer");
        TDeploymentResourceContainerTest.testResourceContainer.setId("_resourcecontainer_test_id");
        TDeploymentResourceContainerTest.optTestResourceContainer = Optional
                .of(TDeploymentResourceContainerTest.testResourceContainer);

        /** optional test assembly context */
        TDeploymentResourceContainerTest.testAssemblyContext = CompositionFactory.eINSTANCE.createAssemblyContext();
        TDeploymentResourceContainerTest.testAssemblyContext.setEntityName("TestAssemblyContext");
        TDeploymentResourceContainerTest.testAssemblyContext.setId("_assemblycontext_test_id");
        TDeploymentResourceContainerTest.optTestAssemblyContext = Optional
                .of(TDeploymentResourceContainerTest.testAssemblyContext);

    }

    /**
     * Define the test situation in which the needed resource container exists in the given resource
     * environment model.
     */
    @Before
    public void stubMocksResourceContainer() {

        /** mock for ModelBuilder */
        // use PowerMockito for calling static methods of these final classes
        PowerMockito.mockStatic(ResourceEnvironmentModelBuilder.class);
        PowerMockito.mockStatic(AllocationModelBuilder.class);
        PowerMockito.mockStatic(SystemModelBuilder.class);

        this.tDeployment = new TDeployment(TDeploymentResourceContainerTest.mockedCorrespondence,
                TDeploymentResourceContainerTest.mockedAllocationModelGraphProvider,
                TDeploymentResourceContainerTest.mockedSystemModelGraphProvider,
                TDeploymentResourceContainerTest.mockedResourceEnvironmentModelGraphProvider);

        /** get models */
        Mockito.when(TDeploymentResourceContainerTest.mockedCorrespondence
                .getCorrespondent(TDeploymentResourceContainerTest.CONTEXT))
                .thenReturn(TDeploymentResourceContainerTest.optTestCorrespondent);

        Mockito.when(TDeploymentResourceContainerTest.mockedAllocationModelGraphProvider
                .readOnlyRootComponent(Allocation.class)).thenReturn(TDeploymentResourceContainerTest.testAllocation);

        Mockito.when(TDeploymentResourceContainerTest.mockedResourceEnvironmentModelGraphProvider
                .readOnlyRootComponent(ResourceEnvironment.class))
                .thenReturn(TDeploymentResourceContainerTest.testResourceEnvironment);

        Mockito.when(TDeploymentResourceContainerTest.mockedSystemModelGraphProvider
                .readOnlyRootComponent(org.palladiosimulator.pcm.system.System.class))
                .thenReturn(TDeploymentResourceContainerTest.testSystem);

        /** get part of models */
        final String asmCtxName = TDeploymentResourceContainerTest.testCorrespondent.getPcmEntityName() + "_"
                + TDeploymentResourceContainerTest.SERVICE;

        Mockito.when(
                SystemModelBuilder.getAssemblyContextByName(TDeploymentResourceContainerTest.testSystem, asmCtxName))
                .thenReturn(TDeploymentResourceContainerTest.optTestAssemblyContext);

        Mockito.when(ResourceEnvironmentModelBuilder.getResourceContainerByName(
                TDeploymentResourceContainerTest.testResourceEnvironment, TDeploymentResourceContainerTest.SERVICE))
                .thenReturn(TDeploymentResourceContainerTest.optTestResourceContainer);

        /** interaction with graphs */
        Mockito.when(SystemModelBuilder.createAssemblyContextsIfAbsent(TDeploymentResourceContainerTest.testSystem,
                TDeploymentResourceContainerTest.SERVICE))
                .thenReturn(TDeploymentResourceContainerTest.testAssemblyContext);

        Mockito.doNothing().when(TDeploymentResourceContainerTest.mockedSystemModelGraphProvider).updateComponent(
                org.palladiosimulator.pcm.system.System.class, TDeploymentResourceContainerTest.testSystem);

        PowerMockito.doNothing().when(AllocationModelBuilder.class);
        AllocationModelBuilder.addAllocationContextIfAbsent(TDeploymentResourceContainerTest.testAllocation,
                TDeploymentResourceContainerTest.testResourceContainer,
                TDeploymentResourceContainerTest.testAssemblyContext);

        Mockito.doNothing().when(TDeploymentResourceContainerTest.mockedAllocationModelGraphProvider)
                .updateComponent(Allocation.class, TDeploymentResourceContainerTest.testAllocation);
    }

    /**
     * Check whether a deployment event is triggered, when the needed resource container exists. A
     * servletDeploymentEvent is defined as input.
     */
    @Test
    public void checkNoServletAllocationNeeded() {
        final List<IDeploymentRecord> deploymentEvents = new ArrayList<>();

        StageTester.test(this.tDeployment).and().send(TDeploymentResourceContainerTest.inputServletEvents)
                .to(this.tDeployment.getInputPort()).and().receive(deploymentEvents)
                .from(this.tDeployment.getDeploymentFinishedOutputPort()).start();

        Assert.assertThat(deploymentEvents.get(0), Is.is(TDeploymentResourceContainerTest.servletDeploymentEvent));
    }

    /**
     * Check whether a deployment event is triggered, when the needed resource container exists. An
     * ejbDeploymentEvent is defined as input.
     */
    @Test
    public void checkNoEjbAllocationNeeded() {
        final List<IDeploymentRecord> deploymentEvents = new ArrayList<>();

        StageTester.test(this.tDeployment).and().send(TDeploymentResourceContainerTest.inputEJBEvents)
                .to(this.tDeployment.getInputPort()).and().receive(deploymentEvents)
                .from(this.tDeployment.getDeploymentFinishedOutputPort()).start();

        Assert.assertThat(deploymentEvents.get(0), Is.is(TDeploymentResourceContainerTest.ejbDeploymentEvent));
    }

}
