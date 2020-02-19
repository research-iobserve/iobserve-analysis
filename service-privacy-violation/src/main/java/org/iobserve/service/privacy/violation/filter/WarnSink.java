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

import org.iobserve.service.privacy.violation.data.WarningModel;

/**
 * @author Reiner Jung
 *
 */
public class WarnSink extends AbstractFileSink<WarningModel> {

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
    protected void execute(final WarningModel element) throws Exception {
        AbstractFileSink.LOGGER.debug(String.format("Warnings for %s %s %s", element.getEvent().getService(),
                element.getEvent().getAssemblyContext().getEntityName(),
                element.getEvent().getResourceContainer().getEntityName()));
        this.output.printf("Warning %s %s %s %s\n", element.getDate(), element.getEvent().getService(),
                element.getEvent().getAssemblyContext().getEntityName(),
                element.getEvent().getResourceContainer().getEntityName());
        for (final String warning : element.getMessages()) {
            AbstractFileSink.LOGGER.debug("\t {}", warning);
            this.output.printf("%s %s\n", element.getDate(), warning);
        }
        this.output.flush();
    }

}
