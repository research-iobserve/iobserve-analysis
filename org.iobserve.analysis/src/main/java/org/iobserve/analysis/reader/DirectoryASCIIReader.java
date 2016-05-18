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
package org.iobserve.analysis.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import kieker.common.exception.MonitoringRecordException;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.util.filesystem.FSUtil;

<<<<<<< HEAD
=======
import org.iobserve.analysis.TimeLogger;

>>>>>>> 8369799fad6bbba0ee0c594bd69ce6afef0b7b41
import teetime.framework.AbstractProducerStage;

// TODO this code should be replaced by the proper Kieker Teetime stages.

/**
 * @author Reiner Jung
 *
 */
public class DirectoryASCIIReader extends AbstractProducerStage<IMonitoringRecord> {
	private static final Log LOG = LogFactory.getLog(DirectoryASCIIReader.class);

	String filePrefix = FSUtil.FILE_PREFIX; // NOPMD NOCS (package visible for inner class)

	private File[] dataFiles;
	private final Map<Integer, Class<? extends IMonitoringRecord>> recordMap = new HashMap<Integer, Class<? extends IMonitoringRecord>>();

	/**
	 * Simplified ASCII file reader.
	 *
	 * @param directory
	 *            is the directory containing kieker record ASCII files.
	 *
	 * @throws IOException
	 *             on all IO errors during file reading
	 * @throws ClassNotFoundException
	 *             on record type which cannot be found by the class loader
	 */
	public DirectoryASCIIReader(final File directory) throws IOException, ClassNotFoundException {
		// check if directory contains data
		if (directory.isDirectory()) {
			this.dataFiles = directory.listFiles(new FileFilter() {

<<<<<<< HEAD
				@Override
=======
>>>>>>> 8369799fad6bbba0ee0c594bd69ce6afef0b7b41
				public boolean accept(final File pathname) {
					final String name = pathname.getName();
					return pathname.isFile()
							&& name.startsWith(DirectoryASCIIReader.this.filePrefix)
							&& name.endsWith(FSUtil.NORMAL_FILE_EXTENSION);
				}
			});

			// read class map file
			final File mappingFile = new File(directory.getAbsolutePath() + File.separator + FSUtil.MAP_FILENAME);

			if (mappingFile.exists()) {
				final BufferedReader mapReader = new BufferedReader(new FileReader(mappingFile));
				final ClassLoader classLoader = this.getClass().getClassLoader();
				String line;
				while ((line = mapReader.readLine()) != null) {
					final String[] parts = line.split("=");
					if (parts[0].startsWith("$")) {
						parts[0] = parts[0].substring(1);
					}
					@SuppressWarnings("unchecked")
					final Class<? extends IMonitoringRecord> clazz = (Class<? extends IMonitoringRecord>) classLoader
							.loadClass(parts[1].trim());
					this.recordMap.put(Integer.decode(parts[0]), clazz);
				}
				mapReader.close();
			}
		}
	}

	@Override
	protected void execute() {
		for (final File dataFile : this.dataFiles) {
			try {
				final BufferedReader dataReader = new BufferedReader(new FileReader(dataFile));
				this.readRecordsFromFile(dataReader);
				dataReader.close();
			} catch (final IOException e) {
				LOG.error("Reading " + dataFile.getName() + " failed " + e.getMessage());
			}
		}
	}

	/**
	 * Read ASCII records from file.
	 *
	 * @param dataReader
	 *            the input buffered reader
	 * @throws IOException
	 *             on io errors
	 */
	private void readRecordsFromFile(final BufferedReader dataReader) throws IOException {
		String line;
		while ((line = dataReader.readLine()) != null) {
			line = line.trim();
			// ignore empty lines
			if (line.length() > 0) {
				final String[] values = line.split(";");
				if (values[0].charAt(0) == '$') { // modern record
					this.processModernRecord(line, values);
				} else { // legacy record
					this.processLegacyRecord(values);
				}
			}
		}
	}

	/**
	 * Process legacy record structure.
	 *
	 * @param values
	 *            values of the record
	 */
	private void processLegacyRecord(final String[] values) {
		final String[] recordFieldsReduced = new String[values.length - 1];
		System.arraycopy(values, 1, recordFieldsReduced, 0, values.length - 1);
		try {
			final IMonitoringRecord record = AbstractMonitoringRecord.createFromStringArray(OperationExecutionRecord.class, recordFieldsReduced);
			this.outputPort.send(record);
		} catch (final MonitoringRecordException e) {
			LOG.error("Class instantiation for record failed: " + e.getMessage());
		}
	}

	private void processModernRecord(final String line, final String[] values) {
		if (values.length > 1) {
			final Integer id = Integer.valueOf(values[0].substring(1));
			final Class<? extends IMonitoringRecord> clazz = this.recordMap.get(id);
			if (clazz != null) {
				final long loggingTimestamp = Long.parseLong(values[1]);
				final int skipValues;
				// check for Kieker < 1.6 OperationExecutionRecords
				if ((values.length == 11) && clazz.equals(OperationExecutionRecord.class)) {
					skipValues = 3;
				} else {
					skipValues = 2;
				}

				try {
					final IMonitoringRecord record = AbstractMonitoringRecord.createFromStringArray(clazz,
							Arrays.copyOfRange(values, skipValues, values.length));
					record.setLoggingTimestamp(loggingTimestamp);
<<<<<<< HEAD
=======
					TimeLogger.getTimeLogger().rememberTime();
>>>>>>> 8369799fad6bbba0ee0c594bd69ce6afef0b7b41
					this.outputPort.send(record);
				} catch (final MonitoringRecordException e) {
					LOG.error("Class instantiation for record failed: " + e.getMessage());
				}
			} else {
				LOG.error("Record type not registered: " + line);
			}
		} else {
			// skip this record
			LOG.error("Illegal record format: " + line);
		}
	}
}
