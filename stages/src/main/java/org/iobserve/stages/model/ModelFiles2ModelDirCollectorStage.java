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

import teetime.stage.basic.AbstractFilter;

import org.iobserve.stages.source.SingleConnectionTcpReaderStage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Collects all incoming PCM model files until there is at least one file of each type. Then passes
 * the parent directory to its output port. Note: This stage is intended to be used after
 * {@link SingleConnectionTcpReaderStage} and therefore assumes that all incoming files are in the
 * same parent directory.
 *
 * @author Lars Bluemke
 *
 */
public class ModelFiles2ModelDirCollectorStage extends AbstractFilter<File> {
    private static final Logger LOG = LoggerFactory.getLogger(ModelFiles2ModelDirCollectorStage.class);

    private boolean receivedAllocationModel;
    private boolean receivedCloudProfileModel;
    private boolean receivedCostModel;
    private boolean receivedDesignDecisionModel;
    private boolean receivedQmlDeclarationsModel;
    private boolean receivedRepositoryModel;
    private boolean receivedResourceEnvironmentModel;
    private boolean receivedSystemModel;
    private boolean receivedUsageModel;

    @Override
    protected void execute(final File modelFile) throws Exception {
        if (ModelFiles2ModelDirCollectorStage.LOG.isDebugEnabled()) {
            ModelFiles2ModelDirCollectorStage.LOG.debug("Received model file " + modelFile);
        }

        final String modelFileName = modelFile.getName();
        final String modelFileExtension = modelFileName.substring(modelFileName.lastIndexOf(".") + 1);

        if ("allocation".equals(modelFileExtension)) {
            this.receivedAllocationModel = true;
        } else if ("cloudprofile".equals(modelFileExtension)) {
            this.receivedCloudProfileModel = true;
        } else if ("cost".equals(modelFileExtension)) {
            this.receivedCostModel = true;
        } else if ("designdecision".equals(modelFileExtension)) {
            this.receivedDesignDecisionModel = true;
        } else if ("qmldeclarations".equals(modelFileExtension)) {
            this.receivedQmlDeclarationsModel = true;
        } else if ("repository".equals(modelFileExtension)) {
            this.receivedRepositoryModel = true;
        } else if ("resourceenvironment".equals(modelFileExtension)) {
            this.receivedResourceEnvironmentModel = true;
        } else if ("system".equals(modelFileExtension)) {
            this.receivedSystemModel = true;
        } else if ("usagemodel".equals(modelFileExtension)) {
            this.receivedUsageModel = true;
        }

        // If all files have been received, send the containing directory
        if (this.receivedAllocationModel && this.receivedCloudProfileModel && this.receivedCostModel
                && this.receivedDesignDecisionModel && this.receivedQmlDeclarationsModel && this.receivedRepositoryModel
                && this.receivedResourceEnvironmentModel && this.receivedSystemModel && this.receivedUsageModel) {
            this.outputPort.send(modelFile.getParentFile());

            this.receivedAllocationModel = false;
            this.receivedCloudProfileModel = false;
            this.receivedCostModel = false;
            this.receivedDesignDecisionModel = false;
            this.receivedQmlDeclarationsModel = false;
            this.receivedRepositoryModel = false;
            this.receivedResourceEnvironmentModel = false;
            this.receivedSystemModel = false;
            this.receivedUsageModel = false;
        }
    }

}
