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

package org.iobserve.analysis.filter.models;

import org.iobserve.analysis.data.EntryCallEvent;

/**
 * table representation of a behavior model
 *
 * @author Christoph Dornieden
 *
 */
public abstract class AbstractBehaviorModelTable {
    public static final int EMPTY_TRANSITION = -1;

    /**
     *
     * @param event
     *            event
     *
     * @return signature of the event used by this class
     */
    public static String getSignatureFromEvent(final EntryCallEvent event) {
        return event.getClassSignature() + " " + event.getOperationSignature();
    }

    /**
     * Checks if signature of Event is allowed
     *
     * @param event
     *            event
     * @return true if signature is allowed, false else
     */
    public boolean isAllowedSignature(final EntryCallEvent event) {
        final String signature = AbstractBehaviorModelTable.getSignatureFromEvent(event);
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

}
