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

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * @author Reiner Jung
 *
 */
public class RepairStage extends AbstractConsumerStage<String> {

    private String storedLine = null; // NOPMD null is used for documentation purposes
    private final OutputPort<String> outputPort = this.createOutputPort(String.class);

    @Override
    protected void execute(final String line) throws Exception {
        if (line.charAt(0) == '$') {
            if (this.storedLine != null) {
                this.outputPort.send(this.storedLine);
            }
            this.storedLine = line;
        } else {
            this.storedLine += "\n" + line;
        }
    }

    @Override
    protected void onTerminating() {
        if (this.storedLine != null) {
            this.outputPort.send(this.storedLine);
        }
        super.onTerminating();
    }

    public OutputPort<String> getOutputPort() {
        return this.outputPort;
    }

}
