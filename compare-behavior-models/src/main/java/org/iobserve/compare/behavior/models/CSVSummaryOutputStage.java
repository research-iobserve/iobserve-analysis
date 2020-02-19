/***************************************************************************
 * Copyright (C) 2019 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.compare.behavior.models;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import teetime.framework.AbstractConsumerStage;

/**
 *
 * @author Lars Jürgensen
 *
 */
public class CSVSummaryOutputStage extends AbstractConsumerStage<BehaviorModelDifference> {

    private final File outputFile;

    /**
     * Configure and setup a file writer for the output of the CSV result.
     *
     * @param outputFile
     *            file descriptor for the output file
     */
    public CSVSummaryOutputStage(final File outputFile) {
        this.outputFile = outputFile;
    }

    @Override
    protected void execute(final BehaviorModelDifference result) throws Exception {

        String fileName = this.outputFile.getName();

        // remove extension of filename. This does not change the file in which it is written, but
        // the name inside the table
        final int pointIndex = fileName.indexOf(".");
        if (pointIndex != -1) {
            fileName = fileName.substring(0, pointIndex);
        }

        // the attributes for the csv table in the correct order
        final int referenceNodesAmount = result.getReferenceNodes().size();
        final int testModelNodesAmount = result.getTestModelNodes().size();

        final int referenceEdgesAmount = result.getReferenceEdges().size();
        final int testModelEdgesAmount = result.getTestModelEdges().size();

        final int referenceEventGroupAmount = result.getReferenceEventGroup().size();
        final int testModelEventGroupAmount = result.getTestModelEventGroup().size();

        final int referenceEventsAmount = result.getReferenceEvents().size();
        final int testModelEventsAmount = result.getTestModelEvents().size();

        final double nodeJaccardIndex = ((double) result.getNodeIntersectionAmount()) / result.getNodeUnionAmount();

        final double edgeJaccardIndex = ((double) result.getEdgeIntersectionAmount()) / result.getEdgeUnionAmount();

        final double graphEditDistance = result.getGraphEditDistance();

        // write the file
        try (BufferedWriter br = Files.newBufferedWriter(this.outputFile.toPath(), StandardCharsets.UTF_8)) {
            br.write(String.format("%s;%d;%d;%d;%d;%d;%d;%d;%d;%f;%f;%f\n", fileName, referenceNodesAmount,
                    testModelNodesAmount, referenceEdgesAmount, testModelEdgesAmount, referenceEventGroupAmount,
                    testModelEventGroupAmount, referenceEventsAmount, testModelEventsAmount, nodeJaccardIndex,
                    edgeJaccardIndex, graphEditDistance));
        }
    }

}
