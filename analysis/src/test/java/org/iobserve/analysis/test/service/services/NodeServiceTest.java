/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.analysis.test.service.services;

import javax.json.Json;
import javax.json.JsonObject;

import org.iobserve.analysis.sink.landscape.NodeService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;

/**
 * Tests for {@link NodeService}.
 *
 * @author Josefine Wegert
 *
 */
public class NodeServiceTest { // NOCS test

    /** class under test. */
    private NodeService nodeService;

    /** test data. */
    private ResourceContainer testResourceContainer;
    private String testSystemId;
    private String testNodegroupId;

    /** expected result. */
    private JsonObject expectedNode;

    /**
     * Prepare test data.
     */
    @Before
    public void setUp() {

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
