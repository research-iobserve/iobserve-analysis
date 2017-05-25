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

/**
 * Abstract class representing an execution script to be executed for a specific
 * action.
 *
 * The actions derived from this class have to implement the {@link #execute()}
 * method which executes the corresponding action.
 *
 * @author Tobias Pöppke
 *
 */
public abstract class ActionScript {
	private static final Logger LOG = LogManager.getLogger();

	/** Data that is shared throughout the adaptation stage */
	protected final AdaptationData data;

	/**
	 * Constructs a new action script instance with the specified data.
	 *
	 * @param data
	 *            data shared for adaptation stage
	 */
	public ActionScript(AdaptationData data) {
		this.data = data;
	}

	/**
	 * Executes this action.
	 *
	 * In case of an exception during the execution, the calling class has to
	 * signal the operator to perform the task manually.
	 *
	 * @throws RunNodesException
	 *             if there was an error acquiring or terminating a node
	 * @throws RunScriptOnNodesException
	 *             if there was an error with a script run on a group of nodes
	 * @throws IllegalArgumentException
	 *             if an argument was not correct
	 */
	public abstract void execute() throws RunNodesException, RunScriptOnNodesException, IllegalArgumentException;

	/**
	 * Retrieves the cloud provider with the given name or if not found, returns
	 * null.
	 *
	 * @param name
	 *            the name of the cloud provider
	 * @return the cloud provider, or null
	 */
	protected CloudProvider getCloudProviderByName(String name) {
		InitializeModelProviders modelProviders = this.data.getRuntimeGraph().getPcmModels();
		List<CloudProvider> cloudProviders = modelProviders.getCloudProfileModelProvider().getModel()
				.getCloudProviders();
		return cloudProviders.stream().filter(provider -> provider.getName().equalsIgnoreCase(name)).findFirst()
				.orElse(null);
	}

	/**
	 * Returns a compute service client to use for further queries to the cloud
	 * provider.
	 *
	 * @param container
	 *            the resource container for which to get the compute service
	 * @return the corresponding compute service
	 */
	protected ComputeService getComputeServiceForContainer(ResourceContainerCloud container) {
		CloudProvider provider = this.getCloudProviderByName(container.getCloudProviderName());

		ComputeServiceContext context = ContextBuilder.newBuilder(provider.getName())
				.credentials(provider.getIdentity(), provider.getCredential())
				.modules(ImmutableSet.<Module>of(new SLF4JLoggingModule(), new SshjSshClientModule()))
				.buildView(ComputeServiceContext.class);

		ComputeService client = context.getComputeService();
		return client;
	}

	/**
	 * Reads the file contents of the given file and returns it as a String.
	 *
	 * @param fileURI
	 *            the file to read
	 * @return the contents of the file as a String
	 * @throws IllegalArgumentException
	 *             if there was an error with the file
	 */
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

	/**
	 * Checks whether the given resource container is a cloud container and
	 * returns it as such.
	 *
	 * @param container
	 *            the container to check
	 * @return the cloud resource container
	 * @throws IllegalArgumentException
	 *             if the container was not a valid cloud container
	 */
	protected ResourceContainerCloud getResourceContainerCloud(ResourceContainer container)
			throws IllegalArgumentException {
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

	/**
	 * Returns the name of the folder where artifacts of the given assembly
	 * context are stored.
	 *
	 * @param context
	 *            the needed assembly context
	 * @return the folder name for this context
	 */
	protected String getAssemblyContextFolderName(AssemblyContext context) {
		return context.getEncapsulatedComponent__AssemblyContext().getEntityName();
	}
}
