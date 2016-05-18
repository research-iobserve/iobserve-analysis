/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

<<<<<<< HEAD
import kieker.common.record.flow.AbstractEvent;
import kieker.common.util.registry.IRegistry;

/**
 * @author Generic Kieker
 *
=======
import kieker.common.util.registry.IRegistry;
import kieker.common.util.Version;

import kieker.common.record.flow.AbstractEvent;
import org.iobserve.common.record.GeoLocation;

/**
 * @author Generic Kieker
 * 
 * @since 1.10
 */
public class ServerGeoLocation extends AbstractEvent implements GeoLocation {
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // AbstractEvent.timestamp
<<<<<<< HEAD
			+ TYPE_SIZE_SHORT // GeoLocation.countryCode
			+ TYPE_SIZE_STRING // ServerGeoLocation.hostname
			+ TYPE_SIZE_STRING // ServerGeoLocation.address
	;
	private static final long serialVersionUID = 2607620721245961693L;

=======
			 + TYPE_SIZE_SHORT // GeoLocation.countryCode
			 + TYPE_SIZE_STRING // ServerGeoLocation.hostname
			 + TYPE_SIZE_STRING // ServerGeoLocation.address
	;
	private static final long serialVersionUID = 2607620721245961693L;
	
>>>>>>> 8369799fad6bbba0ee0c594bd69ce6afef0b7b41
	public static final Class<?>[] TYPES = {
		long.class, // AbstractEvent.timestamp
		short.class, // GeoLocation.countryCode
		String.class, // ServerGeoLocation.hostname
		String.class, // ServerGeoLocation.address
	};
<<<<<<< HEAD

=======
	
>>>>>>> 8369799fad6bbba0ee0c594bd69ce6afef0b7b41
	/* user-defined constants */
	/* default constants */
	public static final short COUNTRY_CODE = 49;
	public static final String HOSTNAME = "";
	public static final String ADDRESS = "";
	/* property declarations */
	private final short countryCode;
	private final String hostname;
	private final String address;

	/**
	 * Creates a new instance of this class using the given parameters.
<<<<<<< HEAD
	 *
=======
	 * 
>>>>>>> 8369799fad6bbba0ee0c594bd69ce6afef0b7b41
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
<<<<<<< HEAD
		this.hostname = hostname == null ? "" : hostname;
		this.address = address == null ? "" : address;
=======
		this.hostname = hostname == null?"":hostname;
		this.address = address == null?"":address;
>>>>>>> 8369799fad6bbba0ee0c594bd69ce6afef0b7b41
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
<<<<<<< HEAD
	 *
=======
	 * 
>>>>>>> 8369799fad6bbba0ee0c594bd69ce6afef0b7b41
	 * @param values
	 *            The values for the record.
	 */
	public ServerGeoLocation(final Object[] values) { // NOPMD (direct store of values)
		super(values, TYPES);
		this.countryCode = (Short) values[1];
		this.hostname = (String) values[2];
		this.address = (String) values[3];
	}
<<<<<<< HEAD

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 *
=======
	
	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
>>>>>>> 8369799fad6bbba0ee0c594bd69ce6afef0b7b41
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected ServerGeoLocation(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.countryCode = (Short) values[1];
		this.hostname = (String) values[2];
		this.address = (String) values[3];
	}

	/**
	 * This constructor converts the given array into a record.
<<<<<<< HEAD
	 *
	 * @param buffer
	 *            The bytes for the record.
	 *
=======
	 * 
	 * @param buffer
	 *            The bytes for the record.
	 * 
>>>>>>> 8369799fad6bbba0ee0c594bd69ce6afef0b7b41
	 * @throws BufferUnderflowException
	 *             if buffer not sufficient
	 */
	public ServerGeoLocation(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		super(buffer, stringRegistry);
		this.countryCode = buffer.getShort();
		this.hostname = stringRegistry.get(buffer.getInt());
		this.address = stringRegistry.get(buffer.getInt());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
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
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		buffer.putLong(this.getTimestamp());
		buffer.putShort(this.getCountryCode());
		buffer.putInt(stringRegistry.get(this.getHostname()));
		buffer.putInt(stringRegistry.get(this.getAddress()));
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
	public int getSize() {
		return SIZE;
	}
<<<<<<< HEAD

	/**
	 * {@inheritDoc}
	 *
=======
	/**
	 * {@inheritDoc}
	 * 
>>>>>>> 8369799fad6bbba0ee0c594bd69ce6afef0b7b41
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.Factory} mechanism. Hence, this method is not implemented.
	 */
	@Override
	@Deprecated
	public void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
<<<<<<< HEAD
	 *
=======
	 * 
>>>>>>> 8369799fad6bbba0ee0c594bd69ce6afef0b7b41
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.BinaryFactory} mechanism. Hence, this method is not implemented.
	 */
	@Override
	@Deprecated
	public void initFromBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		throw new UnsupportedOperationException();
	}

	public final short getCountryCode() {
		return this.countryCode;
	}
<<<<<<< HEAD

	public final String getHostname() {
		return this.hostname;
	}

	public final String getAddress() {
		return this.address;
	}

=======
	
	public final String getHostname() {
		return this.hostname;
	}
	
	public final String getAddress() {
		return this.address;
	}
	
>>>>>>> 8369799fad6bbba0ee0c594bd69ce6afef0b7b41
}
