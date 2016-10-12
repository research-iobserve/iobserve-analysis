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
 * Represents an EntryCallEvent within a BranchModel. The count states the occurrence frequency of
 * the call to calculate the branch likelihoods within a branchModel
 *
 * @author David Peter, Robert Heinrich
 */
public class CallElement implements ISequenceElement {

    private final String classSignature;
    private final String operationSignature;
    private int absoluteCount;

    /**
     * Call element constructor.
     *
     * @param classSignature
     *            the class signature of the called operation
     * @param operationSignature
     *            the operation signature of the called operation
     */
    public CallElement(final String classSignature, final String operationSignature) {
        this.classSignature = classSignature;
        this.operationSignature = operationSignature;
    }

    @Override
    public String getClassSignature() {
        return this.classSignature;
    }

    @Override
    public String getOperationSignature() {
        return this.operationSignature;
    }

    @Override
    public int getAbsoluteCount() {
        return this.absoluteCount;
    }

    @Override
    public void setAbsoluteCount(final int absoluteCount) {
        this.absoluteCount = absoluteCount;
    }

}
