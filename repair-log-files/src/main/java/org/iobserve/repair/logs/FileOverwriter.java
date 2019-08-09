/***************************************************************************
 * Copyright (C) 2019 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.repair.logs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import teetime.framework.AbstractConsumerStage;

/**
 * Write strings line by line to a temporary text file. When null is received, close the file and
 * replace the given file with the temp file.
 *
 * @author Reiner Jung
 *
 */
public class FileOverwriter extends AbstractConsumerStage<String> {

    private final BufferedWriter writer;
    private final File input;
    private final File tempFile;

    /**
     * Create a writer which overwrites the given file on EOF.
     *
     * @param input
     *            file to be overwritten
     * @throws IOException
     *             on io errors
     */
    public FileOverwriter(final File input) throws IOException {
        this.tempFile = File.createTempFile("prefix", "suffix");
        this.writer = Files.newBufferedWriter(this.tempFile.toPath(), StandardOpenOption.WRITE);
        this.input = input;
    }

    @Override
    protected void execute(final String element) throws Exception {
        this.writer.write(this.createFormattedString(element));
    }

    @Override
    protected void onTerminating() {
        try {
            this.writer.close();
            final File oldInputFile = new File(this.input.getPath());
            this.input.renameTo(new File(this.input.getPath() + ".old"));
            this.tempFile.renameTo(oldInputFile);
        } catch (final IOException e) {
            this.logger.error("IO error while terminating.", e);
        }
        super.onTerminating();
    }

    private String createFormattedString(final String element) {
        return element.replace("\n", "\\n") + "\n";
    }

}
