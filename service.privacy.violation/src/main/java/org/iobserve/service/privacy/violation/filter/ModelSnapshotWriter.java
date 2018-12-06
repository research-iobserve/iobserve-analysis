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
package org.iobserve.service.privacy.violation.filter;

import java.io.File;
import java.util.Map;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory.Registry;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.model.correspondence.CorrespondenceModel;
import org.iobserve.model.correspondence.CorrespondencePackage;
import org.iobserve.model.persistence.neo4j.DBException;
import org.iobserve.model.persistence.neo4j.ModelNeo4JUtil;
import org.iobserve.model.persistence.neo4j.ModelResource;
import org.iobserve.model.privacy.PrivacyModel;
import org.iobserve.model.privacy.PrivacyPackage;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationPackage;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryPackage;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.system.SystemPackage;

/**
 * Dumps the complete database with all registered models. Each time it is triggered a separate set
 * of models are written in a separate directory.
 *
 * @author Reiner Jung
 *
 */
public class ModelSnapshotWriter extends AbstractConsumerStage<PCMDeployedEvent> {

    private final ModelResource<CorrespondenceModel> correspondenceResource;
    private final ModelResource<Repository> repositoryResource;
    private final ModelResource<ResourceEnvironment> resourceEnvironmentResource;
    private final ModelResource<System> assemblyResource;
    private final ModelResource<Allocation> allocationResource;
    private final ModelResource<PrivacyModel> privacyModelResource;

    private final File modelDumpDirectory;

    private long modelIndex = 0; // NOPMD relevant for documentation purposes

    private final OutputPort<PCMDeployedEvent> outputPort = this.createOutputPort(PCMDeployedEvent.class);

    public ModelSnapshotWriter(final File modelDumpDirectory,
            final ModelResource<CorrespondenceModel> correspondenceResource,
            final ModelResource<Repository> repositoryResource,
            final ModelResource<ResourceEnvironment> resourceEnvironmentResource,
            final ModelResource<System> systemModelResource, final ModelResource<Allocation> allocationResource,
            final ModelResource<PrivacyModel> privacyModelResource) {
        this.modelDumpDirectory = modelDumpDirectory;
        if (!modelDumpDirectory.exists()) {
            modelDumpDirectory.mkdirs();
        }

        this.correspondenceResource = correspondenceResource;
        this.repositoryResource = repositoryResource;
        this.resourceEnvironmentResource = resourceEnvironmentResource;
        this.assemblyResource = systemModelResource;
        this.allocationResource = allocationResource;
        this.privacyModelResource = privacyModelResource;
    }

    @Override
    protected void execute(final PCMDeployedEvent element) throws Exception {
        final File revisionOutputDirectory = new File(String.format("%s%s%08d",
                this.modelDumpDirectory.toPath().toAbsolutePath().toString(), File.separator, this.modelIndex));
        revisionOutputDirectory.mkdir();
        this.modelIndex++;

        final Registry resourceRegistry = Resource.Factory.Registry.INSTANCE;
        final Map<String, Object> map = resourceRegistry.getExtensionToFactoryMap();
        map.put("*", new XMIResourceFactoryImpl());

        final ResourceSet resourceSet = new ResourceSetImpl();

        resourceSet.setResourceFactoryRegistry(resourceRegistry);

        /** load all models into resource set. */
        final Resource correspondenceEMFResource = this.loadModel(resourceSet, this.correspondenceResource,
                CorrespondenceModel.class, CorrespondencePackage.Literals.CORRESPONDENCE_MODEL,
                CorrespondencePackage.eINSTANCE, revisionOutputDirectory);

        final Resource repositoryModelEMFResource = this.loadModel(resourceSet, this.repositoryResource,
                Repository.class, RepositoryPackage.Literals.REPOSITORY, RepositoryPackage.eINSTANCE,
                revisionOutputDirectory);

        final Resource resourceEnvironmentModelEMFResource = this.loadModel(resourceSet,
                this.resourceEnvironmentResource, ResourceEnvironment.class,
                ResourceenvironmentPackage.Literals.RESOURCE_ENVIRONMENT, ResourceenvironmentPackage.eINSTANCE,
                revisionOutputDirectory);

        final Resource assemblyModelEMFResource = this.loadModel(resourceSet, this.assemblyResource, System.class,
                SystemPackage.Literals.SYSTEM, SystemPackage.eINSTANCE, revisionOutputDirectory);

        final Resource allocationModelEMFResource = this.loadModel(resourceSet, this.allocationResource,
                Allocation.class, AllocationPackage.Literals.ALLOCATION, AllocationPackage.eINSTANCE,
                revisionOutputDirectory);

        final Resource privacyModelEMFResource = this.loadModel(resourceSet, this.privacyModelResource,
                PrivacyModel.class, PrivacyPackage.Literals.PRIVACY_MODEL, PrivacyPackage.eINSTANCE,
                revisionOutputDirectory);

        /** Resolve models. */
        Thread.sleep(1000);
        ModelNeo4JUtil.resolveAll(resourceSet, this.correspondenceResource, this.repositoryResource,
                this.resourceEnvironmentResource, this.assemblyResource, this.allocationResource,
                this.privacyModelResource);

        /** store. */
        repositoryModelEMFResource.save(null);
        resourceEnvironmentModelEMFResource.save(null);
        assemblyModelEMFResource.save(null);
        allocationModelEMFResource.save(null);
        privacyModelEMFResource.save(null);

        correspondenceEMFResource.save(null);

        this.outputPort.send(element);
    }

    private <T extends EObject> Resource loadModel(final ResourceSet resourceSet,
            final ModelResource<T> resourceHandler, final Class<T> clazz, final EClass eClass, final EPackage ePackage,
            final File baseDirectory) throws DBException {
        final T model = resourceHandler.getModelRootNode(clazz, eClass);

        ModelNeo4JUtil.printModel(model);

        final URI writeModelURI = URI.createFileURI(
                String.format("%s%sjpetstore", baseDirectory.toPath().toAbsolutePath().toString(), File.separator));

        final Resource resource = resourceSet.createResource(writeModelURI.appendFileExtension(ePackage.getName()));
        resource.getContents().add(model);

        return resource;
    }

    public OutputPort<PCMDeployedEvent> getOutputPort() {
        return this.outputPort;
    }

}
