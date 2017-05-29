package org.iobserve.planning.peropteryx;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.emf.common.util.URI;

/**
 * Wrapper for executing headless PerOpteryx.
 *
 * @author Tobias Pöppke
 *
 */
public class ExecutionWrapper extends AbstractExecutionWrapper {

	private String execEnvironment;
	private String execEnvironmentParam;
	private String execCommand;
	// private String modelName;

	public ExecutionWrapper(URI inputModelDir, URI perOpteryxDir) throws IOException {
		super(inputModelDir, perOpteryxDir);

		// TODO this does not recognize perOpteryxDir for the execution
		boolean isWindows = System.getProperty("os.name").startsWith("Windows");
		if (isWindows) {
			this.execEnvironment = "cmd.exe";
			this.execEnvironmentParam = "/C";
			this.execCommand = "java  -jar .\\plugins\\org.eclipse.equinox.launcher_1.3.201.v20161025-1711.jar";
		} else {
			this.execEnvironment = "/bin/bash";
			this.execEnvironmentParam = "-c";
			this.execCommand = perOpteryxDir.toFileString() + "/peropteryx-headless";
		}

		// this.modelName = this.extractModelName(inputModelDir);

	}

	@Override
	public void watch(final Process process) throws InterruptedException {
		Thread watcherThread = new Thread(new Runnable() {
			@Override
			public void run() {
				BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line = null;
				try {
					while ((line = input.readLine()) != null) {
						System.err.println("PerOpteryx Output: " + line);
						// LOG.info("PerOpteryx Output: " + line);
					}
				} catch (IOException e) {
					System.err.println("Watcher Thread terminated");
					// LOG.error("IOException during PerOpteryx run: " +
					// e.getStackTrace());
				}
			}
		});
		synchronized (watcherThread) {
			System.out.println("Starting Watcher Thread!");
			watcherThread.start();
			watcherThread.wait();
			System.out.println("Watcher Thread terminated");
		}
	}

	@Override
	public ProcessBuilder createProcess() {
		LOG.info("Starting optimization process...");
		// TODO exchange this with values from parameters
		// String perOpteryxCommand =
		// this.getPerOpteryxDir().appendSegment("peropteryx-headless").toFileString();
		String modelDir = this.getInputModelDir().toFileString();

		// String model = "-m cocome-cloud";

		// ProcessBuilder builder = new ProcessBuilder(this.execEnvironment,
		// "-c", perOpteryxCommand + " -w " + modelDir);
		ProcessBuilder builder = new ProcessBuilder(this.execEnvironment, this.execEnvironmentParam,
				this.execCommand + " -w " + modelDir);

		// LOG.info(String.format("PerOpteryx start parameters: {Command:
		// '/bin/bash -c %s', working-dir: '%s'}", perOpteryxCommand,
		// modelDir));

		String perOpteryxDir = this.getPerOpteryxDir().toFileString();
		builder.directory(new File(perOpteryxDir));
		builder.redirectOutput();
		builder.redirectErrorStream(true);

		return builder;
	}

	// TODO with the new version of the headless peropteryx this is no longer
	// necessary

	// private String extractModelName(URI modelDir) throws IOException {
	// String[] files = new File(modelDir.toFileString()).list();
	// String modelName = null;
	//
	// HashSet<String> fileNames = new HashSet<String>();
	//
	// for (String file : files) {
	// if (!(new File(file).isDirectory())) {
	// String[] fileNameParts = file.split("\\.");
	// String fileName = fileNameParts.length > 0 ? fileNameParts[0] : "";
	//
	// boolean added = fileNames.add(fileName);
	//
	// if (!added) {
	// modelName = fileName;
	// break;
	// }
	// }
	// }
	//
	// if (modelName == null) {
	// throw new IOException("Couldn't determine a model name. No filename was
	// found twice ...");
	// }
	//
	// return modelName;
	// }

}
