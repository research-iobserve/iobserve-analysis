/***************************************************************************
 * Copyright 2014 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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
package org.iobserve.analysis.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class just helps to output data to the console.
 * 
 * @author ITI, VeriAlg Group
 * @author IPD, SDQ Group
 * 
 * @author Alessandro Giusa alessandrogiusa@gmail.com
 * @author Robert Heinrich
 * @version 4
 */
public final class Terminal {

	/**
	 * BufferedReader for reading from standard input line-by-line.
	 */
	private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	/**
	 * Private constructor to avoid object generation.
	 */
	private Terminal() { 
		// nothing to do here
	}

	/**
	 * Print a String to the standard output.
	 * 
	 * The String out must not be null.
	 * 
	 * @param out
	 *            The string to be printed.
	 */
	public static void println(final String out) {
		System.out.println(out);
	}

	/**
	 * Print without break line
	 * @param out text to print
	 */
	public static void print(final String out) {
		System.out.print(out);
	}

	/**
	 * Print with formatting option like {@link String#format(String, Object...)} and 
	 * break line.
	 * @param out text to output
	 * @param objects insert in text
	 */
	public static void printlnf(final String out, final Object...objects) {
		System.out.println(String.format(out, objects));
	}

	/**
	 * Same as {@link #printlnf(String, Object...)} but without break line
	 * @param out text to output
	 * @param objects insert in text
	 */
	public static void printf(final String out, final Object...objects) {
		System.out.print(String.format(out, objects));
	}

	/**
	 * Reads a line from standard input.
	 * 
	 * Returns null at the end of the standard input.
	 * 
	 * Use Ctrl+D to indicate the end of the standard input.
	 * 
	 * @return The next line from the standard input or null.
	 */
	public static String readLine() {
		try {
			return Terminal.in.readLine();
		} catch (IOException e) {
			/*
			 * re-throw unchecked (!) exception to prevent students from being forced to use Exceptions before they have
			 * been introduced in the lecture.
			 */
			throw new RuntimeException(e);
		}
	}

	public static String readLine(final String prompt) {
		try {
			Terminal.print(prompt);
			return Terminal.in.readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Print the given text in a adjusted box
	 * @param content content to wrap into box
	 */
	public static void printInBox(final String content) {
		final String[] lines = content.split(System.lineSeparator());
		int maxCol = 0;
		for (int i = 0; i < lines.length; i++) {
			maxCol = Math.max(maxCol, lines[i].length());
		}
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < maxCol; i++) {
			builder.append("=");
		}
		final String boxLine = builder.toString();
		builder.append(System.lineSeparator());
		builder.append(content);
		builder.append(System.lineSeparator());
		builder.append(boxLine);
		Terminal.println(builder.toString());
	}
}
