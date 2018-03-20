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
package org.iobserve.adaptation.configurations;

import java.io.File;

import org.iobserve.adaptation.stages.AdaptationDataCreator;
import org.iobserve.stages.model.ModelFiles2ModelDirCollectorStage;
import org.iobserve.stages.source.SingleConnectionTcpReaderStage;

import teetime.framework.Configuration;

/**
 * Configuration for the stages of the adaptation service.
 *
 * @author Lars Bluemke
 *
 */
public class AdaptationConfiguration extends Configuration {

    public AdaptationConfiguration(final int runtimeModelInputPort, final int redeploymentModelInputPort,
            final File runtimeModelDirectory, final File redeploymentModelDirectory) {

        final SingleConnectionTcpReaderStage runtimeModelReader = new SingleConnectionTcpReaderStage(
                runtimeModelInputPort, runtimeModelDirectory);
        final SingleConnectionTcpReaderStage redeploymentModelReader = new SingleConnectionTcpReaderStage(
                runtimeModelInputPort, runtimeModelDirectory);

        final ModelFiles2ModelDirCollectorStage runtimeModelCollector = new ModelFiles2ModelDirCollectorStage();
        final ModelFiles2ModelDirCollectorStage redeploymentModelCollector = new ModelFiles2ModelDirCollectorStage();

        final AdaptationDataCreator adaptationDataCreator = new AdaptationDataCreator();

        // more filters to come...

        this.connectPorts(runtimeModelReader.getOutputPort(), runtimeModelCollector.getInputPort());
        this.connectPorts(redeploymentModelReader.getOutputPort(), redeploymentModelCollector.getInputPort());
        this.connectPorts(runtimeModelCollector.getOutputPort(), adaptationDataCreator.getRuntimeModelInputPort());
        this.connectPorts(redeploymentModelCollector.getOutputPort(),
                adaptationDataCreator.getRedeploymentModelInputPort());

        // to be continued...

    }

}
