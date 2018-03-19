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
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import teetime.framework.AbstractProducerStage;

/**
 * Reader for PCM model files passed between the different iObserve services via TCP.
 *
 * @author Lars Bluemke
 *
 */
public class SingleConnectionTcpReaderStage extends AbstractProducerStage<File> {
    public static final String ENCODING = "UTF-8";

    private static final Logger LOGGER = LoggerFactory.getLogger(SingleConnectionTcpReaderStage.class);
    private static final int BUFFER_SIZE = 1024;

    private final int inputPort;
    private final File modelDirectory;

    /**
     * Creates a new instance of this class.
     *
     * @param inputPort
     *            The port number
     * @param modelDirectory
     *            The directory where the incoming files are stored
     */
    public SingleConnectionTcpReaderStage(final int inputPort, final File modelDirectory) {
        this.inputPort = inputPort;
        this.modelDirectory = modelDirectory;
    }

    @Override
    protected void execute() throws Exception {
        // Set up a server socket to receive data
        final ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(this.inputPort));

        while (this.isActive()) {
            SingleConnectionTcpReaderStage.LOGGER.debug("Listening at port " + this.inputPort);

            final SocketChannel socketChannel = serverSocket.accept();
            final ByteBuffer buffer = ByteBuffer.allocate(SingleConnectionTcpReaderStage.BUFFER_SIZE);

            SingleConnectionTcpReaderStage.LOGGER.debug("Connection from " + socketChannel.getRemoteAddress().toString());

            // Read file name length
            socketChannel.read(buffer);

            buffer.flip();

            final int nameLength = buffer.getInt();
            final byte[] nameBytes = new byte[nameLength];

            // Read file name
            for (int i = 0; i < nameLength; i++) {
                // All data read from buffer (if nameLength > FILE_BUFFER_SIZE-4)
                if (!buffer.hasRemaining()) {
                    buffer.clear();
                    socketChannel.read(buffer);
                    buffer.flip();
                }

                nameBytes[i] = buffer.get();
            }

            final String filename = new String(nameBytes, SingleConnectionTcpReaderStage.ENCODING);
            SingleConnectionTcpReaderStage.LOGGER.debug("Received filename=" + filename);

            buffer.compact();

            // Create file channel for model file
            final File modelFile = new File(this.modelDirectory, filename);
            final RandomAccessFile modelAccessFile = new RandomAccessFile(modelFile, "rw");
            final FileChannel fileChannel = modelAccessFile.getChannel();

            // Write bytes from socket channel to file channel
            while (socketChannel.read(buffer) > 0) {
                buffer.flip();
                fileChannel.write(buffer);
                buffer.compact();
            }

            SingleConnectionTcpReaderStage.LOGGER.debug("File received, closing channel.");
            modelAccessFile.close();
            socketChannel.close();

            this.getOutputPort().send(modelFile);
        }
    }
}
