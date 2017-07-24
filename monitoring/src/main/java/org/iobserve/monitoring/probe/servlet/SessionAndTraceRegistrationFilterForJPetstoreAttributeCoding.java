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

import org.iobserve.analysis.filter.models.CallInformation;
import org.iobserve.common.record.ExtendedAfterOperationEvent;

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
public class SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding implements Filter, IMonitoringProbe {
    /** constant for a property name. */
    public static final String CONFIG_PROPERTY_NAME_LOG_FILTER_EXECUTION = "logFilterExecution";

    /** Kieker monitoring controller. */
    protected static final IMonitoringController CTRLINST = MonitoringController.getInstance();
    /** Kieker session registry. */
    protected static final SessionRegistry SESSION_REGISTRY = SessionRegistry.INSTANCE;

    /** Kieker time source. */
    protected static final ITimeSource TIMESOURCE = SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.CTRLINST
            .getTimeSource();
    /** Host name of the host the code is running on. */
    protected static final String VM_NAME = SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.CTRLINST
            .getHostname();
    /** Kieker trace registry. */
    private static final TraceRegistry TRACEREGISTRY = TraceRegistry.INSTANCE;

    /** Code Map **/
    private static Map<String, Integer> codes;

    /** Category Codes **/
    private static final int CATEGORY_FISH = 1000;
    private static final int CATEGORY_DOGS = 2000;
    private static final int CATEGORY_REPTILES = 3000;
    private static final int CATEGORY_CATS = 4000;
    private static final int CATEGORY_BIRDS = 5000;

    /** Product Codes **/
    private static final int PRODUCT_FI_SW_01 = 1000;
    private static final int ITEM_EST_1 = 1000;
    private static final int ITEM_EST_2 = 1001;
    private static final int PRODUCT_FI_SW_02 = 1010;
    private static final int ITEM_EST_3 = 1010;
    private static final int PRODUCT_FI_FW_01 = 1100;
    private static final int ITEM_EST_4 = 1100;
    private static final int ITEM_EST_5 = 1101;
    private static final int PRODUCT_FI_FW_02 = 1110;
    private static final int ITEM_EST_20 = 1110;
    private static final int ITEM_EST_21 = 1111;

    private static final int PRODUCT_K9_BD_01 = 2000;
    private static final int ITEM_EST_6 = 2000;
    private static final int ITEM_EST_7 = 2001;
    private static final int PRODUCT_K9_PO_02 = 2030;
    private static final int ITEM_EST_8 = 2030;
    private static final int PRODUCT_K9_DL_01 = 2060;
    private static final int ITEM_EST_9 = 2060;
    private static final int ITEM_EST_10 = 2061;
    private static final int PRODUCT_K9_RT_01 = 2090;
    private static final int ITEM_EST_28 = 2090;
    private static final int PRODUCT_K9_RT_02 = 2100;
    private static final int ITEM_EST_22 = 2100;
    private static final int ITEM_EST_23 = 2101;
    private static final int ITEM_EST_24 = 2102;
    private static final int ITEM_EST_25 = 2103;
    private static final int PRODUCT_K9_CW_01 = 2130;
    private static final int ITEM_EST_26 = 2130;
    private static final int ITEM_EST_27 = 2131;

    private static final int PRODUCT_RP_SN_01 = 3000;
    private static final int ITEM_EST_11 = 3000;
    private static final int ITEM_EST_12 = 3001;
    private static final int PRODUCT_RP_LI_02 = 3200;
    private static final int ITEM_EST_13 = 3200;

    private static final int PRODUCT_FL_DSH_01 = 4000;
    private static final int ITEM_EST_14 = 4000;
    private static final int ITEM_EST_15 = 4001;
    private static final int PRODUCT_FL_DLH_02 = 4010;
    private static final int ITEM_EST_16 = 4010;
    private static final int ITEM_EST_17 = 4011;

    private static final int PRODUCT_AV_CB_01 = 5000;
    private static final int ITEM_EST_18 = 5000;
    private static final int PRODUCT_AV_SB_02 = 5050;
    private static final int ITEM_EST_19 = 5050;

    /** boolean codes **/
    private static final int BOOL_TRUE = 5000;
    private static final int BOOL_FALSE = 0;

    // private static final Log LOG =
    // LogFactory.getLog(SessionAndTraceRegistrationFilterForJPetstore.class);

