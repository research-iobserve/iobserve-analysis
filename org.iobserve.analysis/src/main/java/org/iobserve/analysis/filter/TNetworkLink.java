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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import kieker.common.record.flow.trace.TraceMetadata;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.iobserve.analysis.AnalysisMain;
import org.iobserve.analysis.correspondence.ICorrespondence;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.ModelProviderPlatform;
import org.iobserve.analysis.model.ResourceEnvironmentModelBuilder;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelProvider;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.Connector;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

import teetime.framework.AbstractConsumerStage;

/**
 * TNetworkLink runs asynchronous from the other filters like TAllocation,
 * TDeployment, TEntryCall , TEntryCallSequence, TEntryEventSequence and so one.
 * It will be called from {@link RecordSwitch}, if a new {@link TraceMetadata}
 * is available. It checks whether or not there are unlinked
 * {@link ResourceContainer} with deployed component. If so, it uses the
 * informations from {@link ResourceEnvironment} to calculate the transitive
 * closure of the deployed component and create {@link LinkingResource} for each
 * connection. In most cases the actual set of connection will be just a subset
 * of the transitive closure. This part of the implementation is used currently
 * as fallback behavior, since there is now way to check whether or not the
 * connection is actual available. With future planed enhancements in
 * monitoring, this will change.
 * 
 * @author Robert Heinrich
 * @author Reiner Jung
 * @author Alessandro Giusa
 */
public class TNetworkLink extends AbstractConsumerStage<TraceMetadata> {

	private static long executionCounter = 0;
	
	private final ICorrespondence correspondence;
	private AllocationModelProvider allocationModelProvider;
	private SystemModelProvider systemModelProvider;
	private ResourceEnvironmentModelProvider resourceEnvModelProvider;

	/**
	 * Create new TNetworkLink filter.
	 */
	public TNetworkLink() {
		final ModelProviderPlatform modelProviderPlatform = AnalysisMain.getInstance().getModelProviderPlatform();
		
		// get all model references
		this.correspondence = modelProviderPlatform.getCorrespondenceModel();
		this.allocationModelProvider = modelProviderPlatform.getAllocationModelProvider();
		this.systemModelProvider = modelProviderPlatform.getSystemModelProvider();
		this.resourceEnvModelProvider = modelProviderPlatform.getResourceEnvironmentModelProvider();
	}

	/**
	 * 
	 * @param event
	 */
	@Override
	protected void execute(final TraceMetadata event) {
		AnalysisMain.getInstance().getTimeMemLogger()
			.before(this, this.getId() + TNetworkLink.executionCounter); 
		
		final ResourceEnvironmentModelBuilder builder = 
					new ResourceEnvironmentModelBuilder(this.resourceEnvModelProvider);
		builder.loadModel();
		
		final ResourceEnvironment resourceEnvironment = builder.getModel();
		final List<ResourceContainer> listUnconnectedResourceContainer = TNetworkLink
				.collectUnconnectedResourceContainer(resourceEnvironment);
		
		if (!listUnconnectedResourceContainer.isEmpty()) {
			
			final org.palladiosimulator.pcm.system.System system = this.systemModelProvider.getModel(true);
			final Allocation allocation = this.allocationModelProvider.getModel(true);
			
			for (final ResourceContainer unconnectedConatiner : listUnconnectedResourceContainer) {
				TNetworkLink.getAsmContextDeployedOnContainer(allocation, unconnectedConatiner).stream()
					.map(listAssemblyContextToQuery -> TNetworkLink.getConnectedAsmCtx(system, listAssemblyContextToQuery))
					.map(listAssemblyContextToConnect -> TNetworkLink.collectResourceContainer(allocation, listAssemblyContextToConnect))
					.map(listResourceContainerToConnectTo -> getLinkingResources(resourceEnvironment, listResourceContainerToConnectTo))
					.flatMap(l -> l.stream())
					.collect(Collectors.toList())
					.forEach(link -> link.getConnectedResourceContainers_LinkingResource().add(unconnectedConatiner));
			}
			
			// loop through all unconnected resource container
//			for (final ResourceContainer unconnectedConatiner : listUnconnectedResourceContainer) {
//				final List<AssemblyContext> listAsmDeployedOnContainer = 
//						TNetworkLink.getAsmContextDeployedOnContainer(allocation, unconnectedConatiner);
//				
//				// loop through all assembly context instances which are deployed on the unconnected resource container
//				for (final AssemblyContext asmCtx : listAsmDeployedOnContainer) {
//					final List<AssemblyContext> listConnectedAsmCtx = TNetworkLink.getConnectedAsmCtx(system, asmCtx);
//					final List<ResourceContainer> listResourceContainerToConnect = 
//							TNetworkLink.collectResourceContainer(allocation, listConnectedAsmCtx);
//					
//					final List<LinkingResource> links = getLinkingResources(resourceEnvironment, listResourceContainerToConnect);
//					links.stream().forEach(link -> link.getConnectedResourceContainers_LinkingResource().add(unconnectedConatiner));
//				}
//			}
		}
		
		// build the model
		builder.build();
		AnalysisMain.getInstance().getTimeMemLogger()
			.after(this, this.getId() + TNetworkLink.executionCounter); //TODO testing logger
		TNetworkLink.executionCounter++;
	}
	
