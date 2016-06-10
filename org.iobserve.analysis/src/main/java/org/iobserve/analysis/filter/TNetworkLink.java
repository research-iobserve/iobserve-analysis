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
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.ModelProviderPlatform;
import org.iobserve.analysis.model.ResourceEnvironmentModelBuilder;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelProvider;
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
		System.out.println("TNetworkLink.execute()");
		
		final ResourceEnvironmentModelBuilder builder = 
					new ResourceEnvironmentModelBuilder(this.resourceEnvModelProvider);
		builder.loadModel();
		
		final ResourceEnvironment resourceEnvironment = builder.getModel();
		final List<ResourceContainer> unconnectedResourceContainer = TNetworkLink.getUnconnectedContainers.apply(resourceEnvironment);
		
		
		
		AnalysisMain.getInstance().getTimeMemLogger()
			.after(this, this.getId() + TNetworkLink.executionCounter); //TODO testing logger
		TNetworkLink.executionCounter++;
	}
	
	
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
				.filter(res -> res.getId().equals(container.getId()))
				.findFirst();
	
}
