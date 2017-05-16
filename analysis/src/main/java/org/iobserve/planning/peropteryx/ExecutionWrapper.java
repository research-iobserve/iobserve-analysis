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
 * Linux specific wrapper, uses bash to call headless PerOpteryx.
 *
 * @author Tobias Pöppke
 *
 */
public class ExecutionWrapper {
	private static final Logger LOG = LogManager.getLogger(ExecutionWrapper.class);

	private final URI inputModelDir;
	private final URI perOpteryxDir;

	@Inject
	public ExecutionWrapper(URI inputModelDir, URI perOpteryxDir) {
		this.inputModelDir = inputModelDir;
		this.perOpteryxDir = perOpteryxDir;
	}

	public int startPerOpteryxProcess() {
		LOG.info("Starting optimization process...");
		// TODO exchange this with values from parameters
		String perOpteryxCommand = this.perOpteryxDir.appendSegment("peropteryx-headless").path();
		String workingDir = this.inputModelDir.path();

		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", perOpteryxCommand + " -w " + workingDir);

		LOG.info(String.format("PerOpteryx start parameters: {Command: '/bin/bash -c %s', working-dir: '%s'}",
				perOpteryxCommand, workingDir));

		builder.directory(new File(this.perOpteryxDir.path()));
		builder.redirectErrorStream(true);

		try {
			final Process process = builder.start();
			watch(process);
			return process.exitValue();
		} catch (IOException | InterruptedException e) {
			LOG.error("Could not start PerOpteryx executable process. Check the parameters and the log files!", e);
			return -1;
		}
	}

	private static void watch(final Process process) throws InterruptedException {
		Thread watcherThread = new Thread(new Runnable() {
			@Override
			public void run() {
				BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line = null;
				try {
					while ((line = input.readLine()) != null) {
						LOG.info("PerOpteryx Output: " + line);
					}
				} catch (IOException e) {
					LOG.error("IOException during PerOpteryx run: " + e.getStackTrace());
				}
			}
		});
		synchronized (watcherThread) {
			watcherThread.start();
			watcherThread.wait();
		}
	}

}
