/***************************************************************************
 * Copyright 2019 iObserve Project (https://www.iobserve-devops.net)
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
import org.iobserve.common.record.ServletDeployedEvent;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;

import org.iobserve.common.record.Privacy;

/**
 * @author Generic Kieker
 * API compatibility: Kieker 1.15.0
 * 
 * @since 1.15
 */
public class Privacy_ServletDeployedEvent extends ServletDeployedEvent implements Privacy {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // IEventRecord.timestamp
			 + TYPE_SIZE_STRING // ServletDescriptor.service
			 + TYPE_SIZE_STRING // ServletDescriptor.context
			 + TYPE_SIZE_STRING // ServletDescriptor.deploymentId
			 + TYPE_SIZE_INT; // GeoLocation.countryCode
	
	public static final Class<?>[] TYPES = {
		long.class, // IEventRecord.timestamp
		String.class, // ServletDescriptor.service
		String.class, // ServletDescriptor.context
		String.class, // ServletDescriptor.deploymentId
		ISOCountryCode.class, // GeoLocation.countryCode
	};
	
	private static final long serialVersionUID = -4255437900010212383L;
	
	/** property name array. */
	public static final String[] VALUE_NAMES = {
		"timestamp",
		"service",
		"context",
		"deploymentId",
		"countryCode",
	};
	
	/** property declarations. */
	private final ISOCountryCode countryCode;
	
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
	 * @param countryCode
	 *            countryCode
	 */
	public Privacy_ServletDeployedEvent(final long timestamp, final String service, final String context, final String deploymentId, final ISOCountryCode countryCode) {
		super(timestamp, service, context, deploymentId);
		this.countryCode = countryCode;
	}


	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public Privacy_ServletDeployedEvent(final IValueDeserializer deserializer) throws RecordInstantiationException {
		super(deserializer);
		this.countryCode = deserializer.getEnumeration(ISOCountryCode.class);
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
		serializer.putInt(this.getCountryCode().ordinal());
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
		
		final Privacy_ServletDeployedEvent castedRecord = (Privacy_ServletDeployedEvent) obj;
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
		if (this.getCountryCode() != castedRecord.getCountryCode()) {
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
		code += this.getCountryCode().hashCode();
		
		return code;
	}
	
	public final ISOCountryCode getCountryCode() {
		return this.countryCode;
	}
	
}
