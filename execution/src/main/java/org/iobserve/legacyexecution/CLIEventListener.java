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
package org.iobserve.legacyexecution;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.iobserve.legacyexecution.actionscripts.AbstractActionScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ToDo .
 *
 * @author unknown
 *
 */
public class CLIEventListener implements IAdaptationEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(CLIEventListener.class);

    private final boolean interactiveMode;

    /**
     * Create an CLIEventListener.
     *
     * @param interactiveMode
     *            allows to set the listener into interactive mode
     */
    public CLIEventListener(final boolean interactiveMode) {
        this.interactiveMode = interactiveMode;
    }

    @Override
    public void notifyUnsupportedActionsFound(final List<AbstractActionScript> unsupportedActions) {
        final String unsupportedActionsDesc = unsupportedActions.stream().map(script -> script.getDescription())
                .collect(Collectors.joining("\n"));

        if (!this.interactiveMode) {
            throw new IllegalStateException(
                    "Could not execute all actions automatically, aborting.\n Not supported actions were:\n"
                            + unsupportedActionsDesc);
        }

        CLIEventListener.LOGGER.info("The following actions can not be executed automatically:");
        CLIEventListener.LOGGER.info(unsupportedActionsDesc);

        CLIEventListener.LOGGER.info(
                "You will be prompted to execute the tasks manually during the process. Do you want to continue?");

        final Scanner scanner = new Scanner(System.in);
        if (!scanner.nextBoolean()) {
            scanner.close();
            throw new RuntimeException("User aborted during adaptation execution.");
        }
        scanner.close();
    }

    @Override
    public void notifyExecutionError(final AbstractActionScript script, final Throwable e) {
        CLIEventListener.LOGGER.info("There was an error executing the following script: ");
        CLIEventListener.LOGGER.info(script.getDescription());
        CLIEventListener.LOGGER.info(e.getMessage());
        e.printStackTrace();
        CLIEventListener.LOGGER
                .info("You can manually execute the script and continue or abort the adaptation process.");
        CLIEventListener.LOGGER.info("Do you want to continue?");

        final Scanner scanner = new Scanner(System.in);
        if (!scanner.nextBoolean()) {
            // abort
            scanner.close();
            throw new RuntimeException("User aborted during adaptation execution.");
        }
        scanner.close();
    }

}
