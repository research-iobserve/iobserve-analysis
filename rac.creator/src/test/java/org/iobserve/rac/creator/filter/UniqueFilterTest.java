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
package org.iobserve.rac.creator.filter;

import java.util.ArrayList;
import java.util.List;

import teetime.framework.test.StageTester;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Reiner Jung
 *
 * @since 0.0.1
 */
public class UniqueFilterTest { // NOCS test does not need default constructor

    private UniqueFilter uniqueFilter;
    private List<String> inputList;
    private List<String> expectedOutputList;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.uniqueFilter = new UniqueFilter();

        this.inputList = new ArrayList<>();

        this.inputList.add("A");
        this.inputList.add("B");
        this.inputList.add("C");
        this.inputList.add("D");
        this.inputList.add("E");
        this.inputList.add("A");
        this.inputList.add("A");
        this.inputList.add("C");
        this.inputList.add("B");

        this.expectedOutputList = new ArrayList<>();

        this.expectedOutputList.add("A");
        this.expectedOutputList.add("B");
        this.expectedOutputList.add("C");
        this.expectedOutputList.add("D");
        this.expectedOutputList.add("E");
    }

    /**
     * Test method for
     * {@link org.iobserve.rac.creator.filter.UniqueFilter#execute(java.lang.String)}.
     */
    @Test
    public void testExecuteString() {
        final List<String> outputList = new ArrayList<>();
        StageTester.test(this.uniqueFilter).and().send(this.inputList).to(this.uniqueFilter.getInputPort()).and()
                .receive(outputList).from(this.uniqueFilter.getOutputPort()).start();
        Assert.assertEquals("Not the correct number of results.", this.expectedOutputList.size(), outputList.size());
        for (int i = 0; i < this.expectedOutputList.size(); i++) {
            Assert.assertEquals("Not the same values.", this.expectedOutputList.get(i), outputList.get(i));
        }
    }

}
