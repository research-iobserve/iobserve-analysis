package org.iobserve.adaptation.execution;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.planning.systemadaptation.MigrateAction;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.RunScriptOnNodesException;
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceContainerCloud;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * Action script for migrating an assembly context from one cloud resource
 * container group to another.
 *
 * This action migrates an assembly context from one node group to another. It
 * looks for a script with the name
 * {@link AdaptationData#PRE_MIGRATE_SCRIPT_NAME} in the folder
 * '{$deployablesRepository}/{$assemblyContextComponentName}/' and executes this
 * script on each node of the group to prepare the migration. After that, the
 * script {@link AdaptationData#POST_MIGRATE_SCRIPT_NAME} in the same folder is
 * executed on the target node group to complete the migration.
 *
 * @author Tobias Pöppke
 *
 */
public class MigrateActionScript extends ActionScript {
	private final MigrateAction action;

	/**
	 * Create a new migration action script with the given data.
	 *
	 * @param data
	 *            the data shared in the adaptation stage
	 * @param action
	 *            the action item to be executed
	 */
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

		// If the assembly context has already been migrated, do nothing
		if (!this.data.getMigratedContexts().contains(assemblyContextName)) {
			client.runScriptOnNodesMatching(node -> node.getGroup().equals(sourceCloudContainer.getGroupName()),
					this.getScript(AdaptationData.PRE_MIGRATE_SCRIPT_NAME, this.action.getSourceAssemblyContext()));
			client.runScriptOnNodesMatching(node -> node.getGroup().equals(targetCloudContainer.getGroupName()),
					this.getScript(AdaptationData.POST_MIGRATE_SCRIPT_NAME, this.action.getSourceAssemblyContext()));
			// TODO add possibility to open up ports defined in a config file
			this.data.getMigratedContexts().add(assemblyContextName);
		}
	}

	private String getScript(String scriptName, AssemblyContext assemblyCtx) {
		String assemblyCtxFolderName = this.getAssemblyContextFolderName(assemblyCtx);

		URI deallocationScriptURI = this.data.getDeployablesFolderURI().appendSegment(assemblyCtxFolderName)
				.appendSegment(scriptName);
		try {
			return this.getFileContents(deallocationScriptURI);
		} catch (IllegalArgumentException e) {
			return "";
		}
	}

}
