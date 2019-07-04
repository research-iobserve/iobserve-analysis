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
package org.iobserve.analysis.userbehavior.data;

import java.util.Objects;

/**
 * Represents an EntryCallEvent within a BranchModel. The count states the occurrence frequency of
 * the call to calculate the branch likelihoods within a branchModel
 *
 * @author David Peter, Robert Heinrich, Nicolas Boltz
 */
public class CallElement extends ISequenceElement {

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

    /**
    *
    * @return returns the class signature for the element.
    */
    public String getClassSignature() {
        return this.classSignature;
    }
    
    /**
    *
    * @return returns the operation signature for the element.
    */
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
    
    @Override
    public boolean equals(Object o) {
    	if(o instanceof CallElement) {
    		CallElement callElement2 = (CallElement)o;
    		if ((this.getClassSignature().equals(callElement2.getClassSignature()))
                    && (this.getOperationSignature().equals(callElement2.getOperationSignature()))) {
                return true;
            }
        }
    	
    	return false;
    }
    
    @Override
    public int hashCode() {
    	return Objects.hash(this.getClass(), this.getClassSignature(), this.getOperationSignature());
    }

}
