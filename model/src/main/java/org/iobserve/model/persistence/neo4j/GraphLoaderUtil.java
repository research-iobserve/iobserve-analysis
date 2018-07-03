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
package org.iobserve.model.persistence.neo4j;

import java.io.File;

/**
 * Provides different utilities for the {@link ModelGraphLoader}.
 *
 * @author Lars Bluemke
 *
 */
public final class GraphLoaderUtil {

    private GraphLoaderUtil() {
        // private constructor, utility class
    }

    /**
     * Returns the highest version number from all passed graph database directories.
     *
     * @param files
     *            The graph database directories
     * @return The highest version number
     */
    public static int getLastVersionNumber(final File[] files) {
        if (files != null) {
            int max = 0;

            for (final File file : files) {
                final int version = GraphLoaderUtil.getVersionNumber(file);

                if (max < version) {
                    max = version;
                }
            }
            return max;
        } else {
            return -1;
        }

    }

    /**
     * Returns the version number of a given graph database directory.
     *
     * @param file
     *            The graph database directory
     * @return The version number
     */
    public static int getVersionNumber(final File file) {
        final int versionNumberIndex = file.getName().lastIndexOf(ModelGraphLoader.VERSION_PREFIX);

        if (versionNumberIndex == -1) {
            throw new InternalError(
                    "Missing version number in " + file + "  Every database path must have a version number.");
        } else {
            return Integer.valueOf(file.getName().substring(versionNumberIndex + ModelGraphLoader.VERSION_PREFIX.length()));
        }
    }
}
