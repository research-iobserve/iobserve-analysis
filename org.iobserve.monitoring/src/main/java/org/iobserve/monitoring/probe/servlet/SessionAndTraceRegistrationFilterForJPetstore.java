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
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.iobserve.analysis.data.ExtendedAfterOperationEvent;
import org.iobserve.analysis.filter.models.CallInformation;

import com.fasterxml.jackson.databind.ObjectMapper;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.flow.trace.TraceMetadata;
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
 *   <filter-name>SessionAndTraceRegistrationFilterForJPetstore</filter-name>
 *   <filter-class>org.spp.iobserve.monitoring.probe.servlet.SessionAndTraceRegistrationFilterForJPetstoreForJPetstore</filter-class>
 * </filter>
 * <filter-mapping>
 *   <filter-name>SessionAndTraceRegistrationFilterForJPetstore</filter-name>
 *   <url-pattern>/*</url-pattern>
 * </filter-mapping>
 * }
 * </pre>
 *
 * @author Reiner Jung
 * @author Christoph Dornieden
 */
public class SessionAndTraceRegistrationFilterForJPetstore implements Filter, IMonitoringProbe {
    /** constant for a property name. */
    public static final String CONFIG_PROPERTY_NAME_LOG_FILTER_EXECUTION = "logFilterExecution";

    /** Kieker monitoring controller. */
    protected static final IMonitoringController CTRLINST = MonitoringController.getInstance();
    /** Kieker session registry. */
    protected static final SessionRegistry SESSION_REGISTRY = SessionRegistry.INSTANCE;

    /** Kieker time source. */
    protected static final ITimeSource TIMESOURCE = SessionAndTraceRegistrationFilterForJPetstore.CTRLINST
            .getTimeSource();
    /** Host name of the host the code is running on. */
    protected static final String VM_NAME = SessionAndTraceRegistrationFilterForJPetstore.CTRLINST.getHostname();
    /** Kieker trace registry. */
    private static final TraceRegistry TRACEREGISTRY = TraceRegistry.INSTANCE;
    private static final int INFORMATION_VALUE = 1;

    // precompile pattern for performance
    private final Pattern pathStrucPattern = Pattern.compile("\\.[A-Za-z0-9\\(\\)]*$");
    private final Pattern equalsPattern = Pattern.compile("=");
    private final Pattern andPattern = Pattern.compile("&");
    private final Pattern isAccountPattern = Pattern.compile("(\\w*\\.)*Account\\.*");
    private final Pattern isActionPattern = Pattern.compile("\\w*\\.actions\\..*");
    private final Pattern isImagePattern = Pattern.compile("\\w*\\.images\\..*");
    private final Pattern removeActionOfIndexPattern = Pattern.compile("\\.action\\(");
    private final Pattern removeActionOfOperationPattern = Pattern.compile("action\\.");

