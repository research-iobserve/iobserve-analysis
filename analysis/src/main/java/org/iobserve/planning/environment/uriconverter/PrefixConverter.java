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
 * If the URI to be converted matches the given prefix, the prefix is replaced by the given
 * replacement.
 *
 * @author Fabian Keller
 */
public class PrefixConverter implements IURIConverterInterceptor {

    private final String prefix;
    private final String replacement;

    /**
     * Create an prefix converter.
     *
     * @param prefix
     *            prefix to be replaced
     * @param replacement
     *            replacement string
     */
    public PrefixConverter(final String prefix, final String replacement) {
        this.prefix = prefix;
        this.replacement = replacement;
    }

    @Override
    public boolean canConvert(final URI uri) {
        return uri.toString().startsWith(this.prefix);
    }

    @Override
    public URI convert(final URI uri) {
        return URI.createURI(uri.toString().replace(this.prefix, this.replacement));
    }
}
