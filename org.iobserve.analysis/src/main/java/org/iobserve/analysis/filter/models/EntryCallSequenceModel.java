/***************************************************************************
 * Copyright 2014 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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

import java.util.List;

import org.iobserve.analysis.userbehavior.data.WorkloadIntensity;

/**
 * Entry Call Sequence Model according to Fig. 7 in paper <i>Run-time Architecture Models for
 * Dynamic Adaptation and Evolution of Cloud Applications</i> Extended to also represent the
 * likelihood of the user group and the workload intensity of the user group the Entry Call Sequence
 * Model represents
 *
 * @author Robert Heinrich, Alessandro Giusa, David Peter
 * @version 1.0
 *
 */
public class EntryCallSequenceModel {

    private final List<UserSession> userSessions;
    private WorkloadIntensity workloadIntensity;
    private double likelihoodOfUserGroup;

    /**
     * Simple constructor set the list
     *
     * @param sessions
     */
    public EntryCallSequenceModel(final List<UserSession> sessions) {
        this.userSessions = sessions;
    }

    /**
     * Constructor to set the user group´s likelihood and its related sessions
     *
     * @param sessions,
     *            likelihood
     */
    public EntryCallSequenceModel(final List<UserSession> sessions, final double likelihoodOfUserGroup) {
        this.userSessions = sessions;
        this.likelihoodOfUserGroup = likelihoodOfUserGroup;
    }

    /**
     * Get the user session objects which contain the entry call events
     *
     * @return
     */
    public List<UserSession> getUserSessions() {
        return this.userSessions;
    }

    /**
     * Get the workload intensity of the user group the enryCallSequenceModel represents
     *
     * @return
     */
    public WorkloadIntensity getWorkloadIntensity() {
        return this.workloadIntensity;
    }

    /**
     * Set the workload intensity of the user group the enryCallSequenceModel represents
     *
     * @return
     */
    public void setWorkloadIntensity(final WorkloadIntensity workloadIntensity) {
        this.workloadIntensity = workloadIntensity;
    }

    /**
     * Get the likelihood of the user group the enryCallSequenceModel represents
     *
     * @return
     */
    public double getLikelihoodOfUserGroup() {
        return this.likelihoodOfUserGroup;
    }

    /**
     * Set the likelihood of the user group the enryCallSequenceModel represents
     *
     * @return
     */
    public void setLikelihoodOfUserGroup(final double likelihoodOfUserGroup) {
        this.likelihoodOfUserGroup = likelihoodOfUserGroup;
    }

}
