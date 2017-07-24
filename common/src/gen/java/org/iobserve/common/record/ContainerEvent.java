<<<<<<< HEAD:org.iobserve.common/src/gen/java/org/iobserve/common/record/ContainerEvent.java
=======
/***************************************************************************
 * Copyright 2017 iObserve Project
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
>>>>>>> master:common/src/gen/java/org/iobserve/common/record/ContainerEvent.java
package org.iobserve.common.record;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.IRegistry;


/**
 * @author iObserve
 * 
 * @since 1.10
 */
public abstract class ContainerEvent extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory {
	private static final long serialVersionUID = -2037622396753518154L;

<<<<<<< HEAD:org.iobserve.common/src/gen/java/org/iobserve/common/record/ContainerEvent.java
	
	/** user-defined constants */
=======
>>>>>>> master:common/src/gen/java/org/iobserve/common/record/ContainerEvent.java
	
	/** default constants */
	public static final String URL = "";
	
<<<<<<< HEAD:org.iobserve.common/src/gen/java/org/iobserve/common/record/ContainerEvent.java
	/** property declarations */
	private final String url;
=======
	/** default constants. */
	public static final String URL = "";
	
		
	/** property declarations. */
	private String url;
>>>>>>> master:common/src/gen/java/org/iobserve/common/record/ContainerEvent.java
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param url
	 *            url
	 */
	public ContainerEvent(final String url) {
		this.url = url == null?"":url;
	}


	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
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
	 * This constructor converts the given buffer into a record.
	 * 
	 * @param buffer
	 *            The bytes for the record
	 * @param stringRegistry
	 *            The string registry for deserialization
	 * 
	 * @throws BufferUnderflowException
	 *             if buffer not sufficient
	 */
	public ContainerEvent(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		this.url = stringRegistry.get(buffer.getInt());
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		if (obj.getClass() != this.getClass()) return false;
		
		final ContainerEvent castedRecord = (ContainerEvent) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) return false;
		if (!this.getUrl().equals(castedRecord.getUrl())) return false;
		return true;
	}
	
	public final String getUrl() {
		return this.url;
<<<<<<< HEAD:org.iobserve.common/src/gen/java/org/iobserve/common/record/ContainerEvent.java
	}	
=======
	}
	
	public final void setUrl(String url) {
		this.url = url;
	}
>>>>>>> master:common/src/gen/java/org/iobserve/common/record/ContainerEvent.java
}
