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


import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Christoph Dornieden
 * 
 * @since 0.0.2
 */
public final class ExtendedAfterOperationEventFactory implements IRecordFactory<ExtendedAfterOperationEvent> {
	
	
	@Override
	public ExtendedAfterOperationEvent create(final IValueDeserializer deserializer) throws RecordInstantiationException {
		return new ExtendedAfterOperationEvent(deserializer);
	}
	
	@Override
	@Deprecated
	public ExtendedAfterOperationEvent create(final Object[] values) {
		return new ExtendedAfterOperationEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return ExtendedAfterOperationEvent.SIZE;
	}
}
