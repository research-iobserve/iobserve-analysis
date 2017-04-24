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
package org.iobserve.analysis.cdoruserbehavior.filter.models.configuration;

import java.util.HashSet;
import java.util.List;
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
    private final Set<String> categoryCodes;

    /**
     * constructor
     */
    public JPetstoreStrategy() {
        this.categoryCodes = new HashSet<>();
        this.categoryCodes.add("FISH");
        this.categoryCodes.add("CATS");
        this.categoryCodes.add("DOGS");
        this.categoryCodes.add("REPTILES");
        this.categoryCodes.add("BIRDS");
    }

    @Override
    public Double findRepresentativeCode(final String signature, List<Double> callInformationCodes) {

        return this.categoryStrategy(callInformationCodes);
        // if (this.categoryCodes.contains(signature)) {
        // return this.categoryStrategy(callInformationCodes);
        // } else {
        // return this.defaultStratey(signature, callInformationCodes);
        // }

    }

    /**
     * find the representatives for categories by summing up all elements
     *
     * @param callInformationCodes
     *            callInformationCodes
     * @return representative value
     */
    private final Double categoryStrategy(List<Double> callInformationCodes) {
        final int num = callInformationCodes.size();
        return Double.valueOf(num);
    }

    /**
     * default representative strategy for jpetstore
     *
     * @param signature
     *            signature
     * @param callInformationCodes
     *            callInformationCodes
     * @return representative code
     */
    private final Double defaultStratey(String signature, List<Double> callInformationCodes) {
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
