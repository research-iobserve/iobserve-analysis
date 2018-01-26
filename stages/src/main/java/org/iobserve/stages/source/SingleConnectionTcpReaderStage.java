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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.emf.ecore.EObject;

import teetime.framework.AbstractProducerStage;

/**
 * Reader for PCM models passed between the different iObserve services via TCP.
 *
 * @author Lars Bluemke
 *
 */
public class SingleConnectionTcpReaderStage extends AbstractProducerStage<EObject> {
    private static final Logger LOG = LogManager.getLogger(SingleConnectionTcpReaderStage.class);

    private final int inputPort;
    private final ByteBuffer buffer;

    /**
     * Creates a new instance of this class.
     *
     * @param inputPort
     *            The port number
     * @param bufferSize
     *            The size of the buffer used for receiving the models
     */
    public SingleConnectionTcpReaderStage(final int inputPort, final int bufferSize) {
        this.inputPort = inputPort;
        this.buffer = ByteBuffer.allocate(SingleConnectionTcpWriterStage.HEADER_BYTES + bufferSize);

    }

    @Override
    protected void execute() throws Exception {
        while (this.isActive()) {
            final SocketChannel socketChannel = this.createServerSocketChannel(this.inputPort);

            if (socketChannel != null) {
                SingleConnectionTcpReaderStage.LOG
                        .debug("Connection from " + socketChannel.getRemoteAddress().toString());
                socketChannel.configureBlocking(false);

                // Read header
                // while (socketChannel.read(this.buffer) <=
                // SingleConnectionTcpWriterStage.HEADER_BYTES) {
                // this.buffer.flip();
                // final int size = this.buffer.getInt();
                // final int version = this.buffer.getInt();
                // }

                // Read body
                final ObjectInputStream ois = new ObjectInputStream(socketChannel.socket().getInputStream());

                final Object object = ois.readObject();

                if (object instanceof EObject) {
                    this.getOutputPort().send((EObject) object);
                }
            }
            socketChannel.close();
        }
    }

    private SocketChannel createServerSocketChannel(final int port) throws IOException {
        ServerSocketChannel serverSocket = null;
        SocketChannel socketChannel = null;

        serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(port));
        socketChannel = serverSocket.accept();

        return socketChannel;
    }
}
