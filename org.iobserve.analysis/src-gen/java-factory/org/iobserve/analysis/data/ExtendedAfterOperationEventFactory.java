package org.iobserve.analysis.data;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Christoph Dornieden
 * 
 * @since 1.0
 */
public final class ExtendedAfterOperationEventFactory implements IRecordFactory<ExtendedAfterOperationEvent> {
	
	@Override
	public ExtendedAfterOperationEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new ExtendedAfterOperationEvent(buffer, stringRegistry);
	}
	
	@Override
	public ExtendedAfterOperationEvent create(final Object[] values) {
		return new ExtendedAfterOperationEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return ExtendedAfterOperationEvent.SIZE;
	}
}
