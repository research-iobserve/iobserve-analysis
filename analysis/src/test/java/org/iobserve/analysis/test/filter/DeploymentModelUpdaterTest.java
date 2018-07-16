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

import org.iobserve.analysis.deployment.DeploymentModelUpdater;
import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.analysis.test.data.ModelLevelDataFactory;
import org.iobserve.common.record.ISOCountryCode;
import org.iobserve.model.persistence.neo4j.ModelResource;
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
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

/**
 * @author Reiner Jung
 *
 */
public class DeploymentModelUpdaterTest {

    private final Repository repository = RepositoryModelDataFactory.createBookstoreRepositoryModel();
    private final System system = SystemDataFactory.createSystem(this.repository);
    private final ResourceEnvironment resourceEnvironment = ResourceEnvironmentDataFactory.createResourceEnvironment();
    private final Allocation allocation = AllocationDataFactory.createAllocation(this.system, this.resourceEnvironment);

    /**
     * Test method for
     * {@link org.iobserve.analysis.deployment.DeploymentModelUpdater#execute(org.iobserve.analysis.deployment.data.PCMDeployedEvent)}.
     */
    @Test
    public void testExecutePCMDeployedEvent() {
        final ModelResource<Allocation> allocationModelResource = this.initializationDatabase();

        final Allocation initDbAllocation = allocationModelResource.getModelRootNode(Allocation.class,
                AllocationPackage.Literals.ALLOCATION);

        final DeploymentModelUpdater deploymentModelUpdater = new DeploymentModelUpdater(allocationModelResource);

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
        final Allocation dbAllocation = allocationModelResource.getModelRootNode(Allocation.class,
                AllocationPackage.Literals.ALLOCATION);
        for (final AllocationContext context : dbAllocation.getAllocationContexts_Allocation()) {
            Assert.assertNotEquals("No assembly context for " + context.getEntityName(),
                    context.getAssemblyContext_AllocationContext(), null);
        }
    }

    private ModelResource<Allocation> initializationDatabase() {
        this.prepareGraph("testExecutePCMDeployedEvent-repository").storeModelPartition(this.repository);
        this.prepareGraph("testExecutePCMDeployedEvent-system").storeModelPartition(this.system);
        this.prepareGraph("testExecutePCMDeployedEvent-resource").storeModelPartition(this.resourceEnvironment);

        final ModelResource<Allocation> resource = this
                .prepareGraph(DeploymentModelUpdaterTest.class + "testExecutePCMDeployedEvent");
        resource.storeModelPartition(this.allocation);

        return resource;
    }

    /**
     * Prepare graph for model.
     *
     * @param name
     *            test directory name
     *
     * @return the prepared graph
     */
    protected ModelResource<Allocation> prepareGraph(final String name) {
        final File graphBaseDir = new File("./testdb/" + this.getClass().getCanonicalName() + "." + name);

        this.removeDirectory(graphBaseDir);

        return new ModelResource<>(AllocationPackage.eINSTANCE, graphBaseDir);
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
