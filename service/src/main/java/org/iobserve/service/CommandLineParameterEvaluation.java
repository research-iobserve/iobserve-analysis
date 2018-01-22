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
package org.iobserve.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.beust.jcommander.JCommander;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * Collection of command line parameter evaluation functions.
 *
 * @author Reiner Jung
 *
 */
public class CommandLineParameterEvaluation {

    private static final Log LOG = LogFactory.getLog(CommandLineParameterEvaluation.class);

    /**
     * Check whether the given handle refers to an existing directory.
     *
     * @param location
     *            path to the directory
     * @param locationLabel
     *            label indicating the parameter of the given location
     * @param commander
     *            command line handler
     *
     * @return returns true if the directory exists, else false
     * @throws IOException
     *             on io error
     */
    public static boolean checkDirectory(final File location, final String locationLabel, final JCommander commander)
            throws IOException {
        if (!location.exists()) {
            CommandLineParameterEvaluation.LOG
                    .error(locationLabel + " path " + location.getCanonicalPath() + " does not exist.");
            commander.usage();
            return false;
        }
        if (!location.isDirectory()) {
            CommandLineParameterEvaluation.LOG
                    .error(locationLabel + " path " + location.getCanonicalPath() + " is not a directory.");
            commander.usage();
            return false;
        }

        return true;
    }

    /**
     * Check whether a specified file is readable.
     *
     * @param file
     *            the file handle
     * @param label
     *            label indicating the parameter of the given location
     * @return true on success else false
     * @throws IOException
     *             on io error
     */
    public static boolean isFileReadable(final File file, final String label) throws IOException {
        if (!file.exists()) {
            CommandLineParameterEvaluation.LOG.error(label + " " + file.getCanonicalPath() + " does not exist.");
            return false;
        }
        if (!file.isFile()) {
            CommandLineParameterEvaluation.LOG.error(label + " " + file.getCanonicalPath() + " is not a file.");
            return false;
        }
        if (!file.canRead()) {
            CommandLineParameterEvaluation.LOG.error(label + " " + file.getCanonicalPath() + " cannot be read.");
            return false;
        }

        return true;
    }

    /**
     * Create an URL from a given string.
     *
     * @param urlString
     *            the url string
     * @param label
     *            label used to indicate which string
     * @return returns an URL or null on error
     */
    public static URL createURL(final String urlString, final String label) {
        try {
            return new URL(urlString);
        } catch (final MalformedURLException e) {
            CommandLineParameterEvaluation.LOG.error(label + " Malformend URL " + urlString);
            return null;
        }

    }
}
