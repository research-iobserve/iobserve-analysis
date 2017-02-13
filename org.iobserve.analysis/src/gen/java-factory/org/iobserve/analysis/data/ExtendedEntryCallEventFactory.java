package org.iobserve.analysis.data;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Christoph Dornieden
 * 
 * @since 1.0
 */
public final class ExtendedEntryCallEventFactory implements IRecordFactory<ExtendedEntryCallEvent> {
	
	@Override
	public ExtendedEntryCallEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new ExtendedEntryCallEvent(buffer, stringRegistry);
	}
	
	@Override
	public ExtendedEntryCallEvent create(final Object[] values) {
		return new ExtendedEntryCallEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return ExtendedEntryCallEvent.SIZE;
	}
}
