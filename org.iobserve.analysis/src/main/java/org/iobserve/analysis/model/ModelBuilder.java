package org.iobserve.analysis.model;

import org.eclipse.emf.ecore.EObject;

/**
 * Base class for all model builders. Each model builder will couple of method to add model
 * elements to the given model. Each of this methods should return the model builder itself,
 * hence the caller can chain the build instructions up. The last method to call should be
 * {@link #build()}. It will actually build the model.
 * 
 * @author Robert Heinrich
 * @author Alessandro Giusa
 * @param <T>
 *
 * @param <T>
 */
public abstract class ModelBuilder<E extends AbstractModelProvider<T>, T extends EObject> {
	
	protected final E modelProvider;
	
	/**
	 * Create a new build based on the given model.
	 * @param modelToStartWith model to work on
	 */
	public ModelBuilder(final E modelToStartWith) {
		this.modelProvider = modelToStartWith;
	}
	
	/**
	 * Will build the model.
	 * @return new model
	 */
	public abstract T build();
}
