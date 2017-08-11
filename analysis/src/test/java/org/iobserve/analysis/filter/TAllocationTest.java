package org.iobserve.analysis.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hamcrest.core.Is;
import org.iobserve.analysis.model.ResourceEnvironmentModelBuilder;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.common.record.ContainerAllocationEvent;
import org.iobserve.common.record.IAllocationRecord;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import teetime.framework.test.StageTester;

/**
 * Tests for TAllocation filter.
 *
 * @author jweg
 *
 */
@RunWith(PowerMockRunner.class)
// write all final classes here
@PrepareForTest({ ResourceEnvironmentModelBuilder.class, ResourceEnvironmentModelProvider.class })
public class TAllocationTest {

    /** mocks */
    @Mock
    private static ModelProvider<ResourceEnvironment> mockedResourceEnvironmentModelGraphProvider;
    @Mock
    private static ResourceEnvironmentModelProvider mockedResourceEnvironmentModelProvider;

    /** data for generating test container allocation event */
    private static final String SERVICE = "test-service";
    private static final String CONTEXT = "/path/test";
    private static final String URL = "http://" + TAllocationTest.SERVICE + '/' + TAllocationTest.CONTEXT;

    /** test event */
    private static ContainerAllocationEvent allocationEvent;

    /** test resource containers */
    private static Optional<ResourceContainer> optTestNullResourceContainer;
    private static ResourceContainer testResourceContainer;
    private static Optional<ResourceContainer> optTestResourceContainer;

    private static ResourceEnvironment testResourceEnvironment;

    /***/
    private TAllocation tAllocation;
    private static List<IAllocationRecord> inputEvents = new ArrayList<>();

    /**
     * Initialize test event and stage under test (TDeployment) and therefore mock necessary
     * classes.
     */
    @BeforeClass
    public static void initializeTAllocationAndMock() {
        /** test event */
        TAllocationTest.allocationEvent = new ContainerAllocationEvent(TAllocationTest.URL);

        /** mock for old model provider */
        TAllocationTest.mockedResourceEnvironmentModelProvider = Mockito.mock(ResourceEnvironmentModelProvider.class);

        /** mock for new graph provider */
        TAllocationTest.mockedResourceEnvironmentModelGraphProvider = Mockito.mock(ModelProvider.class);

        /** input allocation event */
        TAllocationTest.inputEvents.add(TAllocationTest.allocationEvent);

        /** optional test resource container without value */
        TAllocationTest.optTestNullResourceContainer = Optional.ofNullable(null);
        /** optional test resource container with value */
        TAllocationTest.testResourceContainer = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
        TAllocationTest.testResourceContainer.setEntityName("TestResourceContainer");
        TAllocationTest.testResourceContainer.setId("_resourcecontainer_test_id");
        TAllocationTest.optTestResourceContainer = Optional.of(TAllocationTest.testResourceContainer);
        /** test resource environment */
        TAllocationTest.testResourceEnvironment = ResourceenvironmentFactory.eINSTANCE.createResourceEnvironment();

    }

    /**
     * Define the test situation in which a container allocation event is defined as input and the
     * specified resource container does not exist in the resource environment model.
     */
    @Before
    public void stubMocksNoResourceContainer() {

        this.tAllocation = new TAllocation(TAllocationTest.mockedResourceEnvironmentModelGraphProvider);

        /** mock for ResourceEnvironmentModelBuilder */
        // use PowerMockito for calling static methods of this final class
        PowerMockito.mockStatic(ResourceEnvironmentModelBuilder.class);

        // for new model graph provider
        Mockito.when(TAllocationTest.mockedResourceEnvironmentModelGraphProvider
                .readOnlyRootComponent(ResourceEnvironment.class)).thenReturn(TAllocationTest.testResourceEnvironment);

        Mockito.when(ResourceEnvironmentModelBuilder.getResourceContainerByName(TAllocationTest.testResourceEnvironment,
                TAllocationTest.SERVICE)).thenReturn(TAllocationTest.optTestNullResourceContainer);

        Mockito.when(ResourceEnvironmentModelBuilder.createResourceContainer(TAllocationTest.testResourceEnvironment,
                TAllocationTest.SERVICE)).thenReturn(TAllocationTest.testResourceContainer);

        Mockito.doNothing().when(TAllocationTest.mockedResourceEnvironmentModelGraphProvider)
                .updateComponent(ResourceEnvironment.class, TAllocationTest.testResourceEnvironment);
    }

