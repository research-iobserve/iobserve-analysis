package org.iobserve.analysis;

import java.io.File;

import org.iobserve.analysis.model.ModelProviderPlatform;

import teetime.stage.InitialElementProducer;
import teetime.stage.className.ClassNameRegistryRepository;
import teetime.stage.io.filesystem.Dir2RecordsFilter;

/**
 * 
 * @author Reiner Jung
 *
 */
public class FileObservationConfiguration extends AbstractObservationConfiguration {

	final InitialElementProducer<File> files;
	final Dir2RecordsFilter reader;
	
	public FileObservationConfiguration(File directory, ModelProviderPlatform platform) {
		super(platform);
		
		files = new InitialElementProducer<File>(directory);
		reader = new Dir2RecordsFilter(new ClassNameRegistryRepository());
		
		/** connecting filters */
		connectPorts(files.getOutputPort(), reader.getInputPort());
		connectPorts(reader.getOutputPort(), this.recordSwitch.getInputPort());
	}

}
