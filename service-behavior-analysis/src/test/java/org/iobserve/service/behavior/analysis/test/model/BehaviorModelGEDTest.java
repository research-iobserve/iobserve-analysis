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
package org.iobserve.service.behavior.analysis.test.model;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Lars Jürgensen
 *
 */
public class BehaviorModelGEDTest {

    private BehaviorModelGED model1;

    @Before
    public void setupTestData() {
        this.model1 = new BehaviorModelGED();

    }

    @Test
    public void emptyUserSessionTest() {
        MatcherAssert.assertThat(this.model1.getNodes().values(), Is.is(Matchers.empty()));
    }

}
