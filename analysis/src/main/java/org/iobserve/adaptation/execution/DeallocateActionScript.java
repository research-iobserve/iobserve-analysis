package org.iobserve.adaptation.execution;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.planning.systemadaptation.DeallocateAction;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.RunScriptOnNodesException;
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceContainerCloud;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * Action script for a deallocation action.
 *
 * This action undeploys an assembly context off a node group. It looks for a
 * script with the name {@link AdaptationData#DEALLOCATE_SCRIPT_NAME} in the
 * folder '{$deployablesRepository}/{$assemblyContextComponentName}/' and
 * executes this script on each node of the group to undeploy the assembly
 * context.
 *
 * @author Tobias Pöppke
 *
 */
public class DeallocateActionScript extends ActionScript {
	private final DeallocateAction action;

	/**
	 * Create a new deallocate action script with the given data.
	 *
	 * @param data
	 *            the data shared in the adaptation stage
	 * @param action
	 *            the action item to be executed
	 */
	public DeallocateActionScript(AdaptationData data, DeallocateAction action) {
		super(data);
		this.action = action;
	}

	@Override
	public void execute() throws RunScriptOnNodesException {
		ResourceContainer container = this.action.getOldAllocationContext().getResourceContainer_AllocationContext();

		final ResourceContainerCloud cloudContainer = this.getResourceContainerCloud(container);

		ComputeService client = this.getComputeServiceForContainer(cloudContainer);
		String assemblyContextName = this.action.getSourceAssemblyContext().getEntityName();

		// If the assembly context has already been deallocated on the group, do
		// nothing
		if (!this.data.getDeallocatedContexts().contains(assemblyContextName)) {
			client.runScriptOnNodesMatching(node -> node.getGroup().equals(cloudContainer.getGroupName()),
					this.getDeallocateScript(this.action.getSourceAssemblyContext()));
			this.data.getDeallocatedContexts().add(assemblyContextName);
		}
	}

	private String getDeallocateScript(AssemblyContext assemblyCtx) {
		String assemblyCtxFolderName = this.getAssemblyContextFolderName(assemblyCtx);

		URI deallocationScriptURI = this.data.getDeployablesFolderURI().appendSegment(assemblyCtxFolderName)
				.appendSegment(AdaptationData.DEALLOCATE_SCRIPT_NAME);

		return this.getFileContents(deallocationScriptURI);
	}

}
