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
package org.iobserve.analysis.modelneo4j.test;

import java.io.File;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.modelneo4j.Graph;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.junit.Assert;
import org.junit.Test;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.seff.ResourceDemandingSEFF;

public class CreateReadTest {

    private final File pcmModelsDirectory = new File(
            "/Users/LarsBlumke/Documents/CAU/Masterprojekt/iObserveWorkspace/models/WorkingTestPCM/pcm");
    private final File pcmModelsNeo4jDirectory = new File("/Users/LarsBlumke/Desktop/neo4jTestDb");

    private final InitializeModelProviders initializer = new InitializeModelProviders(this.pcmModelsDirectory);
    private final Graph graph = new Graph(this.pcmModelsNeo4jDirectory);
    private final Neo4jEqualityHelper equalityHelper = new Neo4jEqualityHelper();

    // @Test
    public void testAllocationModel() {
        final Allocation allocationModel = this.initializer.getAllocationModelProvider().getModel();

        final ModelProvider<Allocation> modelProvider = new ModelProvider<>(this.graph);
        modelProvider.clearGraph();
        modelProvider.createComponent(allocationModel);

        final Allocation allocationModel2 = modelProvider.readComponentById(Allocation.class, allocationModel.getId());

        Assert.assertTrue(this.equalityHelper.equals(allocationModel, allocationModel2));
    }

    // @Test
    public void testRepositoryModel() {
        final Repository repositoryModel = this.initializer.getRepositoryModelProvider().getModel();

        final ModelProvider<Repository> modelProvider = new ModelProvider<>(this.graph);
        modelProvider.clearGraph();
        modelProvider.createComponent(repositoryModel);

        final Repository repositoryModel2 = modelProvider.readComponentById(Repository.class, repositoryModel.getId());

        Assert.assertTrue(this.equalityHelper.equals(repositoryModel, repositoryModel2));
    }

    // @Test
    public void test() {
        final OperationInterface inter = (OperationInterface) this.initializer.getRepositoryModelProvider().getModel()
                .getInterfaces__Repository().get(0);
        final ModelProvider<OperationInterface> modelProvider = new ModelProvider<>(this.graph);
        modelProvider.clearGraph();
        modelProvider.createComponent(inter);

        final OperationInterface inter2 = modelProvider.readComponentById(OperationInterface.class, inter.getId());

        for (final OperationSignature sig : inter.getSignatures__OperationInterface()) {
            System.out.println(sig.getEntityName() + " " + sig.getId());
        }

        System.out.println("----------------------------");

        for (final OperationSignature sig : inter2.getSignatures__OperationInterface()) {
            System.out.println(sig.getEntityName() + " " + sig.getId());
        }

    }

    @Test
    public void test2() {
        final BasicComponent comp = (BasicComponent) this.initializer.getRepositoryModelProvider().getModel()
                .getComponents__Repository().get(0);
        final ResourceDemandingSEFF seff = (ResourceDemandingSEFF) comp.getServiceEffectSpecifications__BasicComponent()
                .get(0);
        final ModelProvider<ResourceDemandingSEFF> modelProvider = new ModelProvider<>(this.graph);
        modelProvider.clearGraph();
        modelProvider.createComponent(seff);

        final ResourceDemandingSEFF seff2 = modelProvider.readComponentById(ResourceDemandingSEFF.class, seff.getId());

        final TreeIterator<EObject> iter = seff.eAllContents();
        while (iter.hasNext()) {
            final EObject o = iter.next();
            System.out.println(o);
        }

        System.out.println("----------------------------");

        final TreeIterator<EObject> iter2 = seff2.eAllContents();
        while (iter2.hasNext()) {
            final EObject o = iter2.next();
            System.out.println(o);
        }

        Assert.assertTrue(this.equalityHelper.equals(seff, seff2));
    }

}
