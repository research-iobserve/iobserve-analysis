/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.clustering.behaviormodels;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the an entry call.
 *
 * @author Christoph Dornieden
 *
 */

public class EntryCallNode {
    private String signature;
    private final Map<String, CallInformation> entryCallInformation;

    /**
     * constructor.
     */
    public EntryCallNode() {
        this.entryCallInformation = new HashMap<>();
    }

    /**
     * constructor.
     *
     * @param signature
     *            signature
     */
    public EntryCallNode(final String signature) {
        this();
        this.signature = signature;

    }

    public Map<String, CallInformation> getEntryCallInformation() {
        return this.entryCallInformation;
    }

    public void setSignature(final String signature) {
        this.signature = signature;
    }

    public String getSignature() {
        return this.signature;
    }

    /**
     * adds call information with signatures not present in the entry call
     * information set.
     *
     * @param callInformations
     *            callInformations
     */
    public void mergeInformation(final Map<String, CallInformation> callInformations) {
        callInformations.values().stream().forEach(this::mergeInformation);
    }

    /**
     * adds a call information with a signature not present in the entry call
     * information set.
     *
     * @param callInformation
     *            callInformation
     */
    public void mergeInformation(final CallInformation callInformation) {
        final String key = callInformation.getInformationSignature() + callInformation.getInformationParameter();
        final CallInformation match = this.entryCallInformation.get(key);

        if (match == null) {
            this.entryCallInformation.put(key, callInformation);
        } else {
            match.addCount(callInformation.getCount());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof EntryCallNode) {
            // compare signatures
            final EntryCallNode entryCallNode = (EntryCallNode) obj;
            return this.signature.equals(entryCallNode.getSignature());

        } else {
            return super.equals(obj);
        }
    }

    @Override
    public String toString() {
        return "{" + this.getSignature() + "}";
    }
}
