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
import java.nio.CharBuffer;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.IValueSerializer;
import kieker.common.record.io.TextValueSerializer;
import kieker.monitoring.core.configuration.ConfigurationFactory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Reiner Jung
 *
 */
public class TestSessionAndTraceRegistrationPayloadFilter {

    private static final String WRITER_NAME = TestDumpWriter.class.getCanonicalName();

    static {
        System.setProperty(ConfigurationFactory.CONTROLLER_NAME, "iObserve-Experiments");
        System.setProperty(ConfigurationFactory.WRITER_CLASSNAME,
                TestSessionAndTraceRegistrationPayloadFilter.WRITER_NAME);
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {

    }

    /**
     * Test method for
     * {@link org.iobserve.monitoring.probe.servlet.SessionAndTraceRegistrationPayloadFilter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}.
     *
     * @throws InterruptedException
     */
    @Test
    public void testDoFilter() throws InterruptedException {

        final ServletContext context = new TestServletContext();
        final ServletResponse response = new TestHttpServletResponse();
        final HttpSession session = new TestHttpSession(context);
        final HttpServletRequest request = new TestHttpServletRequest(context, session);
        final FilterConfig filterConfig = new TestFilterConfig(context);

        final SessionAndTraceRegistrationPayloadFilter filter = new SessionAndTraceRegistrationPayloadFilter();

        try {
            filter.init(filterConfig);
            filter.doFilter(request, response, this.createChain());

            final List<IMonitoringRecord> storage = TestDumpWriter.getRecords();

            Thread.sleep(1000);

            final CharBuffer buffer = CharBuffer.allocate(4000);
            final IValueSerializer serializer = TextValueSerializer.create(buffer);

            for (final IMonitoringRecord record : storage) {
                record.serialize(serializer);
                buffer.flip();
                System.out.println("> " + record.getClass().getCanonicalName() + " " + buffer);
                buffer.clear();
            }

            Assert.assertEquals("Test value", filter, filter);
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
