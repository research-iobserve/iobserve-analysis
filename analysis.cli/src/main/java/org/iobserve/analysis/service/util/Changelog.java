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
package org.iobserve.analysis.service.util;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * helper class for creating changelogs for deployment visualization.
 *
 * @author Josefine Wegert
 *
 */
public final class Changelog {

    private Changelog() {

    }

    /**
     * Builds a changelog for creating visualization elements.
     *
     * @param data
     *            data that represents a visualization element
     * @return JsonObject for a creating changelog
     */
    public static JsonObject create(final JsonObject data) {
        final JsonObject createChangelog = Json.createObjectBuilder().add("type", "changelog")
                .add("operation", "CREATE").add("data", data).build();
        return createChangelog;

    }

    /**
     * Builds a changelog for deleting visualization elements.
     *
     * @param data
     *            data that represents a visualization element
     * @return JsonObject for a creating changelog
     */
    public static JsonObject delete(final JsonObject data) {
        final JsonObject deleteChangelog = Json.createObjectBuilder().add("type", "changelog")
                .add("operation", "DELETE").add("data", data).build();

        return deleteChangelog;
    }

}
