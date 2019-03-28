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
package org.iobserve.evaluate.jss;

import java.io.FileNotFoundException;

import kieker.common.exception.ConfigurationException;

import teetime.framework.Configuration;

import org.iobserve.common.record.MeasureEventOccurance;
import org.iobserve.service.InstantiationFactory;
import org.iobserve.service.source.ISourceCompositeStage;
import org.iobserve.stages.general.DynamicEventDispatcher;
import org.iobserve.stages.general.IEventMatcher;
import org.iobserve.stages.general.ImplementsEventMatcher;

/**
 * @author Reiner Jung
 *
 */
public class EvaluationTeetimeConfiguration extends Configuration {

    public EvaluationTeetimeConfiguration(final kieker.common.configuration.Configuration kiekerConfiguration,
            final EvaluationParamterConfiguration configuration) throws ConfigurationException, FileNotFoundException {

        final ISourceCompositeStage sourceCompositeStage = InstantiationFactory.createWithConfiguration(
                ISourceCompositeStage.class, configuration.getSourceClassName(), kiekerConfiguration);

        final IEventMatcher<MeasureEventOccurance> rootEventMatcher = new ImplementsEventMatcher<>(
                MeasureEventOccurance.class, null);

        final DynamicEventDispatcher eventdispatcher = new DynamicEventDispatcher(rootEventMatcher, true, true, false);
        final EventCollector eventCollector = new EventCollector();
        final ComputeResults computeResults = new ComputeResults();
        final CSVFileWriter writeResults = new CSVFileWriter(configuration.getOutputFile());

        this.connectPorts(sourceCompositeStage.getOutputPort(), eventdispatcher.getInputPort());
        this.connectPorts(rootEventMatcher.getOutputPort(), eventCollector.getInputPort());
        this.connectPorts(eventCollector.getOutputPort(), computeResults.getInputPort());
        this.connectPorts(computeResults.getOutputPort(), writeResults.getInputPort());
    }

}
