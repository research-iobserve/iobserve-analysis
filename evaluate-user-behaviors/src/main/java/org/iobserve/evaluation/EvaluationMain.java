/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.evaluation;

import java.io.File;
import java.io.IOException;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.converters.FileConverter;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import teetime.framework.Execution;

/**
 * Collector main class.
 *
 * @author Reiner Jung
 */
public final class EvaluationMain {

    private static final Log LOG = LogFactory.getLog(EvaluationMain.class);

    @Parameter(names = { "-b",
            "--baseline-model" }, required = true, description = "Baseline model file.", converter = FileConverter.class)
    private File baselineModelLocation;

    @Parameter(names = { "-t",
            "--test-model" }, required = true, description = "Test model file.", converter = FileConverter.class)
    private File testModelLocation;

    @Parameter(names = { "-o",
            "--output" }, required = true, description = "Result file.", converter = FileConverter.class)
    private File targetLocation;

    /**
     * This is a simple main class which does not need to be instantiated.
     */
    private EvaluationMain() {

    }

    /**
     * Configure and execute the evaluation tool.
     *
     * @param args
     *            arguments are ignored
     */
    public static void main(final String[] args) {

        EvaluationMain.LOG.debug("Evaluation of behavior models.");

        final EvaluationMain main = new EvaluationMain();
        final JCommander commander = new JCommander(main);
        try {
            commander.parse(args);
            main.execute(commander);
        } catch (final ParameterException e) {
            EvaluationMain.LOG.error(e.getLocalizedMessage());
            commander.usage();
        } catch (final IOException e) {
            EvaluationMain.LOG.error(e.getLocalizedMessage());
            commander.usage();
        }
    }

    private void execute(final JCommander commander) throws IOException {
        if (!this.baselineModelLocation.canRead()) {
            EvaluationMain.LOG.error("reading baseline failed: " + this.baselineModelLocation.getCanonicalPath());
            commander.usage();
            return;
        }
        if (!this.testModelLocation.canRead()) {
            EvaluationMain.LOG.error("reading test model failed: " + this.testModelLocation.getCanonicalPath());
            commander.usage();
            return;
        }

        final EvaluationConfiguration configuration = new EvaluationConfiguration(this.baselineModelLocation,
                this.testModelLocation, this.targetLocation);
        final Execution<EvaluationConfiguration> evaluation = new Execution<>(configuration);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (evaluation) {
                        evaluation.abortEventually();
                    }
                } catch (final Exception e) { // NOCS

                }
            }
        }));

        EvaluationMain.LOG.debug("Running evaluation");

        evaluation.executeBlocking();

        EvaluationMain.LOG.debug("Done");

    }

}
