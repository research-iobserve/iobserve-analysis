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

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Generic Kieker
 * 
 * @since 1.10
 */
public final class EJBDeployedEventFactory implements IRecordFactory<EJBDeployedEvent> {
	
	@Override
	public EJBDeployedEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new EJBDeployedEvent(buffer, stringRegistry);
	}
	
	@Override
	public EJBDeployedEvent create(final Object[] values) {
		return new EJBDeployedEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return EJBDeployedEvent.SIZE;
	}
}
