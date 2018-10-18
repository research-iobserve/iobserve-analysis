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

import org.iobserve.analysis.traces.ITraceSignatureCleanupRewriter;

/**
 * Performs cleanup operations on class and operation signatures for the JPetStore application.
 *
 * @author Reiner Jung
 *
 */
public class JIRASignatureCleanupRewriter implements ITraceSignatureCleanupRewriter {

    public JIRASignatureCleanupRewriter() {
        // empty constructor
    }

    @Override
    public String rewriteClassSignature(final String classSignature) {
        return classSignature;
    }

    @Override
    public String rewriteOperationSignature(final String operationSignature) {
        return operationSignature;
    }

}
