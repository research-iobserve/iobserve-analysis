/***************************************************************************
 * Copyright 2014 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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

import org.iobserve.analysis.AnalysisMain;
import org.iobserve.analysis.correspondence.Correspondent;
import org.iobserve.analysis.correspondence.ICorrespondence;
import org.iobserve.analysis.model.AllocationModelBuilder;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.ModelProviderPlatform;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.analysis.utils.Opt;
import org.iobserve.common.record.EJBUndeployedEvent;
import org.iobserve.common.record.IUndeploymentRecord;
import org.iobserve.common.record.ServletUndeployedEvent;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

import teetime.framework.AbstractConsumerStage;

/**
 * It could be interesting to combine DeploymentEventTransformation and
 * UndeploymentEventTransformation. However, that would require two input ports.
 * And I have not used the API for multiple input ports.
 * 
 * @author Robert Heinrich
 * @author Reiner Jung
 * 
 */
public final class TUndeployment extends AbstractConsumerStage<IUndeploymentRecord> {
	
	/**reference to correspondence interface.*/
	private final ICorrespondence correspondence;
	/**reference to allocation model provider.*/
	private AllocationModelProvider allocationModelProvider;
	/**reference to system model provider.*/
	private SystemModelProvider systemModelProvider;
	/**reference to resource environment model provider.*/
	private ResourceEnvironmentModelProvider resourceEnvModelProvider;

	/**
	 * Most likely the constructor needs an additional field for the PCM access.
	 * But this has to be discussed with Robert.
	 *
	 * @param correspondence
	 */
	public TUndeployment() {
		final ModelProviderPlatform modelProviderPlatform = AnalysisMain.getInstance().getModelProviderPlatform();
		this.correspondence = modelProviderPlatform.getCorrespondenceModel();
		this.allocationModelProvider = modelProviderPlatform.getAllocationModelProvider();
		this.systemModelProvider = modelProviderPlatform.getSystemModelProvider();
		this.resourceEnvModelProvider = modelProviderPlatform.getResourceEnvironmentModelProvider();
	}

	/**
	 * This method is triggered for every undeployment event.
	 */
	@Override
	protected void execute(final IUndeploymentRecord event) {
		if (event instanceof ServletUndeployedEvent) {
			this.process((ServletUndeployedEvent) event);
		
		} else if (event instanceof EJBUndeployedEvent) {
			this.process((EJBUndeployedEvent) event);
		}
	}
	
	/**
	 * Process the given {@link ServletUndeployedEvent} event and update the model.
	 * @param event event to process
	 */
	private void process(final ServletUndeployedEvent event) {
		final String service = event.getSerivce();
		final String context = event.getContext();
		Opt.of(this.correspondence.getCorrespondent(context))
			.ifPresent()
			.apply(correspondence -> this.updateModel(service, correspondence))
			.elseApply(() -> System.out.printf("No correspondent found for %s \n", service));
	}
	
	/**
	 * Process the given {@link EJBUndeployedEvent} event and update the model.
	 * @param event event to process
	 */
	private void process(final EJBUndeployedEvent event) {
		final String service = event.getSerivce();
		final String context = event.getContext();
		Opt.of(this.correspondence.getCorrespondent(context))
			.ifPresent()
			.apply(correspondent -> this.updateModel(service, correspondent))
			.elseApply(() -> System.out.printf("No correspondent found for %s \n", service));
	}
	
	/**
	 * Update the system- and allocation-model by the given correspondent.
	 * @param serverName name of the server
	 * @param correspondent correspondent
	 */
	private void updateModel(final String serverName, final Correspondent correspondent) {
		// get the model entity name
		final String entityName = correspondent.getPcmEntityName();
		
		// build the assembly context name
		final String asmContextName = entityName + "_" + serverName;
		
		// get the model parts by name
		final Optional<ResourceContainer> optResourceContainer = 
				this.resourceEnvModelProvider.getResourceContainerByName(serverName);
		
		// this can not happen since TAllocation should have created the resource container already.
		Opt.of(optResourceContainer)
			.ifPresent()
			.apply(resourceContainer -> this.updateModel(resourceContainer, asmContextName))
			.elseApply(()-> System.out.printf("AssemblyContext %s was not available?!\n"));
	}
	
	/**
	 * Remove allocation context from allocation model with the given {@link ResourceContainer} and {@link AssemblyContext}
	 * identified by the given entity name.
	 * @param resourceContainer instance of resource container
	 * @param asmContextName entity name of assembly context
	 */
	private void updateModel(final ResourceContainer resourceContainer, final String asmContextName) {
		// update the allocation model
		final AllocationModelBuilder builder = new AllocationModelBuilder(this.allocationModelProvider);
		
		// get assembly context by name or create it if necessary.
		final Optional<AssemblyContext> optAssemblyContext = this.systemModelProvider.getAssemblyContextByName(asmContextName);
		
		Opt.of(optAssemblyContext)
			.ifPresent()
			.apply(assemblyContext -> builder.loadModel().removeAllocationContext(resourceContainer, assemblyContext).build())
			.elseApply(() -> System.out.printf("AssemblyContext for " + resourceContainer.getEntityName() + "not found! \n"));
	}

}
