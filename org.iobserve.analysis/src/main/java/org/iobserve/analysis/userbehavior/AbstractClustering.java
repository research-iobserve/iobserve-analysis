package org.iobserve.analysis.userbehavior;


import java.util.List;

import org.iobserve.analysis.userbehavior.data.UserSessionAsTransitionMatrix;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;


/**
 * It creates the instances for the Weka clustering. It is abstract to be usable for different clustering methods.
 * 
 * @author David Peter, Robert Heinrich
 */

public abstract class AbstractClustering {
	
	/**
	 * It transforms the transitionModel(userSessions in form of transition matrices) to Weka instance that can be used 
	 * for the clustering.
	 * 
	 * @param transitionModel contains the userSessions in form of transition matrices
	 * @param listOfDistinctOperationSignatures are the extracted distinct operation signatures of the input entryCallSequenceModel
	 * @return the Weka instances that hold the data that is used for the clustering
	 */
	protected Instances createInstances(List<UserSessionAsTransitionMatrix> absoluteTransitionModel, List<String> listOfCalledMethods) {
		
		int numberOfDistinctOperationSignatures = listOfCalledMethods.size();
		FastVector fvWekaAttributes = new FastVector(numberOfDistinctOperationSignatures);
		
		for(int i=0;i<numberOfDistinctOperationSignatures;i++) {
			String attributeName = "Attribute"+i;
			Attribute attribute = new Attribute(attributeName);
			fvWekaAttributes.addElement(attribute);
		}
		
		Instances clusterSet = new Instances("CallCounts", fvWekaAttributes, absoluteTransitionModel.size());
		
		for(final UserSessionAsTransitionMatrix userSession:absoluteTransitionModel) {
			
			int indexOfAttribute = 0;
			Instance instance = new Instance(numberOfDistinctOperationSignatures);
			
			for(int row=0;row<listOfCalledMethods.size();row++) {
					
				instance.setValue((Attribute)fvWekaAttributes.elementAt(indexOfAttribute), userSession.getAbsoluteCountOfCalls()[row]);
				indexOfAttribute++;	
				
			}
			
			clusterSet.add(instance);
		}
		
		return clusterSet;
	}
	
	
}
