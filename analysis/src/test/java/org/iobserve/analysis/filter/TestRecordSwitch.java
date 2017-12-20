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
package org.iobserve.analysis.filter;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.core.Is;
import org.iobserve.common.record.IDeploymentRecord;
import org.iobserve.common.record.ServletDeployedEvent;
import org.iobserve.stages.general.RecordSwitch;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.trace.TraceMetadata;
import teetime.framework.test.StageTester;

/**
 * Test for the stateless RecordSwitch filter. Note: Has one Non-stateless feature: record count.
 *
 * @author Reiner Jung
 *
 */
public class TestRecordSwitch {

    private static final long TRACE_ID = 1;
    private static final long THREAD_ID = 2;
    private static final String SESSION_ID = "123456789";
    private static final String HOSTNAME = "testhost";
    private static final long DEPLOY_TIME = 1;
    private static final String SERVICE = "test-service";
    private static final String CONTEXT = "/path/test";
    private static final String DEPLOYMENT_ID = "service-01";

    private RecordSwitch recordSwitch;
    private final List<IMonitoringRecord> inputRecords = new ArrayList<>();
    private final List<IDeploymentRecord> deploymentResults = new ArrayList<>();

    /**
     * Initialize the record switch test setup.
     */
    @Before
    public void initializeRecordSwitch() {
        this.recordSwitch = new RecordSwitch();

        final ServletDeployedEvent servletDeployment = new ServletDeployedEvent(TestRecordSwitch.DEPLOY_TIME,
                TestRecordSwitch.SERVICE, TestRecordSwitch.CONTEXT, TestRecordSwitch.DEPLOYMENT_ID);

        /** declare all input record types. */
        this.inputRecords.add(servletDeployment);
        this.inputRecords.add(new TraceMetadata(TestRecordSwitch.TRACE_ID, TestRecordSwitch.THREAD_ID,
                TestRecordSwitch.SESSION_ID, TestRecordSwitch.HOSTNAME, 0, -1));

        /** declare deployment records. */
        this.deploymentResults.add(servletDeployment);
        // TODO add one for each type
    }

    /**
     * Check if all records are counted correctly.
     */
    @Test
    public void checkRecordCount() {
        StageTester.test(this.recordSwitch).and().send(this.inputRecords).to(this.recordSwitch.getInputPort()).start();

        Assert.assertThat((int) this.recordSwitch.getRecordCount(), Is.is(this.inputRecords.size()));
    }

    /**
     * Check whether all deployment records are found.
     */
    @Test
    public void checkDeploymentDetection() {
        StageTester.test(this.recordSwitch).and().send(this.inputRecords).to(this.recordSwitch.getInputPort()).and()
                .receive(this.deploymentResults).from(this.recordSwitch.getDeploymentOutputPort()).start();

        Assert.assertThat((int) this.recordSwitch.getRecordCount(), Is.is(this.inputRecords.size()));
    }

    // TODO add tests for all other ports.

}
