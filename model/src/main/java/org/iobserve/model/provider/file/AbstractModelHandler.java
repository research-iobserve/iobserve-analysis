/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.model.provider.file;

import java.io.IOException;
import java.util.Map;

import de.uka.ipd.sdq.identifier.Identifier;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The model handler allows to load and store in memory models in XMI files.
 *
 * @author Philipp Weimann
 * @author Tobias Poeppke
 * @author Robert Heinrich
 * @author Alessandro Giusa
 * @author Reiner Jung
 *
 * @param <T>
 *            root class of metamodel
 *
 */
public abstract class AbstractModelHandler<T extends EObject> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractModelHandler.class);

    private final Resource.Factory.Registry resourceRegistry;

    private final ResourceSet resourceSet;

    /**
     * Create an abstract model handler.
     *
     * @param resourceSet
     *            resource set for all models
     */
    public AbstractModelHandler(final ResourceSet resourceSet) {
        this.resourceSet = resourceSet;
        this.resourceRegistry = Resource.Factory.Registry.INSTANCE;
        final Map<String, Object> map = this.resourceRegistry.getExtensionToFactoryMap();
        map.put("*", new XMIResourceFactoryImpl());

        this.resourceSet.setResourceFactoryRegistry(this.resourceRegistry);

    }

    /**
     * Save the internal model. This will override the existing.
     *
     * @param writeModelURI
     *            URI referring where to store the model
     * @param model
     *            is the model to be stored
     */
    public final void save(final URI writeModelURI, final T model) {
        this.resourceSet.setResourceFactoryRegistry(this.resourceRegistry);

        final Resource resource = this.resourceSet.createResource(writeModelURI.appendFileExtension(this.getSuffix()));
        resource.getContents().add(model);
        try {
            resource.save(null);
        } catch (final IOException e) {
            AbstractModelHandler.LOGGER.error("Cannot save model at {}", writeModelURI.toString());
        }
    }

    /**
     * Return the suffix associated with a specific type of model.
     *
     * @return the suffix
     */
    protected abstract String getSuffix();

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
     * Load the model.
     *
     * @param readModelURI
     *            the uri from which the model is read
     * @return returns the loaded model
     */
    @SuppressWarnings("unchecked")
    public T load(final URI readModelURI) {
        this.getPackage().eClass();

        final Resource resource = this.resourceSet.getResource(readModelURI, true);
        try {
            resource.load(this.resourceSet.getLoadOptions());
        } catch (final IOException e) {
            AbstractModelHandler.LOGGER.error("Cannot load model from {}", readModelURI.toString());
        }
        EcoreUtil.resolveAll(this.resourceSet);
        T model = null;
        if (!resource.getContents().isEmpty()) {
            model = (T) resource.getContents().get(0);
        }

        if (model == null) {
            AbstractModelHandler.LOGGER.debug("Model at {} could not be loaded!", readModelURI.toString());
        }

        return model;
    }

    /**
     * @param id
     *            id
     * @param list
     *            where to search
     * @return identifier or null if no identifier with the given id could be found.
     */
    public static Identifier getIdentifiableComponent(final String id, final EList<? extends Identifier> list) {
        for (final Identifier next : list) {
            if (next.getId().equals(id)) {
                return next;
            }
        }
        return null;
    }

}
