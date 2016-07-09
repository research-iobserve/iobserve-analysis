package org.iobserve.analysis.model;

import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;

@FunctionalInterface
public interface ModelLoader {
	
	/**
	 * Load the model given by the uri.
	 * @param uri
	 * @return model
	 */
	Optional<EObject> load(URI uri);

}
