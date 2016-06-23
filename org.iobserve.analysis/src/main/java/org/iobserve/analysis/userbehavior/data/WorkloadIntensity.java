package org.iobserve.analysis.userbehavior.data;

public class WorkloadIntensity {
	
	private long interarrivalTimeOfUserSessions;
	private int maxNumberOfConcurrentUsers;
	private int avgNumberOfConcurrentUsers;
	
	
	public long getInterarrivalTimeOfUserSessions() {
		return interarrivalTimeOfUserSessions;
	}
	public void setInterarrivalTimeOfUserSessions(long interarrivalTimeOfUserSessions) {
		this.interarrivalTimeOfUserSessions = interarrivalTimeOfUserSessions;
	}
	public int getMaxNumberOfConcurrentUsers() {
		return maxNumberOfConcurrentUsers;
	}
	public void setMaxNumberOfConcurrentUsers(int maxNumberOfConcurrentUsers) {
		this.maxNumberOfConcurrentUsers = maxNumberOfConcurrentUsers;
	}
	public int getAvgNumberOfConcurrentUsers() {
		return avgNumberOfConcurrentUsers;
	}
	public void setAvgNumberOfConcurrentUsers(int avgNumberOfConcurrentUsers) {
		this.avgNumberOfConcurrentUsers = avgNumberOfConcurrentUsers;
	}
	
	

}
