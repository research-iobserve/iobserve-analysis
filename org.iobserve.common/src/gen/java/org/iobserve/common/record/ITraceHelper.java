package org.iobserve.common.record;

import kieker.common.record.IMonitoringRecord;

/**
 * @author Reiner Jung
 * 
 * @since 1.0
 */
public interface ITraceHelper extends IMonitoringRecord {
	public long getTraceId() ;
	
	public String getHost() ;
	
	public int getPort() ;
	
}
