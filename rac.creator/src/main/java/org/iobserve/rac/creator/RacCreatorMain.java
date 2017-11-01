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

import org.xml.sax.SAXException;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.converters.FileConverter;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import teetime.framework.Configuration;
import teetime.framework.Execution;

/**
 * Main CLI class for the rac creator.
 *
 * @author Reiner Jung
 *
 */
public class RacCreatorMain {

    private static final String RAC_FILENAME = "mapping.rac";
    private static final String MAPPED_CLASSES_FILENAME = "mapped.txt";
    private static final String UNMAPPED_CLASSES_FILENAME = "unmapped.txt";
    private static final Log LOG = LogFactory.getLog(RacCreatorMain.class);

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
        final RacCreatorMain main = new RacCreatorMain();
        final JCommander commander = new JCommander(main);
        try {
            commander.parse(args);
            main.execute(commander);
        } catch (final ParameterException e) {
            RacCreatorMain.LOG.error(e.getLocalizedMessage());
            commander.usage();
        }
    }

    private void execute(final JCommander commander) throws IOException, ParserConfigurationException, SAXException {
        if (!this.outputPath.isDirectory() || !this.outputPath.exists()) {
            RacCreatorMain.LOG.error(
                    "Output path " + this.outputPath.getCanonicalPath() + " does not exist or is not a directory.");
            commander.usage();
            System.exit(1);
        }

        if (!this.inputPath.isDirectory() || !this.inputPath.exists()) {
            RacCreatorMain.LOG.error(
                    "Input path " + this.inputPath.getCanonicalPath() + " does not exist or is not a directory.");
            commander.usage();
            System.exit(1);
        }

        if (!this.fileReadOk(this.mappingFile, "Mapping file")) {
            commander.usage();
            System.exit(1);
        }

        if (!this.fileReadOk(this.repositoryFile, "Repository file")) {
            commander.usage();
            System.exit(1);
        }

        final Collection<File> inputPaths = new ArrayList<>();
        inputPaths.add(this.inputPath);

        final File mappedClassesFile = new File(
                this.outputPath.getAbsolutePath() + File.separator + RacCreatorMain.MAPPED_CLASSES_FILENAME);
        final File unmappedClassesFile = new File(
                this.outputPath.getAbsolutePath() + File.separator + RacCreatorMain.UNMAPPED_CLASSES_FILENAME);
        final File racFile = new File(this.outputPath.getAbsolutePath() + File.separator + RacCreatorMain.RAC_FILENAME);

        final RepositoryFileReader repositoryFileReader = new RepositoryFileReader(this.repositoryFile);
        final ModelMappingReader mappingFileReader = new ModelMappingReader(this.mappingFile);

        final Configuration configuration = new ObservationConfiguration(inputPaths, repositoryFileReader,
                mappingFileReader, mappedClassesFile, unmappedClassesFile, racFile);

        RacCreatorMain.LOG.info("RAC creator configuration");
        final Execution<Configuration> analysis = new Execution<>(configuration);
        RacCreatorMain.LOG.info("start");
        analysis.executeBlocking();
        RacCreatorMain.LOG.info("complete");

        // final RacCreator racCreator = new RacCreator(RacCreatorMain.inputPath,
        // RacCreatorMain.repositoryFile,
        // RacCreatorMain.mappingFile, RacCreatorMain.outputPath);
        //
        // racCreator.execute();

        System.exit(0);

    }

    private boolean fileReadOk(final File file, final String label) throws IOException {
        if (!file.exists()) {
            RacCreatorMain.LOG.error(label + " " + file.getCanonicalPath() + " does not exist.");
            return false;
        }
        if (!file.isFile()) {
            RacCreatorMain.LOG.error(label + " " + file.getCanonicalPath() + " is not a file.");
            return false;
        }
        if (!file.canRead()) {
            RacCreatorMain.LOG.error(label + " " + file.getCanonicalPath() + " cannot be read.");
            return false;
        }

        return true;
    }

}
