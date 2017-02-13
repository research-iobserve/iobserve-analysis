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

package org.iobserve.analysis.filter.models;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.util.Pair;
import org.iobserve.analysis.data.EntryCallEvent;

/**
 * table representation of a behavior model
 *
 * @author Christoph Dornieden
 *
 */

public class BehaviorModelTable extends AbstractBehaviorModelTable {

    final Map<String, Pair<Integer, AbstractAggregatedCallInformation[]>> signatures;
    final String[] reverseSignatures;
    final long[][] transitions;

    /**
     * constructor
     *
     * @param signatures
     *            signatures
     */
    public BehaviorModelTable(final String[] signatures) {
        final int size = signatures.length;
        this.reverseSignatures = signatures;

        this.signatures = new HashMap<>();
        for (int i = 0; i < signatures.length; i++) {
            this.signatures.put(signatures[i], new Pair<>(i, new AbstractAggregatedCallInformation[0]));
        }

        this.transitions = new long[size][size];

    }

    /**
     * constructor
     *
     * @param signatures
     *            signatures of CallEvents
     * @param reverseSignatures
     *            reverse signature Map
     * @param transitions
     *            transition matrix with marked EMPTY_TRANSITION fields
     */

    public BehaviorModelTable(final Map<String, Pair<Integer, AbstractAggregatedCallInformation[]>> signatures,
            final String[] reverseSignatures, final long[][] transitions) {

        // verify input
        final int length = signatures.size();

        if ((length == reverseSignatures.length) && (length == transitions.length)) {
            for (final long[] transition : transitions) {
                if (length != transition.length) {
                    throw new IllegalArgumentException("input value size mismatch");
                }
            }
            for (int i = 0; i < length; i++) {
                if (signatures.get(reverseSignatures[i]).getFirst() != i) {
                    throw new IllegalArgumentException("signature mismatch");
                }
            }
        } else {
            throw new IllegalArgumentException("input value size mismatch");
        }

        // assign values
        this.signatures = signatures;
        this.reverseSignatures = reverseSignatures;
        this.transitions = transitions;
    }

    @Override
    public void addTransition(final EntryCallEvent from, final EntryCallEvent to) {
        final String fromSignature = AbstractBehaviorModelTable.getSignatureFromEvent(from);
        final String toSignature = AbstractBehaviorModelTable.getSignatureFromEvent(to);

        if (!(this.isAllowedSignature(fromSignature) && this.isAllowedSignature(toSignature))) {
            throw new IllegalArgumentException("event signature not allowed");
        }

        final Integer fromIndex = this.signatures.get(fromSignature).getFirst();
        final Integer toIndex = this.signatures.get(toSignature).getFirst();

        final long currentTransitionValue = this.transitions[fromIndex][toIndex];

        if (currentTransitionValue == AbstractBehaviorModelTable.EMPTY_TRANSITION) {
            throw new IllegalArgumentException("transition not intended");
        }

        this.transitions[fromIndex][toIndex] = currentTransitionValue + 1;

    }

    @Override
    public boolean isAllowedSignature(final String signature) {
        return this.signatures.containsKey(signature);
    }

}
