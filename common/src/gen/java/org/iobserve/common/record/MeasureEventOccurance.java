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
 * @author Generic Kieker
 * API compatibility: Kieker 1.15.0
 * 
 * @since 1.15
 */
public class MeasureEventOccurance extends AbstractMonitoringRecord  {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // MeasureEventOccurance.timestamp
			 + TYPE_SIZE_LONG // MeasureEventOccurance.id
			 + TYPE_SIZE_INT // MeasureEventOccurance.type
			 + TYPE_SIZE_STRING; // MeasureEventOccurance.label
	
	public static final Class<?>[] TYPES = {
		long.class, // MeasureEventOccurance.timestamp
		long.class, // MeasureEventOccurance.id
		EventTypes.class, // MeasureEventOccurance.type
		String.class, // MeasureEventOccurance.label
	};
	
	/** default constants. */
	public static final String LABEL = "";
	private static final long serialVersionUID = -1882324022611495306L;
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"timestamp",
		"id",
		"type",
		"label",
	};
	
	/** property declarations. */
	private final long timestamp;
	private final long id;
	private final EventTypes type;
	private final String label;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param id
	 *            id
	 * @param type
	 *            type
	 * @param label
	 *            label
	 */
	public MeasureEventOccurance(final long timestamp, final long id, final EventTypes type, final String label) {
		this.timestamp = timestamp;
		this.id = id;
		this.type = type;
		this.label = label == null?"":label;
	}



	
	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public MeasureEventOccurance(final IValueDeserializer deserializer) throws RecordInstantiationException {
		this.timestamp = deserializer.getLong();
		this.id = deserializer.getLong();
		this.type = deserializer.getEnumeration(EventTypes.class);
		this.label = deserializer.getString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		//super.serialize(serializer);
		serializer.putLong(this.getTimestamp());
		serializer.putLong(this.getId());
		serializer.putInt(this.getType().ordinal());
		serializer.putString(this.getLabel());
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
		
		final MeasureEventOccurance castedRecord = (MeasureEventOccurance) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (this.getTimestamp() != castedRecord.getTimestamp()) {
			return false;
		}
		if (this.getId() != castedRecord.getId()) {
			return false;
		}
		if (this.getType() != castedRecord.getType()) {
			return false;
		}
		if (!this.getLabel().equals(castedRecord.getLabel())) {
			return false;
		}
		
		return true;
	}
	
	public final long getTimestamp() {
		return this.timestamp;
	}
	
	
	public final long getId() {
		return this.id;
	}
	
	
	public final EventTypes getType() {
		return this.type;
	}
	
	
	public final String getLabel() {
		return this.label;
	}
	
}
