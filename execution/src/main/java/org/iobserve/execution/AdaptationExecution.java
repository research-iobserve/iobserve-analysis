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
package org.iobserve.execution;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import teetime.stage.basic.AbstractTransformation;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.evaluation.SystemEvaluation;
import org.iobserve.execution.actionscripts.AbstractActionScript;
import org.iobserve.execution.actionscripts.ActionScriptFactory;
import org.iobserve.planning.systemadaptation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This stage executes the ordered adaptation {@link Action}s sequence.
 *
 * @author Philipp Weimann
 * @author Tobias Pöppke
 */
public class AdaptationExecution extends AbstractTransformation<AdaptationData, AdaptationData> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AdaptationExecution.class);

    private final IAdaptationEventListener listener;

    private final URI deployablesFolderURI;

    /**
     * Create a new adaptation execution stage with the given event listener for events generated
     * during adaptation.
     *
     * If the listener is null and an error occurs, the execution will throw an exception and exit.
     *
     * @param listener
     *            the event listener
     * @param deployablesFolderURI
     *            Uri to the folder of deployables
     */
    public AdaptationExecution(final IAdaptationEventListener listener, final URI deployablesFolderURI) {
        this.listener = listener;
        this.deployablesFolderURI = deployablesFolderURI;
    }

    @Override
    protected void execute(final AdaptationData element) throws Exception {

        AdaptationExecution.LOGGER.info("Executing adaptation");

        element.setDeployablesFolderURI(this.deployablesFolderURI);

        final List<AbstractActionScript> notAutoSupported = new ArrayList<>();
        final List<AbstractActionScript> actionScripts = new ArrayList<>();

        final ActionScriptFactory actionFactory = new ActionScriptFactory(element);

        // TODO Finish, by adding execution. Maybe Async?
        for (final Action action : element.getExecutionOrder()) {
            final AbstractActionScript script = actionFactory.getExecutionScript(action);
            actionScripts.add(script);
            if (!script.isAutoExecutable()) {
                notAutoSupported.add(script);
            }
        }

        if (notAutoSupported.size() > 0) {
            if (this.listener == null) {
                final String unsupportedActionsDesc = notAutoSupported.stream().map(script -> script.getDescription())
                        .collect(Collectors.joining("\n"));
                throw new IllegalStateException(
                        "Could not execute all actions automatically, aborting.\n Not supported actions were:\n"
                                + unsupportedActionsDesc);
            }

            this.listener.notifyUnsupportedActionsFound(notAutoSupported);
        }

        SystemEvaluation.enableEvaluation(element);

        try {
            actionScripts.forEach(script -> {
                try {
                    script.execute();
                } catch (final Exception e) {
                    if (this.listener == null) {
                        throw new IllegalStateException("Could not execute action script '" + script.getDescription()
                                + "' automatically and no listener was present. Aborting!");
                    }

                    this.listener.notifyExecutionError(script, e);
                }
            });
        } finally {
            SystemEvaluation.disableEvaluation();
        }
    }

}
