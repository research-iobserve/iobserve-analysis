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
package org.iobserve.analysis.test.service.suites;

import java.io.IOException;

import org.iobserve.analysis.test.service.updater.DeploymentVisualizationStageTest;
import org.iobserve.analysis.test.service.updater.UndeploymentVisualizationStageTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import fi.iki.elonen.NanoHTTPD;

/**
 * Test suite that runs all tests for classes in org.iobserve.analysis.service.updater.
 *
 * @author jweg
 *
 */
@RunWith(Suite.class) // NOCS test
@SuiteClasses({ // AllocationVisualizationStageTest.class,
        DeploymentVisualizationStageTest.class, UndeploymentVisualizationStageTest.class })
public class AllTestsUpdater { // NOPMD all static, but test
    /** handler for http requests. */
    private static NanoHTTPD server; // NOCS initialized in setUpClass

    public static NanoHTTPD getServer() {
        return AllTestsUpdater.server;
    }

    /**
     * Sets up the server to test the methods that require a http connection.
     *
     * @throws IOException
     *             if the server could not be created (E.g. the socket is already used.
     */
    @BeforeClass
    public static void setUpClass() throws IOException {
        /** test server */
        AllTestsUpdater.server = new VisualizationHttpTestServer();
    }

    /**
     * Shuts down the server at the end of the test suite.
     */
    @AfterClass
    public static void cleanUpClass() {
    	if (AllTestsUpdater.server != null) {
            AllTestsUpdater.server.stop();
        }
    }

}
