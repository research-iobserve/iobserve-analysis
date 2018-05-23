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
package org.iobserve.analysis.behavior.clustering.birch.model;

import java.util.List;
import java.util.Optional;

/**
 * General node in CFTree.
 * @author melf
 *
 */
abstract class AbstractNode {
	protected static int dimension;
	protected static int nodeSizeConstraint;
	protected static int leafSizeConstraint;
	protected static double mergeThreshold;
		
	abstract ClusteringFeature getCF();
	
	abstract Optional<AbstractNode> insert(ClusteringFeature cf);
	
	abstract List<AbstractNode> getChildren();
	
	abstract int space();
	
	abstract int size();

    abstract void resplit(AbstractNode child);

	abstract boolean refinementMerge(AbstractNode child);

	abstract Optional<AbstractNode> getNextLevel();
	
	abstract Optional<AbstractNode> getChild(int i);
	
	abstract int getClosestChildIndex(ClusteringFeature cf);
	
	abstract void updateSum();	
	
}
