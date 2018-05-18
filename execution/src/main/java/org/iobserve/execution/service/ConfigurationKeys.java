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
     * Working directory where execution plan and the model directories are stored.
     */
    public static final String WORKING_DIRECTORY = "workingDirectory";

    /**
     * Name of execution plan file.
     */
    public static final String EXECUTIONPLAN_FILENAME = "executionPlanFileName";

    /**
     * Location of correspondence model.
     */
    public static final String CORRESPONDENCE_MODEL_URI = "correspondenceModelUri";

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