    /**
     * Check whether the allocation event is forwarded to the next stage after the allocation is
     * finished.
     */
    @Test
    public void checkAllocationFinished() {
        final List<IAllocationRecord> allocationEvents = new ArrayList<>();

        StageTester.test(this.tAllocation).and().send(TAllocationTest.inputEvents).to(this.tAllocation.getInputPort())
                .and().receive(allocationEvents).from(this.tAllocation.getAllocationFinishedOutputPort()).start();

        Assert.assertThat(allocationEvents.get(0), Is.is(TAllocationTest.allocationEvent));
    }

    /**
     * Define the test situation in which a container allocation event is defined as input and the
     * specified resource container does not exist in the resource environment model.
     */
    @Before
    public void stubMocksNoResourceContainerCopy() {

        this.tAllocation = new TAllocation(TAllocationTest.mockedResourceEnvironmentModelGraphProvider);

        /** mock for ResourceEnvironmentModelBuilder */
        // use PowerMockito for calling static methods of this final class
        PowerMockito.mockStatic(ResourceEnvironmentModelBuilder.class);

        // for new model graph provider
        Mockito.when(TAllocationTest.mockedResourceEnvironmentModelGraphProvider
                .readOnlyRootComponent(ResourceEnvironment.class)).thenReturn(TAllocationTest.testResourceEnvironment);

        Mockito.when(ResourceEnvironmentModelBuilder.getResourceContainerByName(TAllocationTest.testResourceEnvironment,
                TAllocationTest.SERVICE)).thenReturn(TAllocationTest.optTestNullResourceContainer);

        Mockito.when(ResourceEnvironmentModelBuilder.createResourceContainer(TAllocationTest.testResourceEnvironment,
                TAllocationTest.SERVICE)).thenReturn(TAllocationTest.testResourceContainer);

        Mockito.doNothing().when(TAllocationTest.mockedResourceEnvironmentModelGraphProvider)
                .updateComponent(ResourceEnvironment.class, TAllocationTest.testResourceEnvironment);
    }

    /**
     * Check whether the allocation event is forwarded to the next stage after the allocation model
     * is updated.
     */
    @Test
    public void checkAllocationUpdate() {
        final List<IAllocationRecord> allocationEvents = new ArrayList<>();

        StageTester.test(this.tAllocation).and().send(TAllocationTest.inputEvents).to(this.tAllocation.getInputPort())
                .and().receive(allocationEvents).from(this.tAllocation.getAllocationOutputPort()).start();

        Assert.assertThat(allocationEvents.get(0), Is.is(TAllocationTest.allocationEvent));
    }

    /**
     * Define the test situation in which a container allocation event is defined as input and the
     * specified resource container does exist in the resource environment model.
     */
    @Before
    public void stubMocksResourceContainer() {

        this.tAllocation = new TAllocation(TAllocationTest.mockedResourceEnvironmentModelGraphProvider);

        // for new model graph provider
        Mockito.when(TAllocationTest.mockedResourceEnvironmentModelGraphProvider
                .readOnlyRootComponent(ResourceEnvironment.class)).thenReturn(TAllocationTest.testResourceEnvironment);

        Mockito.when(ResourceEnvironmentModelBuilder.getResourceContainerByName(TAllocationTest.testResourceEnvironment,
                TAllocationTest.SERVICE)).thenReturn(TAllocationTest.optTestResourceContainer);
    }

    /**
     * Check that no allocation event is forwarded, if the allocation model is not updated.
     */
    @Test
    public void checkNoAllocationUpdate() {
        final List<IAllocationRecord> allocationEvents = new ArrayList<>();

        StageTester.test(this.tAllocation).and().send(TAllocationTest.inputEvents).to(this.tAllocation.getInputPort())
                .and().receive(allocationEvents).from(this.tAllocation.getAllocationOutputPort()).start();

        // problem: optResourceContainer in TAllocation is empty!
        Assert.assertThat(allocationEvents.size(), Is.is(0));
    }

}
