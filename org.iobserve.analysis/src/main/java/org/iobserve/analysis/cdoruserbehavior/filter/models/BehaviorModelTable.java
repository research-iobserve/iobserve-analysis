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

package org.iobserve.analysis.cdoruserbehavior.filter.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.math3.util.Pair;
import org.iobserve.analysis.cdoruserbehavior.filter.models.configuration.ISignatureCreationStrategy;
import org.iobserve.analysis.cdoruserbehavior.util.SingleOrNoneCollector;
import org.iobserve.analysis.data.EntryCallEvent;
import org.iobserve.analysis.data.ExtendedEntryCallEvent;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * table representation of a behavior model
 *
 * @author Christoph Dornieden
 *
 */

public class BehaviorModelTable extends AbstractBehaviorModelTable {

    private final Map<String, Pair<Integer, AggregatedCallInformation[]>> signatures;
    private final String[] inverseSignatures;
    private final Integer[][] transitions;

    /**
     * constructor
     *
     * @param signatures
     *            signatures
     */
    public BehaviorModelTable(final ISignatureCreationStrategy signatureCreationStrategy, final String[] signatures) {
        super(signatureCreationStrategy);

        final int size = signatures.length;
        this.inverseSignatures = signatures;

        this.signatures = new HashMap<>();
        for (int i = 0; i < signatures.length; i++) {
            this.signatures.put(signatures[i], new Pair<>(i, new AggregatedCallInformation[0]));
        }

        this.transitions = new Integer[size][size];
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

    public BehaviorModelTable(final ISignatureCreationStrategy signatureCreationStrategy,
            final Map<String, Pair<Integer, AggregatedCallInformation[]>> signatures, final String[] reverseSignatures,
            final Integer[][] transitions) {
        super(signatureCreationStrategy);

        // verify input
        final int length = signatures.size();

        if ((length == reverseSignatures.length) && (length == transitions.length)) {

            for (final Integer[] transition : transitions) {
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
        this.inverseSignatures = reverseSignatures;
        this.transitions = transitions;
    }

    @Override
    public void addTransition(final EntryCallEvent from, final EntryCallEvent to) {
        final String fromSignature = this.getSignatureFromEvent(from);
        final String toSignature = this.getSignatureFromEvent(to);

        if (!(this.isAllowedSignature(fromSignature) && this.isAllowedSignature(toSignature))) {
            throw new IllegalArgumentException("event signature not allowed");
        }

        final Integer fromIndex = this.signatures.get(fromSignature).getFirst();
        final Integer toIndex = this.signatures.get(toSignature).getFirst();

        final int currentTransitionValue = this.transitions[fromIndex][toIndex];

        if (currentTransitionValue == AbstractBehaviorModelTable.EMPTY_TRANSITION) {
            throw new IllegalArgumentException("transition not intended");
        }

        this.transitions[fromIndex][toIndex] = currentTransitionValue + 1;
    }

    @Override
    public boolean isAllowedSignature(final String signature) {
        return this.signatures.containsKey(signature);
    }

    @Override
    public void addInformation(final ExtendedEntryCallEvent event) {
        final ObjectMapper objectMapper = new ObjectMapper();
        final String eventSignature = this.getSignatureFromEvent(event);
        final ArrayList<CallInformation> newCallInformations;

        try {
            newCallInformations = objectMapper.readValue(event.getInformations(),
                    new TypeReference<ArrayList<CallInformation>>() {
                    });

            final List<AggregatedCallInformation> aggCallInformations = Arrays
                    .asList(this.signatures.get(eventSignature).getSecond());

            for (final CallInformation newCallInformation : newCallInformations) {

                // add new CallInfromation to the aggregation correctly
                final Optional<AggregatedCallInformation> match = aggCallInformations.stream()
                        .filter(aggCallInformation -> aggCallInformation.belongsTo(newCallInformation))
                        .collect(new SingleOrNoneCollector<>());

                match.ifPresent(aggCallInformation -> aggCallInformation.addCallInformation(newCallInformation));
            }

        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Creates a cleared Copy
     *
     * @return cleared copy
     */
    public BehaviorModelTable getClearedCopy() {
        final Map<String, Pair<Integer, AggregatedCallInformation[]>> clearedSignatures = new HashMap<>();

        for (final String signature : this.signatures.keySet()) {

            final Pair<Integer, AggregatedCallInformation[]> valuePair = this.signatures.get(signature);

            final AggregatedCallInformation[] aggregatedCallInformations = Arrays.stream(valuePair.getSecond())
                    .map(AggregatedCallInformation::getClearedCopy).toArray(AggregatedCallInformation[]::new);

            final Pair<Integer, AggregatedCallInformation[]> fixedPair = new Pair<>(valuePair.getFirst(),
                    aggregatedCallInformations);
            clearedSignatures.put(signature, fixedPair);
        }
        final Integer[][] clearedTransitions = new Integer[clearedSignatures.size()][clearedSignatures.size()];

        Arrays.stream(clearedTransitions).forEach(t -> Arrays.fill(t, 0));

        return new BehaviorModelTable(this.getSignatureCreationStrategy(), clearedSignatures, this.inverseSignatures,
                clearedTransitions);

    }

    /**
     * create an Instances object for clustering
     *
     * @return instance
     */
    public Instances toInstances() {
        final FastVector fastVector = new FastVector();

        // add transitions
        for (int i = 0; i < this.signatures.size(); i++) {
            for (int j = 0; j < this.signatures.size(); j++) {
                if (this.transitions[i][j] > AbstractBehaviorModelTable.TRANSITION_THRESHOLD) {
                    final Attribute attribute = new Attribute(
                            AbstractBehaviorModelTable.EDGE_INDICATOR + this.inverseSignatures[i]
                                    + AbstractBehaviorModelTable.EDGE_DIVIDER + this.inverseSignatures[j]);

                    fastVector.addElement(attribute);

                } else {
                    continue;
                }
            }
        }

        // add informations
        this.signatures.values().stream().forEach(pair -> Arrays.stream(pair.getSecond())
                .forEach(callInformation -> fastVector.addElement(new Attribute(
                        AbstractBehaviorModelTable.INFORMATION_INDICATOR + this.inverseSignatures[pair.getFirst()]
                                + AbstractBehaviorModelTable.INFORMATION_DIVIDER + callInformation.getSignature()))));
        // TODO name
        final Instances instances = new Instances("Test", fastVector, 0);
        final Instance instance = this.toInstance();
        instances.add(instance);

        return instances;
    }

    /**
     * returns an instance vector
     *
     * @return instance
     */
    public Instance toInstance() {
        final List<Double> attValues = new ArrayList<>();

        // add transitions
        for (int i = 0; i < this.signatures.size(); i++) {
            for (int j = 0; j < this.signatures.size(); j++) {

                if (this.transitions[i][j] > AbstractBehaviorModelTable.TRANSITION_THRESHOLD) {
                    attValues.add(Double.valueOf(this.transitions[i][j]));
                } else {
                    continue;
                }
            }
        }

        this.signatures.values().stream().forEach(pair -> Arrays.stream(pair.getSecond())
                .forEach(callInformation -> attValues.add(callInformation.getRepresentativeCode())));

        final double[] attArray = new double[attValues.size()];

        for (int i = 0; i < attValues.size(); i++) {
            attArray[i] = attValues.get(i);
        }

        final Instance instance = new Instance(1.0, attArray);
        return instance;

    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final String string = this.signatures.keySet().stream().reduce("\n", (s1, s2) -> s1 + "\n" + s2) + "\n";
        return string;
    }

}
