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
package org.iobserve.analysis.clustering.filter.similaritymatching;

import java.util.HashMap;

import org.iobserve.analysis.clustering.behaviormodels.BehaviorModel;
import org.iobserve.analysis.clustering.behaviormodels.CallInformation;
import org.iobserve.analysis.clustering.behaviormodels.EntryCallNode;

/**
 * Parameter distance function for JPetStore application
 *
 * @author Jannis Kuckei
 *
 */
public class JPetStoreParameterMetric implements IParameterMetricStrategy {
    private HashMap<String, String> productTable;
    private HashMap<String, String> categoryTable;

    public JPetStoreParameterMetric() {
        // Fill product associations
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
        this.categoryTable.put("FI", "FISH");
        this.categoryTable.put("K9", "DOGS");
        this.categoryTable.put("RP", "REPTILES");
        this.categoryTable.put("AV", "BIRDS");
        this.categoryTable.put("FL", "FISH");
    }

    @Override
    public double getDistance(final BehaviorModel a, final BehaviorModel b) {
        return 0;
    }

    private double getNodeDistance(final EntryCallNode nodeA, final EntryCallNode nodeB) {
        final CallInformation[] ciA = (CallInformation[]) nodeA.getEntryCallInformation().values().toArray();
        final CallInformation[] ciB = (CallInformation[]) nodeB.getEntryCallInformation().values().toArray();

        // Distance is none if both have no call info
        if ((ciA.length == 0) && (ciB.length == 0)) {
            return 0;
        }

        int distance = 0;

        // Find out call info type (always homogenous for the same node type). Note that
        // one of these arrays ahs to contain at least one item due to previous
        // condition.
        final CallInformation sample = ciA.length > 0 ? ciA[0] : ciB[0];
        final boolean comparingItems = sample.getInformationSignature() == "itemId";
        final boolean comparingProducts = sample.getInformationSignature() == "productId";
        final boolean comparingCategories = sample.getInformationSignature() == "categoryId";

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
        for (final CallInformation c : ciA) {
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
        final int countDiff = Math.abs(ciA[a].getCount() - ciB[b].getCount());
        ciA[a] = new CallInformation(ciA[a].getInformationSignature(), ciA[a].getInformationParameter(),
                ciA[a].getCount() - countDiff);
        ciB[b] = new CallInformation(ciB[b].getInformationSignature(), ciB[b].getInformationParameter(),
                ciB[b].getCount() - countDiff);
    }

    private boolean hasSameProduct(final CallInformation a, final CallInformation b) {
        return this.productTable.getOrDefault(a.getInformationParameter(), "X") == this.productTable
                .getOrDefault(b.getInformationParameter(), "Y");
    }

    private boolean hasSameCategory(final CallInformation a, final CallInformation b) {
        String productA, productB;
        if (a.getInformationSignature() == "itemId") {
            productA = this.productTable.get(a.getInformationParameter());
            productB = this.productTable.get(b.getInformationParameter());
        } else {
            productA = a.getInformationParameter();
            productB = b.getInformationParameter();
        }

        return this.categoryTable.get(productA.substring(0, 3)) == this.categoryTable.get(productB.substring(0, 3));
    }
}
