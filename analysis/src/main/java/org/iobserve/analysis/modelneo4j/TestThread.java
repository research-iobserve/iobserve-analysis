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
package org.iobserve.analysis.modelneo4j;

import org.eclipse.emf.ecore.EObject;

public class TestThread extends Thread {

    private final Graph graph;
    private final EObject component;

    public TestThread(final Graph graph, final EObject component) {
        this.graph = graph;
        this.component = component;
    }

    @Override
    public void run() {
        final ModelProvider<EObject> prov = new ModelProvider<>(this.graph);

        for (int i = 0; i < 5; i++) {
            // System.out.println("Thread " + this.getId() + " clearing...");
            // prov.clearGraph();
            // System.out.println("Thread " + this.getId() + " reading...");
            // final Repository rep = prov.readRootComponent(Repository.class);
            // System.out.println("Thread " + this.getId() + " deleting...");
            // prov.deleteComponentAndDatatypes(Repository.class, rep.getId());
            System.out.println("Thread " + this.getId() + " creating...");
            prov.createComponent(this.component);
        }

        System.out.println("Thread " + this.getId() + " is done");
    }

}
