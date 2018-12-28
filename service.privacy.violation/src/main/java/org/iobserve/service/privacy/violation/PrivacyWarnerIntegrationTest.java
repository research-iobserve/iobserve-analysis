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
package org.iobserve.service.privacy.violation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kieker.common.configuration.Configuration;

import teetime.framework.test.StageTester;

import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.common.record.ISOCountryCode;
import org.iobserve.model.ModelImporter;
import org.iobserve.model.correspondence.CorrespondenceModel;
import org.iobserve.model.correspondence.CorrespondencePackage;
import org.iobserve.model.correspondence.EServiceTechnology;
import org.iobserve.model.persistence.DBException;
import org.iobserve.model.persistence.neo4j.Neo4JModelResource;
import org.iobserve.model.privacy.PrivacyModel;
import org.iobserve.model.privacy.PrivacyPackage;
import org.iobserve.service.privacy.violation.data.Warnings;
import org.iobserve.service.privacy.violation.filter.PrivacyWarner;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationPackage;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.CompositionFactory;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryPackage;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.system.SystemPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Clemens Brackmann
 * @author Reienr Jung -- converted to normal executable, as it is not a JunitTest
 *
 */
public class PrivacyWarnerIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrivacyWarnerIntegrationTest.class);

    private final File pcmDirectory;
    private final File modelDatabaseDirectory;

    private PrivacyWarner pw;

    /**
     * Default constructor.
     *
     * @param database
     *            database directory
     * @param model
     *            model directory
     * @throws IOException
     *             when directories do not exist
     */
    public PrivacyWarnerIntegrationTest(final String model, final String database) throws IOException {
        this.pcmDirectory = new File(model);
        if (!this.pcmDirectory.isDirectory()) {
            throw new IOException(model + " is not a directory.");
        }
        this.modelDatabaseDirectory = new File(database);
        if (!this.modelDatabaseDirectory.isDirectory()) {
            throw new IOException(database + " is not a directory.");
        }
    }

    /**
     * Entry point into integration test.
     *
     * @param args
     *            command line arguments
     * @throws IOException
     *             on io errors
     * @throws DBException
     */
    public static void main(final String[] args) throws IOException, DBException {
        if (args.length == 2) {
            final PrivacyWarnerIntegrationTest test = new PrivacyWarnerIntegrationTest(args[0], args[1]);
            test.initializePW();
            test.executeTestPW();
        } else {
            java.lang.System.err.println("Usage: warner <initialization-model> <database-directory>");
        }
    }

    /**
     * Initialize database.
     *
     * @throws DBException
     */
    public void initializePW() throws DBException {
        this.clearDirectory(this.modelDatabaseDirectory);
        this.modelDatabaseDirectory.mkdirs();

        try {
            final ModelImporter modelHandler = new ModelImporter(this.pcmDirectory);

            /** graphs. */
            final Neo4JModelResource<CorrespondenceModel> correspondenceModelResource = new Neo4JModelResource<>(
                    CorrespondencePackage.eINSTANCE, new File(this.modelDatabaseDirectory, "correspondence"));
            correspondenceModelResource.storeModelPartition(modelHandler.getCorrespondenceModel());

            final Neo4JModelResource<Repository> repositoryModelResource = new Neo4JModelResource<>(RepositoryPackage.eINSTANCE,
                    new File(this.modelDatabaseDirectory, "repository"));
            repositoryModelResource.storeModelPartition(modelHandler.getRepositoryModel());

            final Neo4JModelResource<ResourceEnvironment> resourceEnvironmentModelResource = new Neo4JModelResource<>(
                    ResourceenvironmentPackage.eINSTANCE, new File(this.modelDatabaseDirectory, "resourceenvironment"));
            resourceEnvironmentModelResource.storeModelPartition(modelHandler.getResourceEnvironmentModel());

            final Neo4JModelResource<System> systemModelResource = new Neo4JModelResource<>(SystemPackage.eINSTANCE,
                    new File(this.modelDatabaseDirectory, "system"));
            systemModelResource.storeModelPartition(modelHandler.getSystemModel());

            final Neo4JModelResource<Allocation> allocationModelResource = new Neo4JModelResource<>(AllocationPackage.eINSTANCE,
                    new File(this.modelDatabaseDirectory, "allocation"));
            allocationModelResource.storeModelPartition(modelHandler.getAllocationModel());

            final Neo4JModelResource<PrivacyModel> privacyModelResource = new Neo4JModelResource<>(PrivacyPackage.eINSTANCE,
                    new File(this.modelDatabaseDirectory, "privacy"));
            privacyModelResource.storeModelPartition(modelHandler.getPrivacyModel());

            final Configuration configration = new Configuration();
            configration.setProperty(PrivacyConfigurationsKeys.POLICY_PACKAGE_PREFIX,
                    "org.iobserve.service.privacy.violation.transformation.privacycheck.policies");
            configration.setStringArrayProperty(PrivacyConfigurationsKeys.POLICY_LIST,
                    "NoPersonalDataInUSAPolicy".split(","));

            this.pw = new PrivacyWarner(configration, repositoryModelResource, resourceEnvironmentModelResource,
                    systemModelResource, allocationModelResource, privacyModelResource);
        } catch (final IOException e) {
            PrivacyWarnerIntegrationTest.LOGGER.error("File IO error.", e);
        }
    }

    private void clearDirectory(final File directory) {
        if (directory.isDirectory()) {
            for (final File content : directory.listFiles()) {
                if (content.isFile()) {
                    content.delete();
                } else if (content.isDirectory()) {
                    this.clearDirectory(content);
                }
            }
            directory.delete();
        }
    }

    /**
     * Test run component.
     */
    public void executeTestPW() {
        final AssemblyContext assemblyContext = CompositionFactory.eINSTANCE.createAssemblyContext();
        assemblyContext.setEntityName("EntityName");
        final PCMDeployedEvent pcmdpe = new PCMDeployedEvent(EServiceTechnology.SERVLET, "TestService", assemblyContext,
                "http://Test.test", ISOCountryCode.EVIL_EMPIRE, 0);

        final List<Warnings> results = new ArrayList<>();
        StageTester.test(this.pw).and().send(pcmdpe).to(this.pw.getDeployedInputPort()).and().receive(results)
                .from(this.pw.getWarningsOutputPort()).and().start();

        for (final Warnings s : results) {
            java.lang.System.out.println("RESULT: " + s.getMessages());
        }
        // Assert.assertThat("No warning generated", false, Is.is(results.isEmpty()));

        if (results.isEmpty()) {
            java.lang.System.err.println("No warnings generated.");
        }
    }

}
