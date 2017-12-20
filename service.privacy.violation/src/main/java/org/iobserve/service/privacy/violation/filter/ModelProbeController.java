package org.iobserve.service.privacy.violation.filter;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.service.privacy.violation.data.Warnings;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

public class ModelProbeController extends AbstractConsumerStage<Warnings> {

    public ModelProbeController(final ModelProvider<Allocation> allocationModelGraphProvider,
            final ModelProvider<System> systemModelGraphProvider,
            final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider) {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void execute(final Warnings element) throws Exception {
        // TODO Auto-generated method stub

    }

    public OutputPort getOutputPort() {
        // TODO Auto-generated method stub
        return null;
    }

}
