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

	private static final Logger LOG = LogManager.getLogger(ExecutionWrapper.class);

	private final URI inputModelDir;
	private final URI perOpteryxDir;

	/**
	 * The constructor
	 * 
	 * @param inputModelDir the directory containing the pcm model for modification
	 * @param perOpteryxDir the headless PerOpteryx directory
	 */
	public AbstractExecutionWrapper(URI inputModelDir, URI perOpteryxDir) {
		this.inputModelDir = inputModelDir;
		this.perOpteryxDir = perOpteryxDir;
	}

	/**
	 * Generates and starts the execution of PerOpteryx. The main thread gets
	 * blocked until the execution is finished.
	 * 
	 * @return the process exit value
	 */
	public int startModelGeneration() {
		final Process process = this.createProcess();
		this.watch(process);
		return process.exitValue();
	}

	/**
	 * This method creates the PerOpteryx headless process with all the command
	 * line arguments. It doesn't start the process.
	 * 
	 * @return the PerOpteryx process
	 */
	public abstract Process createProcess();

	/**
	 * This method watches the process, writes the process output into the
	 * console and blocks the main thread until PerOpteryx finishes execution.
	 * 
	 * @param process
	 *            the process executing the PerOpteryx headless
	 */
	public abstract void watch(final Process process);
}
