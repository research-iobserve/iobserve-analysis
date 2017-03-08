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
package org.iobserve.analysis.cdoruserbehavior.filter.models;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * strategy to find the representative of a list of callinformations for jPetstore
 *
 * @author Christoph Dornieden
 *
 */
public class JPetstoreStrategy implements IRepresentativeStrategy {

    @Override
    public Double findRepresentativeCode(final String signature, final Set<Double> callInformationCodes) {

        final Double representative;
        final Map<Double, Integer> candidates;
        final int informationQuotient;

        switch (signature) {
        case "ITEM-ID":
            informationQuotient = 1000;
            break;
        default:
            informationQuotient = 1;

        }

        // sum up all code occurences in a map
        candidates = callInformationCodes.stream().map(code -> code / informationQuotient)
                .collect(Collectors.groupingBy(trimmedCode -> trimmedCode, Collectors.summingInt(i -> 1)));

        // find the code with the highest occurence
        representative = candidates.keySet().stream()
                .max((a, b) -> Integer.compare(candidates.get(a), candidates.get(b))).get() * informationQuotient;

        return representative;
    }

}
