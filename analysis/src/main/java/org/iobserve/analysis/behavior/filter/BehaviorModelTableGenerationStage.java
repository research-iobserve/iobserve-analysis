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

import java.util.List;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.behavior.models.data.BehaviorModelTable;
import org.iobserve.analysis.behavior.models.data.DynamicBehaviorModelTable;
import org.iobserve.analysis.behavior.models.data.configuration.IRepresentativeStrategy;
import org.iobserve.analysis.data.UserSessionCollectionModel;
import org.iobserve.analysis.session.data.UserSession;
import org.iobserve.stages.general.data.EntryCallEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * auxiliary filter to generate the base of the BehaviorModelTable.
 *
 * @author Christoph Dornieden
 *
 */
public final class BehaviorModelTableGenerationStage extends AbstractConsumerStage<UserSessionCollectionModel> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BehaviorModelTableGenerationStage.class);

    private final OutputPort<BehaviorModelTable> outputPort = this.createOutputPort();

    private final DynamicBehaviorModelTable modelTable;
    private final boolean keepEmptyTransitions;

    /**
     * constructor
     *
     * input values are used to create the inner {@link DynamicBehaviorModelTable}.
     *
     * @param strategy
     *            representative strategy
     * @param keepEmptyTransitions
     *            allows to keep empty transitions
     */
    public BehaviorModelTableGenerationStage(final IRepresentativeStrategy strategy,
            final boolean keepEmptyTransitions) {
        super();

        this.modelTable = new DynamicBehaviorModelTable(strategy);
        this.keepEmptyTransitions = keepEmptyTransitions;

    }

    @Override
    protected void execute(final UserSessionCollectionModel entryCallSequenceModel) {
        final List<UserSession> userSessions = entryCallSequenceModel.getUserSessions();

        for (final UserSession userSession : userSessions) {
            BehaviorModelTableGenerationStage.LOGGER.debug("entryCalls: {}", userSession.getEvents().size());
            final List<EntryCallEvent> entryCalls = userSession.getEvents();

            EntryCallEvent lastCall = null;
            for (final EntryCallEvent eventCall : entryCalls) {
                if (lastCall != null) {
                    this.modelTable.addTransition(lastCall, eventCall);
                    this.modelTable.addInformation(eventCall);

                } else {
                    /**
                     * only called at first valid event (condition lastCall == null is not needed)
                     */
                    this.modelTable.addInformation(eventCall);

                }
                lastCall = eventCall;
            }
        }
    }

    @Override
    public void onTerminating() {

        final BehaviorModelTable fixedTable = this.modelTable
                .toClearedFixedSizeBehaviorModelTable(this.keepEmptyTransitions);
        this.outputPort.send(fixedTable);

        super.onTerminating();
    }

    /**
     * getter.
     *
     * @return output port
     */
    public OutputPort<BehaviorModelTable> getOutputPort() {
        return this.outputPort;
    }

}