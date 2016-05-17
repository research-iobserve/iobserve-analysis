package org.iobserve.analysis.model;

import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;

public class ResourceEnvironmentModelBuilder extends ModelBuilder<ResourceEnvironmentModelProvider, ResourceEnvironment> {

	public ResourceEnvironmentModelBuilder(final ResourceEnvironmentModelProvider modelToStartWith) {
		super(modelToStartWith);
	}

	@Override
	public ResourceEnvironment build() {
		return this.modelProvider.getModel();
	}
	
	// *****************************************************************
	//
	// *****************************************************************

	public ResourceEnvironmentModelBuilder save(final ModelSaveStrategy saveStrategy) {
		this.modelProvider.save(saveStrategy);
		return this;
	}
	
	public ResourceEnvironmentModelBuilder loadModel() {
		this.modelProvider.loadModel();
		return this;
	}
	
	public ResourceEnvironmentModelBuilder resetModel() {
		final ResourceEnvironment model = this.modelProvider.getModel();
		model.getResourceContainer_ResourceEnvironment().clear();
		model.getLinkingResources__ResourceEnvironment().clear();
		return this;
	}
	
	public ResourceEnvironmentModelBuilder createResourceContainerIfAbsent(final String name) {
		final boolean absent = this.modelProvider.getResourceContainerByName(name) == null;
		if (absent) {
			final ResourceEnvironment model = this.modelProvider.getModel();
			final ResourceContainer resContainer = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
			resContainer.setEntityName(name);
			model.getResourceContainer_ResourceEnvironment().add(resContainer);
		}
		return this;
	}
	
	/**
	 * Create a {@link ResourceContainer} with the given name, without checking if it already
	 * exists. Use {@link #createResourceContainerIfAbsent(String)} instead if you wont create
	 * the container if it is already available.
	 * @param name name of the new container
	 * @return builder
	 */
	public ResourceEnvironmentModelBuilder createResourceContainer(final String name) {
		final ResourceEnvironment model = this.modelProvider.getModel();
		final ResourceContainer resContainer = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
		resContainer.setEntityName(name);
		model.getResourceContainer_ResourceEnvironment().add(resContainer);
		return this;
	}

}
