package util;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * helper class for creating changelogs for deployment visualization
 * 
 * @author jweg
 *
 */
public final class Changelog {

    private Changelog() {

    }

    public static JsonObject create(final JsonObject data) {
        final JsonObject createChangelog = Json.createObjectBuilder().add("type", "changelog")
                .add("operation", "CREATE").add("data", data).build();
        return createChangelog;

    }

    public static JsonObject delete(final JsonObject data) {
        final JsonObject deleteChangelog = Json.createObjectBuilder().add("type", "changelog")
                .add("operation", "DELETE").add("data", data).build();

        return deleteChangelog;
    }

}
