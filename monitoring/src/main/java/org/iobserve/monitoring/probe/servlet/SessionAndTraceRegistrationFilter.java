/***************************************************************************
 * Copyright (C) 2014 iObserve Project (https://www.iobserve-devops.net)
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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.core.registry.TraceRegistry;
import kieker.monitoring.probe.IMonitoringProbe;
import kieker.monitoring.timer.ITimeSource;

/**
 * For each incoming request via {@link #doFilter(ServletRequest, ServletResponse, FilterChain)},
 * this class (i) registers session and trace information into the thread-local data structures
 * {@link SessionRegistry} and {@link kieker.monitoring.core.registry.TraceRegistry} accessible to
 * other probes in the control-flow of this request, (ii) executes the given {@link FilterChain} and
 * subsequently (iii) unregisters the thread-local data. If configured in the {@link FilterConfig}
 * (see below), the execution of the {@link #doFilter(ServletRequest, ServletResponse, FilterChain)}
 * method is also part of the trace and logged to the {@link IMonitoringController} (note that this
 * is the default behavior when no property is found).
 *
 * The filter can be integrated into the web.xml as follows:
 *
 * <pre>
 * {@code
 * <filter>
 *   <filter-name>sessionAndTraceRegistrationFilter</filter-name>
 *   <filter-class>org.spp.iobserve.monitoring.probe.servlet.SessionAndTraceRegistrationFilter</filter-class>
 * </filter>
 * <filter-mapping>
 *   <filter-name>sessionAndTraceRegistrationFilter</filter-name>
 *   <url-pattern>/*</url-pattern>
 * </filter-mapping>
 * }
 * </pre>
 *
 * @author Reiner Jung
 */
public class SessionAndTraceRegistrationFilter implements Filter, IMonitoringProbe {
    /** constant for a property name. */
    public static final String CONFIG_PROPERTY_NAME_LOG_FILTER_EXECUTION = "logFilterExecution";

    /** Kieker monitoring controller. */
    protected static final IMonitoringController CTRLINST = MonitoringController.getInstance();
    /** Kieker session registry. */
    protected static final SessionRegistry SESSION_REGISTRY = SessionRegistry.INSTANCE;

    /** Kieker time source. */
    protected static final ITimeSource TIMESOURCE = SessionAndTraceRegistrationFilter.CTRLINST.getTimeSource();
    /** Host name of the host the code is running on. */
    protected static final String VM_NAME = SessionAndTraceRegistrationFilter.CTRLINST.getHostname();
    /** Kieker trace registry. */
    private static final TraceRegistry TRACEREGISTRY = TraceRegistry.INSTANCE;

    // private static final Log LOG = LogFactory.getLog(SessionAndTraceRegistrationFilter.class);

    /**
     * Create an SessionAndTraceRegistrationFilter and initialize the filter operation signature.
     */
    public SessionAndTraceRegistrationFilter() {
        // nothing to be done here at the moment.
    }

    @Override
    public void init(final FilterConfig config) throws ServletException {
        // the filter does not need any initialization at the time.
        // Just fulfilling API
    }

