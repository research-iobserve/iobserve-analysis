/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.common.record.jvm.GCRecord;
import kieker.common.record.misc.KiekerMetadataRecord;

import teetime.framework.test.StageTester;

import org.hamcrest.core.Is;
import org.iobserve.common.record.ContainerAllocationEvent;
import org.iobserve.common.record.ContainerDeallocationEvent;
import org.iobserve.common.record.EJBDeployedEvent;
import org.iobserve.common.record.EJBUndeployedEvent;
import org.iobserve.common.record.IAllocationEvent;
import org.iobserve.common.record.IDeallocationEvent;
import org.iobserve.common.record.IDeployedEvent;
import org.iobserve.common.record.ISessionEvent;
import org.iobserve.common.record.IUndeployedEvent;
import org.iobserve.common.record.ServerGeoLocation;
import org.iobserve.common.record.ServletDeployedEvent;
import org.iobserve.common.record.ServletUndeployedEvent;
import org.iobserve.common.record.SessionEndEvent;
import org.iobserve.common.record.SessionStartEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for the stateless RecordSwitch filter. Note: Has one Non-stateless feature: record count.
 *
 * @author Reiner Jung
 *
 */
public class RecordSwitchTests {

    private static final long TRACE_ID = 1;
    private static final long THREAD_ID = 2;
    private static final String SESSION_ID = "123456789";
    private static final String HOSTNAME = "testhost";
    private static final String SERVICE = "test-service";
    private static final String CONTEXT = "/path/test";
    private static final String DEPLOYMENT_ID = "service-01";
    private static final String ADDRESS = "192.168.1.2";
    private static final short COUNTRY_CODE = 49;
    private static final String URL = "http://" + RecordSwitchTests.HOSTNAME + "/" + RecordSwitchTests.CONTEXT;
    private static final int ORDER_INDEX = 0;
    private static final String OPERATION_SIGNATURE = "a.b.c.d.Class.runTest()";
    private static final String CLASS_SIGNATURE = "a.b.c.d.Class";
    private static final String VERSION = "1.13";
    private static final String CONTROLLER_NAME = "test controller";
    private static final int EXPERIMENT_ID = 0;
    private static final boolean DEBUG_MODE = false;
    private static final long TIME_OFFSET = 0;
    private static final String TIME_UNIT = "ns";
    private static final long NUMBER_OF_RECORDS = 0;
    private static final String VM_NAME = "vm-name";
    private static final String GC_NAME = "gc-name";
    private static final long COLLECTION_COUNT = 10;
    private static final long COLLECTION_TIME_MS = 121230;

    private final List<IMonitoringRecord> inputRecords = new ArrayList<>();
    private final List<IDeployedEvent> deploymentRecords = new ArrayList<>();
    private final List<ServerGeoLocation> geolocationRecords = new ArrayList<>();
    private final List<ISessionEvent> sessionEventRecords = new ArrayList<>();
    private final List<IUndeployedEvent> undeploymentRecords = new ArrayList<>();
    private final List<IAllocationEvent> allocationRecords = new ArrayList<>();
    private final List<IDeallocationEvent> deallocationRecords = new ArrayList<>();
    private final List<IFlowRecord> flowRecords = new ArrayList<>();
    private final List<TraceMetadata> traceMetadataRecords = new ArrayList<>();
    private final List<KiekerMetadataRecord> kiekerMetadataRecords = new ArrayList<>();
    private final List<IMonitoringRecord> otherRecords = new ArrayList<>();
    private long time = 0;

