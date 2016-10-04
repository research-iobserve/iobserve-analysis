/***************************************************************************
 * Copyright 2014 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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
package org.iobserve.analysis.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.iobserve.analysis.AnalysisMain;
import org.iobserve.analysis.correspondence.Correspondent;
import org.iobserve.analysis.correspondence.ICorrespondence;
import org.iobserve.analysis.data.EntryCallEvent;
import org.iobserve.analysis.filter.models.EntryCallSequenceModel;
import org.iobserve.analysis.filter.models.UserSession;
import org.iobserve.analysis.model.ModelProviderPlatform;
import org.iobserve.analysis.model.UsageModelBuilder;
import org.iobserve.analysis.model.UsageModelProvider;
import org.iobserve.analysis.userbehavior.UserBehaviorModeling;

import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.Start;
import org.palladiosimulator.pcm.usagemodel.Stop;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;


import com.google.common.base.Optional;

import teetime.framework.AbstractConsumerStage;

import org.iobserve.analysis.correspondence.Correspondent;
import org.iobserve.analysis.correspondence.ICorrespondence;
import org.iobserve.analysis.data.EntryCallEvent;
import org.iobserve.analysis.filter.models.EntryCallSequenceModel;
import org.iobserve.analysis.filter.models.UserSession;
import org.iobserve.analysis.model.UsageModelBuilder;
import org.iobserve.analysis.model.UsageModelProvider;

import teetime.framework.AbstractConsumerStage;

/**
 * Represents the TEntryEventSequence Transformation in the paper <i>Run-time Architecture Models
 * for Dynamic Adaptation and Evolution of Cloud Applications</i> Triggers the user behavior
 * modeling process that creates a PCM usage model from an EntryCallSequenceModel
 *
 * @author Robert Heinrich
 * @author Alessandro Guisa
 * @author David Peter
 * 
 * @version 1.0
 */
public final class TEntryEventSequence extends AbstractConsumerStage<EntryCallSequenceModel> {

    private int counterSavedUsageModel = 0;

    private final UsageModelBuilder usageModelBuilder;

    /** reference to the correspondence model. */
    private final ICorrespondence correspondenceModel;
    /** reference to the usage model provider. */
    private final UsageModelProvider usageModelProvider;

    /**
     * Create a entry event sequence filter.
     *
     * @param correspondenceModel
     * @param usageModelProvider
     */
    public TEntryEventSequence(final ICorrespondence correspondenceModel, final UsageModelProvider usageModelProvider) {
        this.correspondenceModel = correspondenceModel;
        this.usageModelProvider = usageModelProvider;
        this.usageModelBuilder = new UsageModelBuilder(modelProviderPlatform.getUsageModelProvider());
    }

    @Override
    protected void execute(final EntryCallSequenceModel model) {
        // logging execution time and memory
        AnalysisMain.getInstance().getTimeMemLogger().before(this, this.getId());

        // Gets the input parameter for the user behavior modeling
        final int numberOfUserGroups = this.usageModelProvider.getModel().getUsageScenario_UsageModel().size();
        final int varianceOfUserGroups = AnalysisMain.getInstance().getInputParameter().getVarianceOfUserGroups();
        final int thinkTime = AnalysisMain.getInstance().getInputParameter().getThinkTime();
        final boolean isClosedWorkload = AnalysisMain.getInstance().getInputParameter().isClosedWorkload();

        // Resets the current usage model
        this.usageModelBuilder.loadModel().resetModel();
        // Executes the user behavior modeling procedure
        final UserBehaviorModeling behaviorModeling = new UserBehaviorModeling(model, numberOfUserGroups,
                varianceOfUserGroups, isClosedWorkload, thinkTime, this.usageModelBuilder, this.correspondenceModel);
        try {
            behaviorModeling.modelUserBehavior();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        // Sets the new usage model within iObserve
        this.usageModelBuilder.build();

        // logging execution time and memory
        AnalysisMain.getInstance().getTimeMemLogger().after(this, this.getId());

    }

    /**
     * Calculate the interarrival time of the given user sessions.
     *
     * @param sessions
     *            sessions.
     * @return >= 0.
     */
    private long calculateInterarrivalTime(final List<UserSession> sessions) {
        long interArrivalTime = 0;
        if (sessions.size() > 0) {
            // sort user sessions
            Collections.sort(sessions, this.sortUserSessionByExitTime);

            long sum = 0;
            for (int i = 0; i < (sessions.size() - 1); i++) {
                final long exitTimeU1 = sessions.get(i).getExitTime();
                final long exitTimeU2 = sessions.get(i + 1).getExitTime();
                sum += exitTimeU2 - exitTimeU1;
            }

            final long numberSessions = sessions.size() > 1 ? sessions.size() - 1 : 1;
            interArrivalTime = sum / numberSessions;
        }

        return interArrivalTime;
    }

    /**
     * Do update the PCM usage model by iterating over user sessions and constructing the different
     * paths.
     *
     * This procedure creates a test scenario. The creation of a PCM usage model is now done by
     * {@link org.iobserve.analysis.userbehavior.UserBehaviorModeling}
     *
     * @param sessions
     *            user session
     */
    @Deprecated
    private void doUpdateUsageModel(final List<UserSession> sessions) {
        final long averageInterarrivalTime = this.calculateInterarrivalTime(sessions);

        // iterate over user sessions
        for (final UserSession userSession : sessions) {

            // create simple usage model builder
            final UsageModelBuilder builder = new UsageModelBuilder(this.usageModelProvider);

            // like re-load
            builder.loadModel().resetModel();

            final UsageScenario usageScenario = builder.createUsageScenario("MyTestScenario");
            builder.createOpenWorkload(averageInterarrivalTime, usageScenario);

            final Start start = builder.createStart();
            builder.addUserAction(usageScenario, start);

            AbstractUserAction lastAction = start;

            // iterate over all events to create the usage behavior
            final Iterator<EntryCallEvent> iteratorEvents = userSession.iterator();
            while (iteratorEvents.hasNext()) {
                final EntryCallEvent event = iteratorEvents.next();
                final String classSig = event.getClassSignature();
                final String opSig = event.getOperationSignature();

                final Optional<Correspondent> optionCorrespondent = this.correspondenceModel.getCorrespondent(classSig,
                        opSig);
                if (optionCorrespondent.isPresent()) {
                    final Correspondent correspondent = optionCorrespondent.get();
                    final EntryLevelSystemCall eSysCall = builder.createEntryLevelSystemCall(correspondent);
                    builder.connect(lastAction, eSysCall);
                    builder.addUserAction(usageScenario, eSysCall);
                    lastAction = eSysCall;
                }
            }

            final Stop stop = builder.createStop();
            builder.connect(lastAction, stop);
            builder.addUserAction(usageScenario, stop);

            builder.build();
            this.counterSavedUsageModel++; // TODO just for now
        }
    }

    /**
     * Sorts {@link UserSession} by the exit time.
     */
    private final Comparator<UserSession> sortUserSessionByExitTime = new Comparator<UserSession>() {

        @Override
        public int compare(final UserSession o1, final UserSession o2) {
            final long exitO1 = o1.getExitTime();
            final long exitO2 = o2.getExitTime();
            if (exitO1 > exitO2) {
                return 1;
            } else if (exitO1 < exitO2) {
                return -1;
            }
            return 0;
        }
    };

}
