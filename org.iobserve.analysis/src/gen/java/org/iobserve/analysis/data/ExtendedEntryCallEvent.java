package org.iobserve.analysis.data;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import org.iobserve.analysis.data.EntryCallEvent;
import kieker.common.util.registry.IRegistry;


/**
 * @author Christoph Dornieden
 * 
 * @since 1.0
 */
public class ExtendedEntryCallEvent extends EntryCallEvent  {
	private static final long serialVersionUID = -5800400653208862798L;

		/** Descriptive definition of the serialization size of the record. */
		public static final int SIZE = TYPE_SIZE_LONG // EntryCallEvent.entryTime
				 + TYPE_SIZE_LONG // EntryCallEvent.exitTime
				 + TYPE_SIZE_STRING // EntryCallEvent.operationSignature
				 + TYPE_SIZE_STRING // EntryCallEvent.classSignature
				 + TYPE_SIZE_STRING // EntryCallEvent.sessionId
				 + TYPE_SIZE_STRING // EntryCallEvent.hostname
				 + TYPE_SIZE_STRING // ExtendedEntryCallEvent.informations
		;
	
		public static final Class<?>[] TYPES = {
			long.class, // EntryCallEvent.entryTime
			long.class, // EntryCallEvent.exitTime
			String.class, // EntryCallEvent.operationSignature
			String.class, // EntryCallEvent.classSignature
			String.class, // EntryCallEvent.sessionId
			String.class, // EntryCallEvent.hostname
			String.class, // ExtendedEntryCallEvent.informations
		};
	
	/** user-defined constants */

	/** default constants */
	public static final String INFORMATIONS = "";

	/** property declarations */
	private final String informations;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param entryTime
	 *            entryTime
	 * @param exitTime
	 *            exitTime
	 * @param operationSignature
	 *            operationSignature
	 * @param classSignature
	 *            classSignature
	 * @param sessionId
	 *            sessionId
	 * @param hostname
	 *            hostname
	 * @param informations
	 *            informations
	 */
	public ExtendedEntryCallEvent(final long entryTime, final long exitTime, final String operationSignature, final String classSignature, final String sessionId, final String hostname, final String informations) {
		super(entryTime, exitTime, operationSignature, classSignature, sessionId, hostname);
		this.informations = informations == null?"":informations;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public ExtendedEntryCallEvent(final Object[] values) { // NOPMD (direct store of values)
		super(values, TYPES);
		this.informations = (String) values[6];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected ExtendedEntryCallEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.informations = (String) values[6];
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
	public ExtendedEntryCallEvent(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		super(buffer, stringRegistry);
		this.informations = stringRegistry.get(buffer.getInt());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return new Object[] {
			this.getEntryTime(),
			this.getExitTime(),
			this.getOperationSignature(),
			this.getClassSignature(),
			this.getSessionId(),
			this.getHostname(),
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
		stringRegistry.get(this.getSessionId());
		stringRegistry.get(this.getHostname());
		stringRegistry.get(this.getInformations());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		buffer.putLong(this.getEntryTime());
		buffer.putLong(this.getExitTime());
		buffer.putInt(stringRegistry.get(this.getOperationSignature()));
		buffer.putInt(stringRegistry.get(this.getClassSignature()));
		buffer.putInt(stringRegistry.get(this.getSessionId()));
		buffer.putInt(stringRegistry.get(this.getHostname()));
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
		
		final ExtendedEntryCallEvent castedRecord = (ExtendedEntryCallEvent) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) return false;
		if (this.getEntryTime() != castedRecord.getEntryTime()) return false;
		if (this.getExitTime() != castedRecord.getExitTime()) return false;
		if (!this.getOperationSignature().equals(castedRecord.getOperationSignature())) return false;
		if (!this.getClassSignature().equals(castedRecord.getClassSignature())) return false;
		if (!this.getSessionId().equals(castedRecord.getSessionId())) return false;
		if (!this.getHostname().equals(castedRecord.getHostname())) return false;
		if (!this.getInformations().equals(castedRecord.getInformations())) return false;
		return true;
	}
	
	public final String getInformations() {
		return this.informations;
	}	
}
