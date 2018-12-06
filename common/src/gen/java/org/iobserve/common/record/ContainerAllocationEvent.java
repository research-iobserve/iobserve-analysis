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

import java.nio.BufferOverflowException;

import kieker.common.exception.RecordInstantiationException;
import org.iobserve.common.record.AbstractContainerEvent;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;

import org.iobserve.common.record.IAllocationEvent;

/**
 * @author Reiner Jung
 * API compatibility: Kieker 1.15.0
 * 
 * @since 0.0.2
 */
public class ContainerAllocationEvent extends AbstractContainerEvent implements IAllocationEvent {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // IEvent.timestamp
			 + TYPE_SIZE_STRING; // AbstractContainerEvent.service
	
	public static final Class<?>[] TYPES = {
		long.class, // IEvent.timestamp
		String.class, // AbstractContainerEvent.service
	};
	
	private static final long serialVersionUID = 6174419585510598093L;
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"timestamp",
		"service",
	};
	
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param service
	 *            service
	 */
	public ContainerAllocationEvent(final long timestamp, final String service) {
		super(timestamp, service);
	}



	
	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public ContainerAllocationEvent(final IValueDeserializer deserializer) throws RecordInstantiationException {
		super(deserializer);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		//super.serialize(serializer);
		serializer.putLong(this.getTimestamp());
		serializer.putString(this.getService());
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
		return PROPERTY_NAMES; // NOPMD
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
		
		final ContainerAllocationEvent castedRecord = (ContainerAllocationEvent) obj;
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
	
}
