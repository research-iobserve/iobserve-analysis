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

import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.DataType;
import org.palladiosimulator.pcm.repository.Interface;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationRequiredRole;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.PrimitiveDataType;
import org.palladiosimulator.pcm.repository.PrimitiveTypeEnum;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.repository.RepositoryFactory;
import org.palladiosimulator.pcm.repository.RequiredRole;

/**
 * @author Reiner Jung
 *
 */
public final class RepositoryModelDataFactory {

    public static final String QUERY_COMPONENT = "org.mybookstore.queryInputComponent";
    public static final String PROCESSING_COMPONENT = "org.mybookstore.orderComponent.queryProcessingComponent";
    public static final String SEARCH_COMPONENT = "org.mybookstore.orderComponent.catologSearchComponent";
    public static final String PAYMENT_COMPONENT = "org.mybookstore.paymentComponent";
    public static final String ORDER_COMPONENT = "org.mybookstore.orderComponent";

    public static final String CATALOG_SEARCH_PROVIDED_ROLE = "catalogSearch";
    public static final String PAYMENT_PROVIDED_ROLE = "creditCardPayment";
    public static final String QUERY_PROVIDED_ROLE = "queryStringInput";

    public static final String PAYMENT_INTERFACE = "IPay";
    public static final String SEARCH_INTERFACE = "ISearch";
    public static final String QUERY_INTERFACE = "IQueryInput";

    public static final String QUERY_REQUIRED_ROLE = "input";
    public static final String PAYMENT_REQUIRED_ROLE = "payment";
    public static final String SEARCH_REQUIRED_ROLE = "search";

    /** factory. */
    private RepositoryModelDataFactory() {
        // nothing to do here
    }

    /**
     * Fine a component by name in the repository.
     *
     * @param repository
     *            repository model
     * @param name
     *            name of the component
     *
     * @return returns the component
     */
    public static RepositoryComponent findComponentByName(final Repository repository, final String name) {
        for (final RepositoryComponent component : repository.getComponents__Repository()) {
            if (component.getEntityName().equals(name)) {
                return component;
            }
        }

        return null;
    }

    /**
     * Create a repository with a strange bookstore example.
     *
     * @return the repository
     */
    public static Repository createBookstoreRepositoryModel() {
        final Repository repository = RepositoryFactory.eINSTANCE.createRepository();

        repository.setEntityName("MyBookstore");

        /** data types. */
        final PrimitiveDataType intDataType = RepositoryFactory.eINSTANCE.createPrimitiveDataType();
        intDataType.setType(PrimitiveTypeEnum.INT);
        final PrimitiveDataType stringDataType = RepositoryFactory.eINSTANCE.createPrimitiveDataType();
        intDataType.setType(PrimitiveTypeEnum.STRING);

        repository.getDataTypes__Repository().add(intDataType);
        repository.getDataTypes__Repository().add(stringDataType);

        /** interfaces. */
        final OperationInterface queryInterface = RepositoryModelDataFactory.createInterface(
                RepositoryModelDataFactory.QUERY_INTERFACE,
                RepositoryModelDataFactory.createSignature("getQuery", stringDataType));
        final OperationInterface searchInterface = RepositoryModelDataFactory.createInterface(
                RepositoryModelDataFactory.SEARCH_INTERFACE,
                RepositoryModelDataFactory.createSignature("getPrice", intDataType));
        final OperationInterface paymentInterface = RepositoryModelDataFactory.createInterface(
                RepositoryModelDataFactory.PAYMENT_INTERFACE,
                RepositoryModelDataFactory.createSignature("withdraw", intDataType));

        repository.getInterfaces__Repository().add(queryInterface);
        repository.getInterfaces__Repository().add(searchInterface);
        repository.getInterfaces__Repository().add(paymentInterface);

        /** roles. */
        final OperationProvidedRole providedInputRole = RepositoryModelDataFactory
                .createProvidedRole(RepositoryModelDataFactory.QUERY_PROVIDED_ROLE, queryInterface);
        final OperationProvidedRole providedPayRole = RepositoryModelDataFactory
                .createProvidedRole(RepositoryModelDataFactory.PAYMENT_PROVIDED_ROLE, paymentInterface);
        final OperationProvidedRole providedSearchRole = RepositoryModelDataFactory
                .createProvidedRole(RepositoryModelDataFactory.CATALOG_SEARCH_PROVIDED_ROLE, searchInterface);

        final OperationRequiredRole requiredInputRole = RepositoryModelDataFactory
                .createRequiredRole(RepositoryModelDataFactory.QUERY_REQUIRED_ROLE, queryInterface);
        final OperationRequiredRole requiredPayRole = RepositoryModelDataFactory
                .createRequiredRole(RepositoryModelDataFactory.PAYMENT_REQUIRED_ROLE, paymentInterface);
        final OperationRequiredRole requiredSearchRole = RepositoryModelDataFactory
                .createRequiredRole(RepositoryModelDataFactory.SEARCH_REQUIRED_ROLE, searchInterface);

        /** components. */
        repository.getComponents__Repository().add(RepositoryModelDataFactory
                .createBasicComponent(RepositoryModelDataFactory.QUERY_COMPONENT, requiredInputRole, null));
        repository.getComponents__Repository().add(RepositoryModelDataFactory
                .createBasicComponent(RepositoryModelDataFactory.PROCESSING_COMPONENT, requiredSearchRole, null));
        repository.getComponents__Repository().add(RepositoryModelDataFactory
                .createBasicComponent(RepositoryModelDataFactory.SEARCH_COMPONENT, null, providedSearchRole));
        repository.getComponents__Repository().add(RepositoryModelDataFactory
                .createBasicComponent(RepositoryModelDataFactory.PAYMENT_COMPONENT, null, providedPayRole));
        repository.getComponents__Repository().add(RepositoryModelDataFactory
                .createBasicComponent(RepositoryModelDataFactory.ORDER_COMPONENT, requiredPayRole, providedInputRole));

        return repository;
    }

