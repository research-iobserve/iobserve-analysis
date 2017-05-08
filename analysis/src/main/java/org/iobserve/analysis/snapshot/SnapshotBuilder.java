package org.iobserve.analysis.snapshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.imageio.IIOException;

import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.model.AbstractModelProvider;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

public class SnapshotBuilder extends AbstractStage {

	private static CopyOption[] copyOptions = new CopyOption[] { StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES };

	private static boolean createSnapshot;
	private final URI snapshotURI;
	private final InitializeModelProviders modelProviders;

	// private final InputPort<Boolean> inputPort = super.createInputPort();
	private final OutputPort<URI> outputPort = super.createOutputPort();

	public SnapshotBuilder(final URI snapshotURI, final InitializeModelProviders modelProviders) {
		super();

		SnapshotBuilder.createSnapshot = false;
		this.snapshotURI = snapshotURI;
		this.modelProviders = modelProviders;
	}

	@Override
	protected void execute() throws Exception {
		// TODO fix this and make .recieve() possible!
		// List<InputPort<?>> inputPorts = super.getInputPorts();
		// Boolean createSnapshot = this.inputPort.receive();

		if (SnapshotBuilder.createSnapshot) {
			this.createModelSnapshot(modelProviders.getAllocationModelProvider());
			this.createModelSnapshot(modelProviders.getRepositoryModelProvider());
			this.createModelSnapshot(modelProviders.getResourceEnvironmentModelProvider());
			this.createModelSnapshot(modelProviders.getSystemModelProvider());
			this.createModelSnapshot(modelProviders.getUsageModelProvider());
			// this.createModelSnapshot(modelProviders.getCorrespondenceModel());
			SnapshotBuilder.createSnapshot = false;

			outputPort.send(this.snapshotURI);
		}
	}

	private void createModelSnapshot(AbstractModelProvider<?> modelProvider) throws IOException {
		modelProvider.save();

		URI modelURI = modelProvider.getModelUri();

		File modelFile = new File(modelURI.path());
		if (!modelFile.exists()) {
			throw new IIOException("The given file URI did not point to a file");
		}
		String fileName = modelFile.getName();
		String targetFileLocation = this.snapshotURI.toFileString() + File.separator + fileName;
		File modelFileCopy = new File(targetFileLocation);

		Files.copy(modelFile.toPath(), modelFileCopy.toPath(), SnapshotBuilder.copyOptions);
	}

	public static void setSnapshotFlag() {
		SnapshotBuilder.createSnapshot = true;
	}

	public InputPort<Boolean> getInputPort() {
		return super.createInputPort();
	}
	
	public OutputPort<URI> getOutputPort()
	{
		return this.outputPort;
	}

}