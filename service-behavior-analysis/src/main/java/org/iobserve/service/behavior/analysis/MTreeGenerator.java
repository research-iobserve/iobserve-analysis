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

import java.util.List;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mtree.DistanceFunction;
import mtree.MTree;

/**
 *
 * @author Lars Jürgensen
 *
 * @param <T>
 */
public class MTreeGenerator<T> extends AbstractStage {

    private static final Logger LOGGER = LoggerFactory.getLogger(MTreeGenerator.class);

    private final InputPort<List<T>> inputPort = this.createInputPort();

    private final OutputPort<MTree<T>> outputPort = this.createOutputPort();

    private final DistanceFunction<T> distanceFunction;
    private int minNodeCapacity = 50;
    private int maxNodeCapacity = 99;

    public MTreeGenerator(final DistanceFunction<T> distanceFunction) {
        this.distanceFunction = distanceFunction;
    }

    @Override
    protected void execute() throws Exception {

        final List<T> models = this.inputPort.receive();

        final MTree<T> mtree = new MTree<>(this.minNodeCapacity, this.maxNodeCapacity, this.distanceFunction, null);

        if (models != null) {
            MTreeGenerator.LOGGER.info("Received " + models.size() + " new models");
            for (final T model : models) {
                mtree.add(model);
            }
            MTreeGenerator.LOGGER.info("Created MTree");

            this.outputPort.send(mtree);

        } else {
            MTreeGenerator.LOGGER.warn("Received null as model list");
        }
    }

    public OutputPort<MTree<T>> getOutputPort() {
        return this.outputPort;
    }

    public InputPort<List<T>> getInputPort() {
        return this.inputPort;
    }

    public int getMaxNodeCapacity() {
        return this.maxNodeCapacity;
    }

    public void setMaxNodeCapacity(final int maxNodeCapacity) {
        this.maxNodeCapacity = maxNodeCapacity;
    }

    public int getMinNodeCapacity() {
        return this.minNodeCapacity;
    }

    public void setMinNodeCapacity(final int minNodeCapacity) {
        this.minNodeCapacity = minNodeCapacity;
    }
}
