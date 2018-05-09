/***************************************************************************
 * Copyright 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.common.record;


import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;

import org.iobserve.common.record.IEvent;

/**
 * @author Reiner Jung
 * API compatibility: Kieker 1.14.0
 * 
 * @since 0.0.2
 */
public abstract class ServletDescriptor extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory, IEvent {			
	
	/** default constants. */
	public static final String SERVICE = "";
	public static final String CONTEXT = "";
	public static final String DEPLOYMENT_ID = "";
	private static final long serialVersionUID = -4046452426687004817L;
	
		
	/** property declarations. */
	private final long timestamp;
	private final String service;
	private final String context;
	private final String deploymentId;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param service
	 *            service
	 * @param context
	 *            context
	 * @param deploymentId
	 *            deploymentId
	 */
	public ServletDescriptor(final long timestamp, final String service, final String context, final String deploymentId) {
		this.timestamp = timestamp;
		this.service = service == null?"":service;
		this.context = context == null?"":context;
		this.deploymentId = deploymentId == null?"":deploymentId;
	}


	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 *
	 * @deprecated to be removed 1.15
	 */
	@Deprecated
	protected ServletDescriptor(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
		this.timestamp = (Long) values[0];
		this.service = (String) values[1];
		this.context = (String) values[2];
		this.deploymentId = (String) values[3];
	}

	
	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public ServletDescriptor(final IValueDeserializer deserializer) throws RecordInstantiationException {
		this.timestamp = deserializer.getLong();
		this.service = deserializer.getString();
		this.context = deserializer.getString();
		this.deploymentId = deserializer.getString();
	}
	

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated to be rmeoved in 1.15
	 */
	@Override
	@Deprecated
	public void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != this.getClass()) {
			return false;
		}
		
		final ServletDescriptor castedRecord = (ServletDescriptor) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (this.getTimestamp() != castedRecord.getTimestamp()) {
			return false;
		}
		if (!this.getService().equals(castedRecord.getService())) {
			return false;
		}
		if (!this.getContext().equals(castedRecord.getContext())) {
			return false;
		}
		if (!this.getDeploymentId().equals(castedRecord.getDeploymentId())) {
			return false;
		}
		
		return true;
	}
	
	public final long getTimestamp() {
		return this.timestamp;
	}
	
	
	public final String getService() {
		return this.service;
	}
	
	
	public final String getContext() {
		return this.context;
	}
	
	
	public final String getDeploymentId() {
		return this.deploymentId;
	}
	
}
