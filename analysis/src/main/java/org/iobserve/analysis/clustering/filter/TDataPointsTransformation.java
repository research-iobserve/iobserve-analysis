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

package org.iobserve.analysis.clustering.filter;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import java.util.List;

import org.iobserve.analysis.clustering.birch.DataPoints;
import org.iobserve.analysis.clustering.filter.models.BehaviorModelTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Transforms BehaviorModelTable into weka instances. Adds all behavior model elements to the
 * instances and pass them to the output port on termination.
 *
 * @author Melf Lorenzen
 *
 */

public class TDataPointsTransformation extends AbstractConsumerStage<List<BehaviorModelTable>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TInstanceTransformations.class);

    private DataPoints dataPoints;
    private final OutputPort<DataPoints> outputPort = this.createOutputPort();

    /**
     * constructor.
     */
    public TDataPointsTransformation() {
        this.dataPoints = null;
    }

    @Override
    protected void execute(final List<BehaviorModelTable> list) {
    	/** TODO: Nur dazukommende Punkte verwenden */
            this.dataPoints = new DataPoints(list);
    }

    /*
     * (non-Javadoc)
     *
     * @see teetime.framework.AbstractStage#onTerminating()
     */
    @Override
    public void onTerminating() {
        if (this.dataPoints == null) {
        	TDataPointsTransformation.LOGGER.error("No instances created!");
        } else {
            this.outputPort.send(this.dataPoints);
        }

        super.onTerminating();
    }

    public OutputPort<DataPoints> getOutputPort() {
        return this.outputPort;
    }

}

