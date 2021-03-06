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
package org.iobserve.model.test.provider.neo4j;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.iobserve.model.persistence.DBException;
import org.iobserve.model.persistence.neo4j.ModelGraphFactory;
import org.iobserve.model.persistence.neo4j.Neo4JModelResource;
import org.iobserve.model.persistence.neo4j.NodeLookupException;
import org.iobserve.model.test.data.RepositoryModelDataFactory;
import org.iobserve.model.test.data.SystemDataFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.CompositionFactory;
import org.palladiosimulator.pcm.core.composition.CompositionPackage;
import org.palladiosimulator.pcm.core.composition.Connector;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationRequiredRole;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.system.SystemPackage;

/**
 * Test cases for the model provider using a System model.
 *
 * @author Lars Bluemke
 *
 * @since 0.0.2
 *
 */
@Ignore
public class SystemModelProviderTest extends AbstractEnityModelProviderTest<System> { // NOCS no
                                                                                      // constructor
                                                                                      // in test

    @Override
    @Before
    public void setUp() {
        this.prefix = this.getClass().getCanonicalName();
        this.testModel = this.system;
        this.ePackage = SystemPackage.eINSTANCE;
        this.clazz = System.class;
        this.eClass = SystemPackage.Literals.SYSTEM;
    }

    @Override
    @Test
    public void createThenReadByType() throws DBException {
        final Neo4JModelResource<System> resource = ModelProviderTestUtils.prepareResource("createThenReadByType",
                this.prefix, this.ePackage);

        resource.storeModelPartition(this.testModel);
        // TODO add actual test

    }

    @Override
    @Test
    public void createThenReadContaining() throws DBException {

    }

    @Override
    @Test
    public void createThenReadReferencing() throws DBException {
        final Neo4JModelResource<System> resource = ModelProviderTestUtils.prepareResource("createThenReadReferencing",
                this.prefix, this.ePackage);

        resource.storeModelPartition(this.testModel);

        final List<EObject> expectedReferencingComponents = new LinkedList<>();
        final List<EObject> readReferencingComponents;

        final Connector businessQueryConnector = SystemDataFactory.findConnector(this.system,
                SystemDataFactory.BUSINESS_QUERY_CONNECTOR);
        final Connector businessPayConnector = SystemDataFactory.findConnector(this.system,
                SystemDataFactory.BUSINESS_PAY_CONNECTOR);

        expectedReferencingComponents.add(businessQueryConnector);
        expectedReferencingComponents.add(businessPayConnector);

        final AssemblyContext context = SystemDataFactory.findAssemblyContext(this.system,
                SystemDataFactory.BUSINESS_ORDER_ASSEMBLY_CONTEXT);

        readReferencingComponents = resource.collectReferencingObjectsByTypeAndProperty(AssemblyContext.class,
                CompositionPackage.Literals.ASSEMBLY_CONTEXT, ModelGraphFactory.getIdentification(context));

        // Only the businessQueryInputConnector and the businessPayConnector are referencing the
        // businessOrderContext
        Assert.assertTrue(readReferencingComponents.size() == 2);

        Assert.assertTrue(this.equalityHelper.comparePartitions(expectedReferencingComponents,
                readReferencingComponents, readReferencingComponents.get(0).eClass()));

    }

