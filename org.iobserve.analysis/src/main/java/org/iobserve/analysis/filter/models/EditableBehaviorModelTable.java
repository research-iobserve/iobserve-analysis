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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.math3.util.Pair;
import org.iobserve.analysis.data.EntryCallEvent;
import org.iobserve.analysis.data.ExtendedEntryCallEvent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * table representation of a behavior model
 *
 * @author Christoph Dornieden
 *
 */

public class EditableBehaviorModelTable extends AbstractBehaviorModelTable {

    // a map for adding and updating transitions
    private final Map<String, Pair<Integer, ArrayList<AbstractAggregatedCallInformation>>> signatures;

    // a list for getting transitions
    private final ArrayList<String> signatureArray;

    // transition matrix
    private final LinkedList<LinkedList<Integer>> transitions;

    // filter list of regex expressions
    private final ArrayList<String> filterList;
    private final boolean blacklistMode;

    /**
     * simple constructor
     */
    public EditableBehaviorModelTable() {
        this.signatures = new HashMap<>();
        this.signatureArray = new ArrayList<>();
        this.transitions = new LinkedList<>();
        this.filterList = new ArrayList<>();
        this.blacklistMode = true;
    }

    /**
     * advanced constructor
     *
     * @param filterList
     *            list of regex expressions to filter the signatures
     * @param blacklistMode
     *            true if the filterList is a blacklist, else a whitelist
     */

    public EditableBehaviorModelTable(final Collection<String> filterList, final boolean blacklistMode) {
        this.signatures = new HashMap<>();
        this.signatureArray = new ArrayList<>();
        this.transitions = new LinkedList<>();
        this.filterList = new ArrayList<>(filterList);
        this.blacklistMode = blacklistMode;

    }

    // TODO constructor with signature set

    // TODO update without adding

    /**
     *
     * @param from
     *            where the transition comes
     * @param to
     *            where the transition goes
     * @return true if transition is allowed
     */
    @Override
    public void addTransition(final EntryCallEvent from, final EntryCallEvent to) throws IllegalArgumentException {

        final String fromSignature = AbstractBehaviorModelTable.getSignatureFromEvent(from);
        final String toSignature = AbstractBehaviorModelTable.getSignatureFromEvent(to);

        if (!(this.isAllowedSignature(fromSignature) && this.isAllowedSignature(toSignature))) {
            throw new IllegalArgumentException("event signature not allowed");
        }

        final Integer fromIndex = this.getSignatureIndex(fromSignature);
        final Integer toIndex = this.getSignatureIndex(toSignature);

        final Integer transitionCount = this.transitions.get(fromIndex).get(toIndex);
        this.transitions.get(fromIndex).set(toIndex, transitionCount + 1);
    }

    /**
     *
     * @param signature
     *            operation signature
     * @return index of the signature in the transition table
     */
    private int getSignatureIndex(final String signature) {
        final Integer index = this.signatures.containsKey(signature) ? this.signatures.get(signature).getFirst()
                : this.addSignature(signature);
        return index;
    }

    /**
     * add a callinformation to the aggregated callinformation
     * 
     * @param event
     *            event
     */
    public void addInformation(final EntryCallEvent event) {
        if (event instanceof ExtendedEntryCallEvent) {
            final ObjectMapper objectMapper = new ObjectMapper();
            final String eventSignature = AbstractBehaviorModelTable.getSignatureFromEvent(event);
            final ExtendedEntryCallEvent extFrom = (ExtendedEntryCallEvent) event;
            final ArrayList<CallInformation> newCallInformations;

            try {
                newCallInformations = objectMapper.readValue(extFrom.getInformations(),
                        new TypeReference<ArrayList<CallInformation>>() {
                        });

                final ArrayList<AbstractAggregatedCallInformation> aggCallInformations = this.signatures
                        .get(eventSignature).getSecond();

                // add new CallInfromation to the aggregation correctly
                for (final AbstractAggregatedCallInformation aggCallInformation : aggCallInformations) {
                    for (final CallInformation callInformation : newCallInformations) {

                        if (aggCallInformation.belongsTo(callInformation)) {
                            aggCallInformation.addCallInformation(callInformation);
                        }
                    }

                }

            } catch (final IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (final IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    /**
     * extends the signature matrix
     *
     * @param signature
     *            to be added
     * @return Index of new signature
     */
    private Integer addSignature(final String signature) {

        final Integer size = this.signatures.size();

        this.signatures.put(signature, new Pair<>(size, new ArrayList<>()));
        this.signatureArray.add(signature);

        // extend all columns
        for (final LinkedList<Integer> transition : this.transitions) {
            transition.addLast(0);
        }

        // create a new row in the matrix
        final Integer[] newRowArray = new Integer[1 + size];
        Arrays.fill(newRowArray, 0);
        final LinkedList<Integer> newRow = new LinkedList<>(Arrays.asList(newRowArray));

        this.transitions.addLast(newRow);

        return size;
    }

    @Override
    public boolean isAllowedSignature(final String signature) {
        boolean isAllowed = true;

        for (final String filterRule : this.filterList) {

            final boolean isMatch = signature.matches(filterRule);
            isAllowed = isAllowed && this.blacklistMode ? !isMatch : isMatch;
        }
        return isAllowed;
    }

    @Override
    public String toString() {
        final ObjectMapper objectMapper = new ObjectMapper();

        String string = "";
        String transitionString = "";
        try {
            for (final String signature : this.signatureArray) {
                string += signature + "\n";
            }

            transitionString = objectMapper.writeValueAsString(this.transitions);

            transitionString = transitionString.replaceAll("\\[\\[", "").replaceAll("\\]\\]", "");
            final String[] transitionStrings = transitionString.split("\\],\\[");

            for (final String row : transitionStrings) {
                string += row + "\n";
            }

        } catch (final JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //
        // for (final LinkedList<Integer> transitionlist : this.transitions) {
        //
        // for (final Integer transitioncount : transitionlist) {
        // string += transitionlist.toString().replaceAll("\\[|\\]", "") + ",";
        // }
        //
        // string += "\n";
        // }
        //
        return string;
    }

}