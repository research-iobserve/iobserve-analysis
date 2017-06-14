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
import java.util.List;

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
        final GraphDatabaseService graph2 = new GraphDatabaseFactory().newEmbeddedDatabase(TestGeneric.DB_PATH2);
        TestGeneric.registerShutdownHook(graph2);
        System.out.println("Started DBs");

        final InitializeModelProviders modelProviderPlatform = new InitializeModelProviders(
                TestGeneric.PCM_MODELS_DIRECTORY);

        /** Load repository model */
        final RepositoryModelProvider repositoryModelProvider = modelProviderPlatform.getRepositoryModelProvider();
        final Repository repositoryModel = repositoryModelProvider.getModel();

        /** Load usage model */
        // final UsageModelProvider usageModelProvider =
        // modelProviderPlatform.getUsageModelProvider();
        // final UsageModel usageModel = usageModelProvider.getModel();

        /** Load system model */
        // final SystemModelProvider systemModelProvider =
        // modelProviderPlatform.getSystemModelProvider();
        // final org.palladiosimulator.pcm.system.System systemModel =
        // systemModelProvider.getModel();

        /** Load resource environment model */
        // final ResourceEnvironmentModelProvider resourceEnvironmentModelProvider =
        // modelProviderPlatform
        // .getResourceEnvironmentModelProvider();
        // final ResourceEnvironment resourceEnvironmentModel =
        // resourceEnvironmentModelProvider.getModel();

        /** Load allocation model */
        // final AllocationModelProvider allocationModelProvider =
        // modelProviderPlatform.getAllocationModelProvider();
        // final Allocation allocationModel = allocationModelProvider.getModel();

        /*************************************************************************************************/

        /** Write to DB1 */
        System.out.println("Writing to DB1");
        new ModelProvider<>(graph).createComponent(repositoryModel);

        /** Reading (id -> object) from DB1 */
        // System.out.println("Reading (id -> object) from DB1");
        // final Repository repositoryModel2 = (Repository) new ModelProvider<Repository>(graph)
        // .readComponent(Repository.class, repositoryModel.getId());

        /** Reading (type -> ids) from DB1 */
        System.out.println("Reading (type -> ids) from DB1");
        final List<String> ids = new ModelProvider<OperationInterface>(graph).readComponent(OperationInterface.class);
        System.out.println(ids);

        /** Writing to DB2 */
        // System.out.println("Writing to DB2");
        // new ModelProvider<>(graph2).createComponent(repositoryModel2);

        /** Deleting from DB2 */
        // System.out.println("Deleting from DB2");
        // new ModelProvider<Repository>(graph).deleteComponent(Repository.class,
        // repositoryModel.getId());
        // new ModelProvider<Repository>(graph2).deleteComponent(Repository.class,
        // repositoryModel.getId());

        graph.shutdown();
        graph2.shutdown();
        System.out.print("Shut down DBs");
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
