/**
 *
 */
package org.iobserve.analysis;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

// TODO replace with kieker instrumentation if possible.

/**
 * Minimalistic rubbish logger for ASE paper.
 *
 * @author Reiner Jung
 *
 */
public class TimeLogger {

	public static TimeLogger INSTANCE = new TimeLogger();

	private BufferedWriter writer;

	private long presentTime;

	private long pastTime;

	private long rememberTime;

	private volatile boolean haveVal = false;

	/**
	 *
	 */
	private TimeLogger() {}

	public static TimeLogger getTimeLogger() {
		return INSTANCE;
	}

	public void open(final String filename) throws UnsupportedEncodingException, FileNotFoundException {
		this.writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"));
		this.presentTime = System.nanoTime();
	}

	public void write(final Object value) {
		try {
			if (value instanceof String) {
				this.writer.write("\"" + value + "\";");
			} else {
				this.writer.write(value + ";");
			}
		} catch (final IOException e) {
			System.out.println("record failure.");
		}
	}

	public void newline() {
		try {
			this.writer.write("\n");
		} catch (final IOException e) {
			System.out.println("record failure.");
		}
	}

	public void setPastTime(final long pastTime) {
		this.pastTime = pastTime;
	}

	public void close() throws IOException {
		this.writer.close();
	}

	public long getTime() {
		return (System.nanoTime() - this.presentTime) + this.pastTime;
	}

	public long getPresentTime() {
		return System.nanoTime();
	}

	public void rememberTime() {
		if (!this.haveVal) {
			this.rememberTime = System.nanoTime();
			this.haveVal = true;
		}
	}

	public long getRemberedTime() {
		this.haveVal = false;
		return this.rememberTime;
	}
}
