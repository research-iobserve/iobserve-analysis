/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.stages.sink;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;

import teetime.framework.AbstractConsumerStage;

/**
 * @author Reiner Jung
 *
 */
public class CSVFileWriter extends AbstractConsumerStage<Map<String, ?>> {

    private final PrintWriter writer;
    private boolean first;

    /**
     * Create a csv writer stage.
     *
     * @param resultFile
     *            result file handle
     * @throws FileNotFoundException
     *             if file creation is not possible
     */
    public CSVFileWriter(final File resultFile) throws FileNotFoundException {
        this.writer = new PrintWriter(resultFile);
        this.first = true;
    }

    @Override
    protected void execute(final Map<String, ?> element) throws Exception {
        if (this.first) {
            boolean firstEntry = true;
            for (final String key : element.keySet()) {
                if (firstEntry) {
                    this.writer.printf("\"%s\"", key);
                    firstEntry = false;
                } else {
                    this.writer.printf(",\"%s\"", key);
                }
            }
            this.writer.println();
            this.first = false;
        }
        boolean firstEntry = true;
        for (final Object value : element.values()) {
            if (firstEntry) {
                if (value instanceof String) {
                    this.writer.printf("\"%s\"", value);
                } else if (value instanceof Long || value instanceof Integer) {
                    this.writer.printf("%d", value);
                } else {
                    this.writer.print(value);
                }
                firstEntry = false;
            } else {
                if (value instanceof String) {
                    this.writer.printf(",\"%s\"", value);
                } else if (value instanceof Long || value instanceof Integer) {
                    this.writer.printf(",%d", value);
                } else {
                    this.writer.print("," + value);
                }
            }
        }
        this.writer.println();
    }

    @Override
    protected void onTerminating() {
        this.writer.close();
        super.onTerminating();
    }

}
