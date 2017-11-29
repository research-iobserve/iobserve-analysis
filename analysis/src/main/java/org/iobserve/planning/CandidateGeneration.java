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
package org.iobserve.planning;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.analysis.utils.AbstractLinearComposition;

/**
 * This class encapsulates the system planning filter in the teetime framework. It generates and
 * selects a re-deployment PCM model. The sub-stages are a model processor, a model optimizer and a
 * candidate processor.
 *
 * @author Philipp Weimann
 * @author Tobias Pöppke
 */
public class CandidateGeneration extends AbstractLinearComposition<URI, AdaptationData> {

    protected static final Logger LOG = LogManager.getLogger(CandidateGeneration.class);

    /**
     * The constructor for the model creation part.
     *
     * @param modelPreProcessor
     *            the pre processing stage to prepare the model for optimization
     * @param modelOptimizer
     *            the optimizer
     * @param candidateProcessor
     *            the processor for the generated candidate
     */
    public CandidateGeneration(final ModelProcessing modelPreProcessor, final ModelOptimization modelOptimizer,
            final CandidateProcessing candidateProcessor) {
        super(modelPreProcessor.getInputPort(), candidateProcessor.getOutputPort());

        this.connectPorts(modelPreProcessor.getOutputPort(), modelOptimizer.getInputPort());
        this.connectPorts(modelOptimizer.getOutputPort(), candidateProcessor.getInputPort());
    }

}
