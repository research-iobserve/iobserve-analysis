/***************************************************************************
 * Copyright 2017 iObserve Project (http://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.analysis.data;

import java.nio.BufferOverflowException;

import org.iobserve.analysis.data.EntryCallEvent;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;
import kieker.common.util.registry.IRegistry;

import org.iobserve.common.record.IUserInformation;

/**
 * @author Christoph Dornieden
 * API compatibility: Kieker 1.13.0
 * 
 * @since 0.0.2-cdor
 */
public class ExtendedEntryCallEvent extends EntryCallEvent implements IUserInformation {
	private static final long serialVersionUID = -2306741230906112991L;

	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // EntryCallEvent.entryTime
			 + TYPE_SIZE_LONG // EntryCallEvent.exitTime
			 + TYPE_SIZE_STRING // EntryCallEvent.operationSignature
			 + TYPE_SIZE_STRING // EntryCallEvent.classSignature
			 + TYPE_SIZE_STRING // EntryCallEvent.sessionId
			 + TYPE_SIZE_STRING // EntryCallEvent.hostname
			 + TYPE_SIZE_STRING // IUserInformation.informations
	;
	
	public static final Class<?>[] TYPES = {
		long.class, // EntryCallEvent.entryTime
		long.class, // EntryCallEvent.exitTime
		String.class, // EntryCallEvent.operationSignature
		String.class, // EntryCallEvent.classSignature
		String.class, // EntryCallEvent.sessionId
		String.class, // EntryCallEvent.hostname
		String.class, // IUserInformation.informations
	};
	
	
	/** default constants. */
	public static final String INFORMATIONS = "";
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"entryTime",
		"exitTime",
		"operationSignature",
		"classSignature",
		"sessionId",
		"hostname",
		"informations",
	};
	
	/** property declarations. */
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
	 *
	 * @deprecated since 1.13. Use {@link #ExtendedEntryCallEvent(IValueDeserializer)} instead.
	 */
	@Deprecated
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
	 *
	 * @deprecated since 1.13. Use {@link #ExtendedEntryCallEvent(IValueDeserializer)} instead.
	 */
	@Deprecated
	protected ExtendedEntryCallEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.informations = (String) values[6];
	}

	
	/**
	 * @param deserializer
	 *            The deserializer to use
	 */
	public ExtendedEntryCallEvent(final IValueDeserializer deserializer) {
		super(deserializer);
		this.informations = deserializer.getString();
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
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		//super.serialize(serializer);
		serializer.putLong(this.getEntryTime());
		serializer.putLong(this.getExitTime());
		serializer.putString(this.getOperationSignature());
		serializer.putString(this.getClassSignature());
		serializer.putString(this.getSessionId());
		serializer.putString(this.getHostname());
		serializer.putString(this.getInformations());
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
