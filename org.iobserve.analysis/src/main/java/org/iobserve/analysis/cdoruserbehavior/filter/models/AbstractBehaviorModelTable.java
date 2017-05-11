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

package org.iobserve.analysis.cdoruserbehavior.filter.models;

import java.util.regex.Pattern;

import org.iobserve.analysis.cdoruserbehavior.filter.models.configuration.ISignatureCreationStrategy;
import org.iobserve.analysis.data.EntryCallEvent;
import org.iobserve.analysis.data.ExtendedEntryCallEvent;

/**
 * table representation of a behavior model
 *
 * @author Christoph Dornieden
 *
 */
public abstract class AbstractBehaviorModelTable {
    public static final int EMPTY_TRANSITION = -1;
    public static final int TRANSITION_THRESHOLD = -1;
    public static final String EDGE_INDICATOR = "><";
    public static final String EDGE_DIVIDER = "->";
    public static final String INFORMATION_INDICATOR = "##";
    public static final String INFORMATION_DIVIDER = "~~";

    public static final Pattern EDGE_INDICATOR_PATTERN = Pattern.compile(AbstractBehaviorModelTable.EDGE_INDICATOR);
    public static final Pattern EDGE_DIVIDER_PATTERN = Pattern.compile(AbstractBehaviorModelTable.EDGE_DIVIDER);
    public static final Pattern INFORMATION_INDICATOR_PATTERN = Pattern
            .compile(AbstractBehaviorModelTable.INFORMATION_INDICATOR);
    public static final Pattern INFORMATION_DIVIDER_PATTERN = Pattern
            .compile(AbstractBehaviorModelTable.INFORMATION_DIVIDER);

    private final ISignatureCreationStrategy signatureCreationStrategy;

    /**
     * constructor
     *
     * @param signatureCreationStrategy
     *            strategy defining the signature string format
     */
    public AbstractBehaviorModelTable(final ISignatureCreationStrategy signatureCreationStrategy) {
        this.signatureCreationStrategy = signatureCreationStrategy;
    }

    /**
     *
     * @param event
     *            event
     *
     * @return signature of the event used by this class
     */
    public String getSignatureFromEvent(final EntryCallEvent event) {
        return this.signatureCreationStrategy.getSignature(event);
    }

    /**
     * Checks if signature of Event is allowed
     *
     * @param event
     *            event
     * @return true if signature is allowed, false else
     */
    public boolean isAllowedSignature(final EntryCallEvent event) {
        final String signature = this.getSignatureFromEvent(event);
        return this.isAllowedSignature(signature);
    }

    /**
     * Checks if signature of Event is allowed
     *
     * @param event
     *            event
     * @return true if signature is allowed, false else
     */
    public abstract boolean isAllowedSignature(final String signature);

    /**
     *
     * @param from
     *            where the transition comes
     * @param to
     *            where the transition goes
     */
    public abstract void addTransition(final EntryCallEvent from, final EntryCallEvent to);

    /**
     * adds call information to the behavior table for existing signatures *
     *
     * @param event
     *            event that could contain information.
     */
    public void addInformation(final EntryCallEvent event) {

        if (event instanceof ExtendedEntryCallEvent) {
            this.addInformation((ExtendedEntryCallEvent) event);
        } else {
            // TODO
        }
    }

    /**
     * getter
     *
     * @return signatureCreationStrategy
     */
    public ISignatureCreationStrategy getSignatureCreationStrategy() {
        return this.signatureCreationStrategy;
    }

    /**
     * adds call information to the behavior table for existing signatures *
     *
     * @param extendedEntryCallEvent
     *            event containing information.
     */
    public abstract void addInformation(final ExtendedEntryCallEvent extendedEntryCallEvent);

}
