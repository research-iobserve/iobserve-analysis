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
package org.iobserve.analysis.modelneo4j;

import java.io.File;

/**
 * Provides different utilities for the {@link GraphLoader}.
 *
 * @author Lars Bluemke
 *
 */
public class GraphLoaderUtil {

    public static int getMaxVersionNumber(final File[] files) {
        int max = 0;

        if (files != null) {
            for (final File file : files) {
                final int v = GraphLoaderUtil.getVersionNumber(file);

                if (max < v) {
                    max = v;
                }
            }
        }

        return max;
    }

    public static int getVersionNumber(final File file) {
        final int versionNumberIndex = file.getName().lastIndexOf(GraphLoader.VERSION_PREFIX);
        if (versionNumberIndex == -1) {
            return 0;
        }
        final int versionNumber = Integer
                .valueOf(file.getName().substring(versionNumberIndex + GraphLoader.VERSION_PREFIX.length()));

        return versionNumber;
    }
}
