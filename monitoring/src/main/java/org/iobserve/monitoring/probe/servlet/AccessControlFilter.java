/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
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
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.probe.IMonitoringProbe;

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
 *   <filter-name>accessControlFilter</filter-name>
 *   <filter-class>org.spp.iobserve.monitoring.probe.servlet.AccessControlFilter</filter-class>
 * </filter>
 * <filter-mapping>
 *   <filter-name>accessControlFilter</filter-name>
 *   <url-pattern>/*</url-pattern>
 * </filter-mapping>
 * }
 * </pre>
 *
 * @author Reiner Jung
 * @since 0.0.2
 */
public class AccessControlFilter implements Filter, IMonitoringProbe {
    /** constant for a property name. */
    public static final String CONFIG_PROPERTY_NAME_LOG_FILTER_EXECUTION = "logFilterExecution";

    /** Kieker monitoring controller. */
    protected static final IMonitoringController CTRLINST = MonitoringController.getInstance();

    /** Host name of the host the code is running on. */
    protected static final String VM_NAME = SessionAndTraceRegistrationPayloadFilter.CTRLINST.getHostname();

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        // no parameters required
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(request, response);

        if (AccessControlFilter.CTRLINST.isMonitoringEnabled()) {
            if (request instanceof HttpServletRequest) {
                final String remoteAddr = request.getRemoteAddr();
                if (this.isInList(EListType.WHITELIST, remoteAddr,
                        this.computeOperationSignature((HttpServletRequest) request))) {
                    chain.doFilter(request, response);
                } else if (this.isInList(EListType.BLACKLIST, remoteAddr,
                        this.computeOperationSignature((HttpServletRequest) request))) {
                    ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_FORBIDDEN);
                    chain.doFilter(request, response);
                } else {
                    try {
                        Thread.sleep(200);
                        if (this.isInList(EListType.WHITELIST, remoteAddr,
                                this.computeOperationSignature((HttpServletRequest) request))) {
                            chain.doFilter(request, response);
                        } else {
                            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_FORBIDDEN);
                            chain.doFilter(request, response);
                        }
                    } catch (final InterruptedException e) {
                        ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        chain.doFilter(request, response);
                    }
                }
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    /**
     * Create the operation signature based on the HTTP Servlet request URI and parameters.
     *
     * @param request
     *            servlet request
     * @return retuns operation signature
     */
    private String computeOperationSignature(final HttpServletRequest request) {
        String parameters = null;
        final Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            if (parameters == null) {
                parameters = names.nextElement();
            } else {
                parameters += ", " + names.nextElement();
            }
        }

        return String.format("%s (%s)", request.getRequestURI(), parameters);
    }

    /**
     * Check whether an remote address is contained in a list associated with an operation
     * signature. The list is (in iObserve) either a whitelist or a blacklist. The whitelist
     * contains all IP addresses which are allowed to request an operation and the blacklist
     * contains all IPs which are not allowed to request the service.
     *
     * @param listType
     *            name of the list (black or whitelist)
     * @param remoteAddr
     * @param operationSignature
     * @return
     */
    private boolean isInList(final EListType listType, final String remoteAddr, final String operationSignature) {
        final Map<String, List<String>> parameters = AccessControlFilter.CTRLINST.getAllParameters(operationSignature);
        if (parameters != null) {
            final List<String> valueList = parameters.get(listType.name());
            if (valueList != null) {
                return valueList.contains(remoteAddr);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void destroy() {
        // by default, we do nothing here. Extending classes may override this method
    }

}
