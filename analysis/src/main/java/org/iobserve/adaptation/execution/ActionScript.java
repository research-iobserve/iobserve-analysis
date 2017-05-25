package org.iobserve.adaptation.execution;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.planning.cloudprofile.CloudProvider;
import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.RunScriptOnNodesException;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.sshj.config.SshjSshClientModule;
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceContainerCloud;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

import com.google.common.collect.ImmutableSet;
import com.google.common.io.Files;
import com.google.inject.Module;

public abstract class ActionScript {
	private static final Logger LOG = LogManager.getLogger();

	protected final AdaptationData data;

	public ActionScript(AdaptationData data) {
		this.data = data;
	}

	public abstract void execute() throws RunNodesException, RunScriptOnNodesException;

	protected CloudProvider getCloudProviderByName(String name) {
		InitializeModelProviders modelProviders = this.data.getRuntimeGraph().getPcmModels();
		List<CloudProvider> cloudProviders = modelProviders.getCloudProfileModelProvider().getModel()
				.getCloudProviders();
		return cloudProviders.stream().filter(provider -> provider.getName().equalsIgnoreCase(name)).findFirst()
				.orElse(null);
	}

	protected ComputeService getComputeServiceForContainer(ResourceContainerCloud container) {
		CloudProvider provider = this.getCloudProviderByName(container.getCloudProviderName());

		ComputeServiceContext context = ContextBuilder.newBuilder(provider.getName())
				.credentials(provider.getIdentity(), provider.getCredential())
				.modules(ImmutableSet.<Module>of(new SLF4JLoggingModule(), new SshjSshClientModule()))
				.buildView(ComputeServiceContext.class);

		ComputeService client = context.getComputeService();
		return client;
	}

	protected String getFileContents(URI fileURI) throws IllegalArgumentException {
		if (fileURI.isFile()) {
			try {
				return Files.toString(new File(fileURI.toFileString()), StandardCharsets.UTF_8);
			} catch (IOException e) {
				String msg = String.format("Could not read file at '%s'! ", fileURI.toFileString());
				LOG.warn(msg, e);
				throw new IllegalArgumentException(msg, e);
			}
		} else {
			String msg = String.format("File at '%s' is not a valid file! Path is probably wrong.",
					fileURI.toFileString());
			LOG.warn(msg);
			throw new IllegalArgumentException(msg);
		}
	}

	protected ResourceContainerCloud getResourceContainerCloud(ResourceContainer container) {
		if (!(container instanceof ResourceContainerCloud)) {
			String error = String.format(
					"ResourceContainer '%s' was not a cloud container, therefore no action is possible on it!",
					container.getEntityName());
			LOG.error(error);
			throw new IllegalArgumentException(error);
		}

		final ResourceContainerCloud cloudContainer = (ResourceContainerCloud) container;
		return cloudContainer;
	}

	protected String getAssemblyContextFolderName(AssemblyContext context) {
		return context.getEncapsulatedComponent__AssemblyContext().getEntityName();
	}
}
