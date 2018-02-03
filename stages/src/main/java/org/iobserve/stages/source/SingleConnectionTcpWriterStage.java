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

import java.io.File;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import teetime.framework.AbstractConsumerStage;

/**
 * Writer for PCM model files passed between the different iObserve services via TCP.
 *
 * @author Lars Bluemke
 *
 */
public class SingleConnectionTcpWriterStage extends AbstractConsumerStage<File> {
    private static final Logger LOG = LoggerFactory.getLogger(SingleConnectionTcpWriterStage.class);
    private static final int FILE_BUFFER_SIZE = 1024;

    private final String hostname;
    private final int inputPort;

    /**
     * Creates a new instance of this class.
     *
     * @param hostname
     *            The reader's host name
     * @param inputPort
     *            The reader's port number
     *
     */
    public SingleConnectionTcpWriterStage(final String hostname, final int inputPort) {
        this.hostname = hostname;
        this.inputPort = inputPort;
    }

    @Override
    protected void execute(final File modelFile) throws Exception {
        // Connect to reader
        final SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(this.hostname, this.inputPort));

        // Send file name length + file name
        final String filename = modelFile.getName();
        final byte[] nameBytes = filename.getBytes(SingleConnectionTcpReaderStage.ENCODING);
        final int nameLength = nameBytes.length;
        final ByteBuffer nameBuffer = ByteBuffer.allocate(Integer.BYTES + nameLength);
        nameBuffer.putInt(nameLength);
        nameBuffer.put(nameBytes);
        nameBuffer.flip();
        socketChannel.write(nameBuffer);

        // Create file channel for model file
        final RandomAccessFile modelAccessFile = new RandomAccessFile(modelFile, "r");
        final FileChannel fileChannel = modelAccessFile.getChannel();
        final ByteBuffer fileBuffer = ByteBuffer.allocate(SingleConnectionTcpWriterStage.FILE_BUFFER_SIZE);

        // Write bytes from file channel to socket channel
        while (fileChannel.read(fileBuffer) > 0) {
            fileBuffer.flip();
            socketChannel.write(fileBuffer);
            fileBuffer.compact();
        }

        SingleConnectionTcpWriterStage.LOG.debug("File sent, closing channel.");

        socketChannel.close();
        modelAccessFile.close();
    }

}
