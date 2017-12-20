package org.iobserve.service.privacy.violation.filter;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.service.privacy.violation.data.Warnings;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

public class PrivacyWarner extends AbstractConsumerStage<Object> {

    private final ModelProvider<Allocation> allocationModelGraphProvider;
    private final ModelProvider<System> systemModelGraphProvider;
    private final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider;

    private final OutputPort<Warnings> probesOutputPort = this.createOutputPort(Warnings.class);
    private final OutputPort<Warnings> warningsOutputPort = this.createOutputPort(Warnings.class);

    public PrivacyWarner(final ModelProvider<Allocation> allocationModelGraphProvider,
            final ModelProvider<System> systemModelGraphProvider,
            final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider) {
        this.allocationModelGraphProvider = allocationModelGraphProvider;
        this.systemModelGraphProvider = systemModelGraphProvider;
        this.resourceEnvironmentModelGraphProvider = resourceEnvironmentModelGraphProvider;
    }

    @Override
    protected void execute(final Object element) throws Exception {
        final Warnings warnings = new Warnings();

        // TODO compute privacy setup

        this.probesOutputPort.send(warnings);
        this.warningsOutputPort.send(warnings);
    }

    public OutputPort<Warnings> getProbesOutputPort() {
        return this.probesOutputPort;
    }

    public OutputPort<Warnings> getWarningsOutputPort() {
        return this.warningsOutputPort;
    }

}
