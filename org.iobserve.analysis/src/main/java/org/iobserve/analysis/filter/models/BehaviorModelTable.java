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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.iobserve.analysis.data.EntryCallEvent;

/**
 * table representation of a behavior model
 *
 * @author Christoph Dornieden
 *
 */

public class BehaviorModelTable {
    // a map for adding and updating transitions
    private final Map<String, Integer> signatures;

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
    public BehaviorModelTable() {
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

    public BehaviorModelTable(final Collection<String> filterList, final boolean blacklistMode) {
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
    public boolean addTransition(final EntryCallEvent from, final EntryCallEvent to) {

        final String fromSignature = from.getClassSignature() + " " + from.getOperationSignature();
        final String toSignature = to.getClassSignature() + " " + to.getOperationSignature();

        if (!(this.isAllowedSignature(fromSignature) && this.isAllowedSignature(toSignature))) {
            return false;
        }

        final Integer fromIndex = this.signatures.containsKey(fromSignature) ? this.signatures.get(fromSignature)
                : this.addSignature(fromSignature);

        final Integer toIndex = this.signatures.containsKey(toSignature) ? this.signatures.get(toSignature)
                : this.addSignature(toSignature);

        final Integer transitionCount = this.transitions.get(fromIndex).get(toIndex);
        this.transitions.get(fromIndex).set(toIndex, transitionCount + 1);

        return true;
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

        this.signatures.put(signature, size);
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

    /**
     * Check weather a signature is allowed in the Model or not
     *
     * @param signature
     *            of a transition
     * @return true if signature is allowed, false else
     */
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

        String string = "";

        for (final String signature : this.signatureArray) {
            string += signature + "\n";
        }
        string += "\n";
        string += "\n";
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