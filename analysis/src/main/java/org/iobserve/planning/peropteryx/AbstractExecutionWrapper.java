package org.iobserve.planning.peropteryx;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.emf.common.util.URI;

/**
 * This class is an abstract execution warpper for starting an headless
 * PerOpteryx instance.
 * 
 * @author Philipp Weimann
 *
 */
public abstract class AbstractExecutionWrapper {

	protected static final Logger LOG = LogManager.getLogger(ExecutionWrapper.class);

	private final URI inputModelDir;
	private final URI perOpteryxDir;
	private final URI lqnsDir;
	private final URI privacyAnalysisFile;

	/**
	 * The constructor
	 * 
	 * @param inputModelDir
	 *            the directory containing the pcm model for modification
	 * @param perOpteryxDir
	 *            the headless PerOpteryx directory
	 */
	public AbstractExecutionWrapper(URI inputModelDir, URI perOpteryxDir, URI lqnsDir, URI privacyAnalysisFile) {
		this.inputModelDir = inputModelDir;
		this.perOpteryxDir = perOpteryxDir;
		this.lqnsDir = lqnsDir;
		this.privacyAnalysisFile = privacyAnalysisFile;
	}

	/**
	 * Generates and starts the execution of PerOpteryx. The main thread gets
	 * blocked until the execution is finished.
	 * 
	 * @return the process exit value
	 */
	public int startModelGeneration() {
		final ProcessBuilder processBuilder = this.createProcess();
		Process process = null;
		try {
			process = processBuilder.start();
			this.watch(process);
			return process.waitFor();
		} catch (IOException | InterruptedException e) {
			LOG.error("Could not start PerOpteryx executable process. Check the parameters and the log files!", e);
			return -1;
		}
	}

	/**
	 * This method creates the PerOpteryx headless process with all the command
	 * line arguments. It doesn't start the process.
	 * 
	 * @return the PerOpteryx process
	 */
	public abstract ProcessBuilder createProcess();

	/**
	 * This method watches the process, writes the process output into the
	 * console and blocks the main thread until PerOpteryx finishes execution.
	 * 
	 * @param process
	 *            the process executing the PerOpteryx headless
	 */
	public abstract void watch(final Process process) throws InterruptedException;

	/*
	 * HELPERS
	 */
	/**
	 * @return the inputModelDir
	 */
	protected URI getInputModelDir() {
		return inputModelDir;
	}

	/**
	 * @return the perOpteryxDir
	 */
	protected URI getPerOpteryxDir() {
		return perOpteryxDir;
	}

	/**
	 * @return theLQNDir
	 */
	protected URI getLQNSDir() {
		return this.lqnsDir;
	}

	/**
	 * @return the privacyAnalysisFile
	 */
	protected URI getPrivacyAnalysisFile() {
		return privacyAnalysisFile;
	}

}
