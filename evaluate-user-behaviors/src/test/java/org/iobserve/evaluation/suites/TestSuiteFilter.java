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
package org.iobserve.evaluation.suites;

import org.iobserve.evaluation.filter.ModelComparisonStageTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite that runs all tests for classes org.iobserve.evaluation.filters.
 *
 * @author Reiner Jung
 *
 * @since 0.0.2
 */
@RunWith(Suite.class) // NOCS no constructor for tests
@SuiteClasses({ ModelComparisonStageTest.class }) // NOCS array is necessary for test API
public class TestSuiteFilter {

}
