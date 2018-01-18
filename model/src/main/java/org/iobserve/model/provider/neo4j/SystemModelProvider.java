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

import java.io.File;

import org.eclipse.emf.ecore.EPackage;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.system.SystemPackage;

/**
 * Model provider to provide {@link System} model.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 * @author Lars Bluemke
 *
 * @deprecated since 0.0.2 this provider was implemented to provide a convenient way to migrate from
 *             the old file based providers.
 */
@Deprecated
public final class SystemModelProvider extends AbstractModelProvider<System> {

    /**
     * Create model provider to provide {@link System} model.
     *
     * @param neo4jPcmModelDirectory
     *            DB root directory
     */
    public SystemModelProvider(final File baseDirectory) {
        super(baseDirectory);
    }

    @Override
    public void loadModel() {
        this.model = this.modelProvider.readRootComponent(System.class);

        if (this.model == null) {
            AbstractModelProvider.LOG.debug("System model could not be loaded!");
        }
    }

    @Override
    public void resetModel() {
        final org.palladiosimulator.pcm.system.System model = this.getModel();
        model.getAssemblyContexts__ComposedStructure().clear();
    }

    @Override
    protected EPackage getPackage() {
        return SystemPackage.eINSTANCE;
    }

    @Override
    protected Graph getModelTypeGraph(final File baseDirectory) {
        return new GraphLoader(baseDirectory).getSystemModelGraph();
    }

}