    private static final Log LOG = LogFactory.getLog(SessionAndTraceRegistrationFilterForJPetstore.class);

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
        if (SessionAndTraceRegistrationFilterForJPetstore.CTRLINST.isMonitoringEnabled()) {
            // if (CTRLINST.isProbeActivated(this.filterOperationSignatureString)) {

            String operationSignature;
            String componentSignature;
            final String method;
            final String path;
            final String sessionId;
            final ArrayList<CallInformation> callInformations = new ArrayList<>();

            String query = null;
            String[] queryParameters = null;

            if (request instanceof HttpServletRequest) {
                final HttpServletRequest httpRequest = (HttpServletRequest) request;
                method = httpRequest.getMethod();
                final String requestPath = httpRequest.getRequestURI().replace('/', '.').substring(1);

                // remove sessionId from request Path
                path = requestPath.contains(";") ? requestPath.substring(0, requestPath.indexOf(";")) : requestPath;

                sessionId = httpRequest.getSession().getId();

                query = httpRequest.getQueryString();

                if (query == null) {
                    query = "";
                } else {
                    queryParameters = this.andPattern.split(query);
                }
            } else {
                method = "POST";
                path = request.getServletContext().getContextPath().replace('/', '.').substring(1);
                sessionId = "<no session>";
                query = "";
            }

            final String trimmedPath = path;

            TraceMetadata trace = SessionAndTraceRegistrationFilterForJPetstore.TRACEREGISTRY.getTrace();
            final boolean newTrace = trace == null;

            if (newTrace) {
                SessionRegistry.INSTANCE.storeThreadLocalSessionId(sessionId);
                trace = SessionAndTraceRegistrationFilterForJPetstore.TRACEREGISTRY.registerTrace();
                SessionAndTraceRegistrationFilterForJPetstore.CTRLINST.newMonitoringRecord(trace);
            }

            if ("GET".equals(method)) {

                if ((queryParameters != null)) {
                    // pattern ="jpetstore\\.actions\\."
                    operationSignature = trimmedPath + "."
                            + this.equalsPattern.matcher(queryParameters[0]).replaceAll("") + "()";

                    // is operation called with parameters
                    if ((queryParameters.length > 1)) {
                        // then add the parameters as call informations
                        for (int i = 1; i < queryParameters.length; i++) {
                            final String[] queryParameterSplit = this.equalsPattern.split(queryParameters[i]);

                            callInformations.add(new CallInformation(queryParameterSplit[i],
                                    SessionAndTraceRegistrationFilterForJPetstore.INFORMATION_VALUE));
                        }
                    }

                    // pattern = "\\.action\\."
                    operationSignature = this.removeActionOfOperationPattern.matcher(operationSignature).replaceAll("");

                } else {
                    // pattern = "\\w*\\.actions\\."
                    if (this.isActionPattern.matcher(trimmedPath).matches()) {
                        // pattern ="jpetstore\\.actions\\."
                        operationSignature = trimmedPath + "()";

                        // pattern = "\\.action\\("
                        operationSignature = this.removeActionOfIndexPattern.matcher(operationSignature)
                                .replaceAll(".index(");
                        SessionAndTraceRegistrationFilterForJPetstore.LOG.debug("SINGLE OP");

                        // pattern "\\w*\\.images\\."
                    } else if (this.isImagePattern.matcher(trimmedPath).matches()) {
                        operationSignature = trimmedPath;
                        SessionAndTraceRegistrationFilterForJPetstore.LOG.debug("IMAGE");

                    } else {
                        SessionAndTraceRegistrationFilterForJPetstore.LOG.debug("ELSE");
                        operationSignature = trimmedPath + "()";

                    }
                }

            } else if ("POST".equals(method)) {

                operationSignature = trimmedPath + "()";

                // matches "(\\w*\\.)*Account\\.*" ?
                if (this.isAccountPattern.matcher(operationSignature).matches()) {

                } else {
                    // post on the account is always a login
                    // pattern = "\\.action\\("
                    operationSignature = this.removeActionOfIndexPattern.matcher(operationSignature)
                            .replaceAll(".login(");
                }

                // pattern = "\\.action\\."
                operationSignature = this.removeActionOfOperationPattern.matcher(operationSignature).replaceAll("");

            } else {
                chain.doFilter(request, response);
                return;
            }
            componentSignature = this.pathStrucPattern.matcher(operationSignature).replaceAll("");
            SessionAndTraceRegistrationFilterForJPetstore.LOG.debug(operationSignature);

            final long traceId = trace.getTraceId();

            try {
                // mapps Object to String
                final ObjectMapper objectMapper = new ObjectMapper();

                SessionAndTraceRegistrationFilterForJPetstore.CTRLINST.newMonitoringRecord(
                        new BeforeOperationEvent(SessionAndTraceRegistrationFilterForJPetstore.TIMESOURCE.getTime(),
                                traceId, trace.getNextOrderId(), operationSignature, componentSignature));

                chain.doFilter(request, response);

                SessionAndTraceRegistrationFilterForJPetstore.CTRLINST
                        .newMonitoringRecord(new ExtendedAfterOperationEvent(
                                SessionAndTraceRegistrationFilterForJPetstore.TIMESOURCE.getTime(), traceId,
                                trace.getNextOrderId(), operationSignature, componentSignature,
                                objectMapper.writeValueAsString(callInformations)));

            } catch (final Throwable th) { // NOPMD NOCS (catch throw is ok here)
                SessionAndTraceRegistrationFilterForJPetstore.CTRLINST
                        .newMonitoringRecord(new AfterOperationFailedEvent(
                                SessionAndTraceRegistrationFilterForJPetstore.TIMESOURCE.getTime(), traceId,
                                trace.getNextOrderId(), operationSignature, componentSignature, th.toString()));
                throw new ServletException(th);
            } finally {
                // is this correct?
                SessionAndTraceRegistrationFilterForJPetstore.SESSION_REGISTRY.unsetThreadLocalSessionId();
                // Reset the thread-local trace information
                if (newTrace) { // close the trace
                    SessionAndTraceRegistrationFilterForJPetstore.TRACEREGISTRY.unregisterTrace();
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

        if ((request == null) || !(request instanceof HttpServletRequest)) {
            return sessionId;
        }

        final HttpSession session = ((HttpServletRequest) request).getSession(false);
        if (session != null) {
            sessionId = session.getId();
            SessionAndTraceRegistrationFilterForJPetstore.SESSION_REGISTRY.storeThreadLocalSessionId(sessionId);
        }

        return sessionId;
    }
}
