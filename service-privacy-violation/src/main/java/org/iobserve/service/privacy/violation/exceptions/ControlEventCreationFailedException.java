/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.service.privacy.violation.exceptions;

import org.iobserve.utility.tcp.events.AbstractTcpControlEvent;

/**
 *
 * Is thrown if errors occur during the creation of a {@link AbstractTcpControlEvent control event}.
 *
 * @author Marc Adolf
 *
 */
public class ControlEventCreationFailedException extends Exception {

    private static final long serialVersionUID = 1L;

    public ControlEventCreationFailedException(final String message) {
        super(message);
    }

}
