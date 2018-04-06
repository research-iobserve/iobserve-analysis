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

import java.util.List;


import org.iobserve.analysis.clustering.birch.model.ClusteringFeature;
/**
 * @author Melf Lorenzen
 * Interface for different 
 * metrics for the lmethod of the 
 * ClusterSelection stage.
 */
public interface ILMethodEvalStrategy {

	/** Calculates the value of the clustering.
	 * @param cluster clustering to be valued
	 * @return the value 
	 */
	public double calculateClusterMetric(final List<ClusteringFeature> cluster);
	
}
