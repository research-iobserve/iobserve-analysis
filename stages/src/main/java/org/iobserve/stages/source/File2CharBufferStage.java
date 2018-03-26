/**
 * Copyright © 2015 Christian Wulf, Nelson Tavares de Sousa (http://teetime-framework.github.io)
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
 */
package org.iobserve.stages.source;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;

import kieker.analysisteetime.plugin.reader.filesystem.className.ClassNameRegistry;
import kieker.analysisteetime.plugin.reader.filesystem.className.ClassNameRegistryRepository;
import kieker.common.exception.MonitoringRecordException;
import kieker.common.exception.UnknownRecordTypeException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.factory.CachedRecordFactoryCatalog;
import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.TextValueDeserializer;
import teetime.stage.basic.AbstractTransformation;
import teetime.stage.util.MappingException;

public class File2CharBufferStage extends AbstractTransformation<File, IMonitoringRecord> {

    private final String charset;
    private final ClassNameRegistryRepository classNameRegistryRepository;
    private final CachedRecordFactoryCatalog recordFactories = CachedRecordFactoryCatalog.getInstance();

    /**
     *
     * @param charset
     *            to be used when interpreting text files
     *
     * @since 1.1
     */
    public File2CharBufferStage(final ClassNameRegistryRepository classNameRegistryRepository, final String charset) {
        super();
        this.classNameRegistryRepository = classNameRegistryRepository;
        this.charset = charset;
    }

    @Override
    protected void execute(final File textFile) {
        final ClassNameRegistry classNameRegistry = this.classNameRegistryRepository.get(textFile.getParentFile());
        FileChannel inputStream = null;
        try {
            int lineNumber = 1;

            final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            final CharBuffer buffer = CharBuffer.allocate(1024);
            inputStream = new FileInputStream(textFile).getChannel();
            byteBuffer.clear();

            boolean endOfFile = false;
            while (!endOfFile) {
                final int count = inputStream.read(byteBuffer);
                if (count == 0) {
                    endOfFile = true;
                }
                byteBuffer.flip();
                for (int i = 0; i < byteBuffer.position(); i++) {
                    final char ch = byteBuffer.getChar(i);
                    if ((ch != '\n') && (ch != '\r')) {
                        buffer.put(ch);
                    } else {
                        if (byteBuffer.getChar(i + 1) == '\n') {
                            i++;
                        }
                        this.outputPort.send(this.createRecord(buffer, classNameRegistry));
                        buffer.clear();
                        lineNumber++;
                        // remember old position
                    }
                }
            }
        } catch (final FileNotFoundException e) {
            this.logger.error("", e);
        } catch (final IOException e) {
            this.logger.error("", e);
        } catch (final MappingException e) {
            this.logger.error("", e);
        } catch (final MonitoringRecordException e) {
            this.logger.error("", e);
        } catch (final UnknownRecordTypeException e) {
            this.logger.error("", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (final IOException e) {
                this.logger.warn("", e);
            }
        }
    }

    private IMonitoringRecord createRecord(final CharBuffer buffer, final ClassNameRegistry classNameRegistry)
            throws MappingException, MonitoringRecordException, UnknownRecordTypeException {
        final char lead = buffer.get();
        if (lead == '$') {
            final TextValueDeserializer deserializer = TextValueDeserializer.create(buffer);
            final int id = deserializer.getInt();
            final String classname = classNameRegistry.get(id);
            if (classname == null) {
                throw new MappingException("Missing classname mapping for record type id " + "'" + id + "'");
            }
            final Class<? extends IMonitoringRecord> clazz = this.getClassByName(classname);
            final long loggingTimestamp = deserializer.getLong();
            final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.recordFactories.get(classname);

            return recordFactory.create(deserializer);
        } else {
            return null;
        }
    }

    public String getCharset() {
        return this.charset;
    }

    /**
     * @since 1.10
     */
    private Class<? extends IMonitoringRecord> getClassByName(final String classname)
            throws MonitoringRecordException, UnknownRecordTypeException {
        try {
            return AbstractMonitoringRecord.classForName(classname);
        } catch (final MonitoringRecordException ex) {
            throw new UnknownRecordTypeException("Failed to load record type " + classname, classname, ex);
        }
    }

    /**
     * @since 1.10
     */
    private IMonitoringRecord createLegacyRecordFromRecordFiels(final String[] recordFields)
            throws MonitoringRecordException {
        final String[] recordFieldsReduced = new String[recordFields.length - 1];
        System.arraycopy(recordFields, 1, recordFieldsReduced, 0, recordFields.length - 1);
        return AbstractMonitoringRecord.createFromStringArray(OperationExecutionRecord.class, recordFieldsReduced);
    }

}
