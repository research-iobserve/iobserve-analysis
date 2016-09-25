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
import java.util.Optional;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.iobserve.analysis.utils.Opt;

import de.uka.ipd.sdq.identifier.Identifier;

/**
 * Base class for pcm model provider. Implements common methods for loading/saving pcm model.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 * @param <T>
 */
abstract class AbstractModelProvider<T extends EObject> {

    /** uri to the pcm model. */
    private final URI uriModelInstance;
    /** parent where this provider belongs to. */
    private final ModelProviderPlatform platform;
    /** save strategy of model. */
    private ModelSaveStrategy saveStrategy = ModelSaveStrategy.OVERRIDE;
    /** model saver */
    private final ModelSaver modelSaver;
    /** model loader */
    private final ModelLoader modelLoader;

    /** the model instance. */
    private T model;

    /**
     * Create a model provider for the given. Uses {@link #MODEL_FILE_LOADER} as loader and
     * {@link #MODEL_FILE_SAVER} as saver.
     * 
     * @param thePlatform
     *            platform
     * @param theUriModelInstance
     *            uri to the model
     */
    AbstractModelProvider(final ModelProviderPlatform thePlatform, final URI theUriModelInstance) {
        this(thePlatform, theUriModelInstance, MODEL_FILE_LOADER, MODEL_FILE_SAVER);
    }

    /**
     * Create a model provider for the given .
     * 
     * @param thePlatform
     *            platform
     * @param theUriModelInstance
     *            uri to the model
     * @param loader
     *            model loader
     * @param saver
     *            model saver
     * 
     */
    AbstractModelProvider(final ModelProviderPlatform thePlatform, final URI theUriModelInstance,
            final ModelLoader loader, final ModelSaver saver) {
        this.uriModelInstance = theUriModelInstance;
        this.platform = thePlatform;

        this.modelSaver = saver;
        this.modelLoader = loader;

        // perhaps this should be called client?
        this.loadModel();
    }

    /**
     * Set the save strategy which is used to save the model, when {@link #save()} is called.
     * 
     * @param theSaveStrategy
     *            save strategy
     */
    public void setSaveStrategy(final ModelSaveStrategy theSaveStrategy) {
        this.saveStrategy = theSaveStrategy;
    }

    /**
     * Save the internal model. This will override the existing.
     * 
     * @param strategy
     *            strategy how to save the model. Default {@link ModelSaveStrategy#OVERRIDE}
     */
    public final void save(final ModelSaveStrategy strategy) {
        switch (strategy) {
        case OVERRIDE:
            this.modelSaver.save(this.model, this.uriModelInstance);
            break;
        case MERGE:
            throw new UnsupportedOperationException(
                    String.format("%s save strategy does not exist yet!", ModelSaveStrategy.MERGE.name()));
        default:
            this.modelSaver.save(this.model, this.uriModelInstance);
            break;
        }
    }

    /**
     * Save the model with the defined {@link ModelSaveStrategy} by
     * {@link #setSaveStrategy(ModelSaveStrategy)}. default = {@link ModelSaveStrategy#OVERRIDE}.
     */
    public final void save() {
        this.save(this.saveStrategy);
    }

    /**
     * Get an instance of the package where this model belongs to. <br>
     * <br>
     * For instance:
     *
     * <pre>
     * public EPackage getPackage() {
     *     return AllocationPackage.eINSTANCE;
     * }
     * </pre>
     * 
     * @return return the package of this model
     */
    protected abstract EPackage getPackage();

    /**
     * Load the model from {@link #uriModelInstance}.
     */
    public final void loadModel() {
        this.getPackage().eClass();
        Opt.of(this.modelLoader.load(this.uriModelInstance)).ifPresent().apply(this::setModel).elseApply(
                () -> System.out.printf("Model at %s could not be loaded!\n", this.uriModelInstance.toString()));
    }

    /**
     * Get the platform.
     * 
     * @return the platform
     */
    public ModelProviderPlatform getPlatform() {
        return this.platform;
    }

    /**
     * Get the loaded model.
     * 
     * @return model
     */
    public T getModel() {
        return this.model;
    }

    /**
     * Set the model.
     * 
     * @param obj
     *            model
     */
    @SuppressWarnings("unchecked")
    private void setModel(final EObject obj) {
        this.model = (T) obj;
    }

    /**
     * Get the uri to the model.
     * 
     * @return uri to model
     */
    public URI getModelUri() {
        return this.uriModelInstance;
    }

    /**
     * Get the loaded model.
     * 
     * @param reload
     *            if true, it will reloaded the model before return.
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
     * @return identifier or null if no identifier with the given id could be found.
     */
    static Identifier getIdentifiableComponent(final String id, final EList<? extends Identifier> list) {
        for (final Identifier next : list) {
            if (next.getId().equals(id)) {
                return next;
            }
        }
        return null;
    }

    /**
     * Model loader which saves models to file
     */
    public static final ModelSaver MODEL_FILE_SAVER = (obj, uri) -> {
        final Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        final Map<String, Object> map = reg.getExtensionToFactoryMap();
        map.put("*", new XMIResourceFactoryImpl());

        final ResourceSet resSet = new ResourceSetImpl();
        resSet.setResourceFactoryRegistry(reg);

        final Resource res = resSet.createResource(uri);
        res.getContents().add(obj);
        try {
            res.save(null);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    };

    /**
     * Model loader which loads models from file
     */
    public static final ModelLoader MODEL_FILE_LOADER = uri -> {
        final Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        final Map<String, Object> map = reg.getExtensionToFactoryMap();
        map.put("*", new XMIResourceFactoryImpl());

        final ResourceSet resSet = new ResourceSetImpl();
        resSet.setResourceFactoryRegistry(reg);

        final Resource resource = resSet.getResource(uri, true);
        try {
            resource.load(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        EcoreUtil.resolveAll(resSet);
        EObject model = null;
        if (!resource.getContents().isEmpty()) {
            model = resource.getContents().get(0);
        }
        return Optional.ofNullable(model);
    };
}
