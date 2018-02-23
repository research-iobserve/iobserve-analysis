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
package org.iobserve.analysis;

import java.net.MalformedURLException;
import java.net.URL;

import kieker.common.configuration.Configuration;

import org.iobserve.analysis.clustering.EAggregationType;
import org.iobserve.analysis.clustering.EOutputMode;
import org.iobserve.analysis.configurations.MultiInputObservationConfiguration;
import org.iobserve.analysis.service.updater.AllocationVisualizationStage;
import org.iobserve.analysis.service.updater.DeploymentVisualizationStage;
import org.iobserve.analysis.service.updater.UndeploymentVisualizationStage;
import org.iobserve.analysis.snapshot.SnapshotBuilder;
import org.iobserve.analysis.toggle.FeatureToggle;
import org.iobserve.model.correspondence.ICorrespondence;
import org.iobserve.model.provider.neo4j.IModelProvider;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

/**
 * @author Reiner Jung
 *
 */
public class ServiceConfiguration extends MultiInputObservationConfiguration {

    /**
     * Setup service configuration.
     *
     * @param configuration
     *            configuration object containing all parameters for all filters
     * @param visualizationBaseUrl
     *            base URL of the visualization service
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
     * @param resourceContainerModelProvider
     *            graph provider for the resource container model
     * @param assemblyContextModelProvider
     *            graph provider for the assembly context model
     * @param visualizationServiceURL
     *            url to the visualization service
     * @param snapshotBuilder
     *            snapshot builder
     * @param featureToggle
     *            feature toggle
     *
     * @throws MalformedURLException
     *             if any passed URL in the configuration is broken.
     */
    public ServiceConfiguration(final Configuration configuration, final URL visualizationBaseUrl,
            final String systemId, final int varianceOfUserGroups, final int thinkTime, final boolean closedWorkload,
            final ICorrespondence correspondenceModel, final IModelProvider<UsageModel> usageModelProvider,
            final IModelProvider<Repository> repositoryModelProvider,
            final IModelProvider<ResourceEnvironment> resourceEnvironmentModelProvider,
            final IModelProvider<Allocation> allocationModelProvider, final IModelProvider<System> systemModelProvider,
            final IModelProvider<ResourceContainer> resourceContainerModelProvider,
            final IModelProvider<AssemblyContext> assemblyContextModelProvider, final String visualizationServiceURL,
            final SnapshotBuilder snapshotBuilder, final FeatureToggle featureToggle) throws MalformedURLException {
        super(configuration, correspondenceModel, usageModelProvider, repositoryModelProvider,
                resourceEnvironmentModelProvider, allocationModelProvider, systemModelProvider, varianceOfUserGroups,
                thinkTime, closedWorkload, visualizationServiceURL, EAggregationType.X_MEANS_CLUSTERING,
                EOutputMode.UBM_VISUALIZATION, snapshotBuilder, featureToggle);

        final URL containerManagementURL = new URL(visualizationBaseUrl, "/v1/systems/" + systemId + "/changelogs");

        final DeploymentVisualizationStage deploymentVisualizationStage = new DeploymentVisualizationStage(
                containerManagementURL, systemId, resourceContainerModelProvider, assemblyContextModelProvider);
        final AllocationVisualizationStage allocationVisualizationStage = new AllocationVisualizationStage(
                containerManagementURL, systemId);
        final UndeploymentVisualizationStage undeploymentVisualizationStage = new UndeploymentVisualizationStage(
                containerManagementURL, systemId, resourceContainerModelProvider, assemblyContextModelProvider,
                systemModelProvider);

        this.connectPorts(this.deploymentStage.getDeployedOutputPort(), deploymentVisualizationStage.getInputPort());
        this.connectPorts(this.deploymentStage.getAllocationOutputPort(), allocationVisualizationStage.getInputPort());
        this.connectPorts(this.undeploymentStage.getUndeployedOutputPort(),
                undeploymentVisualizationStage.getInputPort());
        // TODO missing deallocate

    }

}
