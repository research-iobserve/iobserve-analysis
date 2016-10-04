/***************************************************************************
 * Copyright 2014 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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
package org.iobserve.analysis.filter;

import org.iobserve.analysis.correspondence.ICorrespondence;
import org.iobserve.analysis.model.ResourceEnvironmentModelBuilder;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.utils.Opt;
import org.iobserve.common.record.EJBDeployedEvent;
import org.iobserve.common.record.IDeploymentRecord;
import org.iobserve.common.record.ServletDeployedEvent;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * It could be interesting to combine DeploymentEventTransformation and
 * UndeploymentEventTransformation. However, that would require two input ports. And I have not used
 * the API for multiple input ports. TAllocation creates new resource container if and only if there
 * not already available.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 */
public final class TAllocation extends AbstractConsumerStage<IDeploymentRecord> {

    private final ICorrespondence correspondence;
    /** reference to {@link ResourceEnvironment} provider. */
    private final ResourceEnvironmentModelProvider resourceEnvModelProvider;
    /** output port. */
    private final OutputPort<IDeploymentRecord> deploymentOutputPort = this.createOutputPort();

    /**
     * Most likely the constructor needs an additional field for the PCM access. But this has to be
     * discussed with Robert.
     *
     * @param correspondence
     *            the correspondence model
     * @param resourceEvnironmentModelProvider
     *            the resource evnironment model provider
     */
    public TAllocation(final ICorrespondence correspondence,
            final ResourceEnvironmentModelProvider resourceEvnironmentModelProvider) {
        this.correspondence = correspondence;
        this.resourceEnvModelProvider = resourceEvnironmentModelProvider;
    }

    /**
     * @return the deploymentOutputPort
     */
    public OutputPort<IDeploymentRecord> getDeploymentOutputPort() {
        return this.deploymentOutputPort;
    }

    /**
     * This method is triggered for every deployment event.
     *
     * @param event
     *            one deployment event to be processed
     */
    @Override
    protected void execute(final IDeploymentRecord event) {
        if (event instanceof ServletDeployedEvent) {
            this.process((ServletDeployedEvent) event);

        } else if (event instanceof EJBDeployedEvent) {
            this.process((EJBDeployedEvent) event);
        }

        // forward the event
        this.deploymentOutputPort.send(event);
    }

    /**
     * Process the given {@link ServletDeployedEvent} event. And call {@link #updateModel(String)}
     * to create a new server if necessary.
     *
     * @param event
     *            event to process
     */
    private void process(final ServletDeployedEvent event) {
        final String serivce = event.getSerivce();
        this.updateModel(serivce);
    }

    /**
     * Process the given {@link EJBDeployedEvent} event. And call {@link #updateModel(String)} to
     * create a new server if necessary.
     *
     * @param event
     *            event to process
     */
    private void process(final EJBDeployedEvent event) {
        final String service = event.getSerivce();
        this.updateModel(service);
    }

    /**
     * Update the allocation model with the given server-name if necessary.
     *
     * @param serverName
     *            server name
     */
    private void updateModel(final String serverName) {
        Opt.of(this.resourceEnvModelProvider.getResourceContainerByName(serverName)).ifNotPresent().apply(() -> {
            final ResourceEnvironmentModelBuilder builder = new ResourceEnvironmentModelBuilder(
                    TAllocation.this.resourceEnvModelProvider);
            builder.loadModel();
            builder.createResourceContainer(serverName);
            builder.build();
        }).elseApply(Opt::doNothing);
    }
}
