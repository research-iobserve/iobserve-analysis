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
package org.iobserve.analysis.behavior.filter;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.behavior.models.data.BehaviorModelTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weka.core.Instances;

/**
 * Transforms BehaviorModelTable into weka instances. Adds all behavior model elements to the
 * instances and pass them to the output port on termination.
 *
 * @author Christoph Dornieden
 *
 */

public class CreateWekaInstancesStage extends AbstractConsumerStage<BehaviorModelTable> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateWekaInstancesStage.class);

    private Instances instances;
    private final OutputPort<Instances> outputPort = this.createOutputPort();

    /**
     * constructor.
     */
    public CreateWekaInstancesStage() {
        // empty constructor
    }

    @Override
    protected void execute(final BehaviorModelTable behaviorModelTable) {
        if (this.instances == null) {
            this.instances = behaviorModelTable.toInstances();

        } else {
            this.instances.add(behaviorModelTable.toInstance());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see teetime.framework.AbstractStage#onTerminating()
     */
    @Override
    public void onTerminating() {
        if (this.instances == null) {
            CreateWekaInstancesStage.LOGGER.error("No instances created!");
        } else {
            this.outputPort.send(this.instances);
        }

        super.onTerminating();
    }

    public OutputPort<Instances> getOutputPort() {
        return this.outputPort;
    }

}