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
 * Metric for the lmethod of the 
 * ClusterSelection stage based on
 * the cluster diameter.
 */
public class DiameterEvalMetric implements ILMethodEvalStrategy {

	@Override
	public double calculateClusterMetric(final List<ClusteringFeature> cluster) {
		double sum = 0.0;
		double cnt = 0.0;
		
		for (ClusteringFeature cf : cluster) {
			sum += (cf.getNumber() * (cf.getNumber() - 1)) * Math.pow(cf.getDiameter(), 2);
			cnt += (cf.getNumber() * (cf.getNumber() - 1));
		}
			
		return cnt != 0.0 ? sum / cnt : 0;
	}
}
