package org.iobserve.analysis.model.correspondence;

import java.io.File;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;

/**
 * This class is supposed to validate the {@link CorrespondenceModelImpl}. By
 * applying the idea of the Proxy-Design-Pattern, this class is able to
 * intercept each of the interface methods of {@link ICorrespondence}
 * implemented by the implementation under test (implUnderTest).
 * 
 * @author Robert Heinrich
 * @author Alessandro Giusa
 *
 */
final class CorrespondenceModelImplValidator implements ICorrespondence {

	/**the implementation under test.*/
	private final ICorrespondence implUnderTest;
	
	/**file to save the results.*/
	private final File outputFile;
	
	/**Counter for overall invocations of interface methods*/
	private int cntInvocations = 0;
	
	/**Counter for all correct results*/
	private int cntCorrectResults = 0;

	CorrespondenceModelImplValidator(final URI pathToCM, final File theOutputFile, final ICorrespondence implUnderTest) {
		this.implUnderTest = implUnderTest;
		this.outputFile = theOutputFile;
	}

	@Override
	public boolean containsCorrespondent(final String classSig, final String funcionSig) {
		final boolean result = this.implUnderTest.containsCorrespondent(classSig, funcionSig);
		if (!this.testContainsCorrespondent(classSig, funcionSig, result)) {
			// TODO do something
		}
		return result;
	}

	@Override
	public Optional<Correspondent> getCorrespondent(final String classSig, final String functionSig) {
		final Optional<Correspondent> result = this.implUnderTest.getCorrespondent(classSig, functionSig);
		if (!this.testGetCorrespondent(classSig, functionSig, result)) {
			// TODO do something
		}
		return result;
	}

	@Override
	public Optional<Correspondent> getCorrespondent(final String classSig) {
		final Optional<Correspondent> result = this.implUnderTest.getCorrespondent(classSig);
		if (!this.testGetCorrespondent(classSig, result)) {
			// TODO do something
		}
		return result;
	}
	
	// ************************************************************************
	// TEST METHODS
	// ************************************************************************
	
	private boolean testContainsCorrespondent(final String classSig, final String funcionSig, final boolean result) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean testGetCorrespondent(final String classSig, final String functionSig, final Optional<Correspondent> result) {
		
		return false;
	}

	private boolean testGetCorrespondent(final String classSig, final Optional<Correspondent> result) {
		
		return false;
	}

}
