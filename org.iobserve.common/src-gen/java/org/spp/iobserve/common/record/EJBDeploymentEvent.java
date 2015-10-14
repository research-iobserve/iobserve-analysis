/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package org.spp.iobserve.common.record;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.util.registry.IRegistry;
import kieker.common.util.Version;

import kieker.common.record.flow.AbstractEvent;

/**
 * @author Generic Kieker
 * 
 * @since 1.10
 */
public abstract class EJBDeploymentEvent extends AbstractEvent  {
		private static final long serialVersionUID = -25855649455459678L;
	
	
	/* user-defined constants */
	/* default constants */
	public static final String CONTEXT = "";
	public static final String DEPLOYMENT_ID = "";
	/* property declarations */
	private final String context;
	private final String deploymentId;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param context
	 *            context
	 * @param deploymentId
	 *            deploymentId
	 */
	public EJBDeploymentEvent(final long timestamp, final String context, final String deploymentId) {
		super(timestamp);
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
	 */
	protected EJBDeploymentEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.context = (String) values[1];
		this.deploymentId = (String) values[2];
	}

	/**
	 * This constructor converts the given array into a record.
	 * 
	 * @param buffer
	 *            The bytes for the record.
	 * 
	 * @throws BufferUnderflowException
	 *             if buffer not sufficient
	 */
	public EJBDeploymentEvent(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		super(buffer, stringRegistry);
		this.context = stringRegistry.get(buffer.getInt());
		this.deploymentId = stringRegistry.get(buffer.getInt());
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
	 * 
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.BinaryFactory} mechanism. Hence, this method is not implemented.
	 */
	@Override
	@Deprecated
	public void initFromBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		throw new UnsupportedOperationException();
	}

	public final String getContext() {
		return this.context;
	}
	
	public final String getDeploymentId() {
		return this.deploymentId;
	}
	
}
