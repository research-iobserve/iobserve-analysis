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
package org.iobserve.stages.test.source;

import org.iobserve.stages.source.ITraceMetadataRewriter;
import org.iobserve.stages.source.MultipleConnectionTcpReaderStage;
import org.iobserve.stages.source.NoneTraceMetadataRewriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Reiner Jung
 *
 */
@RunWith(PowerMockRunner.class) // NOCS test class, no constructor
@PrepareForTest({ MultipleConnectionTcpReaderStage.class }) // NOCS api
public class MultipleConnectionTcpReaderStageTest {

    private static final int PORT = 9876;

    private static final int BUFFER_SIZE = 1024;

    /**
     * Setup TCP connection testing.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test if receiving connections work.
     *
     * @throws Exception
     *             on various error
     */
    @Ignore
    @Test
    public void receiveConnections() throws Exception {
        final ITraceMetadataRewriter rewriter = new NoneTraceMetadataRewriter();
        final MultipleConnectionTcpReaderStage classUnderTest = PowerMockito.spy(new MultipleConnectionTcpReaderStage(
                MultipleConnectionTcpReaderStageTest.PORT, MultipleConnectionTcpReaderStageTest.BUFFER_SIZE, rewriter));

        // StageTester.test(classUnderTest).start();
    }
}
