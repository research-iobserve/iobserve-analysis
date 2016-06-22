package org.iobserve.analysis.model;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.correspondence.CorrespondeceModelFactory;
import org.iobserve.analysis.correspondence.ICorrespondence;



public final class ModelProviderPlatform {
	
	private RepositoryModelProvider repositoryModelProvider;
	private UsageModelProvider usageModelProvider;
	private AllocationModelProvider allocationModelProvider;
	private ResourceEnvironmentModelProvider resourceEnvironmentModelProvider;
	private SystemModelProvider systemModelProvider;
	private ICorrespondence correspondenceModel;
	
	// *****************************************************************
	// CONFIGURATION
	// *****************************************************************
	
	public ModelProviderPlatform(final String pathPcm) {
		final File dirPcm = new File(pathPcm);
		if (!dirPcm.exists()) {
			throw new NullPointerException(
					String.format("the pcm dir %s does not exist?!", pathPcm));
		}
		this.createModelProviders(dirPcm);
	}
	
	private void createModelProviders(final File dirPcm) {
		final File[] files = dirPcm.listFiles();
		for (final File nextFile : files) {
			final String extension = this.getFileExtension(nextFile.getName());
			if (extension.equalsIgnoreCase("repository")) {
				final URI uri = this.getUri(nextFile);
				this.repositoryModelProvider = new RepositoryModelProvider(uri, this);
				
			} else if (extension.equalsIgnoreCase("allocation")) {
				final URI uri = this.getUri(nextFile);
				this.allocationModelProvider = new AllocationModelProvider(uri, this);
				
			} else if (extension.equalsIgnoreCase("resourceenvironment")) {
				final URI uri = this.getUri(nextFile);
				this.resourceEnvironmentModelProvider = new ResourceEnvironmentModelProvider(uri, this);
				
			} else if (extension.equalsIgnoreCase("system")) {
				final URI uri = this.getUri(nextFile);
				this.systemModelProvider = new SystemModelProvider(uri, this);
				
			} else if (extension.equalsIgnoreCase("usagemodel")) {
				final URI uri = this.getUri(nextFile);
				this.usageModelProvider = new UsageModelProvider(uri, this);
				
			} else if (extension.equalsIgnoreCase("rac")) {
				final String pathMappingFile = nextFile.getAbsolutePath();
				this.correspondenceModel = CorrespondeceModelFactory.INSTANCE
						.createCorrespondenceModel(pathMappingFile,
								CorrespondeceModelFactory.INSTANCE.DEFAULT_OPERATION_SIGNATURE_MAPPER_2);
			}
		}
	}
	
	// *****************************************************************
	// GET MODEL PROVIDERS
	// *****************************************************************
	
	public AllocationModelProvider getAllocationModelProvider() {
		return this.allocationModelProvider;
	}
	
	public ResourceEnvironmentModelProvider getResourceEnvironmentModelProvider() {
		return this.resourceEnvironmentModelProvider;
	}
	
	public SystemModelProvider getSystemModelProvider() {
		return this.systemModelProvider;
	}
	
	public UsageModelProvider getUsageModelProvider() {
		return this.usageModelProvider;
	}
	
	public ICorrespondence getCorrespondenceModel() {
		return this.correspondenceModel;
	}
	
	public RepositoryModelProvider getRepositoryModelProvider() {
		return this.repositoryModelProvider;
	}
	
	// *****************************************************************
	// HELPER
	// *****************************************************************
	
	private String getFileExtension(final String path) {
		return path.substring(path.lastIndexOf(".") + 1, path.length());
	}
	
	private URI getUri(final File file) {
		return URI.createFileURI(file.getAbsolutePath());
	}
	
	

}
