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
package org.iobserve.execution.service;

/**
 * Configuration keys for the execution service's configuration file.
 *
 * @author Lars Bluemke
 *
 */
public final class ConfigurationKeys {

    /**
     * Input port for receiving the execution plan via TCP.
     */
    public static final String EXECUTIONPLAN_INPUTPORT = "executionPlanInputPort";

    /**
     * Input port for receiving runtime models via TCP.
     */
    public static final String RUNTIMEMODEL_INPUTPORT = "runtimeModelInputPort";

    /**
     * Input port for receiving redeployment models via TCP.
     */
    public static final String REDEPLOYMENTMODEL_INPUTPORT = "redeploymentModelInputPort";

    /**
     * Working directory where execution plan and the model directories are stored.
     */
    public static final String WORKING_DIRECTORY = "workingDirectory";

    /**
     * Correspondence model file name (in the working directory) e.g. "default.correspondence".
     */
    public static final String CORRESPONDENCEMODEL_NAME = "correspondenceModelName";

    /**
     * Locator for application images, e.g. "blade1.se.internal:5000" for JPetStore example
     */
    public static final String IMAGE_LOCATOR = "imageLocator";

    /**
     * The kubernetes namespace to be used. Use "default" if you didn't define a custom namespace.
     */
    public static final String NAMESPACE = "namespace";

    /**
     * A component's subdomain, e.g. "jpetstore" for JPetStore example
     */
    public static final String SUBDOMAIN = "subdomain";

    /**
     * Factory, do not instantiate.
     */
    private ConfigurationKeys() {
        // empty constructor.
    }
}
