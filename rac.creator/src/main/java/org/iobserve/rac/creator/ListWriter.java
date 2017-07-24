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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import teetime.framework.AbstractConsumerStage;

/**
 * Write a list of strings in a text file.
 *
 * @author Reiner Jung
 *
 */
public class ListWriter extends AbstractConsumerStage<String> {

    private final PrintWriter writer;

    /**
     * Construct a list writer for the specified file.
     *
     * @param file
     *            specifies in which file to write the strings.
     * @throws FileNotFoundException
     *             when the file cannot be created.
     * @throws UnsupportedEncodingException
     *             when the specified encoding is not supported
     */
    public ListWriter(final File file) throws FileNotFoundException, UnsupportedEncodingException {
        this.writer = new PrintWriter(file, "UTF-8");
    }

    @Override
    protected void execute(final String element) throws Exception {
        this.writer.println(element);
    }

    @Override
    public void onTerminating() throws Exception {
        super.onTerminating();
        this.writer.close();
    }

}
