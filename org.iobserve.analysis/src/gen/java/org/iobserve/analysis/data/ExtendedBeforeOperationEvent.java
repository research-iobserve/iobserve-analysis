package org.iobserve.analysis.data;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.common.util.registry.IRegistry;


/**
 * @author Christoph Dornieden
 * 
 * @since 1.0
 */
public class ExtendedBeforeOperationEvent extends BeforeOperationEvent  {
	private static final long serialVersionUID = -7201271040819166422L;

		/** Descriptive definition of the serialization size of the record. */
		public static final int SIZE = TYPE_SIZE_LONG // AbstractEvent.timestamp
				 + TYPE_SIZE_LONG // AbstractTraceEvent.traceId
				 + TYPE_SIZE_INT // AbstractTraceEvent.orderIndex
				 + TYPE_SIZE_STRING // AbstractOperationEvent.operationSignature
				 + TYPE_SIZE_STRING // AbstractOperationEvent.classSignature
				 + TYPE_SIZE_STRING // ExtendedBeforeOperationEvent.informations
		;
	
		public static final Class<?>[] TYPES = {
			long.class, // AbstractEvent.timestamp
			long.class, // AbstractTraceEvent.traceId
			int.class, // AbstractTraceEvent.orderIndex
			String.class, // AbstractOperationEvent.operationSignature
			String.class, // AbstractOperationEvent.classSignature
			String.class, // ExtendedBeforeOperationEvent.informations
		};
	
	/** user-defined constants */

	/** default constants */
	public static final String INFORMATIONS = "";

	/** property declarations */
	private final String informations;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param traceId
	 *            traceId
	 * @param orderIndex
	 *            orderIndex
	 * @param operationSignature
	 *            operationSignature
	 * @param classSignature
	 *            classSignature
	 * @param informations
	 *            informations
	 */
	public ExtendedBeforeOperationEvent(final long timestamp, final long traceId, final int orderIndex, final String operationSignature, final String classSignature, final String informations) {
		super(timestamp, traceId, orderIndex, operationSignature, classSignature);
		this.informations = informations == null?"":informations;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public ExtendedBeforeOperationEvent(final Object[] values) { // NOPMD (direct store of values)
		super(values, TYPES);
		this.informations = (String) values[5];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected ExtendedBeforeOperationEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.informations = (String) values[5];
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
	public ExtendedBeforeOperationEvent(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		super(buffer, stringRegistry);
		this.informations = stringRegistry.get(buffer.getInt());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return new Object[] {
			this.getTimestamp(),
			this.getTraceId(),
			this.getOrderIndex(),
			this.getOperationSignature(),
			this.getClassSignature(),
			this.getInformations()
		};
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
		stringRegistry.get(this.getOperationSignature());
		stringRegistry.get(this.getClassSignature());
		stringRegistry.get(this.getInformations());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		buffer.putLong(this.getTimestamp());
		buffer.putLong(this.getTraceId());
		buffer.putInt(this.getOrderIndex());
		buffer.putInt(stringRegistry.get(this.getOperationSignature()));
		buffer.putInt(stringRegistry.get(this.getClassSignature()));
		buffer.putInt(stringRegistry.get(this.getInformations()));
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
		
		final ExtendedBeforeOperationEvent castedRecord = (ExtendedBeforeOperationEvent) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) return false;
		if (this.getTimestamp() != castedRecord.getTimestamp()) return false;
		if (this.getTraceId() != castedRecord.getTraceId()) return false;
		if (this.getOrderIndex() != castedRecord.getOrderIndex()) return false;
		if (!this.getOperationSignature().equals(castedRecord.getOperationSignature())) return false;
		if (!this.getClassSignature().equals(castedRecord.getClassSignature())) return false;
		if (!this.getInformations().equals(castedRecord.getInformations())) return false;
		return true;
	}
	
	public final String getInformations() {
		return this.informations;
	}	
}
