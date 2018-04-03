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
package org.iobserve.evaluation.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iobserve.analysis.clustering.behaviormodels.BehaviorModel;
import org.iobserve.analysis.clustering.behaviormodels.CallInformation;
import org.iobserve.analysis.clustering.behaviormodels.EntryCallEdge;
import org.iobserve.analysis.clustering.behaviormodels.EntryCallNode;
import org.iobserve.evaluation.data.ComparisonResult;
import org.iobserve.evaluation.data.NodeDifference;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

/**
 * Compare two behavior models and compute their differences.
 *
 * @author Reiner Jung
 *
 * @since 0.0.2
 */
public class ModelComparisonStage extends AbstractStage {

    private final InputPort<BehaviorModel> referenceModelInputPort = this.createInputPort();
    private final InputPort<BehaviorModel> testModelInputPort = this.createInputPort();
    private final OutputPort<ComparisonResult> resultPort = this.createOutputPort();

    private BehaviorModel referenceModel;

    private BehaviorModel testModel;

    /**
     * Model comparison stage.
     */
    public ModelComparisonStage() {
        // empty
    }

    /*
     * (non-Javadoc)
     *
     * @see teetime.framework.AbstractStage#execute()
     */
    @Override
    protected void execute() throws Exception {
        /**
         * We cannot user else if here, as (a) there could be an input at each input port, (b) there
         * could be a model at testModel but not at the referenceModel input, in an if-then-else
         * style, the test model would not be received until we have a reference model, which
         * unnecessarily would imply a sequence between both ports.
         */
        if (this.referenceModel == null) {
            this.referenceModel = this.referenceModelInputPort.receive();
        }
        if (this.testModel == null) {
            this.testModel = this.testModelInputPort.receive();
        }
        /** We still have to check both, as there could be nothing a both ports. */
        if ((this.referenceModel != null) && (this.testModel != null)) {
            final ComparisonResult result = new ComparisonResult();

            result.getBaselineNodes().addAll(Arrays.asList(this.referenceModel.getNodes()));
            result.getBaselineEdges().addAll(Arrays.asList(this.referenceModel.getEdges()));
            result.getTestModelNodes().addAll(Arrays.asList(this.testModel.getNodes()));
            result.getTestModelEdges().addAll(Arrays.asList(this.testModel.getEdges()));

            /** M2: Similarity Ratio */
            /**
             * Number of differences and similarities of two behavior graphs and the
             * call-information on each node.
             */
            /** Missing nodes. */
            for (final EntryCallNode baselineNode : this.referenceModel.getNodes()) {
                final EntryCallNode testModelNode = this.findMatchingModelNode(this.testModel.getNodes(), baselineNode);
                if (testModelNode == null) {
                    result.getMissingNodes().add(baselineNode);
                } else {
                    result.getSimilarNodes().add(baselineNode);
                    /** Compute mismatch in call information. */
                    final List<CallInformation> missingInformation = this.computeAdditionalInformation(
                            baselineNode.getEntryCallInformation(), testModelNode.getEntryCallInformation());
                    final List<CallInformation> additionalInformation = this.computeAdditionalInformation(
                            testModelNode.getEntryCallInformation(), baselineNode.getEntryCallInformation());
                    result.getNodeDifferences().add(
                            new NodeDifference(baselineNode, testModelNode, missingInformation, additionalInformation));
                }
            }

            /** Additional nodes. */
            for (final EntryCallNode testModelNode : this.testModel.getNodes()) {
                final EntryCallNode baselineNode = this.findMatchingModelNode(this.referenceModel.getNodes(),
                        testModelNode);
                if (baselineNode == null) {
                    result.getAdditionalNodes().add(testModelNode);
                }
            }

            /** Missing edges. */
            int missingEdgeCount = 0;
            for (final EntryCallEdge baselineEdge : this.referenceModel.getEdges()) {
                final EntryCallEdge testModelEdge = this.findMatchingModelEdge(this.testModel.getEdges(), baselineEdge);
                if (testModelEdge == null) {
                    missingEdgeCount += (int) baselineEdge.getCalls();
                } else {
                    missingEdgeCount += Math.abs((int) (baselineEdge.getCalls() - testModelEdge.getCalls()));
                }
            }
            result.setMissingEdgeCount(missingEdgeCount);

            /** Additional edges. */
            int additionalEdgeCount = 0;
            for (final EntryCallEdge testModelEdge : this.testModel.getEdges()) {
                final EntryCallEdge baselineEdge = this.findMatchingModelEdge(this.referenceModel.getEdges(),
                        testModelEdge);
                if (baselineEdge == null) {
                    additionalEdgeCount += (int) testModelEdge.getCalls();
                } else {
                    additionalEdgeCount += Math.abs((int) (baselineEdge.getCalls() - testModelEdge.getCalls()));
                }
            }
            result.setAdditionalEdgeCount(additionalEdgeCount);

            /** Forget models after processing to be able to process the next elements. */
            this.referenceModel = null;
            this.testModel = null;

            /** Add baseline and testModelNodes */

            this.resultPort.send(result);
        }
    }

    /**
     * Find a matching edge to the source edge in a set of edges.
     *
     * @param entryCallEdges
     *            set of edges
     * @param sourceEdge
     *            source edge
     * @return returns the matching edge or null when no match was found
     */
    private EntryCallEdge findMatchingModelEdge(final EntryCallEdge[] entryCallEdges, final EntryCallEdge sourceEdge) {
        for (final EntryCallEdge entryCallEdge : entryCallEdges) {
            if (sourceEdge.getSource().getSignature().equals(entryCallEdge.getSource().getSignature())
                    && sourceEdge.getTarget().getSignature().equals(entryCallEdge.getTarget().getSignature())) {
                return entryCallEdge;
            }
        }
        return null;
    }

    /**
     * Find call information which exists in firstModelSet and not in lastModelSet.
     *
     * @param firstCallInformationSet
     * @param testModelCallInformationSet
     * @return list of missing call information
     */
    private List<CallInformation> computeAdditionalInformation(final CallInformation[] firstCallInformationSet,
            final CallInformation[] lastCallInformationSet) {
        final List<CallInformation> result = new ArrayList<>();
        for (final CallInformation firstCallInformation : firstCallInformationSet) {
            boolean found = false;
            for (final CallInformation lastCallInformation : lastCallInformationSet) {
                if (lastCallInformation.getInformationSignature()
                        .equals(firstCallInformation.getInformationSignature())) {
                    found = true;
                }
            }
            if (!found) {
                result.add(firstCallInformation);
            }
        }
        return result;
    }

    /**
     * Find the matching entry call node from the test model for a given baseline node.
     *
     * @param entryCallNodes
     *            all test model nodes
     * @param baselineNode
     *            the baseline node
     * @return the matching test model node, or null on fail
     */
    private EntryCallNode findMatchingModelNode(final EntryCallNode[] entryCallNodes,
            final EntryCallNode baselineNode) {
        for (final EntryCallNode testModelNode : entryCallNodes) {
            if (testModelNode.getSignature().equals(baselineNode.getSignature())) {
                return testModelNode;
            }
        }
        return null;
    }

    public InputPort<BehaviorModel> getReferenceModelInputPort() {
        return this.referenceModelInputPort;
    }

    public InputPort<BehaviorModel> getTestModelInputPort() {
        return this.testModelInputPort;
    }

    public OutputPort<ComparisonResult> getOutputPort() {
        return this.resultPort;
    }

}
