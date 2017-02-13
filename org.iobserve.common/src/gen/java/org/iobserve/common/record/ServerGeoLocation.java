package org.iobserve.common.record;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.flow.AbstractEvent;
import kieker.common.util.registry.IRegistry;

import org.iobserve.common.record.GeoLocation;

/**
 * @author iObserve
 * 
 * @since 1.0
 */
public class ServerGeoLocation extends AbstractEvent implements GeoLocation {
	private static final long serialVersionUID = -9109740651531232541L;

		/** Descriptive definition of the serialization size of the record. */
		public static final int SIZE = TYPE_SIZE_LONG // AbstractEvent.timestamp
				 + TYPE_SIZE_SHORT // GeoLocation.countryCode
				 + TYPE_SIZE_STRING // ServerGeoLocation.hostname
				 + TYPE_SIZE_STRING // ServerGeoLocation.address
		;
	
		public static final Class<?>[] TYPES = {
			long.class, // AbstractEvent.timestamp
			short.class, // GeoLocation.countryCode
			String.class, // ServerGeoLocation.hostname
			String.class, // ServerGeoLocation.address
		};
	
	/** user-defined constants */

	/** default constants */
	public static final short COUNTRY_CODE = 49;
	public static final String HOSTNAME = "";
	public static final String ADDRESS = "";

	/** property declarations */
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
	 */
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
	 */
	protected ServerGeoLocation(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.countryCode = (Short) values[1];
		this.hostname = (String) values[2];
		this.address = (String) values[3];
	}

	/**
	 * This constructor converts the given array into a record.
	 * 
	 * @param buffer
	 *            The bytes for the record.
	 * 
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
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
		stringRegistry.get(this.getHostname());
		stringRegistry.get(this.getAddress());
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
	 * 
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.BinaryFactory} mechanism. Hence, this method is not implemented.
	 */
	@Override
	@Deprecated
	public void initFromBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
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
