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

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Intercepts URIs to be converted by the Eclipse {@link URIConverter}.
 *
 * @author Fabian Keller
 */
public class URIConverterHandler extends ExtensibleURIConverterImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(URIConverterHandler.class.getName());

    private final URIConverter uriConverter;

    private final List<IURIConverterInterceptor> interceptors = new ArrayList<>();

    /**
     * Provide a delegate {@link URIConverter} to handle conversions not handled by the registered
     * interceptors.
     *
     * @param uriConverter
     *            URI converter
     */
    public URIConverterHandler(final URIConverter uriConverter) {
        this.uriConverter = uriConverter;
    }

    /**
     * Adds an interceptor to this handlers.
     * <p>
     * Note: Interceptors will be called in the order they are added to the handler. The first
     * interceptor that is able to convert the URI will convert the URI.
     *
     * @param interceptor
     *            the interceptor to be added
     * @return this URIConverterHandler
     */
    public URIConverterHandler addInterceptor(final IURIConverterInterceptor interceptor) {
        this.interceptors.add(interceptor);
        return this;
    }

    @Override
    public URI normalize(final URI uri) {
        final URI normalized = this.doNormalize(uri);

        if (URIConverterHandler.LOGGER.isInfoEnabled()) {
            URIConverterHandler.LOGGER.info(String.format("Normalize uri '%s' to '%s'", uri, normalized));
        }
        if (!new java.io.File(normalized.toString()).exists()) {
            if (URIConverterHandler.LOGGER.isWarnEnabled()) { // NOPMD nesting is relevant for
                                                              // understanding the code
                URIConverterHandler.LOGGER.warn("Normalized URI is not a file: " + normalized);
            }
        }
        return normalized;
    }

    private URI doNormalize(final URI uri) {
        for (final IURIConverterInterceptor interceptor : this.interceptors) {
            if (interceptor.canConvert(uri)) {
                return interceptor.convert(uri);
            }
        }
        return this.uriConverter.normalize(uri);
    }
}
