package org.iobserve.analysis.service.services;

import javax.json.Json;
import javax.json.JsonObject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;

/**
 * Tests for {@link NodeService}
 *
 * @author jweg
 *
 */
public class NodeServiceTest {

    /** class under test */
    private NodeService nodeService;

    /** test data */
    private ResourceContainer testResourceContainer;
    private String testSystemId;
    private String testNodegroupId;

    /** expected result */
    private JsonObject expectedNode;

    /**
     * Prepare test data.
     */
    @Before
    public void setup() {

        this.nodeService = new NodeService();

        /** test data */
        this.testResourceContainer = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
        this.testResourceContainer.setId("test_id");
        this.testResourceContainer.setEntityName("test_name");

        this.testSystemId = "test_systemId";
        this.testNodegroupId = "test_nodegroupId";

        /** expected result */
        this.expectedNode = Json.createObjectBuilder().add("type", "node").add("id", "test_id")
                .add("systemId", this.testSystemId).add("nodeGroupId", this.testNodegroupId)
                .add("hostname", "test_name").build();

    }

    /**
     * Checks whether {@link NodeService#createNode(ResourceContainer,String,String)} works as
     * expected.
     */
    @Test
    public void checkCreateNode() {

        final JsonObject actualNode = this.nodeService.createNode(this.testResourceContainer, this.testSystemId,
                this.testNodegroupId);

        Assert.assertEquals(this.expectedNode, actualNode);
    }

}
