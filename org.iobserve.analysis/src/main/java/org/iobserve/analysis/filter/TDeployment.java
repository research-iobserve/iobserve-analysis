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

import org.iobserve.analysis.AnalysisMain;
import org.iobserve.analysis.correspondence.Correspondent;
import org.iobserve.analysis.correspondence.ICorrespondence;
import org.iobserve.analysis.model.AllocationModelBuilder;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.ModelProviderPlatform;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelBuilder;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.common.record.EJBDeployedEvent;
import org.iobserve.common.record.IDeploymentRecord;
import org.iobserve.common.record.ServletDeployedEvent;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

import teetime.framework.AbstractConsumerStage;

import com.google.common.base.Optional;

/**
 * It could be interesting to combine DeploymentEventTransformation and
 * UndeploymentEventTransformation. However, that would require two input ports. And I have not used
 * the API for multiple input ports.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 */
public class TDeployment extends AbstractConsumerStage<IDeploymentRecord> {

    private static long executionCounter = 0;
    private final ICorrespondence correspondence;
    private AllocationModelProvider allocationModelProvider;
    private SystemModelProvider systemModelProvider;
    private ResourceEnvironmentModelProvider resourceEnvModelProvider;

    /**
     * Most likely the constructor needs an additional field for the PCM access. But this has to be
     * discussed with Robert.
     *
     * @param correspondence
     *            the correspondence model access
     */
    public TDeployment() {
        final ModelProviderPlatform modelProviderPlatform = AnalysisMain.getInstance().getModelProviderPlatform();

        // get all model references
        this.correspondence = modelProviderPlatform.getCorrespondenceModel();
        this.allocationModelProvider = modelProviderPlatform.getAllocationModelProvider();
        this.systemModelProvider = modelProviderPlatform.getSystemModelProvider();
        this.resourceEnvModelProvider = modelProviderPlatform.getResourceEnvironmentModelProvider();
    }

    /**
     * This method is triggered for every deployment event.
     *
     * @param event
     *            one deployment event to be processed
     */
    @Override
    protected void execute(final IDeploymentRecord event) {
        AnalysisMain.getInstance().getTimeMemLogger().before(this, this.getId() + TDeployment.executionCounter); // TODO
                                                                                                                 // testing
                                                                                                                 // logger

        if (event instanceof ServletDeployedEvent) {
            this.process((ServletDeployedEvent) event);

        } else if (event instanceof EJBDeployedEvent) {
            this.process((EJBDeployedEvent) event);
        }

        AnalysisMain.getInstance().getTimeMemLogger().after(this, this.getId() + TDeployment.executionCounter); // TODO
                                                                                                                // testing
                                                                                                                // logger

        TDeployment.executionCounter++;
    }

    /**
     * Process the given {@link ServletDeployedEvent} event.
     * 
     * @param event
     *            event to process
     */
    private void process(final ServletDeployedEvent event) {
        final String serivce = event.getSerivce();
        final String context = event.getContext();

        final Optional<Correspondent> optionCorrespondent = this.correspondence.getCorrespondent(context);
        if (optionCorrespondent.isPresent()) {
            final Correspondent correspondent = optionCorrespondent.get();
            this.updateModel(serivce, correspondent);
        }
    }

    /**
     * Process the given {@link EJBDeployedEvent} event.
     * 
     * @param event
     *            event to process
     */
    private void process(final EJBDeployedEvent event) {
        final String service = event.getSerivce();
        final String context = event.getContext();

        final Optional<Correspondent> optionCorrespondent = this.correspondence.getCorrespondent(context);
        if (optionCorrespondent.isPresent()) {
            final Correspondent correspondent = optionCorrespondent.get();
            this.updateModel(service, correspondent);
        }
    }

    /**
     * Update the system- and allocation-model by the given correspondent.
     * 
     * @param correspondent
     *            correspondent
     */
    private void updateModel(final String serverName, final Correspondent correspondent) {
        // get the model entity name
        final String entityName = correspondent.getPcmEntityName();

        // build the assembly context name
        final String asmContextName = entityName + "_" + serverName;

        // get the model parts by name
        final ResourceContainer resourceContainer = this.resourceEnvModelProvider
                .getResourceContainerByName(serverName);

        // this can not happen since TAllocation should have created the resource container already.
        if (resourceContainer == null) {
            System.out.println("ResourceContainer is null in TDeployment?!");
            return;
        }

        // get the assembly context. Create it if necessary
        AssemblyContext assemblyContext = this.systemModelProvider.getAssemblyContextByName(asmContextName);

        // in case the assembly context is null, TDeployment should create it
        if (assemblyContext == null) {
            // create assembly context
            final SystemModelBuilder systemBuilder = new SystemModelBuilder(this.systemModelProvider);
            systemBuilder.loadModel().createAssemblyContextsIfAbsent(asmContextName).build();
            assemblyContext = this.systemModelProvider.getAssemblyContextByName(asmContextName);
        }

        // update the allocation model
        final AllocationModelBuilder builder = new AllocationModelBuilder(this.allocationModelProvider);
        builder.loadModel()
                // .resetModel() //TODO if we do this, it will delete all the other elements in the
                // model?!
                .addAllocationContextIfAbsent(resourceContainer, assemblyContext).build();
    }
}
