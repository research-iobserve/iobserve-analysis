/* Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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

import java.util.HashSet;
import java.util.Set;

/**
 * aggregates CallInformation with the same signature
 *
 * @author Christoph Dornieden
 *
 */

public abstract class AbstractAggregatedCallInformation {

    protected Set<CallInformation> callInformations;

    private CallInformation representative;

    public AbstractAggregatedCallInformation() {
        this.callInformations = new HashSet<>();
        this.representative = null;
    }

    /**
     *
     * @return representative of callInformations
     */
    protected abstract CallInformation findRepresentative();

    /**
     *
     * Add callInformation with matching signature
     *
     * @param callInformation
     *            callInformation
     * @throws IllegalArgumentException
     *             if callInformation signature does not match
     */
    public void addCallInformation(final CallInformation callInformation) throws IllegalArgumentException {
        if (this.belongsTo(callInformation)) {
            this.callInformations.add(callInformation);
            this.representative = this.findRepresentative();
        } else {
            throw new IllegalArgumentException(
                    "callInformation signature does not match mit the aggregation signature");
        }
    }

    /**
     * Checks weather the given callInformation belongs to the aggregation
     *
     * @param callInformation
     *            callInformation
     * @return true if callInformation belongs to the aggregation, false else
     */
    public boolean belongsTo(final CallInformation callInformation) {
        return this.getSignature().equals(callInformation);
    }

    /**
     * getter
     *
     * @return signature of the representative
     */
    public String getSignature() {
        return this.representative.getInformationSignature();
    }

    /**
     * setter
     *
     * @param callInformations
     *            the callInformations to set
     */
    public void setCallInformations(final Set<CallInformation> callInformations) {
        this.callInformations = callInformations;
        this.representative = this.findRepresentative();
    }

    /**
     * getter
     *
     * @return the representative
     */
    public CallInformation getRepresentative() {
        return this.representative;
    }

}