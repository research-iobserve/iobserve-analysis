package org.iobserve.evaluation;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;

import teetime.stage.basic.AbstractFilter;
import teetime.stage.basic.AbstractTransformation;

public class ModelComparer extends AbstractTransformation<URI, Boolean> {
	
	private AdaptationData adaptationData;

	@Override
	protected void execute(URI element) throws Exception {
		
		if (adaptationData != null)
		{
			//TODO finish
		}

	}

	public void setBaseData(AdaptationData adaptationData) {
		this.adaptationData = adaptationData;
	}

}
