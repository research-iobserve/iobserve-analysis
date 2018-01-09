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
package org.iobserve.analysis.test.data;

import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.analysis.deployment.data.PCMUndeployedEvent;

/**
 * @author Reiner Jung
 *
 */
public class ModelLevelData {

    public static PCMDeployedEvent createPCMDeployedEvent() {
        final String urlContext = ImplementationLevelData.CONTEXT.replaceAll("\\.", "/");
        final String url = "http://" + ImplementationLevelData.SERVICE + '/' + urlContext;

        return new PCMDeployedEvent(ImplementationLevelData.SERVICE, CorrespondenceModelData.createCorrespondent(), url,
                (short) 49);
    }

    public static PCMUndeployedEvent createPCMUndeployedEvent() {
        return new PCMUndeployedEvent(ImplementationLevelData.SERVICE, CorrespondenceModelData.createCorrespondent());
    }

}
