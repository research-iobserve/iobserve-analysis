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

import java.util.List;

/**
 * Interface that state the elements of a call sequence.
 *
 * @author David Peter, Robert Heinrich, Nicolas Boltz
 */
public abstract class ISequenceElement {

    /**
     * @return returns the count
     */
    abstract public int getAbsoluteCount();

    /**
     * @param absoluteCount
     *            sets the count.
     */
    abstract public void setAbsoluteCount(int absoluteCount);
    
    public static boolean doSequencesMatch(List<ISequenceElement> sequence1, List<ISequenceElement> sequence2) {
    	if(sequence1.size() != sequence2.size()) {
    		return false;
    	}
    	
    	for(int i = 0; i < sequence1.size(); i++) {
    		if(!sequence1.get(i).equals(sequence2.get(i))) {
    			return false;
    		}
    	}
    	
    	return true;
    }
}
