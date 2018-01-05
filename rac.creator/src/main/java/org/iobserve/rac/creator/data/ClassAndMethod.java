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
package org.iobserve.rac.creator.data;

import org.iobserve.analysis.protocom.PcmCorrespondentMethod;

/**
 * We must pass method and class signature in one event. Therefore, this class bundles the class
 * signature and the method together.
 *
 * @author Reiner Jung
 *
 */
public class ClassAndMethod {

    private final PcmCorrespondentMethod method;
    private final String classSignature;

    /**
     * Create a container for class signature and method.
     *
     * @param classSignature
     *            class signature for the method
     * @param method
     *            method object
     */
    public ClassAndMethod(final String classSignature, final PcmCorrespondentMethod method) {
        this.classSignature = classSignature;
        this.method = method;
    }

    public PcmCorrespondentMethod getMethod() {
        return this.method;
    }

    public String getClassSignature() {
        return this.classSignature;
    }

}
