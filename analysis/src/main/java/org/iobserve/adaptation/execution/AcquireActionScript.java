package org.iobserve.adaptation.execution;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.planning.systemadaptation.AcquireAction;
import org.jclouds.compute.ComputeService;
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceContainerCloud;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

public class AcquireActionScript extends ExecutionScript {
	private static final Logger LOG = LogManager.getLogger();

	private final AcquireAction action;

	public AcquireActionScript(AdaptationData data, AcquireAction action) {
		super(data);
		this.action = action;
	}

	@Override
	public void execute() {
		ResourceContainer container = this.action.getSourceResourceContainer();
		ResourceContainerCloud cloudContainer = null;

		if (!(container instanceof ResourceContainerCloud)) {
			// TODO notify operator that operation can not be executed
			String error = String.format(
					"ResourceContainer '%s' was not a cloud container, therefore it can not be acquired!",
					container.getEntityName());
			LOG.error(error);
			throw new IllegalArgumentException(error);
		}

		ComputeService client = this.getComputeServiceForContainer(container);

		// client.templateBuilder()
	}
}
