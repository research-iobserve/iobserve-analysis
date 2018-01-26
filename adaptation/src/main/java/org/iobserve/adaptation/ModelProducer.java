package org.iobserve.adaptation;

import java.io.File;

import org.eclipse.emf.ecore.EObject;
import org.iobserve.model.PCMModelHandler;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import teetime.framework.AbstractProducerStage;

/**
 * Only for debugging.
 *
 * @author Lars Bluemke
 *
 */
public class ModelProducer extends AbstractProducerStage<EObject> {

    private static final File PCM_MODELS_DIRECTORY = new File(
            "/Users/LarsBlumke/Documents/CAU/Masterarbeit/working-dir");

    @Override
    protected void execute() throws Exception {

        final PCMModelHandler modelHandler = new PCMModelHandler(ModelProducer.PCM_MODELS_DIRECTORY);

        final Repository repositoryModel = modelHandler.getRepositoryModel();
        final UsageModel usageModel = modelHandler.getUsageModel();
        final System systemModel = modelHandler.getSystemModel();
        final ResourceEnvironment resourceEnvironmentModel = modelHandler.getResourceEnvironmentModel();
        final Allocation allocationModel = modelHandler.getAllocationModel();

        this.outputPort.send(allocationModel);

    }

}
