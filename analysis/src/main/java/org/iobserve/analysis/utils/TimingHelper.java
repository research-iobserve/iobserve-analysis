package org.iobserve.analysis.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TimingHelper {
	private static int spaces = 25;

	/* Private Instance Variables */
	/**
	 * Stores the start time when an object of the StopWatch class is
	 * initialized.
	 */
	private static long[] times;
	private static String[] names;

	private static int currentIndex;

	/**
	 * Custom constructor which initializes the {@link #startTime} parameter.
	 */
	public TimingHelper() {
	}

	public static void start(final String runName) {
		times = new long[spaces];
		names = new String[spaces];
		currentIndex = 0;
		createRound(runName);
	}

	public static void createRound(final String roundName) {
		times[currentIndex] = System.currentTimeMillis();
		names[currentIndex++] = roundName;
	}

	public static void end(final String roundName) {
		createRound(roundName);

		List<Double> roundTimes = new ArrayList<Double>();
		long roundStart = times[0];

		for (int i = 1; i < currentIndex; i++) {
			long roundEnd = times[i];
			double roundTime = ((double) (roundEnd - roundStart)) / 1000.0;
			roundTimes.add(roundTime);
			
			roundStart = roundEnd;
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < currentIndex; i++) {
			sb.append(names[i] + ";");
		}
		sb.append("\n");
		for (int i = 0; i < roundTimes.size(); i++) {
			sb.append(String.format("%,3f;", roundTimes.get(i)));
		}
		sb.append("\n");

		saveFile("time.csv", sb.toString());
	}

	private static void saveFile(String timeFile, String content) {
		try {
			File datFile = new File(timeFile);

			if (!datFile.exists()) {
				datFile.createNewFile();
			}

			FileWriter fw = new FileWriter(datFile, true);
			fw.write(content);
			fw.flush();
			fw.close();

		} catch (IOException ex) {
			// do stuff with exception
			ex.printStackTrace();
		}
	}

	/**
	 * Gets the elapsed time (in seconds) since the time the object of StopWatch
	 * was initialized.
	 * 
	 * @return Elapsed time in seconds.
	 */
	// public static double getElapsedTime() {
	// long endTime = System.currentTimeMillis();
	// return (double) (endTime - startTime) / (1000);
	// }

}
