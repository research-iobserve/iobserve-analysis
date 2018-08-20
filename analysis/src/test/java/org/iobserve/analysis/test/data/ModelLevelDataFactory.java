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
import org.iobserve.common.record.ISOCountryCode;
import org.iobserve.model.correspondence.EServiceTechnology;
import org.iobserve.model.test.data.ImplementationLevelDataFactory;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;

/**
 * @author Reiner Jung
 *
 */
public final class ModelLevelDataFactory {

    private ModelLevelDataFactory() {
        // private factory constructor
    }

    public static PCMDeployedEvent createPCMDeployedEvent(final ISOCountryCode countryCode,
            final AssemblyContext assemblyContext) { // NOPMD
        final String urlContext = ImplementationLevelDataFactory.CONTEXT.replaceAll("\\.", "/");
        final String url = "http://" + ImplementationLevelDataFactory.SERVICE + '/' + urlContext;

        return new PCMDeployedEvent(EServiceTechnology.SERVLET, ImplementationLevelDataFactory.SERVICE, assemblyContext,
                url, countryCode, 0);
    }

    public static PCMUndeployedEvent createPCMUndeployedEvent(final AssemblyContext assemblyContext) {
        return new PCMUndeployedEvent(ImplementationLevelDataFactory.SERVICE, assemblyContext, null, 0);
    }

}
