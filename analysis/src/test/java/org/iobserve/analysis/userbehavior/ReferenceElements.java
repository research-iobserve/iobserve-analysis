/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.userbehavior;

import org.iobserve.analysis.data.UserSessionCollectionModel;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

/**
 * Contains the reference elements that are created by the ReferenceUsageModelBuilder: Reference
 * model, user sessions, workload. Used for the evaluation of the modeling accuracy
 *
 * @author David Peter, Robert Heinrich
 */
public class ReferenceElements {

    private UserSessionCollectionModel entryCallSequenceModel;
    private UsageModel usageModel;
    private long meanInterArrivalTime;
    private int meanConcurrentUserSessions;

    /**
     * Default entity constructor.
     */
    public ReferenceElements() {
    }

    public UserSessionCollectionModel getEntryCallSequenceModel() {
        return this.entryCallSequenceModel;
    }

    public void setEntryCallSequenceModel(final UserSessionCollectionModel entryCallSequenceModel) {
        this.entryCallSequenceModel = entryCallSequenceModel;
    }

    public UsageModel getUsageModel() {
        return this.usageModel;
    }

    public void setUsageModel(final UsageModel usageModel) {
        this.usageModel = usageModel;
    }

    public long getMeanInterArrivalTime() {
        return this.meanInterArrivalTime;
    }

    public void setMeanInterArrivalTime(final long meanInterArrivalTime) {
        this.meanInterArrivalTime = meanInterArrivalTime;
    }

    public int getMeanConcurrentUserSessions() {
        return this.meanConcurrentUserSessions;
    }

    public void setMeanConcurrentUserSessions(final int meanConcurrentUserSessions) {
        this.meanConcurrentUserSessions = meanConcurrentUserSessions;
    }

}
