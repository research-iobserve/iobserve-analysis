/***************************************************************************
 * Copyright (C) 2019 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.check.log.sessions;

import java.io.FileNotFoundException;

import kieker.analysis.source.ISourceCompositeStage;
import kieker.common.exception.ConfigurationException;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.object.CallOperationObjectEvent;
import kieker.tools.source.SourceStageFactory;

import teetime.framework.Configuration;

import org.iobserve.stages.general.DynamicEventDispatcher;
import org.iobserve.stages.general.IEventMatcher;
import org.iobserve.stages.general.ImplementsEventMatcher;
import org.iobserve.stages.sink.CSVFileWriter;

/**
 * @author Reiner Jung
 *
 */
public class PiplineConfiguration extends Configuration {

    public PiplineConfiguration(final kieker.common.configuration.Configuration configuration, final Settings settings)
            throws ConfigurationException, FileNotFoundException {
        final ISourceCompositeStage sourceCompositeStage = SourceStageFactory.createSourceCompositeStage(configuration);

        final DynamicEventDispatcher eventDispatcher = new DynamicEventDispatcher(null, true, true, false);
        final IEventMatcher<TraceMetadata> traceMetadataMatcher = new ImplementsEventMatcher<>(TraceMetadata.class,
                null);
        eventDispatcher.registerOutput(traceMetadataMatcher);
        final IEventMatcher<CallOperationObjectEvent> operationEventMatcher = new ImplementsEventMatcher<>(
                CallOperationObjectEvent.class, null);
        eventDispatcher.registerOutput(operationEventMatcher);

        final EventPropertyCounter<TraceMetadata, String, SessionModel> counter = new EventPropertyCounter<>(
                new IPropertySelector<TraceMetadata, SessionModel>() {

                    @SuppressWarnings("unchecked")
                    @Override
                    public String match(final TraceMetadata input) {
                        return input.getSessionId();
                    }

                    @Override
                    public void compute(final SessionModel sessionModel, final TraceMetadata input) {
                        sessionModel.setCounter(sessionModel.getCounter() + 1);
                        if (sessionModel.getFirst() == null) {
                            sessionModel.setFirst(input.getLoggingTimestamp());
                        }
                        sessionModel.setLast(input.getLoggingTimestamp());
                    }

                    @Override
                    public SessionModel createNewSessionModel() {
                        final SessionModel data = new SessionModel();
                        data.setCounter(1);
                        return data;
                    }

                });

        final MapTwister mapTwister = new MapTwister();

        final CSVFileWriter csvFileWriter = new CSVFileWriter(settings.getOutputFile());

        this.connectPorts(sourceCompositeStage.getOutputPort(), eventDispatcher.getInputPort());
        this.connectPorts(traceMetadataMatcher.getOutputPort(), counter.getInputPort());
        this.connectPorts(counter.getOutputPort(), mapTwister.getInputPort());
        this.connectPorts(mapTwister.getOutputPort(), csvFileWriter.getInputPort());

    }

}
