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
package org.iobserve.model.test.data;

import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.analysis.deployment.data.PCMUndeployedEvent;

/**
 * @author Reiner Jung
 *
 */
public final class ModelLevelData {

    public static final PCMDeployedEvent PCM_DEPLOYED_EVENT = ModelLevelData.createPCMDeployedEvent((short) 0);
    public static final PCMDeployedEvent PCM_DEPLOYED_DE_EVENT = ModelLevelData.createPCMDeployedEvent((short) 49);

    public static final PCMUndeployedEvent PCM_UNDEPLOYED_EVENT = ModelLevelData.createPCMUndeployedEvent();

    private ModelLevelData() {
        // private factory constructor
    }

    private static PCMDeployedEvent createPCMDeployedEvent(final short countryCode) { // NOPMD
        final String urlContext = ImplementationLevelDataFactory.CONTEXT.replaceAll("\\.", "/");
        final String url = "http://" + ImplementationLevelDataFactory.SERVICE + '/' + urlContext;

        return new PCMDeployedEvent(ImplementationLevelDataFactory.SERVICE,
                CorrespondenceModelDataFactory.CORRESPONDENT, url, countryCode);
    }

    private static PCMUndeployedEvent createPCMUndeployedEvent() {
        return new PCMUndeployedEvent(ImplementationLevelDataFactory.SERVICE,
                CorrespondenceModelDataFactory.CORRESPONDENT);
    }

}
