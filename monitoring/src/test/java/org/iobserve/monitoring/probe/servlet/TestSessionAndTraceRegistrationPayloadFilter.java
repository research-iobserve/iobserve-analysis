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
package org.iobserve.monitoring.probe.servlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.misc.KiekerMetadataRecord;
import kieker.monitoring.core.configuration.ConfigurationKeys;
import kieker.monitoring.core.controller.MonitoringController;

import org.iobserve.common.record.EntryLevelBeforeOperationEvent;
import org.junit.Assert;

/**
 * @author Reiner Jung
 *
 */
public class TestSessionAndTraceRegistrationPayloadFilter {

    private static final String WRITER_NAME = TestDumpWriter.class.getCanonicalName();

    static {
        System.setProperty(ConfigurationKeys.CONTROLLER_NAME, "iObserve-Experiments");
        System.setProperty(ConfigurationKeys.WRITER_CLASSNAME,
                TestSessionAndTraceRegistrationPayloadFilter.WRITER_NAME);
    }

    /**
     * Test method for
     * {@link org.iobserve.monitoring.probe.servlet.SessionAndTraceRegistrationPayloadFilter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}.
     *
     * @throws InterruptedException
     *             during the sleep time of the thread
     */

    // TODO Something goes occasionally wrong in this test. FixME!
    // @Test
    public void testDoFilter() throws InterruptedException {

        final ServletContext context = new TestServletContext();
        final ServletResponse response = new TestHttpServletResponse();
        final HttpSession session = new TestHttpSession(context);
        final TestHttpServletRequest request = new TestHttpServletRequest(context, session);
        final FilterConfig filterConfig = new TestFilterConfig(context);

        final SessionAndTraceRegistrationPayloadFilter filter = new SessionAndTraceRegistrationPayloadFilter();

        try {
            filter.init(filterConfig);
            filter.doFilter(request, response, this.createChain());

            MonitoringController.getInstance().terminateMonitoring();
            while (!MonitoringController.getInstance().isMonitoringTerminated()) {
                Thread.sleep(1000);
            }

            final List<IMonitoringRecord> storage = TestDumpWriter.getRecords();

            Assert.assertTrue("No records received", storage.size() > 0);

            IMonitoringRecord record = storage.get(0);
            Assert.assertEquals("Should be KiekerMetadataRecord", KiekerMetadataRecord.class, record.getClass());
            record = storage.get(1);
            Assert.assertEquals("Should be TraceMetadata", TraceMetadata.class, record.getClass());
            record = storage.get(2);
            Assert.assertEquals("Should be EntryLevelBeforeOperationEvent", EntryLevelBeforeOperationEvent.class,
                    record.getClass());
            if (record instanceof EntryLevelBeforeOperationEvent) {
                final EntryLevelBeforeOperationEvent beforeEvent = (EntryLevelBeforeOperationEvent) record;

                Assert.assertEquals("Class signature does not match", TestServletContext.CONTEXT_PATH,
                        beforeEvent.getClassSignature());
                Assert.assertEquals("Operation signature does not match", TestHttpServletRequest.REQUEST_URI,
                        beforeEvent.getOperationSignature());

                final Iterator<String> keys = request.getParameters().keySet().iterator();
                for (final String label : beforeEvent.getParameters()) {
                    if (!keys.hasNext()) {
                        Assert.fail("Found more parameters than defined in the request.");
                    } else {
                        Assert.assertEquals("Same parameter", keys.next(), label);
                    }
                }

                final Iterator<String> values = request.getParameters().values().iterator();
                for (final String label : beforeEvent.getValues()) {
                    if (!values.hasNext()) {
                        Assert.fail("Found more parameters than defined in the request.");
                    } else {
                        Assert.assertEquals("Same parameter", values.next(), label);
                    }
                }

            }
            record = storage.get(3);
            Assert.assertEquals("Should be AfterOperationEvent", AfterOperationEvent.class, record.getClass());

            Assert.assertEquals("Wrong number of records ", 4, storage.size());
        } catch (final ServletException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private FilterChain createChain() {
        return new FilterChain() {

            @Override
            public void doFilter(final ServletRequest request, final ServletResponse response)
                    throws IOException, ServletException {
                // Nothing to be done in the test.
                // Assertion could be checked here.
            }
        };
    }

}
