package org.iobserve.model.correspondence2.generator;

import org.iobserve.model.correspondence2.CorrespondenceModel;
import org.iobserve.model.correspondence2.HighLevelModelElement;
import org.iobserve.model.correspondence2.LowLevelModelElement;

/**
 * The {@link CorrespondenceModelBuilder} is the component which builds the
 * {@link CorrespondenceModel}. It is intended to be called from your code
 * generator whenever a new correspondence should be created.
 * 
 * @author Robert Heinrich
 * @author Alessandro Giusa
 *
 */
public interface CorrespondenceModelBuilder {

	/**
	 * Create a correspondence between the given high-level- and low-level model
	 * element. The {@link CorrespondenceModelBuilder} will incrementally
	 * output the current {@link CorrespondenceModel}. This means each call to
	 * this method will cause an output.
	 * 
	 * @param highLevelElement
	 *            high-level-model element
	 * @param lowLevelElement
	 *            low-level model element
	 */
	void createCorrespondence(HighLevelModelElement highLevelElement, LowLevelModelElement lowLevelElement);
	
	/**
	 * Clears the builder. Which means it will clear the model it is building.
	 */
	void clear(); 
}
