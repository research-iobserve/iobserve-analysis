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
import org.palladiosimulator.pcm.allocation.AllocationFactory;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.CompositionFactory;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import teetime.framework.test.StageTester;

/**
 * Tests for TDeployment filter.
 *
 * @author jweg
 *
 */
@RunWith(PowerMockRunner.class)
// write all final classes here
@PrepareForTest({ ResourceEnvironmentModelBuilder.class, AllocationModelBuilder.class, AllocationModelProvider.class,
        SystemModelProvider.class, ResourceEnvironmentModelProvider.class })
public class TDeploymentTest {

    /** mocks */
    @Mock
    private static AllocationModelProvider mockedAllocationModelProvider;
    @Mock
    private static SystemModelProvider mockedSystemModelProvider;
    @Mock
    private static ResourceEnvironmentModelProvider mockedResourceEnvironmentModelProvider;
    @Mock
    private static ModelProvider<Allocation> mockedAllocationModelGraphProvider;
    @Mock
    private static ICorrespondence mockedCorrespondence;

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
    private TDeployment tDeployment;
    private static List<IDeploymentRecord> inputServletEvents = new ArrayList<>();
    private static List<IDeploymentRecord> inputEJBEvents = new ArrayList<>();

    /** test correspondent */
    private static Correspondent testCorrespondent;
    private static Optional<Correspondent> optTestCorrespondent;

    /** test resource containers */
    private static Optional<ResourceContainer> optTestNullResourceContainer;
    private static ResourceContainer testResourceContainer;
    private static Optional<ResourceContainer> optTestResourceContainer;

    /** test allocation */
    private static Allocation testAllocation;

    /** test assembly context */
    private static AssemblyContext testAssemblyContext;

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

        /** input deployment event */
        TDeploymentTest.inputServletEvents.add(TDeploymentTest.servletDeploymentEvent);
        TDeploymentTest.inputEJBEvents.add(TDeploymentTest.ejbDeploymentEvent);

        /** test correspondent */
        // TODO sinnvolle Werte
        TDeploymentTest.testCorrespondent = CorrespondentFactory.newInstance("test.org.pcm.entity", "pcmEntityId",
                "pcmOperationName", "pcmOperationId");
        TDeploymentTest.optTestCorrespondent = Optional.of(TDeploymentTest.testCorrespondent);

        /** optional test resource container without value */
        TDeploymentTest.optTestNullResourceContainer = Optional.ofNullable(null);
        /** optional test resource container with value */
        TDeploymentTest.testResourceContainer = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
        // TDeploymentTest.testResourceContainer.setEntityName("TestResourceContainer");
        // TDeploymentTest.testResourceContainer.setId("_resourcecontainer_test_id");
        TDeploymentTest.optTestResourceContainer = Optional.of(TDeploymentTest.testResourceContainer);

        /** test allocation */
        TDeploymentTest.testAllocation = AllocationFactory.eINSTANCE.createAllocation();

