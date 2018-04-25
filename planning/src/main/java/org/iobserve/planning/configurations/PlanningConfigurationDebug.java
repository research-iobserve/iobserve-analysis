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
package org.iobserve.planning.configurations;

import java.io.File;

import teetime.framework.Configuration;
import teetime.stage.CollectorSink;
import teetime.stage.InitialElementProducer;

import org.iobserve.planning.ModelOptimization;
import org.iobserve.planning.ModelProcessing;

/**
 *
 * @author Lars Bluemke
 *
 */
public class PlanningConfigurationDebug extends Configuration {

    public PlanningConfigurationDebug() {
        final InitialElementProducer<File> producer = new InitialElementProducer<>(
                new File("/Users/LarsBlumke/Documents/CAU/Masterarbeit/working-dir"));
        final ModelProcessing modelProcessor = new ModelProcessing();
        final ModelOptimization modelOptimizer = new ModelOptimization(
                new File("/Users/LarsBlumke/Documents/CAU/Masterarbeit/iObserve/eclipse-product-export/MacOS"),
                new File("usr/local/bin"));
        final CollectorSink<File> runtimeConsumer = new CollectorSink<>();
        final CollectorSink<File> redeploymentConsumer = new CollectorSink<>();

        this.connectPorts(producer.getOutputPort(), modelProcessor.getInputPort());
        this.connectPorts(modelProcessor.getOutputPort(), modelOptimizer.getInputPort());
        this.connectPorts(modelOptimizer.getRuntimeModelOutputPort(), runtimeConsumer.getInputPort());
        this.connectPorts(modelOptimizer.getRedeploymentModelOutputPort(), redeploymentConsumer.getInputPort());
    }
}
