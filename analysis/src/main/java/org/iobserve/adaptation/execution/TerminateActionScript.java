package org.iobserve.adaptation.execution;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.planning.systemadaptation.TerminateAction;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.RunScriptOnNodesException;
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceContainerCloud;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * Action script for terminating a group of cloud resource containers.
 *
 * @author Tobias Pöppke
 *
 */
public class TerminateActionScript extends ActionScript {

	private final TerminateAction action;

	/**
	 * Create a new terminate action script with the given data.
	 *
	 * @param data
	 *            the data shared in the adaptation stage
	 * @param action
	 *            the action item to be executed
	 */
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
