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
package org.iobserve.analysis.service.services;

import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;

import org.eclipse.emf.ecore.EObject;
import org.iobserve.analysis.sink.landscape.CommunicationInstanceService;
import org.iobserve.analysis.sink.landscape.ServiceInstanceService;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.CompositionFactory;

/**
 * Tests for {@link ServiceInstanceService}.
 *
 * @author Josefine Wegert
 *
 */
@RunWith(MockitoJUnitRunner.class) // NOCS test
public class ServiceInstanceServiceTest { // NOCS test

    /** class under test. */
    private ServiceInstanceService serviceInstanceService;

    /** mocks. */
    @Mock
    private ModelProvider<org.palladiosimulator.pcm.system.System> mockedSystemModelGraphProvider;
    @Mock
    private CommunicationInstanceService mockedCommunicationInstanceService;

    /** test data. */
    private AssemblyContext testAssemblyContext;
    private String systemId;
    private String nodeId;
    private String serviceId;

    private List<EObject> noAssemblyConnectors;

    /** expected result. */
    private JsonObject expectedCreateServiceInstance;
    private JsonObject expectedDeleteServiceInstance;

    /**
     * Prepare test data.
     */
    @Before
    public void setup() {

        this.serviceInstanceService = new ServiceInstanceService();

        /** test data */
        this.testAssemblyContext = CompositionFactory.eINSTANCE.createAssemblyContext();
        this.testAssemblyContext.setId("_test_serviceId");
        this.testAssemblyContext.setEntityName("test_serviceName");

        this.systemId = "test_systemId";
        this.nodeId = "test_nodeId";
        this.serviceId = "test_serviceId";

        this.noAssemblyConnectors = new ArrayList<>();
        /** expected result */
        this.expectedCreateServiceInstance = Json.createObjectBuilder().add("type", "serviceInstance")
                .add("id", "si_test_serviceId").add("systemId", this.systemId).add("name", "test_serviceName")
                .add("serviceId", this.serviceId).add("nodeId", this.nodeId).build();
        this.expectedDeleteServiceInstance = Json.createObjectBuilder().add("type", "serviceInstance")
                .add("id", "si_test_serviceId").add("systemId", this.systemId).add("serviceId", "_test_serviceId")
                .add("nodeId", this.nodeId).build();

        // stubbing
        Mockito.when(this.mockedSystemModelGraphProvider.readOnlyReferencingComponentsById(AssemblyContext.class,
                this.testAssemblyContext.getId())).thenReturn(this.noAssemblyConnectors);
    }

    /**
     * Check whether
     * {@link ServiceInstanceService#createServiceInstance(AssemblyContext, String, String, String)}
     * builds the JsonObject for serviceInstance right.
     */
    @Test
    public void checkCreateServiceInstance() {
        final JsonObject actualServiceInstance = this.serviceInstanceService
                .createServiceInstance(this.testAssemblyContext, this.systemId, this.nodeId, this.serviceId);

        Assert.assertEquals(this.expectedCreateServiceInstance, actualServiceInstance);
    }

    /**
     * Check whether
     * {@link ServiceInstanceService#deleteServiceInstance(AssemblyContext, String, String, org.iobserve.analysis.model.provider.neo4j.ModelProvider)}
     * works as expected, when the ServiceInstance is not referenced by communicationInstances.
     */
    // TODO test when serviceInstance is referenced by communicationInstance
    @Test
    public void checkDeleteServiceInstance() {
        final JsonObject actualDeleteServiceInstance = this.serviceInstanceService.deleteServiceInstance(
                this.testAssemblyContext, this.systemId, this.nodeId, this.mockedSystemModelGraphProvider);

        Assert.assertEquals(this.expectedDeleteServiceInstance, actualDeleteServiceInstance);

    }

}
