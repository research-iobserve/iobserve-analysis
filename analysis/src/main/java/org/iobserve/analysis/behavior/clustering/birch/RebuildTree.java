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
package org.iobserve.analysis.behavior.clustering.birch;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.behavior.clustering.birch.model.CFTree;

/**
 * This stage implements phase 2 of the birch algorithm. Incoming tree will be rebuilt with higher
 * merge thresholds until they reach the target size.
 * 
 * @author Melf Lorenzen
 */
public class RebuildTree extends AbstractConsumerStage<CFTree> {

    private final int maxLeafEntries;
    private final OutputPort<CFTree> outputPort = this.createOutputPort();

    /**
     * Constructor for the CFTree rebuilding phase.
     *
     * @param maxLeafEntries
     *            maximum number of leaf entries allowed.
     */
    public RebuildTree(final int maxLeafEntries) {
        super();
        this.maxLeafEntries = maxLeafEntries;
    }

    @Override
    protected void execute(final CFTree tree) throws Exception {
        int noChange = 0;
        int last = tree.getNumberOfLeafEntries();
        /**
         * if the number of leaf entries hasnt shrunk for a 100 consecutive rebuildings, end this
         * phase anyway
         */
        CFTree newTree = tree;
        while (newTree.getNumberOfLeafEntries() > this.maxLeafEntries && noChange < 100) {
            newTree = newTree.rebuild(newTree.getAvgMinimalLeafDistance());
            noChange = last == newTree.getNumberOfLeafEntries() ? noChange + 1 : 0;
            last = newTree.getNumberOfLeafEntries();
        }
        this.outputPort.send(newTree);
    }

    public OutputPort<CFTree> getOutputPort() {
        return this.outputPort;
    }

}