	// *****************************************************************
	//
	// *****************************************************************
	
	/**
	 * Check whether or not the given {@link ResourceContainer} are the same.
	 * @param res1 first
	 * @param res2 second
	 * @return true if same
	 */
	private static boolean isEqual(final ResourceContainer res1, final ResourceContainer res2) {
		return res1.getId().equals(res2.getId());
	}
	
	/**
	 * Check whether or not the given {@link AssemblyContext} are the same.
	 * @param asm1 first
	 * @param asm2 second
	 * @return true if same
	 */
	private static boolean isEqualByName(final AssemblyContext asm1, final AssemblyContext asm2) {
		final String asm1Name = asm1.getEntityName().substring(0, asm1.getEntityName().indexOf("_", 0));
		final String asm2Name = asm2.getEntityName().substring(0, asm2.getEntityName().indexOf("_", 0));
		
		System.out.printf("isEqual?Id: %s==%s, Name: %s==%s\n", asm1.getId(), asm2.getId(),
				asm1Name, asm2Name);
		
		
		return asm1Name.contains(asm2Name);
	}
	
	/**
	 * Check whether or not the given {@link AssemblyContext} are the same.
	 * @param asm1 first
	 * @param asm2 second
	 * @return true if same
	 */
	private static boolean isEqualById(final AssemblyContext asm1, final AssemblyContext asm2) {
		System.out.printf("isEqual?Id: %s==%s, Name: %s==%s\n", asm1.getId(), asm2.getId(),
				asm1.getEntityName(), asm2.getEntityName());
		return asm1.getId().equals(asm2.getId());
	}
	
	
	// *****************************************************************
	//
	// *****************************************************************
	
	private static List<LinkingResource> getLinkingResources(final ResourceEnvironment env,
			final List<ResourceContainer> listContainer) {
		return listContainer.stream()
			.map(nextContainer -> env.getLinkingResources__ResourceEnvironment().stream()
				.filter(link -> link.getConnectedResourceContainers_LinkingResource().stream()
						.filter(c -> c.getId().equals(nextContainer.getId())).findAny().isPresent())
				.collect(Collectors.toList()))
			.flatMap(l->l.stream())
			.collect(Collectors.toList());
	}
	
	/**
	 * Collect all {@link ResourceContainer} which are not connected yet.
	 * @param env env
	 * @return list
	 */
	private static List<ResourceContainer> collectUnconnectedResourceContainer(final ResourceEnvironment env) {
		return env.getResourceContainer_ResourceEnvironment().stream()
						.filter(container -> TNetworkLink.getLinks(env, container).isEmpty())
						.collect(Collectors.toList());
	}
	
	/**
	 * Get all {@link LinkingResource} for the given {@link ResourceContainer}.
	 * @param env env
	 * @param container container
	 * @return list
	 */
	private static List<LinkingResource> getLinks(final ResourceEnvironment env, final ResourceContainer container) {
		return env.getLinkingResources__ResourceEnvironment().stream()
				.filter(linking -> TNetworkLink.linkHasResContainer(linking, container).isPresent())
				.collect(Collectors.toList());
	}
	
