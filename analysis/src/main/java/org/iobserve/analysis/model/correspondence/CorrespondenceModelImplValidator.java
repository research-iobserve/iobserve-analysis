package org.iobserve.analysis.model.correspondence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;

import giusa.software.framework.table.Table;
import giusa.software.framework.table.TableHeader;
import giusa.software.tools.parsing.csv.CSVParser;

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
	
	/**Counter for all wrong results*/
	private int cntWrongResults = 0;
	
	private final Map<String, String> testMap;

	CorrespondenceModelImplValidator(final URI pathToCM, final File theOutputFile, final Map<String, String> theTestMap, final ICorrespondence implUnderTest) {
		this.implUnderTest = implUnderTest;
		this.outputFile = theOutputFile;
		this.testMap = theTestMap;
	}

	@Override
	public boolean containsCorrespondent(final String classSig, final String funcionSig) {
		final boolean result = this.implUnderTest.containsCorrespondent(classSig, funcionSig);
		this.cntInvocations++;
		this.testContainsCorrespondent(classSig, funcionSig, result);
		this.writeResults();
		return result;
	}

	@Override
	public Optional<Correspondent> getCorrespondent(final String classSig, final String functionSig) {
		final Optional<Correspondent> result = this.implUnderTest.getCorrespondent(classSig, functionSig);
		this.cntInvocations++;
		this.testGetCorrespondent(classSig, functionSig, result);
		this.writeResults();
		return result;
	}

	@Override
	public Optional<Correspondent> getCorrespondent(final String classSig) {
		final Optional<Correspondent> result = this.implUnderTest.getCorrespondent(classSig);
		this.cntInvocations++;
		this.testGetCorrespondent(classSig, result);
		this.writeResults();
		return result;
	}
	
	/**
	 * Write the results to file.
	 */
	private void writeResults() {
		String csv = "";
		
		// read the last state and append new results
		try (final FileInputStream input = new FileInputStream(this.outputFile)) {
			final CSVParser parser;
			if (!this.outputFile.exists()) {
				final Table<String> table = new Table<>();
				final TableHeader h1 = new TableHeader("Invocations", 0);
				final TableHeader h2 = new TableHeader("CorrectResults", 1);
				final TableHeader h3 = new TableHeader("WrongResults", 2);
				table.addHeader(h1);
				table.addHeader(h2);
				table.addHeader(h3);
				parser = new CSVParser();
				parser.setModel(table);
			} else {
				parser = new CSVParser();
				parser.parse(input);
			}
			
			final Table<String> table = parser.getModel();
			final int row = table.size();
			table.addColumn(row, 0, String.valueOf(this.cntInvocations), true);
			table.addColumn(row, 1, String.valueOf(this.cntCorrectResults), true);
			table.addColumn(row, 2, String.valueOf(this.cntWrongResults), true);
			csv = parser.toCSV();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// write the output
		try (final PrintWriter output = new PrintWriter(this.outputFile)) {
			output.write(csv);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	// ************************************************************************
	// TEST METHODS
	// ************************************************************************
	
	/**
	 * Test if the expected answer matches the one got from the
	 * {@link ICorrespondence} implementation.
	 * 
	 * @param classSig
	 *            class signature
	 * @param funcionSig
	 *            function signature assuming that the functionsSig contains
	 *            also the full qualified class name like
	 *            org.this.package.is.Nice.foo
	 * @param result
	 *            the result got from the {@link ICorrespondence} implementation
	 */
	private void testContainsCorrespondent(final String classSig, final String funcionSig, final boolean result) {
		final boolean resultIsCorrect = (result == this.testMap.containsKey(funcionSig));
		if (resultIsCorrect) {
			this.cntCorrectResults++;
		} else {
			this.cntWrongResults++;
		}
	}

	/**
	 * Test if the expected answer matches the one got from the
	 * {@link ICorrespondence} implementation.
	 * 
	 * @param classSig
	 *            class signature
	 * @param funcionSig
	 *            function signature assuming that the functionsSig contains
	 *            also the full qualified class name like
	 *            org.this.package.is.Nice.foo
	 * @param result
	 *            the result got from the {@link ICorrespondence} implementation
	 */
	private void testGetCorrespondent(final String classSig, final String functionSig, final Optional<Correspondent> result) {
		final String entityId = this.testMap.get(functionSig);
		if (entityId != null) {
			final boolean resultIsCorrect = result.isPresent() && result.get().getPcmOperationId().equals(entityId);
			if (resultIsCorrect) {
				this.cntCorrectResults++;
			} else {
				this.cntWrongResults++;
			}
		} else {
			final boolean resultIsCorrect = !result.isPresent();
			if (resultIsCorrect) {
				this.cntCorrectResults++;
			} else {
				this.cntWrongResults++;
			}
		}
	}

	/**
	 * Test if the expected answer matches the one got from the
	 * {@link ICorrespondence} implementation.
	 * 
	 * @param classSig
	 *            class signature
	 * @param result
	 *            the result got from the {@link ICorrespondence} implementation
	 */
	private void testGetCorrespondent(final String classSig, final Optional<Correspondent> result) {
		final String entityId = this.testMap.get(classSig);
		if (entityId != null) {
			final boolean resultIsCorrect = result.isPresent() && result.get().getPcmEntityId().equals(entityId);
			if (resultIsCorrect) {
				this.cntCorrectResults++;
			} else {
				this.cntWrongResults++;
			}
		} else {
			final boolean resultIsCorrect = !result.isPresent();
			if (resultIsCorrect) {
				this.cntCorrectResults++;
			} else {
				this.cntWrongResults++;
			}
		}
	}
}
