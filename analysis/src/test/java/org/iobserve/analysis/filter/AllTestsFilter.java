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
package org.iobserve.analysis.filter;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite that run all tests for classes in org.iobserve.analysis.filter.
 *
 * @author jweg
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ TAllocationNoResourceContainerTest.class, TAllocationResourceContainerTest.class,
        TDeploymentNoResourceContainerTest.class, TDeploymentResourceContainerTest.class,
        TUndeploymentNoResourceContainerTest.class, TUndeploymentResourceContainerTest.class })
public class AllTestsFilter {

}
