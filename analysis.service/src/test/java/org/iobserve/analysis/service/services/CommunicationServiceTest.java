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
 * Tests for {@link CommunicationService}
 * 
 * @author jweg
 *
 */
public class CommunicationServiceTest {

    /** class under test */
    private CommunicationService communicationService;

    /** test data */
    private AssemblyConnector testAssemblyConnector;
    private AssemblyContext testSourceAssemblyContext;
    private AssemblyContext testTargetAssemblyContext;
    private String systemId;
    private String technology;

    /** expected result */
    private JsonObject expectedCommunication;

    /**
     * Prepare test data.
     */
    @Before
    public void setup() {

        this.communicationService = new CommunicationService();

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
        this.technology = "test_technology";

        /** expected result */
        this.expectedCommunication = Json.createObjectBuilder().add("type", "communication").add("id", "_test_id")
                .add("systemId", this.systemId).add("technology", "test_technology").add("sourceId", "_test_sourceId")
                .add("targetId", "_test_targetId").build();

    }

    /**
     * Check whether
     * {@link CommunicationService#createCommunication(AssemblyConnector, String, String)} works as
     * expected.
     */
    @Test
    public void checkCreateCommunication() {
        final JsonObject actualCommunication = this.communicationService.createCommunication(this.testAssemblyConnector,
                this.systemId, this.technology);

        Assert.assertEquals(this.expectedCommunication, actualCommunication);
    }

}
