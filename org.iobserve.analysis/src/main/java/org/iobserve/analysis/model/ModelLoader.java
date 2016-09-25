package org.iobserve.analysis.model;

import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;

/**
 * Model loader will load a model from the given uri.
 * 
 * @author Robert Heinrich
 * @author Alessandro Giusa
 *
 */
@FunctionalInterface
public interface ModelLoader {

    /**
     * Load the model given by the uri.
     * 
     * @param uri
     *            uri
     * @return model
     */
    Optional<EObject> load(URI uri);

}
