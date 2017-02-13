package org.iobserve.common.record;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Reiner Jung
 * 
 * @since 1.0
 */
public final class ServletTraceHelperFactory implements IRecordFactory<ServletTraceHelper> {
	
	@Override
	public ServletTraceHelper create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new ServletTraceHelper(buffer, stringRegistry);
	}
	
	@Override
	public ServletTraceHelper create(final Object[] values) {
		return new ServletTraceHelper(values);
	}
	
	public int getRecordSizeInBytes() {
		return ServletTraceHelper.SIZE;
	}
}
