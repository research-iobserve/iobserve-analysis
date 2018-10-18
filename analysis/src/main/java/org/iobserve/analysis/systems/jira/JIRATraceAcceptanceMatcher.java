/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.systems.jira;

import org.iobserve.analysis.session.IEntryCallAcceptanceMatcher;
import org.iobserve.stages.general.data.EntryCallEvent;

/**
 * Matches if a given call is valid in context of the user behavior analysis for the JPetStore.
 *
 * @author Reiner Jung
 *
 */
public class JIRATraceAcceptanceMatcher implements IEntryCallAcceptanceMatcher {

    public JIRATraceAcceptanceMatcher() {
        // empty constructor
    }

    @Override
    public boolean match(final EntryCallEvent call) {
        return this.matchClassSignature(call.getClassSignature())
                && this.matchOperationSignature(call.getOperationSignature());
    }

    private boolean matchOperationSignature(final String operationSignature) {
        return true;
    }

    private boolean matchClassSignature(final String classSignature) {
        return true;
    }

}
