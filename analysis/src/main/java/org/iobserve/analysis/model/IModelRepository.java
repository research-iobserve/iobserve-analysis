package org.iobserve.analysis.model;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;

/**
 * TODO
 *
 * @author Tobias Pöppke
 *
 */
public interface IModelRepository {
	/**
	 * TODO
	 *
	 * @param modelDir
	 */
	public void initialize(URI modelDir, String modelName);

	/**
	 * TODO
	 *
	 * @param destinationDir
	 * @param modelName
	 */
	public void save(URI destinationDir, String modelName);

	/**
	 * TODO
	 *
	 * @return
	 */
	public URI getModelDir();

	/**
	 * TODO
	 *
	 * @return
	 */
	public String getModelName();

	/**
	 *
	 * @param decisionCandidateModel
	 */
	public void initializeDecisionCandidate(URI decisionCandidateModel);

	/**
	 * TODO
	 *
	 * @return
	 */
	public AllocationModelProvider getAllocationModelProvider();

	/**
	 * TODO
	 *
	 * @return
	 */
	public ResourceEnvironmentModelProvider getResourceEnvironmentModelProvider();

	/**
	 * TODO
	 *
	 * @return
	 */
	public RepositoryModelProvider getRepositoryModelProvider();

	/**
	 * TODO
	 *
	 * @return
	 */
	public SystemModelProvider getSystemModelProvider();

	/**
	 * TODO
	 *
	 * @return
	 */
	public UsageModelProvider getUsageModelProvider();

	/**
	 * TODO
	 *
	 * @return
	 */
	public CloudProfileModelProvider getCloudProfileModelProvider();

	/**
	 * TODO
	 *
	 * @return
	 */
	public DesignDecisionModelProvider getDesignDecisionModelProvider();

	/**
	 * TODO
	 *
	 * @return
	 */
	public DecisionCandidateModelProvider getDecisionCandidateModelProvider();

	/**
	 * TODO
	 *
	 * @return
	 * @throws IOException 
	 */
	public ChangeGroupModelProvider getChangeGroupModelProvider() throws IOException;

	/**
	 * TODO
	 *
	 * @return
	 */
	public CostModelProvider getCostModelProvider();
}
