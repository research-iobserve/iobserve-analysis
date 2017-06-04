package org.iobserve.analysis.graph;

import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

public class ModelCollection {

	private Repository repositoryModel;
	private org.palladiosimulator.pcm.system.System systemModel;
	private Allocation allocationModel;
	private ResourceEnvironment resourceEnvironmentModel;

	public ModelCollection(Repository repositoryModel, org.palladiosimulator.pcm.system.System systemModel, Allocation allocationModel,
			ResourceEnvironment resourceEnvironmentModel) {
		super();
		this.repositoryModel = repositoryModel;
		this.systemModel = systemModel;
		this.allocationModel = allocationModel;
		this.resourceEnvironmentModel = resourceEnvironmentModel;
	}

	/**
	 * @return the repositoryModel
	 */
	public Repository getRepositoryModel() {
		return repositoryModel;
	}

	/**
	 * @param repositoryModel
	 *            the repositoryModel to set
	 */
	public void setRepositoryModel(Repository repositoryModel) {
		this.repositoryModel = repositoryModel;
	}

	/**
	 * @return the systemModel
	 */
	public org.palladiosimulator.pcm.system.System getSystemModel() {
		return systemModel;
	}

	/**
	 * @param systemModel
	 *            the systemModel to set
	 */
	public void setSystemModel(org.palladiosimulator.pcm.system.System systemModel) {
		this.systemModel = systemModel;
	}

	/**
	 * @return the allocationModel
	 */
	public Allocation getAllocationModel() {
		return allocationModel;
	}

	/**
	 * @param allocationModel
	 *            the allocationModel to set
	 */
	public void setAllocationModel(Allocation allocationModel) {
		this.allocationModel = allocationModel;
	}

	/**
	 * @return the resourceEnvironmentModel
	 */
	public ResourceEnvironment getResourceEnvironmentModel() {
		return resourceEnvironmentModel;
	}

	/**
	 * @param resourceEnvironmentModel
	 *            the resourceEnvironmentModel to set
	 */
	public void setResourceEnvironmentModel(ResourceEnvironment resourceEnvironmentModel) {
		this.resourceEnvironmentModel = resourceEnvironmentModel;
	}

}
