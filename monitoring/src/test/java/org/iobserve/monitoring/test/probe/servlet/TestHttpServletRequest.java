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
package org.iobserve.monitoring.test.probe.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

/**
 * Dummy servlet request used to test servlet filter.
 *
 * @author Reiner Jung
 *
 */
public class TestHttpServletRequest implements HttpServletRequest {

    public static final String REQUEST_URI = "http://test/context/path/example";

    private final Map<String, String> parameters = new HashMap<>();
    private final ServletContext servletContext;
    private final HttpSession httpSession;

    /**
     * Create a http servlet request.
     *
     * @param servletContext
     *            the servlet context
     * @param httpSession
     *            the related hattp session
     */
    public TestHttpServletRequest(final ServletContext servletContext, final HttpSession httpSession) {
        for (int i = 0; i < 10; i++) {
            this.parameters.put("parameter" + i, "value" + i);
        }
        this.servletContext = servletContext;
        this.httpSession = httpSession;
    }

    public Map<String, String> getParameters() {
        return this.parameters;
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
        // test mock
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
        return new Vector<>(this.parameters.keySet()).elements(); // NOPMD vector is used to create
                                                                  // enumeration
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
        return "1.2.3.4"; // NOPMD this is just mocking code
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
        return "127.1.1.1"; // NOPMD this is just mocking code
    }

    @Override
    public int getLocalPort() {
        return 8080;
    }

    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
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
        return new Cookie[0];
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
        return TestHttpServletRequest.REQUEST_URI;
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
        return this.httpSession;
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
        // test mock
    }

    @Override
    public void logout() throws ServletException {
        // test mock
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
    public <T extends HttpUpgradeHandler> T upgrade(final Class<T> handlerClass) throws IOException, ServletException {
        return null;
    }

}
