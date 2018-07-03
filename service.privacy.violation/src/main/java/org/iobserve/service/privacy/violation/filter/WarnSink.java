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
package org.iobserve.service.privacy.violation.filter;

import java.io.File;
import java.io.IOException;

import org.iobserve.stages.data.Warnings;

/**
 * @author Reiner Jung
 *
 */
public class WarnSink extends AbstractFileSink<Warnings> {

    /**
     * Create a warning sink.
     *
     * @param warningFile
     *            file to log all warnings.
     * @throws IOException
     *             on file operation errors
     */
    public WarnSink(final File warningFile) throws IOException {
        super(warningFile);
    }

    @Override
    protected void execute(final Warnings element) throws Exception {
        AbstractFileSink.LOGGER.debug("Warnings");
        this.output.printf("Warning %s\n", element.getDate());
        for (final String warning : element.getMessages()) {
            AbstractFileSink.LOGGER.debug("\t {}", warning);
            this.output.printf("%s %s\n", element.getDate(), warning);
        }
        this.output.flush();
    }

}
