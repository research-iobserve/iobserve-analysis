package org.iobserve.adaptation.execution;

import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.getOnlyElement;
import static org.jclouds.compute.options.TemplateOptions.Builder.runScript;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.planning.systemadaptation.AcquireAction;
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

public class AcquireActionScript extends ActionScript {
	private static final Logger LOG = LogManager.getLogger();

	private final AcquireAction action;

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

		NodeMetadata node = getOnlyElement(client.createNodesInGroup(cloudContainer.getGroupName(), 1, template));

		LOG.info(String.format("Acquired node for resource container '%s'. NodeID: %s, Hostname: %s, Adresses: %s",
				cloudContainer.getEntityName(), node.getId(), node.getHostname(),
				concat(node.getPrivateAddresses(), node.getPublicAddresses())));

		// TODO write resource container to original model to enable mapping

	}

	private static String getChangeHostnameScript(ResourceContainerCloud cloudContainer) {
		// This only works on a *nix OS and is valid until rebooting the node
		return String.format("hostname %s", cloudContainer.getId());
	}

	private String getStartupScript() {
		URI nodeStartupScriptURI = this.data.getDeployablesFolderURI()
				.appendSegment(AdaptationData.NODE_STARTUP_SCRIPT_NAME);

		try {
			return this.getFileContents(nodeStartupScriptURI);
		} catch (IllegalArgumentException e) {
			return "";
		}
	}
}