    /**
     * Initialize the record switch test setup.
     */
    @Before
    public void initializeRecordSwitch() {

        this.kiekerMetadataRecords.add(new KiekerMetadataRecord(RecordSwitchTests.VERSION,
                RecordSwitchTests.CONTROLLER_NAME, RecordSwitchTests.HOSTNAME, RecordSwitchTests.EXPERIMENT_ID,
                RecordSwitchTests.DEBUG_MODE, RecordSwitchTests.TIME_OFFSET, RecordSwitchTests.TIME_UNIT,
                RecordSwitchTests.NUMBER_OF_RECORDS));

        /** allocation. */
        this.allocationRecords.add(new ContainerAllocationEvent(RecordSwitchTests.URL));

        /** declare deployment records. */
        this.deploymentRecords.add(new ServletDeployedEvent(this.time++, RecordSwitchTests.SERVICE,
                RecordSwitchTests.CONTEXT, RecordSwitchTests.DEPLOYMENT_ID));
        this.deploymentRecords.add(new EJBDeployedEvent(this.time++, RecordSwitchTests.SERVICE,
                RecordSwitchTests.CONTEXT, RecordSwitchTests.DEPLOYMENT_ID));

        /** geolocation. */
        this.geolocationRecords.add(new ServerGeoLocation(this.time++, RecordSwitchTests.COUNTRY_CODE,
                RecordSwitchTests.HOSTNAME, RecordSwitchTests.ADDRESS));

        /** session event (start). */
        final SessionStartEvent sessionStartEvent = new SessionStartEvent(this.time++, RecordSwitchTests.HOSTNAME,
                RecordSwitchTests.SESSION_ID);
        this.sessionEventRecords.add(sessionStartEvent);

        /** start trace. */
        final TraceMetadata traceMetadata = new TraceMetadata(RecordSwitchTests.TRACE_ID, RecordSwitchTests.THREAD_ID,
                RecordSwitchTests.SESSION_ID, RecordSwitchTests.HOSTNAME, 0, -1);
        this.traceMetadataRecords.add(traceMetadata);
        this.flowRecords.add(traceMetadata);

        /** flow record. */
        this.flowRecords
                .add(new BeforeOperationEvent(this.time++, RecordSwitchTests.TRACE_ID, RecordSwitchTests.ORDER_INDEX,
                        RecordSwitchTests.OPERATION_SIGNATURE, RecordSwitchTests.CLASS_SIGNATURE));
        this.flowRecords
                .add(new AfterOperationEvent(this.time++, RecordSwitchTests.TRACE_ID, RecordSwitchTests.ORDER_INDEX + 1,
                        RecordSwitchTests.OPERATION_SIGNATURE, RecordSwitchTests.CLASS_SIGNATURE));

        /** session event (end). */
        final SessionEndEvent sessionEndEvent = new SessionEndEvent(this.time++, RecordSwitchTests.HOSTNAME,
                RecordSwitchTests.SESSION_ID);
        this.sessionEventRecords.add(sessionEndEvent);

        /** declare undeployment records. */
        this.undeploymentRecords.add(new ServletUndeployedEvent(this.time++, RecordSwitchTests.SERVICE,
                RecordSwitchTests.CONTEXT, RecordSwitchTests.DEPLOYMENT_ID));
        this.undeploymentRecords.add(new EJBUndeployedEvent(this.time++, RecordSwitchTests.SERVICE,
                RecordSwitchTests.CONTEXT, RecordSwitchTests.DEPLOYMENT_ID));

        /** allocation. */
        this.deallocationRecords.add(new ContainerDeallocationEvent(RecordSwitchTests.URL));

        /** other records. */
        this.otherRecords.add(new GCRecord(this.time++, RecordSwitchTests.HOSTNAME, RecordSwitchTests.VM_NAME,
                RecordSwitchTests.GC_NAME, RecordSwitchTests.COLLECTION_COUNT, RecordSwitchTests.COLLECTION_TIME_MS));

        /** declare all input record types. */
        this.inputRecords.addAll(this.kiekerMetadataRecords);

        this.inputRecords.addAll(this.allocationRecords);

        this.inputRecords.addAll(this.deploymentRecords);

        this.inputRecords.addAll(this.geolocationRecords);

        this.inputRecords.add(sessionStartEvent);

        this.inputRecords.addAll(this.flowRecords);

        this.inputRecords.add(sessionEndEvent);

        this.inputRecords.addAll(this.undeploymentRecords);
        this.inputRecords.addAll(this.deallocationRecords);

        this.inputRecords.addAll(this.otherRecords);

    }

    /**
     * Test reporting elements of the record switch.
     */
    @Test
    public void checkLogCounting() {
        final RecordSwitch recordSwitch = new RecordSwitch();

        final List<IMonitoringRecord> thousandRecords = new ArrayList<>();

        for (int i = 0; i < 2000; i++) {
            thousandRecords.addAll(this.otherRecords);
        }

        StageTester.test(recordSwitch).and().send(thousandRecords).to(recordSwitch.getInputPort()).start();

        Assert.assertThat((int) recordSwitch.getRecordCount(), Is.is(thousandRecords.size()));
    }