    /**
     * Register thread-local session and trace information, executes the given {@link FilterChain}
     * and unregisters the session/trace information. If configured, the execution of this filter is
     * also logged to the {@link IMonitoringController}. This method returns immediately if
     * monitoring is not enabled.
     *
     * @param request
     *            The request.
     * @param response
     *            The response.
     * @param chain
     *            The filter chain to be used.
     *
     * @throws IOException
     *             on io errors
     * @throws ServletException
     *             on servlet errors
     */
    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        if (SessionAndTraceRegistrationFilter.CTRLINST.isMonitoringEnabled()) {
            // if (CTRLINST.isProbeActivated(this.filterOperationSignatureString)) {

            final String operationSignature;
            final String componentSignature;
            final String method;
            final String path;
            final String sessionId;
            String query;

            if (request instanceof HttpServletRequest) {
                final HttpServletRequest httpRequest = (HttpServletRequest) request;
                method = httpRequest.getMethod();
                path = httpRequest.getRequestURI().replace('/', '.').substring(1);
                sessionId = httpRequest.getSession().getId();
                query = httpRequest.getQueryString();
                if (query == null) {
                    query = "";
                }
            } else {
                method = "POST";

                path = ""; // TODO this is not sufficient, but for now we have to use it
                sessionId = "<no session>";
                query = "";
            }

            componentSignature = path.replaceAll("\\.[A-Za-z0-9]*$", "");

            TraceMetadata trace = SessionAndTraceRegistrationFilter.TRACEREGISTRY.getTrace();
            final boolean newTrace = trace == null;

            if (newTrace) {
                SessionRegistry.INSTANCE.storeThreadLocalSessionId(sessionId);
                trace = SessionAndTraceRegistrationFilter.TRACEREGISTRY.registerTrace();
                SessionAndTraceRegistrationFilter.CTRLINST.newMonitoringRecord(trace);
            }

            if ("GET".equals(method)) {
                operationSignature = path + "(" + query.replace(';', ':') + ")";
            } else if ("POST".equals(method)) {
                operationSignature = path + "()";
            } else {
                chain.doFilter(request, response);
                return;
            }

            final long traceId = trace.getTraceId();

            try {
                SessionAndTraceRegistrationFilter.CTRLINST.newMonitoringRecord(
                        new BeforeOperationEvent(SessionAndTraceRegistrationFilter.TIMESOURCE.getTime(), traceId,
                                trace.getNextOrderId(), operationSignature, componentSignature));
                chain.doFilter(request, response);
                SessionAndTraceRegistrationFilter.CTRLINST.newMonitoringRecord(
                        new AfterOperationEvent(SessionAndTraceRegistrationFilter.TIMESOURCE.getTime(), traceId,
                                trace.getNextOrderId(), operationSignature, componentSignature));
            } catch (final Throwable th) { // NOPMD NOCS (catch throw is ok here)
                SessionAndTraceRegistrationFilter.CTRLINST.newMonitoringRecord(
                        new AfterOperationFailedEvent(SessionAndTraceRegistrationFilter.TIMESOURCE.getTime(), traceId,
                                trace.getNextOrderId(), operationSignature, componentSignature, th.toString()));
                throw new ServletException(th);
            } finally {
                // is this correct?
                SessionAndTraceRegistrationFilter.SESSION_REGISTRY.unsetThreadLocalSessionId();
                // Reset the thread-local trace information
                if (newTrace) { // close the trace
                    SessionAndTraceRegistrationFilter.TRACEREGISTRY.unregisterTrace();
                }
            }
            // } else {
            // chain.doFilter(request, response);
            // return;
            // }
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        // by default, we do nothing here. Extending classes may override this method
    }

    /**
     * If the given {@link ServletRequest} is an instance of {@link HttpServletRequest}, this
     * methods extracts the session ID and registers it in the {@link #SESSION_REGISTRY} in order to
     * be accessible for other probes in this thread. In case no session is associated with this
     * request (or if the request is not an instance of {@link HttpServletRequest}), this method
     * returns without any further actions and returns
     * {@link kieker.common.record.controlflow.OperationExecutionRecord#NO_SESSION_ID}.
     *
     * @param request
     *            The request.
     *
     * @return The session ID.
     */
    protected String registerSessionInformation(final ServletRequest request) {
        String sessionId = TraceMetadata.NO_SESSION_ID;

        if (request == null || !(request instanceof HttpServletRequest)) {
            return sessionId;
        }

        final HttpSession session = ((HttpServletRequest) request).getSession(false);
        if (session != null) {
            sessionId = session.getId();
            SessionAndTraceRegistrationFilter.SESSION_REGISTRY.storeThreadLocalSessionId(sessionId);
        }

        return sessionId;
    }
}
