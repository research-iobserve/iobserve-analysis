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
package org.iobserve.analysis.behavior.filter.similaritymatching;

import java.util.HashMap;

import org.iobserve.analysis.behavior.models.extended.BehaviorModel;
import org.iobserve.analysis.behavior.models.extended.CallInformation;
import org.iobserve.analysis.behavior.models.extended.EntryCallNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parameter distance function for JPetStore application
 *
 * @author Jannis Kuckei
 *
 */
public class JPetStoreParameterMetric implements IParameterMetricStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(JPetStoreParameterMetric.class);

    private final HashMap<String, String> productTable;
    private final HashMap<String, String> categoryTable;

    public JPetStoreParameterMetric() {
        // Fill product associations
        this.productTable = new HashMap<>();
        this.productTable.put("EST-1", "FI-SW-01");
        this.productTable.put("EST-2", "FI-SW-01");
        this.productTable.put("EST-3", "FI-SW-02");
        this.productTable.put("EST-4", "FI-FW-01");
        this.productTable.put("EST-5", "FI-FW-01");
        this.productTable.put("EST-6", "K9-BD-01");
        this.productTable.put("EST-7", "K9-BD-01");
        this.productTable.put("EST-8", "K9-PO-02");
        this.productTable.put("EST-9", "K9-DL-01");
        this.productTable.put("EST-10", "K9-DL-01");
        this.productTable.put("EST-11", "RP-SN-01");
        this.productTable.put("EST-12", "RP-SN-01");
        this.productTable.put("EST-13", "RP-LI-02");
        this.productTable.put("EST-14", "FL-DSH-01");
        this.productTable.put("EST-15", "FL-DSH-01");
        this.productTable.put("EST-16", "FL-DLH-02");
        this.productTable.put("EST-17", "FL-DLH-02");
        this.productTable.put("EST-18", "AV-CB-01");
        this.productTable.put("EST-19", "AV-SB-02");
        this.productTable.put("EST-20", "FI-FW-02");
        this.productTable.put("EST-21", "FI-FW-02");
        this.productTable.put("EST-22", "K9-RT-02");
        this.productTable.put("EST-23", "K9-RT-02");
        this.productTable.put("EST-24", "K9-RT-02");
        this.productTable.put("EST-25", "K9-RT-02");
        this.productTable.put("EST-26", "K9-CW-01");
        this.productTable.put("EST-27", "K9-CW-01");
        this.productTable.put("EST-28", "K9-RT-01");

        // Fill category associations
        this.categoryTable = new HashMap<>();
        this.categoryTable.put("FI", "FISH");
        this.categoryTable.put("K9", "DOGS");
        this.categoryTable.put("RP", "REPTILES");
        this.categoryTable.put("AV", "BIRDS");
        this.categoryTable.put("FL", "CATS");
    }

    @Override
    public double getDistance(final BehaviorModel a, final BehaviorModel b) {
        // If a node is not shared by the other behavior model, it will be compared to
        // this empty dummy node
        final EntryCallNode dummyNode = new EntryCallNode();
        double distance = 0;
        double comparisons = 0;

        EntryCallNode matchingNode;
        for (final EntryCallNode nodeA : a.getNodes()) {
            matchingNode = b.findNode(nodeA.getSignature()).orElse(dummyNode);
            distance += this.getNodeDistance(nodeA, matchingNode);
            comparisons++;
        }

        // Some nodes in model b will still be unmatched. We compare all the nodes of b
        // for which we cannot find matches with the dummy node.
        for (final EntryCallNode nodeB : b.getNodes()) {
            matchingNode = b.findNode(nodeB.getSignature()).orElse(null);
            if (matchingNode == null) { // We already compared it in the first loop if it's not null
                distance += this.getNodeDistance(nodeB, dummyNode);
                comparisons++;
            }
        }

        return comparisons > 0 ? distance / comparisons : 0;
    }

    private double getNodeDistance(final EntryCallNode nodeA, final EntryCallNode nodeB) {
        final CallInformation[] ciA = nodeA.getEntryCallInformation();
        final CallInformation[] ciB = nodeB.getEntryCallInformation();

        // Distance is none if both have no call info
        if ((ciA.length == 0) && (ciB.length == 0)) {
            return 0;
        }

        int distance = 0;

        // Find out call info type (always homogenous for the same node type). Note that
        // one of these arrays ahs to contain at least one item due to previous
        // condition.
        final CallInformation sample = ciA.length > 0 ? ciA[0] : ciB[0];
        final boolean comparingItems = sample.getInformationSignature().equals("itemId");
        final boolean comparingProducts = sample.getInformationSignature().equals("productId");
        final boolean comparingCategories = sample.getInformationSignature().equals("categoryId");

        // Return if unknown information signature
        if (!(comparingItems || comparingProducts || comparingCategories)) {
            return 0;
        }

        // Match equal call info (all types)
        for (int a = 0; a < ciA.length; a++) {
            for (int b = 0; b < ciB.length; b++) {
                if (ciA[a].hasSameParameters(ciB[b])) {
                    this.matchCounts(a, b, ciA, ciB);
                    break;
                }
            }
        }

        // Match call info of same product (items only)
        if (comparingItems) {
            for (int a = 0; a < ciA.length; a++) {
                for (int b = 0; b < ciB.length; b++) {
                    // Skip trying to match this if either have been matched completely
                    if ((ciA[a].getCount() == 0) || (ciB[b].getCount() == 0)) {
                        continue;
                    }

                    if (this.hasSameProduct(ciA[a], ciB[b])) {
                        this.matchCounts(a, b, ciA, ciB);
                        distance += 1;
                        break;
                    }
                }
            }
        }

        // Match call info of same category (items and products only)
        if (comparingItems || comparingProducts) {
            for (int a = 0; a < ciA.length; a++) {
                for (int b = 0; b < ciB.length; b++) {
                    // Skip trying to match this if either have been matched completely
                    if ((ciA[a].getCount() == 0) || (ciB[b].getCount() == 0)) {
                        continue;
                    }

                    if (this.hasSameCategory(ciA[a], ciB[b])) {
                        this.matchCounts(a, b, ciA, ciB);
                        // Add distance of 1 when comparing products, distance of 2 for items
                        distance += comparingItems ? 2 : 1;
                        break;
                    }
                }
            }
        }

        // Find remaining non-matched call infos and match them, adding 1, 2 or 3
        // distance for
        // each match when comparing categories, products or items respectively, and 4
        // distance for any remaining call infos in either node when
        // the other one is empty
        final int distanceFactor = 3 - (comparingProducts ? 1 : 0) - (comparingCategories ? 2 : 0);
        int remainingA = 0;
        int remainingB = 0;
        for (final CallInformation c : ciA) {
            remainingA += c.getCount();
        }
        for (final CallInformation c : ciB) {
            remainingB += c.getCount();
        }
        final int remainingDiff = Math.abs(remainingA - remainingB);
        // Distance for every matchable element
        distance += Math.min(remainingA, remainingB) * distanceFactor;
        // Distance for every unmatchable element
        distance += remainingDiff * 4;

        return distance;
    }

    private void matchCounts(final int a, final int b, final CallInformation[] ciA, final CallInformation[] ciB) {
        final int countShared = Math.min(ciA[a].getCount(), ciB[b].getCount());
        ciA[a] = new CallInformation(ciA[a].getInformationSignature(), ciA[a].getInformationParameter(),
                ciA[a].getCount() - countShared);
        ciB[b] = new CallInformation(ciB[b].getInformationSignature(), ciB[b].getInformationParameter(),
                ciB[b].getCount() - countShared);
    }

    private boolean hasSameProduct(final CallInformation a, final CallInformation b) {
        return this.productTable.getOrDefault(a.getInformationParameter(), "X")
                .equals(this.productTable.getOrDefault(b.getInformationParameter(), "Y"));
    }

    private boolean hasSameCategory(final CallInformation a, final CallInformation b) {
        String productA, productB;
        if (a.getInformationSignature().equals("itemId")) {
            productA = this.productTable.get(a.getInformationParameter());
            productB = this.productTable.get(b.getInformationParameter());
        } else {
            productA = a.getInformationParameter();
            productB = b.getInformationParameter();
        }

        return this.categoryTable.getOrDefault(productA.substring(0, 2), "X")
                .equals(this.categoryTable.getOrDefault(productB.substring(0, 2), "Y"));
    }
}
