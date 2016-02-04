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

package org.iobserve.analysis.data;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.IRegistry;


/**
 * @author Reiner Jung
 * 
 * @since 1.0
 */
public class EntryCallEvent extends AbstractMonitoringRecord implements 
	IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory {
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // EntryCallEvent.entryTime
			 + TYPE_SIZE_LONG // EntryCallEvent.exitTime
			 + TYPE_SIZE_STRING // EntryCallEvent.operationSignature
			 + TYPE_SIZE_STRING // EntryCallEvent.classSignature
			 + TYPE_SIZE_STRING // EntryCallEvent.sessionId
			 + TYPE_SIZE_STRING // EntryCallEvent.hostname
	;
	private static final long serialVersionUID = -4397283558299092423L;
	
	public static final Class<?>[] TYPES = {
		long.class, // EntryCallEvent.entryTime
		long.class, // EntryCallEvent.exitTime
		String.class, // EntryCallEvent.operationSignature
		String.class, // EntryCallEvent.classSignature
		String.class, // EntryCallEvent.sessionId
		String.class, // EntryCallEvent.hostname
	};
	
	/* user-defined constants */
	/* default constants */
	public static final String OPERATION_SIGNATURE = "";
	public static final String CLASS_SIGNATURE = "";
	public static final String SESSION_ID = "";
	public static final String HOSTNAME = "";
	/* property declarations */
	private final long entryTime;
	private final long exitTime;
	private final String operationSignature;
	private final String classSignature;
	private final String sessionId;
	private final String hostname;

	/* added by Alessandro Giusa at 19.12.2015
	 * Reason: when entryTime and exitTime accidently is the same, there is no way of
	 * sorting the entry call events, therefore this index is used.
	 * */
	private int index;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * @param index index of this entry call event
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
	 */
	public EntryCallEvent(final int index, final long entryTime,
			final long exitTime, final String operationSignature,
			final String classSignature, final String sessionId, final String hostname) {
		this.index = index;
		this.entryTime = entryTime;
		this.exitTime = exitTime;
		this.operationSignature = operationSignature == null?"":operationSignature;
		this.classSignature = classSignature == null?"":classSignature;
		this.sessionId = sessionId == null?"":sessionId;
		this.hostname = hostname == null?"":hostname;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public EntryCallEvent(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.entryTime = (Long) values[0];
		this.exitTime = (Long) values[1];
		this.operationSignature = (String) values[2];
		this.classSignature = (String) values[3];
		this.sessionId = (String) values[4];
		this.hostname = (String) values[5];
	}
	
	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected EntryCallEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
		this.entryTime = (Long) values[0];
		this.exitTime = (Long) values[1];
		this.operationSignature = (String) values[2];
		this.classSignature = (String) values[3];
		this.sessionId = (String) values[4];
		this.hostname = (String) values[5];
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
	public EntryCallEvent(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		this.entryTime = buffer.getLong();
		this.exitTime = buffer.getLong();
		this.operationSignature = stringRegistry.get(buffer.getInt());
		this.classSignature = stringRegistry.get(buffer.getInt());
		this.sessionId = stringRegistry.get(buffer.getInt());
		this.hostname = stringRegistry.get(buffer.getInt());
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
			this.getHostname()
		};
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
	
	public int getIndex() {
		return this.index;
	}

	public final long getEntryTime() {
		return this.entryTime;
	}
	
	public final long getExitTime() {
		return this.exitTime;
	}
	
	public final String getOperationSignature() {
		return this.operationSignature;
	}
	
	public final String getClassSignature() {
		return this.classSignature;
	}
	
	public final String getSessionId() {
		return this.sessionId;
	}
	
	public final String getHostname() {
		return this.hostname;
	}
	
}
