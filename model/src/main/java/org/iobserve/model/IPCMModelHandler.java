/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.model;

import de.uka.ipd.sdq.dsexplore.qml.declarations.QMLDeclarations.QMLDeclarations;
import de.uka.ipd.sdq.pcm.cost.CostRepository;
import de.uka.ipd.sdq.pcm.designdecision.DecisionSpace;

import org.eclipse.emf.common.util.URI;
import org.iobserve.model.correspondence.CorrespondenceModel;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.cloud.pcmcloud.cloudprofile.CloudProfile;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

/**
 * Interface for model handlers. Enables mocking implementations for testing for example.
 *
 * @author Lars Bluemke
 *
 */
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
    CorrespondenceModel getCorrespondenceModel();

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