package org.iobserve.analysis.snapshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.model.AbstractModelProvider;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

/**
 * This class creates a copy of the current PCM runtime model. (also called
 * Snapshot) The output port contains the send URI of the snapshot.
 *
 * @author Philipp Weimann
 */
public class SnapshotBuilder extends AbstractStage {

	private static URI baseSnapshotLocation = null;
	private static CopyOption[] copyOptions = new CopyOption[] { StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES };

	private static boolean createSnapshot;
	private static boolean evaluationMode;

	private final URI snapshotURI;
	private final InitializeModelProviders modelProviders;

	private final OutputPort<URI> outputPort = super.createOutputPort();
	private final OutputPort<URI> evaluationOutputPort = super.createOutputPort();

	/**
	 * The constructor.
	 *
	 * @param snapshotURI
	 *            where the snapshot will be saved to
	 * @param modelProviders
	 *            the source pcm models
	 * @throws InitializationException
	 */
	public SnapshotBuilder(final String subURI, final InitializeModelProviders modelProviders) throws InitializationException {
		super();

		if (SnapshotBuilder.baseSnapshotLocation == null)
			throw new InitializationException("Intitialize baseSnapshotLocation via setBaseSnapshotURI(...) first, befor calling the constructor!");

		SnapshotBuilder.createSnapshot = false;
		this.snapshotURI = SnapshotBuilder.baseSnapshotLocation.appendSegment(subURI);
		this.modelProviders = modelProviders;

		String fileString = this.snapshotURI.toFileString();
		File baseFolder = new File(fileString);
		if (!baseFolder.exists())
			baseFolder.mkdirs();
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

	/**
	 * Creates a Snapshot to the location specified during instance creation.
	 * 
	 * @throws IOException
	 */
	public URI createSnapshot() throws IOException {
		this.createModelSnapshot(this.modelProviders.getAllocationModelProvider());
		this.createModelSnapshot(this.modelProviders.getRepositoryModelProvider());
		this.createModelSnapshot(this.modelProviders.getResourceEnvironmentModelProvider());
		this.createModelSnapshot(this.modelProviders.getSystemModelProvider());
		this.createModelSnapshot(this.modelProviders.getUsageModelProvider());
		this.createModelSnapshot(this.modelProviders.getCloudProfileModelProvider());
		this.createModelSnapshot(this.modelProviders.getCostModelProvider());
		this.createModelSnapshot(this.modelProviders.getDesignDecisionModelProvider());
		// this.createModelSnapshot(modelProviders.getCorrespondenceModel());
		return this.snapshotURI;
	}

	/*
	 * Creates the actual copy
	 */
	private void createModelSnapshot(AbstractModelProvider<?> modelProvider) throws IOException {
		if (modelProvider == null) {
			return;
		}

		modelProvider.save();

		URI modelURI = modelProvider.getModelUri();

		File modelFile = new File(modelURI.toFileString());
		if (!modelFile.exists()) {
			throw new IOException("The given file URI did not point to a file");
		}
		String fileName = modelFile.getName();
		String targetFileLocation = this.snapshotURI.toFileString() + File.separator + fileName;
		File modelFileCopy = new File(targetFileLocation);

		Files.copy(modelFile.toPath(), modelFileCopy.toPath(), SnapshotBuilder.copyOptions);
	}

	/**
	 * Sets the base location for all snapshots. Must be called before
	 * initializing the SnapshotBuilder.
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
	 * call this function if a snapshot should be created upon snapshot
	 * execution.
	 */
	public static void setSnapshotFlag() {
		SnapshotBuilder.createSnapshot = true;
	}

	/**
	 * @param evaluationMode
	 *            the evaluationMode to set
	 */
	public static void setEvaluationMode(boolean evaluationMode) {
		SnapshotBuilder.evaluationMode = evaluationMode;
	}

	/**
	 * @return the input port for the teetime framework
	 */
	public InputPort<Boolean> getInputPort() {
		return super.createInputPort();
	}

	/**
	 * @return the output port for the teetime framework, transmitting the
	 *         snapshot location as URI
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
