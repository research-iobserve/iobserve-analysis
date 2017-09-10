/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.iobserve.analysis.clustering.filter.models.CallInformation;
import org.iobserve.analysis.clustering.filter.models.EntryCallEdge;
import org.iobserve.analysis.clustering.filter.models.EntryCallNode;

import teetime.framework.AbstractConsumerStage;

/**
 * Sync all incoming records with a Kieker writer to a text file log.
 *
 * @author Reiner Jung
 *
 */
public class ComparisonOutputStage extends AbstractConsumerStage<ComparisonResult> {

    private final File outputFile;

    /**
     * Configure and setup the Kieker writer.
     *
     * @param outputFile
     */
    public ComparisonOutputStage(File outputFile) {
        this.outputFile = outputFile;
    }

    @Override
    protected void execute(final ComparisonResult result) throws IOException {
        final FileWriter fw = new FileWriter(this.outputFile);
        final BufferedWriter writer = new BufferedWriter(fw);

        final int baselineNodeCount = result.getBaselineNodes().size();
        final int testModelNodeCount = result.getTestModelNodes().size();
        final int missingNodeCount = result.getMissingNodes().size();
        final int additionalNodeCount = result.getAdditionalNodes().size();

        final int baselineEdgeCount = result.getBaselineEdges().size();
        final int testModelEdgeCount = result.getTestModelEdges().size();
        final int missingEdgeCount = result.getMissingEdgeCount();
        final int additionalEdgeCount = result.getAdditionalEdgeCount();

        writer.write("CP;" + this.outputFile.getName() + ";" + baselineNodeCount + ";" + baselineEdgeCount + ";"
                + testModelNodeCount + ";" + testModelEdgeCount + ";" + missingNodeCount + ";" + additionalNodeCount
                + ";" + missingEdgeCount + ";" + additionalEdgeCount + ";"
                + (double) missingNodeCount / (double) baselineNodeCount + ";"
                + (double) additionalNodeCount / (double) baselineNodeCount + ";"
                + (double) missingEdgeCount / (double) baselineEdgeCount + ";"
                + (double) additionalEdgeCount / (double) baselineEdgeCount + "\n");

        final List<EntryCallNode> allNodes = this.createAllNodesList(result.getBaselineNodes(),
                result.getTestModelNodes());
        final List<EntryCallEdge> allEdges = this.createAllEdgesList(result.getBaselineEdges(),
                result.getTestModelEdges());

        this.generateNodeCallInformation(writer, "baseline;" + this.outputFile.getName() + ";", allNodes,
                result.getBaselineNodes());
        this.generateNodeCallInformation(writer, "compared;" + this.outputFile.getName() + ";", allNodes,
                result.getTestModelNodes());
        writer.write("baseline;------------------------------------------ edges\n");
        writer.write("compared;------------------------------------------ edges\n");
        this.generateEdges(writer, "baseline;" + this.outputFile.getName() + ";", allEdges, result.getBaselineEdges());
        this.generateEdges(writer, "compared;" + this.outputFile.getName() + ";", allEdges, result.getTestModelEdges());
        writer.write("Nodes:\n\tmissing=" + result.getMissingNodes().size() + "\n\tadditional="
                + result.getAdditionalNodes().size() + "\n");
        for (final EntryCallNode node : result.getMissingNodes()) {
            writer.write("\t - " + node.getSignature() + "\n");
        }
        for (final EntryCallNode node : result.getAdditionalNodes()) {
            writer.write("\t + " + node.getSignature() + "\n");
        }
        for (final EntryCallNode node : result.getSimilarNodes()) {
            writer.write("\t = " + node.getSignature() + "\n");
        }
        writer.write("Edges:\n\tmissing=" + result.getMissingEdgeCount() + "\n\tadditional="
                + result.getAdditionalEdgeCount() + "\n");
        writer.write("Node differences:\n");
        for (final NodeDifference difference : result.getNodeDifferences()) {
            writer.write("\tNode" + difference.getBaselineNode().getSignature() + "\n");
            writer.write("\t\tMissing");
            String separator = "=";
            for (final CallInformation callInformation : difference.getMissingInformation()) {
                writer.write(separator + callInformation);
                separator = ", ";
            }
            writer.write("\n");
            writer.write("\t\tAdditional");
            separator = "=";
            for (final CallInformation callInformation : difference.getMissingInformation()) {
                writer.write(separator + callInformation);
                separator = ", ";
            }
            writer.write("\n");
        }

        writer.close();
        fw.close();
    }

    private List<EntryCallEdge> createAllEdgesList(List<EntryCallEdge> baselineEdges,
            List<EntryCallEdge> testModelEdges) {
        final List<EntryCallEdge> result = new ArrayList<>();
        for (final EntryCallEdge edge : baselineEdges) {
            final EntryCallEdge duplicateEdge = new EntryCallEdge();
            duplicateEdge.setCalls(edge.getCalls());
            duplicateEdge.setSource(edge.getSource());
            duplicateEdge.setTarget(edge.getTarget());
            result.add(duplicateEdge);
        }

        for (final EntryCallEdge edge : testModelEdges) {
            if (!this.edgeExists(baselineEdges, edge)) {
                result.add(edge);
            } else {
                final EntryCallEdge duplicateEdge = this.findEdge(result, edge);
                duplicateEdge.addCalls(edge.getCalls());
            }
        }
        return result;
    }

