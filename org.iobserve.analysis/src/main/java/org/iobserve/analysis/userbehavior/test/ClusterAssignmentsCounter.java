package org.iobserve.analysis.userbehavior.test;

/**
 * Counts the assignments of user user sessions of each user group to a cluster
 * Used for the clustering evaluation
 * 
 * @author David
 *
 */
public class ClusterAssignmentsCounter {
	
	int numberOfUserGroupCustomer;
	int numberOfUserGroupStoreManager;
	int numberOfUserGroupStockManager;

	
	public ClusterAssignmentsCounter() {
		this.numberOfUserGroupCustomer = 0;
		this.numberOfUserGroupStoreManager = 0;
		this.numberOfUserGroupStockManager = 0;
	}
	
	public void increaseNumberOfUserGroupCustomer() {
		this.numberOfUserGroupCustomer++;
	}
	
	public void increaseNumberOfUserGroupStoreManager() {
		this.numberOfUserGroupStoreManager++;
	}
	
	public void increaseNumberOfUserGroupStockManager() {
		this.numberOfUserGroupStockManager++;
	}




	public int getNumberOfUserGroupCustomer() {
		return numberOfUserGroupCustomer;
	}

	public int getNumberOfUserGroupStoreManager() {
		return numberOfUserGroupStoreManager;
	}

	public int getNumberOfUserGroupStockManager() {
		return numberOfUserGroupStockManager;
	}
	
	

}
