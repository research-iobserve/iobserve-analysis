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

/**
 * This strategy compares clustering features by
 * average inter-cluster distance D2.
 * @author Melf Lorenzen
 *
 */
public class CFCompareD2Strategy implements ICFComparisonStrategy {
	
	@Override
	public double getDistance(final ClusteringFeature cf1, final ClusteringFeature cf2) {
		return cf1.compareD2(cf2);
	}

}