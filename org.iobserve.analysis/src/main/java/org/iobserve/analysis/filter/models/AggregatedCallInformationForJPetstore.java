/***************************************************************************
0 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 ***************************************************************************/

package org.iobserve.analysis.filter.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * aggregates CallInformation with the same signature
 *
 * @author Christoph Dornieden
 *
 */

public class AggregatedCallInformationForJPetstore extends AbstractAggregatedCallInformation {

    // TODO more efficient solution

    @Override
    protected CallInformation findRepresentative() {
        final CallInformation representative;
        final Map<Long, Set<CallInformation>> candidates = new HashMap<>();
        int informationQuotient = 1;

        switch (this.getSignature()) {
        case "ITEM-ID":
            informationQuotient = 1000;
            break;
        default:
            informationQuotient = 1;

        }

        // sort callinformations
        for (final CallInformation callInformation : this.callInformations) {
            final long code = callInformation.getInformationCode() / informationQuotient;

            if (candidates.containsKey(code)) {
                candidates.get(code).add(callInformation);
            } else {
                final Set<CallInformation> newCandidateSet = new HashSet<>();
                newCandidateSet.add(callInformation);
                candidates.put(code, newCandidateSet);
            }
        }

        // find the largest set
        Set<CallInformation> largestSet = null;
        for (final Set<CallInformation> candidateSet : candidates.values()) {
            largestSet = largestSet == null ? candidateSet : largestSet;

            largestSet = largestSet.size() < candidateSet.size() ? candidateSet : largestSet;
        }

        if (largestSet == null) {
            representative = null;
        } else {
            representative = largestSet.iterator().next();
        }

        return representative;

    }

}