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
package org.iobserve.common.record;

import java.nio.BufferOverflowException;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;


/**
 * @author Reiner Jung
 * API compatibility: Kieker 1.14.0
 * 
 * @since 0.0.3
 */
public class JSSObservationEvent extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // JSSObservationEvent.iObserveTime
			 + TYPE_SIZE_INT // JSSObservationEvent.type
			 + TYPE_SIZE_STRING // JSSObservationEvent.typeOfObservedEvent
			 + TYPE_SIZE_LONG; // JSSObservationEvent.timeOfObservedEvent
	
	public static final Class<?>[] TYPES = {
		long.class, // JSSObservationEvent.iObserveTime
		JSSObservationPoint.class, // JSSObservationEvent.type
		String.class, // JSSObservationEvent.typeOfObservedEvent
		long.class, // JSSObservationEvent.timeOfObservedEvent
	};
	
	/** default constants. */
	public static final String TYPE_OF_OBSERVED_EVENT = "";
	private static final long serialVersionUID = -5032288461041547665L;
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"iObserveTime",
		"type",
		"typeOfObservedEvent",
		"timeOfObservedEvent",
	};
	
	/** property declarations. */
	private final long iObserveTime;
	private final JSSObservationPoint type;
	private final String typeOfObservedEvent;
	private final long timeOfObservedEvent;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param iObserveTime
	 *            iObserveTime
	 * @param type
	 *            type
	 * @param typeOfObservedEvent
	 *            typeOfObservedEvent
	 * @param timeOfObservedEvent
	 *            timeOfObservedEvent
	 */
	public JSSObservationEvent(final long iObserveTime, final JSSObservationPoint type, final String typeOfObservedEvent, final long timeOfObservedEvent) {
		this.iObserveTime = iObserveTime;
		this.type = type;
		this.typeOfObservedEvent = typeOfObservedEvent == null?"":typeOfObservedEvent;
		this.timeOfObservedEvent = timeOfObservedEvent;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 *
	 * @deprecated to be removed 1.15
	 */
	@Deprecated
	public JSSObservationEvent(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.iObserveTime = (Long) values[0];
		this.type = (JSSObservationPoint) values[1];
		this.typeOfObservedEvent = (String) values[2];
		this.timeOfObservedEvent = (Long) values[3];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 *
	 * @deprecated to be removed 1.15
	 */
	@Deprecated
	protected JSSObservationEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
		this.iObserveTime = (Long) values[0];
		this.type = (JSSObservationPoint) values[1];
		this.typeOfObservedEvent = (String) values[2];
		this.timeOfObservedEvent = (Long) values[3];
	}

	
	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public JSSObservationEvent(final IValueDeserializer deserializer) throws RecordInstantiationException {
		this.iObserveTime = deserializer.getLong();
		this.type = deserializer.getEnumeration(JSSObservationPoint.class);
		this.typeOfObservedEvent = deserializer.getString();
		this.timeOfObservedEvent = deserializer.getLong();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @deprecated to be removed in 1.15
	 */
	@Override
	@Deprecated
	public Object[] toArray() {
		return new Object[] {
			this.getIObserveTime(),
			this.getType(),
			this.getTypeOfObservedEvent(),
			this.getTimeOfObservedEvent(),
		};
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		//super.serialize(serializer);
		serializer.putLong(this.getIObserveTime());
		serializer.putInt(this.getType().ordinal());
		serializer.putString(this.getTypeOfObservedEvent());
		serializer.putLong(this.getTimeOfObservedEvent());
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
	 * @deprecated to be rmeoved in 1.15
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
		
		final JSSObservationEvent castedRecord = (JSSObservationEvent) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (this.getIObserveTime() != castedRecord.getIObserveTime()) {
			return false;
		}
		if (this.getType() != castedRecord.getType()) {
			return false;
		}
		if (!this.getTypeOfObservedEvent().equals(castedRecord.getTypeOfObservedEvent())) {
			return false;
		}
		if (this.getTimeOfObservedEvent() != castedRecord.getTimeOfObservedEvent()) {
			return false;
		}
		
		return true;
	}
	
	public final long getIObserveTime() {
		return this.iObserveTime;
	}
	
	
	public final JSSObservationPoint getType() {
		return this.type;
	}
	
	
	public final String getTypeOfObservedEvent() {
		return this.typeOfObservedEvent;
	}
	
	
	public final long getTimeOfObservedEvent() {
		return this.timeOfObservedEvent;
	}
	
}
