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
package org.iobserve.service.behavior.analysis;

import java.io.File;

import com.beust.jcommander.JCommander;

import kieker.common.exception.ConfigurationException;
import kieker.tools.common.AbstractService;

public class BehaviorAnalysisServiceMain
        extends AbstractService<BehaviorAnalysisTeetimeConfiguration, BehaviorAnalysisSettings> {

    @Override
    protected BehaviorAnalysisTeetimeConfiguration createTeetimeConfiguration() throws ConfigurationException {

        return null;

    }

    @Override
    protected File getConfigurationFile() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected boolean checkConfiguration(final kieker.common.configuration.Configuration configuration,
            final JCommander commander) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected void shutdownService() {
        // TODO Auto-generated method stub

    }

}
