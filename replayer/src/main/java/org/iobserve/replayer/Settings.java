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
package org.iobserve.replayer;

import java.io.File;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;
import com.beust.jcommander.converters.IntegerConverter;

/**
 * @author Reiner Jung
 *
 */
public class Settings {

    @Parameter(names = { "-i",
            "--input" }, required = true, description = "Input data directory.", converter = FileConverter.class)
    private File dataLocation;

    @Parameter(names = { "-p",
            "--port" }, required = true, description = "Output port.", converter = IntegerConverter.class)
    private Integer outputPort;

    @Parameter(names = { "-h",
            "--host" }, required = true, description = "Name or IP address of the host where the data is send to.")
    private String hostname;

    @Parameter(names = { "-n",
            "--no-delay" }, required = false, description = "Read and send events as fast as possible.")
    private boolean noDelay;

    @Parameter(names = { "-d",
            "--delay" }, required = false, description = "Delay factor. Default is 1 = realtime, 2 = twice the speed/half of the delay.")
    private Long delayFactor;

    @Parameter(names = { "-c",
            "--count" }, required = false, description = "Show count of events sent. Display count every n-th event.")
    private Long showRecordCount;

    @Parameter(names = { "-r",
            "--time-rewrite" }, required = false, description = "Set event timestamps relative to present time.")
    private boolean timeRelative; // NOPMD pmd thinks this is not used, but this is not the case.

    public final File getDataLocation() {
        return this.dataLocation;
    }

    public final Integer getOutputPort() {
        return this.outputPort;
    }

    public final String getHostname() {
        return this.hostname;
    }

    public final boolean isNoDelay() {
        return this.noDelay;
    }

    public final Long getDelayFactor() {
        return this.delayFactor;
    }

    public final Long getShowRecordCount() {
        return this.showRecordCount;
    }

    public boolean isTimeRelative() {
        return this.timeRelative;
    }

}
