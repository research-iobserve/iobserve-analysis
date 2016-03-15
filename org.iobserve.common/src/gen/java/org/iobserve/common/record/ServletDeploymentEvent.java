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

package org.iobserve.common.record;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.flow.AbstractEvent;
import kieker.common.util.registry.IRegistry;

/**
 * @author Generic Kieker
 *
 * @since 1.10
 */
public abstract class ServletDeploymentEvent extends AbstractEvent {
	private static final long serialVersionUID = -3181427847418420309L;

	/* user-defined constants */
	/* default constants */
	public static final String SERIVCE = "";
	public static final String CONTEXT = "";
	public static final String DEPLOYMENT_ID = "";
	/* property declarations */
	private final String serivce;
	private final String context;
	private final String deploymentId;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param timestamp
	 *            timestamp
	 * @param serivce
	 *            serivce
	 * @param context
	 *            context
	 * @param deploymentId
	 *            deploymentId
	 */
	public ServletDeploymentEvent(final long timestamp, final String serivce, final String context, final String deploymentId) {
		super(timestamp);
		this.serivce = serivce == null ? "" : serivce;
		this.context = context == null ? "" : context;
		this.deploymentId = deploymentId == null ? "" : deploymentId;
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 *
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected ServletDeploymentEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.serivce = (String) values[1];
		this.context = (String) values[2];
		this.deploymentId = (String) values[3];
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
	public ServletDeploymentEvent(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		super(buffer, stringRegistry);
		this.serivce = stringRegistry.get(buffer.getInt());
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

	public final String getSerivce() {
		return this.serivce;
	}

	public final String getContext() {
		return this.context;
	}

	public final String getDeploymentId() {
		return this.deploymentId;
	}

}
