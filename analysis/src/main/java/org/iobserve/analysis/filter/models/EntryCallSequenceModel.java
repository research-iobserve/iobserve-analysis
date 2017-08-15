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
package org.iobserve.analysis.filter.models;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.iobserve.analysis.userbehavior.data.WorkloadIntensity;

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
public final class EntryCallSequenceModel {

    /** list of user sessions. */
    private List<UserSession> userSessions;
    private WorkloadIntensity workloadIntensity;
    private double likelihoodOfUserGroup;
    private HashMap<String, UserSession> sessionMap;

    /**
     * Create new model.
     * 
     *  @deprecated
     */
    public EntryCallSequenceModel(final List<UserSession> sessions) {
        this.userSessions = sessions;
        this.sessionMap = new HashMap<>();
        for(UserSession session : this.userSessions) {
            sessionMap.put(session.getSessionId(), session);
        }
    }
    
    /**
     * Create new model.
     */
    public EntryCallSequenceModel() {
        this.userSessions = new LinkedList<>();
        this.sessionMap = new HashMap<>();
    }
    
    public void addOrUpdateSession(UserSession session) {
        if(sessionMap.containsKey(session.getSessionId())) {
            sessionMap.replace(session.getSessionId(), session);
        } else {
            sessionMap.put(session.getSessionId(), session);
        }
        
        this.userSessions = new LinkedList<>(sessionMap.values());
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
