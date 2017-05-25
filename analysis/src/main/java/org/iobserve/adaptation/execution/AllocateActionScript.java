package org.iobserve.adaptation.execution;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.planning.systemadaptation.AllocateAction;
import org.jclouds.compute.ComputeService;
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceContainerCloud;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

public class AllocateActionScript extends ExecutionScript {
	private static final Logger LOG = LogManager.getLogger();

	private final AllocateAction action;

	public AllocateActionScript(AdaptationData data, AllocateAction action) {
		super(data);
		this.action = action;
	}

	@Override
	public void execute() {
		ResourceContainer container = this.action.getNewAllocationContext().getResourceContainer_AllocationContext();
		ResourceContainerCloud cloudContainer = null;

		if (!(container instanceof ResourceContainerCloud)) {
			String error = String.format(
					"ResourceContainer '%s' was not a cloud container, therefore allocation context '%s' can not be deployed!",
					container.getEntityName(), this.action.getNewAllocationContext().getEntityName());
			LOG.error(error);
			throw new IllegalArgumentException(error);
		}

		cloudContainer = (ResourceContainerCloud) container;
		ComputeService client = this.getComputeServiceForContainer(cloudContainer);

	}

}
