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

import org.iobserve.analysis.model.CloudProfileModelProvider;
import org.iobserve.analysis.model.CostModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelBuilder;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.utils.Opt;
import org.iobserve.common.record.EJBDeployedEvent;
import org.iobserve.common.record.IDeploymentRecord;
import org.iobserve.common.record.ServletDeployedEvent;
import org.iobserve.planning.utils.ModelHelper;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * TAllocation creates a new resource container if and only if there is no
 * corresponding container already available.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 * @author Tobias Pöppke
 */
public final class TAllocation extends AbstractConsumerStage<IDeploymentRecord> {

	/** reference to {@link ResourceEnvironment} provider. */
	private final ResourceEnvironmentModelProvider resourceEnvModelProvider;

	private final CloudProfileModelProvider cloudProfileModelProvider;

	private final CostModelProvider costModelProvider;
	/** output port. */
	private final OutputPort<IDeploymentRecord> deploymentOutputPort = this.createOutputPort();

	/**
	 * Most likely the constructor needs an additional field for the PCM access.
	 * But this has to be discussed with Robert.
	 *
	 * @param resourceEvnironmentModelProvider
	 *            the resource environment model provider
	 */
	public TAllocation(final ResourceEnvironmentModelProvider resourceEvnironmentModelProvider,
			final CloudProfileModelProvider cloudProfileModelProvider, final CostModelProvider costModelProvider) {
		this.resourceEnvModelProvider = resourceEvnironmentModelProvider;
		this.cloudProfileModelProvider = cloudProfileModelProvider;
		this.costModelProvider = costModelProvider;
	}

	/**
	 * @return the deploymentOutputPort
	 */
	public OutputPort<IDeploymentRecord> getDeploymentOutputPort() {
		return this.deploymentOutputPort;
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
			final String serivce = ((ServletDeployedEvent) event).getSerivce();
			this.updateModel(serivce);
		} else if (event instanceof EJBDeployedEvent) {
			final String service = ((EJBDeployedEvent) event).getSerivce();
			this.updateModel(service);
		}

		// forward the event
		this.deploymentOutputPort.send(event);
	}

	/**
	 * Update the allocation model with the given server-name if necessary.
	 *
	 * @param serverName
	 *            server name
	 */
	private void updateModel(final String serverName) {
		Opt.of(ResourceEnvironmentModelBuilder.getResourceContainerByName(this.resourceEnvModelProvider.getModel(),
				serverName)).ifNotPresent().apply(() -> {
					this.createNewResourceContainer(serverName);
				});
	}

	private void createNewResourceContainer(final String serverName) {
		ResourceContainer cloudContainer = ModelHelper.getResourceContainerFromHostname(this.resourceEnvModelProvider,
				this.costModelProvider, this.cloudProfileModelProvider, serverName);

		if (cloudContainer == null) {
			this.resourceEnvModelProvider.loadModel();
			final ResourceEnvironment model = TAllocation.this.resourceEnvModelProvider.getModel();
			ResourceEnvironmentModelBuilder.createResourceContainer(model, serverName);
		}

		this.resourceEnvModelProvider.save();
	}
}