	/**
	 * Check whether or not the given {@link LinkingResource} has the given {@link ResourceContainer} connected.
	 * @param link link
	 * @param container container
	 * @return option on the res container. If empty, it means there is no link.
	 */
	private static Optional<ResourceContainer> linkHasResContainer(final LinkingResource link, final ResourceContainer container) {
		return link.getConnectedResourceContainers_LinkingResource().stream()
					.filter(res -> TNetworkLink.isEqual(res, container))
					.findFirst();
	}
			
	// *****************************************************************
	//
	// *****************************************************************
	
	/**
	 * Get all {@link AssemblyContext} instances which are deployed on the given {@link ResourceContainer}.
	 * @param allocation allocation
	 * @param container container
	 * @return list
	 */
	private static List<AssemblyContext> getAsmContextDeployedOnContainer(final Allocation allocation, final ResourceContainer container) {
		return allocation.getAllocationContexts_Allocation().stream()
				.filter(ctx -> TNetworkLink.isEqual(ctx.getResourceContainer_AllocationContext(), container))
				.map(ctx -> ctx.getAssemblyContext_AllocationContext())
				.collect(Collectors.toList());
	}
	
	/**
	 * Get all {@link AssemblyContext} instances which are connected to the given {@link AssemblyContext}
	 * @param system system
	 * @param queryAsm asm
	 * @return list of connected asm context
	 */
	private static List<AssemblyContext> getConnectedAsmCtx(final org.palladiosimulator.pcm.system.System system,
			final AssemblyContext queryAsm) {
		return system.getConnectors__ComposedStructure().stream()
				.map(connector -> TNetworkLink.tryCast(connector))
				.filter(option -> option.isPresent())
				.map(option -> option.get())
				.filter(connector -> TNetworkLink.isEqualByName(queryAsm, connector.getRequiringAssemblyContext_AssemblyConnector()))
				.map(connector -> connector.getProvidingAssemblyContext_AssemblyConnector())
				.collect(Collectors.toList());
	}
	
	/**
	 * same as {@link #getConnectedAsmCtx(org.palladiosimulator.pcm.system.System, AssemblyContext)} 
	 * but for all the {@link AssemblyContext} objects in the provided list.
	 * @param system system object
	 * @param listQueryAsm list to loop through
	 * @return list
	 */
	private static List<AssemblyContext> getConnectedAsmCtx(final org.palladiosimulator.pcm.system.System system,
			final List<AssemblyContext> listQueryAsm) {
		final List<AssemblyContext> listToReturn = new ArrayList<>();
		listQueryAsm.stream()
			.map(asm -> getConnectedAsmCtx(system, asm))
			.forEach(list -> listToReturn.addAll(list));
		return listToReturn;
				
	}
	
	/**
	 * Collect all {@link ResourceContainer} where the given list of {@link AssemblyContext} are deployed.
	 * @param allocation allocation
	 * @param listAsm list of asm context
	 * @return list of {@link ResourceContainer}
	 */
	private static List<ResourceContainer> collectResourceContainer(final Allocation allocation,
			final List<AssemblyContext> listAsm) {
		final List<ResourceContainer> listToReturn = new ArrayList<>();
		for (final AssemblyContext nextSearchedAsmCtx : listAsm) {
			final String queryId = nextSearchedAsmCtx.getId();
			for (final AllocationContext allocationCtx : allocation.getAllocationContexts_Allocation()) {
				final AssemblyContext asm = allocationCtx.getAssemblyContext_AllocationContext();
				if (asm.eIsProxy()) {
					EcoreUtil.resolve(asm, allocationCtx.eResource());
				}
				final String asmId = asm.getId();
				if (asmId.equals(queryId)) {
					final ResourceContainer resContainer = allocationCtx.getResourceContainer_AllocationContext();
					if (resContainer.eIsProxy()) {
						EcoreUtil.resolve(resContainer, allocationCtx.eResource());
					}
					listToReturn.add(resContainer);
					break;
				}
			}
		}
		return listToReturn;
	}
	
	/**
	 * Try the cast and give an option of the casted connector back.
	 * @param connector connector
	 * @return option on the cased connector. If present the cast was successfully
	 */
	private static Optional<AssemblyConnector> tryCast(final Connector connector) {
		return connector instanceof AssemblyConnector?Optional.of((AssemblyConnector)connector):Optional.empty();
	}
}
