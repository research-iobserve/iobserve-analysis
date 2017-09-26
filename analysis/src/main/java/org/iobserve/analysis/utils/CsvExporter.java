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
package org.iobserve.analysis.utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 *
 * @author unknown
 *
 */
public class CsvExporter {

    private final String filePath;
    private final StringBuilder fileContent;

    /**
     * Create new CSV exporter.
     * 
     * @param filePath
     *            path for output file
     */
    public CsvExporter(final String filePath) {
        this.filePath = /* "output\\logging\\" + */filePath;
        this.fileContent = new StringBuilder();
    }

    /**
     * Set header.
     * 
     * @param headline
     *            the header.
     */
    public void setHeadline(final String headline) {
        this.fileContent.append(headline);
        this.fileContent.append(';');

        this.fileContent.append('\n');
    }

    /**
     * Add a row to the CSV file.
     * 
     * @param elements
     *            elements for the row
     */
    public void addRow(final List<? extends Object> elements) {
        this.fileContent.append(';');
        for (final Object object : elements) {
            this.fileContent.append(object.toString());
            this.fileContent.append(';');
        }

        this.fileContent.append('\n');
    }

    /**
     * Export CSV data.
     */
    public void export() {
        try {
            final PrintWriter pw = new PrintWriter(this.filePath, "UTF-8");
            pw.write(this.fileContent.toString());
            pw.close();
        } catch (final FileNotFoundException e) {
            System.err.println("Could not create file " + this.filePath);
            e.printStackTrace();
        } catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
