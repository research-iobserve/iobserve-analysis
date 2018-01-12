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
package org.iobserve.analysis.utils;

import java.io.File;
import java.io.IOException;

import com.beust.jcommander.JCommander;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * Collection of command line parameter evaluation functions.
 *
 * @author Reiner Jung
 *
 */
public class CommandlineParameterEvaluation {

    private static final Log LOG = LogFactory.getLog(CommandlineParameterEvaluation.class);

    /**
     * Check whether the given handle refers to an existing directory.
     * 
     * @param location
     *            path to the directory
     * @param locationLabel
     *            label indicating the parameter of the given location
     * @param commander
     *            command line handler
     * @throws IOException
     *             on io error
     */
    public static void checkDirectory(final File location, final String locationLabel, final JCommander commander)
            throws IOException {
        if (!location.exists()) {
            CommandlineParameterEvaluation.LOG
                    .error(locationLabel + " path " + location.getCanonicalPath() + " does not exist.");
            commander.usage();
            System.exit(1);
        }
        if (!location.isDirectory()) {
            CommandlineParameterEvaluation.LOG
                    .error(locationLabel + " path " + location.getCanonicalPath() + " is not a directory.");
            commander.usage();
            System.exit(1);
        }

    }
}
