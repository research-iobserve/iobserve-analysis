/***************************************************************************
 * Copyright 2019 Kieker Project (http://kieker-monitoring.net)
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
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;


/**
 * @author Reiner Jung
 * API compatibility: Kieker 1.15.0
 * 
 * @since 0.0.2
 */
public class EntryCallEvent extends AbstractMonitoringRecord  {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // EntryCallEvent.entryTime
			 + TYPE_SIZE_LONG // EntryCallEvent.exitTime
			 + TYPE_SIZE_STRING // EntryCallEvent.operationSignature
			 + TYPE_SIZE_STRING // EntryCallEvent.classSignature
			 + TYPE_SIZE_STRING // EntryCallEvent.sessionId
			 + TYPE_SIZE_STRING; // EntryCallEvent.hostname
	
	public static final Class<?>[] TYPES = {
		long.class, // EntryCallEvent.entryTime
		long.class, // EntryCallEvent.exitTime
		String.class, // EntryCallEvent.operationSignature
		String.class, // EntryCallEvent.classSignature
		String.class, // EntryCallEvent.sessionId
		String.class, // EntryCallEvent.hostname
	};
	
	/** default constants. */
	public static final String OPERATION_SIGNATURE = "";
	public static final String CLASS_SIGNATURE = "";
	public static final String SESSION_ID = "";
	public static final String HOSTNAME = "";
	private static final long serialVersionUID = -9019303768669463280L;
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"entryTime",
		"exitTime",
		"operationSignature",
		"classSignature",
		"sessionId",
		"hostname",
	};
	
	/** property declarations. */
	private final long entryTime;
	private final long exitTime;
	private String operationSignature;
	private String classSignature;
	private final String sessionId;
	private final String hostname;
	
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
	 */
	public EntryCallEvent(final long entryTime, final long exitTime, final String operationSignature, final String classSignature, final String sessionId, final String hostname) {
		this.entryTime = entryTime;
		this.exitTime = exitTime;
		this.operationSignature = operationSignature == null?"":operationSignature;
		this.classSignature = classSignature == null?"":classSignature;
		this.sessionId = sessionId == null?"":sessionId;
		this.hostname = hostname == null?"":hostname;
	}



	
	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public EntryCallEvent(final IValueDeserializer deserializer) throws RecordInstantiationException {
		this.entryTime = deserializer.getLong();
		this.exitTime = deserializer.getLong();
		this.operationSignature = deserializer.getString();
		this.classSignature = deserializer.getString();
		this.sessionId = deserializer.getString();
		this.hostname = deserializer.getString();
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
		
		final EntryCallEvent castedRecord = (EntryCallEvent) obj;
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
		
		return code;
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
	
	public final void setOperationSignature(String operationSignature) {
		this.operationSignature = operationSignature;
	}
	
	public final String getClassSignature() {
		return this.classSignature;
	}
	
	public final void setClassSignature(String classSignature) {
		this.classSignature = classSignature;
	}
	
	public final String getSessionId() {
		return this.sessionId;
	}
	
	
	public final String getHostname() {
		return this.hostname;
	}
	
}
