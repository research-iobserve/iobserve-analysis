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
import java.util.Enumeration;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.iobserve.common.record.EntryLevelBeforeOperationEvent;

import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
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
 *   <filter-name>sessionAndTraceRegistrationPayloadFilter</filter-name>
 *   <filter-class>org.spp.iobserve.monitoring.probe.servlet.SessionAndTraceRegistrationPayloadFilter</filter-class>
 * </filter>
 * <filter-mapping>
 *   <filter-name>sessionAndTraceRegistrationPayloadFilter</filter-name>
 *   <url-pattern>/*</url-pattern>
 * </filter-mapping>
 * }
 * </pre>
 *
 * @author Reiner Jung
 * @since 0.0.2
 */
public class SessionAndTraceRegistrationPayloadFilter implements Filter, IMonitoringProbe {
    /** constant for a property name. */
    public static final String CONFIG_PROPERTY_NAME_LOG_FILTER_EXECUTION = "logFilterExecution";

    /** Kieker monitoring controller. */
    protected static final IMonitoringController CTRLINST = MonitoringController.getInstance();
    /** Kieker session registry. */
    protected static final SessionRegistry SESSION_REGISTRY = SessionRegistry.INSTANCE;

    /** Kieker time source. */
    protected static final ITimeSource TIMESOURCE = SessionAndTraceRegistrationPayloadFilter.CTRLINST
            .getTimeSource();
    /** Host name of the host the code is running on. */
    protected static final String VM_NAME = SessionAndTraceRegistrationPayloadFilter.CTRLINST
            .getHostname();
    /** Kieker trace registry. */
    private static final TraceRegistry TRACEREGISTRY = TraceRegistry.INSTANCE;
	
    // private static final Log LOG =
    // LogFactory.getLog(SessionAndTraceRegistrationFilterForJPetstore.class);

    /**
     * Create an SessionAndTraceRegistrationFilterForJPetstore and initialize the filter operation
     * signature.
     */
    public SessionAndTraceRegistrationPayloadFilter() {
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
        if (SessionAndTraceRegistrationPayloadFilter.CTRLINST.isMonitoringEnabled()) {
            // if (CTRLINST.isProbeActivated(this.filterOperationSignatureString)) {
            final String method;
            final String path;
            final String sessionId;
            
            if (request instanceof HttpServletRequest) {
                final HttpServletRequest httpRequest = (HttpServletRequest) request;
                method = httpRequest.getMethod();
                final String requestPath = httpRequest.getRequestURI().replace('/', '.').substring(1);

                // TODO is this a generic thing? */
                /** remove sessionId from request Path. */
                path = requestPath.contains(";") ? requestPath.substring(0, requestPath.indexOf(";")) : requestPath;

                sessionId = httpRequest.getSession().getId();
                
                final String trimmedPath = path.replaceAll("\\.[A-Za-z0-9]*$", "");
                
                final List<String> parameters = new ArrayList<>();
        		final List<String> parameterValues = new ArrayList<>();
        		
        		Enumeration<String> names = httpRequest.getParameterNames();
            	while (names.hasMoreElements()) {
            		String name = names.nextElement();
            		String[] values = httpRequest.getParameterValues(name);
            		for (String value : values) {
            			parameters.add(name);
            			parameterValues.add(value);
            		}
            	}

                String componentSignature = request.getServletContext().getContextPath();

                TraceMetadata trace = SessionAndTraceRegistrationPayloadFilter.TRACEREGISTRY.getTrace();
                final boolean newTrace = trace == null;

                if (newTrace) {
                    SessionRegistry.INSTANCE.storeThreadLocalSessionId(sessionId);
                    trace = SessionAndTraceRegistrationPayloadFilter.TRACEREGISTRY.registerTrace();
                    SessionAndTraceRegistrationPayloadFilter.CTRLINST.newMonitoringRecord(trace);
                }
                
                final long traceId = trace.getTraceId();

                if ("GET".equals(method)) {
                	String operationSignature = this.normalize(trimmedPath + "()");
                	
               	 	logMonitoringAndHandleFilterChain(chain, httpRequest, response, traceId, trace.getNextOrderId(), 
               	 			operationSignature, componentSignature,
                    		parameters.toArray(new String[parameters.size()]),
                    		parameterValues.toArray(new String[parameterValues.size()]), 1, newTrace);		
                } else if ("POST".equals(method)) {
                	String operationSignature = this.normalize(trimmedPath); 
                    
                    logMonitoringAndHandleFilterChain(chain, httpRequest, response, traceId, trace.getNextOrderId(),
                    		operationSignature, componentSignature,
                    		parameters.toArray(new String[parameters.size()]),
                    		parameterValues.toArray(new String[parameterValues.size()]), 1, newTrace);
                } else {
                    chain.doFilter(request, response);
                    return;
                }
            } else {
            	/** not a http request. */
            	chain.doFilter(request, response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    public String normalize(String operationSignature) {
		return operationSignature.replaceAll("\\.action\\(", "(").replaceAll("action\\.", "");
	}
	
	private void logMonitoringAndHandleFilterChain(final FilterChain chain, 
			final ServletRequest request, final ServletResponse response, final long traceId, final int orderIndex,
			String operationSignature, String componentSignature, String[] parameters, String[] parameterValues, int type, boolean newTrace) throws ServletException {
		try {                		
			SessionAndTraceRegistrationPayloadFilter.CTRLINST
                    .newMonitoringRecord(new EntryLevelBeforeOperationEvent(
                            SessionAndTraceRegistrationPayloadFilter.TIMESOURCE.getTime(),
                            traceId, orderIndex, operationSignature, componentSignature,
                            parameters, 
                            parameterValues, type));

            chain.doFilter(request, response);

            SessionAndTraceRegistrationPayloadFilter.CTRLINST
                    .newMonitoringRecord(
                            new AfterOperationEvent(
                                    SessionAndTraceRegistrationPayloadFilter.TIMESOURCE
                                            .getTime(),
                                    traceId, orderIndex, operationSignature, componentSignature));

        } catch (final Throwable th) { // NOPMD NOCS (catch throw is ok here)
            SessionAndTraceRegistrationPayloadFilter.CTRLINST
                    .newMonitoringRecord(
                            new AfterOperationFailedEvent(
                                    SessionAndTraceRegistrationPayloadFilter.TIMESOURCE
                                            .getTime(),
                                    traceId, orderIndex, operationSignature, componentSignature,
                                    th.toString()));
            throw new ServletException(th);
        } finally {
            // is this correct?
            SessionAndTraceRegistrationPayloadFilter.SESSION_REGISTRY
                    .unsetThreadLocalSessionId();
            // Reset the thread-local trace information
            if (newTrace) { // close the trace
                SessionAndTraceRegistrationPayloadFilter.TRACEREGISTRY.unregisterTrace();
            }

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
            SessionAndTraceRegistrationPayloadFilter.SESSION_REGISTRY
                    .storeThreadLocalSessionId(sessionId);
        }

        return sessionId;
    }
}
