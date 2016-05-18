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

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.IRegistry;
<<<<<<< HEAD

/**
 * @author Generic Kieker
 *
 * @since 1.10
 */
public abstract class ContainerEvent extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory {
	private static final long serialVersionUID = 8392000994764985766L;

=======
import kieker.common.util.Version;


/**
 * @author Generic Kieker
 * 
 * @since 1.10
 */
public abstract class ContainerEvent extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory {
		private static final long serialVersionUID = 8392000994764985766L;
	
	
>>>>>>> 8369799fad6bbba0ee0c594bd69ce6afef0b7b41
	/* user-defined constants */
	/* default constants */
	public static final String URL = "";
	/* property declarations */
	private final String url;

	/**
	 * Creates a new instance of this class using the given parameters.
<<<<<<< HEAD
	 *
=======
	 * 
>>>>>>> 8369799fad6bbba0ee0c594bd69ce6afef0b7b41
	 * @param url
	 *            url
	 */
	public ContainerEvent(final String url) {
<<<<<<< HEAD
		this.url = url == null ? "" : url;
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 *
=======
		this.url = url == null?"":url;
	}

	
	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
>>>>>>> 8369799fad6bbba0ee0c594bd69ce6afef0b7b41
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected ContainerEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
		this.url = (String) values[0];
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
	public ContainerEvent(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		this.url = stringRegistry.get(buffer.getInt());
	}

	/**
	 * {@inheritDoc}
<<<<<<< HEAD
	 *
=======
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

	public final String getUrl() {
		return this.url;
	}
<<<<<<< HEAD

=======
	
>>>>>>> 8369799fad6bbba0ee0c594bd69ce6afef0b7b41
}
