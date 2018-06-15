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
package org.iobserve.model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.iobserve.model.privacy.EDataPrivacyLevel;
import org.iobserve.model.privacy.EISOCode;
import org.iobserve.model.privacy.GeoLocation;
import org.iobserve.model.privacy.ParameterPrivacy;
import org.iobserve.model.privacy.PrivacyFactory;
import org.iobserve.model.privacy.PrivacyModel;
import org.iobserve.model.privacy.ReturnTypePrivacy;
import org.palladiosimulator.pcm.repository.Interface;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.Parameter;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

/**
 * @author Reiner Jung
 *
 */
public final class CreatePrivacyMain {

    private static final Map<String, EISOCode> ISO_CODE_MAPS = new HashMap<>();
    private static final Map<String, Map<String, OperationSignaturePrivacy>> PRIVACY_LEVEL_MAPS = new HashMap<>();

    private CreatePrivacyMain() {

    }

    /**
     * @param args
     *            arguments
     */
    public static void main(final String[] args) {

        CreatePrivacyMain.createIsoCodes();
        CreatePrivacyMain.createPrivacyAnnotations();

        final String pcmDirectory = "/home/reiner/Projects/iObserve/experiments/distributed-jpetstore-experiment/pcm/JPetStore";
        try {
            final ModelImporter modelHandler = new ModelImporter(new File(pcmDirectory));

            final Repository repository = modelHandler.getRepositoryModel();
            final ResourceEnvironment environment = modelHandler.getResourceEnvironmentModel();

            final PrivacyModel privacyModel = PrivacyFactory.eINSTANCE.createPrivacyModel();

            CreatePrivacyMain.addGeoLocations(privacyModel, environment);
            CreatePrivacyMain.addPrivacyAnnotations(privacyModel, repository);

            final URI outputURI = URI.createFileURI("/home/reiner/privacyTestModel.privacy");
            CreatePrivacyMain.save(privacyModel, outputURI);
        } catch (final IOException e) {
            java.lang.System.err.println("Canot load all models " + e.getLocalizedMessage());
        }
    }

