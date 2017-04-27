package org.iobserve.common.record;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author iObserve
 * 
 * @since 1.10
 */
public final class SessionEndEventFactory implements IRecordFactory<SessionEndEvent> {
	
	@Override
	public SessionEndEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new SessionEndEvent(buffer, stringRegistry);
	}
	
	@Override
	public SessionEndEvent create(final Object[] values) {
		return new SessionEndEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return SessionEndEvent.SIZE;
	}
}
