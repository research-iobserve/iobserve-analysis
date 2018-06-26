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
package org.iobserve.stages.general.data;

import java.nio.BufferOverflowException;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;

import org.iobserve.common.record.IPayloadCharacterization;

/**
 * @author Reiner Jung
 * API compatibility: Kieker 1.14.0
 * 
 * @since 0.0.2
 */
public class PayloadAwareEntryCallEvent extends EntryCallEvent implements IPayloadCharacterization {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // EntryCallEvent.entryTime
			 + TYPE_SIZE_LONG // EntryCallEvent.exitTime
			 + TYPE_SIZE_STRING // EntryCallEvent.operationSignature
			 + TYPE_SIZE_STRING // EntryCallEvent.classSignature
			 + TYPE_SIZE_STRING // EntryCallEvent.sessionId
			 + TYPE_SIZE_STRING // EntryCallEvent.hostname
			 + TYPE_SIZE_STRING // IPayloadCharacterization.parameters
			 + TYPE_SIZE_STRING // IPayloadCharacterization.values
			 + TYPE_SIZE_INT; // IPayloadCharacterization.requestType
	
	public static final Class<?>[] TYPES = {
		long.class, // EntryCallEvent.entryTime
		long.class, // EntryCallEvent.exitTime
		String.class, // EntryCallEvent.operationSignature
		String.class, // EntryCallEvent.classSignature
		String.class, // EntryCallEvent.sessionId
		String.class, // EntryCallEvent.hostname
		String[].class, // IPayloadCharacterization.parameters
		String[].class, // IPayloadCharacterization.values
		int.class, // IPayloadCharacterization.requestType
	};
	
	private static final long serialVersionUID = 5364265571284290753L;
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"entryTime",
		"exitTime",
		"operationSignature",
		"classSignature",
		"sessionId",
		"hostname",
		"parameters",
		"values",
		"requestType",
	};
	
	/** property declarations. */
	private final String[] parameters;
	private final String[] values;
	private final int requestType;
	
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
	 * @param parameters
	 *            parameters
	 * @param values
	 *            values
	 * @param requestType
	 *            requestType
	 */
	public PayloadAwareEntryCallEvent(final long entryTime, final long exitTime, final String operationSignature, final String classSignature, final String sessionId, final String hostname, final String[] parameters, final String[] values, final int requestType) {
		super(entryTime, exitTime, operationSignature, classSignature, sessionId, hostname);
		this.parameters = parameters;
		this.values = values;
		this.requestType = requestType;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 *
	 * @deprecated since 1.13. Use {@link #PayloadAwareEntryCallEvent(IValueDeserializer)} instead.
	 */
	@Deprecated
	public PayloadAwareEntryCallEvent(final Object[] values) { // NOPMD (direct store of values)
		super(values, TYPES);
		this.parameters = (String[]) values[6];
		this.values = (String[]) values[7];
		this.requestType = (Integer) values[8];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 *
	 * @deprecated since 1.13. Use {@link #PayloadAwareEntryCallEvent(IValueDeserializer)} instead.
	 */
	@Deprecated
	protected PayloadAwareEntryCallEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.parameters = (String[]) values[6];
		this.values = (String[]) values[7];
		this.requestType = (Integer) values[8];
	}

	
	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public PayloadAwareEntryCallEvent(final IValueDeserializer deserializer) throws RecordInstantiationException {
		super(deserializer);
		// load array sizes
		int _parameters_size0 = deserializer.getInt();
		this.parameters = new String[_parameters_size0];
		for (int i0=0;i0<_parameters_size0;i0++)
			this.parameters[i0] = deserializer.getString();
		
		// load array sizes
		int _values_size0 = deserializer.getInt();
		this.values = new String[_values_size0];
		for (int i0=0;i0<_values_size0;i0++)
			this.values[i0] = deserializer.getString();
		
		this.requestType = deserializer.getInt();
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
			this.getEntryTime(),
			this.getExitTime(),
			this.getOperationSignature(),
			this.getClassSignature(),
			this.getSessionId(),
			this.getHostname(),
			this.getParameters(),
			this.getValues(),
			this.getRequestType(),
		};
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		//super.serialize(serializer);
		serializer.putLong(this.getEntryTime());
		serializer.putLong(this.getExitTime());
		serializer.putString(this.getOperationSignature());
		serializer.putString(this.getClassSignature());
		serializer.putString(this.getSessionId());
		serializer.putString(this.getHostname());
		// store array sizes
		int _parameters_size0 = this.getParameters().length;
		serializer.putInt(_parameters_size0);
		for (int i0=0;i0<_parameters_size0;i0++)
			serializer.putString(this.getParameters()[i0]);
		
		// store array sizes
		int _values_size0 = this.getValues().length;
		serializer.putInt(_values_size0);
		for (int i0=0;i0<_values_size0;i0++)
			serializer.putString(this.getValues()[i0]);
		
		serializer.putInt(this.getRequestType());
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
		
		final PayloadAwareEntryCallEvent castedRecord = (PayloadAwareEntryCallEvent) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (this.getEntryTime() != castedRecord.getEntryTime()) {
			return false;
		}
		if (this.getExitTime() != castedRecord.getExitTime()) {
			return false;
		}
		if (!this.getOperationSignature().equals(castedRecord.getOperationSignature())) {
			return false;
		}
		if (!this.getClassSignature().equals(castedRecord.getClassSignature())) {
			return false;
		}
		if (!this.getSessionId().equals(castedRecord.getSessionId())) {
			return false;
		}
		if (!this.getHostname().equals(castedRecord.getHostname())) {
			return false;
		}
		// get array length
		int _parameters_size0 = this.getParameters().length;
		if (_parameters_size0 != castedRecord.getParameters().length) {
			return false;
		}
		for (int i0=0;i0<_parameters_size0;i0++)
			if (!this.getParameters()[i0].equals(castedRecord.getParameters()[i0])) {
				return false;
			}
		
		// get array length
		int _values_size0 = this.getValues().length;
		if (_values_size0 != castedRecord.getValues().length) {
			return false;
		}
		for (int i0=0;i0<_values_size0;i0++)
			if (!this.getValues()[i0].equals(castedRecord.getValues()[i0])) {
				return false;
			}
		
		if (this.getRequestType() != castedRecord.getRequestType()) {
			return false;
		}
		
		return true;
	}
	
	public final String[] getParameters() {
		return this.parameters;
	}
	
	
	public final String[] getValues() {
		return this.values;
	}
	
	
	public final int getRequestType() {
		return this.requestType;
	}
	
}
