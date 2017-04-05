package org.iobserve.analysis.snapshot;

import java.io.File;
import java.io.IOException;

import javax.imageio.IIOException;

import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.model.AbstractModelProvider;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

public class SnapshotBuilder extends AbstractStage {

	private static boolean createSnapshot;
	private final URI snapshotURI;
	private final InitializeModelProviders modelProviders;
	
    private final InputPort<Object> inputPort = super.createInputPort();
    private final OutputPort<URI> outputPort = super.createOutputPort();

    
	public SnapshotBuilder(final URI snapshotURI, final InitializeModelProviders modelProviders) {
		super();

		this.snapshotURI = snapshotURI;
		this.modelProviders = modelProviders;

	}

	@Override
	protected void execute() throws Exception {
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
		URI modelURI = modelProvider.getModelUri();

		File modelFile = new File(modelURI.path());
		if (!modelFile.exists()) {
			throw new IIOException("The given file URI did not point to a file");
		}
		String fileName = modelFile.getName();

		URI reModelUri = URI.createFileURI(this.snapshotURI.path() + File.separator + fileName);
		modelProvider.save(reModelUri);
	}

	public static void setSnapshotFlag() {
		SnapshotBuilder.createSnapshot = true;
	}
	
	
	public InputPort<Object> getInputPort()
	{
		return this.inputPort;
	}

}
