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
        if (AccessControlFilter.CTRLINST.isMonitoringEnabled()) {
            if (request instanceof HttpServletRequest) {
                final String remoteAddr = request.getRemoteAddr();
                if (this.isInWhiteList(remoteAddr, this.computeLocationId((HttpServletRequest) request))) {
                    chain.doFilter(request, response);
                } else {
                    ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_FORBIDDEN);
                    chain.doFilter(request, response);
                }
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private String computeLocationId(final HttpServletRequest request) {
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

    private boolean isInWhiteList(final String remoteAddr, final String locationId) {
        final Map<String, List<String>> parameters = AccessControlFilter.CTRLINST.getParameters(locationId);
        if (parameters != null) {
            final List<String> whitelist = parameters.get("whitelist");
            if (whitelist != null) {
                return whitelist.contains(remoteAddr);
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
