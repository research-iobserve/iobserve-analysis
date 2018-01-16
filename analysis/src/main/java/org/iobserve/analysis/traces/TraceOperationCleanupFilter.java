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
package org.iobserve.analysis.traces;

import org.iobserve.analysis.data.UserSession;
import org.iobserve.stages.general.data.EntryCallEvent;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * Cleanup filter to rewrite class and operation signatures.
 *
 * @author Reiner Jung
 *
 */
public class TraceOperationCleanupFilter extends AbstractConsumerStage<UserSession> {

    private final ITraceSignatureCleanupRewriter rewriter;
    private final OutputPort<UserSession> outputPort = this.createOutputPort();

    /**
     * Create the cleanup.
     *
     * @param rewriter
     *            rewrite rule class.
     */
    public TraceOperationCleanupFilter(final ITraceSignatureCleanupRewriter rewriter) {
        this.rewriter = rewriter;
    }

    @Override
    protected void execute(final UserSession session) throws Exception {
        for (final EntryCallEvent event : session.getEvents()) {
            event.setClassSignature(this.rewriter.rewriteClassSignature(event.getClassSignature()));
            event.setClassSignature(this.rewriter.rewriteOperationSignature(event.getOperationSignature()));

        }
    }

    public OutputPort<UserSession> getOutputPort() {
        return this.outputPort;
    }

}
