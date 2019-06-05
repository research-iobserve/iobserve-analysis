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
package org.iobserve.service.behavior.analysis;

import java.util.ArrayList;
import java.util.List;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Lars Jürgensen
 *
 * @param <T>
 */
public class DataCollector<T> extends AbstractStage {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataCollector.class);
    private final InputPort<T> dataInputPort = this.createInputPort();
    private final InputPort<Long> timeTriggerInputPort = this.createInputPort();
    private final OutputPort<List<T>> mTreeOutputPort = this.createOutputPort();
    private final OutputPort<List<T>> opticsOutputPort = this.createOutputPort();

    private List<T> dataList = new ArrayList<>();

    private boolean stopAfterAmount = false;
    private int maxAmount = 0;

    /**
     * Collect behavior models and send them based on an external time trigger to the mtree
     * generation and a clustering stage
     *
     * @param keepTime
     *            the time interval to keep user sessions
     * @param minCollectionSize
     *            minimal number of collected user session
     */
    public DataCollector() {
        this.declareActive();
    }

    public DataCollector(final int amount) {
        this.declareActive();
        this.maxAmount = amount;
        this.stopAfterAmount = true;
    }

    @Override
    protected void execute() throws Exception {
        final T newData = this.dataInputPort.receive();

        if (newData != null) {
            DataCollector.LOGGER.info("Received a behavior model!");
            this.dataList.add(newData);

            if (this.stopAfterAmount) {
                if (this.dataList.size() >= this.maxAmount) {
                    DataCollector.LOGGER.info("Reached model amount maximum, sending models..");
                    this.opticsOutputPort.send(this.dataList);
                    this.mTreeOutputPort.send(this.dataList);
                    this.dataList = new ArrayList<>();
                }
            }
        }

        final Long triggerTime = this.timeTriggerInputPort.receive();
        if (triggerTime != null) {

            DataCollector.LOGGER.debug("Sending models...");
            this.opticsOutputPort.send(this.dataList);
            this.mTreeOutputPort.send(this.dataList);

        }

    }

    public InputPort<T> getDataInputPort() {
        return this.dataInputPort;
    }

    public InputPort<Long> getTimeTriggerInputPort() {
        return this.timeTriggerInputPort;
    }

    public OutputPort<List<T>> getmTreeOutputPort() {
        return this.mTreeOutputPort;
    }

    public OutputPort<List<T>> getOpticsOutputPort() {
        return this.opticsOutputPort;
    }

}
