/***************************************************************************
 * Copyright 2016 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.analysis.userbehavior.test;

/**
 * Counts the assignments of user user sessions of each user group to a cluster Used for the
 * clustering evaluation
 *
 * @author David Peter, Robert Heinrich
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
        return this.numberOfUserGroupCustomer;
    }

    public int getNumberOfUserGroupStoreManager() {
        return this.numberOfUserGroupStoreManager;
    }

    public int getNumberOfUserGroupStockManager() {
        return this.numberOfUserGroupStockManager;
    }

}
