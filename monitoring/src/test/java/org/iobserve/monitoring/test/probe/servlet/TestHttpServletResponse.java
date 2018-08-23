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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;

/**
 * Dummy servlet response used to test servlet filter.
 *
 * @author Reiner Jung
 *
 */
public class TestHttpServletResponse implements ServletResponse {

    /**
     * Create a dummy response object.
     */
    public TestHttpServletResponse() {
        // test mock
    }

    @Override
    public void setLocale(final Locale loc) {
        // test mock
    }

    @Override
    public void setContentType(final String type) {
        // test mock
    }

    @Override
    public void setContentLengthLong(final long len) {
        // test mock
    }

    @Override
    public void setContentLength(final int len) {
        // test mock
    }

    @Override
    public void setCharacterEncoding(final String charset) {
        // test mock
    }

    @Override
    public void setBufferSize(final int size) {
        // test mock
    }

    @Override
    public void resetBuffer() {
        // test mock
    }

    @Override
    public void reset() {
        // test mock
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
        // test mock
    }

}
