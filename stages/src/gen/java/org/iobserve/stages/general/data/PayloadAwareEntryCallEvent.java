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
package org.iobserve.stages.general.data;

import java.nio.BufferOverflowException;

import kieker.common.exception.RecordInstantiationException;
import org.iobserve.stages.general.data.EntryCallEvent;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;

import org.iobserve.common.record.IPayloadCharacterization;

/**
 * @author Reiner Jung
 * API compatibility: Kieker 1.15.0
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
	public static final String[] VALUE_NAMES = {
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
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public PayloadAwareEntryCallEvent(final IValueDeserializer deserializer) throws RecordInstantiationException {
		super(deserializer);
		// load array sizes
		final int _parameters_size0 = deserializer.getInt();
		this.parameters = new String[_parameters_size0];
		for (int i0=0;i0<_parameters_size0;i0++)
			this.parameters[i0] = deserializer.getString();
		
		// load array sizes
		final int _values_size0 = deserializer.getInt();
		this.values = new String[_values_size0];
		for (int i0=0;i0<_values_size0;i0++)
			this.values[i0] = deserializer.getString();
		
		this.requestType = deserializer.getInt();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
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
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int code = 0;
		code += ((int)this.getEntryTime());
		code += ((int)this.getExitTime());
		code += this.getOperationSignature().hashCode();
		code += this.getClassSignature().hashCode();
		code += this.getSessionId().hashCode();
		code += this.getHostname().hashCode();
		// get array length
		for (int i0=0;i0 < this.parameters.length;i0++) {
			for (int i1=0;i1 < this.parameters.length;i1++) {
				code += this.getParameters()[i0].hashCode();
			}
		}
		
		// get array length
		for (int i0=0;i0 < this.values.length;i0++) {
			for (int i1=0;i1 < this.values.length;i1++) {
				code += this.getValues()[i0].hashCode();
			}
		}
		
		code += ((int)this.getRequestType());
		
		return code;
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
