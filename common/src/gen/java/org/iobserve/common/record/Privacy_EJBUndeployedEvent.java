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
import org.iobserve.common.record.EJBUndeployedEvent;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;

import org.iobserve.common.record.Privacy;

/**
 * @author Generic Kieker
 * API compatibility: Kieker 1.14.0
 * 
 * @since 1.14
 */
public class Privacy_EJBUndeployedEvent extends EJBUndeployedEvent implements Privacy {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // IEvent.timestamp
			 + TYPE_SIZE_STRING // EJBDescriptor.service
			 + TYPE_SIZE_STRING // EJBDescriptor.context
			 + TYPE_SIZE_STRING // EJBDescriptor.deploymentId
			 + TYPE_SIZE_SHORT; // GeoLocation.countryCode
	
	public static final Class<?>[] TYPES = {
		long.class, // IEvent.timestamp
		String.class, // EJBDescriptor.service
		String.class, // EJBDescriptor.context
		String.class, // EJBDescriptor.deploymentId
		short.class, // GeoLocation.countryCode
	};
	
	private static final long serialVersionUID = -6804603248511972839L;
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"timestamp",
		"service",
		"context",
		"deploymentId",
		"countryCode",
	};
	
	/** property declarations. */
	private final short countryCode;
	
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
	public Privacy_EJBUndeployedEvent(final long timestamp, final String service, final String context, final String deploymentId, final short countryCode) {
		super(timestamp, service, context, deploymentId);
		this.countryCode = countryCode;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 *
	 * @deprecated since 1.13. Use {@link #Privacy_EJBUndeployedEvent(IValueDeserializer)} instead.
	 */
	@Deprecated
	public Privacy_EJBUndeployedEvent(final Object[] values) { // NOPMD (direct store of values)
		super(values, TYPES);
		this.countryCode = (Short) values[4];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 *
	 * @deprecated since 1.13. Use {@link #Privacy_EJBUndeployedEvent(IValueDeserializer)} instead.
	 */
	@Deprecated
	protected Privacy_EJBUndeployedEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.countryCode = (Short) values[4];
	}

	
	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public Privacy_EJBUndeployedEvent(final IValueDeserializer deserializer) throws RecordInstantiationException {
		super(deserializer);
		this.countryCode = deserializer.getShort();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @deprecated since 1.13. Use {@link #serialize(IValueSerializer)} with an array serializer instead.
	 */
	@Override
	@Deprecated
	public Object[] toArray() {
		return new Object[] {
			this.getTimestamp(),
			this.getService(),
			this.getContext(),
			this.getDeploymentId(),
			this.getCountryCode(),
		};
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		//super.serialize(serializer);
		serializer.putLong(this.getTimestamp());
		serializer.putString(this.getService());
		serializer.putString(this.getContext());
		serializer.putString(this.getDeploymentId());
		serializer.putShort(this.getCountryCode());
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
	 * 
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.Factory} mechanism. Hence, this method is not implemented.
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
		
		final Privacy_EJBUndeployedEvent castedRecord = (Privacy_EJBUndeployedEvent) obj;
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
	
	public final short getCountryCode() {
		return this.countryCode;
	}
	
}
