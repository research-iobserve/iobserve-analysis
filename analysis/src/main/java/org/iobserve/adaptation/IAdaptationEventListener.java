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
package org.iobserve.adaptation;

import java.util.List;

import org.iobserve.adaptation.execution.AbstractActionScript;

/**
 * Interface for an event listener that is used while executing the adaptation stage.
 *
 * @author Tobias Pöppke
 *
 */
public interface IAdaptationEventListener {
    /**
     * Notifies the listener of actions in the adaptation plan that can not be executed
     * automatically.
     *
     * The listener may decide to throw a runtime exception to abort the adaptation process or
     * return normally to continue.
     *
     * @param unsupportedActions
     *            list of unsupported actions
     */
    void notifyUnsupportedActionsFound(List<AbstractActionScript> unsupportedActions);

    /**
     * Notifies the listener of an error during script execution.
     *
     * The listener may decide to throw a runtime exception to abort the adaptation process or
     * return normally to continue.
     *
     * @param script
     *            the script for which the error was encountered
     * @param e
     *            the exception raised by the script
     */
    void notifyExecutionError(AbstractActionScript script, Throwable e);
}
