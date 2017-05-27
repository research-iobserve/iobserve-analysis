/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 ***************************************************************************/

package org.iobserve.analysis.cdoruserbehavior.filter;

import java.util.List;

import org.iobserve.analysis.cdoruserbehavior.filter.models.BehaviorModelTable;
import org.iobserve.analysis.cdoruserbehavior.filter.models.DynamicBehaviorModelTable;
import org.iobserve.analysis.cdoruserbehavior.filter.models.configuration.IRepresentativeStrategy;
import org.iobserve.analysis.cdoruserbehavior.filter.models.configuration.ISignatureCreationStrategy;
import org.iobserve.analysis.cdoruserbehavior.filter.models.configuration.ModelGenerationFilter;
import org.iobserve.analysis.data.EntryCallEvent;
import org.iobserve.analysis.filter.models.EntryCallSequenceModel;
import org.iobserve.analysis.filter.models.UserSession;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * auxiliary filter to generate the base of the BehaviorModelTable
 *
 * @author Christoph Dornieden
 *
 */
public final class TBehaviorModelTableGeneration extends AbstractConsumerStage<EntryCallSequenceModel> {

    private final OutputPort<BehaviorModelTable> outputPort = this.createOutputPort();

    private final DynamicBehaviorModelTable modelTable;
    private final boolean keepEmptyTransitions;

    /**
     * constructor
     *
     * input values are used to create the inner {@link DynamicBehaviorModelTable}.
     *
     * @param signatureCreationStrategy
     *            signature creation strategy
     * @param strategy
     *            representative strategy
     * @param modelGenerationFilter
     *            model generation filter
     */
    public TBehaviorModelTableGeneration(final ISignatureCreationStrategy signatureCreationStrategy,
            final IRepresentativeStrategy strategy, final ModelGenerationFilter modelGenerationFilter,
            final boolean keepEmptyTransitions) {
        super();

        this.modelTable = new DynamicBehaviorModelTable(signatureCreationStrategy, strategy, modelGenerationFilter);
        this.keepEmptyTransitions = keepEmptyTransitions;

    }

    @Override
    protected void execute(final EntryCallSequenceModel entryCallSequenceModel) {
        final List<UserSession> userSessions = entryCallSequenceModel.getUserSessions();

        for (final UserSession userSession : userSessions) {

            final List<EntryCallEvent> entryCalls = userSession.getEvents();

            EntryCallEvent lastCall = null;
            for (final EntryCallEvent eventCall : entryCalls) {
                final boolean isAllowed = this.modelTable.isAllowedSignature(eventCall.getOperationSignature());

                if ((lastCall != null) && isAllowed) {
                    System.out.println(eventCall.getClassSignature() + eventCall.getOperationSignature());
                    this.modelTable.addTransition(lastCall, eventCall);
                    this.modelTable.addInformation(eventCall);

                } else if (isAllowed) { // only called at first valid event (condition lastCall ==
                                        // null is not needed)
                    this.modelTable.addInformation(eventCall);

                }

                lastCall = isAllowed ? eventCall : lastCall;
            }
        }
    }

    @Override
    public void onTerminating() throws Exception {

        final BehaviorModelTable fixedTable = this.modelTable
                .toClearedFixedSizeBehaviorModelTable(this.keepEmptyTransitions);
        this.outputPort.send(fixedTable);

        super.onTerminating();
    }

    /**
     * getter
     *
     * @return output port
     */
    public OutputPort<BehaviorModelTable> getOutputPort() {
        return this.outputPort;
    }

}