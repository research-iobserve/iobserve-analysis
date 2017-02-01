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
import java.util.HashMap;
import java.util.Map;

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

    /** Code Map **/
    private static Map<String, Integer> codes;

    /** Category Codes **/
    private static final int CATEGORY_FISH = 100;
    private static final int CATEGORY_DOGS = 200;
    private static final int CATEGORY_REPTILES = 300;
    private static final int CATEGORY_CATS = 400;
    private static final int CATEGORY_BIRDS = 500;

    /** Product Codes **/
    private static final int PRODUCT_FI_SW_01 = 100;
    private static final int PRODUCT_FI_SW_02 = 101;
    private static final int PRODUCT_FI_FW_01 = 110;
    private static final int PRODUCT_FI_FW_02 = 111;

    private static final int PRODUCT_K9_BD_01 = 200;
    private static final int PRODUCT_K9_PO_02 = 203;
    private static final int PRODUCT_K9_DL_01 = 206;
    private static final int PRODUCT_K9_RT_01 = 209;
    private static final int PRODUCT_K9_RT_02 = 210;
    private static final int PRODUCT_K9_CW_01 = 213;

    private static final int PRODUCT_RP_SN_01 = 300;
    private static final int PRODUCT_RP_LI_02 = 320;

    private static final int PRODUCT_FL_DSH_01 = 400;
    private static final int PRODUCT_FL_DLH_02 = 401;

    private static final int PRODUCT_AV_CB_01 = 500;
    private static final int PRODUCT_AV_SB_02 = 505;

    /** boolean codes **/
    private static final int BOOL_TRUE = 500;
    private static final int BOOL_FALSE = 0;

    // private static final Log LOG =
    // LogFactory.getLog(SessionAndTraceRegistrationFilterForJPetstore.class);

    /**
     * Create an SessionAndTraceRegistrationFilterForJPetstore and initialize the filter operation
     * signature.
     */
    public SessionAndTraceRegistrationFilterForJPetstore() {
        SessionAndTraceRegistrationFilterForJPetstore.codes = new HashMap<>();
        SessionAndTraceRegistrationFilterForJPetstore.codes.put("FISH",
                SessionAndTraceRegistrationFilterForJPetstore.CATEGORY_FISH);
        SessionAndTraceRegistrationFilterForJPetstore.codes.put("CATS",
                SessionAndTraceRegistrationFilterForJPetstore.CATEGORY_CATS);
        SessionAndTraceRegistrationFilterForJPetstore.codes.put("DOGS",
                SessionAndTraceRegistrationFilterForJPetstore.CATEGORY_DOGS);
        SessionAndTraceRegistrationFilterForJPetstore.codes.put("REPTILES",
                SessionAndTraceRegistrationFilterForJPetstore.CATEGORY_REPTILES);
        SessionAndTraceRegistrationFilterForJPetstore.codes.put("BIRDS",
                SessionAndTraceRegistrationFilterForJPetstore.CATEGORY_BIRDS);

        SessionAndTraceRegistrationFilterForJPetstore.codes.put("FI-SW-01",
                SessionAndTraceRegistrationFilterForJPetstore.PRODUCT_FI_SW_01);
        SessionAndTraceRegistrationFilterForJPetstore.codes.put("FI-SW-02",
                SessionAndTraceRegistrationFilterForJPetstore.PRODUCT_FI_SW_02);
        SessionAndTraceRegistrationFilterForJPetstore.codes.put("FI-FW-01",
                SessionAndTraceRegistrationFilterForJPetstore.PRODUCT_FI_FW_01);
        SessionAndTraceRegistrationFilterForJPetstore.codes.put("FI-FW-02",
                SessionAndTraceRegistrationFilterForJPetstore.PRODUCT_FI_FW_02);

        SessionAndTraceRegistrationFilterForJPetstore.codes.put("K9-BD-01",
                SessionAndTraceRegistrationFilterForJPetstore.PRODUCT_K9_BD_01);
        SessionAndTraceRegistrationFilterForJPetstore.codes.put("K9-PO-02",
                SessionAndTraceRegistrationFilterForJPetstore.PRODUCT_K9_PO_02);
        SessionAndTraceRegistrationFilterForJPetstore.codes.put("K9-DL-01",
                SessionAndTraceRegistrationFilterForJPetstore.PRODUCT_K9_DL_01);
        SessionAndTraceRegistrationFilterForJPetstore.codes.put("K9-RT-01",
                SessionAndTraceRegistrationFilterForJPetstore.PRODUCT_K9_RT_01);
        SessionAndTraceRegistrationFilterForJPetstore.codes.put("K9-RT-02",
                SessionAndTraceRegistrationFilterForJPetstore.PRODUCT_K9_RT_02);
        SessionAndTraceRegistrationFilterForJPetstore.codes.put("K9-CW-01",
                SessionAndTraceRegistrationFilterForJPetstore.PRODUCT_K9_CW_01);

        SessionAndTraceRegistrationFilterForJPetstore.codes.put("RP-SN-01",
                SessionAndTraceRegistrationFilterForJPetstore.PRODUCT_RP_SN_01);
        SessionAndTraceRegistrationFilterForJPetstore.codes.put("RP-LI-02",
                SessionAndTraceRegistrationFilterForJPetstore.PRODUCT_RP_LI_02);

        SessionAndTraceRegistrationFilterForJPetstore.codes.put("FL-DSH-01",
                SessionAndTraceRegistrationFilterForJPetstore.PRODUCT_FL_DSH_01);
        SessionAndTraceRegistrationFilterForJPetstore.codes.put("FL-DLH-02",
                SessionAndTraceRegistrationFilterForJPetstore.PRODUCT_FL_DLH_02);

        SessionAndTraceRegistrationFilterForJPetstore.codes.put("AV-CB-01",
                SessionAndTraceRegistrationFilterForJPetstore.PRODUCT_AV_CB_01);
        SessionAndTraceRegistrationFilterForJPetstore.codes.put("AV-SB-02",
                SessionAndTraceRegistrationFilterForJPetstore.PRODUCT_AV_SB_02);

        SessionAndTraceRegistrationFilterForJPetstore.codes.put("true",
                SessionAndTraceRegistrationFilterForJPetstore.BOOL_TRUE);
        SessionAndTraceRegistrationFilterForJPetstore.codes.put("false",
                SessionAndTraceRegistrationFilterForJPetstore.BOOL_FALSE);

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
        if (SessionAndTraceRegistrationFilterForJPetstore.CTRLINST.isMonitoringEnabled()) {
            // if (CTRLINST.isProbeActivated(this.filterOperationSignatureString)) {

            final String operationSignature;
            final String componentSignature;
            final String method;
            final String path;
            final String sessionId;
            final ArrayList<CallInformation> callInformations = new ArrayList<>();

            String query = null;
            String[] queryParameters = null;

            if (request instanceof HttpServletRequest) {
                final HttpServletRequest httpRequest = (HttpServletRequest) request;
                method = httpRequest.getMethod();
                path = httpRequest.getRequestURI().replace('/', '.').substring(1);
                sessionId = httpRequest.getSession().getId();
                query = httpRequest.getQueryString();

                if (query == null) {
                    query = "";
                } else {
                    queryParameters = query.split("&");
                }
            } else {
                method = "POST";
                path = request.getServletContext().getContextPath().replace('/', '.').substring(1);
                sessionId = "<no session>";
                query = "";
            }

            componentSignature = path.replaceAll("\\.[A-Za-z0-9]*$", "");

            TraceMetadata trace = SessionAndTraceRegistrationFilterForJPetstore.TRACEREGISTRY.getTrace();
            final boolean newTrace = trace == null;

            if (newTrace) {
                SessionRegistry.INSTANCE.storeThreadLocalSessionId(sessionId);
                trace = SessionAndTraceRegistrationFilterForJPetstore.TRACEREGISTRY.registerTrace();
                SessionAndTraceRegistrationFilterForJPetstore.CTRLINST.newMonitoringRecord(trace);
            }

            if ("GET".equals(method)) {
                if ((queryParameters != null) && (queryParameters.length == 2)) {

                    operationSignature = path + "(" + queryParameters[0] + ")";

                    final String[] queryParameterSplit = queryParameters[1].split("=");

                    final Long code = SessionAndTraceRegistrationFilterForJPetstore.codes
                            .containsKey(queryParameterSplit[1])
                                    ? SessionAndTraceRegistrationFilterForJPetstore.codes.get(queryParameterSplit[1])
                                    : -100L;

                    callInformations.add(new CallInformation(queryParameterSplit[0], code));

                } else {
                    operationSignature = path + "(" + query.replace(';', ':') + ")";
                }

            } else if ("POST".equals(method)) {
                operationSignature = path + "()";
            } else {
                chain.doFilter(request, response);
                return;
            }

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
