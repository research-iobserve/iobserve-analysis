package org.iobserve.common.record;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author iObserve
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
