package org.iobserve.adaptation.execution;

import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.getOnlyElement;
import static org.jclouds.compute.options.TemplateOptions.Builder.runScript;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.planning.systemadaptation.AcquireAction;
import org.iobserve.planning.utils.ModelHelper;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.domain.TemplateBuilder;
import org.jclouds.compute.options.TemplateOptions;
import org.jclouds.scriptbuilder.domain.Statement;
import org.jclouds.scriptbuilder.statements.login.AdminAccess;
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceContainerCloud;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * Action script for acquiring a new cloud resource container.
 *
 * @author Tobias Pöppke
 *
 */
public class AcquireActionScript extends ActionScript {
	private static final Logger LOG = LogManager.getLogger();

	private final AcquireAction action;

	/**
	 * Create a new acquire action script with the given data.
	 *
	 * @param data
	 *            the data shared in the adaptation stage
	 * @param action
	 *            the action item to be executed
	 */
	public AcquireActionScript(AdaptationData data, AcquireAction action) {
		super(data);
		this.action = action;
	}

	@Override
	public void execute() throws RunNodesException {
		ResourceContainer container = this.action.getSourceResourceContainer();

		ResourceContainerCloud cloudContainer = this.getResourceContainerCloud(container);

		ComputeService client = this.getComputeServiceForContainer(cloudContainer);

		TemplateBuilder templateBuilder = client.templateBuilder();

		templateBuilder.hardwareId(cloudContainer.getInstanceType());
		templateBuilder.locationId(cloudContainer.getLocation());
		// TODO maybe make this configurable
		templateBuilder.osFamily(OsFamily.UBUNTU);

		Statement setupAdminInstructions = AdminAccess.standard();

		// Necessary to set hostname to allow mapping for later events
		TemplateOptions options = runScript(setupAdminInstructions).runScript(getChangeHostnameScript(cloudContainer))
				.runScript(this.getStartupScript());
		templateBuilder.options(options);

		Template template = templateBuilder.build();

		String groupName = ModelHelper.getGroupName(cloudContainer);

		NodeMetadata node = getOnlyElement(client.createNodesInGroup(groupName, 1, template));

		LOG.info(String.format("Acquired node for resource container '%s'. NodeID: %s, Hostname: %s, Adresses: %s",
				cloudContainer.getEntityName(), node.getId(), node.getHostname(),
				concat(node.getPrivateAddresses(), node.getPublicAddresses())));

		// TODO write resource container to original model to enable mapping

	}

	private static String getChangeHostnameScript(ResourceContainerCloud cloudContainer) {
		// This only works on a *nix OS and is valid until rebooting the node
		return String.format("hostname %s", cloudContainer.getEntityName());
	}

	private String getStartupScript() {
		URI nodeStartupScriptURI = this.data.getDeployablesFolderURI()
				.appendSegment(AdaptationData.NODE_STARTUP_SCRIPT_NAME);

		try {
			return this.getFileContents(nodeStartupScriptURI);
		} catch (IOException e) {
			// No script found, so we can not execute anything
			LOG.warn("Could not find script for node startup. No script will be executed.");
			return "";
		}
	}

	@Override
	public boolean isAutoExecutable() {
		return true;
	}

	@Override
	public String getDescription() {
		ResourceContainerCloud container = this.getResourceContainerCloud(this.action.getSourceResourceContainer());

		StringBuilder builder = new StringBuilder();
		builder.append("Acquire Action: Acquire container from provider '");
		builder.append(container.getCloudProviderName());
		builder.append("' of type '");
		builder.append(container.getInstanceType());
		builder.append("' in location '");
		builder.append(container.getLocation());
		builder.append("'");
		return builder.toString();

	}
}
