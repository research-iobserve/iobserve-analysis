package org.iobserve.common.record;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author iObserve
 * 
 * @since 1.10
 */
public final class SessionStartEventFactory implements IRecordFactory<SessionStartEvent> {
	
	@Override
	public SessionStartEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new SessionStartEvent(buffer, stringRegistry);
	}
	
	@Override
	public SessionStartEvent create(final Object[] values) {
		return new SessionStartEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return SessionStartEvent.SIZE;
	}
}
