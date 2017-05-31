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
package org.iobserve.analysis.modelneo4j.genericapproach;

import java.io.File;

import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.model.RepositoryModelProvider;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.Repository;

/**
 *
 * @author Lars Bluemke
 *
 */
public class TestGeneric {
    private static final File DB_PATH = new File("neo4jdb");
    private static final File DB_PATH2 = new File("neo4jdb2");
    private static final File PCM_MODELS_DIRECTORY = new File(
            "/Users/LarsBlumke/Documents/CAU/Masterprojekt/iObserveWorkspace/models/WorkingTestPCM/pcm");

    public static void main(final String[] args) {
        final GraphDatabaseService graph = new GraphDatabaseFactory().newEmbeddedDatabase(TestGeneric.DB_PATH);
        TestGeneric.registerShutdownHook(graph);
        System.out.println("Started db");

        final InitializeModelProviders modelProviderPlatform = new InitializeModelProviders(
                TestGeneric.PCM_MODELS_DIRECTORY);
        final RepositoryModelProvider repositoryModelProvider = modelProviderPlatform.getRepositoryModelProvider();
        final Repository repository = repositoryModelProvider.getModel();
        System.out.println("Loaded model");

        // System.out.println("Writing to db");
        // new GenericComponentProvider<>(graph).createComponent(repository);

        System.out.println("Reading id -> object from db");
        final OperationInterface inter = (OperationInterface) new GenericComponentProvider<>(graph)
                .readComponent("OperationInterface", "_j8RD0NYgEeWrM-HnT5f_ug");

        final GraphDatabaseService graph2 = new GraphDatabaseFactory().newEmbeddedDatabase(TestGeneric.DB_PATH2);
        TestGeneric.registerShutdownHook(graph2);

        System.out.println("Writing to db2");
        new GenericComponentProvider<>(graph2).createComponent(inter);

        // System.out.println("Reading type -> ids from db");
        // final List<String> ids = new
        // GenericComponentProvider<>(graph).readComponent("OperationInterface");
        // System.out.println(ids);

        graph.shutdown();
        System.out.print("Shut down db");
    }

    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }
}
