package org.iobserve.adaptation.execution;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.planning.systemadaptation.TerminateAction;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.RunScriptOnNodesException;
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceContainerCloud;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

public class TerminateActionScript extends ActionScript {
	private static final Logger LOG = LogManager.getLogger();

	private final TerminateAction action;

	public TerminateActionScript(AdaptationData data, TerminateAction action) {
		super(data);
		this.action = action;
	}

	@Override
	public void execute() throws RunScriptOnNodesException {
		ResourceContainer container = this.action.getSourceResourceContainer();

		ResourceContainerCloud cloudContainer = this.getResourceContainerCloud(container);

		ComputeService client = this.getComputeServiceForContainer(cloudContainer);

		// If the container group has already been terminated, do nothing
		if (!this.data.getTerminatedGroups().contains(cloudContainer.getGroupName())) {
			client.runScriptOnNodesMatching(node -> node.getGroup().equals(cloudContainer.getGroupName()),
					this.getScript(AdaptationData.NODE_PRE_TERMINATE_SCRIPT_NAME));
			client.destroyNodesMatching(node -> node.getGroup().equals(cloudContainer.getGroupName()));
			this.data.getTerminatedGroups().add(cloudContainer.getGroupName());
		}
	}

	private String getScript(String scriptName) {
		URI deallocationScriptURI = this.data.getDeployablesFolderURI().appendSegment(scriptName);

		return this.getFileContents(deallocationScriptURI);
	}

}
