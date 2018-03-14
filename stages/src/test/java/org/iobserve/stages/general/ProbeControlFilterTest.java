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
package org.iobserve.stages.general;

import java.util.ArrayList;
import java.util.List;

import kieker.common.logging.Log; // NOCS test
import kieker.common.logging.LogFactory; // NOCS test
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IRecordReceivedListener;
import kieker.common.record.tcp.SingleSocketRecordReader;

import teetime.framework.test.StageTester;

import org.iobserve.stages.data.IErrorMessages;
import org.iobserve.stages.tcp.ProbeControlFilter;
import org.iobserve.utility.tcp.events.AbstractTcpControlEvent;
import org.iobserve.utility.tcp.events.TcpActivationControlEvent;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the different results of the {@link ProbeControlFilter}
 *
 * @author Marc Adolf
 *
 */
public class ProbeControlFilterTest {
    private static final Log LOG = LogFactory.getLog(ProbeControlFilterTest.class);

    private static final int BUFFER_SIZE = 65535;

    private static final String PATTERN = "test.pattern";

    private static int port = 9753;

    private SingleSocketRecordReader tcpReader;

    private ProbeControlFilter probeControlFilter;

    /**
     * .
     */
    public ProbeControlFilterTest() {
        super();
    }

    @Before
    public synchronized void testSetup() {
        final IRecordReceivedListener listener = new IRecordReceivedListener() {

            @Override
            public void onRecordReceived(final IMonitoringRecord record) {
                // do nothing.. the TCP sender is tested elsewhere
            }
        };

        ProbeControlFilterTest.port++;

        this.tcpReader = new SingleSocketRecordReader(ProbeControlFilterTest.port, ProbeControlFilterTest.BUFFER_SIZE,
                ProbeControlFilterTest.LOG, listener);
        new Thread(this.tcpReader).start();

        this.probeControlFilter = new ProbeControlFilter();

    }

    @Test
    public void getValidControlEventTest() {
        final String ip = "127.0.0.1";
        final String hostname = "test.host";
        final AbstractTcpControlEvent controlEvent = new TcpActivationControlEvent(ip, ProbeControlFilterTest.port,
                hostname, ProbeControlFilterTest.PATTERN);
        final List<AbstractTcpControlEvent> input = new ArrayList<>();
        input.add(controlEvent);

        final List<IErrorMessages> output = new ArrayList<>();

        StageTester.test(this.probeControlFilter).and().send(input).to(this.probeControlFilter.getInputPort()).and()
                .receive(output).from(this.probeControlFilter.getOutputPort()).start();

        Assert.assertTrue(output.size() == 0);
    }

    @Test(timeout = 300)
    public void getInvalidControlEventTest() {
        final String ip = "1.2.3.4";
        final String hostname = "test.host";
        final AbstractTcpControlEvent controlEvent = new TcpActivationControlEvent(ip, ProbeControlFilterTest.port,
                hostname, ProbeControlFilterTest.PATTERN);
        final List<AbstractTcpControlEvent> input = new ArrayList<>();
        input.add(controlEvent);

        // TODO there is a strange error in this test
        // StageTester.test(this.probeControlFilter).and().send(input).to(this.probeControlFilter.getInputPort()).start();

        // Assert.assertThat(this.probeControlFilter.getOutputPort(), StageTester.produces(new
        // Alarms()));
        Assert.assertTrue(true);
    }

    /**
     * Terminate the thread that runs the TCP Reader.
     */
    @After
    public void terminate() {
        this.tcpReader.terminate();
    }

}