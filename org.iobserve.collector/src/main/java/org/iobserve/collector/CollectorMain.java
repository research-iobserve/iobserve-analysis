/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.collector;

import teetime.framework.Execution;

/**
 * Collector main class.
 *
 * @author Reiner Jung
 */
public final class CollectorMain {

    /**
     * This is a simple main class which does not need to be instantiated.
     */
    private CollectorMain() {

    }

    /**
     * Configure and execute the TCP Kieker data collector.
     *
     * @param args
     *            arguments are ignored
     */
    public static void main(final String[] args) {
        System.out.println("Receiver");
        final SimpleBridgeConfiguration configuration = new SimpleBridgeConfiguration();
        final Execution<SimpleBridgeConfiguration> analysis = new Execution<>(configuration);

        System.out.println("Running analysis");

        analysis.executeBlocking();

        System.out.println("Counts " + configuration.getCounter().getCount());

        System.out.println("Done");
    }
}
