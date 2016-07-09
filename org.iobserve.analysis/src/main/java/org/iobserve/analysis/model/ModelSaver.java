package org.iobserve.analysis.model;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;

/**
 * Model saver is able to save the given model at the given uri.
 * @author Robert Heinrich
 * @author Alessandro Giusa
 */
@FunctionalInterface
public interface ModelSaver {
	
	/**
	 * Save the given model at the given url
	 * @param eObj object
	 * @param uri uri
	 */
	public void save(EObject eObj, URI uri);

}
