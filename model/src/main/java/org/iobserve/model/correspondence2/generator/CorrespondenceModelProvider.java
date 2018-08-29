package org.iobserve.model.correspondence2.generator;

import java.util.Optional;

import org.eclipse.emf.common.util.URI;

import org.iobserve.model.correspondence2.CorrespondenceModel;

/**
 * Loads a persisted {@link CorrespondenceModel}.
 * 
 * @author Alessandro Giusa, alessandrogiusa@gmail.com
 *
 */
public interface CorrespondenceModelProvider {
	
	/**
	 * @param uri {@link URI} to correspondence model.
	 * @return loaded correspondence model.
	 */
	Optional<CorrespondenceModel> load(URI uri);
}
