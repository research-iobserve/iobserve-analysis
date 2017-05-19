package org.iobserve.planning;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaption.data.AdapdationData;
import org.iobserve.planning.peropteryx.ExecutionWrapper;

import teetime.stage.basic.AbstractTransformation;

/**
 * This class creates potential migration candidates via PerOpteryx.
 * 
 * @author Philipp Weimann
 */
public class CandidateCreation extends AbstractTransformation<AdapdationData, CandidateInformations> {
	
	private final static int EXEC_SUCCESS = 0;
	private final static int EXEC_ERROR = 1;
	
	private final URI perOpteryxDir;

	public CandidateCreation(final URI perOpteryxDir) {
		this.perOpteryxDir = perOpteryxDir;
	}

	@Override
	protected void execute(AdapdationData element) throws Exception {
		
		AdapdationData adapdationData = new AdapdationData();
		adapdationData.setRuntimeModelURI(element);

		ExecutionWrapper exec = new ExecutionWrapper(element, this.perOpteryxDir);

		int result = exec.startModelGeneration();
	}

}
