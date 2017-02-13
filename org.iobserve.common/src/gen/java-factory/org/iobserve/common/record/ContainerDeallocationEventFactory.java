package org.iobserve.common.record;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author iObserve
 * 
 * @since 1.0
 */
public final class ContainerDeallocationEventFactory implements IRecordFactory<ContainerDeallocationEvent> {
	
	@Override
	public ContainerDeallocationEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new ContainerDeallocationEvent(buffer, stringRegistry);
	}
	
	@Override
	public ContainerDeallocationEvent create(final Object[] values) {
		return new ContainerDeallocationEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return ContainerDeallocationEvent.SIZE;
	}
}