        /** test assembly context */
        TDeploymentTest.testAssemblyContext = CompositionFactory.eINSTANCE.createAssemblyContext();

    }

    /**
     * Define the test situation in which the needed resource container does not exist in the given
     * resource environment model. A servletDeploymentEvent is defined as input.
     */
    @Before
    public void stubMocksNoServletResourceContainer() {
        this.tDeployment = new TDeployment(TDeploymentTest.mockedCorrespondence,
                TDeploymentTest.mockedAllocationModelProvider, TDeploymentTest.mockedAllocationModelGraphProvider,
                TDeploymentTest.mockedSystemModelProvider, TDeploymentTest.mockedResourceEnvironmentModelProvider);

        Mockito.when(TDeploymentTest.mockedCorrespondence.getCorrespondent(TDeploymentTest.CONTEXT))
                .thenReturn(TDeploymentTest.optTestCorrespondent);

        /** mock for ResourceEnvironmentModelBuilder */
        // use PowerMockito for calling static methods of this final class
        PowerMockito.mockStatic(ResourceEnvironmentModelBuilder.class);

        Mockito.when(ResourceEnvironmentModelBuilder.getResourceContainerByName(
                TDeploymentTest.mockedResourceEnvironmentModelProvider.getModel(), TDeploymentTest.SERVICE))
                .thenReturn(TDeploymentTest.optTestNullResourceContainer);
    }

    /**
     * Check whether an allocation event is triggered, if the needed resource container does not
     * exist yet.
     */
    @Test
    public void checkServletAllocationNeeded() {
        final List<IAllocationRecord> allocationEvents = new ArrayList<>();

        StageTester.test(this.tDeployment).and().send(TDeploymentTest.inputServletEvents)
                .to(this.tDeployment.getInputPort()).and().receive(allocationEvents)
                .from(this.tDeployment.getAllocationOutputPort()).start();

        Assert.assertThat(allocationEvents.get(0), Is.is(TDeploymentTest.allocationEvent));
    }

    /**
     * Define the test situation in which the needed resource container does not exist in the given
     * resource environment model. A servletDeploymentEvent is defined as input.
     */

    // TODO This is a copy of one @Before above. Can I check allocation- and deploymentEvents in one
    // @Test?
    @Before
    public void stubMocksNoServletResourceContainerCopy() {
        this.tDeployment = new TDeployment(TDeploymentTest.mockedCorrespondence,
                TDeploymentTest.mockedAllocationModelProvider, TDeploymentTest.mockedAllocationModelGraphProvider,
                TDeploymentTest.mockedSystemModelProvider, TDeploymentTest.mockedResourceEnvironmentModelProvider);

        Mockito.when(TDeploymentTest.mockedCorrespondence.getCorrespondent(TDeploymentTest.CONTEXT))
                .thenReturn(TDeploymentTest.optTestCorrespondent);

        /** mock for ResourceEnvironmentModelBuilder */
        // use PowerMockito for calling static methods of this final class
        PowerMockito.mockStatic(ResourceEnvironmentModelBuilder.class);

        Mockito.when(ResourceEnvironmentModelBuilder.getResourceContainerByName(
                TDeploymentTest.mockedResourceEnvironmentModelProvider.getModel(), TDeploymentTest.SERVICE))
                .thenReturn(TDeploymentTest.optTestNullResourceContainer);
    }

    /**
     * Check whether an deployment event is forwarded, if the needed resource container does not
     * exist yet.
     */
    @Test
    public void checkServletDeploymentAfterAllocationNeeded() {
        final List<IDeploymentRecord> deploymentEvents = new ArrayList<>();

        StageTester.test(this.tDeployment).and().send(TDeploymentTest.inputServletEvents)
                .to(this.tDeployment.getInputPort()).and().receive(deploymentEvents)
                .from(this.tDeployment.getDeploymentOutputPort()).start();

        Assert.assertThat(deploymentEvents.get(0), Is.is(TDeploymentTest.servletDeploymentEvent));

    }

    /**
     * Define the test situation in which the needed resource container does exist in the given
     * resource environment model. A servletDeploymentEvent is defined as input.
     */
    @Before
    public void stubMocksResourceContainer() {
        this.tDeployment = new TDeployment(TDeploymentTest.mockedCorrespondence,
                TDeploymentTest.mockedAllocationModelProvider, TDeploymentTest.mockedAllocationModelGraphProvider,
                TDeploymentTest.mockedSystemModelProvider, TDeploymentTest.mockedResourceEnvironmentModelProvider);

        Mockito.when(TDeploymentTest.mockedCorrespondence.getCorrespondent(TDeploymentTest.CONTEXT))
                .thenReturn(TDeploymentTest.optTestCorrespondent);

        /** mock for ResourceEnvironmentModelBuilder */
        // use PowerMockito for calling static methods of this final class
        PowerMockito.mockStatic(ResourceEnvironmentModelBuilder.class);
        PowerMockito.mockStatic(AllocationModelBuilder.class);

        Mockito.when(ResourceEnvironmentModelBuilder.getResourceContainerByName(
                TDeploymentTest.mockedResourceEnvironmentModelProvider.getModel(), TDeploymentTest.SERVICE))
                .thenReturn(TDeploymentTest.optTestResourceContainer);

        // new: updating the allocation graph
        // final Allocation allocationModel =
        // this.allocationModelGraphProvider.readOnlyRootComponent(Allocation.class);
        // AllocationModelBuilder.addAllocationContextIfAbsent(allocationModel, resourceContainer,
        // assemblyContext);
        // this.allocationModelGraphProvider.updateComponent(Allocation.class, allocationModel);
        // Mockito.when(TDeployment.createAssemblyContextByName(TDeploymentTest.mockedSystemModelProvider,
        // TDeploymentTest.SERVICE)).thenReturn(TDeploymentTest.testAssemblyContext);

        Mockito.when(TDeploymentTest.mockedAllocationModelGraphProvider.readOnlyRootComponent(Allocation.class))
                .thenReturn(TDeploymentTest.testAllocation);

        // Mockito.doNothing().when(AllocationModelBuilder.class).addAllocationContextIfAbsent(
        // TDeploymentTest.testAllocation, TDeploymentTest.testResourceContainer,
        // TDeploymentTest.testAssemblyContext);

        Mockito.doNothing().when(TDeploymentTest.mockedAllocationModelGraphProvider).updateComponent(Allocation.class,
                TDeploymentTest.testAllocation);
    }

    /**
     * Check whether a deployment event is triggered, when the needed resource container does exist.
     */
    // problem: optTestResourceContainer is empty in TDeployment
    @Test
    public void checkNoServletAllocationNeeded() {
        final List<IDeploymentRecord> deploymentEvents = new ArrayList<>();

        StageTester.test(this.tDeployment).and().send(TDeploymentTest.inputServletEvents)
                .to(this.tDeployment.getInputPort()).and().receive(deploymentEvents)
                .from(this.tDeployment.getDeploymentFinishedOutputPort()).start();

        Assert.assertThat(deploymentEvents.get(0), Is.is(TDeploymentTest.servletDeploymentEvent));
    }

}
