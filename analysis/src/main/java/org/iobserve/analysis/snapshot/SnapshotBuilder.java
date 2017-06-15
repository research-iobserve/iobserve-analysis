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
import org.iobserve.analysis.model.AbstractModelProvider;
import org.iobserve.analysis.model.QMLDeclarationsModelProvider;

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

	protected static final Logger LOG = LogManager.getLogger(SnapshotBuilder.class);

	private static URI baseSnapshotLocation = null;
	private static CopyOption[] copyOptions = new CopyOption[] { StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES };
	private static String FileEnding_QMLContractType = "qmlcontracttype";

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
		else {
			for (File file : baseFolder.listFiles())
				if (!file.isDirectory())
					file.delete();
		}
	}

	@Override
	protected void execute() throws Exception {

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
		LOG.info("Creating Snapshot: \t" + this.snapshotURI.toFileString());

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
		
		this.copyQMLContractFiles(this.modelProviders.getQMLDeclarationsModelProvider());
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
	public void createModelSnapshot(AbstractModelProvider<?> modelProvider) throws IOException {
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

	/*
	 * Copies all qmlcontracttype-files to the designated base folder.
	 */
	private void copyQMLContractFiles(QMLDeclarationsModelProvider qmlModelProver) throws IOException {
		if (qmlModelProver == null) {
			return;
		}
		URI modelURI = qmlModelProver.getModelUri();

		File modelFile = new File(modelURI.toFileString());
		if (!modelFile.exists()) {
			throw new IOException("The given file URI did not point to a file");
		}

		File parentFile = modelFile.getParentFile();
		for (File sourceFile : parentFile.listFiles()) {
			if (sourceFile.getName().endsWith(FileEnding_QMLContractType))
			{
				String targetFileLocation = this.snapshotURI.toFileString() + File.separator + sourceFile.getName();
				File modelFileCopy = new File(targetFileLocation);
				Files.copy(sourceFile.toPath(), modelFileCopy.toPath(), SnapshotBuilder.copyOptions);
			}
		}
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
