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
import java.io.IOException;

import teetime.framework.Configuration;

/**
 * Analysis configuration for the data collector.
 *
 * @author Reiner Jung
 *
 */
public class EvaluationConfiguration extends Configuration {

    /**
     * Configure analysis.
     */
    public EvaluationConfiguration(final File baselineModelFile, final File testModelFile, final File resultFile)
            throws IOException {

        System.out.println("Baseline " + baselineModelFile.getAbsolutePath());
        System.out.println("Test model " + testModelFile.getAbsolutePath());
        System.out.println("Results in " + resultFile.getAbsolutePath());

        final BehaviorModelJSONReader baselineModelReader = new BehaviorModelJSONReader(baselineModelFile);
        final BehaviorModelJSONReader testModelReader = new BehaviorModelJSONReader(testModelFile);

        final ModelComparisonStage modelComparisonStage = new ModelComparisonStage();
        modelComparisonStage.declareActive();

        final ComparisonOutputStage resultWriter = new ComparisonOutputStage(resultFile);

        this.connectPorts(baselineModelReader.getOutputPort(), modelComparisonStage.getBaselineModelInputPort());
        this.connectPorts(testModelReader.getOutputPort(), modelComparisonStage.getTestModelInputPort());
        this.connectPorts(modelComparisonStage.getOutputPort(), resultWriter.getInputPort());
    }

}
