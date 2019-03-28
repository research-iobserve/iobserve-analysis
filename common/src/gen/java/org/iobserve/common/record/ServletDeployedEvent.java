/***************************************************************************
 * Copyright 2019 Kieker Project (http://kieker-monitoring.net)
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

import java.nio.BufferOverflowException;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;

import org.iobserve.common.record.IDeployedEvent;
import org.iobserve.common.record.ServletDescriptor;

/**
 * @author Reiner Jung
 * API compatibility: Kieker 1.15.0
 * 
 * @since 0.0.2
 */
public class ServletDeployedEvent extends AbstractMonitoringRecord implements IDeployedEvent, ServletDescriptor {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // IEventRecord.timestamp
			 + TYPE_SIZE_STRING // ServletDescriptor.service
			 + TYPE_SIZE_STRING // ServletDescriptor.context
			 + TYPE_SIZE_STRING; // ServletDescriptor.deploymentId
	
	public static final Class<?>[] TYPES = {
		long.class, // IEventRecord.timestamp
		String.class, // ServletDescriptor.service
		String.class, // ServletDescriptor.context
		String.class, // ServletDescriptor.deploymentId
	};
	
	/** property name array. */
	public static final String[] VALUE_NAMES = {
		"timestamp",
		"service",
		"context",
		"deploymentId",
	};
	
	/** default constants. */
	public static final String SERVICE = "";
	public static final String CONTEXT = "";
	public static final String DEPLOYMENT_ID = "";
	private static final long serialVersionUID = 5978540046869193725L;
	
	/** property declarations. */
	private long timestamp;
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
	public ServletDeployedEvent(final long timestamp, final String service, final String context, final String deploymentId) {
		this.timestamp = timestamp;
		this.service = service == null?"":service;
		this.context = context == null?"":context;
		this.deploymentId = deploymentId == null?"":deploymentId;
	}


	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public ServletDeployedEvent(final IValueDeserializer deserializer) throws RecordInstantiationException {
		this.timestamp = deserializer.getLong();
		this.service = deserializer.getString();
		this.context = deserializer.getString();
		this.deploymentId = deserializer.getString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		serializer.putLong(this.getTimestamp());
		serializer.putString(this.getService());
		serializer.putString(this.getContext());
		serializer.putString(this.getDeploymentId());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?>[] getValueTypes() {
		return TYPES; // NOPMD
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getValueNames() {
		return VALUE_NAMES; // NOPMD
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSize() {
		return SIZE;
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
		
		final ServletDeployedEvent castedRecord = (ServletDeployedEvent) obj;
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
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int code = 0;
		code += ((int)this.getTimestamp());
		code += this.getService().hashCode();
		code += this.getContext().hashCode();
		code += this.getDeploymentId().hashCode();
		
		return code;
	}
	
	public final long getTimestamp() {
		return this.timestamp;
	}
	
	public final void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
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
