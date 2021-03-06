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
package org.iobserve.analysis.test.filter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import teetime.framework.test.StageTester;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.iobserve.analysis.deployment.DeploymentModelUpdater;
import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.analysis.test.data.ModelLevelDataFactory;
import org.iobserve.common.record.ISOCountryCode;
import org.iobserve.model.correspondence.CorrespondenceFactory;
import org.iobserve.model.correspondence.CorrespondenceModel;
import org.iobserve.model.correspondence.CorrespondencePackage;
import org.iobserve.model.persistence.DBException;
import org.iobserve.model.persistence.neo4j.Neo4JModelResource;
import org.iobserve.model.test.data.AllocationDataFactory;
import org.iobserve.model.test.data.RepositoryModelDataFactory;
import org.iobserve.model.test.data.ResourceEnvironmentDataFactory;
import org.iobserve.model.test.data.SystemDataFactory;
import org.junit.Assert;
import org.junit.Test;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.allocation.AllocationPackage;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryPackage;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.system.SystemPackage;

/**
 * @author Reiner Jung
 *
 */
public class DeploymentModelUpdaterTest {

    private final Repository repository = RepositoryModelDataFactory.createBookstoreRepositoryModel();
    private final System system = SystemDataFactory.createSystem(this.repository);
    private final ResourceEnvironment resourceEnvironment = ResourceEnvironmentDataFactory.createResourceEnvironment();
    private final Allocation allocation = AllocationDataFactory.createAllocation(this.system, this.resourceEnvironment);
    private final CorrespondenceModel correspondenceModel = CorrespondenceFactory.eINSTANCE.createCorrespondenceModel();
    private Neo4JModelResource<Allocation> allocationResource;
    private Neo4JModelResource<CorrespondenceModel> correspondenceResource;

    /**
     * Test method for
     * {@link org.iobserve.analysis.deployment.DeploymentModelUpdater#execute(org.iobserve.analysis.deployment.data.PCMDeployedEvent)}.
     *
     * @throws DBException
     */
    @Test
    public void testExecutePCMDeployedEvent() throws DBException {
        java.lang.System.err.println("AAAAAAAAAAAAAAAAAAAAAAa");
        this.initializationDatabase();
        java.lang.System.err.println("q alloc");
        final Allocation initDbAllocation = this.allocationResource.getModelRootNode(Allocation.class,
                AllocationPackage.Literals.ALLOCATION);
        java.lang.System.err.println("deployer filter");
        final DeploymentModelUpdater deploymentModelUpdater = new DeploymentModelUpdater(this.correspondenceResource,
                this.allocationResource);

        /** input deployment event */
        final AssemblyContext assemblyContext = SystemDataFactory.findAssemblyContext(this.system,
                SystemDataFactory.QUERY_ASSEMBLY_CONTEXT);
        final PCMDeployedEvent deploymentEvent = ModelLevelDataFactory
                .createPCMDeployedEvent(ISOCountryCode.EVIL_EMPIRE, assemblyContext);
        deploymentEvent.setResourceContainer(ResourceEnvironmentDataFactory.findContainer(this.resourceEnvironment,
                ResourceEnvironmentDataFactory.QUERY_CONTAINER_3));

        final List<PCMDeployedEvent> inputEvents = new ArrayList<>();
        inputEvents.add(deploymentEvent);

        StageTester.test(deploymentModelUpdater).and().send(inputEvents).to(deploymentModelUpdater.getInputPort())
                .start();

        Assert.assertThat(deploymentModelUpdater.getDeployedNotifyOutputPort(), StageTester.produces(deploymentEvent));

        // TODO check is DB contains a deployment
        final Allocation dbAllocation = this.allocationResource.getModelRootNode(Allocation.class,
                AllocationPackage.Literals.ALLOCATION);
        for (final AllocationContext context : dbAllocation.getAllocationContexts_Allocation()) {
            Assert.assertNotEquals("No assembly context for " + context.getEntityName(),
                    context.getAssemblyContext_AllocationContext(), null);
        }
    }

    private void initializationDatabase() throws DBException {
        java.lang.System.err.println("init rep");
        this.prepareGraph(RepositoryPackage.eINSTANCE, "testExecutePCMDeployedEvent-repository")
                .storeModelPartition(this.repository);
        java.lang.System.err.println("init sys");
        this.prepareGraph(SystemPackage.eINSTANCE, "testExecutePCMDeployedEvent-system")
                .storeModelPartition(this.system);
        java.lang.System.err.println("init env");
        this.prepareGraph(ResourceenvironmentPackage.eINSTANCE, "testExecutePCMDeployedEvent-resource")
                .storeModelPartition(this.resourceEnvironment);
        java.lang.System.err.println("prep correspondence");
        this.correspondenceResource = this.prepareGraph(CorrespondencePackage.eINSTANCE,
                "testExecutePCMDeployedEvent-correspondence");
        java.lang.System.err.println("store corr");
        this.correspondenceResource.storeModelPartition(this.correspondenceModel);
        java.lang.System.err.println("prep alloc");
        this.allocationResource = this.prepareGraph(AllocationPackage.eINSTANCE, "testExecutePCMDeployedEvent-package");
        java.lang.System.err.println("store alloc");
        this.allocationResource.storeModelPartition(this.allocation);
        java.lang.System.err.println("init complete.");
    }

    /**
     * Prepare graph for model.
     *
     * @param name
     *            test directory name
     *
     * @return the prepared graph
     */
    protected <T extends EObject> Neo4JModelResource<T> prepareGraph(final EPackage ePackage, final String name) {
        final File graphBaseDir = new File("testdb/" + this.getClass().getCanonicalName() + "/" + name);

        this.removeDirectory(graphBaseDir);

        return new Neo4JModelResource<>(ePackage, graphBaseDir);
    }

    /**
     * Delete directory tree.
     *
     * @param dir
     *            directory
     */
    protected void removeDirectory(final File dir) {
        if (dir.isDirectory()) {
            for (final File file : dir.listFiles()) {
                this.removeDirectory(file);
            }
            dir.delete();
        } else {
            dir.delete();
        }
    }

}
