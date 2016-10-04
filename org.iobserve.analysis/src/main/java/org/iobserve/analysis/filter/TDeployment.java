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

import java.util.Optional;

import org.iobserve.analysis.correspondence.Correspondent;
import org.iobserve.analysis.correspondence.ICorrespondence;
import org.iobserve.analysis.model.AllocationModelBuilder;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelBuilder;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.analysis.utils.Opt;
import org.iobserve.common.record.EJBDeployedEvent;
import org.iobserve.common.record.IDeploymentRecord;
import org.iobserve.common.record.ServletDeployedEvent;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

import teetime.framework.AbstractConsumerStage;

/**
 * It could be interesting to combine DeploymentEventTransformation and
 * UndeploymentEventTransformation. However, that would require two input ports. And I have not used
 * the API for multiple input ports.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 */
public final class TDeployment extends AbstractConsumerStage<IDeploymentRecord> {

    /** reference to correspondent model. */
    private final ICorrespondence correspondence;
    /** reference to allocation model provider. */
    private final AllocationModelProvider allocationModelProvider;
    /** reference to system model provider. */
    private final SystemModelProvider systemModelProvider;
    /** reference to resource environment model provider. */
    private final ResourceEnvironmentModelProvider resourceEnvModelProvider;

    /**
     * Most likely the constructor needs an additional field for the PCM access. But this has to be
     * discussed with Robert.
     *
     * @param correspondence
     *            the correspondence model access
     */
    public TDeployment(final ICorrespondence correspondence, final AllocationModelProvider allocationModelProvider,
            final SystemModelProvider systemModelProvider,
            final ResourceEnvironmentModelProvider resourceEnvironmentModelProvider) {
        // get all model references
        this.correspondence = correspondence;
        this.allocationModelProvider = allocationModelProvider;
        this.systemModelProvider = systemModelProvider;
        this.resourceEnvModelProvider = resourceEnvironmentModelProvider;
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
    }

    /**
     * Process the given {@link ServletDeployedEvent} event and update the model.
     *
     * @param event
     *            event to process
     */
    private void process(final ServletDeployedEvent event) {
        final String service = event.getSerivce();
        final String context = event.getContext();
        Opt.of(this.correspondence.getCorrespondent(context)).ifPresent()
                .apply(correspondence -> this.updateModel(service, correspondence))
                .elseApply(() -> System.out.printf("No correspondent found for %s \n", service));
    }

    /**
     * Process the given {@link EJBDeployedEvent} event and update the model.
     *
     * @param event
     *            event to process
     */
    private void process(final EJBDeployedEvent event) {
        final String service = event.getSerivce();
        final String context = event.getContext();

        Opt.of(this.correspondence.getCorrespondent(context)).ifPresent()
                .apply(correspondent -> this.updateModel(service, correspondent))
                .elseApply(() -> System.out.printf("No correspondent found for %s \n", service));
    }

    /**
     * Update the system- and allocation-model by the given correspondent.
     *
     * @param serverName
     *            name of the server
     * @param correspondent
     *            correspondent
     */
    private void updateModel(final String serverName, final Correspondent correspondent) {
        // get the model entity name
        final String entityName = correspondent.getPcmEntityName();

        // build the assembly context name
        final String asmContextName = entityName + "_" + serverName;

        // get the model parts by name
        final Optional<ResourceContainer> optResourceContainer = this.resourceEnvModelProvider
                .getResourceContainerByName(serverName);

        // this can not happen since TAllocation should have created the resource container already.
        Opt.of(optResourceContainer).ifPresent()
                .apply(resourceContainer -> this.updateModel(resourceContainer, asmContextName))
                .elseApply(() -> System.out.printf("AssemblyContext %s was not available?!\n"));

        // get the assembly context. Create it if necessary
        AssemblyContext assemblyContext = this.systemModelProvider.getAssemblyContextByName(asmContextName).get();

        // in case the assembly context is null, TDeployment should create it
        if (assemblyContext == null) {
            // create assembly context
            final SystemModelBuilder systemBuilder = new SystemModelBuilder(this.systemModelProvider);
            systemBuilder.loadModel().createAssemblyContextsIfAbsent(asmContextName).build();
            assemblyContext = this.systemModelProvider.getAssemblyContextByName(asmContextName).get();
        }
    }

    /**
     * Add allocation context to allocation model with the given {@link ResourceContainer} and
     * {@link AssemblyContext} identified by the given entity name.
     *
     * @param resourceContainer
     *            instance of resource container
     * @param asmContextName
     *            entity name of assembly context
     */
    private void updateModel(final ResourceContainer resourceContainer, final String asmContextName) {
        // get assembly context by name or create it if necessary.
        final AssemblyContext assemblyContext = this.systemModelProvider.getAssemblyContextByName(asmContextName)
                .orElse(TDeployment.createAssemblyContextByName(this.systemModelProvider, asmContextName));

        // update the allocation model
        final AllocationModelBuilder builder = new AllocationModelBuilder(this.allocationModelProvider);
        builder.loadModel()
                // .resetModel() //TODO if we do this, it will delete all the other elements in the
                // model?!
                .addAllocationContextIfAbsent(resourceContainer, assemblyContext).build();
    }

    /**
     * Create {@link AssemblyContext} with the given name.
     *
     * @param provider
     *            provider
     * @param name
     *            name
     * @return created assembly context
     */
    private static AssemblyContext createAssemblyContextByName(final SystemModelProvider provider, final String name) {
        final SystemModelBuilder systemBuilder = new SystemModelBuilder(provider);
        systemBuilder.loadModel();
        final AssemblyContext ctx = systemBuilder.createAssemblyContext(name);
        systemBuilder.build();
        return ctx;
    }

}