    private static void createPrivacyAnnotations() {
        /** IAccountService. */
        final Map<String, OperationSignaturePrivacy> accountServiceMap = new HashMap<>();
        CreatePrivacyMain.PRIVACY_LEVEL_MAPS.put("IAccountService", accountServiceMap);

        final Map<String, EDataPrivacyLevel> userRequestParameter = new HashMap<>();
        userRequestParameter.put("username", EDataPrivacyLevel.PERSONAL);
        userRequestParameter.put("password", EDataPrivacyLevel.PERSONAL);
        accountServiceMap.put("userRequest",
                new OperationSignaturePrivacy(EDataPrivacyLevel.PERSONAL, userRequestParameter));

        final Map<String, EDataPrivacyLevel> insertAccountParameter = new HashMap<>();
        insertAccountParameter.put("account", EDataPrivacyLevel.PERSONAL);
        accountServiceMap.put("insertAccount",
                new OperationSignaturePrivacy(EDataPrivacyLevel.PERSONAL, insertAccountParameter));

        final Map<String, EDataPrivacyLevel> updateAccountParameter = new HashMap<>();
        updateAccountParameter.put("account", EDataPrivacyLevel.PERSONAL);
        accountServiceMap.put("updateAccount",
                new OperationSignaturePrivacy(EDataPrivacyLevel.PERSONAL, updateAccountParameter));

        /** ICatalogService. */
        final Map<String, OperationSignaturePrivacy> catalogServiceMap = new HashMap<>();
        CreatePrivacyMain.PRIVACY_LEVEL_MAPS.put("ICatalogService", catalogServiceMap);

        final Map<String, EDataPrivacyLevel> getCategoryListParameter = new HashMap<>();
        catalogServiceMap.put("getCategoryList",
                new OperationSignaturePrivacy(EDataPrivacyLevel.ANONYMOUS, getCategoryListParameter));

        final Map<String, EDataPrivacyLevel> getCategoryByIdParameter = new HashMap<>();
        getCategoryByIdParameter.put("id", EDataPrivacyLevel.ANONYMOUS);
        catalogServiceMap.put("getCategoryById",
                new OperationSignaturePrivacy(EDataPrivacyLevel.ANONYMOUS, getCategoryByIdParameter));

        final Map<String, EDataPrivacyLevel> itemInStockParameter = new HashMap<>();
        itemInStockParameter.put("id", EDataPrivacyLevel.ANONYMOUS);
        catalogServiceMap.put("itemInStock",
                new OperationSignaturePrivacy(EDataPrivacyLevel.ANONYMOUS, itemInStockParameter));

        final Map<String, EDataPrivacyLevel> itemListByProductParameter = new HashMap<>();
        itemListByProductParameter.put("productId", EDataPrivacyLevel.ANONYMOUS);
        catalogServiceMap.put("itemListByProduct",
                new OperationSignaturePrivacy(EDataPrivacyLevel.ANONYMOUS, itemListByProductParameter));

        final Map<String, EDataPrivacyLevel> itemByIdParameter = new HashMap<>();
        itemByIdParameter.put("itemId", EDataPrivacyLevel.ANONYMOUS);
        catalogServiceMap.put("itemById",
                new OperationSignaturePrivacy(EDataPrivacyLevel.ANONYMOUS, itemByIdParameter));

        final Map<String, EDataPrivacyLevel> productListByCategoryParameter = new HashMap<>();
        productListByCategoryParameter.put("categoryId", EDataPrivacyLevel.ANONYMOUS);
        catalogServiceMap.put("productListByCategory",
                new OperationSignaturePrivacy(EDataPrivacyLevel.ANONYMOUS, productListByCategoryParameter));

        final Map<String, EDataPrivacyLevel> productByIdParameter = new HashMap<>();
        productByIdParameter.put("productId", EDataPrivacyLevel.ANONYMOUS);
        catalogServiceMap.put("productById",
                new OperationSignaturePrivacy(EDataPrivacyLevel.ANONYMOUS, productByIdParameter));

        final Map<String, EDataPrivacyLevel> searchProductListParameter = new HashMap<>();
        searchProductListParameter.put("keywords", EDataPrivacyLevel.ANONYMOUS);
        catalogServiceMap.put("searchProductList",
                new OperationSignaturePrivacy(EDataPrivacyLevel.ANONYMOUS, searchProductListParameter));

        /** OrderService. */
        final Map<String, OperationSignaturePrivacy> orderServiceMap = new HashMap<>();
        CreatePrivacyMain.PRIVACY_LEVEL_MAPS.put("IOrderService", orderServiceMap);

        final Map<String, EDataPrivacyLevel> insertOrderParameter = new HashMap<>();
        insertOrderParameter.put("order", EDataPrivacyLevel.PERSONAL);
        orderServiceMap.put("insertOrder",
                new OperationSignaturePrivacy(EDataPrivacyLevel.PERSONAL, insertOrderParameter));

        final Map<String, EDataPrivacyLevel> nextIdParameter = new HashMap<>();
        nextIdParameter.put("name", EDataPrivacyLevel.ANONYMOUS);
        orderServiceMap.put("nextId", new OperationSignaturePrivacy(EDataPrivacyLevel.ANONYMOUS, nextIdParameter));

        final Map<String, EDataPrivacyLevel> orderByIdParameter = new HashMap<>();
        orderByIdParameter.put("oderId", EDataPrivacyLevel.DEPERSONALIZED);
        orderServiceMap.put("orderById", new OperationSignaturePrivacy(EDataPrivacyLevel.PERSONAL, orderByIdParameter));

        final Map<String, EDataPrivacyLevel> odersByUsernameParameter = new HashMap<>();
        odersByUsernameParameter.put("username", EDataPrivacyLevel.PERSONAL);
        orderServiceMap.put("ordersByUsername",
                new OperationSignaturePrivacy(EDataPrivacyLevel.PERSONAL, odersByUsernameParameter));

        /** IAccountDatabase. */
        final Map<String, OperationSignaturePrivacy> accountDatabaseMap = new HashMap<>();
        CreatePrivacyMain.PRIVACY_LEVEL_MAPS.put("IAccountDatabase", accountDatabaseMap);

        final Map<String, EDataPrivacyLevel> applySQLStatement = new HashMap<>();
        applySQLStatement.put("sqlStatement", EDataPrivacyLevel.PERSONAL);
        accountDatabaseMap.put("applySQLStatement",
                new OperationSignaturePrivacy(EDataPrivacyLevel.PERSONAL, applySQLStatement));

        /** IOrderDatabase. */
        final Map<String, OperationSignaturePrivacy> orderDatabaseMap = new HashMap<>();
        CreatePrivacyMain.PRIVACY_LEVEL_MAPS.put("IOrderDatabase", orderDatabaseMap);
        orderDatabaseMap.put("applySQLStatement",
                new OperationSignaturePrivacy(EDataPrivacyLevel.PERSONAL, applySQLStatement));

        /** ICatalogDatabase. */
        final Map<String, OperationSignaturePrivacy> catalogDatabaseMap = new HashMap<>();
        CreatePrivacyMain.PRIVACY_LEVEL_MAPS.put("ICatalogDatabase", catalogDatabaseMap);
        final Map<String, EDataPrivacyLevel> publicApplySQLStatement = new HashMap<>();
        publicApplySQLStatement.put("sqlStatement", EDataPrivacyLevel.ANONYMOUS);
        catalogDatabaseMap.put("applySQLStatement",
                new OperationSignaturePrivacy(EDataPrivacyLevel.ANONYMOUS, publicApplySQLStatement));
    }

