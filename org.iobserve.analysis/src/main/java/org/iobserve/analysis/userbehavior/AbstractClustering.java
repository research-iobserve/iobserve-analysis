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
	protected Instances createInstances(List<UserSessionAsTransitionMatrix> transitionModel, List<String> listOfDistinctOperationSignatures) {
		
		int numberOfMatrixElements = listOfDistinctOperationSignatures.size()*listOfDistinctOperationSignatures.size();
		FastVector fvWekaAttributes = new FastVector(numberOfMatrixElements);
		
		for(int i=0;i<numberOfMatrixElements;i++) {
			String attributeName = "Attribute"+i;
			Attribute attribute = new Attribute(attributeName);
			fvWekaAttributes.addElement(attribute);
		}
		
		Instances clusterSet = new Instances("TransitionCounts", fvWekaAttributes, transitionModel.size());
		
		for(final UserSessionAsTransitionMatrix userSession:transitionModel) {
			
			int indexOfAttribute = 0;
			Instance instance = new Instance(numberOfMatrixElements);
			
			for(int row=0;row<listOfDistinctOperationSignatures.size();row++) {
				for(int column=0;column<listOfDistinctOperationSignatures.size();column++) {
					
					instance.setValue((Attribute)fvWekaAttributes.elementAt(indexOfAttribute), userSession.getAbsoluteTransitionMatrix()[row][column]);
					indexOfAttribute++;
					
				}
			}
			
			clusterSet.add(instance);
		}
			
		return clusterSet;
	}
	
	
}
