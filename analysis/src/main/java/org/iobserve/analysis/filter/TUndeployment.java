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
import org.eclipse.emf.common.util.EList;
import org.iobserve.analysis.data.RemoveAllocationContextEvent;
import org.iobserve.analysis.model.AllocationModelBuilder;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.DesignDecisionModelBuilder;
import org.iobserve.analysis.model.DesignDecisionModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelBuilder;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelBuilder;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.analysis.model.correspondence.Correspondent;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
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
 * This class contains the transformation for updating the PCM allocation model
 * with respect to undeployment. It processes undeployment events and uses the
 * correspondence information in the RAC to update the PCM allocation model.
 *
 * @author Robert Heinrich
 * @author Reiner Jung
 *
 */
public final class TUndeployment extends AbstractConsumerStage<IUndeploymentRecord> {

	private static final Logger LOG = LogManager.getLogger(TUndeployment.class);

	/** reference to correspondence interface. */
	private final ICorrespondence correspondence;
	/** reference to allocation model provider. */
	private final AllocationModelProvider allocationModelProvider;
	/** reference to system model provider. */
	private final SystemModelProvider systemModelProvider;
	/** reference to resource environment model provider. */
	private final ResourceEnvironmentModelProvider resourceEnvModelProvider;
	/** reference to design decisions model provider. */
	private final DesignDecisionModelProvider designDecisionModelProvider;

	private final OutputPort<RemoveAllocationContextEvent> outputPort = this.createOutputPort();
	private final OutputPort<Boolean> outputPortSnapshot = this.createOutputPort();

	/**
	 * Most likely the constructor needs an additional field for the PCM access.
	 * But this has to be discussed with Robert.
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
			final SystemModelProvider systemModelProvider, final ResourceEnvironmentModelProvider resourceEnvironmentModelProvider,
			final DesignDecisionModelProvider designDecisionModelProvider) {
		this.correspondence = correspondence;
		this.allocationModelProvider = allocationModelProvider;
		this.systemModelProvider = systemModelProvider;
		this.resourceEnvModelProvider = resourceEnvironmentModelProvider;
		this.designDecisionModelProvider = designDecisionModelProvider;
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
		this.outputPortSnapshot.send(false);
	}

	/**
	 * Process the given {@link ServletUndeployedEvent} event and update the
	 * model.
	 *
	 * @param event
	 *            event to process
	 */
	private void process(final ServletUndeployedEvent event) {
		final String service = event.getSerivce();
		final String context = event.getContext();
		Opt.of(this.correspondence.getCorrespondent(context)).ifPresent().apply(correspondence -> this.updateModel(service, correspondence))
				.elseApply(() -> LOG.error(String.format("No correspondent found for %s \n", service)));
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

		LOG.info("New TUnDeploymentEvent: " + context + "\t ->\t" + service);

		EList<ResourceContainer> resContainers = this.resourceEnvModelProvider.getModel().getResourceContainer_ResourceEnvironment();
		Optional<ResourceContainer> resContainer = resContainers.stream().filter(s -> s.getEntityName().equals(service)).findAny();

		if (resContainer.isPresent()) {
			this.updateModel(resContainer.get(), context);
		} else {
			LOG.error(String.format("No correspondent found for %s\n", service));
		}

		// Opt.of(this.correspondence.getCorrespondent(context)).ifPresent()
		// .apply(correspondent -> this.updateModel(service, correspondent))
		// .elseApply(() -> System.out.printf("No correspondent found for %s
		// \n", service));
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
				.getResourceContainerByName(this.resourceEnvModelProvider.getModel(), serverName);

		// this can not happen since TAllocation should have created the
		// resource container already.
		Opt.of(optResourceContainer).ifPresent().apply(resourceContainer -> this.updateModel(resourceContainer, asmContextName))
				.elseApply(() -> LOG.error("AssemblyContext %s was not available?!\n"));
	}

	/**
	 * Remove allocation context from allocation model with the given
	 * {@link ResourceContainer} and {@link AssemblyContext} identified by the
	 * given entity name.
	 *
	 * @param resourceContainer
	 *            instance of resource container
	 * @param asmContextName
	 *            entity name of assembly context
	 */
	private void updateModel(final ResourceContainer resourceContainer, final String asmContextName) {
		// update the allocation model

		// get assembly context by name or create it if necessary.
		final Optional<AssemblyContext> optAssemblyContext = SystemModelBuilder.getAssemblyContextByName(this.systemModelProvider.getModel(),
				asmContextName);

		if (optAssemblyContext.isPresent()) {
			// this.allocationModelProvider.loadModel();

			AllocationContext allocCont = this.allocationModelProvider.getAllocationContext(optAssemblyContext.get(), resourceContainer);
			if (allocCont != null) {
				DesignDecisionModelBuilder.deleteDegreeOfFreedom(this.designDecisionModelProvider.getModel(), allocCont.getEntityName());
				this.designDecisionModelProvider.save();

				AllocationModelBuilder.removeAllocationContext(this.allocationModelProvider.getModel(), resourceContainer, optAssemblyContext.get());
				this.allocationModelProvider.save();

				this.outputPort.send(new RemoveAllocationContextEvent(resourceContainer));
			} else {
				LOG.info("AllocationContext for " + optAssemblyContext.get().getEntityName() + " @ " + resourceContainer.getEntityName()
						+ "not found! \n");
			}
		} else {
			LOG.info("AssemblyContext for " + resourceContainer.getEntityName() + "not found! \n");
		}
	}

	/**
	 * @return outputPort for visualization
	 */
	public OutputPort<RemoveAllocationContextEvent> getOutputPort() {
		return this.outputPort;
	}

	public OutputPort<Boolean> getOutputPortSnapshot() {
		return this.outputPortSnapshot;
	}

}
