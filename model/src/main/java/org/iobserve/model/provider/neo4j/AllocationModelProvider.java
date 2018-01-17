/***************************************************************************
 * Copyright (C) 2015 iObserve Project (https://www.iobserve-devops.net)
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

import org.eclipse.emf.ecore.EPackage;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationPackage;

import kieker.common.configuration.Configuration;

/**
 * Model provider to provide {@link Allocation} model.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 * @author Lars Bluemke
 *
 * @deprecated since 0.0.2 this provider was implemented to provide a convenient way to migrate from
 *             the old file based providers.
 */
@Deprecated
public final class AllocationModelProvider extends AbstractModelProvider<Allocation> {

    /**
     * Create model provider to provide {@link Allocation} model.
     *
     * @param neo4jPcmModelDirectory
     *            DB root directory
     */
    public AllocationModelProvider(final Configuration configuration) {
        super(configuration);
    }

    @Override
    public void loadModel() {
        this.model = this.modelProvider.readRootComponent(Allocation.class);

        if (this.model == null) {
            AbstractModelProvider.LOG.debug("Allocation model could not be loaded!");
        }
    }

    /**
     * Reset model.
     */
    @Override
    public void resetModel() {
        final Allocation model = this.getModel();
        model.getAllocationContexts_Allocation().clear();
    }

    @Override
    public EPackage getPackage() {
        return AllocationPackage.eINSTANCE;
    }

    @Override
    protected Graph getModelTypeGraph(final Configuration configuration) {
        return new GraphLoader(configuration).getAllocationModelGraph();
    }
}
