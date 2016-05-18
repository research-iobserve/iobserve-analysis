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

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.util.registry.IRegistry;
<<<<<<< HEAD

/**
 * @author Generic Kieker
 *
=======
import kieker.common.util.Version;

import org.iobserve.common.record.EJBDeploymentEvent;
import org.iobserve.common.record.IDeploymentRecord;

/**
 * @author Generic Kieker
 * 
>>>>>>> 8369799fad6bbba0ee0c594bd69ce6afef0b7b41
 * @since 1.10
 */
public class EJBDeployedEvent extends EJBDeploymentEvent implements IDeploymentRecord {
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // AbstractEvent.timestamp
<<<<<<< HEAD
			+ TYPE_SIZE_STRING // EJBDeploymentEvent.context
			+ TYPE_SIZE_STRING // EJBDeploymentEvent.deploymentId
	;
	private static final long serialVersionUID = 9038309625452967861L;

=======
			 + TYPE_SIZE_STRING // EJBDeploymentEvent.context
			 + TYPE_SIZE_STRING // EJBDeploymentEvent.deploymentId
	;
	private static final long serialVersionUID = 9038309625452967861L;
	
>>>>>>> 8369799fad6bbba0ee0c594bd69ce6afef0b7b41
	public static final Class<?>[] TYPES = {
		long.class, // AbstractEvent.timestamp
		String.class, // EJBDeploymentEvent.context
		String.class, // EJBDeploymentEvent.deploymentId
	};
<<<<<<< HEAD

=======
	
>>>>>>> 8369799fad6bbba0ee0c594bd69ce6afef0b7b41
	/* user-defined constants */
	/* default constants */
	/* property declarations */

	/**
	 * Creates a new instance of this class using the given parameters.
<<<<<<< HEAD
	 *
=======
	 * 
>>>>>>> 8369799fad6bbba0ee0c594bd69ce6afef0b7b41
	 * @param timestamp
	 *            timestamp
	 * @param context
	 *            context
	 * @param deploymentId
	 *            deploymentId
	 */
	public EJBDeployedEvent(final long timestamp, final String context, final String deploymentId) {
		super(timestamp, context, deploymentId);
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
<<<<<<< HEAD
	 *
=======
	 * 
>>>>>>> 8369799fad6bbba0ee0c594bd69ce6afef0b7b41
	 * @param values
	 *            The values for the record.
	 */
	public EJBDeployedEvent(final Object[] values) { // NOPMD (direct store of values)
		super(values, TYPES);
	}
<<<<<<< HEAD

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 *
=======
	
	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
>>>>>>> 8369799fad6bbba0ee0c594bd69ce6afef0b7b41
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected EJBDeployedEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
	}

	/**
	 * This constructor converts the given array into a record.
<<<<<<< HEAD
	 *
	 * @param buffer
	 *            The bytes for the record.
	 *
=======
	 * 
	 * @param buffer
	 *            The bytes for the record.
	 * 
>>>>>>> 8369799fad6bbba0ee0c594bd69ce6afef0b7b41
	 * @throws BufferUnderflowException
	 *             if buffer not sufficient
	 */
	public EJBDeployedEvent(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		super(buffer, stringRegistry);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return new Object[] {
			this.getTimestamp(),
			this.getContext(),
			this.getDeploymentId()
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		buffer.putLong(this.getTimestamp());
		buffer.putInt(stringRegistry.get(this.getContext()));
		buffer.putInt(stringRegistry.get(this.getDeploymentId()));
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
	public int getSize() {
		return SIZE;
	}
<<<<<<< HEAD

	/**
	 * {@inheritDoc}
	 *
=======
	/**
	 * {@inheritDoc}
	 * 
>>>>>>> 8369799fad6bbba0ee0c594bd69ce6afef0b7b41
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.Factory} mechanism. Hence, this method is not implemented.
	 */
	@Override
	@Deprecated
	public void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
<<<<<<< HEAD
	 *
=======
	 * 
>>>>>>> 8369799fad6bbba0ee0c594bd69ce6afef0b7b41
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.BinaryFactory} mechanism. Hence, this method is not implemented.
	 */
	@Override
	@Deprecated
	public void initFromBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		throw new UnsupportedOperationException();
	}

}
