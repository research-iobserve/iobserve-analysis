package org.iobserve.analysis.service.services;

import javax.json.Json;
import javax.json.JsonObject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.CompositionFactory;

/**
 * Tests for {@link ServiceService}
 *
 * @author jweg
 *
 */
public class ServiceServiceTest {

    /** class under test */
    private ServiceService serviceService;

    /** test data */
    private AssemblyContext testAssemblyContext;
    private String systemId;

    /** expected result */
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
