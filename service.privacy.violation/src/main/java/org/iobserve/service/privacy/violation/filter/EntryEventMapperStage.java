/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.service.privacy.violation.filter;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.model.correspondence.AllocationEntry;
import org.iobserve.model.correspondence.ComponentEntry;
import org.iobserve.model.correspondence.OperationEntry;
import org.iobserve.model.persistence.neo4j.IModelProvider;
import org.iobserve.model.persistence.neo4j.ModelGraph;
import org.iobserve.model.persistence.neo4j.ModelProvider;
import org.iobserve.service.privacy.violation.data.PCMEntryCallEvent;
import org.iobserve.stages.general.data.EntryCallEvent;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.RepositoryComponent;

/**
 * Transforms {@link EntryCallEvent}s to model level {@link PCMEntryCallEvent}s.
 *
 * @author Reiner Jung
 *
 */
public class EntryEventMapperStage extends AbstractConsumerStage<EntryCallEvent> {

    private final OutputPort<PCMEntryCallEvent> outputPort = this.createOutputPort(PCMEntryCallEvent.class);

    private final IModelProvider<ComponentEntry> componentEntryProvider;

    private final ModelProvider<OperationEntry> operationSignatureEntryProvider;

    private final ModelProvider<AllocationEntry> allocationEntryProvider;

    private final ModelProvider<AllocationContext> allocationContextProvider;

    private final ModelProvider<RepositoryComponent> componentProvider;

    private final ModelProvider<OperationSignature> operationSignatureProvider;

    private final ModelProvider<AssemblyContext> assemblyContextProvider;

    /**
     * Entry event mapper.
     *
     * @param correspondenceModelGraph
     *            correspondence model graph
     * @param repositoryGraph
     *            repository model graph
     * @param assemblyGraph
     *            assembly model graph
     * @param allocationGraph
     *            allocation model graph
     */
    public EntryEventMapperStage(final ModelGraph correspondenceModelGraph, final ModelGraph repositoryGraph,
            final ModelGraph assemblyGraph, final ModelGraph allocationGraph) {
        this.componentEntryProvider = new ModelProvider<>(correspondenceModelGraph, null,
                ModelProvider.IMPLEMENTATION_ID);
        this.operationSignatureEntryProvider = new ModelProvider<>(correspondenceModelGraph, null,
                ModelProvider.IMPLEMENTATION_ID);
        this.allocationEntryProvider = new ModelProvider<>(correspondenceModelGraph, null,
                ModelProvider.IMPLEMENTATION_ID);

        this.componentProvider = new ModelProvider<>(repositoryGraph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        this.operationSignatureProvider = new ModelProvider<>(repositoryGraph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        this.assemblyContextProvider = new ModelProvider<>(assemblyGraph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        this.allocationContextProvider = new ModelProvider<>(allocationGraph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
    }

    @Override
    protected void execute(final EntryCallEvent event) throws Exception {
        /** retrieve mapping. */
        final ComponentEntry componentEntry = this.componentEntryProvider.getObjectByTypeAndId(ComponentEntry.class,
                event.getClassSignature());
        if (componentEntry != null) {
            final OperationEntry operationEntry = this.operationSignatureEntryProvider
                    .getObjectByTypeAndId(OperationEntry.class, event.getOperationSignature());

            if (operationEntry != null) {
                final AllocationEntry allocationEntry = this.allocationEntryProvider
                        .getObjectByTypeAndId(AllocationEntry.class, event.getHostname());
                if (allocationEntry != null) {
                    this.computePcmEntryCallEvent(componentEntry, operationEntry, allocationEntry, event);
                } else {
                    this.logger.debug("No corresponding allocation for entry call {}", event.toString());
                }
            } else {
                this.logger.debug("No corresponding component for entry call {}", event.toString());
            }
        } else {
            this.logger.debug("No corresponding component for entry call {}", event.toString());
        }
    }

    private void computePcmEntryCallEvent(final ComponentEntry componentEntry, final OperationEntry operationEntry,
            final AllocationEntry allocationEntry, final EntryCallEvent event) {
        /** retrieve PCM model elements from mapping. */
        final AllocationContext allocationContext = this.allocationContextProvider
                .getObjectByTypeAndId(AllocationContext.class, allocationEntry.getAllocation().getId());
        final OperationSignature operationSignature = this.operationSignatureProvider
                .findAndLockObjectById(OperationSignature.class, operationEntry.getOperation().getId());
        final RepositoryComponent component = this.componentProvider.getObjectByTypeAndId(RepositoryComponent.class,
                componentEntry.getComponent().getId());

        /** assembly is inferred from allocation. */
        final AssemblyContext assemblyContext = this.assemblyContextProvider.getObjectByTypeAndId(AssemblyContext.class,
                allocationContext.getAssemblyContext_AllocationContext().getId());

        /** assemble event. */
        final PCMEntryCallEvent mappedEvent = new PCMEntryCallEvent(event.getEntryTime(), event.getExitTime(),
                component, operationSignature, assemblyContext, allocationContext);

        this.outputPort.send(mappedEvent);
    }

    public OutputPort<PCMEntryCallEvent> getOutputPort() {
        return this.outputPort;
    }

}
