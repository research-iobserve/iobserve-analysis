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

import org.iobserve.analysis.cdoruserbehavior.filter.models.CallInformation;
import org.iobserve.analysis.cdoruserbehavior.filter.models.EntryCallNode;

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

        writer.write("= " + this.outputFile.getName() + " =");
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

}
