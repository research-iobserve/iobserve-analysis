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

import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.emf.ecore.EObject;

import teetime.framework.AbstractConsumerStage;

/**
 * Writer for PCM models passed between the different iObserve services via TCP.
 *
 * @author Lars Bluemke
 *
 */
public class SingleConnectionTcpWriterStage extends AbstractConsumerStage<EObject> {
    public static final int HEADER_BYTES = 8;

    private static final Logger LOG = LogManager.getLogger(SingleConnectionTcpWriterStage.class);

    private final String hostname;
    private final int inputPort;
    private final ByteBuffer buffer;

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
        this.buffer = ByteBuffer.allocate(SingleConnectionTcpWriterStage.HEADER_BYTES);

    }

    @Override
    protected void execute(final EObject model) throws Exception {
        final SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(this.hostname, this.inputPort));

        // Write header
        // int size = model.;
        // int version;
        // this.buffer.putInt(value);
        // this.buffer.putInt(value);
        // socketChannel.write(buffer);

        // Write body
        final ObjectOutputStream oos = new ObjectOutputStream(socketChannel.socket().getOutputStream());
        oos.writeObject(model);
        oos.close();

        socketChannel.close();

    }

}