    @Override
    @Test
    public void createThenUpdateThenReadUpdated() throws NodeLookupException, DBException {
        final Neo4JModelResource<System> resource = ModelProviderTestUtils
                .prepareResource("createThenUpdateThenReadUpdated", this.prefix, this.ePackage);

        resource.storeModelPartition(this.testModel);

        // Update the model by renaming and removing the business context
        this.testModel.setEntityName("MyVideoOnDemandService");

        final AssemblyContext businessQueryAssemblyContext = SystemDataFactory.findAssemblyContext(this.system,
                SystemDataFactory.BUSINESS_ORDER_ASSEMBLY_CONTEXT);
        final Connector businessQueryConnector = SystemDataFactory.findConnector(this.system,
                SystemDataFactory.BUSINESS_QUERY_CONNECTOR);
        final Connector businessPayConnector = SystemDataFactory.findConnector(this.system,
                SystemDataFactory.BUSINESS_PAY_CONNECTOR);

        this.testModel.getAssemblyContexts__ComposedStructure().remove(businessQueryAssemblyContext);
        this.testModel.getConnectors__ComposedStructure().remove(businessQueryConnector);
        this.testModel.getConnectors__ComposedStructure().remove(businessPayConnector);

        // Replace the business context by a context for groups of people placing an order
        final AssemblyContext sharedOrderContext = CompositionFactory.eINSTANCE.createAssemblyContext();
        sharedOrderContext.setEntityName("sharedOrderContext_org.myvideoondemandservice.orderComponent");
        sharedOrderContext.setEncapsulatedComponent__AssemblyContext(RepositoryModelDataFactory
                .findComponentByName(this.repository, RepositoryModelDataFactory.ORDER_COMPONENT));

        final AssemblyContext queryInputAssemblyContext = SystemDataFactory.findAssemblyContext(this.system,
                SystemDataFactory.QUERY_ASSEMBLY_CONTEXT);
        final OperationProvidedRole providedInputRole = RepositoryModelDataFactory.findProvidedRole(
                sharedOrderContext.getEncapsulatedComponent__AssemblyContext(),
                RepositoryModelDataFactory.QUERY_PROVIDED_ROLE);
        final OperationRequiredRole requiredInputRole = RepositoryModelDataFactory.findRequiredRole(
                queryInputAssemblyContext.getEncapsulatedComponent__AssemblyContext(),
                RepositoryModelDataFactory.QUERY_REQUIRED_ROLE);

        final AssemblyConnector sharedQueryInputConnector = CompositionFactory.eINSTANCE.createAssemblyConnector();
        sharedQueryInputConnector.setEntityName("sharedQueryInput");
        sharedQueryInputConnector.setProvidedRole_AssemblyConnector(providedInputRole);
        sharedQueryInputConnector.setRequiredRole_AssemblyConnector(requiredInputRole);
        sharedQueryInputConnector.setProvidingAssemblyContext_AssemblyConnector(sharedOrderContext);
        sharedQueryInputConnector.setRequiringAssemblyContext_AssemblyConnector(queryInputAssemblyContext);

        final AssemblyContext paymentAssemblyContext = SystemDataFactory.findAssemblyContext(this.system,
                SystemDataFactory.PAYMENT_ASSEMBLY_CONTEXT);
        final OperationProvidedRole providedPayRole = RepositoryModelDataFactory.findProvidedRole(
                paymentAssemblyContext.getEncapsulatedComponent__AssemblyContext(),
                RepositoryModelDataFactory.PAYMENT_PROVIDED_ROLE);
        final OperationRequiredRole requiredPayRole = RepositoryModelDataFactory.findRequiredRole(
                sharedOrderContext.getEncapsulatedComponent__AssemblyContext(),
                RepositoryModelDataFactory.PAYMENT_REQUIRED_ROLE);

        final AssemblyConnector sharedPayConnector = CompositionFactory.eINSTANCE.createAssemblyConnector();
        sharedPayConnector.setEntityName("sharedPayment");

        sharedPayConnector.setProvidedRole_AssemblyConnector(providedPayRole);
        sharedPayConnector.setRequiredRole_AssemblyConnector(requiredPayRole);
        sharedPayConnector.setProvidingAssemblyContext_AssemblyConnector(paymentAssemblyContext);
        sharedPayConnector.setRequiringAssemblyContext_AssemblyConnector(sharedOrderContext);

        this.testModel.getAssemblyContexts__ComposedStructure().add(sharedOrderContext);
        this.testModel.getConnectors__ComposedStructure().add(sharedQueryInputConnector);
        this.testModel.getConnectors__ComposedStructure().add(sharedPayConnector);

        resource.updatePartition(this.testModel);

        final System readModel = resource.getModelRootNode(System.class, this.eClass);

        Assert.assertTrue(this.equalityHelper.comparePartition(this.testModel, readModel, readModel.eClass()));
    }

    @Override
    @Test
    public void createThenDeleteObject() throws DBException {

    }

    @Override
    @Test
    public void createThenDeleteObjectAndDatatypes() throws DBException {

    }

    /**
     * Writes a model to the graph, reads it from the graph using
     * {@link ModelProvider#findObjectsByTypeAndName(Class, String)} and asserts that it is equal to
     * the one written to the graph.
     *
     * @throws DBException
     */
    @Test
    public final void createThenReadByName() throws DBException {
        final Neo4JModelResource<System> resource = ModelProviderTestUtils.prepareResource("createThenReadByName",
                this.prefix, this.ePackage);

        resource.storeModelPartition(this.testModel);

        final List<System> readModels = resource.findObjectsByTypeAndProperty(this.clazz, this.eClass, "entityName",
                this.testModel.getEntityName());

        for (final System readModel : readModels) {
            Assert.assertTrue(this.equalityHelper.comparePartition(this.testModel, readModel, readModel.eClass()));
        }

        resource.getGraphDatabaseService().shutdown();
    }

}
