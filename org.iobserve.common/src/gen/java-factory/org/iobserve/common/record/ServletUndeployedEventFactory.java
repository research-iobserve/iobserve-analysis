package org.iobserve.common.record;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author iObserve
 * 
 * @since 1.0
 */
public final class ServletUndeployedEventFactory implements IRecordFactory<ServletUndeployedEvent> {
	
	@Override
	public ServletUndeployedEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new ServletUndeployedEvent(buffer, stringRegistry);
	}
	
	@Override
	public ServletUndeployedEvent create(final Object[] values) {
		return new ServletUndeployedEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return ServletUndeployedEvent.SIZE;
	}
}
