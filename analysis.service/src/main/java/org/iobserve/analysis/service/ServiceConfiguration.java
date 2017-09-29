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
package org.iobserve.analysis.service;

import java.net.MalformedURLException;
import java.net.URL;

import org.iobserve.analysis.MultiInputObservationConfiguration;
import org.iobserve.analysis.clustering.EAggregationType;
import org.iobserve.analysis.clustering.EOutputMode;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.RepositoryModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.analysis.model.UsageModelProvider;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.analysis.service.updater.AllocationVisualizationStage;
import org.iobserve.analysis.service.updater.DeploymentVisualizationStage;
import org.iobserve.analysis.service.updater.UndeploymentVisualizationStage;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

/**
 * @author Reiner Jung
 *
 */
public class ServiceConfiguration extends MultiInputObservationConfiguration {

    /**
     * Setup service configuration.
     *
     * @param inputPort
     *            analysis input port, default is 9876
     * @param outputHostname
     *            visualization hostname
     * @param outputPort
     *            port to be used for the visualization host
     * @param systemId
     *            system id to be used for the visualization
     * @param varianceOfUserGroups
     *            variance of user groups
     * @param thinkTime
     *            think time
     * @param closedWorkload
     *            flag for closed and open workload
     * @param correspondenceModel
     *            the correspondence model
     * @param usageModelProvider
     *            provider for the usage model
     * @param repositoryModelProvider
     *            provider for the repository model
     * @param resourceEnvironmentModelProvider
     *            provider for the resource model
     * @param allocationModelProvider
     *            provider for the allocation model
     * @param systemModelProvider
     *            provider for the system model
     * @param visualizationServiceURL
     *            url to the visualization service
     *
     * @throws MalformedURLException
     *             if any passed URL in the configuration is broken.
     */
    public ServiceConfiguration(final int inputPort, final String outputHostname, final String outputPort,
            final String systemId, final int varianceOfUserGroups, final int thinkTime, final boolean closedWorkload,
            final ICorrespondence correspondenceModel, final UsageModelProvider usageModelProvider,
            final RepositoryModelProvider repositoryModelProvider,
            final ResourceEnvironmentModelProvider resourceEnvironmentModelProvider,
            final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider,
            final ModelProvider<ResourceContainer> resourceContainerModelGraphProvider,
            final AllocationModelProvider allocationModelProvider,
            final ModelProvider<Allocation> allocationModelGraphProvider,
            final ModelProvider<AssemblyContext> assemblyContextModelGraphProvider,
            final SystemModelProvider systemModelProvider,
            final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider,
            final ModelProvider<AssemblyContext> assCtxSystemModelGraphProvider, final String visualizationServiceURL) throws MalformedURLException {
        super(inputPort, correspondenceModel, usageModelProvider, repositoryModelProvider,
                resourceEnvironmentModelProvider, resourceEnvironmentModelGraphProvider, allocationModelProvider,
                allocationModelGraphProvider, systemModelProvider, systemModelGraphProvider, varianceOfUserGroups,
                thinkTime, closedWorkload, visualizationServiceURL, EAggregationType.X_MEANS_CLUSTERING,
                EOutputMode.UBM_VISUALIZATION);

        final URL url = new URL(
                "http://" + outputHostname + ":" + outputPort + "/v1/systems/" + systemId + "/changelogs");

        final DeploymentVisualizationStage deploymentVisualizationStage = new DeploymentVisualizationStage(url,
                systemId, resourceContainerModelGraphProvider, assemblyContextModelGraphProvider, correspondenceModel);
        final AllocationVisualizationStage allocationVisualizationStage = new AllocationVisualizationStage(url,
                systemId, resourceContainerModelGraphProvider);
        final UndeploymentVisualizationStage undeploymentVisualizationStage = new UndeploymentVisualizationStage(url,
                systemId, resourceContainerModelGraphProvider, assCtxSystemModelGraphProvider, systemModelGraphProvider,
                correspondenceModel);

        this.connectPorts(this.deploymentAfterAllocation.getDeploymentFinishedOutputPort(),
                deploymentVisualizationStage.getInputPort());
        this.connectPorts(this.tAllocationAfterDeploy.getAllocationOutputPort(),
                allocationVisualizationStage.getInputPort());
        this.connectPorts(this.undeployment.getVisualizationOutputPort(),
                undeploymentVisualizationStage.getInputPort());

    }

}
