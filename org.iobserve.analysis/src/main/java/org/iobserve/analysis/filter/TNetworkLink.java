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

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import kieker.common.record.flow.trace.TraceMetadata;

import org.iobserve.analysis.AnalysisMain;
import org.iobserve.analysis.correspondence.ICorrespondence;
import org.iobserve.analysis.model.AllocationModelBuilder;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.ModelProviderPlatform;
import org.iobserve.analysis.model.ResourceEnvironmentModelBuilder;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelBuilder;
import org.iobserve.analysis.model.SystemModelProvider;
import org.palladiosimulator.pcm.allocation.Allocation;
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
			.before(this, this.getId() + TNetworkLink.executionCounter); //TODO testing logger
		// add your transformation here
		
		final ResourceEnvironmentModelBuilder builder = 
					new ResourceEnvironmentModelBuilder(this.resourceEnvModelProvider);
		builder.loadModel();
		
		final ResourceEnvironment resourceEnvironment = builder.getModel();
		final List<ResourceContainer> unconnectedResourceContainer = TNetworkLink.getUnconnectedContainers.apply(resourceEnvironment);
		
		if (!unconnectedResourceContainer.isEmpty()) {
			
			final SystemModelBuilder sysBuilder = new SystemModelBuilder(this.systemModelProvider);
			sysBuilder.loadModel();
			final org.palladiosimulator.pcm.system.System system = sysBuilder.getModel();
			
			final AllocationModelBuilder allocationBuilder = new AllocationModelBuilder(this.allocationModelProvider);
			allocationBuilder.loadModel();
			final Allocation allocation = allocationBuilder.getModel();
			
			unconnectedResourceContainer.stream()
				.forEach(unconnectedConatiner -> {
					TNetworkLink.getAsmContextDeployedOnContainer.apply(allocation, unconnectedConatiner).stream()
						.forEach(asmCtx -> {
							TNetworkLink.getResourceContainer.apply(allocation, TNetworkLink.getConnectedAsmCtx.apply(system, asmCtx)).stream()
							.forEach(container -> builder.connectResourceContainer(container, unconnectedConatiner));
						});
				});
		}
		
		AnalysisMain.getInstance().getTimeMemLogger()
			.after(this, this.getId() + TNetworkLink.executionCounter); //TODO testing logger
		TNetworkLink.executionCounter++;
	}
	
	// *****************************************************************
	//
	// *****************************************************************
	
	private static boolean isEqual(final ResourceContainer res1, final ResourceContainer res2) {
		return res1.getId().equals(res2.getId());
	}
	
	private static boolean isEqual(final AssemblyContext asm1, final AssemblyContext asm2) {
		return asm1.getId().equals(asm2.getId());
	}
	
			
	private static final BiFunction<AssemblyContext, AssemblyContext, Boolean> isEqualAsmCtx =
					(asm1, asm2) -> asm1.getId().equals(asm2.getId());
	
	
	// *****************************************************************
	//
	// *****************************************************************
	
	private static final Function<ResourceEnvironment, List<ResourceContainer>> getUnconnectedContainers =
			env -> env.getResourceContainer_ResourceEnvironment().stream()
				.filter(container -> TNetworkLink.getLinks.apply(env, container).isEmpty())
				.collect(Collectors.toList());
	
	private static final BiFunction<ResourceEnvironment, ResourceContainer, List<LinkingResource>> getLinks =
			(env, container) -> env.getLinkingResources__ResourceEnvironment().stream()
				.filter(linking -> TNetworkLink.findFirstLink.apply(linking, container).isPresent())
				.collect(Collectors.toList());
	
	private static final BiFunction<LinkingResource, ResourceContainer, Optional<ResourceContainer>> findFirstLink =
			(linking, container) -> linking.getConnectedResourceContainers_LinkingResource().stream()
				.filter(res -> TNetworkLink.isEqual(res, container))
				.findFirst();
			
	
			
	// *****************************************************************
	//
	// *****************************************************************
			
	private static final BiFunction<Allocation, ResourceContainer, List<AssemblyContext>> getAsmContextDeployedOnContainer =
			(allocation, container) -> allocation.getAllocationContexts_Allocation().stream()
				.filter(ctx -> TNetworkLink.isEqual(ctx.getResourceContainer_AllocationContext(), container))
				.map(ctx -> ctx.getAssemblyContext_AllocationContext())
				.collect(Collectors.toList());
	

	private static final BiFunction<org.palladiosimulator.pcm.system.System, AssemblyContext, List<AssemblyContext>> getConnectedAsmCtx =
			(system, queryAsm) -> system.getConnectors__ComposedStructure().stream()
				.map(connector -> TNetworkLink.tryCast(connector))
				.filter(option -> option.isPresent())
				.map(option -> option.get())
				.filter(connector -> TNetworkLink.isEqual(queryAsm, connector.getRequiringAssemblyContext_AssemblyConnector()))
				.map(connector -> connector.getRequiringAssemblyContext_AssemblyConnector())
				.collect(Collectors.toList());
	
	private static BiFunction<Allocation, List<AssemblyContext>, List<ResourceContainer>> getResourceContainer =
			(allocation, listAsm) -> allocation.getAllocationContexts_Allocation().stream()
				.filter(ctx -> listAsm.stream()
						.filter(asm -> TNetworkLink.isEqual(asm, ctx.getAssemblyContext_AllocationContext())).findAny().isPresent())
				.map(ctx -> ctx.getResourceContainer_AllocationContext())
				.collect(Collectors.toList());
		
	private static final Optional<AssemblyConnector> tryCast(final Connector connector) {
		return connector instanceof AssemblyConnector?Optional.of((AssemblyConnector)connector):Optional.empty();
	}
}
