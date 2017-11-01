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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.palladiosimulator.pcm.system.SystemFactory;

/**
 * Tests for {@link SystemService}.
 *
 * @author Josefine Wegert
 *
 */
public class SystemServiceTest {

    /** class under test. */
    private SystemService systemService;

    /** test data. */
    private org.palladiosimulator.pcm.system.System testSystemModel;

    /** expected result. */
    private JsonObject expectedSystem;

    /**
     * Test setup.
     */
    @Before
    public void setup() {

        this.systemService = new SystemService();

        /** test data */
        this.testSystemModel = SystemFactory.eINSTANCE.createSystem();
        this.testSystemModel.setId("test_systemId");
        this.testSystemModel.setEntityName("test_systemName");

        /** expected result */
        this.expectedSystem = Json.createObjectBuilder().add("type", "system").add("id", "test_systemId")
                .add("name", "test_systemName").build();

    }

    /**
     * Check whether {@link SystemService#createSystem(org.palladiosimulator.pcm.system.System)}
     * works as expected.
     */
    @Test
    public void checkCreateSystem() {
        final JsonObject actualSystem = this.systemService.createSystem(this.testSystemModel);

        Assert.assertEquals(this.expectedSystem, actualSystem);
    }

}
