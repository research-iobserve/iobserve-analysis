package org.iobserve.service.privacy.violation.filter;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.service.privacy.violation.data.Warnings;

public class ProbeMapper extends AbstractConsumerStage<Warnings> {

    private final OutputPort<String> outputPort = this.createOutputPort();

    public ProbeMapper(final ICorrespondence rac) {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void execute(final Warnings element) throws Exception {
        // TODO Auto-generated method stub

    }

    public OutputPort<String> getOutputPort() {
        return this.outputPort;
    }

}
