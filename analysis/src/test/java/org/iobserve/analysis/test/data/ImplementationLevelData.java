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

import org.iobserve.common.record.ContainerAllocationEvent;
import org.iobserve.common.record.EJBDeployedEvent;
import org.iobserve.common.record.ServletDeployedEvent;

/**
 * @author Reiner Jung
 *
 */
public class ImplementationLevelData {
    public static final long DEPLOY_TIME = 1;
    public static final String SERVICE = "test-service";
    public static final String CONTEXT = "/path/test";
    public static final String DEPLOYMENT_ID = "service-01";

    private static final String URL = "http://" + ImplementationLevelData.SERVICE + '/'
            + ImplementationLevelData.CONTEXT;

    public ServletDeployedEvent createServletDeployedEvent() {
        return new ServletDeployedEvent(ImplementationLevelData.DEPLOY_TIME, ImplementationLevelData.SERVICE,
                ImplementationLevelData.CONTEXT, ImplementationLevelData.DEPLOYMENT_ID);
    }

    public EJBDeployedEvent createEJBDeployedEvent() {
        return new EJBDeployedEvent(ImplementationLevelData.DEPLOY_TIME, ImplementationLevelData.SERVICE,
                ImplementationLevelData.CONTEXT, ImplementationLevelData.DEPLOYMENT_ID);
    }

    public ContainerAllocationEvent createContainerAllocationEvent() {
        return new ContainerAllocationEvent(ImplementationLevelData.URL);
    }
}
