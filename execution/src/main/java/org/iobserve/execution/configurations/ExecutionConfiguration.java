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
package org.iobserve.execution.configurations;

import java.io.File;

import teetime.framework.Configuration;

import org.iobserve.evaluation.ModelComparer;
import org.iobserve.evaluation.SystemEvaluation;
import org.iobserve.execution.AdaptationExecution;
import org.iobserve.execution.IAdaptationEventListener;

/**
 * Configuration for the stages of the execution service.
 *
 * @author Lars Bluemke
 *
 */
public class ExecutionConfiguration extends Configuration {

    public ExecutionConfiguration(final IAdaptationEventListener eventListener, final File deployablesFolder) {

        // There is an AdaptionEventListener class, but in the previous implementation null was
        // used instead.
        if (deployablesFolder != null) {
            new AdaptationExecution(eventListener, deployablesFolder);
            new SystemEvaluation(new ModelComparer());
        }

    }
}