    /**
     * Check if all records are counted correctly.
     */
    @Test
    public void checkRecordCount() {
        final RecordSwitch recordSwitch = new RecordSwitch();

        StageTester.test(recordSwitch).and().send(this.inputRecords).to(recordSwitch.getInputPort()).start();

        Assert.assertThat((int) recordSwitch.getRecordCount(), Is.is(this.inputRecords.size()));
    }

    /**
     * Check whether all deployment records are found.
     */
    @Test
    public void checkDeploymentDetection() {
        final RecordSwitch recordSwitch = new RecordSwitch();

        final List<IDeployedEvent> localDeploymentRecords = new ArrayList<>();

        StageTester.test(recordSwitch).and().send(this.inputRecords).to(recordSwitch.getInputPort()).and()
                .receive(localDeploymentRecords).from(recordSwitch.getDeployedOutputPort()).start();

        Assert.assertThat((int) recordSwitch.getRecordCount(), Is.is(this.inputRecords.size()));
        Assert.assertEquals("Wrong number of deployments", this.deploymentRecords.size(),
                localDeploymentRecords.size());
    }

    /**
     * Check whether all session events are found.
     */
    @Test
    public void checkSesionEventDetection() {
        final RecordSwitch recordSwitch = new RecordSwitch();

        final List<ISessionEvent> localSessionEventRecords = new ArrayList<>();

        StageTester.test(recordSwitch).and().send(this.inputRecords).to(recordSwitch.getInputPort()).and()
                .receive(localSessionEventRecords).from(recordSwitch.getSessionEventOutputPort()).start();

        Assert.assertThat((int) recordSwitch.getRecordCount(), Is.is(this.inputRecords.size()));
        Assert.assertEquals("Wrong number of session events", this.sessionEventRecords.size(),
                localSessionEventRecords.size());
    }

    /**
     * Check whether all undeployment events are found.
     */
    @Test
    public void checkUndeploymentDetection() {
        final RecordSwitch recordSwitch = new RecordSwitch();

        final List<IUndeployedEvent> localUndeploymentRecords = new ArrayList<>();

        StageTester.test(recordSwitch).and().send(this.inputRecords).to(recordSwitch.getInputPort()).and()
                .receive(localUndeploymentRecords).from(recordSwitch.getUndeployedOutputPort()).start();

        Assert.assertThat((int) recordSwitch.getRecordCount(), Is.is(this.inputRecords.size()));
        Assert.assertEquals("Wrong number of undeployments", this.undeploymentRecords.size(),
                localUndeploymentRecords.size());
    }

    /**
     * Check whether all allocation events are found.
     */
    @Test
    public void checkAllocationDetection() {
        final RecordSwitch recordSwitch = new RecordSwitch();

        final List<IAllocationEvent> localAllocationRecords = new ArrayList<>();

        StageTester.test(recordSwitch).and().send(this.inputRecords).to(recordSwitch.getInputPort()).and()
                .receive(localAllocationRecords).from(recordSwitch.getAllocationOutputPort()).start();

        Assert.assertThat((int) recordSwitch.getRecordCount(), Is.is(this.inputRecords.size()));
        Assert.assertEquals("Wrong number of allocations", this.allocationRecords.size(),
                localAllocationRecords.size());
    }

    /**
     * Check whether all deallocation events are found.
     */
    @Test
    public void checkDeallocationDetection() {
        final RecordSwitch recordSwitch = new RecordSwitch();

        final List<IDeallocationEvent> localDeallocationRecords = new ArrayList<>();

        StageTester.test(recordSwitch).and().send(this.inputRecords).to(recordSwitch.getInputPort()).and()
                .receive(localDeallocationRecords).from(recordSwitch.getDeallocationOutputPort()).start();

        Assert.assertThat((int) recordSwitch.getRecordCount(), Is.is(this.inputRecords.size()));
        Assert.assertEquals("Wrong number of deallocations", this.deallocationRecords.size(),
                localDeallocationRecords.size());
    }

