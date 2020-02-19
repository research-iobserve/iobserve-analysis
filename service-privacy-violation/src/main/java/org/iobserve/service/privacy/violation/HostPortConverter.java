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
package org.iobserve.service.privacy.violation;

import com.beust.jcommander.IStringConverter;

/**
 * Converter for command line input to separate host and port.
 *
 * @author Reiner Jung
 *
 */
public class HostPortConverter implements IStringConverter<ConnectionData> {

    /**
     * default constructor.
     */
    public HostPortConverter() {
        // empty default constructor
    }

    @Override
    public ConnectionData convert(final String value) {
        final String[] s = value.split(":");

        return new ConnectionData(s[0], Integer.valueOf(Integer.parseInt(s[1])));
    }
}
