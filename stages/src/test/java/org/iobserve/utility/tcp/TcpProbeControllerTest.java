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
package org.iobserve.utility.tcp;

import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IRecordReceivedListener;
import kieker.common.record.remotecontrol.ActivationEvent;
import kieker.common.record.remotecontrol.IRemoteControlEvent;
import kieker.common.record.tcp.SingleSocketRecordReader;

/**
 * @author Marc Adolf
 *
 */
public class TcpProbeControllerTest {
    private static TcpProbeController tcpProbeController;
    private static SingleSocketRecordReader tcpReader;
    private static final int BUFFER_SIZE = 65535;
    private static final Log LOG = LogFactory.getLog(TcpProbeControllerTest.class);
    private static TestListener listener;

    private static int port = 9753;
    private static String pattern = "test.pattern";

    /**
     * .
     */
    public TcpProbeControllerTest() {
        super();
    }

    /**
     * Initiates all necessary things like the ProbeController and the TestListener for Receiving
     * other things.
     */
    @BeforeClass
    public static void init() {
        TcpProbeControllerTest.tcpProbeController = new TcpProbeController();
        TcpProbeControllerTest.listener = new TestListener();
        TcpProbeControllerTest.tcpReader = new SingleSocketRecordReader(TcpProbeControllerTest.port,
                TcpProbeControllerTest.BUFFER_SIZE, TcpProbeControllerTest.LOG, TcpProbeControllerTest.listener);
        new Thread(TcpProbeControllerTest.tcpReader).start();
    }

    /**
     * An exception should be thrown if we want to connect to an unknown host.
     *
     * @throws RemoteControlFailedException
     *             the expected exception.
     */
    @Test(expected = RemoteControlFailedException.class)
    public void testUnknownHostFailure() throws RemoteControlFailedException {
        TcpProbeControllerTest.tcpProbeController.activateMonitoredPattern("90.090.90.90", TcpProbeControllerTest.port,
                "test.host", TcpProbeControllerTest.pattern);
    }

    /**
     * Send (de-)activation commands and check if the listener could process them.
     *
     * @throws RemoteControlFailedException
     *             if the test fails due to bad connections.
     */
    @Test(timeout = 30000)
    public void testDeAndActivatePattern() throws RemoteControlFailedException {
        final String ip = "127.0.0.1";
        final String hostname = "test.host";

        final Map<String, Boolean> state = TcpProbeControllerTest.listener.getState();
        Assert.assertFalse(TcpProbeControllerTest.tcpProbeController.isKnownHost(ip, TcpProbeControllerTest.port));
        Assert.assertFalse(state.containsKey(TcpProbeControllerTest.pattern));

        TcpProbeControllerTest.tcpProbeController.activateMonitoredPattern(ip, TcpProbeControllerTest.port, hostname,
                TcpProbeControllerTest.pattern);

        Assert.assertTrue(TcpProbeControllerTest.tcpProbeController.isKnownHost(ip, TcpProbeControllerTest.port));
        // wait for the other thread
        while (!state.containsKey(TcpProbeControllerTest.pattern)) {
            Thread.yield();
        }
        Assert.assertTrue(state.get(TcpProbeControllerTest.pattern));
        TcpProbeControllerTest.tcpProbeController.deactivateMonitoredPattern(ip, TcpProbeControllerTest.port, hostname,
                TcpProbeControllerTest.pattern);
        while (state.get(TcpProbeControllerTest.pattern)) {
            Thread.yield();
        }
        Assert.assertFalse(state.get(TcpProbeControllerTest.pattern));

    }

    /**
     * Terminate the thread that runs the TCP Reader.
     */
    @AfterClass
    public static void terminate() {
        TcpProbeControllerTest.tcpReader.terminate();
    }

    /**
     * Saves the received pattern and if they are active (true).
     *
     * @author Marc Adolf
     *
     */
    static class TestListener implements IRecordReceivedListener {
        private final Map<String, Boolean> state = new HashMap<>();

        public Map<String, Boolean> getState() {
            return this.state;
        }

        @Override
        public void onRecordReceived(final IMonitoringRecord arg0) {
            this.state.put(((IRemoteControlEvent) arg0).getPattern(), arg0 instanceof ActivationEvent);
        }
    }
}
