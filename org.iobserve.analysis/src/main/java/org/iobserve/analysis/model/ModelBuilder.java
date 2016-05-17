package org.iobserve.analysis.model;

/**
 * Base class for all model builders. Each model builder will couple of method to add model
 * elements to the given model. Each of this methods should return the model builder itself,
 * hence the caller can chain the build instructions up. The last method to call should be
 * {@link #build()}. It will actually build the model.
 * 
 * @author Robert Heinrich
 * @author Alessandro Giusa
 *
 * @param <T>
 */
public abstract class ModelBuilder<T> {
	
	protected final T model;
	
	/**
	 * Create a new build based on the given model.
	 * @param modelToStartWith model to work on
	 */
	public ModelBuilder(final T modelToStartWith) {
		this.model = modelToStartWith;
	}
	
	/**
	 * Will build the model.
	 * @return new model
	 */
	public abstract T build();
}
