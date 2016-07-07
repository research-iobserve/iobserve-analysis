/***************************************************************************
 * Copyright 2015 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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

import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import de.uka.ipd.sdq.identifier.Identifier;

/**
 * Base class for pcm model provider.
 * Implements common methods for loading/saving pcm model.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 * @param <T>
 */
abstract class AbstractModelProvider<T extends EObject> {

	/**uri to the pcm model.*/
	private final URI uriModelInstance;
	/**parent where this provider belongs to.*/
	private final ModelProviderPlatform platform;
	/**save strategy of model.*/
	private ModelSaveStrategy saveStrategy = ModelSaveStrategy.OVERRIDE;

	/**the model instance.*/
	private T model;

	/**
	 * Create a model provider for the given .
	 * @param thePlatform platform
	 * @param theUriModelInstance uri to the model
	 */
	AbstractModelProvider(final ModelProviderPlatform thePlatform,
			final URI theUriModelInstance) {
		this.uriModelInstance = theUriModelInstance;
		this.platform = thePlatform;

		// perhaps this should be called client?
		this.loadModel();
	}
	
	/**
	 * Set the save strategy which is used to save the model,
	 *  when {@link #save()} is called.
	 * @param theSaveStrategy save strategy
	 */
	public void setSaveStrategy(final ModelSaveStrategy theSaveStrategy) {
		this.saveStrategy = theSaveStrategy;
	}
	
	/**
	 * Save the internal model. This will override the existing.
	 * @param strategy strategy how to save the model.
	 *  Default {@link ModelSaveStrategy#OVERRIDE}
	 */
	public void save(final ModelSaveStrategy strategy) {
		switch (strategy) {
		case OVERRIDE:
			new PcmModelSaver(this.uriModelInstance).save(this.getModel());
			break;
		case MERGE:
			throw new UnsupportedOperationException(
					String.format("%s save strategy does not exist yet!",
							ModelSaveStrategy.MERGE.name()));
		default:
			new PcmModelSaver(this.uriModelInstance).save(this.getModel());
			break;
		}
	}
	
	/**
	 * Save the model with the defined {@link ModelSaveStrategy} 
	 * by {@link #setSaveStrategy(ModelSaveStrategy)}.
	 * default = {@link ModelSaveStrategy#OVERRIDE}.
	 */
	public void save() {
		this.save(this.saveStrategy);
	}

	/**
	 * Get an instance of the package where this model belongs to. <br>
	 * <br>
	 * For instance:
	 *
	 * <pre>
	 * public EPackage getPackage() {
	 * 	return AllocationPackage.eINSTANCE;
	 * }
	 * </pre>
	 * @return return the package of this model
	 */
	public abstract EPackage getPackage();

	/**
	 * Load the model from {@link #uriModelInstance}.
	 */
	@SuppressWarnings("unchecked")
	protected void loadModel() {
		this.getPackage().eClass();

		final Resource.Factory.Registry reg =
				Resource.Factory.Registry.INSTANCE;
		final Map<String, Object> map = reg.getExtensionToFactoryMap();
		map.put("*", new XMIResourceFactoryImpl());

		final ResourceSet resSet = new ResourceSetImpl();
		resSet.setResourceFactoryRegistry(reg);

		final Resource resource = resSet
				.getResource(this.uriModelInstance, true);
		try {
			resource.load(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		EcoreUtil.resolveAll(resSet);
		
		if (!resource.getContents().isEmpty()) {
			this.model = (T) resource.getContents().get(0);
		} else {
			System.err.printf("%s model was empty!"
					+ " Could not load anything!", this.getClass().getName());
		}
	}
	
	/**
	 * Get the platform.
	 * @return the platform
	 */
	public ModelProviderPlatform getPlatform() {
		return this.platform;
	}

	// ********************************************************************
	// * GETTER / SETTER
	// ********************************************************************

	/**
	 * Get the loaded model.
	 * @return model
	 */
	public T getModel() {
		return this.model;
	}
	
	/**
	 * Get the uri to the model.
	 * @return uri to model
	 */
	public URI getModelUri() {
		return this.uriModelInstance;
	}
	
	/**
	 * Get the loaded model.
	 * @param reload if true, it will reloaded the model before return.
	 * @return the model
	 */
	public T getModel(final boolean reload) {
		if (reload) {
			this.loadModel();
		}
		return this.getModel();
	}

	/**
	 * @param id
	 *            id
	 * @param list
	 *            where to search
	 * @return identifier or null if no identifier with the given id could be
	 *         found.
	 */
	static Identifier getIdentifiableComponent(final String id,
			final EList<? extends Identifier> list) {
		for (final Identifier next : list) {
			if (next.getId().equals(id)) {
				return next;
			}
		}
		return null;
	}
}
