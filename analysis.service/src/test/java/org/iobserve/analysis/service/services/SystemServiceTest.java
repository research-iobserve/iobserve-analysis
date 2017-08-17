package org.iobserve.analysis.service.services;

import javax.json.Json;
import javax.json.JsonObject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.palladiosimulator.pcm.system.SystemFactory;

/**
 * Tests for {@link SystemService}
 * 
 * @author jweg
 *
 */
public class SystemServiceTest {

    /** class under test */
    private SystemService systemService;

    /** test data */
    private org.palladiosimulator.pcm.system.System testSystemModel;

    /** expected result */
    private JsonObject expectedSystem;

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
