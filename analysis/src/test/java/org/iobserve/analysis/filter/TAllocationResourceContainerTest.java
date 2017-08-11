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
import org.mockito.Mockito;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import teetime.framework.test.StageTester;

/**
 * Tests for TAllocation filter, when the resource container already exists.
 *
 * @author jweg
 *
 */
@RunWith(PowerMockRunner.class)
// write all final classes here
@PrepareForTest({ ResourceEnvironmentModelBuilder.class, ResourceEnvironmentModelProvider.class })
public class TAllocationResourceContainerTest {

    /** mocks */
    private static ModelProvider<ResourceEnvironment> mockedResourceEnvironmentModelGraphProvider;

    /** data for generating test container allocation event */
    private static final String SERVICE = "test-service";
    private static final String CONTEXT = "/path/test";
    private static final String URL = "http://" + TAllocationResourceContainerTest.SERVICE + '/'
            + TAllocationResourceContainerTest.CONTEXT;

    /** test event */
    private static ContainerAllocationEvent allocationEvent;

    /** test resource containers */
    private static ResourceContainer testResourceContainer;
    private static Optional<ResourceContainer> optTestResourceContainer;

    private static ResourceEnvironment testResourceEnvironment;

    /***/
    private TAllocation tAllocation;
    private static List<IAllocationRecord> inputEvents = new ArrayList<>();

    /**
     * Initialize test data.
     */
    @BeforeClass
    public static void initializeTAllocationAndMock() {

        /** test event */
        TAllocationResourceContainerTest.allocationEvent = new ContainerAllocationEvent(
                TAllocationResourceContainerTest.URL);

        /** input allocation event */
        TAllocationResourceContainerTest.inputEvents.add(TAllocationResourceContainerTest.allocationEvent);

        /** optional test resource container with value */
        TAllocationResourceContainerTest.testResourceContainer = ResourceenvironmentFactory.eINSTANCE
                .createResourceContainer();
        TAllocationResourceContainerTest.testResourceContainer.setEntityName("TestResourceContainer");
        TAllocationResourceContainerTest.testResourceContainer.setId("_resourcecontainer_test_id");
        TAllocationResourceContainerTest.optTestResourceContainer = Optional
                .of(TAllocationResourceContainerTest.testResourceContainer);
        /** test resource environment */
        TAllocationResourceContainerTest.testResourceEnvironment = ResourceenvironmentFactory.eINSTANCE
                .createResourceEnvironment();

    }

    /**
     * Define the test situation in which a container allocation event is defined as input and the
     * specified resource container does exist in the resource environment model.
     */
    @Before
    public void stubMocksResourceContainer() {

        /** mock for ResourceEnvironmentModelBuilder */
        // use PowerMockito for calling static methods of this final class
        PowerMockito.mockStatic(ResourceEnvironmentModelBuilder.class);
        /** mock for new graph provider */
        TAllocationResourceContainerTest.mockedResourceEnvironmentModelGraphProvider = Mockito
                .mock(ModelProvider.class);

        this.tAllocation = new TAllocation(
                TAllocationResourceContainerTest.mockedResourceEnvironmentModelGraphProvider);

        // for new model graph provider
        Mockito.when(TAllocationResourceContainerTest.mockedResourceEnvironmentModelGraphProvider
                .readOnlyRootComponent(ResourceEnvironment.class))
                .thenReturn(TAllocationResourceContainerTest.testResourceEnvironment);

        Mockito.when(
                ResourceEnvironmentModelBuilder.getResourceContainerByName(
                        TAllocationResourceContainerTest.mockedResourceEnvironmentModelGraphProvider
                                .readOnlyRootComponent(ResourceEnvironment.class),
                        TAllocationResourceContainerTest.SERVICE))
                .thenReturn(TAllocationResourceContainerTest.optTestResourceContainer);
    }

    /**
     * Check that no allocation event is forwarded, if the allocation model is not updated.
     */
    @Test
    public void checkNoAllocationUpdate() {

        final List<IAllocationRecord> noAllocationEvents = new ArrayList<>();

        StageTester.test(this.tAllocation).and().send(TAllocationResourceContainerTest.inputEvents)
                .to(this.tAllocation.getInputPort()).and().receive(noAllocationEvents)
                .from(this.tAllocation.getAllocationOutputPort()).start();

        Assert.assertThat(noAllocationEvents.size(), Is.is(0));
    }

}
