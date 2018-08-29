package org.iobserve.model.correspondence2.generator;

import org.eclipse.emf.common.util.URI;

import org.iobserve.model.correspondence2.HighLevelModel;
import org.iobserve.model.correspondence2.LowLevelModel;

/**
 * This interface provides the functionality to configure the
 * {@link CorrespondenceModelBuilder}.
 * 
 * @author Robert Heinrich
 * @author Alessandro Giusa
 *
 */
public interface CorrespondenceModelBuilderConfiguration {

	/**
	 * @return The URI where the generated model should be written.
	 */
	URI getOutput();
	
	/**
	 * @return the URI to the repository of the PCM model istance.
	 */
	URI getRepository();

	/**
	 * @return an empty high-level model instance.
	 */
	HighLevelModel getHighLevelModel();

	/**
	 * @return an empty low-level model instance.
	 */
	LowLevelModel getLowLevelModel();
}
