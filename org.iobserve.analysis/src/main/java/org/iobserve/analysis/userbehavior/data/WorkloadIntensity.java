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
package org.iobserve.analysis.userbehavior.data;

/**
 * Contains the workload intensity values for a user group. The interarrivalTimeOfUserSessions is
 * used for an open workload and the avgNumberOfConcurrentUsers is used for a closed workload
 *
 * @author David Peter, Robert Heinrich
 */
public class WorkloadIntensity {

    private long interarrivalTimeOfUserSessions;
    private int maxNumberOfConcurrentUsers;
    private int avgNumberOfConcurrentUsers;

    public long getInterarrivalTimeOfUserSessions() {
        return this.interarrivalTimeOfUserSessions;
    }

    public void setInterarrivalTimeOfUserSessions(final long interarrivalTimeOfUserSessions) {
        this.interarrivalTimeOfUserSessions = interarrivalTimeOfUserSessions;
    }

    public int getMaxNumberOfConcurrentUsers() {
        return this.maxNumberOfConcurrentUsers;
    }

    public void setMaxNumberOfConcurrentUsers(final int maxNumberOfConcurrentUsers) {
        this.maxNumberOfConcurrentUsers = maxNumberOfConcurrentUsers;
    }

    public int getAvgNumberOfConcurrentUsers() {
        return this.avgNumberOfConcurrentUsers;
    }

    public void setAvgNumberOfConcurrentUsers(final int avgNumberOfConcurrentUsers) {
        this.avgNumberOfConcurrentUsers = avgNumberOfConcurrentUsers;
    }

}
