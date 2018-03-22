/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.rac.creator;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;

import kieker.common.record.flow.IFlowRecord;

import teetime.framework.Configuration;
import teetime.stage.InitialElementProducer;
import teetime.stage.className.ClassNameRegistryRepository;

import org.iobserve.rac.creator.filter.DoAllFilter;
import org.iobserve.rac.creator.filter.ListWriter;
import org.iobserve.rac.creator.filter.PcmCorrespondentMethodStage;
import org.iobserve.rac.creator.filter.RACWriter;
import org.iobserve.rac.creator.filter.RecordFilter;
import org.iobserve.rac.creator.filter.UniqueFilter;
import org.iobserve.stages.general.DynamicEventDispatcher;
import org.iobserve.stages.source.Dir2RecordsFilter;
import org.xml.sax.SAXException;

/**
 * processed.
 *
 * @author Reiner Jung
 *
 */
public class ObservationConfiguration extends Configuration {

    /**
     * record switch filter. Is required to be global so we can cheat and get measurements from the
     * filter.
     */
    protected final DynamicEventDispatcher eventDispatcher;

    /**
     * Create a configuration with a ASCII file reader.
     *
     * @param inputPath
     *            input logs
     * @param repository
     *            PCM repository file reader
     * @param modelMapping
     *            Mapping file reader
     * @param mappedClassesFile
     *            output mapped classes
     * @param unmappedClassesFile
     *            output unmapped classes
     * @param racFile
     *            output rac file
     * @throws IOException
     *             file IO error
     * @throws SAXException
     *             parsing error of the repository model
     * @throws ParserConfigurationException
     *             SAX aprser configuration error
     */
    public ObservationConfiguration(final Collection<File> inputPath, final RepositoryFileReader repository,
            final ModelMappingReader modelMapping, final File mappedClassesFile, final File unmappedClassesFile,
            final File racFile) throws ParserConfigurationException, SAXException, IOException {

        /** configure filter. */
        final InitialElementProducer<File> files = new InitialElementProducer<>(inputPath);
        final Dir2RecordsFilter reader = new Dir2RecordsFilter(new ClassNameRegistryRepository());
        this.eventDispatcher = new DynamicEventDispatcher(true, true, false);
        this.eventDispatcher.registerOutput(IFlowRecord.class);
        final RecordFilter filter = new RecordFilter();
        final PcmCorrespondentMethodStage pcmCorrespondentMethodStage = new PcmCorrespondentMethodStage();
        final DoAllFilter doAllfilter = new DoAllFilter(repository, modelMapping);
        final ListWriter mappedWriter = new ListWriter(mappedClassesFile);
        final ListWriter unmappedWriter = new ListWriter(unmappedClassesFile);
        final RACWriter racWriter = new RACWriter(racFile);
        final UniqueFilter mappedUnique = new UniqueFilter();
        final UniqueFilter unmappedUnique = new UniqueFilter();

        /** connections. */
        this.connectPorts(files.getOutputPort(), reader.getInputPort());
        this.connectPorts(reader.getOutputPort(), this.eventDispatcher.getInputPort());
        this.connectPorts(this.eventDispatcher.getOutputPort(IFlowRecord.class), filter.getInputPort());
        this.connectPorts(filter.getOutputPort(), pcmCorrespondentMethodStage.getInputPort());
        this.connectPorts(pcmCorrespondentMethodStage.getOutputPort(), doAllfilter.getInputPort());
        this.connectPorts(doAllfilter.getRACOutputPort(), racWriter.getInputPort());
        this.connectPorts(doAllfilter.getMappedOutputPort(), mappedUnique.getInputPort());
        this.connectPorts(doAllfilter.getUnmappedOutputPort(), unmappedUnique.getInputPort());
        this.connectPorts(mappedUnique.getOutputPort(), mappedWriter.getInputPort());
        this.connectPorts(unmappedUnique.getOutputPort(), unmappedWriter.getInputPort());
    }

    public DynamicEventDispatcher getEventDispatcher() {
        return this.eventDispatcher;
    }

}
