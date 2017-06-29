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

import java.util.ArrayList;
import java.util.List;

import org.iobserve.common.record.ContainerAllocationEvent;
import org.iobserve.common.record.IAllocationRecord;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for TAllocation filter
 *
 * @author jweg
 *
 */
public class TestTAllocation {

    private static final String URL = "";

    private TAllocation tAllocation;
    private final List<IAllocationRecord> inputEvents = new ArrayList<>();

    @Before
    public void initializeTAllocation() {
        // this.tAllocation = new TAllocation(resourceEnvironmentModelProvider,
        // resourceEnvironmentModelGraph);
        final ContainerAllocationEvent allocationEvent = new ContainerAllocationEvent(TestTAllocation.URL);

    }

    // Das testet eher ResourceEnvironmentModelBuilder.createResourceContainer(model, serverName)!!
    /**
     * Check whether allocation was successful.
     */
    @Test
    public void checkAllocation() {

    }
}
