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

import org.eclipse.emf.common.util.URI;

/**
 * @author Fabian Keller
 */
public interface IURIConverterInterceptor {
    /**
     * Determines whether this URI converter can convert the given URI.
     * <p>
     * Returning true results in the {@link IURIConverterInterceptor#convert(URI)} method to be
     * called.
     *
     * @param uri
     *            the uri checked to be convertible
     *
     * @return returns true when the conversion was possible
     */
    boolean canConvert(URI uri);

    /**
     * Converts the given URI.
     *
     * @param uri
     *            the uri to be converted
     *
     * @return returns the converted URI
     */
    URI convert(URI uri);
}
