package org.iobserve.adaptation.execution;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.planning.systemadaptation.DeallocateAction;
import org.iobserve.planning.utils.ModelHelper;
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
	public void execute() throws RunScriptOnNodesException, IOException {
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

	private String getDeallocateScript(AssemblyContext assemblyCtx) throws IOException {
		String assemblyCtxFolderName = this.getAssemblyContextFolderName(assemblyCtx);

		URI deallocationScriptURI = this.data.getDeployablesFolderURI().appendSegment(assemblyCtxFolderName)
				.appendSegment(AdaptationData.DEALLOCATE_SCRIPT_NAME);

		return this.getFileContents(deallocationScriptURI);
	}

	@Override
	public boolean isAutoExecutable() {
		return true;
	}

	@Override
	public String getDescription() {
		ResourceContainerCloud sourceContainer = this.getResourceContainerCloud(
				this.action.getOldAllocationContext().getResourceContainer_AllocationContext());

		StringBuilder builder = new StringBuilder();
		builder.append("Deallocate Action: Deallocate assembly context '");
		builder.append(this.action.getSourceAssemblyContext().getEntityName());
		builder.append("' from container of provider '");
		builder.append(sourceContainer.getInstanceType().getProvider().getName());
		builder.append("' of type '");
		builder.append(sourceContainer.getInstanceType());
		builder.append("' in location '");
		builder.append(sourceContainer.getInstanceType().getLocation());
		builder.append("' with name '");
		builder.append(ModelHelper.getGroupName(sourceContainer));
		builder.append("'");
		return builder.toString();
	}

}
