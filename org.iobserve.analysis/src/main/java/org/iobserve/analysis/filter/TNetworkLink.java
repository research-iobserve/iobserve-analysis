/***************************************************************************
 * Copyright 2014 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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
package org.iobserve.analysis.filter;

import org.iobserve.common.record.IDeploymentRecord;
import kieker.common.record.flow.trace.TraceMetadata;

import org.iobserve.analysis.AnalysisMain;
import org.iobserve.analysis.correspondence.ICorrespondence;

import teetime.framework.AbstractConsumerStage;

/**
 * This transformation does the linking of R
 *
 */
public class TNetworkLink extends AbstractConsumerStage<TraceMetadata> {

	private static long executionCounter = 0;
	
	private final ICorrespondence correspondence;

	/**
	 * Most likely the constructor needs an additional field for the PCM access. But this has to be discussed with Robert.
	 *
	 * @param correspondence
	 */
	public TNetworkLink() {
		this.correspondence = null;
	}

	/**
	 * This method is triggered for every undeployment event
	 */
	@Override
	protected void execute(final TraceMetadata event) {
		AnalysisMain.getInstance().getTimeMemLogger()
			.before(this, this.getId() + TNetworkLink.executionCounter); //TODO testing logger
		// add your transformation here
		System.out.println("TNetworkLink.execute()");
		
		AnalysisMain.getInstance().getTimeMemLogger()
			.after(this, this.getId() + TNetworkLink.executionCounter); //TODO testing logger
		TNetworkLink.executionCounter++;
	}

}
