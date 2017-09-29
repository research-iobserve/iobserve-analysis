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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.FilterRegistration;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

import kieker.monitoring.writer.filesystem.AsciiFileWriter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Reiner Jung
 *
 */
@SuppressWarnings("deprecation")
public class TestSessionAndTraceRegistrationPayloadFilter {

    private static final String WRITER_NAME = AsciiFileWriter.class.getCanonicalName();

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {

    }

    /**
     * Test method for
     * {@link org.iobserve.monitoring.probe.servlet.SessionAndTraceRegistrationPayloadFilter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}.
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
                return TestSessionAndTraceRegistrationPayloadFilter.this.createServletContext();
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
            Assert.assertEquals("Test value", filter, filter);
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
        return new ServletResponse() {

            @Override
            public void setLocale(final Locale loc) {
            }

            @Override
            public void setContentType(final String type) {
            }

            @Override
            public void setContentLengthLong(final long len) {
            }

            @Override
            public void setContentLength(final int len) {
            }

            @Override
            public void setCharacterEncoding(final String charset) {
            }

            @Override
            public void setBufferSize(final int size) {
            }

            @Override
            public void resetBuffer() {
            }

            @Override
            public void reset() {
            }

            @Override
            public boolean isCommitted() {
                return false;
            }

            @Override
            public PrintWriter getWriter() throws IOException {
                return null;
            }

            @Override
            public ServletOutputStream getOutputStream() throws IOException {
                return null;
            }

            @Override
            public Locale getLocale() {
                return null;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public String getCharacterEncoding() {
                return null;
            }

            @Override
            public int getBufferSize() {
                return 0;
            }

            @Override
            public void flushBuffer() throws IOException {
            }
        };
    }

    private ServletRequest createRequest() {
        return new MyHttpServletRequest();
    }

    private ServletContext createServletContext() {
        return new ServletContext() {

            @Override
            public String getContextPath() {
                return "/main/context/path";
            }

            @Override
            public ServletContext getContext(final String uripath) {
                return null;
            }

            @Override
            public int getMajorVersion() {
                return 0;
            }

            @Override
            public int getMinorVersion() {
                return 0;
            }

            @Override
            public int getEffectiveMajorVersion() {
                return 0;
            }

            @Override
            public int getEffectiveMinorVersion() {
                return 0;
            }

            @Override
            public String getMimeType(final String file) {
                return "text/plain";
            }

            @Override
            public Set<String> getResourcePaths(final String path) {
                return null;
            }

            @Override
            public URL getResource(final String path) throws MalformedURLException {
                return null;
            }

            @Override
            public InputStream getResourceAsStream(final String path) {
                return null;
            }

            @Override
            public RequestDispatcher getRequestDispatcher(final String path) {
                return null;
            }

            @Override
            public RequestDispatcher getNamedDispatcher(final String name) {
                return null;
            }

            @Override
            public Servlet getServlet(final String name) throws ServletException {
                return null;
            }

            @Override
            public Enumeration<Servlet> getServlets() {
                return null;
            }

            @Override
            public Enumeration<String> getServletNames() {
                return null;
            }

            @Override
            public void log(final String msg) {
            }

            @Override
            public void log(final Exception exception, final String msg) {
            }

            @Override
            public void log(final String message, final Throwable throwable) {
            }

            @Override
            public String getRealPath(final String path) {
                return null;
            }

            @Override
            public String getServerInfo() {
                return null;
            }

            @Override
            public String getInitParameter(final String name) {
                return null;
            }

            @Override
            public Enumeration<String> getInitParameterNames() {
                return null;
            }

            @Override
            public boolean setInitParameter(final String name, final String value) {
                return false;
            }

            @Override
            public Object getAttribute(final String name) {
                return null;
            }

            @Override
            public Enumeration<String> getAttributeNames() {
                return null;
            }

            @Override
            public void setAttribute(final String name, final Object object) {
            }

            @Override
            public void removeAttribute(final String name) {

            }

            @Override
            public String getServletContextName() {
                return null;
            }

            @Override
            public Dynamic addServlet(final String servletName, final String className) {
                return null;
            }

            @Override
            public Dynamic addServlet(final String servletName, final Servlet servlet) {
                return null;
            }

            @Override
            public Dynamic addServlet(final String servletName, final Class<? extends Servlet> servletClass) {
                return null;
            }

            @Override
            public <T extends Servlet> T createServlet(final Class<T> clazz) throws ServletException {
                return null;
            }

            @Override
            public ServletRegistration getServletRegistration(final String servletName) {
                return null;
            }

            @Override
            public Map<String, ? extends ServletRegistration> getServletRegistrations() {
                return null;
            }

            @Override
            public javax.servlet.FilterRegistration.Dynamic addFilter(final String filterName, final String className) {
                return null;
            }

            @Override
            public javax.servlet.FilterRegistration.Dynamic addFilter(final String filterName, final Filter filter) {
                return null;
            }

            @Override
            public javax.servlet.FilterRegistration.Dynamic addFilter(final String filterName,
                    final Class<? extends Filter> filterClass) {
                return null;
            }

            @Override
            public <T extends Filter> T createFilter(final Class<T> clazz) throws ServletException {
                return null;
            }

            @Override
            public FilterRegistration getFilterRegistration(final String filterName) {
                return null;
            }

            @Override
            public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
                return null;
            }

            @Override
            public SessionCookieConfig getSessionCookieConfig() {
                return null;
            }

            @Override
            public void setSessionTrackingModes(final Set<SessionTrackingMode> sessionTrackingModes) {
            }

            @Override
            public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
                return null;
            }

            @Override
            public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
                return null;
            }

            @Override
            public void addListener(final String className) {
            }

            @Override
            public <T extends EventListener> void addListener(final T t) {
            }

            @Override
            public void addListener(final Class<? extends EventListener> listenerClass) {
            }

            @Override
            public <T extends EventListener> T createListener(final Class<T> clazz) throws ServletException {
                return null;
            }

            @Override
            public JspConfigDescriptor getJspConfigDescriptor() {
                return null;
            }

            @Override
            public ClassLoader getClassLoader() {

                return null;
            }

            @Override
            public void declareRoles(final String... roleNames) {

            }

            @Override
            public String getVirtualServerName() {

                return null;
            }

        };
    }

    class MyHttpServletRequest implements HttpServletRequest {

        Map<String, String> parameters = new HashMap<>();

        public MyHttpServletRequest() {
            for (int i = 0; i < 10; i++) {
                this.parameters.put("parameter" + i, "value" + i);
            }
        }

        @Override
        public Object getAttribute(final String name) {
            return null;
        }

        @Override
        public Enumeration<String> getAttributeNames() {
            return null;
        }

        @Override
        public String getCharacterEncoding() {
            return "UTF-8";
        }

        @Override
        public void setCharacterEncoding(final String env) throws UnsupportedEncodingException {

        }

        @Override
        public int getContentLength() {
            return 0;
        }

        @Override
        public long getContentLengthLong() {
            return 0;
        }

        @Override
        public String getContentType() {
            return "text/plain";
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            return null;
        }

        @Override
        public String getParameter(final String name) {
            return null;
        }

        @Override
        public Enumeration<String> getParameterNames() {
            return new Vector<>(this.parameters.keySet()).elements();
        }

        @Override
        public String[] getParameterValues(final String name) {
            if (this.parameters.get(name) != null) {
                return new String[] { this.parameters.get(name) };
            } else {
                return new String[] {};
            }
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            return null;
        }

        @Override
        public String getProtocol() {
            return "http";
        }

        @Override
        public String getScheme() {
            return "http";
        }

        @Override
        public String getServerName() {
            return "test";
        }

        @Override
        public int getServerPort() {
            return 8080;
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return null;
        }

        @Override
        public String getRemoteAddr() {
            return "1.2.3.4";
        }

        @Override
        public String getRemoteHost() {
            return "remote set";
        }

        @Override
        public void setAttribute(final String name, final Object o) {
            // is ignored
        }

        @Override
        public void removeAttribute(final String name) {
            // is ignored
        }

        @Override
        public Locale getLocale() {
            return null;
        }

        @Override
        public Enumeration<Locale> getLocales() {
            return null;
        }

        @Override
        public boolean isSecure() {
            return false;
        }

        @Override
        public RequestDispatcher getRequestDispatcher(final String path) {
            return null;
        }

        @Override
        public String getRealPath(final String path) {
            return "/webtest/example";
        }

        @Override
        public int getRemotePort() {
            return 8080;
        }

        @Override
        public String getLocalName() {
            return "localname";
        }

        @Override
        public String getLocalAddr() {
            return "127.1.1.1";
        }

        @Override
        public int getLocalPort() {
            return 8080;
        }

        @Override
        public ServletContext getServletContext() {
            return TestSessionAndTraceRegistrationPayloadFilter.this.createServletContext();
        }

        @Override
        public AsyncContext startAsync() throws IllegalStateException {
            return null;
        }

        @Override
        public AsyncContext startAsync(final ServletRequest servletRequest, final ServletResponse servletResponse)
                throws IllegalStateException {
            return null;
        }

        @Override
        public boolean isAsyncStarted() {
            return false;
        }

        @Override
        public boolean isAsyncSupported() {
            return false;
        }

        @Override
        public AsyncContext getAsyncContext() {
            return null;
        }

        @Override
        public DispatcherType getDispatcherType() {
            return null;
        }

        @Override
        public String getAuthType() {
            return null;
        }

        @Override
        public Cookie[] getCookies() {

            return null;
        }

        @Override
        public long getDateHeader(final String name) {

            return 0;
        }

        @Override
        public String getHeader(final String name) {

            return null;
        }

        @Override
        public Enumeration<String> getHeaders(final String name) {

            return null;
        }

        @Override
        public Enumeration<String> getHeaderNames() {

            return null;
        }

        @Override
        public int getIntHeader(final String name) {
            return 0;
        }

        @Override
        public String getMethod() {
            return "GET";
        }

        @Override
        public String getPathInfo() {
            return null;
        }

        @Override
        public String getPathTranslated() {
            return null;
        }

        @Override
        public String getContextPath() {
            return null;
        }

        @Override
        public String getQueryString() {
            return null;
        }

        @Override
        public String getRemoteUser() {
            return null;
        }

        @Override
        public boolean isUserInRole(final String role) {
            return false;
        }

        @Override
        public Principal getUserPrincipal() {
            return null;
        }

        @Override
        public String getRequestedSessionId() {
            return null;
        }

        @Override
        public String getRequestURI() {
            return "http://test/context/path/example";
        }

        @Override
        public StringBuffer getRequestURL() {
            return null;
        }

        @Override
        public String getServletPath() {
            return null;
        }

        @Override
        public HttpSession getSession(final boolean create) {
            return null;
        }

        @Override
        public HttpSession getSession() {
            return new HttpSession() {

                @Override
                public long getCreationTime() {
                    return 0;
                }

                @Override
                public String getId() {
                    return "1234567890";
                }

                @Override
                public long getLastAccessedTime() {
                    return 0;
                }

                @Override
                public ServletContext getServletContext() {
                    return TestSessionAndTraceRegistrationPayloadFilter.this.createServletContext();
                }

                @Override
                public void setMaxInactiveInterval(final int interval) {
                }

                @Override
                public int getMaxInactiveInterval() {
                    return 0;
                }

                @SuppressWarnings("deprecation")
                @Override
                public HttpSessionContext getSessionContext() {
                    return null;
                }

                @Override
                public Object getAttribute(final String name) {
                    return null;
                }

                @Override
                public Object getValue(final String name) {
                    return null;
                }

                @Override
                public Enumeration<String> getAttributeNames() {
                    return null;
                }

                @Override
                public String[] getValueNames() {
                    return null;
                }

                @Override
                public void setAttribute(final String name, final Object value) {
                }

                @Override
                public void putValue(final String name, final Object value) {
                }

                @Override
                public void removeAttribute(final String name) {
                }

                @Override
                public void removeValue(final String name) {
                }

                @Override
                public void invalidate() {
                }

                @Override
                public boolean isNew() {
                    return false;
                }

            };
        }

        @Override
        public String changeSessionId() {
            return null;
        }

        @Override
        public boolean isRequestedSessionIdValid() {
            return false;
        }

        @Override
        public boolean isRequestedSessionIdFromCookie() {
            return false;
        }

        @Override
        public boolean isRequestedSessionIdFromURL() {
            return false;
        }

        @Override
        public boolean isRequestedSessionIdFromUrl() {
            return false;
        }

        @Override
        public boolean authenticate(final HttpServletResponse response) throws IOException, ServletException {
            return false;
        }

        @Override
        public void login(final String username, final String password) throws ServletException {
        }

        @Override
        public void logout() throws ServletException {
        }

        @Override
        public Collection<Part> getParts() throws IOException, ServletException {
            return null;
        }

        @Override
        public Part getPart(final String name) throws IOException, ServletException {
            return null;
        }

        @Override
        public <T extends HttpUpgradeHandler> T upgrade(final Class<T> handlerClass)
                throws IOException, ServletException {
            return null;
        }

    }
}
