package org.iobserve.adaptation.execution;

import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.planning.systemadaptation.ReplicateAction;
import org.iobserve.planning.utils.ModelHelper;
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceContainerCloud;

/**
 * Action script for replicating an assembly context.
 *
 * Currently not supported automatically, so the execute method will raise an
 * exception to trigger operator interaction.
 *
 * @author Tobias Pöppke
 *
 */
public class ReplicateActionScript extends ActionScript {

	private final ReplicateAction action;

	/**
	 * Create a new replicate action script with the given data.
	 *
	 * @param data
	 *            the data shared in the adaptation stage
	 * @param action
	 *            the action item to be executed
	 */
	public ReplicateActionScript(AdaptationData data, ReplicateAction action) {
		super(data);
		this.action = action;
	}

	@Override
	public void execute() {
		throw new UnsupportedOperationException(
				"Automated replication is currently not possible. Please use the acquire and allocate actions to replicate components!");
	}

	@Override
	public boolean isAutoExecutable() {
		return false;
	}

	@Override
	public String getDescription() {
		ResourceContainerCloud sourceContainer = this
				.getResourceContainerCloud(this.action.getSourceResourceContainer());
		ResourceContainerCloud targetContainer = this.getResourceContainerCloud(this.action.getNewResourceContainer());

		StringBuilder builder = new StringBuilder();
		builder.append("Replicate Action: Replicate container from provider '");
		builder.append(sourceContainer.getCloudProviderName());
		builder.append("' of type '");
		builder.append(sourceContainer.getInstanceType());
		builder.append("' in location '");
		builder.append(sourceContainer.getLocation());
		builder.append("' with name '");
		builder.append(ModelHelper.getGroupName(sourceContainer));
		builder.append(" to container from provider '");
		builder.append(targetContainer.getCloudProviderName());
		builder.append("' of type '");
		builder.append(targetContainer.getInstanceType());
		builder.append("' in location '");
		builder.append(targetContainer.getLocation());
		builder.append("' with name '");
		builder.append(ModelHelper.getGroupName(targetContainer));
		return builder.toString();
	}

}
