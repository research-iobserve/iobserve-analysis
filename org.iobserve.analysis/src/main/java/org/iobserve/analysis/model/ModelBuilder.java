/***************************************************************************
 * Copyright 2014 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.analysis.model;

import org.eclipse.emf.ecore.EObject;

/**
 * Base class for all model builders. Each model builder will couple of method to add model elements
 * to the given model. Each of this methods should return the model builder itself, hence the caller
 * can chain the build instructions up. The last method to call should be {@link #build()}. It will
 * actually build the model.
 * 
 * @author Robert Heinrich
 * @author Alessandro Giusa
 * @param <T>
 *            type of model builder
 * @param <E>
 *
 */
public abstract class ModelBuilder<E extends AbstractModelProvider<T>, T extends EObject> {

    /** model provider. */
    protected final E modelProvider;

    /**
     * Create a new build based on the given model.
     * 
     * @param modelToStartWith
     *            model to work on
     */
    public ModelBuilder(final E modelToStartWith) {
        this.modelProvider = modelToStartWith;
    }

    /**
     * Save the model using the model provider.
     * 
     * @see AbstractModelProvider#save()
     */
    public void build() {
        this.modelProvider.save();
    }

    /**
     * Get the model from the builder using the model provider.
     * 
     * @return the model
     */
    public T getModel() {
        return this.modelProvider.getModel();
    }
}
