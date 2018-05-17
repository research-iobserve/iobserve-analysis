/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.iobserve.model.correspondence.AssemblyEntry;
import org.iobserve.model.correspondence.CorrespondenceFactory;
import org.iobserve.model.correspondence.CorrespondenceModel;
import org.iobserve.model.correspondence.Part;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.system.System;

/**
 * @author Reiner Jung
 *
 */
public final class CreateCorrespondenceMain {

    private static final Map<String, String> NAME_MAPS = new HashMap<>();

    private CreateCorrespondenceMain() {

    }

    /**
     * @param args
     */
    public static void main(final String[] args) {

        CreateCorrespondenceMain.NAME_MAPS.put("Assembly_FrontendService", "JPetStore-Frontend");
        CreateCorrespondenceMain.NAME_MAPS.put("Assembly_AccountService", "JPetStore-Account");
        CreateCorrespondenceMain.NAME_MAPS.put("Assembly_CatalogService", "JPetStore-Catalog");
        CreateCorrespondenceMain.NAME_MAPS.put("Assembly_OdrderService", "JPetStore-Order");
        CreateCorrespondenceMain.NAME_MAPS.put("Assembly_AccountDatabase", "Database-Account");
        CreateCorrespondenceMain.NAME_MAPS.put("Assembly_CatalogDatabase", "Database-Catalog");
        CreateCorrespondenceMain.NAME_MAPS.put("Assembly_OrderDatabase", "Database-Order");

        final String pcmDirectory = "/home/reiner/Projects/iObserve/experiments/distributed-jpetstore-experiment/pcm/JPetStore";
        final PCMModelHandler modelHandler = new PCMModelHandler(new File(pcmDirectory));

        // final Repository repository = modelHandler.getRepositoryModel();
        // final ResourceEnvironment environment = modelHandler.getResourceEnvironmentModel();
        final System system = modelHandler.getSystemModel();
        // final Allocation allocation = modelHandler.getAllocationModel();

        final CorrespondenceModel correspondenceModel = CorrespondenceFactory.eINSTANCE.createCorrespondenceModel();

        final Part part = CorrespondenceFactory.eINSTANCE.createPart();
        correspondenceModel.getParts().add(part);

        part.setModelType(system);
        for (final AssemblyContext assembly : system.getAssemblyContexts__ComposedStructure()) {
            final AssemblyEntry entry = CorrespondenceFactory.eINSTANCE.createAssemblyEntry();
            final String value = CreateCorrespondenceMain.NAME_MAPS.get(assembly.getEntityName());
            entry.setImplementationId(value);
            entry.setAssembly(assembly);
            part.getEntries().add(entry);
        }

        final URI outputURI = URI.createFileURI("/home/reiner/correspondence.rac");
        CreateCorrespondenceMain.save(correspondenceModel, outputURI);
    }

    private static void save(final CorrespondenceModel correspondenceModel, final URI writeModelURI) {
        final Resource.Factory.Registry resourceRegistry = Resource.Factory.Registry.INSTANCE;
        final Map<String, Object> map = resourceRegistry.getExtensionToFactoryMap();
        map.put("*", new XMIResourceFactoryImpl());

        final ResourceSet resourceSet = new ResourceSetImpl();
        resourceSet.setResourceFactoryRegistry(resourceRegistry);

        final Resource resource = resourceSet.createResource(writeModelURI);
        resource.getContents().add(correspondenceModel);
        try {
            resource.save(null);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

}
