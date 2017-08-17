package org.iobserve.analysis.service.services;

import javax.json.Json;
import javax.json.JsonObject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.CompositionFactory;

/**
 * Tests for {@link CommunicationInstanceService}
 * 
 * @author jweg
 *
 */
public class CommunicationInstanceServiceTest {

    /** class under test */
    private CommunicationInstanceService communicationInstanceService;

    /** test data */
    private AssemblyConnector testAssemblyConnector;
    private AssemblyContext testSourceAssemblyContext;
    private AssemblyContext testTargetAssemblyContext;
    private String systemId;
    private String communicationId;

    /** expected result */
    private JsonObject expectedCreateCommunicationInstance;
    private JsonObject expectedDeleteCommunicationInstance;

    /**
     * Prepare test data.
     */
    @Before
    public void setup() {
        this.communicationInstanceService = new CommunicationInstanceService();

        /** test data */
        this.testSourceAssemblyContext = CompositionFactory.eINSTANCE.createAssemblyContext();
        this.testSourceAssemblyContext.setId("_test_sourceId");
        this.testTargetAssemblyContext = CompositionFactory.eINSTANCE.createAssemblyContext();
        this.testTargetAssemblyContext.setId("_test_targetId");

        this.testAssemblyConnector = CompositionFactory.eINSTANCE.createAssemblyConnector();
        this.testAssemblyConnector.setId("_test_id");
        this.testAssemblyConnector.setProvidingAssemblyContext_AssemblyConnector(this.testSourceAssemblyContext);
        this.testAssemblyConnector.setRequiringAssemblyContext_AssemblyConnector(this.testTargetAssemblyContext);

        this.systemId = "test_systemId";
        this.communicationId = "test_communicationId";

        /** expected results */
        this.expectedCreateCommunicationInstance = Json.createObjectBuilder().add("type", "communicationInstance")
                .add("id", "ci_test_id").add("systemId", this.systemId).add("communicationId", this.communicationId)
                .add("workload", "1").add("sourceId", "si_test_sourceId").add("targetId", "si_test_targetId").build();

        this.expectedDeleteCommunicationInstance = Json.createObjectBuilder().add("type", "communicationInstance")
                .add("id", "ci_test_id").build();
    }

    /**
     * Check whether
     * {@link CommunicationInstanceService#createCommunicationInstance(AssemblyConnector, String, String)}
     * works as expected.
     */
    @Test
    public void checkCreateCommunicationInstance() {
        final JsonObject actualCreateCommunicationInstance = this.communicationInstanceService
                .createCommunicationInstance(this.testAssemblyConnector, this.systemId, this.communicationId);

        Assert.assertEquals(this.expectedCreateCommunicationInstance, actualCreateCommunicationInstance);

    }

    /**
     * Check whether
     * {@link CommunicationInstanceService#deleteCommunicationInstance(AssemblyConnector)} works as
     * expected.
     */
    @Test
    public void checkDeleteCommunicationInstance() {
        final JsonObject actualDeleteCommunicationInstance = this.communicationInstanceService
                .deleteCommunicationInstance(this.testAssemblyConnector);

        Assert.assertEquals(this.expectedDeleteCommunicationInstance, actualDeleteCommunicationInstance);
    }

}
