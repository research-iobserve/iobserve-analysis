/***************************************************************************
 * Copyright (C) 2014 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.deployment;

import java.util.Optional;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.iobserve.analysis.model.builder.AllocationModelBuilder;
import org.iobserve.analysis.model.builder.ResourceEnvironmentModelBuilder;
import org.iobserve.analysis.model.builder.SystemModelBuilder;
import org.iobserve.analysis.model.correspondence.Correspondent;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.analysis.utils.Opt;
import org.iobserve.common.record.ContainerAllocationEvent;
import org.iobserve.common.record.EJBDeployedEvent;
import org.iobserve.common.record.IAllocationRecord;
import org.iobserve.common.record.IDeploymentRecord;
import org.iobserve.common.record.ServletDeployedEvent;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * This class contains the transformation for updating the PCM allocation model with respect to
 * deployment. It processes deployment events and uses the correspondence information in the RAC to
 * update the PCM allocation model.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 * @author jweg
 */
public final class DeploymentModelUpdater extends AbstractConsumerStage<IDeploymentRecord> {

    private static final Logger LOGGER = LogManager.getLogger(DeploymentModelUpdater.class);

    /** reference to correspondent model. */
    private final ICorrespondence correspondence;
    /** reference to allocation model provider. */
    private final ModelProvider<Allocation> allocationModelGraphProvider;
    /** reference to system model provider. */
    private final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider;
    /** reference to resource environment model provider. */
    private final ModelProvider<ResourceEnvironment> resourceEnvModelGraphProvider;

    private final OutputPort<IAllocationRecord> allocationOutputPort = this.createOutputPort();
    private final OutputPort<IDeploymentRecord> deploymentOutputPort = this.createOutputPort();
    private final OutputPort<IDeploymentRecord> deploymentFinishedOutputPort = this.createOutputPort();
    private final OutputPort<Boolean> outputPortSnapshot = this.createOutputPort();

    /**
     * Most likely the constructor needs an additional field for the PCM access. But this has to be
     * discussed with Robert.
     *
     * @param correspondence
     *            the correspondence model access
     * @param allocationModelGraphProvider
     *            allocation model provider
     * @param systemModelGraphProvider
     *            system model provider
     * @param resourceEnvironmentModelGraphProvider
     *            resource environment model provider
     */
    public DeploymentModelUpdater(final ICorrespondence correspondence,
            final ModelProvider<Allocation> allocationModelGraphProvider,
            final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider,
            final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider) {
        this.correspondence = correspondence;
        this.allocationModelGraphProvider = allocationModelGraphProvider;
        this.systemModelGraphProvider = systemModelGraphProvider;
        this.resourceEnvModelGraphProvider = resourceEnvironmentModelGraphProvider;
    }

    /**
     * This method is triggered for every deployment event.
     *
     * @param event
     *            one deployment event to be processed
     */
    @Override
    protected void execute(final IDeploymentRecord event) {
        if (event instanceof ServletDeployedEvent) {
            this.process((ServletDeployedEvent) event);

        } else if (event instanceof EJBDeployedEvent) {
            this.process((EJBDeployedEvent) event);
        }

        this.outputPortSnapshot.send(false);
    }

    /**
     * @return allocationOutputPort
     */
    public OutputPort<IAllocationRecord> getAllocationOutputPort() {
        return this.allocationOutputPort;
    }

    /**
     * @return deploymentOutputPort
     */
    public OutputPort<IDeploymentRecord> getDeploymentOutputPort() {
        return this.deploymentOutputPort;
    }

    /**
     * @return deploymentFinishedOutputPort
     */
    public OutputPort<IDeploymentRecord> getDeploymentFinishedOutputPort() {
        return this.deploymentFinishedOutputPort;
    }

    /**
     * @return output port for snapshot
     */
    public OutputPort<Boolean> getOutputPortSnapshot() {
        return this.outputPortSnapshot;
    }

    /**
     * Process the given {@link ServletDeployedEvent} event and update the model.
     *
     * @param event
     *            event to process
     */
    private void process(final ServletDeployedEvent event) {
        final String service = event.getSerivce();
        final String context = event.getContext();

        // build the containerAllocationEvent
        final String urlContext = context.replaceAll("\\.", "/");
        final String url = "http://" + service + '/' + urlContext;

        if (this.correspondence.getCorrespondent(context) != null) {
            this.updateModel(service, this.correspondence.getCorrespondent(context).get(), url, event);
        } else {
            DeploymentModelUpdater.LOGGER.info("No correspondent found for " + service + ".");
        }
    }

