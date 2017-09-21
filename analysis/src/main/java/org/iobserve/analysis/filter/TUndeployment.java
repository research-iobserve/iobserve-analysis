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
package org.iobserve.analysis.filter;

import java.util.Optional;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.iobserve.analysis.data.RemoveAllocationContextEvent;
import org.iobserve.analysis.model.AllocationModelBuilder;
import org.iobserve.analysis.model.ResourceEnvironmentModelBuilder;
import org.iobserve.analysis.model.SystemModelBuilder;
import org.iobserve.analysis.model.correspondence.Correspondent;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.analysis.utils.ExecutionTimeLogger;
import org.iobserve.analysis.utils.Opt;
import org.iobserve.common.record.EJBUndeployedEvent;
import org.iobserve.common.record.IUndeploymentRecord;
import org.iobserve.common.record.ServletUndeployedEvent;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * This class contains the transformation for updating the PCM allocation model with respect to
 * undeployment. It processes undeployment events and uses the correspondence information in the RAC
 * to update the PCM allocation model.
 *
 * @author Robert Heinrich
 * @author Reiner Jung
 * @author jweg
 *
 */
public final class TUndeployment extends AbstractConsumerStage<IUndeploymentRecord> {

    private static final Logger LOGGER = LogManager.getLogger(TUndeployment.class);

    /** reference to correspondence interface. */
    private final ICorrespondence correspondence;
    /** reference to allocation model provider. */
    private final ModelProvider<Allocation> allocationModelGraphProvider;
    /** reference to system model provider. */
    private final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider;
    /** reference to resource environment model provider. */
    private final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider;

    private final OutputPort<RemoveAllocationContextEvent> modelOutputPort = this.createOutputPort();
    private final OutputPort<IUndeploymentRecord> visualizationOutputPort = this.createOutputPort();

    /**
     * Most likely the constructor needs an additional field for the PCM access. But this has to be
     * discussed with Robert.
     *
     * @param correspondence
     *            correspondence model
     * @param allocationModelGraphProvider
     *            allocation model access
     * @param systemModelGraphProvider
     *            system model access
     * @param resourceEnvironmentModelGraphProvider
     *            resource environment model access
     */
    public TUndeployment(final ICorrespondence correspondence,
            final ModelProvider<Allocation> allocationModelGraphProvider,
            final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider,
            final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider) {
        this.correspondence = correspondence;
        this.allocationModelGraphProvider = allocationModelGraphProvider;
        this.systemModelGraphProvider = systemModelGraphProvider;
        this.resourceEnvironmentModelGraphProvider = resourceEnvironmentModelGraphProvider;
    }

    /**
     * This method is triggered for every undeployment event.
     *
     * @param event
     *            undeployment event
     */
    @Override
    protected void execute(final IUndeploymentRecord event) {
        ExecutionTimeLogger.getInstance().startLogging(event);

        if (event instanceof ServletUndeployedEvent) {
            this.process((ServletUndeployedEvent) event);

        } else if (event instanceof EJBUndeployedEvent) {
            this.process((EJBUndeployedEvent) event);
        }

        ExecutionTimeLogger.getInstance().stopLogging(event);
    }

    /**
     * Process the given {@link ServletUndeployedEvent} event and update the model.
     *
     * @param event
     *            event to process
     */
    private void process(final ServletUndeployedEvent event) {
        final String service = event.getSerivce();
        final String context = event.getContext();
        Opt.of(this.correspondence.getCorrespondent(context)).ifPresent()
                .apply(correspondence -> this.updateModel(service, correspondence, event))
                .elseApply(() -> TUndeployment.LOGGER.info("No correspondent found for " + service + "."));
    }

    /**
     * Process the given {@link EJBUndeployedEvent} event and update the model.
     *
     * @param event
     *            event to process
     */
    private void process(final EJBUndeployedEvent event) {
        final String service = event.getSerivce();
        final String context = event.getContext();
        Opt.of(this.correspondence.getCorrespondent(context)).ifPresent()
                .apply(correspondent -> this.updateModel(service, correspondent, event))
                .elseApply(() -> TUndeployment.LOGGER.info("No correspondent found for " + service + "."));
    }

    /**
     * Update the system- and allocation-model by the given correspondent.
     *
     * @param serverName
     *            name of the server
     * @param correspondent
     *            correspondent
     * @param event
     *            undeployment event
     */
    private void updateModel(final String serverName, final Correspondent correspondent,
            final IUndeploymentRecord event) {
        // get the model entity name
        final String entityName = correspondent.getPcmEntityName();

        // build the assembly context name
        final String asmContextName = entityName + "_" + serverName;

        // get the model parts by name
        final Optional<ResourceContainer> optResourceContainer = ResourceEnvironmentModelBuilder
                .getResourceContainerByName(
                        this.resourceEnvironmentModelGraphProvider.readOnlyRootComponent(ResourceEnvironment.class),
                        serverName);

        // this can not happen since TAllocation should have created the resource container already.
        Opt.of(optResourceContainer).ifPresent()
                .apply(resourceContainer -> this.updateModel(resourceContainer, asmContextName, event)).elseApply(
                        () -> TUndeployment.LOGGER.info("AssemblyContext " + asmContextName + " was not available."));
    }

    /**
     * Remove allocation context from allocation model with the given {@link ResourceContainer} and
     * {@link AssemblyContext} identified by the given entity name.
     *
     * @param resourceContainer
     *            instance of resource container
     * @param asmContextName
     *            entity name of assembly context
     * @param event
     *            undeployment event
     */
    private void updateModel(final ResourceContainer resourceContainer, final String asmContextName,
            final IUndeploymentRecord event) {

        // get assembly context by name or create it if necessary.
        final Optional<AssemblyContext> optAssemblyContext = SystemModelBuilder.getAssemblyContextByName(
                this.systemModelGraphProvider.readOnlyRootComponent(org.palladiosimulator.pcm.system.System.class),
                asmContextName);

        if (optAssemblyContext.isPresent()) {

            // update the allocation graph
            final Allocation allocationModel = this.allocationModelGraphProvider
                    .readOnlyRootComponent(Allocation.class);
            AllocationModelBuilder.removeAllocationContext(allocationModel, resourceContainer,
                    optAssemblyContext.get());
            this.allocationModelGraphProvider.updateComponent(Allocation.class, allocationModel);

            this.modelOutputPort.send(new RemoveAllocationContextEvent(resourceContainer));
            // signal allocation update
            this.visualizationOutputPort.send(event);
        } else {
            this.logger.info("AssemblyContext for " + resourceContainer.getEntityName() + " not found! \n");
        }
    }

    /**
     *
     * @deprecated This port is not used anymore.
     * @return model output port
     */
    @Deprecated
    public OutputPort<RemoveAllocationContextEvent> getModelOutputPort() {
        return this.modelOutputPort;
    }

    /**
     *
     * @return output port that signals a model update to the deployment visualization
     */
    public OutputPort<IUndeploymentRecord> getVisualizationOutputPort() {
        return this.visualizationOutputPort;
    }

}
