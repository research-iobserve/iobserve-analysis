package org.iobserve.adaptation.execution;

import java.util.List;

import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.planning.cloudprofile.CloudProvider;
import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.sshj.config.SshjSshClientModule;
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceContainerCloud;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

public abstract class ExecutionScript {

	protected final AdaptationData data;

	public ExecutionScript(AdaptationData data) {
		this.data = data;
	}

	public abstract void execute();

	protected CloudProvider getCloudProviderByName(String name) {
		InitializeModelProviders modelProviders = this.data.getRuntimeGraph().getPcmModels();
		List<CloudProvider> cloudProviders = modelProviders.getCloudProfileModelProvider().getModel()
				.getCloudProviders();
		return cloudProviders.stream().filter(provider -> provider.getName().equalsIgnoreCase(name)).findFirst()
				.orElse(null);
	}

	protected ComputeService getComputeServiceForContainer(ResourceContainer container) {
		ResourceContainerCloud cloudContainer;
		cloudContainer = (ResourceContainerCloud) container;
		CloudProvider provider = this.getCloudProviderByName(cloudContainer.getCloudProvider());
	
		ComputeServiceContext context = ContextBuilder.newBuilder(provider.getName())
				.credentials(provider.getIdentity(), provider.getCredential())
				.modules(ImmutableSet.<Module>of(new SLF4JLoggingModule(), new SshjSshClientModule()))
				.buildView(ComputeServiceContext.class);
	
		ComputeService client = context.getComputeService();
		return client;
	}
}
