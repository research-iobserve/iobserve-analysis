package org.iobserve.common.record;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author iObserve
 * 
 * @since 1.10
 */
public final class EJBUndeployedEventFactory implements IRecordFactory<EJBUndeployedEvent> {
	
	@Override
	public EJBUndeployedEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new EJBUndeployedEvent(buffer, stringRegistry);
	}
	
	@Override
	public EJBUndeployedEvent create(final Object[] values) {
		return new EJBUndeployedEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return EJBUndeployedEvent.SIZE;
	}
}
