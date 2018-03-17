/***************************************************************************
 * Copyright 2018 iObserve Project (http://iobserve-devops.net)
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


import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.flow.AbstractEvent;
import kieker.common.record.io.IValueDeserializer;


/**
 * @author Reiner Jung
 * API compatibility: Kieker 1.13.0
 * 
 * @since 0.0.2
 */
public abstract class EJBDescriptor extends AbstractEvent  {
	private static final long serialVersionUID = -539937375049134834L;

	
	
	/** default constants. */
	public static final String SERVICE = "";
	public static final String CONTEXT = "";
	public static final String DEPLOYMENT_ID = "";
	
		
	/** property declarations. */
	private final String service;
	private final String context;
	private final String deploymentId;
	
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
	public EJBDescriptor(final long timestamp, final String service, final String context, final String deploymentId) {
		super(timestamp);
		this.service = service == null?"":service;
		this.context = context == null?"":context;
		this.deploymentId = deploymentId == null?"":deploymentId;
	}


	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 *
	 * @deprecated since 1.13. Use {@link #EJBDescriptor(IValueDeserializer)} instead.
	 */
	@Deprecated
	protected EJBDescriptor(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.service = (String) values[1];
		this.context = (String) values[2];
		this.deploymentId = (String) values[3];
	}

	
	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 */
	public EJBDescriptor(final IValueDeserializer deserializer) throws RecordInstantiationException {
		super(deserializer);
		this.service = deserializer.getString();
		this.context = deserializer.getString();
		this.deploymentId = deserializer.getString();
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
		
		final EJBDescriptor castedRecord = (EJBDescriptor) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) return false;
		if (this.getTimestamp() != castedRecord.getTimestamp()) return false;
		if (!this.getService().equals(castedRecord.getService())) return false;
		if (!this.getContext().equals(castedRecord.getContext())) return false;
		if (!this.getDeploymentId().equals(castedRecord.getDeploymentId())) return false;
		return true;
	}
	
	public final String getService() {
		return this.service;
	}
	
	
	public final String getContext() {
		return this.context;
	}
	
	
	public final String getDeploymentId() {
		return this.deploymentId;
	}
	
}
