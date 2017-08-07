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
import org.mockito.Mockito;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;

import teetime.framework.test.StageTester;

/**
 * Tests for TAllocation filter.
 *
 * @author jweg
 *
 */
public class TAllocationTest {
    /** mocks */
    private static ModelProvider<ResourceEnvironment> mockedResourceEnvironmentModelGraphProvider;
    private static ResourceEnvironmentModelProvider mockedResourceEnvironmentModelProvider;
    /** data for generating test container allocation event */
    private static final String SERVICE = "test-service";
    private static final String CONTEXT = "/path/test";
    private static final String URL = "http://" + TAllocationTest.SERVICE + '/' + TAllocationTest.CONTEXT;
    /** test event */
    private static ContainerAllocationEvent allocationEvent;

    private static TAllocation tAllocation;
    private final List<IAllocationRecord> inputEvents = new ArrayList<>();

    /**
     * Initialize test event and stage under test (TDeployment) and therefore mock necessary
     * classes.
     */
    @BeforeClass
    public static void initializeTAllocationAndMock() {
        TAllocationTest.allocationEvent = new ContainerAllocationEvent(TAllocationTest.URL);

        /** mock for old model provider */
        TAllocationTest.mockedResourceEnvironmentModelProvider = Mockito.mock(ResourceEnvironmentModelProvider.class);

        /** mock for new graph provider */
        TAllocationTest.mockedResourceEnvironmentModelGraphProvider = Mockito.mock(ModelProvider.class);

        TAllocationTest.tAllocation = new TAllocation(TAllocationTest.mockedResourceEnvironmentModelProvider,
                TAllocationTest.mockedResourceEnvironmentModelGraphProvider);

    }

    /**
     * Define the test situation in which a container allocation event is defined as input and the
     * specified resource container does not exist in the resource environment model.
     */
    @Before
    public void stubMocksNoResourceContainer() {
        this.inputEvents.add(TAllocationTest.allocationEvent);

        Mockito.when(ResourceEnvironmentModelBuilder.getResourceContainerByName(
                TAllocationTest.mockedResourceEnvironmentModelProvider.getModel(), TAllocationTest.SERVICE))
                .thenReturn(null);
    }

    /**
     * Check whether the allocation event is forwarded to the next stage after the allocation is
     * finished.
     */
    @Test
    public void checkAllocationFinished() {
        final List<IAllocationRecord> allocationEvents = new ArrayList<>();
        StageTester.test(TAllocationTest.tAllocation).and().send(this.inputEvents)
                .to(TAllocationTest.tAllocation.getInputPort()).and().receive(allocationEvents)
                .from(TAllocationTest.tAllocation.getAllocationFinishedOutputPort()).start();
        Assert.assertThat(allocationEvents, Is.is(TAllocationTest.allocationEvent));
    }

    /**
     * Check whether the allocation event is forwarded to the next stage after the allocation model
     * is updated.
     */
    @Test
    public void checkAllocationUpdate() {
        final List<IAllocationRecord> allocationEvents = new ArrayList<>();
        StageTester.test(TAllocationTest.tAllocation).and().send(this.inputEvents)
                .to(TAllocationTest.tAllocation.getInputPort()).and().receive(allocationEvents)
                .from(TAllocationTest.tAllocation.getAllocationOutputPort()).start();
        Assert.assertThat(allocationEvents, Is.is(TAllocationTest.allocationEvent));
    }

    /**
     * Define the test situation in which a container allocation event is defined as input and the
     * specified resource container does exist in the resource environment model.
     */
    @Before
    public void stubMocksResourceContainer() {
        this.inputEvents.add(TAllocationTest.allocationEvent);

        /** test resource container */
        final ResourceContainer testResourceContainer = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
        final Optional<ResourceContainer> optTestResourceContainer = Optional.of(testResourceContainer);

        Mockito.when(ResourceEnvironmentModelBuilder.getResourceContainerByName(
                TAllocationTest.mockedResourceEnvironmentModelProvider.getModel(), TAllocationTest.SERVICE))
                .thenReturn(optTestResourceContainer);
    }

}
