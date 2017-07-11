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

import java.io.File;

import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.model.RepositoryModelProvider;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.Parameter;
import org.palladiosimulator.pcm.repository.Repository;

/**
 * An ugly test class for debugging
 *
 * @author Lars Bluemke
 *
 */
public class TestGeneric {
    private static final File DB_PATH = new File("/Users/LarsBlumke/Desktop/neo4jdb");
    private static final File DB_PATH2 = new File("/Users/LarsBlumke/Desktop/neo4jdb2");
    private static final File PCM_MODELS_DIRECTORY = new File(
            "/Users/LarsBlumke/Documents/CAU/Masterprojekt/iObserveWorkspace/models/WorkingTestPCM/pcm");

    public static void main(final String[] args) {
        final Graph graph = new Graph(TestGeneric.DB_PATH);
        final Graph graph2 = new Graph(TestGeneric.DB_PATH2);
        System.out.println("Started DBs");

        final InitializeModelProviders modelProviderPlatform = new InitializeModelProviders(
                TestGeneric.PCM_MODELS_DIRECTORY);

        /** Load repository model */
        final RepositoryModelProvider repositoryModelProvider = modelProviderPlatform.getRepositoryModelProvider();
        final Repository repositoryModel = repositoryModelProvider.getModel();
        //
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

        /** Clear graph */
        System.out.println("Clearing graphs");
        new ModelProvider<>(graph).clearGraph();
        new ModelProvider<>(graph2).clearGraph();

        /** Write to DB1 */
        System.out.println("Writing to DB1");
        new ModelProvider<>(graph).createComponent(repositoryModel.getInterfaces__Repository().get(0));
        new ModelProvider<>(graph2).createComponent(repositoryModel.getInterfaces__Repository().get(0));

        /** Reading (id -> object) from DB1 */
        // System.out.println("Reading (id -> object) from DB1");
        // final Repository repositoryModel2 = new
        // ModelProvider<Repository>(graph).readComponentById(Repository.class,
        // repositoryModel.getId());
        final OperationInterface inter = new ModelProvider<OperationInterface>(graph).readOnlyComponentById(
                OperationInterface.class, repositoryModel.getInterfaces__Repository().get(0).getId());
        // final UsageModel usageModel2 = new
        // ModelProvider<UsageModel>(graph).readRootComponent(UsageModel.class);

        /** Reading (type -> ids) from DB1 */
        // System.out.println("Reading (type -> ids) from DB1");
        // final List<String> ids = new
        // ModelProvider<OperationInterface>(graph).readComponent(OperationInterface.class);
        // System.out.println(ids);

        /** Updating in DB1 */
        System.out.println("Updating DB1");
        inter.setEntityName("UPDATED" + inter.getEntityName());
        final OperationSignature sig = inter.getSignatures__OperationInterface().get(0);
        sig.setEntityName("UPDATED" + sig.getEntityName());
        // inter.getSignatures__OperationInterface().remove(1);
        final Parameter param = (Parameter) ModelProviderUtil.instantiateEObject("Parameter");
        param.setParameterName("ADDEDParam");
        sig.getParameters__OperationSignature().add(param);
        new ModelProvider<OperationInterface>(graph).updateComponent2(OperationInterface.class, inter);

        /** Writing to DB2 */
        // System.out.println("Writing to DB2");
        // new
        // ModelProvider<>(graph2).createComponent(repositoryModel.getInterfaces__Repository().get(0));
        // new ModelProvider<>(graph2).createComponent(usageModel2);

        /** Reading from DB2 */
        // System.out.println("Reading from DB2");
        // final List<OperationInterface> inters = new ModelProvider<OperationInterface>(graph2)
        // .readComponentByName(OperationInterface.class, "ITradingEnterprise");
        // System.out.println(inters.size() + " " + inters);
        // final List<OperationSignature> sigs = new ModelProvider<OperationSignature>(graph2)
        // .readComponentByName(OperationSignature.class, "getId");
        // System.out.println(sigs.size() + " " + sigs);

        /** Deleting from DB2 */
        // System.out.println("Deleting from DB2");
        // new ModelProvider<Repository>(graph2).deleteComponent(Repository.class,
        // repositoryModel.getId());
        // new
        // ModelProvider<OperationInterface>(graph2).deleteComponentAndDatatypes(OperationInterface.class,
        // inter.getId());

        /** Test old provider */
        // System.out.println("Create old provider");
        // final org.iobserve.analysis.modelneo4j.legacyprovider.RepositoryModelProvider
        // repositoryModelProvider2 = new
        // org.iobserve.analysis.modelneo4j.legacyprovider.RepositoryModelProvider(
        // TestGeneric.DB_PATH);
        // System.out.println("Save with old provider");
        // repositoryModelProvider2.save();
        // System.out.println("Load with old provider");
        // repositoryModelProvider2.loadModel();
        // System.out.println("Write to DB2");
        // new ModelProvider<>(graph2).createComponent(repositoryModelProvider2.getModel());

        /** Test parallel access */
        // System.out.println("Starting parallel access");
        // new TestThread(graph, repositoryModel).start();
        // new TestThread(graph, repositoryModel).start();

        System.out.println("Shut down DBs");

    }

}
