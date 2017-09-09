/**
 * 
 */
package org.iobserve.monitoring.probe.servlet;

import static org.junit.Assert.*;

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

import org.junit.Before;
import org.junit.Test;

/**
 * @author reiner
 *
 */
public class TestSessionAndTraceRegistrationPayloadFilter {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link org.iobserve.monitoring.probe.servlet.SessionAndTraceRegistrationPayloadFilter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}.
	 */
	@Test
	public void testDoFilter() {
		SessionAndTraceRegistrationPayloadFilter filter = new SessionAndTraceRegistrationPayloadFilter();
		FilterConfig filterConfig = new FilterConfig() {

			@Override
			public String getFilterName() {
				return "sessionAndTraceRegistrationFilter";
			}

			@Override
			public ServletContext getServletContext() {
				return createServletContext();
			}

			@Override
			public String getInitParameter(String name) {
				return "example parameter";
			}

			@Override
			public Enumeration<String> getInitParameterNames() {
				return null;
			}
			
		};
				
		try {
			filter.init(filterConfig);
			filter.doFilter(createRequest(), createResponse(), createChain());
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private FilterChain createChain() {
		return new FilterChain() {
			
			@Override
			public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
				// Nothing to be done in the test.				
			}
		};
	}

	private ServletResponse createResponse() {
		return new ServletResponse() {
			
			@Override
			public void setLocale(Locale loc) {
								
			}
			
			@Override
			public void setContentType(String type) {
								
			}
			
			@Override
			public void setContentLengthLong(long len) {
								
			}
			
			@Override
			public void setContentLength(int len) {
			
			}
			
			@Override
			public void setCharacterEncoding(String charset) {
				
			}
			
			@Override
			public void setBufferSize(int size) {
				
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
			public ServletContext getContext(String uripath) {
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
			public String getMimeType(String file) {
				return "text/plain";
			}

			@Override
			public Set<String> getResourcePaths(String path) {
				return null;
			}

			@Override
			public URL getResource(String path) throws MalformedURLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public InputStream getResourceAsStream(String path) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public RequestDispatcher getRequestDispatcher(String path) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public RequestDispatcher getNamedDispatcher(String name) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Servlet getServlet(String name) throws ServletException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Enumeration<Servlet> getServlets() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Enumeration<String> getServletNames() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void log(String msg) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void log(Exception exception, String msg) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void log(String message, Throwable throwable) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public String getRealPath(String path) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getServerInfo() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getInitParameter(String name) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Enumeration<String> getInitParameterNames() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean setInitParameter(String name, String value) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public Object getAttribute(String name) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Enumeration<String> getAttributeNames() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setAttribute(String name, Object object) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void removeAttribute(String name) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public String getServletContextName() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Dynamic addServlet(String servletName, String className) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Dynamic addServlet(String servletName, Servlet servlet) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <T extends Servlet> T createServlet(Class<T> clazz) throws ServletException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ServletRegistration getServletRegistration(String servletName) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Map<String, ? extends ServletRegistration> getServletRegistrations() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public javax.servlet.FilterRegistration.Dynamic addFilter(String filterName, String className) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public javax.servlet.FilterRegistration.Dynamic addFilter(String filterName, Filter filter) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public javax.servlet.FilterRegistration.Dynamic addFilter(String filterName,
					Class<? extends Filter> filterClass) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <T extends Filter> T createFilter(Class<T> clazz) throws ServletException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public FilterRegistration getFilterRegistration(String filterName) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public SessionCookieConfig getSessionCookieConfig() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void addListener(String className) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public <T extends EventListener> void addListener(T t) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void addListener(Class<? extends EventListener> listenerClass) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public <T extends EventListener> T createListener(Class<T> clazz) throws ServletException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public JspConfigDescriptor getJspConfigDescriptor() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ClassLoader getClassLoader() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void declareRoles(String... roleNames) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public String getVirtualServerName() {
				// TODO Auto-generated method stub
				return null;
			}
			
		};
	}

	class MyHttpServletRequest implements HttpServletRequest {
		
		Map<String,String> parameters = new HashMap<>();
				
		public MyHttpServletRequest() {
			for (int i=0;i<10;i++) {
				parameters.put("parameter" + i, "value" + i);
			}
		}
				
				

		@Override
		public Object getAttribute(String name) {
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
		public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
			
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
		public String getParameter(String name) {
			return null;
		}

		@Override
		public Enumeration<String> getParameterNames() {
			return new Vector<String>(parameters.keySet()).elements();
		}

		@Override
		public String[] getParameterValues(String name) {
			if (parameters.get(name) != null)
				return new String[] { parameters.get(name) };
			else
				return new String[] {};
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
		public void setAttribute(String name, Object o) {
			// is ignored					
		}

		@Override
		public void removeAttribute(String name) {
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
		public RequestDispatcher getRequestDispatcher(String path) {
			return null;
		}

		@Override
		public String getRealPath(String path) {
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
			return createServletContext();
		}

		@Override
		public AsyncContext startAsync() throws IllegalStateException {
			return null;
		}

		@Override
		public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse)
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
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getAuthType() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Cookie[] getCookies() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getDateHeader(String name) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public String getHeader(String name) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Enumeration<String> getHeaders(String name) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Enumeration<String> getHeaderNames() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getIntHeader(String name) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public String getMethod() {
			return "GET";
		}

		@Override
		public String getPathInfo() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getPathTranslated() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getContextPath() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getQueryString() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getRemoteUser() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isUserInRole(String role) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Principal getUserPrincipal() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getRequestedSessionId() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getRequestURI() {
			return "http://test/context/path/example";
		}

		@Override
		public StringBuffer getRequestURL() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getServletPath() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public HttpSession getSession(boolean create) {
			// TODO Auto-generated method stub
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
					return createServletContext();
				}

				@Override
				public void setMaxInactiveInterval(int interval) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public int getMaxInactiveInterval() {
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public HttpSessionContext getSessionContext() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public Object getAttribute(String name) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public Object getValue(String name) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public Enumeration<String> getAttributeNames() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public String[] getValueNames() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public void setAttribute(String name, Object value) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void putValue(String name, Object value) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void removeAttribute(String name) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void removeValue(String name) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void invalidate() {
					// TODO Auto-generated method stub
					
				}

				@Override
				public boolean isNew() {
					// TODO Auto-generated method stub
					return false;
				}
				
			};
		}

		@Override
		public String changeSessionId() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isRequestedSessionIdValid() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isRequestedSessionIdFromCookie() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isRequestedSessionIdFromURL() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isRequestedSessionIdFromUrl() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void login(String username, String password) throws ServletException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void logout() throws ServletException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Collection<Part> getParts() throws IOException, ServletException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Part getPart(String name) throws IOException, ServletException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass)
				throws IOException, ServletException {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
