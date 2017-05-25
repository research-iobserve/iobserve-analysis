package org.iobserve.adaptation.execution;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

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

import com.google.common.io.Files;

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

		if (!(container instanceof ResourceContainerCloud)) {
			String error = String.format(
					"ResourceContainer '%s' was not a cloud container, therefore allocation context '%s' can not be deployed!",
					container.getEntityName(), this.action.getNewAllocationContext().getEntityName());
			LOG.error(error);
			throw new IllegalArgumentException(error);
		}

		final ResourceContainerCloud cloudContainer = (ResourceContainerCloud) container;
		ComputeService client = this.getComputeServiceForContainer(cloudContainer);
		String assemblyContextName = this.action.getSourceAssemblyContext().getEntityName();

		// If the assembly context has already been allocated on the group, do
		// nothing
		if (!this.data.getAllocatedContexts().contains(assemblyContextName)) {
			client.runScriptOnNodesMatching(node -> node.getGroup().equals(cloudContainer.getEntityName()),
					this.getAllocateScript(this.action.getSourceAssemblyContext()));
			this.data.getAllocatedContexts().add(cloudContainer.getGroupName());
		}
	}

	private String getAllocateScript(AssemblyContext assemblyCtx) {
		String assemblyCtxFolderName = assemblyCtx.getEncapsulatedComponent__AssemblyContext().getEntityName();

		URI allocationScriptURI = this.data.getDeployablesFolderURI().appendSegment(assemblyCtxFolderName)
				.appendSegment(AdaptationData.ALLOCATE_SCRIPT_NAME);

		if (allocationScriptURI.isFile()) {
			try {
				return Files.toString(new File(allocationScriptURI.toFileString()), StandardCharsets.UTF_8);
			} catch (IOException e) {
				LOG.warn(String.format("Could not read allocation script for assembly context '%s' at '%s'! ",
						assemblyCtx.getEntityName(), allocationScriptURI.toFileString()), e);
			}
		} else {
			LOG.warn(String.format("Allocation script for assembly context '%s' at '%s' is not a file!",
					assemblyCtx.getEntityName(), allocationScriptURI.toFileString()));
		}

		throw new IllegalArgumentException(String.format(
				"Could not allocate assembly context '%s'. Probably an error with file paths for allocation scripts.",
				assemblyCtx.getEntityName()));
	}

}
