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

import org.iobserve.analysis.model.AllocationModelBuilder;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelBuilder;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelBuilder;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.analysis.model.correspondence.Correspondent;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.utils.ExecutionTimeLogger;
import org.iobserve.analysis.utils.Opt;
import org.iobserve.common.record.EJBUndeployedEvent;
import org.iobserve.common.record.IUndeploymentRecord;
import org.iobserve.common.record.ServletUndeployedEvent;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * This class contains the transformation for updating the PCM allocation model with respect to
 * undeployment. It processes undeployment events and uses the correspondence information to update 
 * the PCM allocation model.
 *
 * @author Robert Heinrich
 * @author Reiner Jung
 * @author Nicolas Boltz
 */
public final class TUndeployment extends AbstractConsumerStage<IUndeploymentRecord> {

    /** reference to correspondence interface. */
    private final ICorrespondence correspondence;
    /** reference to allocation model provider. */
    private final AllocationModelProvider allocationModelProvider;
    /** reference to system model provider. */
    private final SystemModelProvider systemModelProvider;
    /** reference to resource environment model provider. */
    private final ResourceEnvironmentModelProvider resourceEnvironmentModelProvider;
    /** output port. */
    private final OutputPort<ResourceContainer> outputPort = this.createOutputPort();

    /**
     * Create new TUndeployment filter.
     *
     * @param correspondence
     *            correspondence model
     * @param allocationModelProvider
     *            allocation model access
     * @param systemModelProvider
     *            system model access
     * @param resourceEnvironmentModelProvider
     *            resource environment model access
     */
    public TUndeployment(final ICorrespondence correspondence, final AllocationModelProvider allocationModelProvider,
            final SystemModelProvider systemModelProvider,
            final ResourceEnvironmentModelProvider resourceEnvironmentModelProvider) {
        this.correspondence = correspondence;
        this.allocationModelProvider = allocationModelProvider;
        this.systemModelProvider = systemModelProvider;
        this.resourceEnvironmentModelProvider = resourceEnvironmentModelProvider;
    }

    /**
     * This method is triggered for every undeployment event.
     * 
     * @param event
     *            one undeployment event to be processed
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
                .apply(correspondence -> this.updateModel(service, correspondence))
                .elseApply(() -> System.out.printf("No correspondent found for %s \n", service));
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
                .apply(correspondent -> this.updateModel(service, correspondent))
                .elseApply(() -> System.out.printf("No correspondent found for %s \n", service));
    }

    /**
     * Update the system- and allocation-model by the given correspondent.
     *
     * @param serverName
     *            name of the server
     * @param correspondent
     *            correspondent
     */
    private void updateModel(final String serverName, final Correspondent correspondent) {
        // get the model entity name
        final String entityName = correspondent.getPcmEntityName();

        // build the assembly context name
        final String asmContextName = entityName + "_" + serverName;

        // get the model parts by name
        final Optional<ResourceContainer> optResourceContainer = ResourceEnvironmentModelBuilder
                .getResourceContainerByName(this.resourceEnvironmentModelProvider.getModel(), serverName);

        // this can not happen since TAllocation should have created the resource container already.
        Opt.of(optResourceContainer).ifPresent()
                .apply(resourceContainer -> this.updateModel(resourceContainer, asmContextName))
                .elseApply(() -> System.out.printf("AssemblyContext %s was not available?!\n"));
    }

    /**
     * Remove allocation context from allocation model with the given {@link ResourceContainer} and
     * {@link AssemblyContext} identified by the given entity name.
     *
     * @param resourceContainer
     *            instance of resource container
     * @param asmContextName
     *            entity name of assembly context
     */
    private void updateModel(final ResourceContainer resourceContainer, final String asmContextName) {
        // update the allocation model

        // get assembly context by name or create it if necessary.
        final Optional<AssemblyContext> optAssemblyContext = SystemModelBuilder
                .getAssemblyContextByName(this.systemModelProvider.getModel(), asmContextName);

        if (optAssemblyContext.isPresent()) {
        	AssemblyContext assemblyContext = optAssemblyContext.get();
            this.allocationModelProvider.loadModel();
            AllocationModelBuilder.removeAllocationContext(this.allocationModelProvider.getModel(), resourceContainer,
            		assemblyContext);
            this.allocationModelProvider.save();
            
            // remove assembly context from system model
            this.systemModelProvider.loadModel();
            SystemModelBuilder.removeAssemblyContext(this.systemModelProvider.getModel(), assemblyContext.getId());
            this.systemModelProvider.save();
            
            if(!isResourceContainerUsed(resourceContainer)) {
            	/* There is no allocation context affiliated with the resource container.
				 * Send to Deallocation to remove resource container from ResourceEnvironment. */
				this.outputPort.send(resourceContainer);
            }
        } else {
            System.out.printf("AssemblyContext for " + resourceContainer.getEntityName() + " not found! \n");
        }
    }
    
    private boolean isResourceContainerUsed(ResourceContainer container) {
    	for(AllocationContext context : this.allocationModelProvider.getModel().getAllocationContexts_Allocation()) {
			if(context.getResourceContainer_AllocationContext().getId().equals(container.getId())) {
				return true;
			}
		}
    	
    	return false;
    }

    public OutputPort<ResourceContainer> getOutputPort() {
        return this.outputPort;
    }
}