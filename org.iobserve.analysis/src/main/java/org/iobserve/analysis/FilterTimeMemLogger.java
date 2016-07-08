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
package org.iobserve.analysis;

import giusa.software.framework.table.Table;
import giusa.software.framework.table.TableHeader;
import giusa.software.tools.parsing.csv.CSVParser;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import teetime.framework.AbstractConsumerStage;

/**
 * Logger to log execution time and memory usage of filter.
 * @author Robert Heinrich
 * @author Alessandro Giusa
 */
public final class FilterTimeMemLogger {
	
	/**simple formatter.*/
	private final SimpleDateFormat dateFormatter = 
			new SimpleDateFormat("yyyy-MM-dd");
	
	/**to map the filters coming in asynch.*/
	private HashMap<String, Integer> asynchFilterRowCache = 
			new HashMap<String, Integer>();
	/**map for logger.*/
	private HashMap<String, Table<String>> tablesFilter = 
			new HashMap<String, Table<String>>();
	/***/
	private String logOutput = "logtest.csv";
	
	/**
	 * Create logger.
	 * @param outputPath path to output logging files
	 */
	public FilterTimeMemLogger(final String outputPath) {
		this.logOutput = outputPath;
	}
	
	/**
	 * Init table.
	 * @param table table
	 */
	private void initTable(final Table<String> table) {
		final TableHeader filter = new TableHeader();
		filter.setIndex(0);
		filter.setName("Filter");
		
		final TableHeader beforeTime = new TableHeader();
		beforeTime.setIndex(1);
		beforeTime.setName("BeforeTime");
		
		final TableHeader afterTime = new TableHeader();
		afterTime.setIndex(2);
		afterTime.setName("AfterTime");
		
		final TableHeader beforeMemory = new TableHeader();
		beforeMemory.setIndex(3);
		beforeMemory.setName("BeforeMemory");
		
		final TableHeader afterMemory = new TableHeader();
		afterMemory.setIndex(4);
		afterMemory.setName("AfterMemory");
		
		final TableHeader diffTime = new TableHeader();
		diffTime.setIndex(5);
		diffTime.setName("DiffTime");
		
		final TableHeader diffMemory = new TableHeader();
		diffMemory.setIndex(6);
		diffMemory.setName("DiffMemory");
		
		table.getHeader().add(filter);//0 
		table.getHeader().add(beforeTime);//1
		table.getHeader().add(afterTime);//2
		table.getHeader().add(beforeMemory);//3
		table.getHeader().add(afterMemory);//4
		table.getHeader().add(diffTime);//5
		table.getHeader().add(diffMemory);//6
	}
	
	/**
	 * Set the path for logging files.
	 * @param path path
	 */
	public void setLogOutput(final String path) {
		this.logOutput = path;
	}
	
	/**
	 * Call this method before filter execution. 
	 * Call {@link #after(AbstractConsumerStage, String)} after execution.
	 * @param filter filter
	 * @param format format like filter %s is used 
	 * 		with {@link String#format(String, Object...)}
	 * @param objects objects used in combination with format
	 * 	in {@link String#format(String, Object...)}
	 * 
	 */
	public void before(final AbstractConsumerStage<?> filter,
			final String format, final Object...objects) {
		this.before(filter, String.format(format, objects));
	}
	
	/**
	 * Call this method before filter execution.
	 * @see #before(AbstractConsumerStage, String, Object...). 
	 * @param filter filter
	 * @param idFilterExecution id of filter
	 */
	public void before(final AbstractConsumerStage<?> filter,
			final String idFilterExecution) {
		
		// get current time
		final long time = System.nanoTime();
		
		// get consumed memory before running the filter
		final Runtime runtime = Runtime.getRuntime();
		final long usedMemory = runtime.totalMemory() - runtime.freeMemory();
		
		// get the table
		Table<String> table = this.tablesFilter.get(filter.getId());
		if (table == null) {
			table = new Table<String>();
			this.initTable(table);
			this.tablesFilter.put(filter.getId(), table);
		}
		
		// get the row-size of the table
		int row = table.size();
		
		// create new row with all columns ahead
		table.addColumn(row, 0, "", true);
		table.addColumn(row, 1, "", true);
		table.addColumn(row, 2, "", true);
		table.addColumn(row, 3, "", true);
		table.addColumn(row, 4, "", true);
		table.addColumn(row, 5, "", true);
		table.addColumn(row, 6, "", true);
		
		// set the values for this filter
		table.getColumn(row, 0).setValue(filter.getId());
		table.getColumn(row, 1).setValue(String.valueOf(time));
		table.getColumn(row, 3).setValue(String.valueOf(usedMemory));
		
		// save the row of this specific filter
		this.asynchFilterRowCache.put(idFilterExecution, Integer.valueOf(row));
		
		runtime.gc();
	}
	
	/**
	 * Call this method after filter execution. 
	 * Call {@link #before(AbstractConsumerStage, String, Object...)}
	 * before execution.
	 * @param filter filter
	 * @param format format like {@link String#format(String, Object...)}
	 * @param objects objects used in {@link String#format(String, Object...)}
	 */
	public void after(final AbstractConsumerStage<?> filter,
			final String format, final Object...objects) {
		this.after(filter, String.format(format, objects));
	}
	
	/**
	 * Call this after filter execution.
	 * @see #after(AbstractConsumerStage, String, Object...)
	 * @param filter filter
	 * @param idFilterExecution id of filter
	 */
	public void after(final AbstractConsumerStage<?> filter,
			final String idFilterExecution) {
		final long time = System.nanoTime();
		final Runtime runtime = Runtime.getRuntime();
		final long usedMemory = runtime.totalMemory() - runtime.freeMemory();
		
		// get the table
		Table<String> table = this.tablesFilter.get(filter.getId());
		if (table == null) {
			throw new NullPointerException("No Table available "
					+ "for filter:" + filter.getId());
		}
		
		// get the row for this specific filter
		final int row = this.asynchFilterRowCache.get(idFilterExecution);
		
		table.getColumn(row, 2).setValue(String.valueOf(time));
		table.getColumn(row, 4).setValue(String.valueOf(usedMemory));
		
		// calculate diffs
		final long diffTime = time - Long.parseLong(table
				.getColumn(row, 1).getValue());
		final long diffMemory = usedMemory - Long.parseLong(table
				.getColumn(row, 3).getValue());

		table.getColumn(row, 5).setValue(String.valueOf(diffTime));
		table.getColumn(row, 6).setValue(String.valueOf(diffMemory));
		
		runtime.gc();
		
		//TODO just for debug purposes
		this.close();
	}
	
	/**
	 * Close the logger.
	 */
	public void close() {
		final CSVParser parser = new CSVParser();
		for (final String filter:this.tablesFilter.keySet()) {
			final Table<String> table = this.tablesFilter.get(filter);
			parser.setModel(table);
			final String csvContent = parser.toString();
			try {
				final String fileName = this.logOutput 
						+ "_" + filter
						+ "_" + this.dateFormatter.format(new Date()) + ".csv";
				final PrintWriter writer = new PrintWriter(fileName);
				writer.append(csvContent);
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