    private static OperationSignature createSignature(final String name, final DataType dataType) {
        final OperationSignature signature = RepositoryFactory.eINSTANCE.createOperationSignature();
        signature.setEntityName(name);
        signature.setReturnType__OperationSignature(dataType);

        return signature;
    }

    private static OperationInterface createInterface(final String name, final OperationSignature signature) {
        final OperationInterface iface = RepositoryFactory.eINSTANCE.createOperationInterface();

        iface.setEntityName(name);
        iface.getSignatures__OperationInterface().add(signature);

        signature.setInterface__OperationSignature(iface);

        return iface;
    }

    private static BasicComponent createBasicComponent(final String name, final RequiredRole requiredRole,
            final ProvidedRole providedRole) {
        final BasicComponent component = RepositoryFactory.eINSTANCE.createBasicComponent();

        component.setEntityName(name);
        if (requiredRole != null) {
            component.getRequiredRoles_InterfaceRequiringEntity().add(requiredRole);
        }
        if (providedRole != null) {
            component.getProvidedRoles_InterfaceProvidingEntity().add(providedRole);
        }

        return component;
    }

    private static OperationRequiredRole createRequiredRole(final String name, final OperationInterface iface) {
        final OperationRequiredRole requiredRole = RepositoryFactory.eINSTANCE.createOperationRequiredRole();

        requiredRole.setEntityName(name);
        requiredRole.setRequiredInterface__OperationRequiredRole(iface);

        return requiredRole;
    }

    private static OperationProvidedRole createProvidedRole(final String name, final OperationInterface iface) {
        final OperationProvidedRole providedRole = RepositoryFactory.eINSTANCE.createOperationProvidedRole();

        providedRole.setEntityName(name);
        providedRole.setProvidedInterface__OperationProvidedRole(iface);

        return providedRole;
    }

    public static OperationProvidedRole findProvidedRole(final RepositoryComponent component, final String roleName) {
        for (final ProvidedRole role : component.getProvidedRoles_InterfaceProvidingEntity()) {
            if (role.getEntityName().equals(roleName)) {
                return (OperationProvidedRole) role;
            }
        }
        return null;
    }

    public static OperationRequiredRole findRequiredRole(final RepositoryComponent component, final String roleName) {
        for (final RequiredRole role : component.getRequiredRoles_InterfaceRequiringEntity()) {
            if (role.getEntityName().equals(roleName)) {
                return (OperationRequiredRole) role;
            }
        }
        return null;
    }

    public static Interface findInterfaceByName(final Repository repository, final String interfaceName) {
        for (final Interface iface : repository.getInterfaces__Repository()) {
            if (iface.getEntityName().equals(interfaceName)) {
                return iface;
            }
        }
        return null;
    }

}
