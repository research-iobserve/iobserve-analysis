package org.iobserve.model.correspondence2.generator.impl;

import org.eclipse.emf.common.util.URI;
import org.iobserve.model.correspondence2.generator.CorrespondenceModelBuilder;
import org.iobserve.model.correspondence2.generator.CorrespondenceModelBuilderConfiguration;

import org.iobserve.model.correspondence2.CorrespondenceModel;

/**
 * Factory to create a {@link CorrespondenceModelBuilder} instance.
 * @author Robert Heinrich
 * @author Alessandro Giusa
 *
 */
public final class CorrespondenceModelBuilderFactory {
	
	/**
	 * Create a new instance of {@link CorrespondenceModelBuilder}.
	 * @param theConfiguration the configuration needed to create the builder.
	 * @return new instance.
	 */
	public static CorrespondenceModelBuilder createBuilder(final CorrespondenceModelBuilderConfiguration theConfiguration) {
		return new CorrespondenceModelBuilderImpl(theConfiguration);
	}
	
	/**
	 * Create a new instance of {@link CorrespondenceModelBuilderConfiguration}.
	 * @param theModelOutput the output path where the builder should write the {@link CorrespondenceModel}.
	 * @param theRepositoryUri the URI to the PCM repository.
	 * @return the configuration.
	 */
	public static CorrespondenceModelBuilderConfiguration createConfiguration(final URI theModelOutput, final URI theRepositoryUri) {
		return new CorrespondenceModelBuilderConfigurationImpl(theModelOutput, theRepositoryUri);
	}
	
	/**
	 * Create a new instance of {@link CorrespondenceModelBuilderConfiguration}.
	 * @param theModelOutput the output file path where the builder should write the {@link CorrespondenceModel}.
	 * @param theRepositoryUri the URI to the PCM repository.
	 * @return the configuration.
	 */
	public static CorrespondenceModelBuilderConfiguration createConfiguration(final String fileUriToModelOutput, final String theRepositoryUri) {
		return new CorrespondenceModelBuilderConfigurationImpl(URI.createFileURI(fileUriToModelOutput), URI.createFileURI(theRepositoryUri));
	}

}
