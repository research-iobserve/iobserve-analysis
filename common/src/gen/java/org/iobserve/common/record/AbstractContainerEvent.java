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
 * API compatibility: Kieker 1.15.0
 * 
 * @since 0.0.2
 */
public abstract class AbstractContainerEvent extends AbstractMonitoringRecord implements IEvent {			
	
	/** default constants. */
	public static final String SERVICE = "";
	private static final long serialVersionUID = 743898805447896033L;
	
		
	/** property declarations. */
	private final long timestamp;
	private final String service;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param service
	 *            service
	 */
	public AbstractContainerEvent(final long timestamp, final String service) {
		this.timestamp = timestamp;
		this.service = service == null?"":service;
	}



	
	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public AbstractContainerEvent(final IValueDeserializer deserializer) throws RecordInstantiationException {
		this.timestamp = deserializer.getLong();
		this.service = deserializer.getString();
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
		
		final AbstractContainerEvent castedRecord = (AbstractContainerEvent) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (this.getTimestamp() != castedRecord.getTimestamp()) {
			return false;
		}
		if (!this.getService().equals(castedRecord.getService())) {
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
		
		return code;
	}
	
	public final long getTimestamp() {
		return this.timestamp;
	}
	
	
	public final String getService() {
		return this.service;
	}
	
}
