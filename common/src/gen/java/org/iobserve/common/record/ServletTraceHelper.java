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
package org.iobserve.common.record;

import java.nio.BufferOverflowException;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;

import org.iobserve.common.record.ITraceHelper;
import kieker.common.record.flow.IFlowRecord;

/**
 * @author Reiner Jung
 * API compatibility: Kieker 1.15.0
 * 
 * @since 0.0.2
 */
public class ServletTraceHelper extends AbstractMonitoringRecord implements ITraceHelper, IFlowRecord {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // ITraceHelper.traceId
			 + TYPE_SIZE_STRING // ITraceHelper.host
			 + TYPE_SIZE_INT // ITraceHelper.port
			 + TYPE_SIZE_STRING; // ServletTraceHelper.requestURI
	
	public static final Class<?>[] TYPES = {
		long.class, // ITraceHelper.traceId
		String.class, // ITraceHelper.host
		int.class, // ITraceHelper.port
		String.class, // ServletTraceHelper.requestURI
	};
	
	/** property name array. */
	public static final String[] VALUE_NAMES = {
		"traceId",
		"host",
		"port",
		"requestURI",
	};
	
	/** default constants. */
	public static final String HOST = "";
	public static final String REQUEST_URI = "";
	private static final long serialVersionUID = 2363353535794190244L;
	
	/** property declarations. */
	private final long traceId;
	private final String host;
	private final int port;
	private final String requestURI;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param traceId
	 *            traceId
	 * @param host
	 *            host
	 * @param port
	 *            port
	 * @param requestURI
	 *            requestURI
	 */
	public ServletTraceHelper(final long traceId, final String host, final int port, final String requestURI) {
		this.traceId = traceId;
		this.host = host == null?"":host;
		this.port = port;
		this.requestURI = requestURI == null?"":requestURI;
	}


	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public ServletTraceHelper(final IValueDeserializer deserializer) throws RecordInstantiationException {
		this.traceId = deserializer.getLong();
		this.host = deserializer.getString();
		this.port = deserializer.getInt();
		this.requestURI = deserializer.getString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		serializer.putLong(this.getTraceId());
		serializer.putString(this.getHost());
		serializer.putInt(this.getPort());
		serializer.putString(this.getRequestURI());
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
		
		final ServletTraceHelper castedRecord = (ServletTraceHelper) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (this.getTraceId() != castedRecord.getTraceId()) {
			return false;
		}
		if (!this.getHost().equals(castedRecord.getHost())) {
			return false;
		}
		if (this.getPort() != castedRecord.getPort()) {
			return false;
		}
		if (!this.getRequestURI().equals(castedRecord.getRequestURI())) {
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
		code += ((int)this.getTraceId());
		code += this.getHost().hashCode();
		code += ((int)this.getPort());
		code += this.getRequestURI().hashCode();
		
		return code;
	}
	
	public final long getTraceId() {
		return this.traceId;
	}
	
	
	public final String getHost() {
		return this.host;
	}
	
	
	public final int getPort() {
		return this.port;
	}
	
	
	public final String getRequestURI() {
		return this.requestURI;
	}
	
}
