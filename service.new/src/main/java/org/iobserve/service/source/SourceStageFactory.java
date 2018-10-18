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
package org.iobserve.service.source;

import kieker.common.configuration.Configuration;

import org.iobserve.service.CommonConfigurationKeys;
import org.iobserve.service.InstantiationFactory;
import org.iobserve.stages.general.ConfigurationException;

/**
 * @author Reiner Jung
 *
 * @since 0.0.3
 */
public final class SourceStageFactory {

    private SourceStageFactory() {
        // private constructor for factory
    }

    /**
     * Create a source stage based on the given configuration.
     *
     * @param configuration
     *            iobserve configuration
     * @return instantiated reader composite stage
     * @throws ConfigurationException
     *             on error
     */
    public static ISourceCompositeStage createSourceCompositeStage(final Configuration configuration)
            throws ConfigurationException {
        final String sourceStageClassName = configuration.getStringProperty(CommonConfigurationKeys.SOURCE_STAGE);

        return InstantiationFactory.createWithConfiguration(ISourceCompositeStage.class, sourceStageClassName,
                configuration);
    }
}
