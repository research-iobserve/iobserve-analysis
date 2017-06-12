package org.iobserve.common.record;

import kieker.common.record.IMonitoringRecord;

/**
 * @author iObserve
 * 
 * @since 1.10
 */
public interface ISessionEvent extends IMonitoringRecord {
	public String getSessionId() ;
	
}
