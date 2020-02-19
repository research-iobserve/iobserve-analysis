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
package org.iobserve.service.behavior.analysis.clustering;

import teetime.stage.basic.AbstractTransformation;

import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Converts Behavior Models to Optics Data objects. This is necessary for the optics algorithm.
 * 
 * @author Lars Jürgensen
 *
 */
public class BehaviorModelToOpticsDataConverter extends AbstractTransformation<BehaviorModelGED, OpticsData> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BehaviorModelToOpticsDataConverter.class);

    @Override
    protected void execute(final BehaviorModelGED model) throws Exception {
        this.outputPort.send(new OpticsData(model));
        BehaviorModelToOpticsDataConverter.LOGGER.info("Converted BehaviorModelGED to OpticsData");

    }

}
