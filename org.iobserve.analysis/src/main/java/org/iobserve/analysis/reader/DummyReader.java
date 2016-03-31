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
package org.iobserve.analysis.reader;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;

import org.iobserve.common.record.EJBDeployedEvent;
import org.iobserve.common.record.EJBUndeployedEvent;

import teetime.framework.AbstractProducerStage;

/**
 * Produces an event stream for testing purposes.
 *
 * @author Reiner Jung
 *
 */
public class DummyReader extends AbstractProducerStage<IMonitoringRecord> {

	private static final long THREAD_ID = 0;
	private static final String SESSION_ID = "<session id>";
	private static final String HOSTNAME = "demo";
	private static final String CLASS_SIGNATURE = "org.spp.cocome.SampleClass";
	private static final String OPERATION_SIGNATURE = "void ping(String value)";

	private int count = 0;

	/**
	 * Empty default constructor.
	 */
	public DummyReader() {
		// nothing to do here
	}

	@Override
	protected void execute() {
		if (this.count < 1000) {
			final EJBDeployedEvent deploymentEvent = new EJBDeployedEvent(0, "node-context", "http://localhost:4848/CoCoME");

			this.outputPort.send(deploymentEvent);
			for (int i = 0; i < 100; i++) {
				final long timestamp = i + (this.count * 100);
				final TraceMetadata traceMetadataRecord = new TraceMetadata(i, THREAD_ID, SESSION_ID, HOSTNAME, 0, 0);
				final BeforeOperationEvent beforeRecord = new BeforeOperationEvent(timestamp, i, 0, OPERATION_SIGNATURE, CLASS_SIGNATURE);
				final AfterOperationEvent afterRecord = new AfterOperationEvent(timestamp, i, 0, OPERATION_SIGNATURE, CLASS_SIGNATURE);

				this.outputPort.send(traceMetadataRecord);
				this.outputPort.send(beforeRecord);
				this.outputPort.send(afterRecord);
			}

			final EJBUndeployedEvent undeploymentEvent = new EJBUndeployedEvent(200, "node-context", "http://localhost:4848/CoCoME");
			this.outputPort.send(undeploymentEvent);
			this.count++;
		} else {
			this.terminate();
		}
		System.out.println("read " + this.count);
	}

}