    /**
     * Create an SessionAndTraceRegistrationFilterForJPetstore and initialize the filter operation
     * signature.
     */
    public SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding() {
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes = new HashMap<>();
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("FISH",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.CATEGORY_FISH);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("CATS",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.CATEGORY_CATS);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("DOGS",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.CATEGORY_DOGS);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("REPTILES",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.CATEGORY_REPTILES);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("BIRDS",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.CATEGORY_BIRDS);

        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("FI-SW-01",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.PRODUCT_FI_SW_01);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("FI-SW-02",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.PRODUCT_FI_SW_02);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("FI-FW-01",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.PRODUCT_FI_FW_01);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("FI-FW-02",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.PRODUCT_FI_FW_02);

        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("K9-BD-01",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.PRODUCT_K9_BD_01);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("K9-PO-02",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.PRODUCT_K9_PO_02);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("K9-DL-01",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.PRODUCT_K9_DL_01);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("K9-RT-01",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.PRODUCT_K9_RT_01);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("K9-RT-02",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.PRODUCT_K9_RT_02);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("K9-CW-01",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.PRODUCT_K9_CW_01);

        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("RP-SN-01",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.PRODUCT_RP_SN_01);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("RP-LI-02",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.PRODUCT_RP_LI_02);

        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("FL-DSH-01",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.PRODUCT_FL_DSH_01);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("FL-DLH-02",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.PRODUCT_FL_DLH_02);

        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("AV-CB-01",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.PRODUCT_AV_CB_01);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("AV-SB-02",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.PRODUCT_AV_SB_02);

        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("EST-1",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.ITEM_EST_1);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("EST-2",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.ITEM_EST_2);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("EST-3",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.ITEM_EST_3);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("EST-4",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.ITEM_EST_4);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("EST-5",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.ITEM_EST_5);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("EST-6",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.ITEM_EST_6);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("EST-7",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.ITEM_EST_7);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("EST-8",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.ITEM_EST_8);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("EST-9",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.ITEM_EST_9);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("EST-10",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.ITEM_EST_10);

        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("EST-11",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.ITEM_EST_11);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("EST-12",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.ITEM_EST_12);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("EST-13",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.ITEM_EST_13);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("EST-14",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.ITEM_EST_14);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("EST-15",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.ITEM_EST_15);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("EST-16",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.ITEM_EST_16);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("EST-17",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.ITEM_EST_17);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("EST-18",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.ITEM_EST_18);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("EST-19",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.ITEM_EST_19);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("EST-20",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.ITEM_EST_20);

        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("EST-21",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.ITEM_EST_21);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("EST-22",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.ITEM_EST_22);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("EST-23",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.ITEM_EST_23);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("EST-24",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.ITEM_EST_24);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("EST-25",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.ITEM_EST_25);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("EST-26",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.ITEM_EST_26);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("EST-27",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.ITEM_EST_27);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("EST-28",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.ITEM_EST_28);

        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("true",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.BOOL_TRUE);
        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes.put("false",
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.BOOL_FALSE);

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
        if (SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.CTRLINST.isMonitoringEnabled()) {
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
                    queryParameters = query.split("&");
                }
            } else {
                method = "POST";
                path = request.getServletContext().getContextPath().replace('/', '.').substring(1);
                sessionId = "<no session>";
                query = "";
            }

            final String trimmedPath = path.replaceAll("\\.[A-Za-z0-9]*$", "");
            // final int lastComponentSignatureIndex = trimmedPath.lastIndexOf("actions.");

            componentSignature = "jpetstore.actions"; // lastComponentSignatureIndex < 0 ?
                                                      // trimmedPath: trimmedPath.substring(0,
                                                      // lastComponentSignatureIndex - 1);

            TraceMetadata trace = SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.TRACEREGISTRY.getTrace();
            final boolean newTrace = trace == null;

            if (newTrace) {
                SessionRegistry.INSTANCE.storeThreadLocalSessionId(sessionId);
                trace = SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.TRACEREGISTRY.registerTrace();
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.CTRLINST.newMonitoringRecord(trace);
            }

            if ("GET".equals(method)) {
                if ((queryParameters != null) && (queryParameters.length == 2)) {

                    operationSignature = trimmedPath.replaceAll("jpetstore\\.actions\\.", "") + "."
                            + queryParameters[0].replace("=", "") + "()"; // "("
                    // +
                    // queryParameters[0]
                    // +
                    // ")";

                    final String[] queryParameterSplit = queryParameters[1].split("=");

                    final Long code = SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes
                            .containsKey(queryParameterSplit[1])
                                    ? SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.codes
                                            .get(queryParameterSplit[1])
                                    : -100L;

                    callInformations.add(new CallInformation(queryParameterSplit[0], code));

                } else {
                    operationSignature = trimmedPath.replaceAll("jpetstore\\.actions\\.", "") + "("
                            + query.replace(';', ':') + ")";
                }

                operationSignature = operationSignature.replaceAll("\\.action\\(", "(");
                operationSignature = operationSignature.replaceAll("action\\.", "");

            } else if ("POST".equals(method)) {
                operationSignature = trimmedPath.replaceAll("jpetstore\\.actions\\.", "") + "()";
                operationSignature = operationSignature.replaceAll("\\.action\\(", "(");
                operationSignature = operationSignature.replaceAll("action\\.", "");
            } else {
                chain.doFilter(request, response);
                return;
            }

            final long traceId = trace.getTraceId();

            try {
                // mapps Object to String
                final ObjectMapper objectMapper = new ObjectMapper();

                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.CTRLINST
                        .newMonitoringRecord(new BeforeOperationEvent(
                                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.TIMESOURCE.getTime(),
                                traceId, trace.getNextOrderId(), operationSignature, componentSignature));

                chain.doFilter(request, response);

                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.CTRLINST
                        .newMonitoringRecord(
                                new ExtendedAfterOperationEvent(
                                        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.TIMESOURCE
                                                .getTime(),
                                        traceId, trace.getNextOrderId(), operationSignature, componentSignature,
                                        objectMapper.writeValueAsString(callInformations)));

            } catch (final Throwable th) { // NOPMD NOCS (catch throw is ok here)
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.CTRLINST
                        .newMonitoringRecord(
                                new AfterOperationFailedEvent(
                                        SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.TIMESOURCE
                                                .getTime(),
                                        traceId, trace.getNextOrderId(), operationSignature, componentSignature,
                                        th.toString()));
                throw new ServletException(th);
            } finally {
                // is this correct?
                SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.SESSION_REGISTRY
                        .unsetThreadLocalSessionId();
                // Reset the thread-local trace information
                if (newTrace) { // close the trace
                    SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.TRACEREGISTRY.unregisterTrace();
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
            SessionAndTraceRegistrationFilterForJPetstoreAttributeCoding.SESSION_REGISTRY
                    .storeThreadLocalSessionId(sessionId);
        }

        return sessionId;
    }
}
