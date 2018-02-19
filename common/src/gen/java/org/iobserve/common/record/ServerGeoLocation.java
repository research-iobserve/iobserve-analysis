/***************************************************************************
 * Copyright 2018 iObserve Project (http://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE2.0
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
import kieker.common.record.flow.AbstractEvent;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;
import kieker.common.util.registry.IRegistry;

import org.iobserve.common.record.GeoLocation;

/**
 * @author Reiner Jung
 * API compatibility: Kieker 1.13.0
 * 
 * @since 0.0.2
 */
public class ServerGeoLocation extends AbstractEvent implements GeoLocation {
	private static final long serialVersionUID = -9109740651531232541L;

	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // IEventRecord.timestamp
			 + TYPE_SIZE_SHORT // GeoLocation.countryCode
			 + TYPE_SIZE_STRING // ServerGeoLocation.hostname
			 + TYPE_SIZE_STRING // ServerGeoLocation.address
	;
	
	public static final Class<?>[] TYPES = {
		long.class, // IEventRecord.timestamp
		short.class, // GeoLocation.countryCode
		String.class, // ServerGeoLocation.hostname
		String.class, // ServerGeoLocation.address
	};
	
	
	/** default constants. */
	public static final short COUNTRY_CODE = 49;
	public static final String HOSTNAME = "";
	public static final String ADDRESS = "";
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"timestamp",
		"countryCode",
		"hostname",
		"address",
	};
	
	/** property declarations. */
	private final short countryCode;
	private final String hostname;
	private final String address;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param countryCode
	 *            countryCode
	 * @param hostname
	 *            hostname
	 * @param address
	 *            address
	 */
	public ServerGeoLocation(final long timestamp, final short countryCode, final String hostname, final String address) {
		super(timestamp);
		this.countryCode = countryCode;
		this.hostname = hostname == null?"":hostname;
		this.address = address == null?"":address;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 *
	 * @deprecated since 1.13. Use {@link #ServerGeoLocation(IValueDeserializer)} instead.
	 */
	@Deprecated
	public ServerGeoLocation(final Object[] values) { // NOPMD (direct store of values)
		super(values, TYPES);
		this.countryCode = (Short) values[1];
		this.hostname = (String) values[2];
		this.address = (String) values[3];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 *
	 * @deprecated since 1.13. Use {@link #ServerGeoLocation(IValueDeserializer)} instead.
	 */
	@Deprecated
	protected ServerGeoLocation(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.countryCode = (Short) values[1];
		this.hostname = (String) values[2];
		this.address = (String) values[3];
	}

	
	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 */
	public ServerGeoLocation(final IValueDeserializer deserializer) throws RecordInstantiationException {
		super(deserializer);
		this.countryCode = deserializer.getShort();
		this.hostname = deserializer.getString();
		this.address = deserializer.getString();
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
			this.getCountryCode(),
			this.getHostname(),
			this.getAddress()
		};
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
		stringRegistry.get(this.getHostname());
		stringRegistry.get(this.getAddress());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		//super.serialize(serializer);
		serializer.putLong(this.getTimestamp());
		serializer.putShort(this.getCountryCode());
		serializer.putString(this.getHostname());
		serializer.putString(this.getAddress());
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
		if (obj == null) return false;
		if (obj == this) return true;
		if (obj.getClass() != this.getClass()) return false;
		
		final ServerGeoLocation castedRecord = (ServerGeoLocation) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) return false;
		if (this.getTimestamp() != castedRecord.getTimestamp()) return false;
		if (this.getCountryCode() != castedRecord.getCountryCode()) return false;
		if (!this.getHostname().equals(castedRecord.getHostname())) return false;
		if (!this.getAddress().equals(castedRecord.getAddress())) return false;
		return true;
	}
	
	public final short getCountryCode() {
		return this.countryCode;
	}
	
	
	public final String getHostname() {
		return this.hostname;
	}
	
	
	public final String getAddress() {
		return this.address;
	}
	
}
