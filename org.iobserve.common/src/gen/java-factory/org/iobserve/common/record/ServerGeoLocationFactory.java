package org.iobserve.common.record;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author iObserve
 * 
 * @since 1.10
 */
public final class ServerGeoLocationFactory implements IRecordFactory<ServerGeoLocation> {
	
	@Override
	public ServerGeoLocation create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new ServerGeoLocation(buffer, stringRegistry);
	}
	
	@Override
	public ServerGeoLocation create(final Object[] values) {
		return new ServerGeoLocation(values);
	}
	
	public int getRecordSizeInBytes() {
		return ServerGeoLocation.SIZE;
	}
}
