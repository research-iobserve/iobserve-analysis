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
package org.iobserve.adaptation.service;

/**
 * Configuration keys for the adaptation service's configuration file.
 *
 * @author Lars Bluemke
 *
 */
public final class ConfigurationKeys {

    /**
     * Input port for receiving runtime models via TCP.
     */
    public static final String RUNTIMEMODEL_INPUTPORT = "runtimeModelInputPort";

    /**
     * Input port for receiving redeployment models via TCP.
     */
    public static final String REDEPLOYMENTMODEL_INPUTPORT = "redeploymentModelInputPort";

    /**
     * Directory where received runtime models are stored.
     */
    public static final String RUNTIMEMODEL_DIRECTORY = "runtimeModelDirectory";

    /**
     * Directory where received redeployment models are stored.
     */
    public static final String REDEPLOYMENTMODEL_DIRECTORY = "redeploymentModelDirectory";

    /**
     * File location of the serialized execution plan.
     */
    public static final String EXECUTIONPLAN_URI = "executionPlanURI";

    /**
     * Hostname of the execution service.
     */
    public static final String EXECUTION_HOSTNAME = "executionHostname";

    /**
     * Port where the execution service receives the serialized execution plan via TCP.
     */
    public static final String EXECUTION_INPUTPORT = "executionInputPort";

    /**
     * Factory, do not instantiate.
     */
    private ConfigurationKeys() {
        // empty constructor.
    }
}
