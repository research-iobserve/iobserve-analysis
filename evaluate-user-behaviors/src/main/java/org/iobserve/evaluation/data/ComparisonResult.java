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
package org.iobserve.evaluation.data;

import java.util.ArrayList;
import java.util.List;

import org.iobserve.analysis.clustering.filter.models.EntryCallEdge;
import org.iobserve.analysis.clustering.filter.models.EntryCallNode;

/**
 * @author Reiner Jung
 *
 * @since 0.0.2
 *
 */
public class ComparisonResult {

    private final List<NodeDifference> nodeDifferences = new ArrayList<>();
    private final List<EntryCallNode> missingNodes = new ArrayList<>();
    private final List<EntryCallNode> additionalNodes = new ArrayList<>();
    private final List<EntryCallNode> similarNodes = new ArrayList<>();
    private final List<EntryCallNode> baselineNodes = new ArrayList<>();
    private final List<EntryCallNode> testModelNodes = new ArrayList<>();

    private final List<EntryCallEdge> baselineEdges = new ArrayList<>();
    private final List<EntryCallEdge> testModelEdges = new ArrayList<>();

    private int missingEdgeCount;
    private int additionalEdgeCount;

    /**
     * Default constructor.
     */
    public ComparisonResult() {
        // empty
    }

    public List<NodeDifference> getNodeDifferences() {
        return this.nodeDifferences;
    }

    public int getMissingEdgeCount() {
        return this.missingEdgeCount;
    }

    public void setMissingEdgeCount(final int missingEdgeCount) {
        this.missingEdgeCount = missingEdgeCount;
    }

    public int getAdditionalEdgeCount() {
        return this.additionalEdgeCount;
    }

    public void setAdditionalEdgeCount(final int additionalEdgeCount) {
        this.additionalEdgeCount = additionalEdgeCount;
    }

    public List<EntryCallNode> getMissingNodes() {
        return this.missingNodes;
    }

    public List<EntryCallNode> getAdditionalNodes() {
        return this.additionalNodes;
    }

    public List<EntryCallNode> getSimilarNodes() {
        return this.similarNodes;
    }

    public List<EntryCallNode> getBaselineNodes() {
        return this.baselineNodes;
    }

    public List<EntryCallEdge> getBaselineEdges() {
        return this.baselineEdges;
    }

    public List<EntryCallNode> getTestModelNodes() {
        return this.testModelNodes;
    }

    public List<EntryCallEdge> getTestModelEdges() {
        return this.testModelEdges;
    }

}
