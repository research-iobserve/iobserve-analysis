package org.iobserve.analysis.service.services;

import javax.json.Json;
import javax.json.JsonObject;

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
    public JsonObject createUsergroup(final String systemId) {

        final JsonObject usergroup = Json.createObjectBuilder().add("type", "usergroup").add("id", this.usergroupId)
                .add("systemId", systemId).build();

        return usergroup;
    }

}
