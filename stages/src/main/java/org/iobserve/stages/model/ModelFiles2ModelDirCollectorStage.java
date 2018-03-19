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

import org.iobserve.stages.source.SingleConnectionTcpReaderStage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import teetime.stage.basic.AbstractFilter;

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

    private boolean receivedAllocationModel = false;
    private final boolean receivedCloudProfileModel = false;
    private final boolean receivedCostModel = false;
    private final boolean receivedDesignDecisionModel = false;
    private final boolean receivedQmlDeclarationsModel = false;
    private final boolean receivedRepositoryModel = false;
    private final boolean receivedResourceEnvironmentModel = false;
    private final boolean receivedSystemModel = false;
    private final boolean receivedUsageModel = false;

    @Override
    protected void execute(final File modelFile) throws Exception {
        ModelFiles2ModelDirCollectorStage.LOG.debug("Received model file " + modelFile);

        final String modelFileName = modelFile.getName();
        final String modelFileExtension = modelFileName.substring(modelFileName.lastIndexOf(".") + 1);

        if (modelFileExtension.equals("allocation")) {
            this.receivedAllocationModel = true;
        }
        // else if (modelFileExtension.equals("cloudprofile")) {
        // this.receivedCloudProfileModel = true;
        // } else if (modelFileExtension.equals("cost")) {
        // this.receivedCostModel = true;
        // } else if (modelFileExtension.equals("designdecision")) {
        // this.receivedDesignDecisionModel = true;
        // } else if (modelFileExtension.equals("qmldeclarations")) {
        // this.receivedQmlDeclarationsModel = true;
        // } else if (modelFileExtension.equals("repository")) {
        // this.receivedRepositoryModel = true;
        // } else if (modelFileExtension.equals("resourceenvironment")) {
        // this.receivedResourceEnvironmentModel = true;
        // } else if (modelFileExtension.equals("system")) {
        // this.receivedSystemModel = true;
        // } else if (modelFileExtension.equals("usagemodel")) {
        // this.receivedUsageModel = true;
        // }

        // If all files have been received, send the containing directory
        if (this.receivedAllocationModel /*
                                          * && this.receivedCloudProfileModel &&
                                          * this.receivedCostModel &&
                                          * this.receivedDesignDecisionModel &&
                                          * this.receivedQmlDeclarationsModel &&
                                          * this.receivedRepositoryModel &&
                                          * this.receivedResourceEnvironmentModel &&
                                          * this.receivedSystemModel && this.receivedUsageModel
                                          */) {
            this.outputPort.send(modelFile.getParentFile());
        }
    }

}
