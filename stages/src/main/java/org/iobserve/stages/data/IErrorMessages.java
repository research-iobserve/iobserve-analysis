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
package org.iobserve.stages.data;

import java.util.List;

/**
 * Represents messages that contain errors warnings and so on that may occur during the execution of
 * the analysis.
 *
 * @author Marc Adolf
 *
 */
public interface IErrorMessages {

    /**
     * Returns the list of saved messages.
     *
     * @return the contained (error) messages.
     */
    public List<String> getMessages();

    /**
     * Sets the list of saved messages.
     *
     * @param messages
     *            the list of messages.
     */
    public void setMessages(List<String> messages);

    /**
     * Adds one message to the list.
     *
     * @param message
     *            the new message in the list.
     */
    public void addMessage(String message);

}