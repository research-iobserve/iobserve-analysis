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
package org.iobserve.analysis.behavior.filter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.behavior.models.data.BehaviorModelTable;
import org.iobserve.analysis.data.UserSessionCollectionModel;
import org.iobserve.analysis.session.data.UserSession;
import org.iobserve.stages.general.data.EntryCallEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Prepares {@link UserSessioncollectionModel}s for clustering, but also receives @{link
 * BehaviorModelTabel}s.
 *
 * TODO should be renamed, also what is the purpose of the strange execute method? why use Object
 * and the process two different types of events?
 *
 * @author Christoph Dornieden
 *
 */

public final class BehaviorModelPreparation extends AbstractConsumerStage<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BehaviorModelPreparation.class);

    private final OutputPort<BehaviorModelTable> outputPort = this.createOutputPort();

    private BehaviorModelTable behaviorModelTable;
    private final Set<UserSessionCollectionModel> sequenceModelCache;

    private final boolean keepEmptyTransitions;

    /**
     * Constructor for the sequence clustering.
     *
     * @param keepEmptyTransitions
     *            allow to keep empty transitions
     */
    public BehaviorModelPreparation(final boolean keepEmptyTransitions) {
        super();
        this.sequenceModelCache = new HashSet<>();
        this.keepEmptyTransitions = keepEmptyTransitions;
    }

    @Override
    protected void execute(final Object object) {
        if (object instanceof UserSessionCollectionModel) {
            final UserSessionCollectionModel entryCallSequenceModel = (UserSessionCollectionModel) object;
            this.executeEntryCallSequenceModel(entryCallSequenceModel);

        } else if (object instanceof BehaviorModelTable) {
            final BehaviorModelTable modelTable = (BehaviorModelTable) object;
            this.executeBehaviorModelTable(modelTable);

        } else {
            BehaviorModelPreparation.LOGGER
                    .error("input is nether of type EntryCallSequenceModel nor BehaviorModelTable");
        }
    }

    /**
     * Execute case object instanceof EntryCallSequenceModel.
     *
     * @param entryCallSequenceModel
     *            entryCallSequenceModel
     */
    private void executeEntryCallSequenceModel(final UserSessionCollectionModel entryCallSequenceModel) {

        if (this.behaviorModelTable == null) {
            this.sequenceModelCache.add(entryCallSequenceModel);

        } else {
            final List<UserSession> userSessions = entryCallSequenceModel.getUserSessions();
            BehaviorModelPreparation.LOGGER.debug("Executing EntryCallSequenceModel");
            BehaviorModelPreparation.LOGGER.debug("userSessions: {}", userSessions.size());
            for (final UserSession userSession : userSessions) {
                final BehaviorModelTable modelTable = this.behaviorModelTable.getClearedCopy(this.keepEmptyTransitions);
                final List<EntryCallEvent> entryCalls = userSession.getEvents();

                EntryCallEvent lastCall = null;
                for (final EntryCallEvent eventCall : entryCalls) {
                    final boolean isAllowed = modelTable.isAllowedSignature(eventCall);
                    if (lastCall != null && isAllowed) {
                        modelTable.addTransition(lastCall, eventCall);
                        modelTable.addInformation(eventCall);

                    } else if (isAllowed) { // only called at first valid event
                                            // (condition lastCall == null is not needed
                        modelTable.addInformation(eventCall);
                    }

                    lastCall = isAllowed ? eventCall : lastCall;
                }
                this.outputPort.send(modelTable);
            }

        }

    }

    /**
     * Execute case object instanceof BehaviorModelTable.
     *
     * @param newBehaviorModelTable
     *            behaviorModelTable
     */
    private void executeBehaviorModelTable(final BehaviorModelTable newBehaviorModelTable) {
        if (this.behaviorModelTable == null) {
            this.behaviorModelTable = newBehaviorModelTable;
            this.sequenceModelCache.forEach(this::executeEntryCallSequenceModel);
            this.sequenceModelCache.clear();
        } else {
            BehaviorModelPreparation.LOGGER.warn("behaviorModelTable already assigned");
        }
    }

    /**
     * getter.
     *
     * @return the outputPort
     */
    public OutputPort<BehaviorModelTable> getOutputPort() {
        return this.outputPort;
    }

}