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
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Reiner Jung
 *
 */
public class SessionAndTraceRegistrationPayloadFilterTest {

    private ServletContext servletContext;
    private TestHttpSession httpSession;

    /**
     * Configure analysis.
     */
    @Before
    public void setUp() {
        this.servletContext = new TestServletContext();
        this.httpSession = new TestHttpSession(this.servletContext);
    }

    /**
     * Test method for
     * {@link SessionAndTraceRegistrationPayloadFilter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}.
     */
    @Test
    public void testDoFilter() {
        final SessionAndTraceRegistrationPayloadFilter filter = new SessionAndTraceRegistrationPayloadFilter();
        final FilterConfig filterConfig = new FilterConfig() {

            @Override
            public String getFilterName() {
                return "sessionAndTraceRegistrationFilter";
            }

            @Override
            public ServletContext getServletContext() {
                return SessionAndTraceRegistrationPayloadFilterTest.this.servletContext;
            }

            @Override
            public String getInitParameter(final String name) {
                return "example parameter";
            }

            @Override
            public Enumeration<String> getInitParameterNames() {
                return null;
            }

        };

        try {
            filter.init(filterConfig);
            filter.doFilter(this.createRequest(), this.createResponse(), this.createChain());
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
            }
        };
    }

    private ServletResponse createResponse() {
        return new TestHttpServletResponse();
    }

    private ServletRequest createRequest() {
        return new TestHttpServletRequest(this.servletContext, this.httpSession);
    }

}
