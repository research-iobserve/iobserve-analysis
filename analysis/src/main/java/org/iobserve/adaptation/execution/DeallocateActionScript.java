package org.iobserve.adaptation.execution;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.planning.systemadaptation.DeallocateAction;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.RunScriptOnNodesException;
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceContainerCloud;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

import com.google.common.io.Files;

public class DeallocateActionScript extends ActionScript {
	private static final Logger LOG = LogManager.getLogger();

	private final DeallocateAction action;

	public DeallocateActionScript(AdaptationData data, DeallocateAction action) {
		super(data);
		this.action = action;
	}

	@Override
	public void execute() throws RunScriptOnNodesException {
		ResourceContainer container = this.action.getOldAllocationContext().getResourceContainer_AllocationContext();

		if (!(container instanceof ResourceContainerCloud)) {
			String error = String.format(
					"ResourceContainer '%s' was not a cloud container, therefore allocation context '%s' can not be undeployed!",
					container.getEntityName(), this.action.getOldAllocationContext().getEntityName());
			LOG.error(error);
			throw new IllegalArgumentException(error);
		}

		final ResourceContainerCloud cloudContainer = (ResourceContainerCloud) container;
		ComputeService client = this.getComputeServiceForContainer(cloudContainer);
		String assemblyContextName = this.action.getSourceAssemblyContext().getEntityName();

		// If the assembly context has already been deallocated on the group, do
		// nothing
		if (!this.data.getDeallocatedContexts().contains(assemblyContextName)) {
			client.runScriptOnNodesMatching(node -> node.getGroup().equals(cloudContainer.getEntityName()),
					this.getDeallocateScript(this.action.getSourceAssemblyContext()));
			this.data.getDeallocatedContexts().add(cloudContainer.getGroupName());
		}
	}

	private String getDeallocateScript(AssemblyContext assemblyCtx) {
		String assemblyCtxFolderName = assemblyCtx.getEncapsulatedComponent__AssemblyContext().getEntityName();

		URI deallocationScriptURI = this.data.getDeployablesFolderURI().appendSegment(assemblyCtxFolderName)
				.appendSegment(AdaptationData.DEALLOCATE_SCRIPT_NAME);

		if (deallocationScriptURI.isFile()) {
			try {
				return Files.toString(new File(deallocationScriptURI.toFileString()), StandardCharsets.UTF_8);
			} catch (IOException e) {
				LOG.warn(String.format("Could not read deallocation script for assembly context '%s' at '%s'! ",
						assemblyCtx.getEntityName(), deallocationScriptURI.toFileString()), e);
			}
		} else {
			LOG.warn(String.format("Deallocation script for assembly context '%s' at '%s' is not a file!",
					assemblyCtx.getEntityName(), deallocationScriptURI.toFileString()));
		}

		throw new IllegalArgumentException(String.format(
				"Could not deallocate assembly context '%s'. Probably an error with file paths for deallocation scripts.",
				assemblyCtx.getEntityName()));
	}

}
