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
import java.net.SocketAddress;
import java.nio.BufferUnderflowException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.factory.CachedRecordFactoryCatalog;
import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.flow.ITraceRecord;
import kieker.common.record.flow.trace.ConstructionEvent;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.common.record.flow.trace.operation.CallOperationEvent;
import kieker.common.record.flow.trace.operation.constructor.AfterConstructorEvent;
import kieker.common.record.flow.trace.operation.constructor.AfterConstructorFailedEvent;
import kieker.common.record.flow.trace.operation.constructor.BeforeConstructorEvent;
import kieker.common.record.flow.trace.operation.constructor.CallConstructorEvent;
import kieker.common.record.flow.trace.operation.constructor.object.AfterConstructorFailedObjectEvent;
import kieker.common.record.flow.trace.operation.constructor.object.AfterConstructorObjectEvent;
import kieker.common.record.flow.trace.operation.constructor.object.BeforeConstructorObjectEvent;
import kieker.common.record.flow.trace.operation.constructor.object.BeforeConstructorObjectInterfaceEvent;
import kieker.common.record.flow.trace.operation.constructor.object.CallConstructorObjectEvent;
import kieker.common.record.flow.trace.operation.object.AfterOperationFailedObjectEvent;
import kieker.common.record.flow.trace.operation.object.AfterOperationObjectEvent;
import kieker.common.record.flow.trace.operation.object.BeforeOperationObjectEvent;
import kieker.common.record.flow.trace.operation.object.BeforeOperationObjectInterfaceEvent;
import kieker.common.record.flow.trace.operation.object.CallOperationObjectEvent;
import kieker.common.record.misc.KiekerMetadataRecord;
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

    private volatile long traceId = 0;
    private final Map<String, Map<Long, TraceMetadata>> metadatamap = new HashMap<>();

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

    private void processBuffer(final Connection connection) throws IOException {
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

    private boolean onBufferReceived(final Connection connection) throws IOException {
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
        if (connection.getBuffer().remaining() < MultipleConnectionTcpReaderStage.INT_BYTES
                + MultipleConnectionTcpReaderStage.INT_BYTES) {
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

    private boolean deserializeRecord(final Connection connection, final int clazzId) throws IOException {
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

                    final IMonitoringRecord rewrittenRecord = this.recordRewrite(connection, record);
                    if (rewrittenRecord != null) {
                        this.outputPort.send(rewrittenRecord);
                    }
                    return true;
                } catch (final RecordInstantiationException ex) {
                    super.logger.error("Failed to create: " + recordClassName, ex);
                    return false;
                }
            }
        }
    }

    /**
     * Trace data records use unique ids for their respective host. However, in a multi read stage
     * these ids may be used on different hosts. Therefore, they have to be mapped.
     *
     * @param record
     * @return
     * @throws IOException
     */
    private IMonitoringRecord recordRewrite(final Connection connection, final IMonitoringRecord record)
            throws IOException {
        if (record instanceof TraceMetadata) {
            final TraceMetadata traceMetadata = (TraceMetadata) record;
            final TraceMetadata newMetadata = new TraceMetadata(this.traceId, traceMetadata.getThreadId(),
                    traceMetadata.getSessionId(), this.getIP(connection.getChannel().getRemoteAddress()),
                    traceMetadata.getParentTraceId(), traceMetadata.getParentOrderId());
            Map<Long, TraceMetadata> map = this.metadatamap.get(newMetadata.getHostname());
            if (map == null) {
                map = new HashMap<>();
                this.metadatamap.put(newMetadata.getHostname(), map);
            }
            map.put(traceMetadata.getTraceId(), newMetadata);
            this.traceId++;
            return newMetadata;
        } else if (record instanceof ITraceRecord) {
            final TraceMetadata metaData = this.metadatamap.get(this.getIP(connection.getChannel().getRemoteAddress()))
                    .get(((ITraceRecord) record).getTraceId());

            /** this mess could be avoided with setters in Kieker records. */
            if (record instanceof ConstructionEvent) {
                final ConstructionEvent event = (ConstructionEvent) record;
                return new ConstructionEvent(event.getTimestamp(), metaData.getTraceId(), event.getOrderIndex(),
                        event.getClassSignature(), event.getObjectId());
            } else if (record instanceof AfterOperationEvent) {
                final AfterOperationEvent event = (AfterOperationEvent) record;
                return new AfterOperationEvent(event.getTimestamp(), metaData.getTraceId(), event.getOrderIndex(),
                        event.getOperationSignature(), event.getClassSignature());

            } else if (record instanceof AfterOperationFailedEvent) {
                final AfterOperationFailedEvent event = (AfterOperationFailedEvent) record;
                return new AfterOperationFailedEvent(event.getTimestamp(), metaData.getTraceId(), event.getOrderIndex(),
                        event.getOperationSignature(), event.getClassSignature(), event.getCause());

            } else if (record instanceof BeforeOperationEvent) {
                final BeforeOperationEvent event = (BeforeOperationEvent) record;
                return new BeforeOperationEvent(event.getTimestamp(), metaData.getTraceId(), event.getOrderIndex(),
                        event.getOperationSignature(), event.getClassSignature());

            } else if (record instanceof CallOperationEvent) {
                final CallOperationEvent event = (CallOperationEvent) record;
                return new CallOperationEvent(event.getTimestamp(), metaData.getTraceId(), event.getOrderIndex(),
                        event.getOperationSignature(), event.getClassSignature(), event.getCalleeOperationSignature(),
                        event.getCalleeClassSignature());

            } else if (record instanceof AfterOperationFailedObjectEvent) {
                final AfterOperationFailedObjectEvent event = (AfterOperationFailedObjectEvent) record;
                return new AfterOperationFailedObjectEvent(event.getTimestamp(), metaData.getTraceId(),
                        event.getOrderIndex(), event.getOperationSignature(), event.getClassSignature(),
                        event.getCause(), event.getObjectId());

            } else if (record instanceof AfterOperationObjectEvent) {
                final AfterOperationObjectEvent event = (AfterOperationObjectEvent) record;
                return new AfterOperationObjectEvent(event.getTimestamp(), metaData.getTraceId(), event.getOrderIndex(),
                        event.getOperationSignature(), event.getClassSignature(), event.getObjectId());

            } else if (record instanceof BeforeOperationObjectEvent) {
                final BeforeOperationObjectEvent event = (BeforeOperationObjectEvent) record;
                return new BeforeOperationObjectEvent(event.getTimestamp(), metaData.getTraceId(),
                        event.getOrderIndex(), event.getOperationSignature(), event.getClassSignature(),
                        event.getObjectId());

            } else if (record instanceof BeforeOperationObjectInterfaceEvent) {
                final BeforeOperationObjectInterfaceEvent event = (BeforeOperationObjectInterfaceEvent) record;
                return new BeforeOperationObjectInterfaceEvent(event.getTimestamp(), metaData.getTraceId(),
                        event.getOrderIndex(), event.getOperationSignature(), event.getClassSignature(),
                        event.getObjectId(), event.getInterface());

            } else if (record instanceof CallOperationObjectEvent) {
                final CallOperationObjectEvent event = (CallOperationObjectEvent) record;
                return new CallOperationObjectEvent(event.getTimestamp(), metaData.getTraceId(), event.getOrderIndex(),
                        event.getOperationSignature(), event.getClassSignature(), event.getCalleeOperationSignature(),
                        event.getCalleeClassSignature(), event.getCallerObjectId(), event.getCalleeObjectId());

            } else if (record instanceof AfterConstructorEvent) {
                final AfterConstructorEvent event = (AfterConstructorEvent) record;
                return new AfterConstructorEvent(event.getTimestamp(), metaData.getTraceId(), event.getOrderIndex(),
                        event.getOperationSignature(), event.getClassSignature());

            } else if (record instanceof AfterConstructorFailedEvent) {
                final AfterConstructorFailedEvent event = (AfterConstructorFailedEvent) record;
                return new AfterConstructorFailedEvent(event.getTimestamp(), metaData.getTraceId(),
                        event.getOrderIndex(), event.getOperationSignature(), event.getClassSignature(),
                        event.getCause());

            } else if (record instanceof BeforeConstructorEvent) {
                final BeforeConstructorEvent event = (BeforeConstructorEvent) record;
                return new BeforeConstructorEvent(event.getTimestamp(), metaData.getTraceId(), event.getOrderIndex(),
                        event.getOperationSignature(), event.getClassSignature());

            } else if (record instanceof CallConstructorEvent) {
                final CallConstructorEvent event = (CallConstructorEvent) record;
                return new CallConstructorEvent(event.getTimestamp(), metaData.getTraceId(), event.getOrderIndex(),
                        event.getCallerOperationSignature(), event.getCallerClassSignature(),
                        event.getCalleeOperationSignature(), event.getCalleeOperationSignature());

            } else if (record instanceof AfterConstructorFailedObjectEvent) {
                final AfterConstructorFailedObjectEvent event = (AfterConstructorFailedObjectEvent) record;
                return new AfterConstructorFailedObjectEvent(event.getTimestamp(), metaData.getTraceId(),
                        event.getOrderIndex(), event.getOperationSignature(), event.getClassSignature(),
                        event.getCause(), event.getObjectId());

            } else if (record instanceof AfterConstructorObjectEvent) {
                final AfterConstructorObjectEvent event = (AfterConstructorObjectEvent) record;
                return new AfterConstructorObjectEvent(event.getTimestamp(), metaData.getTraceId(),
                        event.getOrderIndex(), event.getOperationSignature(), event.getClassSignature(),
                        event.getObjectId());

            } else if (record instanceof BeforeConstructorObjectEvent) {
                final BeforeConstructorObjectEvent event = (BeforeConstructorObjectEvent) record;
                return new BeforeConstructorObjectEvent(event.getTimestamp(), metaData.getTraceId(),
                        event.getOrderIndex(), event.getOperationSignature(), event.getClassSignature(),
                        event.getObjectId());

            } else if (record instanceof BeforeConstructorObjectInterfaceEvent) {
                final BeforeConstructorObjectInterfaceEvent event = (BeforeConstructorObjectInterfaceEvent) record;
                return new BeforeConstructorObjectInterfaceEvent(event.getTimestamp(), metaData.getTraceId(),
                        event.getOrderIndex(), event.getOperationSignature(), event.getClassSignature(),
                        event.getObjectId(), event.getInterface());

            } else if (record instanceof CallConstructorObjectEvent) {
                final CallConstructorObjectEvent event = (CallConstructorObjectEvent) record;
                return new CallConstructorObjectEvent(event.getTimestamp(), metaData.getTraceId(),
                        event.getOrderIndex(), event.getCallerOperationSignature(), event.getCallerClassSignature(),
                        event.getCalleeOperationSignature(), event.getCalleeClassSignature(), event.getObjectId(),
                        event.getCalleeObjectId());

            } else {
                return record;
            }
        } else if (record instanceof KiekerMetadataRecord) {
            return null;
        } else {
            return record;
        }
    }

    private String getIP(final SocketAddress remoteAddress) {
        final InetSocketAddress sockaddr = (InetSocketAddress) remoteAddress;

        return sockaddr.getHostString();
    }

}
