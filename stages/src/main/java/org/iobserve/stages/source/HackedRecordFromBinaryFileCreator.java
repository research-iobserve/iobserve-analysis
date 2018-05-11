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
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.analysisteetime.plugin.reader.filesystem.className.ClassNameRegistry;
import kieker.analysisteetime.plugin.reader.filesystem.className.ClassNameRegistryRepository;
import kieker.common.exception.MonitoringRecordException;
import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.factory.CachedRecordFactoryCatalog;
import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.BinaryValueDeserializer;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.IRegistryRecordReceiver;

import teetime.framework.OutputPort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a record from file creator, hacked to replace the broken Kieker version.
 *
 * TODO The file directory reading facilities need some extra work to fix it.
 *
 * @author Reiner Jung
 *
 * @since 0.0.3 replaces Kiekers variant from 1.10
 */
public class HackedRecordFromBinaryFileCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(HackedRecordFromBinaryFileCreator.class);

    private static final int LONG_BYTES = AbstractMonitoringRecord.TYPE_SIZE_LONG;

    private final ClassNameRegistryRepository classNameRegistryRepository;

    private final CachedRecordFactoryCatalog recordFactories = CachedRecordFactoryCatalog.getInstance();

    private final ByteBuffer buffer;

    /**
     * Create a new record from binary file creator.
     *
     * @param classNameRegistryRepository
     *            class and string registry
     */
    public HackedRecordFromBinaryFileCreator(final ClassNameRegistryRepository classNameRegistryRepository) {
        this.classNameRegistryRepository = classNameRegistryRepository;
        this.buffer = ByteBuffer.allocate(1024000);
    }

    /**
     * Create records from binary files.
     *
     * @param binaryFile
     *            binary file input, only used for logging info
     * @param inputStream
     *            data stream
     * @param outputPort
     *            filter output port
     * @throws IOException
     *             on io errors during reading
     * @throws MonitoringRecordException
     *             on deserialization issues
     */
    public void createRecordsFromBinaryFile(final File binaryFile, final DataInputStream inputStream,
            final OutputPort<IMonitoringRecord> outputPort) throws IOException, MonitoringRecordException {

        HackedRecordFromBinaryFileCreator.LOGGER.info("reading file {}", binaryFile.getAbsolutePath());

        final ClassNameRegistry classNameRegistry = this.classNameRegistryRepository.get(binaryFile.getParentFile());

        final IRegistry<String> stringRegistryWrapper = new GlueRegistry(classNameRegistry);
        final BinaryValueDeserializer deserializer = BinaryValueDeserializer.create(this.buffer, stringRegistryWrapper);

        boolean endOfStreamReached = false;
        while (!endOfStreamReached) {
            byte[] bytes = this.buffer.array();
            int bytesRead = inputStream.read(bytes, this.buffer.position(), this.buffer.remaining());
            this.buffer.position(this.buffer.position() + bytesRead);
            while (bytesRead > 0) {
                bytes = this.buffer.array();
                bytesRead = inputStream.read(bytes, this.buffer.position(), this.buffer.remaining());
                if (bytesRead >= 0) {
                    this.buffer.position(this.buffer.position() + bytesRead);
                }
            }
            if (bytesRead == -1) {
                endOfStreamReached = true;
            }

            this.processBuffer(classNameRegistry, deserializer, outputPort);

            if (endOfStreamReached) {
                inputStream.close();
            }
        }

    }

    private void processBuffer(final ClassNameRegistry registry, final IValueDeserializer deserializer,
            final OutputPort<IMonitoringRecord> outputPort) throws IOException {
        this.buffer.flip();

        try {
            /** Needs at least an record id. */
            while (this.buffer.position() + 4 <= this.buffer.limit()) {
                this.buffer.mark();
                final IMonitoringRecord record = this.deserializeRecord(registry, deserializer);
                if (record == null) {
                    return;
                } else {
                    outputPort.send(record);
                }
            }
            this.buffer.mark();
            this.buffer.compact();
        } catch (final BufferUnderflowException ex) {
            HackedRecordFromBinaryFileCreator.LOGGER
                    .warn("Unexpected buffer underflow. Resetting and compacting buffer.", ex);
            this.buffer.reset();
            this.buffer.compact();
            throw ex;
        }
    }

    private IMonitoringRecord deserializeRecord(final ClassNameRegistry registry, final IValueDeserializer deserializer)
            throws IOException {
        final int clazzId = this.buffer.getInt();
        final String recordClassName = registry.get(clazzId);

        if (recordClassName == null) {
            HackedRecordFromBinaryFileCreator.LOGGER.error("Missing classname mapping for record type id '{}'",
                    clazzId);
            return null; // we can't easily recover on errors
        }

        // identify logging timestamp
        if (this.buffer.remaining() < HackedRecordFromBinaryFileCreator.LONG_BYTES) {
            // incomplete record, move back
            this.buffer.reset();
            this.buffer.compact();
            return null;
        } else {
            final long loggingTimestamp = this.buffer.getLong();

            // identify record data
            final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.recordFactories.get(recordClassName);
            if (this.buffer.remaining() < recordFactory.getRecordSizeInBytes()) {
                // incomplete record, move back
                this.buffer.reset();
                this.buffer.compact();
                return null;
            } else {
                try {
                    final IMonitoringRecord record = recordFactory.create(deserializer);
                    record.setLoggingTimestamp(loggingTimestamp);
                    return record;
                } catch (final RecordInstantiationException ex) { // TODO this happens when dynamic
                                                                  // arrays are used and the buffer
                                                                  // does not hold the complete
                                                                  // record.
                    HackedRecordFromBinaryFileCreator.LOGGER.warn("Failed to create: {} error {}", recordClassName, ex);
                    // incomplete record, move back
                    this.buffer.reset();
                    this.buffer.compact();
                    return null;
                } catch (final BufferUnderflowException ex) {
                    HackedRecordFromBinaryFileCreator.LOGGER.warn("Failed to create: {} error {}", recordClassName, ex);
                    // incomplete record, move back
                    this.buffer.reset();
                    this.buffer.compact();
                    return null;

                }
            }
        }
    }

    /**
     * Glues the ClassNameRegistry which is used as a StringRegistry onto the IRegistry interface.
     *
     * @author Reiner Jung
     *
     */
    private class GlueRegistry implements IRegistry<String> {

        private final ClassNameRegistry classNameRegistry;

        public GlueRegistry(final ClassNameRegistry classNameRegistry) {
            this.classNameRegistry = classNameRegistry;
        }

        @Override
        public long getId() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int get(final String value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String get(final int key) {
            return this.classNameRegistry.get(key);
        }

        @Override
        public String[] getAll() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getSize() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setRecordReceiver(final IRegistryRecordReceiver registryRecordReceiver) {
            throw new UnsupportedOperationException();
        }

    }

}
