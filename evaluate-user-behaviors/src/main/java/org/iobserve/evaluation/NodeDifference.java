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
package org.iobserve.evaluation;

import java.util.List;

import org.iobserve.analysis.cdoruserbehavior.filter.models.CallInformation;
import org.iobserve.analysis.cdoruserbehavior.filter.models.EntryCallNode;

/**
 * @author Reiner Jung
 *
 */
public class NodeDifference {

    private EntryCallNode baselineNode;
    private EntryCallNode testModelNode;
    private List<CallInformation> missingInformation;
    private List<CallInformation> additionalInformation;

    public NodeDifference(EntryCallNode baselineNode, EntryCallNode testModelNode,
            List<CallInformation> missingInformation, List<CallInformation> additionalInformation) {
        this.baselineNode = baselineNode;
        this.testModelNode = testModelNode;
        this.missingInformation = missingInformation;
        this.additionalInformation = additionalInformation;
    }

    public EntryCallNode getBaselineNode() {
        return this.baselineNode;
    }

    public void setBaselineNode(EntryCallNode baselineNode) {
        this.baselineNode = baselineNode;
    }

    public EntryCallNode getTestModelNode() {
        return this.testModelNode;
    }

    public void setTestModelNode(EntryCallNode testModelNode) {
        this.testModelNode = testModelNode;
    }

    public List<CallInformation> getMissingInformation() {
        return this.missingInformation;
    }

    public void setMissingInformation(List<CallInformation> missingInformation) {
        this.missingInformation = missingInformation;
    }

    public List<CallInformation> getAdditionalInformation() {
        return this.additionalInformation;
    }

    public void setAdditionalInformation(List<CallInformation> additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

}
