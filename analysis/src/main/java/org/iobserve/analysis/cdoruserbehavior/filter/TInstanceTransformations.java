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

package org.iobserve.analysis.cdoruserbehavior.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.iobserve.analysis.cdoruserbehavior.filter.models.BehaviorModelTable;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;
import weka.core.Instances;

/**
 * Transforms BehaviorModelTable into weka instances. Adds all behavior model elements to the
 * instances and pass them to the output port on termination.
 *
 * @author Christoph Dornieden
 *
 */

public class TInstanceTransformations extends AbstractConsumerStage<BehaviorModelTable> {
    private static final Logger LOGGER = LogManager.getLogger(TInstanceTransformations.class);

    private Instances instances;
    private final OutputPort<Instances> outputPort = this.createOutputPort();

    /**
     * constructor.
     */
    public TInstanceTransformations() {
        this.instances = null;
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
    public void onTerminating() throws Exception {
        if (this.instances == null) {
            TInstanceTransformations.LOGGER.error("No instances created!");
        } else {
            this.outputPort.send(this.instances);
        }

        super.onTerminating();
    }

    public OutputPort<Instances> getOutputPort() {
        return this.outputPort;
    }

}
