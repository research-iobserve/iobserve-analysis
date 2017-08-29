package org.iobserve.analysis.service.services;

import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

import org.palladiosimulator.pcm.core.composition.AssemblyContext;

/**
 * This class prepares data such that the visualization element user group is created. It has to be
 * added to a changelog in order to be send to the deployment visualization.
 *
 * @author jweg
 *
 */
public class UsergroupService {

    private final String usergroupId = "test_usergroup_id";

    /**
     * empty default constructor
     */
    public UsergroupService() {

    }

    /**
     * Builds data for creating a user group for the deployment visualization.
     *
     * @param systemId
     * @return
     */
    public JsonObject createUsergroup(final String systemId, final List<AssemblyContext> userInvokedServices) {
        JsonObject invokedServiceObject;
        final JsonArrayBuilder builder = Json.createArrayBuilder();

        // build array of targetIds
        for (int i = 0; i < userInvokedServices.size(); i++) {
            final String serviceId = userInvokedServices.get(i).getId();
            invokedServiceObject = Json.createObjectBuilder().add("targetId", serviceId).build();
            builder.add(invokedServiceObject);
        }
        final JsonArray invokedServicesArray = builder.build();

        final JsonObject usergroup = Json.createObjectBuilder().add("type", "usergroup").add("id", this.usergroupId)
                .add("systemId", systemId).add("name", "test-usergroup").add("calledServices", invokedServicesArray)
                .build();

        return usergroup;
    }

}
