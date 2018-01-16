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
package org.iobserve.service.privacy.violation.filter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import teetime.framework.AbstractConsumerStage;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Generic file sink which serializes input.
 *
 * @author Reiner Jung
 *
 * @param <T>
 *            input data type to be serialized
 *
 */
public abstract class AbstractFileSink<T> extends AbstractConsumerStage<T> {

    protected static final Logger LOGGER = LogManager.getLogger(AbstractFileSink.class);
    protected final PrintWriter output;

    /**
     * Create an file sink.
     *
     * @param file
     *            the file handle to be used.
     * @throws IOException
     *             on file access issues
     */
    public AbstractFileSink(final File file) throws IOException {
        this.output = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
    }

}
