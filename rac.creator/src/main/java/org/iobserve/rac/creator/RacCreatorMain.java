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
package org.iobserve.rac.creator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

import kieker.common.configuration.Configuration;

import org.iobserve.service.AbstractServiceMain;
import org.iobserve.service.CommandLineParameterEvaluation;
import org.iobserve.stages.general.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * Main CLI class for the rac creator.
 *
 * @author Reiner Jung
 *
 */
public class RacCreatorMain extends AbstractServiceMain<ObservationConfiguration> {

    private static final String RAC_FILENAME = "mapping.rac";
    private static final String MAPPED_CLASSES_FILENAME = "mapped.txt";
    private static final String UNMAPPED_CLASSES_FILENAME = "unmapped.txt";

    private static final Logger LOGGER = LoggerFactory.getLogger(RacCreatorMain.class);

    @Parameter(names = { "-i",
            "--input" }, required = true, description = "Input direcory.", converter = FileConverter.class)
    private File inputPath;

    @Parameter(names = { "-o",
            "--output" }, required = true, description = "Output directory.", converter = FileConverter.class)
    private File outputPath;

    @Parameter(names = { "-m",
            "--mapping" }, required = true, description = "Mapping file.", converter = FileConverter.class)
    private File mappingFile;

    @Parameter(names = { "-r",
            "--repository" }, required = true, description = "PCM repository file.", converter = FileConverter.class)
    private File repositoryFile;

    /**
     * Create a new rac creator.
     */
    public RacCreatorMain() {
        // empty constructor
    }

    /**
     * @param args
     *            command line arguments
     * @throws IOException
     *             throw IO error
     * @throws SAXException
     *             SAX parse error
     * @throws ParserConfigurationException
     *             parser configuration error
     */
    public static void main(final String[] args) throws IOException, ParserConfigurationException, SAXException {
        new RacCreatorMain().run("RAC Creator", "rac creator", args);
    }

    @Override
    protected ObservationConfiguration createConfiguration(final Configuration configuration)
            throws ConfigurationException {
        final Collection<File> inputPaths = new ArrayList<>();
        inputPaths.add(this.inputPath);

        final File mappedClassesFile = new File(
                this.outputPath.getAbsolutePath() + File.separator + RacCreatorMain.MAPPED_CLASSES_FILENAME);
        final File unmappedClassesFile = new File(
                this.outputPath.getAbsolutePath() + File.separator + RacCreatorMain.UNMAPPED_CLASSES_FILENAME);
        final File racFile = new File(this.outputPath.getAbsolutePath() + File.separator + RacCreatorMain.RAC_FILENAME);

        final RepositoryFileReader repositoryFileReader = new RepositoryFileReader(this.repositoryFile);
        final ModelMappingReader mappingFileReader = new ModelMappingReader(this.mappingFile);

        try {
            return new ObservationConfiguration(inputPaths, repositoryFileReader, mappingFileReader, mappedClassesFile,
                    unmappedClassesFile, racFile);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new ConfigurationException(e);
        }
    }

    @Override
    protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
        try {
            if (!this.outputPath.isDirectory() || !this.outputPath.exists()) {
                RacCreatorMain.LOGGER.error("Output path {} does not exist or is not a directory.",
                        this.outputPath.getCanonicalPath());
                commander.usage();
                return false;
            }

            if (!this.inputPath.isDirectory() || !this.inputPath.exists()) {
                RacCreatorMain.LOGGER.error("Input path {} does not exist or is not a directory.",
                        this.inputPath.getCanonicalPath());
                commander.usage();
                return false;
            }

            if (!CommandLineParameterEvaluation.isFileReadable(this.mappingFile, "Mapping file")) {
                commander.usage();
                return false;
            }

            if (!CommandLineParameterEvaluation.isFileReadable(this.repositoryFile, "Repository file")) {
                commander.usage();
                return false;
            }

            return true;
        } catch (final IOException e) {
            throw new ConfigurationException(e);
        }
    }

    @Override
    protected File getConfigurationFile() {
        return null;
    }

    @Override
    protected boolean checkConfiguration(final Configuration configuration, final JCommander commander) {
        return true;
    }

    @Override
    protected void shutdownService() {

    }

}
