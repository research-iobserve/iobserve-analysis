package org.iobserve.analysis.data;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Reiner Jung
 * 
 * @since 1.0
 */
public final class EntryCallEventFactory implements IRecordFactory<EntryCallEvent> {
	
	@Override
	public EntryCallEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new EntryCallEvent(buffer, stringRegistry);
	}
	
	@Override
	public EntryCallEvent create(final Object[] values) {
		return new EntryCallEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return EntryCallEvent.SIZE;
	}
}
