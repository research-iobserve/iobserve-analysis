package org.iobserve.analysis.data;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Christoph Dornieden
 * 
 * @since 1.0
 */
public final class ExtendedBeforeOperationEventFactory implements IRecordFactory<ExtendedBeforeOperationEvent> {
	
	@Override
	public ExtendedBeforeOperationEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new ExtendedBeforeOperationEvent(buffer, stringRegistry);
	}
	
	@Override
	public ExtendedBeforeOperationEvent create(final Object[] values) {
		return new ExtendedBeforeOperationEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return ExtendedBeforeOperationEvent.SIZE;
	}
}
