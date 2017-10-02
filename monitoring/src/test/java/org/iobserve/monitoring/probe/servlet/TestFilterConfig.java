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

import java.util.Enumeration;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

/**
 * @author reiner
 *
 */
public class TestFilterConfig implements FilterConfig {

    private final ServletContext context;

    public TestFilterConfig(final ServletContext context) {
        this.context = context;
    }

    @Override
    public String getFilterName() {
        return "sessionAndTraceRegistrationFilter";
    }

    @Override
    public ServletContext getServletContext() {
        return this.context;
    }

    @Override
    public String getInitParameter(final String name) {
        return "example parameter";
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return null;
    }
}