    /**
     * Check whether all flow records are found.
     */
    @Test
    public void checkFlowRecordsDetection() {
        final RecordSwitch recordSwitch = new RecordSwitch();

        final List<IFlowRecord> localFlowRecords = new ArrayList<>();

        StageTester.test(recordSwitch).and().send(this.inputRecords).to(recordSwitch.getInputPort()).and()
                .receive(localFlowRecords).from(recordSwitch.getFlowOutputPort()).start();

        Assert.assertThat((int) recordSwitch.getRecordCount(), Is.is(this.inputRecords.size()));
        Assert.assertEquals("Mismatch in flow events.", this.flowRecords.size() + this.geolocationRecords.size(),
                localFlowRecords.size());
    }

    /**
     * Check whether all trace metadata records are found.
     */
    @Test
    public void checkTraceMetadataDetection() {
        final RecordSwitch recordSwitch = new RecordSwitch();

        final List<TraceMetadata> localTraceMetadataRecords = new ArrayList<>();

        StageTester.test(recordSwitch).and().send(this.inputRecords).to(recordSwitch.getInputPort()).and()
                .receive(localTraceMetadataRecords).from(recordSwitch.getTraceMetadataOutputPort()).start();

        Assert.assertThat((int) recordSwitch.getRecordCount(), Is.is(this.inputRecords.size()));
        Assert.assertEquals("Wrong number of trace metadata", this.traceMetadataRecords.size(),
                localTraceMetadataRecords.size());
    }

    /**
     * Check whether all kieker metadata records are ignored. Needs to check all ports for
     * KiekerMetadataRecord and record count.
     */
    @Test
    public void checkKiekerMetadataAndOtherRecordsIgnored() {
        final RecordSwitch recordSwitch = new RecordSwitch();

        final List<IDeallocationEvent> localDeallocationRecords = new ArrayList<>();
        final List<IAllocationEvent> localAllocationRecords = new ArrayList<>();
        final List<IDeployedEvent> localDeploymentRecords = new ArrayList<>();
        final List<IUndeployedEvent> localUndeploymentRecords = new ArrayList<>();
        final List<IFlowRecord> localFlowRecords = new ArrayList<>();
        final List<ISessionEvent> localSessionEventRecords = new ArrayList<>();
        final List<TraceMetadata> localTraceMetadataRecords = new ArrayList<>();

        StageTester.test(recordSwitch).and().send(this.inputRecords).to(recordSwitch.getInputPort()).and()
                .receive(localDeallocationRecords).from(recordSwitch.getDeallocationOutputPort()).and()
                .receive(localAllocationRecords).from(recordSwitch.getAllocationOutputPort()).and()
                .receive(localDeploymentRecords).from(recordSwitch.getDeployedOutputPort()).and()
                .receive(localUndeploymentRecords).from(recordSwitch.getUndeployedOutputPort()).and()
                .receive(localFlowRecords).from(recordSwitch.getFlowOutputPort()).and()
                .receive(localSessionEventRecords).from(recordSwitch.getSessionEventOutputPort()).and()
                .receive(localTraceMetadataRecords).from(recordSwitch.getTraceMetadataOutputPort()).start();
        /** Got all records. */
        Assert.assertEquals("Number of records did not match", recordSwitch.getRecordCount(), this.inputRecords.size());
        /** Test completeness of output. */

        Assert.assertEquals("Wrong number of deallocations", this.deallocationRecords.size(),
                localDeallocationRecords.size());
        Assert.assertEquals("Wrong number of allocations", this.allocationRecords.size(),
                localAllocationRecords.size());
        Assert.assertEquals("Wrong number of deployments", this.deploymentRecords.size(),
                localDeploymentRecords.size());
        Assert.assertEquals("Wrong number of undeployments", this.undeploymentRecords.size(),
                localUndeploymentRecords.size());
        Assert.assertEquals("Wrong number of flow records", this.flowRecords.size() + this.geolocationRecords.size(),
                localFlowRecords.size());
        Assert.assertEquals("Wrong number of session events", this.sessionEventRecords.size(),
                localSessionEventRecords.size());
        Assert.assertEquals("Wrong number of trace metadata", this.traceMetadataRecords.size(),
                localTraceMetadataRecords.size());

    }

}
