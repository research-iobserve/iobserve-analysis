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

import org.iobserve.analysis.service.services.CommunicationInstanceServiceTest;
import org.iobserve.analysis.service.services.CommunicationServiceTest;
import org.iobserve.analysis.service.services.NodeServiceTest;
import org.iobserve.analysis.service.services.ServiceInstanceServiceTest;
import org.iobserve.analysis.service.services.ServiceServiceTest;
import org.iobserve.analysis.service.services.SystemServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite that runs all tests for classes in org.iobserve.analysis.service.services.
 *
 * @author Josefine Wegert
 *
 */
@RunWith(Suite.class) // NOCS test
@SuiteClasses({ CommunicationInstanceServiceTest.class, CommunicationServiceTest.class, NodeServiceTest.class,
        ServiceInstanceServiceTest.class, ServiceServiceTest.class, SystemServiceTest.class })
public class AllTestsServices {

}
