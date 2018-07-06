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
package org.iobserve.model.test.data;

import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.CompositionFactory;
import org.palladiosimulator.pcm.core.composition.Connector;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationRequiredRole;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.repository.RequiredRole;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.system.SystemFactory;

/**
 * System model.
 *
 * @author Reiner Jung
 *
 */
public final class SystemDataFactory {

    public static final String QUERY_ASSEMBLY_CONTEXT = "queryInputContext_"
            + RepositoryModelDataFactory.QUERY_COMPONENT;
    public static final String BUSINESS_ORDER_ASSEMBLY_CONTEXT = "businessOrderContext_"
            + RepositoryModelDataFactory.ORDER_COMPONENT;
    public static final String PRIVATE_ORDER_ASSMEBLY_CONTEXT = "privateOrderContext_"
            + RepositoryModelDataFactory.ORDER_COMPONENT;
    public static final String PAYMENT_ASSEMBLY_CONTEXT = "paymentContext_"
            + RepositoryModelDataFactory.PAYMENT_COMPONENT;

    public static final String BUSINESS_QUERY_CONNECTOR = "businessQueryInput";
    public static final String PRIVATE_QUERY_CONNECTOR = "privateQueryInput";
    public static final String BUSINESS_PAY_CONNECTOR = "businessPayment";
    public static final String PRIVATE_PAY_CONNECTOR = "privatePayment";

    private SystemDataFactory() {
        // private empty constructor for factory
    }

    /**
     * Create a {@link System} model based on the given {@link Repository} model.
     *
     * @param repository
     *            the repository model
     * @return returns a {@link System} model
     */
    public static System createSystem(final Repository repository) {
        final System system = SystemFactory.eINSTANCE.createSystem();
        system.setEntityName("MyBookstore");

        /** assembly contexts. */
        final AssemblyContext queryInputAssemblyContext = SystemDataFactory.createAssemblyContext(repository,
                SystemDataFactory.QUERY_ASSEMBLY_CONTEXT, RepositoryModelDataFactory.QUERY_COMPONENT);
        final AssemblyContext businessOrderAssemblyContext = SystemDataFactory.createAssemblyContext(repository,
                SystemDataFactory.BUSINESS_ORDER_ASSEMBLY_CONTEXT, RepositoryModelDataFactory.ORDER_COMPONENT);
        final AssemblyContext privateOrderAssemblyContext = SystemDataFactory.createAssemblyContext(repository,
                SystemDataFactory.PRIVATE_ORDER_ASSMEBLY_CONTEXT, RepositoryModelDataFactory.ORDER_COMPONENT);
        final AssemblyContext paymentAssemblyContext = SystemDataFactory.createAssemblyContext(repository,
                SystemDataFactory.PAYMENT_ASSEMBLY_CONTEXT, RepositoryModelDataFactory.PAYMENT_COMPONENT);

        system.getAssemblyContexts__ComposedStructure().add(queryInputAssemblyContext);
        system.getAssemblyContexts__ComposedStructure().add(businessOrderAssemblyContext);
        system.getAssemblyContexts__ComposedStructure().add(paymentAssemblyContext);
        system.getAssemblyContexts__ComposedStructure().add(privateOrderAssemblyContext);

        /** connectors. */
        final AssemblyConnector businessQueryInputConnector = SystemDataFactory.createAssemblyConnector(
                SystemDataFactory.BUSINESS_QUERY_CONNECTOR, businessOrderAssemblyContext, queryInputAssemblyContext);

        final AssemblyConnector privateQueryInputConnector = SystemDataFactory.createAssemblyConnector(
                SystemDataFactory.PRIVATE_QUERY_CONNECTOR, privateOrderAssemblyContext, queryInputAssemblyContext);
        final AssemblyConnector businessPayConnector = SystemDataFactory.createAssemblyConnector(
                SystemDataFactory.BUSINESS_PAY_CONNECTOR, paymentAssemblyContext, businessOrderAssemblyContext);
        final AssemblyConnector privatePayConnector = SystemDataFactory.createAssemblyConnector(
                SystemDataFactory.PRIVATE_PAY_CONNECTOR, paymentAssemblyContext, privateOrderAssemblyContext);

        system.getConnectors__ComposedStructure().add(businessQueryInputConnector);
        system.getConnectors__ComposedStructure().add(privateQueryInputConnector);
        system.getConnectors__ComposedStructure().add(businessPayConnector);
        system.getConnectors__ComposedStructure().add(privatePayConnector);

        return system;
    }

    /**
     * Create an {@link AssemblyConnector} which links two {@link AssemblyContext}.
     *
     * @param name
     *            name of the connector
     * @param providingContext
     *            providing {@link AssemblyContext}
     * @param requiringContext
     *            requiring {@link AssemblyContext}
     * @return returns a new {@link AssemblyConnector}
     */
    public static AssemblyConnector createAssemblyConnector(final String name, final AssemblyContext providingContext,
            final AssemblyContext requiringContext) {
        final AssemblyConnector connector = CompositionFactory.eINSTANCE.createAssemblyConnector();

        final ProvidedRole providedRole = providingContext.getEncapsulatedComponent__AssemblyContext()
                .getProvidedRoles_InterfaceProvidingEntity().get(0);
        final RequiredRole requiredRole = requiringContext.getEncapsulatedComponent__AssemblyContext()
                .getRequiredRoles_InterfaceRequiringEntity().get(0);

        connector.setEntityName(name);
        connector.setProvidedRole_AssemblyConnector((OperationProvidedRole) providedRole);
        connector.setRequiredRole_AssemblyConnector((OperationRequiredRole) requiredRole);
        connector.setProvidingAssemblyContext_AssemblyConnector(providingContext);
        connector.setRequiringAssemblyContext_AssemblyConnector(requiringContext);

        return connector;
    }

    private static AssemblyContext createAssemblyContext(final Repository repository, final String contextName,
            final String componentName) {
        final AssemblyContext assemblyContext = CompositionFactory.eINSTANCE.createAssemblyContext();
        final RepositoryComponent component = RepositoryModelDataFactory.findComponentByName(repository, componentName);
        assemblyContext.setEncapsulatedComponent__AssemblyContext(component);

        assemblyContext.setEntityName(contextName);

        return assemblyContext;
    }

    /**
     * Find an {@link AssemblyContext} in a {@link System} model.
     *
     * @param system
     *            the {@link System} model
     * @param assemblyContextName
     *            the name of the {@link AssemblyContext}
     * @return returns an {@link AssemblyContext}
     */
    public static AssemblyContext findAssemblyContext(final System system, final String assemblyContextName) {
        for (final AssemblyContext context : system.getAssemblyContexts__ComposedStructure()) {
            if (context.getEntityName().equals(assemblyContextName)) {
                return context;
            }
        }
        return null;
    }

    /**
     * Find a {@link Connector} in a {@link System} model.
     *
     * @param system
     *            the {@link System} model
     * @param connectorName
     *            the {@link Connector} name
     * @return returns an {@link Connector} on success or null
     */
    public static Connector findConnector(final System system, final String connectorName) {
        for (final Connector connector : system.getConnectors__ComposedStructure()) {
            if (connector.getEntityName().equals(connectorName)) {
                return connector;
            }
        }
        return null;

    }

}
