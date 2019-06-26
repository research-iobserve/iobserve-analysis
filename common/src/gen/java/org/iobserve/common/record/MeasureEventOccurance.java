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
package org.iobserve.common.record;

import java.nio.BufferOverflowException;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
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
			 + TYPE_SIZE_INT; // MeasureEventOccurance.point
	
	public static final Class<?>[] TYPES = {
		long.class, // MeasureEventOccurance.timestamp
		long.class, // MeasureEventOccurance.id
		EventTypes.class, // MeasureEventOccurance.type
		ObservationPoint.class, // MeasureEventOccurance.point
	};
	
	private static final long serialVersionUID = 2782001322842515724L;
	
	/** property name array. */
	public static final String[] VALUE_NAMES = {
		"timestamp",
		"id",
		"type",
		"point",
	};
	
	/** property declarations. */
	private long timestamp;
	private final long id;
	private final EventTypes type;
	private final ObservationPoint point;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param id
	 *            id
	 * @param type
	 *            type
	 * @param point
	 *            point
	 */
	public MeasureEventOccurance(final long timestamp, final long id, final EventTypes type, final ObservationPoint point) {
		this.timestamp = timestamp;
		this.id = id;
		this.type = type;
		this.point = point;
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
		this.point = deserializer.getEnumeration(ObservationPoint.class);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		serializer.putLong(this.getTimestamp());
		serializer.putLong(this.getId());
		serializer.putInt(this.getType().ordinal());
		serializer.putInt(this.getPoint().ordinal());
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
		if (this.getPoint() != castedRecord.getPoint()) {
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
		code += ((int)this.getTimestamp());
		code += ((int)this.getId());
		code += this.getType().hashCode();
		code += this.getPoint().hashCode();
		
		return code;
	}
	
	public final long getTimestamp() {
		return this.timestamp;
	}
	
	public final void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public final long getId() {
		return this.id;
	}
	
	
	public final EventTypes getType() {
		return this.type;
	}
	
	
	public final ObservationPoint getPoint() {
		return this.point;
	}
	
}
