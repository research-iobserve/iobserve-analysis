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
package org.iobserve.planning.environment.uriconverter;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;

/**
 * Intercepts URIs to be converted by the Eclipse {@link URIConverter}.
 *
 * @author Fabian Keller
 */
public class URIConverterHandler extends ExtensibleURIConverterImpl {

    private static final Logger LOG = LogManager.getLogger(URIConverterHandler.class.getName());

    private final URIConverter delegate;

    private final List<IURIConverterInterceptor> interceptors = new ArrayList<>();

    /**
     * Provide a delegate {@link URIConverter} to handle conversions not handled by the registered
     * interceptors.
     */
    public URIConverterHandler(final URIConverter delegate) {
        this.delegate = delegate;
    }

    /**
     * Adds an interceptor to this handlers.
     * <p>
     * Note: Interceptors will be called in the order they are added to the handler. The first
     * interceptor that is able to convert the URI will convert the URI.
     */
    public URIConverterHandler addInterceptor(final IURIConverterInterceptor interceptor) {
        this.interceptors.add(interceptor);
        return this;
    }

    @Override
    public URI normalize(final URI uri) {
        final URI normalized = this.doNormalize(uri);
        URIConverterHandler.LOG.info(String.format("Normalize uri '%s' to '%s'", uri, normalized));
        if (!new java.io.File(normalized.toString()).exists()) {
            URIConverterHandler.LOG.warn("Normalized URI is not a file: " + normalized);
        }
        return normalized;
    }

    private URI doNormalize(final URI uri) {
        for (final IURIConverterInterceptor interceptor : this.interceptors) {
            if (interceptor.canConvert(uri)) {
                return interceptor.convert(uri);
            }
        }
        return this.delegate.normalize(uri);
    }
}
