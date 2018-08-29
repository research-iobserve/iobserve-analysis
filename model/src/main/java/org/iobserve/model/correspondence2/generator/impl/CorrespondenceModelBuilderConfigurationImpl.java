package org.iobserve.model.correspondence2.generator.impl;

import org.eclipse.emf.common.util.URI;
import org.iobserve.model.correspondence2.generator.CorrespondenceModelBuilderConfiguration;

import org.iobserve.model.correspondence2.HighLevelModel;
import org.iobserve.model.correspondence2.LowLevelModel;

/**
 * Implementation of {@link CorrespondenceModelBuilderConfiguration}. 
 * 
 * <br><br> Nothing exciting here, therefore a 
 * cute little whale..Jippy :-) 
 * 
 * <pre>       __   __
              __ \ / __
             /  \ | /  \
                 \|/
            _,.---v---._
   /\__/\  /            \
   \_  _/ /              \ 
     \ \_|           @ __|
      \                \_
       \     ,__/       /
     ~~~`~~~~~~~~~~~~~~/~~~~
 * </pre>
 * 
 * @author Robert Heinrich
 * @author Alessandro Giusa
 *
 */
final class CorrespondenceModelBuilderConfigurationImpl implements CorrespondenceModelBuilderConfiguration {

	/**output uri.*/
	private URI output;
	
	/**uri to repository*/
	private URI uriRepository;
	
	/**the high-level model instance.*/
	private HighLevelModel hlm;
	
	/**the low-level model instance.*/
	private LowLevelModel llm;
	
	CorrespondenceModelBuilderConfigurationImpl(final URI theModelOutput, final URI theRepositoryURI) {
		this.output = theModelOutput;
		this.uriRepository = theRepositoryURI;
	}

	@Override 
	public URI getOutput() {
		return this.output;
	}

	public void setHighLevelModel(final HighLevelModel model) {
		this.hlm = model;
	}

	@Override
	public HighLevelModel getHighLevelModel() {
		return this.hlm;
	}

	public void setLowLevelModel(final LowLevelModel model) {
		this.llm = model;
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
