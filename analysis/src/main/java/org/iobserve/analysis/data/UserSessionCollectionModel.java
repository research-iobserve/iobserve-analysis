/***************************************************************************
 * Copyright (C) 2014 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.data;

import java.util.ArrayList;
import java.util.List;

import org.iobserve.analysis.behavior.karlsruhe.data.WorkloadIntensity;
import org.iobserve.analysis.session.data.UserSession;

/**
 * Entry Call Sequence Model according to Fig. 7 in paper <i>Run-time Architecture Models for
 * Dynamic Adaptation and Evolution of Cloud Applications</i> Extended to also represent the
 * likelihood of the user group and the workload intensity of the user group the Entry Call Sequence
 * Model represents
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 * @author David Peter
 * @version 1.0
 *
 */
public final class UserSessionCollectionModel {

    /** list of user sessions. */
    private final List<UserSession> userSessions;
    private WorkloadIntensity workloadIntensity;
    private double likelihoodOfUserGroup;

    /**
     * Create new model.
     *
     * @param sessions
     *            sessions.
     */
    public UserSessionCollectionModel(final List<UserSession> sessions) {
        this.userSessions = new ArrayList<UserSession>(sessions);
    }

    /**
     * Constructor to set the user group´s likelihood and its related sessions.
     *
     * @param sessions
     *            sessions
     * @param likelihoodOfUserGroup
     *            probability of user groups
     */
    public UserSessionCollectionModel(final List<UserSession> sessions, final double likelihoodOfUserGroup) {
        this.userSessions = sessions;
        this.likelihoodOfUserGroup = likelihoodOfUserGroup;
    }

    /**
     * Get the user session objects which contain the entry call events.
     *
     * @return list of user sessions
     */
    public List<UserSession> getUserSessions() {
        return this.userSessions;
    }

    public WorkloadIntensity getWorkloadIntensity() {
        return this.workloadIntensity;
    }

    public void setWorkloadIntensity(final WorkloadIntensity workloadIntensity) {
        this.workloadIntensity = workloadIntensity;
    }

    public double getLikelihoodOfUserGroup() {
        return this.likelihoodOfUserGroup;
    }

    public void setLikelihoodOfUserGroup(final double likelihoodOfUserGroup) {
        this.likelihoodOfUserGroup = likelihoodOfUserGroup;
    }

}
