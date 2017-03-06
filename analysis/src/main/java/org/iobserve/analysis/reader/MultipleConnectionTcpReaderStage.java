/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.BufferUnderflowException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.factory.CachedRecordFactoryCatalog;
import kieker.common.record.factory.IRecordFactory;

import teetime.framework.AbstractProducerStage;

/**
 * @author Reiner Jung
 *
 */
public class MultipleConnectionTcpReaderStage extends AbstractProducerStage<IMonitoringRecord> {

    private static final int INT_BYTES = AbstractMonitoringRecord.TYPE_SIZE_INT;
    private static final int LONG_BYTES = AbstractMonitoringRecord.TYPE_SIZE_LONG;
    private static final Charset ENCODING = StandardCharsets.UTF_8;

    private final CachedRecordFactoryCatalog recordFactories = CachedRecordFactoryCatalog.getInstance();

    /** server input port. */
    private final int inputPort;
    private final int bufferSize;

    /**
     * Create a single threaded multi connection tcp reader stage.
     *
     * @param inputPort
     *            used to accept <code>IMonitoringRecord</code>s and string registry entries.
     * @param bufferSize
     *            capacity of the receiving buffer
     */
    public MultipleConnectionTcpReaderStage(final int inputPort, final int bufferSize) {
        this.inputPort = inputPort;
        this.bufferSize = bufferSize;
    }

    @Override
    protected void execute() {
        try {
            final ServerSocketChannel serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress(this.inputPort));
            serverSocket.configureBlocking(false);
            final Selector readSelector = Selector.open();

            while (this.isActive()) {
                final SocketChannel socketChannel = serverSocket.accept();
                if (socketChannel != null) {
                    System.out.println("Connection from " + socketChannel.getRemoteAddress().toString());
                    // add socketChannel to list of channels
                    socketChannel.configureBlocking(false);
                    final SelectionKey key = socketChannel.register(readSelector, SelectionKey.OP_READ);
                    final Connection connection = new Connection(socketChannel, this.bufferSize);
                    key.attach(connection);
                }

                final int readReady = readSelector.selectNow();

                if (readReady > 0) {
                    final Set<SelectionKey> selectedKeys = readSelector.selectedKeys();
                    final Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                    while (keyIterator.hasNext()) {
                        final SelectionKey key = keyIterator.next();

                        this.readFromSocket(key);

                        keyIterator.remove();
                    }

                    selectedKeys.clear();
                }
            }
        } catch (final ClosedByInterruptException e) {
            this.logger.info("External shutdown called");
        } catch (final IOException e) {
            this.logger.error("Cannot establish listening port", e);
        } finally {
            this.terminateStage();
        }
    }

    private void readFromSocket(final SelectionKey key) throws IOException {
        boolean endOfStreamReached = false;
        final Connection connection = (Connection) key.attachment();
        final SocketChannel socketChannel = connection.getChannel();

        int bytesRead = socketChannel.read(connection.getBuffer());

        while (bytesRead > 0) {
            bytesRead = socketChannel.read(connection.getBuffer());
        }
        if (bytesRead == -1) {
            endOfStreamReached = true;
        }

        this.processBuffer(connection);

        if (endOfStreamReached) {
            System.out.println("Socket closed: " + socketChannel.getRemoteAddress().toString());
            key.attach(null);
            key.cancel();
            key.channel().close();
        }
    }

    private void processBuffer(final Connection connection) {
        connection.getBuffer().flip();

        try {
            while (connection.getBuffer().hasRemaining()) {
                connection.getBuffer().mark();
                if (!this.onBufferReceived(connection)) {
                    connection.getBuffer().reset();
                    connection.getBuffer().compact();
                    return;
                }
            }
            connection.getBuffer().clear();
        } catch (final BufferUnderflowException ex) {
            this.logger.warn("Unexpected buffer underflow. Resetting and compacting buffer.", ex);
            connection.getBuffer().reset();
            connection.getBuffer().compact();
        }
    }

    private boolean onBufferReceived(final Connection connection) {
        // identify record class
        if (connection.getBuffer().remaining() < MultipleConnectionTcpReaderStage.INT_BYTES) {
            return false;
        }
        final int clazzId = connection.getBuffer().getInt();

        if (clazzId == -1) {
            return this.registerRegistryEntry(connection, clazzId);
        } else {
            return this.deserializeRecord(connection, clazzId);
        }
    }

    private boolean registerRegistryEntry(final Connection connection, final int clazzId) {
        // identify string identifier and string length
        if (connection.getBuffer().remaining() < (MultipleConnectionTcpReaderStage.INT_BYTES
                + MultipleConnectionTcpReaderStage.INT_BYTES)) {
            return false;
        } else {
            final int id = connection.getBuffer().getInt();
            final int stringLength = connection.getBuffer().getInt();

            if (connection.getBuffer().remaining() < stringLength) {
                return false;
            } else {
                final byte[] strBytes = new byte[stringLength];
                connection.getBuffer().get(strBytes);
                final String string = new String(strBytes, MultipleConnectionTcpReaderStage.ENCODING);

                connection.getRegistry().register(id, string);
                return true;
            }
        }
    }

    private boolean deserializeRecord(final Connection connection, final int clazzId) {
        final String recordClassName = connection.getRegistry().get(clazzId);

        // identify logging timestamp
        if (connection.getBuffer().remaining() < MultipleConnectionTcpReaderStage.LONG_BYTES) {
            return false;
        } else {
            final long loggingTimestamp = connection.getBuffer().getLong();

            // identify record data
            final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.recordFactories.get(recordClassName);
            if (connection.getBuffer().remaining() < recordFactory.getRecordSizeInBytes()) {
                return false;
            } else {
                try {
                    final IMonitoringRecord record = recordFactory.create(connection.getBuffer(),
                            connection.getStringRegistryWrapper());
                    record.setLoggingTimestamp(loggingTimestamp);

                    this.outputPort.send(record);
                    return true;
                } catch (final RecordInstantiationException ex) {
                    super.logger.error("Failed to create: " + recordClassName, ex);
                    return false;
                }
            }
        }
    }

}
