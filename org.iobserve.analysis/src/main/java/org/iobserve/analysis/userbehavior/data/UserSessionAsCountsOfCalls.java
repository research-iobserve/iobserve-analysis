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
 * Represents a user session's call sequence as counts of called operation signatures.
 *
 * @author David Peter, Robert Heinrich
 */
public class UserSessionAsCountsOfCalls {

    private String sessionId;
    private int[] absoluteCountOfCalls;

    /**
     * Constructor for a call sequence for a user session.
     *
     * @param sessionId
     *            the session id
     * @param numberOfDistinctOperationSignatures
     *            number of operation signatures in the sequence.
     */
    public UserSessionAsCountsOfCalls(final String sessionId, final int numberOfDistinctOperationSignatures) {
        this.sessionId = sessionId;
        this.absoluteCountOfCalls = new int[numberOfDistinctOperationSignatures];
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(final String sessionId) {
        this.sessionId = sessionId;
    }

    public int[] getAbsoluteCountOfCalls() {
        return this.absoluteCountOfCalls;
    }

    public void setAbsoluteCountOfCalls(final int[] absoluteCountOfCalls) {
        this.absoluteCountOfCalls = absoluteCountOfCalls;
    }

}
