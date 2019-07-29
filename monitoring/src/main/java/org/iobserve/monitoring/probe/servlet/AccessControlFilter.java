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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessControlFilter.class);

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        // no parameters required
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        AccessControlFilter.LOGGER.debug("filter access control {}", request.getRemoteHost());
        if (AccessControlFilter.CTRLINST.isMonitoringEnabled()) {
            AccessControlFilter.LOGGER.debug("Monitoring enabled");
            if (request instanceof HttpServletRequest) {
                final String remoteAddr = request.getRemoteAddr();
                final String operationSignature = this.computeOperationSignature((HttpServletRequest) request);
                AccessControlFilter.LOGGER.debug("HTTP request {} {}", remoteAddr, operationSignature);
                if (this.isInList(EListType.WHITELIST, remoteAddr, operationSignature)) {
                    chain.doFilter(request, response);
                } else if (this.isInList(EListType.BLACKLIST, remoteAddr, operationSignature)) {
                    ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_FORBIDDEN);
                    chain.doFilter(request, response);
                } else {
                    try {
                        Thread.sleep(200);
                        if (this.isInList(EListType.WHITELIST, remoteAddr, operationSignature)) {
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
     * @return returns the request uri as operation signature
     */
    private String computeOperationSignature(final HttpServletRequest request) {
        final String parameters;
        if ("GET".equals(request.getMethod())) {
            parameters = this.createParameters(request);
        } else {
            String contentType = request.getContentType();
            final int semicolon = contentType.indexOf(";");
            if (semicolon > 0) {
                contentType = contentType.substring(0, semicolon).trim();
            } else {
                contentType = contentType.trim();
            }
            if ("application/json".equals(contentType)) {
                parameters = "Object";
            } else if ("application/x-www-form-urlencoded".equals(contentType)) {
                parameters = this.createParameters(request);
            } else {
                parameters = "<unknown mime type>";
            }
        }

        return String.format("%s (%s)", request.getRequestURI(), parameters);
    }

    private String createParameters(final HttpServletRequest request) {
        String parameters = "";
        final Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            if ("".equals(parameters)) {
                parameters = names.nextElement();
            } else {
                parameters += ", " + names.nextElement();
            }
        }

        return parameters;
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
        AccessControlFilter.LOGGER.debug("isInList {} {}", listType.name(), remoteAddr);
        final Map<String, List<String>> parameters = AccessControlFilter.CTRLINST
                .getAllPatternParameters(operationSignature);
        if (parameters != null) {
            AccessControlFilter.LOGGER.debug("Has parameters for operation {}", operationSignature);
            final List<String> valueList = parameters.get(listType.name());
            if (valueList != null) {
                AccessControlFilter.LOGGER.debug("Has parameters values for list {}", valueList.size());
                return valueList.contains(remoteAddr);
            } else {
                AccessControlFilter.LOGGER.debug("No parameters for list.");
                return false;
            }
        } else {
            AccessControlFilter.LOGGER.debug("No parameters for operation {} found", operationSignature);
            return false;
        }
    }

    @Override
    public void destroy() {
        // by default, we do nothing here. Extending classes may override this method
    }

}
