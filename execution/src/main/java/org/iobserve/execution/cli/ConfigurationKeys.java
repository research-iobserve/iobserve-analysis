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
package org.iobserve.execution.cli;

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
     * Directory where execution plan is stored.
     */
    public static final String EXECUTIONPLAN_DIRECTORY = "executionPlanDirectory";

    /**
     * Name of execution plan file.
     */
    public static final String EXECUTIONPLAN_FILENAME = "executionPlanFileName";

    /**
     * IP address of kubernetes master.
     */
    public static final String KUBERNETES_MASTER_IP = "kubernetesMasterIp";

    /**
     * Port to access kubernetes master.
     */
    public static final String KUBERNETES_MASTER_PORT = "kubernetesMasterPort";

    /**
     * Location of correspondence model.
     */
    public static final String CORRESPONDENCE_MODEL_URI = "correspondenceModelUri";

    /**
     * Prefix of application images, e.g. blade1.se.internal:5000.
     */
    public static final String IMAGE_PREFIX = "imagePrefix";

    /**
     * Factory, do not instantiate.
     */
    private ConfigurationKeys() {
        // empty constructor.
    }
}
