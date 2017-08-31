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
package org.iobserve.analysis.service.updater;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.sun.net.httpserver.HttpServer;

import util.TestHandler;

/**
 * Test suite that runs all tests for classes in org.iobserve.analysis.service.updater.
 *
 * @author jweg
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ AllocationVisualizationStageTest.class, DeploymentVisualizationStageTest.class,
        UndeploymentVisualizationStageTest.class })
public class AllTestsUpdater {
    /** handler for http requests */
    private static TestHandler testHandler;

    @BeforeClass
    public static void setUpClass() throws IOException {
        System.out.println("Master setup");

        /** test server */
        AllTestsUpdater.testHandler = new TestHandler();
        final HttpServer server = HttpServer.create(new InetSocketAddress(9090), 0);
        server.createContext("/v1/systems/test_systemId/changelogs", AllTestsUpdater.testHandler);
        server.start();

    }
}
