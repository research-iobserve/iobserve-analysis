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
package org.iobserve.analysis.behavior.models.data.configuration;

import java.util.regex.Pattern;

import org.iobserve.analysis.behavior.models.extended.EntryCallNode;

/**
 * strategy using the last x parts of the operation.
 *
 * @author Christoph Dornieden
 *
 */
public class GetLastXSignatureStrategy implements ISignatureCreationStrategy {
    private final int x;
    private final boolean keepBrackets;
    private final Pattern dotPattern;
    private final Pattern bracketPattern;

    /**
     * constructor.
     *
     * @param x
     *            number of operation parts
     */
    public GetLastXSignatureStrategy(final int x) {
        this(x, false);
    }

    /**
     * constructor.
     *
     * @param x
     *            number of operation parts
     * @param keepBrackets
     *            keepBrackets?
     */
    public GetLastXSignatureStrategy(final int x, final boolean keepBrackets) {
        this.dotPattern = Pattern.compile("\\.");
        this.bracketPattern = Pattern.compile("\\(.*\\)");
        this.keepBrackets = keepBrackets;
        this.x = x < 1 ? 1 : x;
    }

    @Override
    public String getSignature(final EntryCallNode node) {
        final String operationSignature = this.keepBrackets ? node.getSignature()
                : this.bracketPattern.matcher(node.getSignature()).replaceAll("");
        final String[] operationSignatureSplit = this.dotPattern.split(operationSignature);
        final int operationParts = operationSignatureSplit.length;

        if (this.x >= operationParts) {
            return operationSignature;
        } else {
            String partialOperation = operationSignatureSplit[operationParts - 1];
            for (int i = operationParts - this.x; i < operationParts - 1; i++) {
                partialOperation = operationSignatureSplit[i] + "." + partialOperation;
            }
            return partialOperation;
        }

    }

}
