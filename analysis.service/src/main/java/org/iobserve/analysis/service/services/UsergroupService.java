package org.iobserve.analysis.service.services;

import java.util.List;

import javax.json.Json;
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

    private String usergroupId;

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
        // TODO add list to Json Object
        final JsonObject usergroup = Json.createObjectBuilder().add("type", "usergroup").add("id", this.usergroupId)
                .add("systemId", systemId).build(); // add("services", new
                                                    // JSONArray(userInvokedServices)).

        return usergroup;
    }

}
