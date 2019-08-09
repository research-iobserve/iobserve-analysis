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
package org.iobserve.drive.accounting;

import java.net.URL;

import com.beust.jcommander.Parameter;

/**
 * @author Reiner Jung
 *
 */
public class AccountDriverSettings {

    @Parameter(names = { "-u", "--url" }, required = true, description = "Component request URL")
    private URL url;

    @Parameter(names = { "-d", "--delay" }, required = false, description = "Delay between requests in milliseconds")
    private Integer delay;

    @Parameter(names = { "-c",
            "--count" }, required = false, description = "Count instead of display responses; report only every n-th response")
    private Integer reportModulo;

    @Parameter(names = { "-r",
            "--repeat" }, required = false, description = "Repeat sequence of login and update n times, default 1")
    private Integer repetition;

    public URL getUrl() {
        return this.url;
    }

    public Integer getDelay() {
        return this.delay;
    }

    public Integer getReportModulo() {
        return this.reportModulo;
    }

    public void setDelay(final int delay) {
        this.delay = delay;
    }

    public Integer getRepetition() {
        return this.repetition;
    }

    public void setRepetition(final Integer repetition) {
        this.repetition = repetition;
    }
}