    private boolean edgeExists(List<EntryCallEdge> edges, EntryCallEdge findEdge) {
        if (this.findEdge(edges, findEdge) != null) {
            return true;
        } else {
            return false;
        }
    }

    private EntryCallEdge findEdge(List<EntryCallEdge> edges, EntryCallEdge findEdge) {
        for (final EntryCallEdge edge : edges) {
            if (findEdge.getSource().getSignature().equals(edge.getSource().getSignature())
                    && findEdge.getTarget().getSignature().equals(edge.getTarget().getSignature())) {
                return edge;
            }
        }

        return null;
    }

    /**
     * Get all nodes used in this comparison.
     *
     * @param baselineNodes
     * @param testModelNodes
     * @return
     */
    private List<EntryCallNode> createAllNodesList(List<EntryCallNode> baselineNodes,
            List<EntryCallNode> testModelNodes) {
        final List<EntryCallNode> result = new ArrayList<>();
        result.addAll(baselineNodes);
        for (final EntryCallNode node : testModelNodes) {
            if (!this.nodeExists(baselineNodes, node)) {
                result.add(node);
            }
        }
        return result;
    }

    private boolean nodeExists(List<EntryCallNode> nodes, EntryCallNode findNode) {
        if (this.findNode(nodes, findNode) != null) {
            return true;
        } else {
            return false;
        }
    }

    private EntryCallNode findNode(List<EntryCallNode> nodes, EntryCallNode findNode) {
        for (final EntryCallNode node : nodes) {
            if (findNode.getSignature().equals(node.getSignature())) {
                return node;
            }
        }

        return null;
    }

    private void generateNodeCallInformation(BufferedWriter writer, String prefix, List<EntryCallNode> allNodes,
            List<EntryCallNode> selectedNodes) throws IOException {
        for (final EntryCallNode referenceNode : allNodes) {
            final EntryCallNode printNode = this.findNode(selectedNodes, referenceNode);
            if (printNode != null) {
                final Set<CallInformation> allEntryCallInformation = this.generateAllEntryCallInformationList(
                        referenceNode.getEntryCallInformation(), printNode.getEntryCallInformation());
                writer.write(prefix + printNode.getSignature().substring(18) + "\n");
                this.generateCallInformation(writer, prefix + "\t", allEntryCallInformation,
                        printNode.getEntryCallInformation());
            } else {
                writer.write(prefix + " -- \n");
            }
        }
    }

    private Set<CallInformation> generateAllEntryCallInformationList(Set<CallInformation> entryCallInformation,
            Set<CallInformation> supplementalCallInfo) {
        final Set<CallInformation> allEntryCallInformation = new HashSet<>();
        allEntryCallInformation.addAll(entryCallInformation);
        for (final CallInformation information : supplementalCallInfo) {
            boolean exists = false;
            for (final CallInformation referenceInfo : allEntryCallInformation) {
                if (referenceInfo.getInformationSignature().equals(information.getInformationSignature())
                        && referenceInfo.getInformationCode() == information.getInformationCode()) {
                    exists = true;
                }
            }
            if (!exists) {
                allEntryCallInformation.add(information);
            }
        }

        return allEntryCallInformation;
    }

    private void generateCallInformation(BufferedWriter writer, String prefix,
            Set<CallInformation> allEntryCallInformation, Set<CallInformation> entryCallInformation)
            throws IOException {

        for (final CallInformation information : allEntryCallInformation) {
            if (this.isContainedIn(entryCallInformation, information)) {
                writer.write(
                        prefix + information.getInformationSignature() + "=" + information.getInformationCode() + "\n");
            } else {
                writer.write(prefix + information.getInformationSignature() + "---\n");
            }
        }
    }

    private boolean isContainedIn(Set<CallInformation> entryCallInformation, CallInformation information) {
        for (final CallInformation referenceInfo : entryCallInformation) {
            if (referenceInfo.getInformationSignature().equals(information.getInformationSignature())
                    && referenceInfo.getInformationCode() == information.getInformationCode()) {
                return true;
            }
        }
        return false;
    }

    private void generateEdges(BufferedWriter writer, String prefix, List<EntryCallEdge> allEdges,
            List<EntryCallEdge> selectedEdges) throws IOException {
        for (final EntryCallEdge referenceEdge : allEdges) {
            final EntryCallEdge printEdge = this.findEdge(selectedEdges, referenceEdge);
            if (printEdge != null) {
                writer.write(prefix + printEdge.getSource().getSignature().substring(18) + " -- " + printEdge.getCalls()
                        + ":" + referenceEdge.getCalls() + " -> " + printEdge.getTarget().getSignature().substring(18)
                        + "\n");
            } else {
                writer.write(prefix + referenceEdge.getSource().getSignature().substring(18) + " -- " + "X" + ":"
                        + referenceEdge.getCalls() + " -> " + referenceEdge.getTarget().getSignature().substring(18)
                        + "\n");
            }
        }
    }

}
