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

    /**
     * basic constructor
     */
    public AbstractAggregatedCallInformation() {
        this.callInformations = new HashSet<>();
        this.representative = null;
    }

    /**
     * constructor
     *
     * @param callInformation
     *            callInformation
     */
    public AbstractAggregatedCallInformation(final CallInformation callInformation) {
        this.representative = callInformation;
        this.callInformations = new HashSet<>();
        this.callInformations.add(callInformation);

    }

    /**
     * Find the representative of all aggregated call informations
     *
     * @return representative of callInformations
     */
    protected abstract CallInformation findRepresentative();

    /**
     *
     * @return
     */
    public abstract <T extends AbstractAggregatedCallInformation> T INSTANCE();

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
            this.representative = this.callInformations.isEmpty() ? callInformation : this.findRepresentative();
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

    /**
     *
     */
    public void clearInformations() {
        this.callInformations = new HashSet<>();
    }

}