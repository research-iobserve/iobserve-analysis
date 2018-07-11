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
import org.iobserve.model.correspondence.CorrespondencePackage;
import org.iobserve.model.persistence.neo4j.ModelResource;
import org.iobserve.model.privacy.PrivacyPackage;
import org.iobserve.service.privacy.violation.filter.PrivacyWarner;
import org.iobserve.stages.data.Warnings;
import org.palladiosimulator.pcm.allocation.AllocationPackage;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.CompositionFactory;
import org.palladiosimulator.pcm.repository.RepositoryPackage;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;
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
    // pcm "/home/reiner/Projects/iObserve/jpetstore-6/pcm/5.2";
    // db "/home/reiner/Projects/iObserve/experiments/jss-privacy-experiment/db";

    // pcm "D:/Experiment/distributed-jpetstore-experiment/pcm/JPetStore";
    // db "D:/Experiment/distributed-jpetstore-experiment/db";

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
     */
    public static void main(final String[] args) throws IOException {
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
     */
    public void initializePW() {
        this.clearDirectory(this.modelDatabaseDirectory);
        this.modelDatabaseDirectory.mkdirs();

        try {
            final ModelImporter modelHandler = new ModelImporter(this.pcmDirectory);

            /** graphs. */
            final ModelResource correspondenceModelResource = new ModelResource(CorrespondencePackage.eINSTANCE,
                    this.modelDatabaseDirectory); // add sub dir repository + versioning
            correspondenceModelResource.storeModelPartition(modelHandler.getCorrespondenceModel());

            final ModelResource repositoryModelResource = new ModelResource(RepositoryPackage.eINSTANCE,
                    this.modelDatabaseDirectory); // add sub dir repository + versioning
            repositoryModelResource.storeModelPartition(modelHandler.getRepositoryModel());

            final ModelResource resourceEnvironmentModelResource = new ModelResource(
                    ResourceenvironmentPackage.eINSTANCE, this.modelDatabaseDirectory); // add sub
                                                                                        // dir
                                                                                        // repository
                                                                                        // +
                                                                                        // versioning
            resourceEnvironmentModelResource.storeModelPartition(modelHandler.getResourceEnvironmentModel());

            final ModelResource systemModelResource = new ModelResource(SystemPackage.eINSTANCE,
                    this.modelDatabaseDirectory); // add sub dir repository + versioning
            systemModelResource.storeModelPartition(modelHandler.getSystemModel());

            final ModelResource allocationModelResource = new ModelResource(AllocationPackage.eINSTANCE,
                    this.modelDatabaseDirectory); // add sub dir repository + versioning
            allocationModelResource.storeModelPartition(modelHandler.getAllocationModel());

            final ModelResource privacyModelResource = new ModelResource(PrivacyPackage.eINSTANCE,
                    this.modelDatabaseDirectory); // add sub dir repository + versioning
            privacyModelResource.storeModelPartition(modelHandler.getPrivacyModel());

            final Configuration configration = new Configuration();
            configration.setProperty("policy.package",
                    "org.iobserve.service.privacy.violation.transformation.privacycheck.policies");
            configration.setStringArrayProperty("policy.list", "NoPersonalDataInUSAPolicy".split(","));

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
        final PCMDeployedEvent pcmdpe = new PCMDeployedEvent("TestService", assemblyContext, "http://Test.test",
                ISOCountryCode.EVIL_EMPIRE);

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
