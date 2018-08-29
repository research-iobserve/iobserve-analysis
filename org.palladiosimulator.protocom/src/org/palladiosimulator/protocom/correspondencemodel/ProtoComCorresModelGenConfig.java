package org.palladiosimulator.protocom.correspondencemodel;

import org.eclipse.emf.common.util.URI;
import org.iobserve.model.correspondence2.generator.CorrespondenceModelBuilderConfiguration;

import org.iobserve.model.correspondence2.Correspondence2Factory;
import org.iobserve.model.correspondence2.HighLevelModel;
import org.iobserve.model.correspondence2.LowLevelModel;

/**
 * Special implementation of the {@link CorrespondenceModelBuilderConfiguration}. It is specialized
 * for the use with ProtoCom, which is the code generator for PCM model instances.
 * 
 * @author Robert Heinrich
 * @author Alessandro Giusa
 */
public class ProtoComCorresModelGenConfig implements CorrespondenceModelBuilderConfiguration {
	
	/**empty instance of high-level model*/
	private final HighLevelModel hlm = Correspondence2Factory.eINSTANCE.createHighLevelModel();
	
	/**empty instance of low-level model*/
	private final LowLevelModel llm = Correspondence2Factory.eINSTANCE.createLowLevelModel();
	
	/**URI for output*/
	private final URI uriOutput;
	
	private final URI uriRepository;
	
	public ProtoComCorresModelGenConfig(final URI theUriOutput, final URI theUriRepository) {
		if (theUriOutput == null) new NullPointerException("Provided uri for output path can not be null!");
		this.uriOutput = theUriOutput;
		this.uriRepository = theUriRepository;
	}
	
	public ProtoComCorresModelGenConfig(final String outputPath, final String repositoryPath) {
		if (outputPath == null) new NullPointerException("Provided uri for output path can not be null!");
		this.uriOutput = URI.createFileURI(outputPath);
		this.uriRepository = URI.createFileURI(repositoryPath);
	}

	@Override
	public URI getOutput() {
		return this.uriOutput;
	}

	@Override
	public HighLevelModel getHighLevelModel() {
		return this.hlm;
	}

	@Override
	public LowLevelModel getLowLevelModel() {
		return this.llm;
	}
	
	@Override
	public URI getRepository() {
		return this.uriRepository;
	}
}
