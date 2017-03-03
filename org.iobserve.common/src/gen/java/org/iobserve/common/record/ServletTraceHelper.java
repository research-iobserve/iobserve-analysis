package org.iobserve.common.record;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.IRegistry;

import org.iobserve.common.record.ITraceHelper;
import kieker.common.record.flow.IFlowRecord;

/**
 * @author Reiner Jung
 * 
 * @since 1.0
 */
public class ServletTraceHelper extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory, ITraceHelper, IFlowRecord {
	private static final long serialVersionUID = 2363353535794190244L;

	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // ITraceHelper.traceId
			 + TYPE_SIZE_STRING // ITraceHelper.host
			 + TYPE_SIZE_INT // ITraceHelper.port
			 + TYPE_SIZE_STRING // ServletTraceHelper.requestURI
	;
	
	public static final Class<?>[] TYPES = {
		long.class, // ITraceHelper.traceId
		String.class, // ITraceHelper.host
		int.class, // ITraceHelper.port
		String.class, // ServletTraceHelper.requestURI
	};
	
	/** user-defined constants */
	
	/** default constants */
	public static final String HOST = "";
	public static final String REQUEST_URI = "";
	
	/** property declarations */
	private final long traceId;
	private final String host;
	private final int port;
	private final String requestURI;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param traceId
	 *            traceId
	 * @param host
	 *            host
	 * @param port
	 *            port
	 * @param requestURI
	 *            requestURI
	 */
	public ServletTraceHelper(final long traceId, final String host, final int port, final String requestURI) {
		this.traceId = traceId;
		this.host = host == null?"":host;
		this.port = port;
		this.requestURI = requestURI == null?"":requestURI;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public ServletTraceHelper(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.traceId = (Long) values[0];
		this.host = (String) values[1];
		this.port = (Integer) values[2];
		this.requestURI = (String) values[3];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected ServletTraceHelper(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
		this.traceId = (Long) values[0];
		this.host = (String) values[1];
		this.port = (Integer) values[2];
		this.requestURI = (String) values[3];
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
	public ServletTraceHelper(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		this.traceId = buffer.getLong();
		this.host = stringRegistry.get(buffer.getInt());
		this.port = buffer.getInt();
		this.requestURI = stringRegistry.get(buffer.getInt());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return new Object[] {
			this.getTraceId(),
			this.getHost(),
			this.getPort(),
			this.getRequestURI()
		};
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
		stringRegistry.get(this.getHost());
		stringRegistry.get(this.getRequestURI());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		buffer.putLong(this.getTraceId());
		buffer.putInt(stringRegistry.get(this.getHost()));
		buffer.putInt(this.getPort());
		buffer.putInt(stringRegistry.get(this.getRequestURI()));
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
		
		final ServletTraceHelper castedRecord = (ServletTraceHelper) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) return false;
		if (this.getTraceId() != castedRecord.getTraceId()) return false;
		if (!this.getHost().equals(castedRecord.getHost())) return false;
		if (this.getPort() != castedRecord.getPort()) return false;
		if (!this.getRequestURI().equals(castedRecord.getRequestURI())) return false;
		return true;
	}
	
	public final long getTraceId() {
		return this.traceId;
	}	
	
	public final String getHost() {
		return this.host;
	}	
	
	public final int getPort() {
		return this.port;
	}	
	
	public final String getRequestURI() {
		return this.requestURI;
	}	
}
