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
package org.iobserve.compare.behavior.models;

import java.io.File;
import java.io.IOException;

import teetime.framework.Configuration;

//import org.iobserve.evaluation.filter.ComparisonOutputStage;
//import org.iobserve.evaluation.filter.ModelComparisonStage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Analysis configuration for the data collector.
 *
 * @author Lars Jürgensen
 *
 */
public class ComparisionConfiguration extends Configuration {
    private static final Logger LOGGER = LoggerFactory.getLogger(ComparisionConfiguration.class);

    /**
     * Configure evaluation analysis to compare a observed model with a reference model.
     *
     * @param referenceModelFile
     *            reference model file descriptor
     * @param testModelFile
     *            file descriptor for a file containing an observed model to be compared to the
     *            reference model
     * @param resultFile
     *            file descriptor for the result file
     * @throws IOException
     *             on any IO error, e.g., when files do not exist, cannot be accessed or created
     */
    public ComparisionConfiguration(final File referenceModelFile, final File testModelFile, final File resultFile)
            throws IOException {

        ComparisionConfiguration.LOGGER.debug("Baseline {}", referenceModelFile.getAbsolutePath());
        ComparisionConfiguration.LOGGER.debug("Test model {}", testModelFile.getAbsolutePath());
        ComparisionConfiguration.LOGGER.debug("Results in {}", resultFile.getAbsolutePath());

        final ModelReaderStage referenceModelReader = new ModelReaderStage(referenceModelFile);
        final ModelReaderStage testModelReader = new ModelReaderStage(testModelFile);

        final ModelComparisonStage modelComparisonStage = new ModelComparisonStage();
        modelComparisonStage.declareActive();

        final ComparisonOutputStage resultWriter = new ComparisonOutputStage(resultFile);

        this.connectPorts(referenceModelReader.getOutputPort(), modelComparisonStage.getReferenceModelInputPort());
        this.connectPorts(testModelReader.getOutputPort(), modelComparisonStage.getTestModelInputPort());
        this.connectPorts(modelComparisonStage.getOutputPort(), resultWriter.getInputPort());
    }

}
