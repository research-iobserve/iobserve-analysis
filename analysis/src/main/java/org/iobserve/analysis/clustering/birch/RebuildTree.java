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
package org.iobserve.analysis.clustering.birch;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.clustering.birch.model.CFTree;

/**
 * @author Melf Lorenzen
 * This stage implements phase 2 
 * of the birch algorithm. Incoming
 * tree will be rebuilt with higher 
 * merge thresholds until they 
 * reach the target size.
 */
public class RebuildTree extends AbstractConsumerStage<CFTree> {
	private int maxLeafEntries;
    private final OutputPort<CFTree> outputPort = this.createOutputPort();
    
	/** Constructor for the CFTree rebuilding phase.
	 * @param maxLeafEntries maximum number of leaf entries allowed.
	 */
	public RebuildTree(final int maxLeafEntries) {
		super();
		this.maxLeafEntries = maxLeafEntries;
	}
    
	@Override
	protected void execute(final CFTree tree) throws Exception {

		if (tree.getNumberOfLeafEntries() > maxLeafEntries) {
			final double newThreshold = Math.max(tree.getAvgMinimalLeafDistance(), tree.getMergeThreshold() * 1.10);
            final CFTree newTree = tree.rebuild(newThreshold);
			this.execute(newTree);
		} else {
			this.outputPort.send(tree);
		}
	}

	public OutputPort<CFTree> getOutputPort() {
		return outputPort;
	}

}
