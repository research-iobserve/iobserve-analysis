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

import org.iobserve.service.privacy.violation.data.Alarms;

/**
 * @author Reiner Jung
 *
 */
public class AlarmSink extends AbstractFileSink<Alarms> {

    /**
     * Create an alarm sink.
     *
     * @param file
     *            file where the alarms go
     * @throws IOException
     *             on file errors
     */
    public AlarmSink(final File file) throws IOException {
        super(file);
    }

    @Override
    protected void execute(final Alarms element) throws Exception {
        AbstractFileSink.LOGGER.debug("Alarms");
        for (final String alarms : element.getAlarms()) {
            AbstractFileSink.LOGGER.debug("\t {}", alarms);
            this.output.println(alarms);
        }
    }

}
