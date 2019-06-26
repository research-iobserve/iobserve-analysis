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
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;

import kieker.common.record.flow.IEventRecord;
import org.iobserve.common.record.GeoLocation;

/**
 * @author Reiner Jung
 * API compatibility: Kieker 1.15.0
 * 
 * @since 0.0.2
 */
public class ServerGeoLocation extends AbstractMonitoringRecord implements IEventRecord, GeoLocation {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // IEventRecord.timestamp
			 + TYPE_SIZE_INT // GeoLocation.countryCode
			 + TYPE_SIZE_STRING // ServerGeoLocation.hostname
			 + TYPE_SIZE_STRING; // ServerGeoLocation.address
	
	public static final Class<?>[] TYPES = {
		long.class, // IEventRecord.timestamp
		ISOCountryCode.class, // GeoLocation.countryCode
		String.class, // ServerGeoLocation.hostname
		String.class, // ServerGeoLocation.address
	};
	
	/** default constants. */
	public static final String HOSTNAME = "";
	public static final String ADDRESS = "";
	private static final long serialVersionUID = -2727780590184532240L;
	
	/** property name array. */
	public static final String[] VALUE_NAMES = {
		"timestamp",
		"countryCode",
		"hostname",
		"address",
	};
	
	/** property declarations. */
	private long timestamp;
	private final ISOCountryCode countryCode;
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
	public ServerGeoLocation(final long timestamp, final ISOCountryCode countryCode, final String hostname, final String address) {
		this.timestamp = timestamp;
		this.countryCode = countryCode;
		this.hostname = hostname == null?"":hostname;
		this.address = address == null?"":address;
	}


	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public ServerGeoLocation(final IValueDeserializer deserializer) throws RecordInstantiationException {
		this.timestamp = deserializer.getLong();
		this.countryCode = deserializer.getEnumeration(ISOCountryCode.class);
		this.hostname = deserializer.getString();
		this.address = deserializer.getString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		serializer.putLong(this.getTimestamp());
		serializer.putInt(this.getCountryCode().ordinal());
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
		
		final ServerGeoLocation castedRecord = (ServerGeoLocation) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (this.getTimestamp() != castedRecord.getTimestamp()) {
			return false;
		}
		if (this.getCountryCode() != castedRecord.getCountryCode()) {
			return false;
		}
		if (!this.getHostname().equals(castedRecord.getHostname())) {
			return false;
		}
		if (!this.getAddress().equals(castedRecord.getAddress())) {
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
		code += this.getCountryCode().hashCode();
		code += this.getHostname().hashCode();
		code += this.getAddress().hashCode();
		
		return code;
	}
	
	public final long getTimestamp() {
		return this.timestamp;
	}
	
	public final void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public final ISOCountryCode getCountryCode() {
		return this.countryCode;
	}
	
	
	public final String getHostname() {
		return this.hostname;
	}
	
	
	public final String getAddress() {
		return this.address;
	}
	
}
