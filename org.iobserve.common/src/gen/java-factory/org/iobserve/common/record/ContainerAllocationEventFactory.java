package org.iobserve.common.record;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author iObserve
 * 
 * @since 1.10
 */
public final class ContainerAllocationEventFactory implements IRecordFactory<ContainerAllocationEvent> {
	
	@Override
	public ContainerAllocationEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new ContainerAllocationEvent(buffer, stringRegistry);
	}
	
	@Override
	public ContainerAllocationEvent create(final Object[] values) {
		return new ContainerAllocationEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return ContainerAllocationEvent.SIZE;
	}
}
