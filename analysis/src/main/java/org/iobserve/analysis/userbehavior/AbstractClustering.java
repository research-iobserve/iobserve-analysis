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
package org.iobserve.analysis.userbehavior;

import java.util.List;

import org.iobserve.analysis.userbehavior.data.UserSessionAsCountsOfCalls;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * It creates the instances for the Weka clustering. It is abstract to be usable for different
 * clustering methods.
 *
 * @author David Peter, Robert Heinrich
 */
public abstract class AbstractClustering {

    /**
     * It transforms the user sessions(userSessions in form of counts of their called operation
     * signatures) to Weka instances that can be used for the clustering.
     *
     * @param countModel
     *            contains the userSessions in form of counts of called operation signatures
     * @param listOfDistinctOperationSignatures
     *            contains the extracted distinct operation signatures of the input
     *            entryCallSequenceModel
     * @return the Weka instances that hold the data that is used for the clustering
     */
    protected Instances createInstances(final List<UserSessionAsCountsOfCalls> countModel,
            final int numberOfDistinctOperationSignatures) {

        final FastVector fvWekaAttributes = new FastVector(numberOfDistinctOperationSignatures);

        //O(m)
        for (int i = 0; i < numberOfDistinctOperationSignatures; i++) {
            final String attributeName = "Attribute" + i;
            final Attribute attribute = new Attribute(attributeName);
            fvWekaAttributes.addElement(attribute);
        }

        final Instances clusterSet = new Instances("CallCounts", fvWekaAttributes, countModel.size());

        for (final UserSessionAsCountsOfCalls userSession : countModel) {

        	double[] values = new double[numberOfDistinctOperationSignatures];
        	
        	for (int index = 0; index < numberOfDistinctOperationSignatures; index++) {
        		values[index] = userSession.getAbsoluteCountOfCalls()[index];
        	}
        	
        	final Instance instance = new Instance(1, values);
        	
        	// indesOfAttribute and row are always the same value...
        	// Performance: Calling setValue on an instance creates a copy of the value array on each call!
        	// Definite performance issue with a high number of possible distinct call signatures
        	// Implementation above should be faster
        	
//            int indexOfAttribute = 0;
//            
//            final Instance instance = new Instance(numberOfDistinctOperationSignatures);
//
//            for (int row = 0; row < numberOfDistinctOperationSignatures; row++) {
//                instance.setValue((Attribute) fvWekaAttributes.elementAt(indexOfAttribute),
//                        userSession.getAbsoluteCountOfCalls()[row]);
//                indexOfAttribute++;
//            }

            clusterSet.add(instance);
        }

        return clusterSet;
    }

}
