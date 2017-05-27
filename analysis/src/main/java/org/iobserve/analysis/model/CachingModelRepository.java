package org.iobserve.analysis.model;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.iobserve.planning.changegroup.ChangeGroupRepository;

/**
 *
 * @author Tobias Pöppke
 *
 *         TODO use caching
 *
 */
public class CachingModelRepository implements IModelRepository {
	private static final String COST_EXTENSION = "cost";
	private static final String CHANGEGROUP_EXTENSION = "changegroup";
	private static final String DESIGNDECISION_EXTENSION = "designdecision";
	private static final String CLOUDPROFILE_EXTENSION = "cloudprofile";
	private static final String USAGE_EXTENSION = "usagemodel";
	private static final String SYSTEM_EXTENSION = "system";
	private static final String REPOSITORY_EXTENSION = "repository";
	private static final String RESOURCEENVIRONMENT_EXTENSION = "resourceenvironment";
	private static final String ALLOCATION_EXTENSION = "allocation";

	private static final Logger LOG = LogManager.getLogger();

	private URI modelDir;
	private String modelName;

	@Override
	public void initialize(URI modelDir, String modelName) {
		this.modelDir = modelDir;
		this.modelName = modelName;
	}

	@Override
	public AllocationModelProvider getAllocationModelProvider() {
		return new AllocationModelProvider(this.getBaseModelURI().appendFileExtension(ALLOCATION_EXTENSION));
	}

	@Override
	public ResourceEnvironmentModelProvider getResourceEnvironmentModelProvider() {
		return new ResourceEnvironmentCloudModelProvider(
				this.getBaseModelURI().appendFileExtension(RESOURCEENVIRONMENT_EXTENSION));
	}

	@Override
	public RepositoryModelProvider getRepositoryModelProvider() {
		return new RepositoryModelProvider(this.getBaseModelURI().appendFileExtension(REPOSITORY_EXTENSION));
	}

	@Override
	public SystemModelProvider getSystemModelProvider() {
		return new SystemModelProvider(this.getBaseModelURI().appendFileExtension(SYSTEM_EXTENSION));
	}

	@Override
	public UsageModelProvider getUsageModelProvider() {
		return new UsageModelProvider(this.getBaseModelURI().appendFileExtension(USAGE_EXTENSION));
	}

	@Override
	public CloudProfileModelProvider getCloudProfileModelProvider() {
		URI cloudProfile = this.getBaseModelURI().appendFileExtension(CLOUDPROFILE_EXTENSION);
		LOG.info("Cloud Profile URI: " + cloudProfile.toString());
		return new CloudProfileModelProvider(this.getBaseModelURI().appendFileExtension(CLOUDPROFILE_EXTENSION));
	}

	@Override
	public DesignDecisionModelProvider getDesignDecisionModelProvider() {
		return new DesignDecisionModelProvider(this.getBaseModelURI().appendFileExtension(DESIGNDECISION_EXTENSION));
	}

	@Override
	public ChangeGroupModelProvider getChangeGroupModelProvider() throws IOException {
		URI modelFileURI = this.getBaseModelURI().appendFileExtension(CHANGEGROUP_EXTENSION);
		File model = new File(modelFileURI.toFileString());

		if (!model.exists()) {
			ChangeGroupRepository newRepository = ChangeGroupModelBuilder.createChangeGroupRepository();
			ChangeGroupModelBuilder.saveChangeGroupRepository(this.modelDir, this.modelName, newRepository);
		}
		return new ChangeGroupModelProvider(this.getBaseModelURI().appendFileExtension(CHANGEGROUP_EXTENSION));
	}

	@Override
	public void initializeDecisionCandidate(URI decisionCandidateModel) {
		// TODO Auto-generated method stub

	}

	@Override
	public DecisionCandidateModelProvider getDecisionCandidateModelProvider() {
		return new DecisionCandidateModelProvider(this.getBaseModelURI().appendFileExtension(DESIGNDECISION_EXTENSION));
	}

	@Override
	public CostModelProvider getCostModelProvider() {
		return new CostModelProvider(this.getBaseModelURI().appendFileExtension(COST_EXTENSION));
	}

	@Override
	public URI getModelDir() {
		return this.modelDir;
	}

	@Override
	public String getModelName() {
		return this.modelName;
	}

	@Override
	public void save(URI destinationDir, String modelName) {
		URI destinationBaseURI = destinationDir.appendSegment(modelName);

		this.getAllocationModelProvider().save(destinationBaseURI.appendFileExtension(ALLOCATION_EXTENSION));

		try {
			this.getChangeGroupModelProvider().save(destinationBaseURI.appendFileExtension(CHANGEGROUP_EXTENSION));
		} catch (IOException e) {
			// Nothing to do, missing change group model could not be created
		}

		this.getCloudProfileModelProvider().save(destinationBaseURI.appendFileExtension(CLOUDPROFILE_EXTENSION));
		this.getCostModelProvider().save(destinationBaseURI.appendFileExtension(COST_EXTENSION));
		this.getDecisionCandidateModelProvider().save(destinationBaseURI.appendFileExtension(DESIGNDECISION_EXTENSION));
		this.getDesignDecisionModelProvider().save(destinationBaseURI.appendFileExtension(DESIGNDECISION_EXTENSION));
		this.getRepositoryModelProvider().save(destinationBaseURI.appendFileExtension(REPOSITORY_EXTENSION));
		this.getResourceEnvironmentModelProvider()
				.save(destinationBaseURI.appendFileExtension(RESOURCEENVIRONMENT_EXTENSION));
		this.getSystemModelProvider().save(destinationBaseURI.appendFileExtension(SYSTEM_EXTENSION));
		this.getUsageModelProvider().save(destinationBaseURI.appendFileExtension(USAGE_EXTENSION));
	}

	private URI getBaseModelURI() {
		return this.modelDir.appendSegment(this.modelName);
	}

}
