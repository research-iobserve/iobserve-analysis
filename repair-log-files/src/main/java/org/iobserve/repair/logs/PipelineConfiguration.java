/***************************************************************************
 * Copyright (C) 2019 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.repair.logs;

import java.io.File;
import java.io.IOException;

import teetime.framework.Configuration;

/**
 *
 * @author Reiner Jung
 *
 */
public class PipelineConfiguration extends Configuration {

    /**
     * Create the teetime configuration.
     *
     * @param input
     *            input file
     * @throws IOException
     *             on io errors
     */
    public PipelineConfiguration(final File input) throws IOException {
        final TextFileLineReader fileReader = new TextFileLineReader(input);
        final RepairStage repairStage = new RepairStage();
        final FileOverwriter fileOverwriter = new FileOverwriter(input);

        this.connectPorts(fileReader.getOutputPort(), repairStage.getInputPort());
        this.connectPorts(repairStage.getOutputPort(), fileOverwriter.getInputPort());
    }
}
