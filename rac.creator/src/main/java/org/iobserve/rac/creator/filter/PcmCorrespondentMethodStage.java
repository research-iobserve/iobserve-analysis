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
package org.iobserve.rac.creator.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kieker.common.record.flow.trace.operation.AbstractOperationEvent;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.model.protocom.PcmCorrespondentMethod;
import org.iobserve.rac.creator.data.ClassAndMethod;

/**
 *
 * @author Nicolas Boltz
 * @author Reiner Jung
 *
 */
public class PcmCorrespondentMethodStage extends AbstractConsumerStage<AbstractOperationEvent> {

    private final OutputPort<ClassAndMethod> outputPort = this.createOutputPort();

    public PcmCorrespondentMethodStage() {
        // empty default constructor
    }

    public OutputPort<ClassAndMethod> getOutputPort() {
        return this.outputPort;
    }

    @Override
    protected void execute(final AbstractOperationEvent element) throws Exception {
        /** name is in field 6. */
        String classSignature = element.getClassSignature().replaceAll("\\s+", "");
        if (classSignature.contains("$")) {
            classSignature = classSignature.substring(0, classSignature.lastIndexOf('$'));
        }

        this.filterMethodString(classSignature, element.getOperationSignature());
    }

    /**
     * Returns an array in the order: visibilityModifier, returnType, methodName or null if the
     * given string is not a valid method.
     *
     * @param operationSignature
     *            signature of a correspondent, i.e., an operation
     * @return returns an operation object or null
     */
    private void filterMethodString(final String classSignature, final String operationSignature) {
        final PcmCorrespondentMethod method = new PcmCorrespondentMethod();
        /**
         * signature grammar: MODIFIER (' ' MODIFIER)* ' ' TYPE ' ' FQN ' '? '(' parameters ')'
         */
        final String signaturePattern = "^(public|private|static|transient)( (public|private|static|transient))* " // modifiers
                + "([A-Za-z0-9\\.\\[\\]]+) " // return type
                + "([A-Za-z0-9\\.]+) ?" // fqn
                + "\\((.*)\\)$"; // parameters
        final Pattern pattern = Pattern.compile(signaturePattern);
        final Matcher matcher = pattern.matcher(operationSignature);
        if (matcher.find()) {
            int i = 0;
            /** get modifier. */
            do {
                i++;
                if (matcher.group(i).equals("public")) {
                    method.setVisibilityModifier("public");
                } else if (matcher.group(i).equals("private")) {
                    method.setVisibilityModifier("private");
                }
                /** other modifiers are not relevant here. (jump 2 for nested groups) */
                i++;
                if (matcher.groupCount() < i) {
                    return;
                }
            } while (" public".equals(matcher.group(i)) || " private".equals(matcher.group(i))
                    || " static".equals(matcher.group(i)) || "transient".equals(matcher.group(i)));
            /** in case there is only one modifier, the next tokens might be null. */
            while (matcher.group(i) == null) {
                i++;
            }
            /** next must be type. we are actually one step ahead, so we have to go one back. */
            method.setReturnType(matcher.group(i));
            i++;
            /** full qualified name of operation. */
            String methodName = matcher.group(i);
            if (methodName.contains("$")) {
                methodName = methodName.substring(0, methodName.lastIndexOf('$'));
            }
            method.setName(methodName);
            i++;
            /** next token, parameters. */
            method.setParameters(matcher.group(i));

            this.outputPort.send(new ClassAndMethod(classSignature, method));
        }

    }

}
