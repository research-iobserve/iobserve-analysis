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
package org.iobserve.model.provider.neo4j;

import java.io.File;

import de.uka.ipd.sdq.identifier.Identifier;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.iobserve.model.ModelSaveStrategy;

/**
 * Base class for pcm model provider. Implements common methods for loading/saving pcm model.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 * @author Lars Bluemke
 * @param <T>
 *
 * @deprecated since 0.0.2 this provider was implemented to provide a convenient way to migrate from
 *             the old file based providers.
 */
@Deprecated
public abstract class AbstractModelProvider<T extends EObject> {

    /** database location. */
    protected File neo4jPcmModelDirectory;
    /** generic model provider. */
    protected final ModelProvider<T> modelProvider;
    /** save strategy of model. */
    private ModelSaveStrategy saveStrategy = ModelSaveStrategy.OVERRIDE;
    /** the model instance. */
    protected T model;

    protected static final Logger LOG = LogManager.getLogger(AbstractModelProvider.class);

    /**
     * Create a model provider for the given.
     *
     * @param neo4jPcmModelDirectory
     *            DB root directory
     */
    public AbstractModelProvider(final File neo4jPcmModelDirectory) {
        final Graph graph = this.getModelTypeGraph(neo4jPcmModelDirectory);
        this.neo4jPcmModelDirectory = neo4jPcmModelDirectory;
        this.modelProvider = new ModelProvider<>(graph);
        this.loadModel();
    }

    /**
     * Set the save strategy which is used to save the model, when {@link #save()} is called.
     *
     * @param saveStrategy
     *            save strategy
     */
    public void setSaveStrategy(final ModelSaveStrategy saveStrategy) {
        this.saveStrategy = saveStrategy;
    }

    /**
     * Save the internal model. This will override the existing.
     */
    public final void save() {
        switch (this.saveStrategy) {
        case OVERRIDE:
            this.overrideModel();
            break;
        case MERGE:
            throw new UnsupportedOperationException(
                    String.format("%s save strategy does not exist yet!", ModelSaveStrategy.MERGE.name()));
        default:
            this.overrideModel();
            break;
        }
    }

    /**
     * Override existing model content.
     */
    private void overrideModel() {
        this.modelProvider.clearGraph();
        this.modelProvider.createComponent(this.model);
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
     * Load the model from the graph database.
     */
    public abstract void loadModel();

    /**
     * Clears model content.
     */
    public abstract void resetModel();

    /**
     * Get the loaded model.
     *
     * @return model
     */
    public T getModel() {
        return this.model;
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
    public static Identifier getIdentifiableComponent(final String id, final EList<? extends Identifier> list) {
        for (final Identifier next : list) {
            if (next.getId().equals(id)) {
                return next;
            }
        }
        return null;
    }

    /**
     * Returns a the graph for the provider's type.
     *
     * @param neo4jPcmModelDirectory
     *            DB root directory
     * @return DB Graph
     */
    protected abstract Graph getModelTypeGraph(File neo4jPcmModelDirectory);

}
