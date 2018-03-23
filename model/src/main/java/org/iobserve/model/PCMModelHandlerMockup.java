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

/**
 * A mockup of the regular PCMModelHandler for testing purposes. Instead of having to read the
 * models from the file system this class gets them via its constructor.
 *
 * @author Lars Bluemke
 *
 */
public class PCMModelHandlerMockup implements IPCMModelHandler {

    private final Allocation allocationModel;
    private final ResourceEnvironment resourceEnvironmentModel;
    private final System systemModel;
    private final UsageModel usageModel;
    private final ICorrespondence correspondenceModel;
    private final Repository repositoryModel;
    private final CloudProfile cloudProfileModel;
    private final CostRepository costModel;
    private final DecisionSpace designDecisionModel;
    private final QMLDeclarations qmlDeclarationsModel;

    public PCMModelHandlerMockup(final Allocation allocationModel, final ResourceEnvironment resourceEnvironmentModel,
            final System systemModel, final UsageModel usageModel, final ICorrespondence correspondenceModel,
            final Repository repositoryModel, final CloudProfile cloudProfileModel, final CostRepository costModel,
            final DecisionSpace designDecisionModel, final QMLDeclarations qmlDeclarationsModel) {
        this.allocationModel = allocationModel;
        this.resourceEnvironmentModel = resourceEnvironmentModel;
        this.systemModel = systemModel;
        this.usageModel = usageModel;
        this.correspondenceModel = correspondenceModel;
        this.repositoryModel = repositoryModel;
        this.cloudProfileModel = cloudProfileModel;
        this.costModel = costModel;
        this.designDecisionModel = designDecisionModel;
        this.qmlDeclarationsModel = qmlDeclarationsModel;
    }

    @Override
    public Allocation getAllocationModel() {
        return this.allocationModel;
    }

    @Override
    public ResourceEnvironment getResourceEnvironmentModel() {
        return this.resourceEnvironmentModel;
    }

    @Override
    public System getSystemModel() {
        return this.systemModel;
    }

    @Override
    public UsageModel getUsageModel() {
        return this.usageModel;
    }

    @Override
    public ICorrespondence getCorrespondenceModel() {
        return this.correspondenceModel;
    }

    @Override
    public Repository getRepositoryModel() {
        return this.repositoryModel;
    }

    @Override
    public CloudProfile getCloudProfileModel() {
        return this.cloudProfileModel;
    }

    @Override
    public CostRepository getCostModel() {
        return this.costModel;
    }

    @Override
    public DecisionSpace getDesignDecisionModel() {
        return this.designDecisionModel;
    }

    @Override
    public QMLDeclarations getQMLDeclarationsModel() {
        return this.qmlDeclarationsModel;
    }

    @Override
    public void save(final URI fileLocationURI) {
        // do nothing
    }

}