    private static void createIsoCodes() {
        CreatePrivacyMain.ISO_CODE_MAPS.put("Frontend", EISOCode.GERMANY);
        CreatePrivacyMain.ISO_CODE_MAPS.put("Account", EISOCode.USA);
        CreatePrivacyMain.ISO_CODE_MAPS.put("Catalog", EISOCode.GERMANY);
        CreatePrivacyMain.ISO_CODE_MAPS.put("Order", EISOCode.GERMANY);
        CreatePrivacyMain.ISO_CODE_MAPS.put("Database", EISOCode.GERMANY);
    }

    private static void addGeoLocations(final PrivacyModel privacyModel, final ResourceEnvironment environment) {
        for (final ResourceContainer container : environment.getResourceContainer_ResourceEnvironment()) {
            java.lang.System.out.printf("have container %s\n", container.getEntityName());

            final GeoLocation location = PrivacyFactory.eINSTANCE.createGeoLocation();

            location.setIsocode(CreatePrivacyMain.ISO_CODE_MAPS.get(container.getEntityName()));
            location.setResourceContainer(container);

            privacyModel.getResourceContainerLocations().add(location);
        }
    }

    private static void addPrivacyAnnotations(final PrivacyModel privacyModel, final Repository repository) {
        for (final Interface iface : repository.getInterfaces__Repository()) {
            if (iface instanceof OperationInterface) {
                java.lang.System.out.printf("interface %s\n", iface.getEntityName());
                final OperationInterface operationInterface = (OperationInterface) iface;
                final Map<String, OperationSignaturePrivacy> ifacePrivacy = CreatePrivacyMain.PRIVACY_LEVEL_MAPS
                        .get(iface.getEntityName());
                for (final OperationSignature signature : operationInterface.getSignatures__OperationInterface()) {
                    java.lang.System.out.printf("\tsignature %s\n", signature.getEntityName());
                    final OperationSignaturePrivacy signaturePrivacy = ifacePrivacy.get(signature.getEntityName());

                    if (signature.getReturnType__OperationSignature() != null) {
                        final ReturnTypePrivacy returnTypePrivacy = PrivacyFactory.eINSTANCE.createReturnTypePrivacy();
                        returnTypePrivacy.setLevel(signaturePrivacy.getReturnTypePrivacy());
                        returnTypePrivacy.setOperationSignature(signature);

                        privacyModel.getPrivacyLevels().add(returnTypePrivacy);
                    }

                    for (final Parameter parameter : signature.getParameters__OperationSignature()) {
                        java.lang.System.out.printf("\t\tparameter %s\n", parameter.getParameterName());

                        final ParameterPrivacy parameterPrivacy = PrivacyFactory.eINSTANCE.createParameterPrivacy();
                        parameterPrivacy
                                .setLevel(signaturePrivacy.getParameterPrivacy().get(parameter.getParameterName()));
                        parameterPrivacy.setParameter(parameter);

                        privacyModel.getPrivacyLevels().add(parameterPrivacy);
                    }
                }
            }
        }
    }

    private static void save(final PrivacyModel privacyModel, final URI writeModelURI) {
        final Resource.Factory.Registry resourceRegistry = Resource.Factory.Registry.INSTANCE;
        final Map<String, Object> map = resourceRegistry.getExtensionToFactoryMap();
        map.put("*", new XMIResourceFactoryImpl());

        final ResourceSet resourceSet = new ResourceSetImpl();
        resourceSet.setResourceFactoryRegistry(resourceRegistry);

        final Resource resource = resourceSet.createResource(writeModelURI);
        resource.getContents().add(privacyModel);
        try {
            resource.save(null);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

}
