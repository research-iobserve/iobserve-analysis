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
package org.iobserve.analysis.behavior.clustering.similaritymatching;

import java.util.ArrayList;
import java.util.List;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the grouping phase of Similarity Matching.
 *
 * @author Jannis Kuckei
 *
 */
public class GroupingBehaviorStage extends AbstractConsumerStage<Double[][]> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupingBehaviorStage.class);

    private final OutputPort<Integer[][]> outputPort = this.createOutputPort();

    private final double structureSimilarityRadius;
    private final double parameterSimilarityRadius;

    private List<List<Integer>> groups = new ArrayList<>();

    private Double[][] vectors;

    /**
     * Constructor.
     *
     * @param structureSimilarityRadius
     *            The similarity radius specifies how similar two model's vectors have to be to get
     *            aggregated into the same group
     * @param parameterSimilarityRadius
     *            radius to which extend values are considered similar
     */
    public GroupingBehaviorStage(final double structureSimilarityRadius, final double parameterSimilarityRadius) {
        super();

        this.structureSimilarityRadius = structureSimilarityRadius;
        this.parameterSimilarityRadius = parameterSimilarityRadius;
    }

    @Override
    protected void execute(final Double[][] receivedVectors) {
        this.vectors = receivedVectors;

        for (int i = 0; i < receivedVectors.length; i++) {
            final List<Integer> group = this.findGroup(receivedVectors[i]);

            if (group == null) {
                final List<Integer> newGroup = new ArrayList<>();
                newGroup.add(i);
                this.groups.add(newGroup);
            } else {
                group.add(i);
            }
        }

        /** Convert List<List<Integer>> to Integer[][] for sending */
        final Integer[][] aGroups = new Integer[this.groups.size()][];
        int i = 0;
        for (final List<Integer> g : this.groups) {
            aGroups[i++] = g.toArray(new Integer[g.size()]);
        }

        /** Clear state */
        this.groups = new ArrayList<>();

        this.outputPort.send(aGroups);

        GroupingBehaviorStage.LOGGER.debug("Sent {} groups to next stage", aGroups.length);
    }

    /**
     * Finds the first matching group similar enough for the supplied vector.
     *
     * @param vector
     *            The vector to find a group for
     * @return Returns the group if one has been found, or null otherwise
     */
    private List<Integer> findGroup(final Double[] vector) {
        /** First matching group found is selected */
        for (final List<Integer> group : this.groups) {
            boolean matchAll = true;
            for (final Integer index : group) {
                if (!this.match(vector, this.vectors[index])) {
                    matchAll = false;
                    break;
                }
            }

            if (matchAll) {
                return group;
            }
        }

        return null;
    }

    /**
     * Checks if two vectors are considered similar.
     *
     * @param a
     *
     * @param b
     *
     * @return Returns true if the vectors are considered similar, or false otherwise
     */
    private boolean match(final Double[] a, final Double[] b) {
        /** a and b should always have same length */
        for (int i = 0; i < a.length; i++) {
            // Use structure similarity radius for even entries, parameter radius for odd
            // entries
            final double radius = i % 2 == 0 ? this.structureSimilarityRadius : this.parameterSimilarityRadius;
            if (Math.abs(a[i] - b[i]) >= radius) {
                return false;
            }
        }

        return true;
    }

    public OutputPort<Integer[][]> getOutputPort() {
        return this.outputPort;
    }
}