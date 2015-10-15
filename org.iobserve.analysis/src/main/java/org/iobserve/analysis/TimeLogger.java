/***************************************************************************
 * Copyright 2015 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
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
public final class TimeLogger {

	/** bad excuse for a singleton. */
	public static final TimeLogger INSTANCE = new TimeLogger();

	/** output of the time logger. */
	private BufferedWriter writer;

	/** last present time record. */
	private long presentTime;

	/** remember a time measurement. */
	private long pastTime;

	/** i don't know. !! */
	private long rememberedTime;

	/** what? */
	private volatile boolean haveVal;

	/**
	 *
	 */
	private TimeLogger() {}

	/**
	 * Return the TimeLogger singleton.
	 *
	 * @return
	 */
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
			// TODO there is a thing called Logger, use it.
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
			this.rememberedTime = System.nanoTime();
			this.haveVal = true;
		}
	}

	public long getRemberedTime() {
		this.haveVal = false;
		return this.rememberedTime;
	}
}
