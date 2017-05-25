package org.iobserve.adaptation.execution;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.planning.systemadaptation.MigrateAction;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.RunScriptOnNodesException;
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceContainerCloud;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

public class MigrateActionScript extends ActionScript {
	private static final Logger LOG = LogManager.getLogger();

	private final MigrateAction action;

	public MigrateActionScript(AdaptationData data, MigrateAction action) {
		super(data);
		this.action = action;
	}

	@Override
	public void execute() throws RunScriptOnNodesException {
		ResourceContainer sourceContainer = this.action.getSourceAllocationContext()
				.getResourceContainer_AllocationContext();
		ResourceContainer targetContainer = this.action.getNewAllocationContext()
				.getResourceContainer_AllocationContext();

		ResourceContainerCloud sourceCloudContainer = this.getResourceContainerCloud(sourceContainer);
		ResourceContainerCloud targetCloudContainer = this.getResourceContainerCloud(targetContainer);

		ComputeService client = this.getComputeServiceForContainer(sourceCloudContainer);
		String assemblyContextName = this.action.getSourceAssemblyContext().getEntityName();

		// If the assembly context has already been allocated on the group, do
		// nothing
		if (!this.data.getMigratedContexts().contains(assemblyContextName)) {
			client.runScriptOnNodesMatching(node -> node.getGroup().equals(sourceCloudContainer.getGroupName()),
					this.getScript(AdaptationData.PRE_MIGRATE_SCRIPT_NAME, this.action.getSourceAssemblyContext()));
			client.runScriptOnNodesMatching(node -> node.getGroup().equals(targetCloudContainer.getGroupName()),
					this.getScript(AdaptationData.POST_MIGRATE_SCRIPT_NAME, this.action.getSourceAssemblyContext()));
			this.data.getMigratedContexts().add(assemblyContextName);
		}
	}

	private String getScript(String scriptName, AssemblyContext assemblyCtx) {
		String assemblyCtxFolderName = this.getAssemblyContextFolderName(assemblyCtx);

		URI deallocationScriptURI = this.data.getDeployablesFolderURI().appendSegment(assemblyCtxFolderName)
				.appendSegment(scriptName);

		return this.getFileContents(deallocationScriptURI);
	}

}
