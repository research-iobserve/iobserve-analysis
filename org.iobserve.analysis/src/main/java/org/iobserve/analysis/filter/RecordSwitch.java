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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.iobserve.common.record.IDeploymentRecord;
import org.iobserve.common.record.IUndeploymentRecord;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.IFlowRecord;

import org.iobserve.analysis.AnalysisMain;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * @author Reiner Jung
 *
 */
public class RecordSwitch extends AbstractConsumerStage<IMonitoringRecord> {
	
	private static int executionCounter = 0;

	/** output port for deployment events. */
	private final OutputPort<IDeploymentRecord> deploymentOutputPort = this.createOutputPort();
	/** output port for undeployment events. */
	private final OutputPort<IUndeploymentRecord> undeploymentOutputPort = this.createOutputPort();
	/** output port for flow events. */
	private final OutputPort<IFlowRecord> flowOutputPort = this.createOutputPort();

	/** internal map to collect unknown record types. */
	private final Map<String, Integer> unknownRecords = new ConcurrentHashMap<String, Integer>();

	/** start time of the filter for monitoring purposes. Please use a monitoring framework for that. */
	private final long startTime;
	/** Statistics. */
	private int recordCount;

	/**
	 * Empty default constructor.
	 */
	public RecordSwitch() {
		// nothing to do here
		this.startTime = System.nanoTime();
	}

	@Override
	protected void execute(final IMonitoringRecord element) {
		// logging execution time and memory
		AnalysisMain.getInstance().getTimeMemLogger().before(this, this.getId() + executionCounter);
		
		this.recordCount++;
		if (element instanceof IDeploymentRecord) {
			this.deploymentOutputPort.send((IDeploymentRecord) element);
		} else if (element instanceof IUndeploymentRecord) {
			this.undeploymentOutputPort.send((IUndeploymentRecord) element);
		} else if (element instanceof IFlowRecord) {
			this.flowOutputPort.send((IFlowRecord) element);
		} else {
			final String className = element.getClass().getCanonicalName();
			Integer hits = this.unknownRecords.get(className);
			if (hits == null) {
				// TODO use a logging facility for that
				System.out.println("What the flip! " + className);
				this.unknownRecords.put(className, Integer.valueOf(1));
			} else {
				hits++;
				this.unknownRecords.put(className, hits);
				// TODO use a logging facility for that
				if ((hits % 100) == 0) {
					System.out.println("Unknown record occurances " + hits + " of " + className);
				}
			}
		}
		
		// logging execution time and memory
		AnalysisMain.getInstance().getTimeMemLogger().after(this, this.getId() + executionCounter);
		
		// count execution
		executionCounter++;
	}

	/**
	 * @return the deploymentOutputPort
	 */
	public final OutputPort<IDeploymentRecord> getDeploymentOutputPort() {
		return this.deploymentOutputPort;
	}

	/**
	 * @return the undeploymentOutputPort
	 */
	public final OutputPort<IUndeploymentRecord> getUndeploymentOutputPort() {
		return this.undeploymentOutputPort;
	}

	/**
	 * @return the flowOutputPort
	 */
	public final OutputPort<IFlowRecord> getFlowOutputPort() {
		return this.flowOutputPort;
	}

	public void outputStatistics() {
		final double delta = ((double) (System.nanoTime() - this.startTime)) / 1000000;
		System.out.println("Record " + this.recordCount + " rate " + ((this.recordCount) / delta));
	}

}
