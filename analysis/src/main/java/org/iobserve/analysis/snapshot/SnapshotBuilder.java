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
package org.iobserve.analysis.snapshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.model.provider.AbstractModelProvider;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

/**
 * This class creates a copy of the current PCM runtime model. (also called Snapshot) The output
 * port contains the send URI of the snapshot.
 *
 * @author Philipp Weimann
 */
public class SnapshotBuilder extends AbstractStage {

    protected static final Logger LOG = LogManager.getLogger(SnapshotBuilder.class);

    private static URI baseSnapshotLocation = null;
    private static CopyOption[] copyOptions = new CopyOption[] { StandardCopyOption.REPLACE_EXISTING,
            StandardCopyOption.COPY_ATTRIBUTES };

    private static boolean createSnapshot;
    private static boolean evaluationMode;

    private final URI snapshotURI;
    private final InitializeModelProviders modelProviders;

    private final OutputPort<URI> outputPort = super.createOutputPort();
    private final OutputPort<URI> evaluationOutputPort = super.createOutputPort();

    /**
     * The constructor.
     *
     * @param subURI
     *            where the snapshot will be saved to
     * @param modelProviders
     *            the source pcm models
     * @throws InitializationException
     */
    public SnapshotBuilder(final String subURI, final InitializeModelProviders modelProviders)
            throws InitializationException {
        super();

        if (SnapshotBuilder.baseSnapshotLocation == null) {
            throw new InitializationException(
                    "Intitialize baseSnapshotLocation via setBaseSnapshotURI(...) first, befor calling the constructor!");
        }

        SnapshotBuilder.createSnapshot = false;
        this.snapshotURI = SnapshotBuilder.baseSnapshotLocation.appendSegment(subURI);
        this.modelProviders = modelProviders;

        final String fileString = this.snapshotURI.toFileString();
        final File baseFolder = new File(fileString);
        if (!baseFolder.exists()) {
            baseFolder.mkdirs();
        } else {
            for (final File file : baseFolder.listFiles()) {
                if (!file.isDirectory()) {
                    file.delete();
                }
            }
        }
    }

    @Override
    protected void execute() throws Exception {
        // TODO fix this and make .recieve() possible!
        // List<InputPort<?>> inputPorts = super.getInputPorts();
        // Boolean createSnapshot = this.inputPort.receive();

        if (SnapshotBuilder.createSnapshot) {
            this.createSnapshot();
            SnapshotBuilder.createSnapshot = false;

            if (SnapshotBuilder.evaluationMode) {
                this.evaluationOutputPort.send(this.snapshotURI);
            } else {
                this.outputPort.send(this.snapshotURI);
            }
        }
    }

    public URI createSnapshot() throws IOException {
        SnapshotBuilder.LOG.info("Creating Snapshot: \t" + this.snapshotURI.toFileString());

        this.createModelSnapshot(this.modelProviders.getAllocationModelProvider());
        this.createModelSnapshot(this.modelProviders.getRepositoryModelProvider());
        this.createModelSnapshot(this.modelProviders.getResourceEnvironmentModelProvider());
        this.createModelSnapshot(this.modelProviders.getSystemModelProvider());
        this.createModelSnapshot(this.modelProviders.getUsageModelProvider());
        this.createModelSnapshot(this.modelProviders.getCloudProfileModelProvider());
        this.createModelSnapshot(this.modelProviders.getCostModelProvider());
        this.createModelSnapshot(this.modelProviders.getDesignDecisionModelProvider());
        this.createModelSnapshot(this.modelProviders.getQMLDeclarationsModelProvider());
        // this.createModelSnapshot(modelProviders.getCorrespondenceModel());
        return this.snapshotURI;
    }

    /**
     * Creates the actual snapshot.
     *
     * @param modelProvider
     *            the model for the snapshot
     * @throws IOException
     *             if the model URI does not exist
     */
    public void createModelSnapshot(final AbstractModelProvider<?> modelProvider) throws IOException {
        if (modelProvider == null) {
            return;
        }

        modelProvider.save();

        final URI modelURI = modelProvider.getModelUri();

        final File modelFile = new File(modelURI.toFileString());
        if (!modelFile.exists()) {
            throw new IOException("The given file URI did not point to a file");
        }
        final String fileName = modelFile.getName();
        final String targetFileLocation = this.snapshotURI.toFileString() + File.separator + fileName;
        final File modelFileCopy = new File(targetFileLocation);

        Files.copy(modelFile.toPath(), modelFileCopy.toPath(), SnapshotBuilder.copyOptions);
    }

    /**
     * Sets the base location for all snapshots. Must be called before initializing the
     * SnapshotBuilder.
     *
     * @param baseSnapshotURI
     *            URI base location
     */
    public static void setBaseSnapshotURI(final URI baseSnapshotURI) {
        SnapshotBuilder.baseSnapshotLocation = baseSnapshotURI;
    }

    /**
     * Return the snapshotURI.
     *
     * @return snapshot URI
     */
    public URI getSnapshotURI() {
        return this.snapshotURI;
    }

    /**
     * call this function if a snapshot should be created upon snapshot execution.
     */
    public static void setSnapshotFlag() {
        SnapshotBuilder.createSnapshot = true;
    }

    /**
     * @param evaluationMode
     *            the evaluationMode to set
     */
    public static void setEvaluationMode(final boolean evaluationMode) {
        SnapshotBuilder.evaluationMode = evaluationMode;
    }

    /**
     * @return the input port for the teetime framework
     */
    public InputPort<Boolean> getInputPort() {
        return super.createInputPort();
    }

    /**
     * @return the output port for the teetime framework, transmitting the snapshot location as URI
     */
    public OutputPort<URI> getOutputPort() {
        return this.outputPort;
    }

    /**
     * @return the evaluationOutputPort
     */
    public OutputPort<URI> getEvaluationOutputPort() {
        return this.evaluationOutputPort;
    }

}
