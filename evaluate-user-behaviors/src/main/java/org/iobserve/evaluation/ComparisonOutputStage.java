/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.evaluation;

import java.io.File;

import teetime.framework.AbstractConsumerStage;

/**
 * Sync all incoming records with a Kieker writer to a text file log.
 *
 * @author Reiner Jung
 *
 */
public class ComparisonOutputStage extends AbstractConsumerStage<ComparisonResult> {

    private final File outputFile;

    /**
     * Configure and setup the Kieker writer.
     *
     * @param dataLocation
     *            data location
     * @param hostname
     *            selected hostname
     */
    public ComparisonOutputStage(File outputFile) {
        this.outputFile = outputFile;
    }

    @Override
    protected void execute(final ComparisonResult result) {

    }

}
