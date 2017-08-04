package org.iobserve.analysis.filter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.core.Is;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.modelneo4j.Graph;
import org.iobserve.analysis.modelneo4j.GraphLoader;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.common.record.ContainerAllocationEvent;
import org.iobserve.common.record.IAllocationRecord;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

import teetime.framework.test.StageTester;

public class TAllocationTest {
    // Das eher für Integrationstest
    /** path to test resource environment model */
    private static final File resourceEnvironmentFile = new File(
            "file:///home/jweg/models/WorkingTestPCM/test_pcm/models/test.resourceenvironment");
    /** path to directory for test resource environment graph */
    private static final File resourceEnvironmentGraphDir = new File(
            "file:///home/jweg/models/WorkingTestPCM/test_pcm/graphs/");

    /** data for generating test container allocation event */
    private static final String SERVICE = "test-service";
    private static final String CONTEXT = "/path/test";
    private static final String URL = "http://" + TAllocationTest.SERVICE + '/' + TAllocationTest.CONTEXT;
    /***/
    private static ResourceEnvironmentModelProvider resourceEnvironmentModelProvider;
    private static ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider;
    private TAllocation tAllocation;
    private final List<IAllocationRecord> inputEvents = new ArrayList<>();
    private final List<IAllocationRecord> resultEvents = new ArrayList<>();

    @BeforeClass
    public static void loadResourceEnvironmentModel() {
        // old model
        final InitializeModelProviders modelProvider = new InitializeModelProviders(
                TAllocationTest.resourceEnvironmentFile);
        TAllocationTest.resourceEnvironmentModelProvider = modelProvider.getResourceEnvironmentModelProvider();
        // new graph
        final GraphLoader graphLoader = new GraphLoader(TAllocationTest.resourceEnvironmentGraphDir);
        final Graph resourceEnvironmentModelGraph = graphLoader
                .initializeResourceEnvironmentModelGraph(TAllocationTest.resourceEnvironmentModelProvider.getModel());
        TAllocationTest.resourceEnvironmentModelGraphProvider = new ModelProvider<>(resourceEnvironmentModelGraph);
    }

    @Before
    public void initializeTAllocation() {
        this.tAllocation = new TAllocation(TAllocationTest.resourceEnvironmentModelProvider,
                TAllocationTest.resourceEnvironmentModelGraphProvider);
        final ContainerAllocationEvent allocationEvent = new ContainerAllocationEvent(TAllocationTest.URL);
        this.inputEvents.add(allocationEvent);
        // zweimal hinzufügen, weil es an zwei OutputPorts anliegt?
        this.resultEvents.add(allocationEvent);
    }

    @Test
    public void checkEventCount() {
        StageTester.test(this.tAllocation).and().send(this.inputEvents).to(this.tAllocation.getInputPort()).start();
        Assert.assertThat(this.tAllocation.getRecordCount(), Is.is(this.inputEvents.size()));
    }

    // IAllocationRecord mit neuem ResourceContainer rein, testen:Ist der neue ResourceContainer
    // dazugekommen? -> Integrationstest
    // IAllocationRecord mit neuem ResourceContainer rein und gucken, ob selber IAllocationRecord
    // wieder raus geht?

}
