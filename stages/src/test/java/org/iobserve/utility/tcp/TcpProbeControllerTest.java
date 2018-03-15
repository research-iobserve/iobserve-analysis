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

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IRecordReceivedListener;
import kieker.common.record.remotecontrol.ActivationEvent;
import kieker.common.record.remotecontrol.IRemoteControlEvent;
import kieker.common.record.tcp.SingleSocketRecordReader;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Marc Adolf
 *
 */
public class TcpProbeControllerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TcpProbeControllerTest.class); // NOPMD
                                                                                       // controller

    private static final int BUFFER_SIZE = 65535;
    private static final String ARBITRARY_IP = "90.90.90.90"; // NOPMD do not code IP addresses
    private static final String LOCALHOST_IP = "127.0.0.1"; // NOPMD do not code IP addresses
    private static final String TEST_HOST = "test.host";

    private static final int PORT = 9753;
    private static final String PATTERN = "test.pattern";

    private static TcpProbeController tcpProbeController;
    private static SingleSocketRecordReader tcpReader;

    private static TestListener listener;

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
        TcpProbeControllerTest.tcpReader = new SingleSocketRecordReader(TcpProbeControllerTest.PORT,
                TcpProbeControllerTest.BUFFER_SIZE, TcpProbeControllerTest.LOGGER, TcpProbeControllerTest.listener);
        new Thread(TcpProbeControllerTest.tcpReader).start();
        try { // TODO we must wait until the reader is up. This is not a unit test. Either rewriter
              // as unit test using powermock or transform into integration test
            Thread.sleep(10000);
        } catch (final InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * An exception should be thrown if we want to connect to an unknown host.
     *
     * @throws RemoteControlFailedException
     *             the expected exception.
     */
    @Test(expected = RemoteControlFailedException.class)
    public void testUnknownHostFailure() throws RemoteControlFailedException {
        TcpProbeControllerTest.tcpProbeController.activateMonitoredPattern(TcpProbeControllerTest.ARBITRARY_IP,
                TcpProbeControllerTest.PORT, TcpProbeControllerTest.TEST_HOST, TcpProbeControllerTest.PATTERN);
    }

    /**
     * Send (de-)activation commands and check if the listener could process them.
     *
     * @throws RemoteControlFailedException
     *             if the test fails due to bad connections.
     */
    @Test(timeout = 30000)
    public void testDeAndActivatePattern() throws RemoteControlFailedException {
        final Map<String, Boolean> state = TcpProbeControllerTest.listener.getState();
        Assert.assertFalse(TcpProbeControllerTest.tcpProbeController.isKnownHost(TcpProbeControllerTest.LOCALHOST_IP,
                TcpProbeControllerTest.PORT));
        Assert.assertFalse(state.containsKey(TcpProbeControllerTest.PATTERN));

        TcpProbeControllerTest.tcpProbeController.activateMonitoredPattern(TcpProbeControllerTest.LOCALHOST_IP,
                TcpProbeControllerTest.PORT, TcpProbeControllerTest.TEST_HOST, TcpProbeControllerTest.PATTERN);

        Assert.assertTrue(TcpProbeControllerTest.tcpProbeController.isKnownHost(TcpProbeControllerTest.LOCALHOST_IP,
                TcpProbeControllerTest.PORT));
        // wait for the other thread
        while (!state.containsKey(TcpProbeControllerTest.PATTERN)) {
            Thread.yield();
        }
        Assert.assertTrue(state.get(TcpProbeControllerTest.PATTERN));
        TcpProbeControllerTest.tcpProbeController.deactivateMonitoredPattern(TcpProbeControllerTest.LOCALHOST_IP,
                TcpProbeControllerTest.PORT, TcpProbeControllerTest.TEST_HOST, TcpProbeControllerTest.PATTERN);
        while (state.get(TcpProbeControllerTest.PATTERN)) {
            Thread.yield();
        }
        Assert.assertFalse(state.get(TcpProbeControllerTest.PATTERN));

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
    private static class TestListener implements IRecordReceivedListener { // NOCS private class, no
                                                                           // constructor
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
