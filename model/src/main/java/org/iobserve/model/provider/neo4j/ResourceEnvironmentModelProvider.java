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
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;

import kieker.common.configuration.Configuration;

/**
 * Model provider to provide {@link ResourceEnvironment} model.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 * @author Lars Bluemke
 *
 * @deprecated since 0.0.2 this provider was implemented to provide a convenient way to migrate from
 *             the old file based providers.
 *
 */
@Deprecated
public final class ResourceEnvironmentModelProvider extends AbstractModelProvider<ResourceEnvironment> {

    /**
     * Create model provider to provide {@link ResourceEnvironment} model.
     *
     * @param neo4jPcmModelDirectory
     *            DB root directory
     */
    public ResourceEnvironmentModelProvider(final Configuration configuration) {
        super(configuration);
    }

    @Override
    public void loadModel() {
        this.model = this.modelProvider.readRootComponent(ResourceEnvironment.class);

        if (this.model == null) {
            AbstractModelProvider.LOG.debug("Resource environment could not be loaded!");
        }
    }

    @Override
    public void resetModel() {
        final ResourceEnvironment model = this.getModel();
        model.getResourceContainer_ResourceEnvironment().clear();
        model.getLinkingResources__ResourceEnvironment().clear();
    }

    @Override
    protected EPackage getPackage() {
        return ResourceenvironmentPackage.eINSTANCE;
    }

    @Override
    protected Graph getModelTypeGraph(final Configuration configuration) {
        return new GraphLoader(configuration).getResourceEnvironmentModelGraph();
    }

}
