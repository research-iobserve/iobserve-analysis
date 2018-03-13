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

import javax.json.Json;
import javax.json.JsonObject;

import org.iobserve.analysis.sink.landscape.ServiceService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.CompositionFactory;

/**
 * Tests for {@link ServiceService}.
 *
 * @author Josefine Wegert
 *
 */
public class ServiceServiceTest { // NOCS test

    /** class under test. */
    private ServiceService serviceService;

    /** test data. */
    private AssemblyContext testAssemblyContext;
    private String systemId;

    /** expected result. */
    private JsonObject expectedService;

    /**
     * Prepare test data.
     */
    @Before
    public void setup() {
        this.serviceService = new ServiceService();

        /** test data */
        this.testAssemblyContext = CompositionFactory.eINSTANCE.createAssemblyContext();
        this.testAssemblyContext.setId("_test_serviceId");
        this.testAssemblyContext.setEntityName("test_serviceName");

        this.systemId = "test_systemId";

        /** expected result */
        this.expectedService = Json.createObjectBuilder().add("type", "service").add("id", "_test_serviceId")
                .add("systemId", "test_systemId").add("name", "test_serviceName").build();
    }

    /**
     * Check whether {@link ServiceService#createService(AssemblyContext, String)} works as
     * expected.
     */
    @Test
    public void test() {
        final JsonObject actualService = this.serviceService.createService(this.testAssemblyContext, this.systemId);

        Assert.assertEquals(this.expectedService, actualService);
    }

}
