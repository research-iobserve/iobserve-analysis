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
import org.iobserve.model.privacy.DataProtectionModel;
import org.iobserve.model.privacy.EDataProtectionLevel;
import org.iobserve.model.privacy.EISOCode;
import org.iobserve.model.privacy.GeoLocation;
import org.iobserve.model.privacy.ParameterDataProtection;
import org.iobserve.model.privacy.PrivacyFactory;
import org.iobserve.model.privacy.ReturnTypeDataProtection;
import org.palladiosimulator.pcm.repository.Interface;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.Parameter;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Reiner Jung
 *
 */
public final class CreatePrivacyMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreatePrivacyMain.class);

    private static final Map<String, EISOCode> ISO_CODE_MAPS = new HashMap<>();
    private static final Map<String, Map<String, OperationSignatureDataProtection>> PRIVACY_LEVEL_MAPS = new HashMap<>();

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

            final DataProtectionModel privacyModel = PrivacyFactory.eINSTANCE.createDataProtectionModel();

            CreatePrivacyMain.addGeoLocations(privacyModel, environment);
            CreatePrivacyMain.addPrivacyAnnotations(privacyModel, repository);

            final URI outputURI = URI.createFileURI("/home/reiner/privacyTestModel.privacy");
            CreatePrivacyMain.save(privacyModel, outputURI);
        } catch (final IOException e) {
            CreatePrivacyMain.LOGGER.error("Canot load all models {}", e.getLocalizedMessage());
        }
    }

    private static void createPrivacyAnnotations() {
        /** IAccountService. */
        final Map<String, OperationSignatureDataProtection> accountServiceMap = new HashMap<>();
        CreatePrivacyMain.PRIVACY_LEVEL_MAPS.put("IAccountService", accountServiceMap);

        final Map<String, EDataProtectionLevel> userRequestParameter = new HashMap<>();
        userRequestParameter.put("username", EDataProtectionLevel.PERSONAL);
        userRequestParameter.put("password", EDataProtectionLevel.PERSONAL);
        accountServiceMap.put("userRequest",
                new OperationSignatureDataProtection(EDataProtectionLevel.PERSONAL, userRequestParameter));

        final Map<String, EDataProtectionLevel> insertAccountParameter = new HashMap<>();
        insertAccountParameter.put("account", EDataProtectionLevel.PERSONAL);
        accountServiceMap.put("insertAccount",
                new OperationSignatureDataProtection(EDataProtectionLevel.PERSONAL, insertAccountParameter));

        final Map<String, EDataProtectionLevel> updateAccountParameter = new HashMap<>();
        updateAccountParameter.put("account", EDataProtectionLevel.PERSONAL);
        accountServiceMap.put("updateAccount",
                new OperationSignatureDataProtection(EDataProtectionLevel.PERSONAL, updateAccountParameter));

        /** ICatalogService. */
        final Map<String, OperationSignatureDataProtection> catalogServiceMap = new HashMap<>();
        CreatePrivacyMain.PRIVACY_LEVEL_MAPS.put("ICatalogService", catalogServiceMap);

        final Map<String, EDataProtectionLevel> getCategoryListParameter = new HashMap<>();
        catalogServiceMap.put("getCategoryList",
                new OperationSignatureDataProtection(EDataProtectionLevel.ANONYMOUS, getCategoryListParameter));

        final Map<String, EDataProtectionLevel> getCategoryByIdParameter = new HashMap<>();
        getCategoryByIdParameter.put("id", EDataProtectionLevel.ANONYMOUS);
        catalogServiceMap.put("getCategoryById",
                new OperationSignatureDataProtection(EDataProtectionLevel.ANONYMOUS, getCategoryByIdParameter));

        final Map<String, EDataProtectionLevel> itemInStockParameter = new HashMap<>();
        itemInStockParameter.put("id", EDataProtectionLevel.ANONYMOUS);
        catalogServiceMap.put("itemInStock",
                new OperationSignatureDataProtection(EDataProtectionLevel.ANONYMOUS, itemInStockParameter));

        final Map<String, EDataProtectionLevel> itemListByProductParameter = new HashMap<>();
        itemListByProductParameter.put("productId", EDataProtectionLevel.ANONYMOUS);
        catalogServiceMap.put("itemListByProduct",
                new OperationSignatureDataProtection(EDataProtectionLevel.ANONYMOUS, itemListByProductParameter));

        final Map<String, EDataProtectionLevel> itemByIdParameter = new HashMap<>();
        itemByIdParameter.put("itemId", EDataProtectionLevel.ANONYMOUS);
        catalogServiceMap.put("itemById",
                new OperationSignatureDataProtection(EDataProtectionLevel.ANONYMOUS, itemByIdParameter));

        final Map<String, EDataProtectionLevel> productListByCategoryParameter = new HashMap<>();
        productListByCategoryParameter.put("categoryId", EDataProtectionLevel.ANONYMOUS);
        catalogServiceMap.put("productListByCategory",
                new OperationSignatureDataProtection(EDataProtectionLevel.ANONYMOUS, productListByCategoryParameter));

        final Map<String, EDataProtectionLevel> productByIdParameter = new HashMap<>();
        productByIdParameter.put("productId", EDataProtectionLevel.ANONYMOUS);
        catalogServiceMap.put("productById",
                new OperationSignatureDataProtection(EDataProtectionLevel.ANONYMOUS, productByIdParameter));

        final Map<String, EDataProtectionLevel> searchProductListParameter = new HashMap<>();
        searchProductListParameter.put("keywords", EDataProtectionLevel.ANONYMOUS);
        catalogServiceMap.put("searchProductList",
                new OperationSignatureDataProtection(EDataProtectionLevel.ANONYMOUS, searchProductListParameter));

        /** OrderService. */
        final Map<String, OperationSignatureDataProtection> orderServiceMap = new HashMap<>();
        CreatePrivacyMain.PRIVACY_LEVEL_MAPS.put("IOrderService", orderServiceMap);

        final Map<String, EDataProtectionLevel> insertOrderParameter = new HashMap<>();
        insertOrderParameter.put("order", EDataProtectionLevel.PERSONAL);
        orderServiceMap.put("insertOrder",
                new OperationSignatureDataProtection(EDataProtectionLevel.PERSONAL, insertOrderParameter));

        final Map<String, EDataProtectionLevel> nextIdParameter = new HashMap<>();
        nextIdParameter.put("name", EDataProtectionLevel.ANONYMOUS);
        orderServiceMap.put("nextId",
                new OperationSignatureDataProtection(EDataProtectionLevel.ANONYMOUS, nextIdParameter));

        final Map<String, EDataProtectionLevel> orderByIdParameter = new HashMap<>();
        orderByIdParameter.put("oderId", EDataProtectionLevel.DEPERSONALIZED);
        orderServiceMap.put("orderById",
                new OperationSignatureDataProtection(EDataProtectionLevel.PERSONAL, orderByIdParameter));

        final Map<String, EDataProtectionLevel> odersByUsernameParameter = new HashMap<>();
        odersByUsernameParameter.put("username", EDataProtectionLevel.PERSONAL);
        orderServiceMap.put("ordersByUsername",
                new OperationSignatureDataProtection(EDataProtectionLevel.PERSONAL, odersByUsernameParameter));

        /** IAccountDatabase. */
        final Map<String, OperationSignatureDataProtection> accountDatabaseMap = new HashMap<>();
        CreatePrivacyMain.PRIVACY_LEVEL_MAPS.put("IAccountDatabase", accountDatabaseMap);

        final Map<String, EDataProtectionLevel> applySQLStatement = new HashMap<>();
        applySQLStatement.put("sqlStatement", EDataProtectionLevel.PERSONAL);
        accountDatabaseMap.put("applySQLStatement",
                new OperationSignatureDataProtection(EDataProtectionLevel.PERSONAL, applySQLStatement));

        /** IOrderDatabase. */
        final Map<String, OperationSignatureDataProtection> orderDatabaseMap = new HashMap<>();
        CreatePrivacyMain.PRIVACY_LEVEL_MAPS.put("IOrderDatabase", orderDatabaseMap);
        orderDatabaseMap.put("applySQLStatement",
                new OperationSignatureDataProtection(EDataProtectionLevel.PERSONAL, applySQLStatement));

        /** ICatalogDatabase. */
        final Map<String, OperationSignatureDataProtection> catalogDatabaseMap = new HashMap<>();
        CreatePrivacyMain.PRIVACY_LEVEL_MAPS.put("ICatalogDatabase", catalogDatabaseMap);
        final Map<String, EDataProtectionLevel> publicApplySQLStatement = new HashMap<>();
        publicApplySQLStatement.put("sqlStatement", EDataProtectionLevel.ANONYMOUS);
        catalogDatabaseMap.put("applySQLStatement",
                new OperationSignatureDataProtection(EDataProtectionLevel.ANONYMOUS, publicApplySQLStatement));
    }

    private static void createIsoCodes() {
        CreatePrivacyMain.ISO_CODE_MAPS.put("Frontend", EISOCode.GERMANY);
        CreatePrivacyMain.ISO_CODE_MAPS.put("Account", EISOCode.USA);
        CreatePrivacyMain.ISO_CODE_MAPS.put("Catalog", EISOCode.GERMANY);
        CreatePrivacyMain.ISO_CODE_MAPS.put("Order", EISOCode.GERMANY);
        CreatePrivacyMain.ISO_CODE_MAPS.put("Database", EISOCode.GERMANY);
    }

    private static void addGeoLocations(final DataProtectionModel privacyModel, final ResourceEnvironment environment) {
        for (final ResourceContainer container : environment.getResourceContainer_ResourceEnvironment()) {
            CreatePrivacyMain.LOGGER.debug(String.format("have container %s\n", container.getEntityName()));

            final GeoLocation location = PrivacyFactory.eINSTANCE.createGeoLocation();

            location.setIsocode(CreatePrivacyMain.ISO_CODE_MAPS.get(container.getEntityName()));
            location.setResourceContainer(container);

            privacyModel.getResourceContainerLocations().add(location);
        }
    }

    private static void addPrivacyAnnotations(final DataProtectionModel privacyModel, final Repository repository) {
        for (final Interface iface : repository.getInterfaces__Repository()) {
            if (iface instanceof OperationInterface) {
                CreatePrivacyMain.LOGGER.debug(String.format("interface %s\n", iface.getEntityName()));
                final OperationInterface operationInterface = (OperationInterface) iface;
                final Map<String, OperationSignatureDataProtection> ifacePrivacy = CreatePrivacyMain.PRIVACY_LEVEL_MAPS
                        .get(iface.getEntityName());
                for (final OperationSignature signature : operationInterface.getSignatures__OperationInterface()) {
                    CreatePrivacyMain.LOGGER.debug(String.format("\tsignature %s\n", signature.getEntityName()));
                    final OperationSignatureDataProtection signaturePrivacy = ifacePrivacy
                            .get(signature.getEntityName());

                    if (signature.getReturnType__OperationSignature() != null) {
                        final ReturnTypeDataProtection returnTypeDataProection = PrivacyFactory.eINSTANCE
                                .createReturnTypeDataProtection();
                        returnTypeDataProection.setLevel(signaturePrivacy.getReturnTypePrivacy());
                        returnTypeDataProection.setOperationSignature(signature);

                        privacyModel.getDataProectionLevels().add(returnTypeDataProection);
                    }

                    for (final Parameter parameter : signature.getParameters__OperationSignature()) {
                        CreatePrivacyMain.LOGGER
                                .debug(String.format("\t\tparameter %s\n", parameter.getParameterName()));

                        final ParameterDataProtection parameterPrivacy = PrivacyFactory.eINSTANCE
                                .createParameterDataProtection();
                        parameterPrivacy
                                .setLevel(signaturePrivacy.getParameterPrivacy().get(parameter.getParameterName()));
                        parameterPrivacy.setParameter(parameter);

                        privacyModel.getDataProectionLevels().add(parameterPrivacy);
                    }
                }
            }
        }
    }

    private static void save(final DataProtectionModel privacyModel, final URI writeModelURI) {
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
            CreatePrivacyMain.LOGGER.error("Saving model failed {} {}", writeModelURI, e.getLocalizedMessage());
        }
    }

}
