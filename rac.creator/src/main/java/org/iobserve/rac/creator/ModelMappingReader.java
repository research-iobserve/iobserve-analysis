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
package org.iobserve.rac.creator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * @author Nicolas Boltz -- initial contribution
 * @author Reiner Jung
 *
 */
public class ModelMappingReader {

    private final File mappingFile;

    /**
     * Create a reader for model mappings.
     *
     * @param mappingFile
     *            a file handle for the mapping file
     */
    public ModelMappingReader(final File mappingFile) {
        this.mappingFile = mappingFile;
    }

    /**
     * Read the mapping file and place the mapping in a map.
     *
     * @return returns a map of model and code identifiers
     */
    public Map<String, String> readModelMapping() {
        try {
            final Map<String, String> systemMapping = new HashMap<>();

            final BufferedReader in = Files.newBufferedReader(this.mappingFile.toPath(), StandardCharsets.UTF_8);

            String line = in.readLine();
            while (line != null) {
                final String[] mapping = line.split(" - ");
                systemMapping.put(mapping[0], mapping[1]);

                line = in.readLine();
            }
            in.close();
            return systemMapping;
        } catch (final IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
