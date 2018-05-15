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
package org.iobserve.planning.cli;

/**
 * Configuration keys for the planning service's configuration file.
 *
 * @author Lars Bluemke
 *
 */
public final class ConfigurationKeys {

    /**
     * Input port where runtime models are received via TCP.
     */
    public static final String RUNTIMEMODEL_INPUTPORT = "runtimeModelInputPort";

    /**
     * Directory where runtime models are stored.
     */
    public static final String RUNTIMEMODEL_DIRECTORY = "runtimeModelDirectory";

    /**
     * Directory where redeployment models are stored (only for mockup of planning).
     */
    public static final String REDEPLOYMENTMODEL_DIRECTORY = "redeploymentModelDirectory";

    /**
     * Directory containing the PerOpteryx RCP application's executable.
     */
    public static final String PEROPTERYX_HEADLESS_DIRECTORY = "perOpteryxHeadlessDirectory";

    /**
     * Directory containing the LQN solver's executable (not mandatory if LQNS is on your PATH
     * anyway).
     */
    public static final String LQNS_DIRECTORY = "lqnsDirectory";

    /**
     * Hostname of the adaptaion service.
     */
    public static final String ADAPTATION_HOSTNAME = "adaptationHostname";

    /**
     * Port where the adaptation service receives the runtime models via TCP.
     */
    public static final String ADAPTATION_RUNTIMEMODEL_INPUTPORT = "adaptationRuntimeModelInputPort";

    /**
     * Port where the adaptation service receives the redeployment models via TCP.
     */
    public static final String ADAPTATION_REDEPLOYMENTMODEL_INPUTPORT = "adaptationRedeploymentModelInputPort";

    /**
     * Factory, do not instantiate.
     */
    private ConfigurationKeys() {
        // empty constructor.
    }
}
