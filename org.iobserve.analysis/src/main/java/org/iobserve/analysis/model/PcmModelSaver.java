package org.iobserve.analysis.model;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;

/**
 * Provides facilities to save PCM models
 * @author Robert Heinrich, Alessandro Giusa
 * @version 1.0
 *
 */
public final class PcmModelSaver {
	
	/**used output for the model*/
	private final URI output;
	
	/**
	 * Create model saver with using the given output
	 * @param output
	 */
	public PcmModelSaver(final URI output) {
		this.output = output;
	}
	
	/**
	 * Save the model to the output
	 * @param model
	 */
	public void save(final EObject model) {
		AbstractEcoreModelProvider.saveModel(model, this.output);
	}
	
	/**
	 * Get the output of the model
	 * @return URI where model gets written
	 */
	public URI getOutput() {
		return this.output;
	}
}
