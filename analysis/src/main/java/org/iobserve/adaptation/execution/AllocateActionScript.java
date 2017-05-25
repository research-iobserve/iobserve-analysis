package org.iobserve.adaptation.execution;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.planning.systemadaptation.AllocateAction;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.RunScriptOnNodesException;
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceContainerCloud;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

public class AllocateActionScript extends ActionScript {
	private static final Logger LOG = LogManager.getLogger();

	private final AllocateAction action;

	public AllocateActionScript(AdaptationData data, AllocateAction action) {
		super(data);
		this.action = action;
	}

	@Override
	public void execute() throws RunScriptOnNodesException {
		ResourceContainer container = this.action.getNewAllocationContext().getResourceContainer_AllocationContext();

		final ResourceContainerCloud cloudContainer = this.getResourceContainerCloud(container);

		ComputeService client = this.getComputeServiceForContainer(cloudContainer);
		String assemblyContextName = this.action.getSourceAssemblyContext().getEntityName();

		// If the assembly context has already been allocated on the group, do
		// nothing
		if (!this.data.getAllocatedContexts().contains(assemblyContextName)) {
			client.runScriptOnNodesMatching(node -> node.getGroup().equals(cloudContainer.getGroupName()),
					this.getAllocateScript(this.action.getSourceAssemblyContext()));
			this.data.getAllocatedContexts().add(assemblyContextName);
		}
	}

	private String getAllocateScript(AssemblyContext assemblyCtx) {
		String assemblyCtxFolderName = this.getAssemblyContextFolderName(assemblyCtx);

		URI allocationScriptURI = this.data.getDeployablesFolderURI().appendSegment(assemblyCtxFolderName)
				.appendSegment(AdaptationData.ALLOCATE_SCRIPT_NAME);

		return this.getFileContents(allocationScriptURI);
	}

}
