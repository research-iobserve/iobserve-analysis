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
package org.iobserve.service.behavior.analysis;

import java.io.File;

import com.beust.jcommander.Parameter;

/**
 *
 * @author Lars Jürgensen
 *
 */
public class BehaviorAnalysisSettings {

    @Parameter(names = { "-c", "--configuration" }, required = true, description = "Configuration file")

    private File configurationFile;

    public final File getConfigurationFile() {
        return this.configurationFile;
    }

    public final void setConfigurationFile(final File configurationFile) {
        this.configurationFile = configurationFile;
    }

}