    /**
     * Process the given {@link EJBDeployedEvent} event and update the model.
     *
     * @param event
     *            event to process
     */
    private void process(final EJBDeployedEvent event) {
        final String service = event.getSerivce();
        final String context = event.getContext();

        // build the url for the containerAllocationEvent
        final String urlContext = context.replaceAll("\\.", "/");
        final String url = "http://" + service + '/' + urlContext;

        Opt.of(this.correspondence.getCorrespondent(context)).ifPresent()
                .apply(correspondent -> this.updateModel(service, correspondent, url, event))
                .elseApply(() -> DeploymentModelUpdater.LOGGER.info("No correspondent found for " + service + "."));
    }

    /**
     * Update the system model and allocation model by the given correspondent.
     *
     * @param serverName
     *            name of the server
     * @param correspondent
     *            correspondent
     * @param url
     *            url for {@link ContainerAllocationEvent}
     * @param event
     *            initial deployment event
     *
     */
    private void updateModel(final String serverName, final Correspondent correspondent, final String url,
            final IDeploymentRecord event) {
        // get the model entity name
        final String entityName = correspondent.getPcmEntityName();

        // build the assembly context name
        final String asmContextName = entityName + "_" + serverName;

        // get the model parts by name
        final Optional<ResourceContainer> optResourceContainer = ResourceEnvironmentModelBuilder
                .getResourceContainerByName(
                        this.resourceEnvModelGraphProvider.readOnlyRootComponent(ResourceEnvironment.class),
                        serverName);

        Opt.of(optResourceContainer).ifPresent()
                .apply(resourceContainer -> this.updateAllocationModel(resourceContainer, asmContextName, event))
                .elseApply(() -> {
                    // if the resource container with this serverName is not available, send an
                    // event to TAllocation (creating the resource container) and forward the
                    // deployment event to TDeployment (deploying on created resource container)
                    this.allocationOutputPort.send(new ContainerAllocationEvent(url));
                    this.deploymentOutputPort.send(event);
                });

    }

    /**
     * Add allocation context to allocation model with the given {@link ResourceContainer} and
     * {@link AssemblyContext} identified by the given entity name.
     *
     * @param resourceContainer
     *            instance of resource container
     * @param asmContextName
     *            entity name of assembly context
     * @param event
     *            deployment event
     */
    private void updateAllocationModel(final ResourceContainer resourceContainer, final String asmContextName,
            final IDeploymentRecord event) {
        // get assembly context by name or create it if necessary.
        final AssemblyContext assemblyContext;

        final org.palladiosimulator.pcm.system.System systemModel = this.systemModelGraphProvider
                .readOnlyRootComponent(org.palladiosimulator.pcm.system.System.class);

        final Optional<AssemblyContext> optAssCtx = SystemModelBuilder.getAssemblyContextByName(systemModel,
                asmContextName);

        if (optAssCtx.isPresent()) {
            assemblyContext = optAssCtx.get();
        } else {
            assemblyContext = DeploymentModelUpdater.createAssemblyContextByName(this.systemModelGraphProvider,
                    asmContextName);
        }

        // update the allocation graph
        final Allocation allocationModel = this.allocationModelGraphProvider.readOnlyRootComponent(Allocation.class);
        AllocationModelBuilder.addAllocationContextIfAbsent(allocationModel, resourceContainer, assemblyContext);
        this.allocationModelGraphProvider.updateComponent(Allocation.class, allocationModel);

        // signal deployment update
        this.deploymentFinishedOutputPort.send(event);
    }

    /**
     * Create {@link AssemblyContext} with the given name.
     *
     * @param systemModelGraphProvider
     *            system model provider
     * @param name
     *            name
     * @return created assembly context
     */
    private static AssemblyContext createAssemblyContextByName(
            final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider, final String name) {
        final org.palladiosimulator.pcm.system.System systemModel = systemModelGraphProvider
                .readOnlyRootComponent(org.palladiosimulator.pcm.system.System.class);
        final AssemblyContext ctx = SystemModelBuilder.createAssemblyContextsIfAbsent(systemModel, name);
        systemModelGraphProvider.updateComponent(org.palladiosimulator.pcm.system.System.class, systemModel);
        return ctx;
    }

}
