package org.iobserve.planning;

import org.eclipse.emf.common.util.URI;
import org.iobserve.planning.peropteryx.ExecutionWrapper;

import teetime.stage.basic.AbstractTransformation;

/**
 * This class creates potential migration candidates via PerOpteryx.
 * 
 * @author Philipp Weimann
 */
public class CandidateCreation extends AbstractTransformation<URI, CandidateInformations> {

	private final URI perOpteryxDir;

	public CandidateCreation(final URI perOpteryxDir) {
		this.perOpteryxDir = perOpteryxDir;
	}

	@Override
	protected void execute(URI element) throws Exception {

		ExecutionWrapper exec = new ExecutionWrapper(element, this.perOpteryxDir);

		int result = exec.startModelGeneration();
	}

}
