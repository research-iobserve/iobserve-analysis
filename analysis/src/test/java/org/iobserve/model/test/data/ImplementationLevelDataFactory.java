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

import org.iobserve.common.record.ContainerAllocationEvent;
import org.iobserve.common.record.EJBDeployedEvent;
import org.iobserve.common.record.ServletDeployedEvent;

/**
 * @author Reiner Jung
 *
 */
public class ImplementationLevelDataFactory {
    public static final long DEPLOY_TIME = 1;
    public static final String SERVICE = "test-service";
    public static final String CONTEXT = "/path/test";
    public static final String DEPLOYMENT_ID = "service-01";

    public static final String URL = "http://" + ImplementationLevelDataFactory.SERVICE + '/'
            + ImplementationLevelDataFactory.CONTEXT;

    public static final ServletDeployedEvent SERVLET_DEPLOYED_EVENT = ImplementationLevelDataFactory
            .createServletDeployedEvent();
    public static final EJBDeployedEvent EJB_DEPLOYED_EVENT = ImplementationLevelDataFactory.createEJBDeployedEvent();
    public static final ContainerAllocationEvent CONTAINER_ALLOCATION_EVENT = ImplementationLevelDataFactory
            .createContainerAllocationEvent();

    private static ServletDeployedEvent createServletDeployedEvent() {
        return new ServletDeployedEvent(ImplementationLevelDataFactory.DEPLOY_TIME, ImplementationLevelDataFactory.SERVICE,
                ImplementationLevelDataFactory.CONTEXT, ImplementationLevelDataFactory.DEPLOYMENT_ID);
    }

    private static EJBDeployedEvent createEJBDeployedEvent() {
        return new EJBDeployedEvent(ImplementationLevelDataFactory.DEPLOY_TIME, ImplementationLevelDataFactory.SERVICE,
                ImplementationLevelDataFactory.CONTEXT, ImplementationLevelDataFactory.DEPLOYMENT_ID);
    }

    private static ContainerAllocationEvent createContainerAllocationEvent() {
        return new ContainerAllocationEvent(ImplementationLevelDataFactory.URL);
    }
}
