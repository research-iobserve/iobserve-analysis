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

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.iobserve.analysis.cdoruserbehavior.util.SingleOrNoneCollector;

/**
 * Represents the an entry call
 *
 * @author Christoph Dornieden
 *
 */

public class EntryCallNode {
    private final String signature;
    private final Set<CallInformation> entryCallInformations;

    /**
     * constructor
     *
     * @param signature
     *            signature
     */
    public EntryCallNode(final String signature) {
        this.signature = signature;
        this.entryCallInformations = new HashSet<>();
    }

    /**
     * adds call information with signatures not present in the entry call information set
     *
     * @param callInformations
     *            callInformations
     */
    public void mergeInformation(final Set<CallInformation> callInformations) {
        callInformations.stream().forEach(this::mergeInformation);
    }

    /**
     * adds a call information with a signature not present in the entry call information set
     *
     * @param callInformation
     *            callInformation
     */
    public void mergeInformation(final CallInformation callInformation) {
        final Optional<CallInformation> match = this.entryCallInformations.stream().filter(
                information -> information.getInformationSignature().equals(callInformation.getInformationSignature()))
                .collect(new SingleOrNoneCollector<>());

        if (!match.isPresent()) {
            this.entryCallInformations.add(callInformation);
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
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * getter
     *
     * @return the signature
     */
    public String getSignature() {
        return this.signature;
    }

    /**
     * getter
     *
     * @return the entryCallInformation
     */
    public Set<CallInformation> getEntryCallInformation() {
        return this.entryCallInformations;
    }

    @Override
    public String toString() {
        return "{" + this.getSignature() + "}";
    }

}
