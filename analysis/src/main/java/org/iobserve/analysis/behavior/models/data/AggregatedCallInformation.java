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
package org.iobserve.analysis.behavior.models.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.iobserve.analysis.behavior.models.data.configuration.IRepresentativeStrategy;
import org.iobserve.analysis.behavior.models.extended.CallInformation;

/**
 * aggregates CallInformation with the same signature.
 *
 * @author Christoph Dornieden
 *
 */

public class AggregatedCallInformation {

    private final String signature;
    private List<String> callInformationValues;
    private String representativeValue;
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
        this.callInformationValues = new ArrayList<>();
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
        this.representativeValue = callInformation.getInformationParameter();
        this.callInformationValues = new ArrayList<>();
        this.callInformationValues.add(callInformation.getInformationParameter());
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
            this.callInformationValues.add(callInformation.getInformationParameter());
            this.representativeValue = this.callInformationValues.isEmpty() ? callInformation.getInformationParameter()
                    : this.strategy.findRepresentativeValue(this.signature, this.callInformationValues);
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

        this.callInformationValues = callInformations.stream()
                .map(callInformation -> callInformation.getInformationParameter()).collect(Collectors.toList());
        this.representativeValue = this.strategy.findRepresentativeValue(this.signature, this.callInformationValues);
    }

    public String getRepresentativeValue() {
        return this.representativeValue;
    }

    /**
     * Create call information for the present signature and representativeCode.
     *
     * @return the representative
     */
    public CallInformation createCallInformation() {
        // TODO This method is a factory method and should be removed from this class
        return new CallInformation(this.signature, this.representativeValue);
    }

    /**
     * delete all information of the aggregation.
     */
    public void clearInformations() {
        this.callInformationValues = new ArrayList<>();
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
        return this.callInformationValues.size();
    }

}