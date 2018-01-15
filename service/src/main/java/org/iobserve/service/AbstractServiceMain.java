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

import java.io.IOException;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

import teetime.framework.Configuration;
import teetime.framework.Execution;

/**
 * @author Reiner Jung
 *
 */
public abstract class AbstractServiceMain<T extends Configuration> {

    protected static final Log LOG = LogFactory.getLog(AbstractServiceMain.class);
    protected boolean help = false;

    /**
     * Configure and execute the evaluation tool.
     *
     * @param title
     *            start up label for debug messages
     * @param label
     *            label used during execution
     * @param args
     *            arguments are ignored
     */
    public void run(final String title, final String label, final String[] args) {
        AbstractServiceMain.LOG.debug(title);

        final JCommander commander = new JCommander(this);
        try {
            commander.parse(args);
            this.execute(commander, label);
        } catch (final ParameterException e) {
            AbstractServiceMain.LOG.error(e.getLocalizedMessage());
            commander.usage();
        } catch (final IOException e) {
            AbstractServiceMain.LOG.error(e.getLocalizedMessage());
            commander.usage();
        }
    }

    private void execute(final JCommander commander, final String label) throws IOException {
        if (this.checkParameters(commander)) {

            if (this.help) {
                commander.usage();
                System.exit(1);
            } else {
                final Execution<T> execution = new Execution<>(this.createConfiguration());

                this.shutdownHook(execution);

                AbstractServiceMain.LOG.debug("Running " + label);

                execution.executeBlocking();

                AbstractServiceMain.LOG.debug("Done");
            }
        } else {
            AbstractServiceMain.LOG.error("Configuration Error");
        }
    }

    /**
     * Create and initialize configuration for a service.
     *
     * @return return the newly created service
     *
     * @throws IOException
     *             in case the creation fails
     */
    protected abstract T createConfiguration() throws IOException;

    /**
     * Check all given parameters for correct directory and files path, as well as, all other values
     * for fitness.
     *
     * @param commander
     *            the command line interface
     * @return true if all parameter check out, else false
     *
     * @throws IOException
     *             on error
     */
    protected abstract boolean checkParameters(JCommander commander) throws IOException;

    private <R extends Configuration> void shutdownHook(final Execution<R> execution) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (execution) {
                        execution.abortEventually();
                    }
                } catch (final Exception e) { // NOCS

                }
            }
        }));

    }
}
