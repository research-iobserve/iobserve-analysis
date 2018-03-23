package org.iobserve.model;

import org.eclipse.emf.common.util.URI;
import org.iobserve.model.correspondence.ICorrespondence;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.cloud.pcmcloud.cloudprofile.CloudProfile;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import de.uka.ipd.sdq.dsexplore.qml.declarations.QMLDeclarations.QMLDeclarations;
import de.uka.ipd.sdq.pcm.cost.CostRepository;
import de.uka.ipd.sdq.pcm.designdecision.DecisionSpace;

public interface IPCMModelHandler {

    String ALLOCATION_SUFFIX = "allocation";
    String CLOUD_PROFILE_SUFFIX = "cloudprofile";
    String COST_SUFFIX = "cost";
    String DESIGN_DECISION_SUFFIX = "designdecision";
    String REPOSITORY_SUFFIX = "repository";
    String RESOURCE_ENVIRONMENT_SUFFIX = "resourceenvironment";
    String SYSTEM_SUFFIX = "system";
    String USAGE_MODEL_SUFFIX = "usagemodel";

    /**
     * @return allocation model
     */
    Allocation getAllocationModel();

    /**
     * @return resource environment model
     */
    ResourceEnvironment getResourceEnvironmentModel();

    /**
     * @return system model
     */
    System getSystemModel();

    /**
     * @return usage model
     */
    UsageModel getUsageModel();

    /**
     * @return correspondence model
     */
    ICorrespondence getCorrespondenceModel();

    /**
     * @return repository model provider
     */
    Repository getRepositoryModel();

    /**
     * @return cloud profile model provider
     */
    CloudProfile getCloudProfileModel();

    /**
     * @return cost model
     */
    CostRepository getCostModel();

    /**
     * @return design decision model
     */
    DecisionSpace getDesignDecisionModel();

    /**
     * @return QML declarations model provider
     */
    QMLDeclarations getQMLDeclarationsModel();

    /**
     * Saves all currently available models in this provider into the snapshot location.
     *
     * @param fileLocationURI
     *            the location directory for the snapshot
     */
    void save(URI fileLocationURI);

}