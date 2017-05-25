package org.iobserve.adaptation.execution;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.planning.systemadaptation.AllocateAction;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.RunScriptOnNodesException;
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceContainerCloud;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * Action script for an allocation action.
 *
 * This action deploys an assembly context onto a node group. It looks for a
 * script with the name {@link AdaptationData#ALLOCATE_SCRIPT_NAME} in the
 * folder '{$deployablesRepository}/{$assemblyContextComponentName}/' and
 * executes this script on each node of the group to deploy the assembly
 * context.
 *
 * @author Tobias Pöppke
 *
 */
public class AllocateActionScript extends ActionScript {
	private final AllocateAction action;

	/**
	 * Create a new allocate action script with the given data.
	 *
	 * @param data
	 *            the data shared in the adaptation stage
	 * @param action
	 *            the action item to be executed
	 */
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
			// TODO add possibility to open up ports defined in a config file
		}
	}

	private String getAllocateScript(AssemblyContext assemblyCtx) {
		String assemblyCtxFolderName = this.getAssemblyContextFolderName(assemblyCtx);

		URI allocationScriptURI = this.data.getDeployablesFolderURI().appendSegment(assemblyCtxFolderName)
				.appendSegment(AdaptationData.ALLOCATE_SCRIPT_NAME);

		return this.getFileContents(allocationScriptURI);
	}

}
