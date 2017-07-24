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
package org.iobserve.analysis.modelneo4j.legacyprovider;

import java.io.File;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.iobserve.analysis.model.ModelSaveStrategy;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import de.uka.ipd.sdq.identifier.Identifier;

/**
 * Base class for pcm model provider. Implements common methods for loading/saving pcm model.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 * @author Lars Bluemke
 * @param <T>
 */
public abstract class AbstractModelProvider<T extends EObject> {

    /** database location. */
    protected File dirModelDb;
    /** generic model provider. */
    protected final ModelProvider<T> modelProvider;
    /** save strategy of model. */
    private ModelSaveStrategy saveStrategy = ModelSaveStrategy.OVERRIDE;
    /** the model instance. */
    protected T model;

    /**
     * Create a model provider for the given.
     *
     * @param dirModelDb
     *            DB directory
     */
    AbstractModelProvider(final File dirModelDb) {
        final GraphDatabaseService graph = new GraphDatabaseFactory().newEmbeddedDatabase(dirModelDb);
        this.registerShutdownHook(graph);
        this.dirModelDb = dirModelDb;
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
     * Registers a shutdown hook for the Neo4j instance so that it shuts down when the VM exits
     * (even if you "Ctrl-C" the running application).
     *
     * @param graphDb
     *            The graph database service
     */
    private void registerShutdownHook(final GraphDatabaseService graphDb) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }

}
