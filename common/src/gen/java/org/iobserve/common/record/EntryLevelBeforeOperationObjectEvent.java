/***************************************************************************
 * Copyright 2017 iObserve Project
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

import kieker.common.record.flow.trace.operation.object.BeforeOperationObjectEvent;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;
import kieker.common.util.registry.IRegistry;

import org.iobserve.common.record.IPayloadCharacterization;

/**
 * @author Reiner Jung
 * API compatibility: Kieker 1.13.0
 * 
 * @since 0.0.2
 */
public class EntryLevelBeforeOperationObjectEvent extends BeforeOperationObjectEvent implements IPayloadCharacterization {
	private static final long serialVersionUID = -4017422430129179625L;

	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // IEventRecord.timestamp
			 + TYPE_SIZE_LONG // ITraceRecord.traceId
			 + TYPE_SIZE_INT // ITraceRecord.orderIndex
			 + TYPE_SIZE_STRING // IOperationSignature.operationSignature
			 + TYPE_SIZE_STRING // IClassSignature.classSignature
			 + TYPE_SIZE_INT // IObjectRecord.objectId
			 + TYPE_SIZE_STRING // IPayloadCharacterization.parameters
			 + TYPE_SIZE_STRING // IPayloadCharacterization.values
			 + TYPE_SIZE_INT // IPayloadCharacterization.requestType
	;
	
	public static final Class<?>[] TYPES = {
		long.class, // IEventRecord.timestamp
		long.class, // ITraceRecord.traceId
		int.class, // ITraceRecord.orderIndex
		String.class, // IOperationSignature.operationSignature
		String.class, // IClassSignature.classSignature
		int.class, // IObjectRecord.objectId
		String[].class, // IPayloadCharacterization.parameters
		String[].class, // IPayloadCharacterization.values
		int.class, // IPayloadCharacterization.requestType
	};
	
	
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"timestamp",
		"traceId",
		"orderIndex",
		"operationSignature",
		"classSignature",
		"objectId",
		"parameters",
		"values",
		"requestType",
	};
	
	/** property declarations. */
	private String[] parameters;
	private String[] values;
	private int requestType;
	
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
	 * @param objectId
	 *            objectId
	 * @param parameters
	 *            parameters
	 * @param values
	 *            values
	 * @param requestType
	 *            requestType
	 */
	public EntryLevelBeforeOperationObjectEvent(final long timestamp, final long traceId, final int orderIndex, final String operationSignature, final String classSignature, final int objectId, final String[] parameters, final String[] values, final int requestType) {
		super(timestamp, traceId, orderIndex, operationSignature, classSignature, objectId);
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
	 * @deprecated since 1.13. Use {@link #EntryLevelBeforeOperationObjectEvent(IValueDeserializer)} instead.
	 */
	@Deprecated
	public EntryLevelBeforeOperationObjectEvent(final Object[] values) { // NOPMD (direct store of values)
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
	 * @deprecated since 1.13. Use {@link #EntryLevelBeforeOperationObjectEvent(IValueDeserializer)} instead.
	 */
	@Deprecated
	protected EntryLevelBeforeOperationObjectEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.parameters = (String[]) values[6];
		this.values = (String[]) values[7];
		this.requestType = (Integer) values[8];
	}

	
	/**
	 * @param deserializer
	 *            The deserializer to use
	 */
	public EntryLevelBeforeOperationObjectEvent(final IValueDeserializer deserializer) {
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
			this.getTimestamp(),
			this.getTraceId(),
			this.getOrderIndex(),
			this.getOperationSignature(),
			this.getClassSignature(),
			this.getObjectId(),
			this.getParameters(),
			this.getValues(),
			this.getRequestType()
		};
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
		stringRegistry.get(this.getOperationSignature());
		stringRegistry.get(this.getClassSignature());
		// get array length
		int _parameters_size0 = this.getParameters().length;
		for (int i0=0;i0<_parameters_size0;i0++)
			stringRegistry.get(this.getParameters()[i0]);
		
		// get array length
		int _values_size0 = this.getValues().length;
		for (int i0=0;i0<_values_size0;i0++)
			stringRegistry.get(this.getValues()[i0]);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		//super.serialize(serializer);
		serializer.putLong(this.getTimestamp());
		serializer.putLong(this.getTraceId());
		serializer.putInt(this.getOrderIndex());
		serializer.putString(this.getOperationSignature());
		serializer.putString(this.getClassSignature());
		serializer.putInt(this.getObjectId());
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
		if (obj == null) return false;
		if (obj == this) return true;
		if (obj.getClass() != this.getClass()) return false;
		
		final EntryLevelBeforeOperationObjectEvent castedRecord = (EntryLevelBeforeOperationObjectEvent) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) return false;
		if (this.getTimestamp() != castedRecord.getTimestamp()) return false;
		if (this.getTraceId() != castedRecord.getTraceId()) return false;
		if (this.getOrderIndex() != castedRecord.getOrderIndex()) return false;
		if (!this.getOperationSignature().equals(castedRecord.getOperationSignature())) return false;
		if (!this.getClassSignature().equals(castedRecord.getClassSignature())) return false;
		if (this.getObjectId() != castedRecord.getObjectId()) return false;
		// get array length
		int _parameters_size0 = this.getParameters().length;
		if (_parameters_size0 != castedRecord.getParameters().length)
			return false;
		for (int i0=0;i0<_parameters_size0;i0++)
			if (!this.getParameters()[i0].equals(castedRecord.getParameters()[i0])) return false;
		
		// get array length
		int _values_size0 = this.getValues().length;
		if (_values_size0 != castedRecord.getValues().length)
			return false;
		for (int i0=0;i0<_values_size0;i0++)
			if (!this.getValues()[i0].equals(castedRecord.getValues()[i0])) return false;
		
		if (this.getRequestType() != castedRecord.getRequestType()) return false;
		return true;
	}
	
	public final String[] getParameters() {
		return this.parameters;
	}
	
	public final void setParameters(String[] parameters) {
		this.parameters = parameters;
	}
	
	public final String[] getValues() {
		return this.values;
	}
	
	public final void setValues(String[] values) {
		this.values = values;
	}
	
	public final int getRequestType() {
		return this.requestType;
	}
	
	public final void setRequestType(int requestType) {
		this.requestType = requestType;
	}
}
