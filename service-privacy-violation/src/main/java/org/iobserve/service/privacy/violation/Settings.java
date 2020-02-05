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
package org.iobserve.service.privacy.violation;

import java.io.File;

import com.beust.jcommander.Parameter;

/**
 * @author Reiner Jung
 *
 */
public class Settings {
    @Parameter(names = { "-c", "--configuration" }, required = true, description = "Configuration file")
    private File configurationFile;

    private File modelInitDirectory;

    private File warningFile;

    private File alarmsFile;

    private File modelDatabaseDirectory;

    private File modelDumpDirectory;

    public final File getConfigurationFile() {
        return this.configurationFile;
    }

    public final void setConfigurationFile(final File configurationFile) {
        this.configurationFile = configurationFile;
    }

    public final File getModelInitDirectory() {
        return this.modelInitDirectory;
    }

    public final void setModelInitDirectory(final File modelInitDirectory) {
        this.modelInitDirectory = modelInitDirectory;
    }

    public final File getWarningFile() {
        return this.warningFile;
    }

    public final void setWarningFile(final File warningFile) {
        this.warningFile = warningFile;
    }

    public final File getAlarmsFile() {
        return this.alarmsFile;
    }

    public final void setAlarmsFile(final File alarmsFile) {
        this.alarmsFile = alarmsFile;
    }

    public final File getModelDatabaseDirectory() {
        return this.modelDatabaseDirectory;
    }

    public final void setModelDatabaseDirectory(final File modelDatabaseDirectory) {
        this.modelDatabaseDirectory = modelDatabaseDirectory;
    }

    public final File getModelDumpDirectory() {
        return this.modelDumpDirectory;
    }

    public final void setModelDumpDirectory(final File modelDumpDirectory) {
        this.modelDumpDirectory = modelDumpDirectory;
    }

}
