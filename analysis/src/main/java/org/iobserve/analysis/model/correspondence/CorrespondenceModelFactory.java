package org.iobserve.analysis.model.correspondence;

import java.io.File;

import org.eclipse.emf.common.util.URI;

/**
 * Factory to create {@link ICorrespondence} instances.
 * 
 * @author Robert Heinrich
 * @author Alessandro Giusa
 *
 */
public final class CorrespondenceModelFactory {
	
	private CorrespondenceModelFactory() {
		// hide constructor
	}
	
	/**
	 * Create a brand new instance of {@link ICorrespondence}.
	 * 
	 * @param pathToCM path to the Correspondence Model.
	 * @return instance of {@link ICorrespondence}.
	 */
	public static ICorrespondence createCorrespondence(final URI pathToCM) {
		return new CorrespondenceModelImpl(pathToCM);
	}
	
	/**
	 * Create a brand new instance of {@link ICorrespondence}, which has the capability to 
	 * validates the implementation of {@link ICorrespondence} provided by {@link #createCorrespondence(URI)}.
	 * 
	 * @param pathToCM path to the Correspondence Model.
	 * @param outputFile file to output the results of the validation.
	 * @return instance of {@link ICorrespondence}.
	 */
	public static ICorrespondence createCorrespondenceForValidation(final URI pathToCM, final File outputFile) {
		return new CorrespondenceModelImplValidator(pathToCM, outputFile, createCorrespondence(pathToCM));
	}
}
