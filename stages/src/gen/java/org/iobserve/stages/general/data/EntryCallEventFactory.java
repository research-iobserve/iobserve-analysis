/***************************************************************************
 * Copyright 2020 iObserve Project (https://www.iobserve-devops.net)
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


import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Reiner Jung
 * 
 * @since 0.0.2
 */
public final class EntryCallEventFactory implements IRecordFactory<EntryCallEvent> {
	
	@Override
	public EntryCallEvent create(final IValueDeserializer deserializer) throws RecordInstantiationException {
		return new EntryCallEvent(deserializer);
	}
	@Override
			public String[] getValueNames() {
				return EntryCallEvent.VALUE_NAMES; // NOPMD
			}
	@Override
			public Class<?>[] getValueTypes() {
				return EntryCallEvent.TYPES; // NOPMD
			}
	public int getRecordSizeInBytes() {
		return EntryCallEvent.SIZE;
	}
}
