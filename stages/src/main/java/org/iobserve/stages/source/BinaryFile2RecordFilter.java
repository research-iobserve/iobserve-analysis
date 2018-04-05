/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.stages.source;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;

import javax.naming.ConfigurationException;

import kieker.analysisteetime.plugin.reader.filesystem.className.ClassNameRegistryRepository;
import kieker.common.exception.MonitoringRecordException;
import kieker.common.record.IMonitoringRecord;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * @author Christian Wulf
 * @author Reiner Jung
 *
 * @since 0.0.3 was 1.10 of Kieker
 */
public class BinaryFile2RecordFilter extends AbstractConsumerStage<File> {

    private static final String XZ = ".xz";

    private static final int MB = 1024 * 1024;

    private static final String BIN = ".bin";

    private final OutputPort<IMonitoringRecord> outputPort = this.createOutputPort();

    private HackedRecordFromBinaryFileCreator recordFromBinaryFileCreator;

    private ClassNameRegistryRepository classNameRegistryRepository;

    /**
     * @since 1.10
     */
    public BinaryFile2RecordFilter(final ClassNameRegistryRepository classNameRegistryRepository) {
        this();
        this.classNameRegistryRepository = classNameRegistryRepository;
    }

    /**
     * @since 1.10
     */
    public BinaryFile2RecordFilter() {
        super();
    }

    @Override
    public void onStarting() {
        super.onStarting();
        this.recordFromBinaryFileCreator = new HackedRecordFromBinaryFileCreator(this.classNameRegistryRepository);
    }

    public ClassNameRegistryRepository getClassNameRegistryRepository() {
        return this.classNameRegistryRepository;
    }

    public void setClassNameRegistryRepository(final ClassNameRegistryRepository classNameRegistryRepository) {
        this.classNameRegistryRepository = classNameRegistryRepository;
    }

    @Override
    protected void execute(final File binaryFile) throws ConfigurationException {
        final String name = binaryFile.getName();
        final String extension = name.substring(name.lastIndexOf('.'));

        final IDecompressionMethod method;
        if (BinaryFile2RecordFilter.XZ.equals(extension)) {
            method = new XZDecompressionMethod();
        } else if (BinaryFile2RecordFilter.BIN.equals(extension)) {
            method = new NoneDecompressionMethod();
        } else {
            throw new ConfigurationException("unsupported compression.");
        }
        try {
            final DataInputStream inputStream = method.decompressFile(binaryFile, 1 * BinaryFile2RecordFilter.MB);
            try {
                this.recordFromBinaryFileCreator.createRecordsFromBinaryFile(binaryFile, inputStream, this.outputPort);
            } catch (final MonitoringRecordException e) {
                this.logger.error("Error reading file: " + binaryFile, e);
            } finally {
                if (inputStream != null) {
                    this.closeStream(inputStream);
                }
            }
        } catch (final IOException e) {
            this.logger.error("Error reading file: " + binaryFile, e);
        } catch (final IllegalArgumentException e) {
            this.logger.warn("Unknown file extension for file: " + binaryFile);
        }
    }

    private void closeStream(final DataInputStream dataInputStream) {
        try {
            dataInputStream.close();
        } catch (final IOException ex) {
            this.logger.error("Exception while closing input stream for processing input file", ex);
        }
    }

    public OutputPort<IMonitoringRecord> getOutputPort() {
        return this.outputPort;
    }

}
