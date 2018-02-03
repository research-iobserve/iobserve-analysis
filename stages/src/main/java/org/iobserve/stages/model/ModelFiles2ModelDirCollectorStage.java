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
package org.iobserve.stages.model;

import java.io.File;

import org.gradle.internal.impldep.org.apache.commons.io.FilenameUtils;

import teetime.stage.basic.AbstractFilter;

public class ModelFiles2ModelDirCollectorStage extends AbstractFilter<File> {

    private boolean receivedAllocationModel = false;
    private boolean receivedCloudProfileModel = false;
    private boolean receivedCostModel = false;
    private boolean receivedDesignDecisionModel = false;
    private boolean receivedQmlDeclarationsModel = false;
    private boolean receivedRepositoryModel = false;
    private boolean receivedResourceEnvironmentModel = false;
    private boolean receivedSystemModel = false;
    private boolean receivedUsageModel = false;

    @Override
    protected void execute(final File modelFile) throws Exception {

        final String modelFileExtension = FilenameUtils.getExtension(modelFile.getName());

        if (modelFileExtension.equals("allocation")) {
            this.receivedAllocationModel = true;
        } else if (modelFileExtension.equals("cloudprofile")) {
            this.receivedCloudProfileModel = true;
        } else if (modelFileExtension.equals("cost")) {
            this.receivedCostModel = true;
        } else if (modelFileExtension.equals("designdecision")) {
            this.receivedDesignDecisionModel = true;
        } else if (modelFileExtension.equals("qmldeclarations")) {
            this.receivedQmlDeclarationsModel = true;
        } else if (modelFileExtension.equals("repository")) {
            this.receivedRepositoryModel = true;
        } else if (modelFileExtension.equals("resourceenvironment")) {
            this.receivedResourceEnvironmentModel = true;
        } else if (modelFileExtension.equals("system")) {
            this.receivedSystemModel = true;
        } else if (modelFileExtension.equals("usagemodel")) {
            this.receivedUsageModel = true;
        }

        // If all files have been received, send the containing directory
        if (this.receivedAllocationModel && this.receivedCloudProfileModel && this.receivedCostModel
                && this.receivedDesignDecisionModel && this.receivedQmlDeclarationsModel && this.receivedRepositoryModel
                && this.receivedResourceEnvironmentModel && this.receivedSystemModel && this.receivedUsageModel) {
            this.outputPort.send(modelFile.getParentFile());
        }
    }

}
