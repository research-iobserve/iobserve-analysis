package org.iobserve.execution;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.iobserve.model.provider.file.AllocationModelHandler;
import org.iobserve.model.provider.file.RepositoryModelHandler;
import org.iobserve.model.provider.file.ResourceEnvironmentModelHandler;
import org.iobserve.model.provider.file.SystemModelHandler;
import org.iobserve.model.provider.file.UsageModelHandler;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import teetime.framework.AbstractConsumerStage;

/**
 * Debugging class which writes models to the file System.
 *
 * @author Lars Bluemke
 *
 */
public class ModelSink extends AbstractConsumerStage<EObject> {

    private static final File PCM_MODELS_DIRECTORY = new File(
            "/Users/LarsBlumke/Documents/CAU/Masterarbeit/working-dir-output");

    @Override
    protected void execute(final EObject model) throws Exception {
        if (model instanceof Repository) {
            new RepositoryModelHandler().save(
                    URI.createFileURI(
                            ModelSink.PCM_MODELS_DIRECTORY.getAbsolutePath() + File.separator + "default.repository"),
                    (Repository) model);
        } else if (model instanceof UsageModel) {
            new UsageModelHandler().save(
                    URI.createFileURI(
                            ModelSink.PCM_MODELS_DIRECTORY.getAbsolutePath() + File.separator + "default.usagemodel"),
                    (UsageModel) model);
        } else if (model instanceof System) {
            new SystemModelHandler().save(
                    URI.createFileURI(
                            ModelSink.PCM_MODELS_DIRECTORY.getAbsolutePath() + File.separator + "default.system"),
                    (System) model);
        } else if (model instanceof ResourceEnvironment) {
            new ResourceEnvironmentModelHandler().save(URI.createFileURI(
                    ModelSink.PCM_MODELS_DIRECTORY.getAbsolutePath() + File.separator + "default.resourceenvironment"),
                    (ResourceEnvironment) model);
        } else if (model instanceof Allocation) {
            new AllocationModelHandler().save(
                    URI.createFileURI(
                            ModelSink.PCM_MODELS_DIRECTORY.getAbsolutePath() + File.separator + "default.allocation"),
                    (Allocation) model);
        }

    }

}
