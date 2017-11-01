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
package org.iobserve.analysis.service.suites;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.iobserve.analysis.service.updater.AllocationVisualizationStageTest;
import org.iobserve.analysis.service.updater.DeploymentVisualizationStageTest;
import org.iobserve.analysis.service.updater.UndeploymentVisualizationStageTest;
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
    /** handler for http requests. */
    private static TestHandler testHandler;
    private static HttpServer server;

    /**
     * Sets up the server to test the methods that require a http connection.
     *
     * @throws IOException
     *             if the server could not be created (E.g. the socket is already used.
     */
    @BeforeClass
    public static void setUpClass() throws IOException {
        /** test server */
        AllTestsUpdater.testHandler = new TestHandler();
        AllTestsUpdater.server = HttpServer.create(new InetSocketAddress(9090), 10);
        AllTestsUpdater.server.createContext("/v1/systems/test_systemId/changelogs", AllTestsUpdater.testHandler);
        AllTestsUpdater.server.start();

    }
}
