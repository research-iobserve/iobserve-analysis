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

import java.util.List;

import org.iobserve.analysis.clustering.behaviormodels.CallInformation;
import org.iobserve.analysis.clustering.behaviormodels.EntryCallNode;

/**
 * @author Reiner Jung
 *
 */
public class NodeDifference {

    private EntryCallNode referenceNode;
    private EntryCallNode testModelNode;
    private List<CallInformation> missingInformation;
    private List<CallInformation> additionalInformation;

    /**
     * Difference of one node in the graph.
     *
     * @param referenceNode
     *            reference node
     * @param testModelNode
     *            node of the compared model
     * @param missingInformation
     *            missing information
     * @param additionalInformation
     *            additional information
     */
    public NodeDifference(final EntryCallNode referenceNode, final EntryCallNode testModelNode,
            final List<CallInformation> missingInformation, final List<CallInformation> additionalInformation) {
        this.referenceNode = referenceNode;
        this.testModelNode = testModelNode;
        this.missingInformation = missingInformation;
        this.additionalInformation = additionalInformation;
    }

    public EntryCallNode getReferenceNode() {
        return this.referenceNode;
    }

    public void setReferenceNode(final EntryCallNode referenceNode) {
        this.referenceNode = referenceNode;
    }

    public EntryCallNode getTestModelNode() {
        return this.testModelNode;
    }

    public void setTestModelNode(final EntryCallNode testModelNode) {
        this.testModelNode = testModelNode;
    }

    public List<CallInformation> getMissingInformation() {
        return this.missingInformation;
    }

    public void setMissingInformation(final List<CallInformation> missingInformation) {
        this.missingInformation = missingInformation;
    }

    public List<CallInformation> getAdditionalInformation() {
        return this.additionalInformation;
    }

    public void setAdditionalInformation(final List<CallInformation> additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

}
