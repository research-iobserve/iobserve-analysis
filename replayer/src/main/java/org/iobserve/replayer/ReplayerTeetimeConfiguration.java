/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.replayer;

import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.tools.source.LogsReaderCompositeStage;

import teetime.framework.Configuration;

/**
 * Configuration for the log replayer.
 *
 * @author Reiner Jung
 *
 */
public class ReplayerTeetimeConfiguration extends Configuration {

    private final DataSendStage consumer;

    /**
     * Construct the replayer configuration.
     *
     * @param parameter
     *            configuration parameter object
     */
    public ReplayerTeetimeConfiguration(final ReplayerParameter parameter) {
        final kieker.common.configuration.Configuration configuration = ConfigurationFactory
                .createSingletonConfiguration();
        configuration.setProperty(LogsReaderCompositeStage.LOG_DIRECTORIES,
                parameter.getDataLocation().toPath().toAbsolutePath().toString());
        final LogsReaderCompositeStage reader = new LogsReaderCompositeStage(configuration);

        final ReplayControlStage delayStage = new ReplayControlStage(1000 * 1000, parameter.getDelayFactor(),
                parameter.getShowRecordCount());

        this.consumer = new DataSendStage(parameter.getHostname(), parameter.getOutputPort());

        if (!parameter.isNoDelay()) {
            if (parameter.isTimeRelative()) {
                final RewriteTime rewriteTime = new RewriteTime();

                this.connectPorts(reader.getOutputPort(), rewriteTime.getInputPort());
                this.connectPorts(rewriteTime.getOutputPort(), delayStage.getInputPort());
            } else {
                this.connectPorts(reader.getOutputPort(), delayStage.getInputPort());
            }
            this.connectPorts(delayStage.getOutputPort(), this.consumer.getInputPort());
        } else {
            if (parameter.isTimeRelative()) {
                final RewriteTime rewriteTime = new RewriteTime();

                this.connectPorts(reader.getOutputPort(), rewriteTime.getInputPort());
                this.connectPorts(rewriteTime.getOutputPort(), this.consumer.getInputPort());
            } else {
                this.connectPorts(reader.getOutputPort(), this.consumer.getInputPort());
            }
        }
    }

    public DataSendStage getCounter() {
        return this.consumer;
    }

    public boolean isOutputConnected() {
        return this.consumer.isOutputConnected();
    }

}
