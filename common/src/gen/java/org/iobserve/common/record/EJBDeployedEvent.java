/***************************************************************************
 * Copyright 2018 iObserve Project (http://www.iobserve-devops.net)
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
package org.iobserve.common.record;

import java.nio.BufferOverflowException;

import kieker.common.exception.RecordInstantiationException;
import org.iobserve.common.record.EJBDescriptor;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;
import kieker.common.util.registry.IRegistry;

import org.iobserve.common.record.IDeployedEvent;

/**
 * @author Reiner Jung
 * API compatibility: Kieker 1.13.0
 * 
 * @since 0.0.2
 */
public class EJBDeployedEvent extends EJBDescriptor implements IDeployedEvent {
	private static final long serialVersionUID = -5298761021303334329L;

	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // IEventRecord.timestamp
			 + TYPE_SIZE_STRING // EJBDescriptor.service
			 + TYPE_SIZE_STRING // EJBDescriptor.context
			 + TYPE_SIZE_STRING // EJBDescriptor.deploymentId
	;
	
	public static final Class<?>[] TYPES = {
		long.class, // IEventRecord.timestamp
		String.class, // EJBDescriptor.service
		String.class, // EJBDescriptor.context
		String.class, // EJBDescriptor.deploymentId
	};
	
	
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"timestamp",
		"service",
		"context",
		"deploymentId",
	};
	
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param service
	 *            service
	 * @param context
	 *            context
	 * @param deploymentId
	 *            deploymentId
	 */
	public EJBDeployedEvent(final long timestamp, final String service, final String context, final String deploymentId) {
		super(timestamp, service, context, deploymentId);
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 *
	 * @deprecated since 1.13. Use {@link #EJBDeployedEvent(IValueDeserializer)} instead.
	 */
	@Deprecated
	public EJBDeployedEvent(final Object[] values) { // NOPMD (direct store of values)
		super(values, TYPES);
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 *
	 * @deprecated since 1.13. Use {@link #EJBDeployedEvent(IValueDeserializer)} instead.
	 */
	@Deprecated
	protected EJBDeployedEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
	}

	
	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 */
	public EJBDeployedEvent(final IValueDeserializer deserializer) throws RecordInstantiationException {
		super(deserializer);
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
			this.getService(),
			this.getContext(),
			this.getDeploymentId()
		};
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
		stringRegistry.get(this.getService());
		stringRegistry.get(this.getContext());
		stringRegistry.get(this.getDeploymentId());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		//super.serialize(serializer);
		serializer.putLong(this.getTimestamp());
		serializer.putString(this.getService());
		serializer.putString(this.getContext());
		serializer.putString(this.getDeploymentId());
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
		
		final EJBDeployedEvent castedRecord = (EJBDeployedEvent) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) return false;
		if (this.getTimestamp() != castedRecord.getTimestamp()) return false;
		if (!this.getService().equals(castedRecord.getService())) return false;
		if (!this.getContext().equals(castedRecord.getContext())) return false;
		if (!this.getDeploymentId().equals(castedRecord.getDeploymentId())) return false;
		return true;
	}
	
}
