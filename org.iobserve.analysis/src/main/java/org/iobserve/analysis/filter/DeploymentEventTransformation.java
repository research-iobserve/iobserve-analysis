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
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.AnalysisMain;
import org.iobserve.analysis.correspondence.ICorrespondence;
import org.iobserve.analysis.modelprovider.AllocationModelProvider;
import org.iobserve.analysis.modelprovider.PcmModelSaver;
import org.iobserve.analysis.modelprovider.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.modelprovider.SystemModelProvider;
import org.iobserve.common.record.EJBDeployedEvent;
import org.iobserve.common.record.IDeploymentRecord;
import org.iobserve.common.record.ServletDeployedEvent;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

import teetime.framework.AbstractConsumerStage;

/**
 * It could be interesting to combine DeploymentEventTransformation and UndeploymentEventTransformation.
 * However, that would require two input ports. And I have not used the API for multiple input ports.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 */
public class DeploymentEventTransformation extends AbstractConsumerStage<IDeploymentRecord> {

	private static long executionCounter = 0;
	private final ICorrespondence correspondence;
	private List<DeploymentRecordProcessor> deploymentProcessors;
	private AllocationModelProvider allocationModelProvider;
	private SystemModelProvider systemModelProvider;
	private ResourceEnvironmentModelProvider resourceEnvModelProvider;

	/**
	 * Most likely the constructor needs an additional field for the PCM access. But this has to be discussed with Robert.
	 *
	 * @param correspondence
	 *            the correspondence model access
	 */
	public DeploymentEventTransformation(final ICorrespondence correspondence) {
		this.correspondence = correspondence;
		
		// add processors
		this.deploymentProcessors = new ArrayList<>();
		this.deploymentProcessors.add(new EJBDeployedEventProcessor());
		this.deploymentProcessors.add(new ServletDeployedEventProcessor());
	}

	/**
	 * This method is triggered for every deployment event.
	 *
	 * @param event
	 *            one deployment event to be processed
	 */
	@Override
	protected void execute(final IDeploymentRecord event) {
		
		if (this.systemModelProvider == null) {
			final URI systemModelUri = URI.createURI(AnalysisMain.getInstance().getInputParameter().getPathPcmSystemModel());
			this.systemModelProvider = new SystemModelProvider(systemModelUri);
		}
		
		if (this.resourceEnvModelProvider == null) {
			final URI resEnvModelUri = URI.createURI(AnalysisMain.getInstance().getInputParameter().getPathPcmResEnvModel());
			this.resourceEnvModelProvider = new ResourceEnvironmentModelProvider(resEnvModelUri);
		}
		
		if (this.allocationModelProvider == null) {
			// create model provider
			final URI allocationModelUri = URI.createURI(AnalysisMain.getInstance().getInputParameter().getPathPcmAllocationModel());
			this.allocationModelProvider = new AllocationModelProvider(allocationModelUri);
			this.allocationModelProvider.resetModel();
		}
		
		
		// dispatch event to right processor
		boolean stopDispatching = false;
		final Iterator<DeploymentRecordProcessor> iterator = this.deploymentProcessors.iterator();
		do {
			stopDispatching = iterator.next().processEvent(event);
		} while (!stopDispatching && iterator.hasNext());
	}

	// *****************************************************************
	// PROCESS EVENT MORE SPECIFIC
	// *****************************************************************
	
	/**
	 * Abstract type of a processor for {@link IDeploymentRecord}.
	 * 
	 * @author Robert Heinrich
	 * @author Alessandro Giusa
	 *
	 */
	private abstract class DeploymentRecordProcessor {
		public abstract boolean processEvent(final IDeploymentRecord record);
	}

	/**
	 * Processor for processing {@link ServletDeployedEvent}
	 * @author Robert Heinrich
	 * @author Alessandro Giusa
	 *
	 */
	private class ServletDeployedEventProcessor extends DeploymentRecordProcessor {

		@Override
		public boolean processEvent(final IDeploymentRecord record) {
			if (record.getClass() != ServletDeployedEvent.class) {
				return false;
			}
			
			final ServletDeployedEvent event = (ServletDeployedEvent) record;
			final String context = event.getContext();
			final String deploymentId = event.getDeploymentId();
			final String serivce = event.getSerivce();
			final long loggingTimestamp = event.getLoggingTimestamp();
			final long timestamp = event.getTimestamp();
			
			//TODO debug
			final String debugContext = "Application.ProductDispatcher_EnterpriseServer";
			final String debugResContainer = debugContext.substring(debugContext.lastIndexOf("_") + 1, debugContext.length());
			
			final ResourceContainer resourceContainer = DeploymentEventTransformation.this.resourceEnvModelProvider.getResourceContainerByName(debugResContainer);
			final AssemblyContext assemblyContext = DeploymentEventTransformation.this.systemModelProvider.getAssemblyContextByName("Application.ProductDispatcher_EnterpriseServer");
			DeploymentEventTransformation.this.allocationModelProvider.addAllocationContext(resourceContainer, assemblyContext);
			
			final PcmModelSaver modelSaver = new PcmModelSaver(URI.createURI("output/updated_allocationmodel.allocation"));
			modelSaver.save(DeploymentEventTransformation.this.allocationModelProvider.getModel());
			
			
			return true;
		}

	}

	/**
	 * Processor for processing {@link EJBDeployedEvent}
	 * @author Robert Heinrich
	 * @author Alessandro Giusa
	 *
	 */
	private class EJBDeployedEventProcessor extends DeploymentRecordProcessor {

		@Override
		public boolean processEvent(final IDeploymentRecord record) {
			if (record.getClass() != EJBDeployedEvent.class) {
				return false;
			}
			
			final EJBDeployedEvent event = (EJBDeployedEvent) record;

			System.out
				.println("DeploymentEventTransformation.EJBDeployedEventProcessor.processEvent()");
			return true;
		}

	}


}
