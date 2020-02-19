/***************************************************************************
 * Copyright 2019 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.runtime.reconfigurator;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.beust.jcommander.IStringConverter;

/**
 *
 * @author Reiner Jung
 *
 */
public class ParameterIPConverter implements IStringConverter<Inet4Address> {

    @Override
    public Inet4Address convert(final String host) {
        try {
            return (Inet4Address) InetAddress.getByName(host);
        } catch (final UnknownHostException e) {
            return null;
        }
    }

}
