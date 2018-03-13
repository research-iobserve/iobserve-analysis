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

import teetime.framework.Configuration;
import teetime.stage.InitialElementProducer;
import teetime.stage.className.ClassNameRegistryRepository;

import org.iobserve.rac.creator.filter.DoAllFilter;
import org.iobserve.rac.creator.filter.ListWriter;
import org.iobserve.rac.creator.filter.PcmCorrespondentMethodStage;
import org.iobserve.rac.creator.filter.RACWriter;
import org.iobserve.rac.creator.filter.RecordFilter;
import org.iobserve.rac.creator.filter.UniqueFilter;
import org.iobserve.stages.general.RecordSwitch;
import org.iobserve.stages.source.Dir2RecordsFilter;
import org.xml.sax.SAXException;

// TODO complete this class. This configuration is incomplete as the read Kieker data is not further
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
    protected final RecordSwitch recordSwitch;

    private final InitialElementProducer<File> files;
    private final Dir2RecordsFilter reader;
    private final RecordFilter filter;
    private final DoAllFilter doAllfilter;

    private final ListWriter mappedWriter;

    private final ListWriter unmappedWriter;

    private final RACWriter racWriter;

    private final PcmCorrespondentMethodStage pcmCorrespondentMethodStage;

    private final UniqueFilter unmappedUnique;

    private final UniqueFilter mappedUnique;

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
        this.files = new InitialElementProducer<>(inputPath);
        this.reader = new Dir2RecordsFilter(new ClassNameRegistryRepository());
        this.recordSwitch = new RecordSwitch();
        this.filter = new RecordFilter();
        this.pcmCorrespondentMethodStage = new PcmCorrespondentMethodStage();
        this.doAllfilter = new DoAllFilter(repository, modelMapping);
        this.mappedWriter = new ListWriter(mappedClassesFile);
        this.unmappedWriter = new ListWriter(unmappedClassesFile);
        this.racWriter = new RACWriter(racFile);
        this.mappedUnique = new UniqueFilter();
        this.unmappedUnique = new UniqueFilter();

        /** connections. */
        this.connectPorts(this.files.getOutputPort(), this.reader.getInputPort());
        this.connectPorts(this.reader.getOutputPort(), this.recordSwitch.getInputPort());
        this.connectPorts(this.recordSwitch.getFlowOutputPort(), this.filter.getInputPort());
        this.connectPorts(this.filter.getOutputPort(), this.pcmCorrespondentMethodStage.getInputPort());
        this.connectPorts(this.pcmCorrespondentMethodStage.getOutputPort(), this.doAllfilter.getInputPort());
        this.connectPorts(this.doAllfilter.getRACOutputPort(), this.racWriter.getInputPort());
        this.connectPorts(this.doAllfilter.getMappedOutputPort(), this.mappedUnique.getInputPort());
        this.connectPorts(this.doAllfilter.getUnmappedOutputPort(), this.unmappedUnique.getInputPort());
        this.connectPorts(this.mappedUnique.getOutputPort(), this.mappedWriter.getInputPort());
        this.connectPorts(this.unmappedUnique.getOutputPort(), this.unmappedWriter.getInputPort());
    }

    public RecordSwitch getRecordSwitch() {
        return this.recordSwitch;
    }

}
