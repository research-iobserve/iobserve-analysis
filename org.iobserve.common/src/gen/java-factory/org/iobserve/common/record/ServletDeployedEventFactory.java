package org.iobserve.common.record;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author iObserve
 * 
 * @since 1.10
 */
public final class ServletDeployedEventFactory implements IRecordFactory<ServletDeployedEvent> {
	
	@Override
	public ServletDeployedEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new ServletDeployedEvent(buffer, stringRegistry);
	}
	
	@Override
	public ServletDeployedEvent create(final Object[] values) {
		return new ServletDeployedEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return ServletDeployedEvent.SIZE;
	}
}
