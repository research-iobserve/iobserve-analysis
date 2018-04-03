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
package org.iobserve.analysis.behavior.filter.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.iobserve.analysis.behavior.filter.models.configuration.IRepresentativeStrategy;
import org.iobserve.analysis.behavior.models.basic.CallInformation;

/**
 * aggregates CallInformation with the same signature.
 *
 * @author Christoph Dornieden
 *
 */

public class AggregatedCallInformation {

    private final String signature;
    private List<Double> callInformationCodes;
    private Double representativeCode;
    private final IRepresentativeStrategy strategy;

    /**
     * constructor.
     *
     * @param strategy
     *            strategy
     * @param signature
     *            signature
     */
    public AggregatedCallInformation(final IRepresentativeStrategy strategy, final String signature) {
        this.signature = signature;
        this.callInformationCodes = new ArrayList<>();
        this.strategy = strategy;
    }

    /**
     * constructor.
     *
     * @param strategy
     *            strategy
     * @param callInformation
     *            callInfoprmation
     */
    public AggregatedCallInformation(final IRepresentativeStrategy strategy, final CallInformation callInformation) {
        this.signature = callInformation.getInformationSignature();
        this.representativeCode = callInformation.getInformationCode();
        this.callInformationCodes = new ArrayList<>();
        this.callInformationCodes.add(callInformation.getInformationCode());
        this.strategy = strategy;

    }

    /**
     *
     * Add callInformation with matching signature.
     *
     * @param callInformation
     *            callInformation
     * @throws IllegalArgumentException
     *             if callInformation signature does not match
     */
    public void addCallInformation(final CallInformation callInformation) throws IllegalArgumentException {
        if (this.belongsTo(callInformation)) {
            this.callInformationCodes.add(callInformation.getInformationCode());
            this.representativeCode = this.callInformationCodes.isEmpty() ? callInformation.getInformationCode()
                    : this.strategy.findRepresentativeCode(this.signature, this.callInformationCodes);
        } else {
            throw new IllegalArgumentException(
                    "callInformation signature does not match mit the aggregation signature");
        }
    }

    /**
     * Checks weather the given callInformation belongs to the aggregation.
     *
     * @param callInformation
     *            callInformation
     * @return true if callInformation belongs to the aggregation, false else
     */
    public boolean belongsTo(final CallInformation callInformation) {
        return this.getSignature().equals(callInformation.getInformationSignature());
    }

    public String getSignature() {
        return this.signature;
    }

    /**
     * Set call informations.
     *
     * @param callInformations
     *            the call information
     */
    public void setCallInformations(final List<CallInformation> callInformations) {

        this.callInformationCodes = callInformations.stream()
                .map(callInformation -> callInformation.getInformationCode()).collect(Collectors.toList());
        this.representativeCode = this.strategy.findRepresentativeCode(this.signature, this.callInformationCodes);
    }

    public Double getRepresentativeCode() {
        return this.representativeCode;
    }

    /**
     * Create call information for the present signature and representativeCode.
     *
     * @return the representative
     */
    public CallInformation createCallInformation() {
        // TODO This method is a factory method and should be removed from this class
        return new CallInformation(this.signature, this.representativeCode);
    }

    /**
     * delete all information of the aggregation.
     */
    public void clearInformations() {
        this.callInformationCodes = new ArrayList<>();
    }

    /**
     * returns a cleared copy of this.
     *
     * @return cleared copy
     */
    public AggregatedCallInformation getClearedCopy() {
        return new AggregatedCallInformation(this.strategy, this.signature);
    }

    /**
     * size of the aggregatedCallInformation.
     *
     * @return number of callInformationCodes
     */
    public int size() {
        return this.callInformationCodes.size();
    }

}