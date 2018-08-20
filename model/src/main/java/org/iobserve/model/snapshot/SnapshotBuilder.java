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
package org.iobserve.model.snapshot;

import java.io.File;
import java.io.IOException;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.eclipse.emf.common.util.URI;
import org.iobserve.model.ModelImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class creates a copy of the current PCM runtime model. (also called Snapshot) The output
 * port contains the send URI of the snapshot.
 *
 * @author Philipp Weimann
 */
public class SnapshotBuilder extends AbstractStage {

    protected static final Logger LOGGER = LoggerFactory.getLogger(SnapshotBuilder.class);

    private static URI baseSnapshotLocation;
    // private static CopyOption[] copyOptions = new CopyOption[] {
    // StandardCopyOption.REPLACE_EXISTING,
    // StandardCopyOption.COPY_ATTRIBUTES };

    private static boolean canCreateSnapshot;
    private static boolean evaluationMode;

    private final URI snapshotURI;
    private final ModelImporter modelHandler;

    private final OutputPort<URI> outputPort = super.createOutputPort();
    private final OutputPort<URI> evaluationOutputPort = super.createOutputPort();

    /**
     * The constructor.
     *
     * @param subURI
     *            where the snapshot will be saved to
     * @param modelHandler
     *            the source pcm models
     * @throws InitializationException
     *             when no snapshot location was specified
     */
    public SnapshotBuilder(final String subURI, final ModelImporter modelHandler) throws InitializationException {
        super();

        if (SnapshotBuilder.baseSnapshotLocation == null) {
            throw new InitializationException(
                    "Intitialize baseSnapshotLocation via setBaseSnapshotURI(...) first, befor calling the constructor!");
        }

        SnapshotBuilder.canCreateSnapshot = false;
        this.snapshotURI = SnapshotBuilder.baseSnapshotLocation.appendSegment(subURI);
        this.modelHandler = modelHandler;

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

        if (SnapshotBuilder.canCreateSnapshot) {
            this.createSnapshot();
            SnapshotBuilder.canCreateSnapshot = false;

            if (SnapshotBuilder.evaluationMode) {
                this.evaluationOutputPort.send(this.snapshotURI);
            } else {
                this.outputPort.send(this.snapshotURI);
            }
        }
    }

    /**
     * Create an snapshot URI.
     *
     * @return the URI
     * @throws IOException
     *             on io error while producing output files
     */
    public URI createSnapshot() throws IOException {
        if (SnapshotBuilder.LOGGER.isInfoEnabled()) {
            SnapshotBuilder.LOGGER.info("Creating Snapshot: \t" + this.snapshotURI.toFileString());
        }

        this.modelHandler.save(this.snapshotURI);

        return this.snapshotURI;
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
        SnapshotBuilder.canCreateSnapshot = true;
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
