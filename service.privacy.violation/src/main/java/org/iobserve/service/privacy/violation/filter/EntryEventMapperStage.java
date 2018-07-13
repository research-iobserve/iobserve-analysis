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
import org.iobserve.model.correspondence.CorrespondencePackage;
import org.iobserve.model.correspondence.OperationEntry;
import org.iobserve.model.persistence.neo4j.ModelResource;
import org.iobserve.service.privacy.violation.data.PCMEntryCallEvent;
import org.iobserve.stages.general.data.EntryCallEvent;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.allocation.AllocationPackage;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.CompositionPackage;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.repository.RepositoryPackage;

/**
 * Transforms {@link EntryCallEvent}s to model level {@link PCMEntryCallEvent}s.
 *
 * @author Reiner Jung
 *
 */
public class EntryEventMapperStage extends AbstractConsumerStage<EntryCallEvent> {

    private final OutputPort<PCMEntryCallEvent> outputPort = this.createOutputPort(PCMEntryCallEvent.class);

    private final ModelResource correspondenceResource;

    private final ModelResource allocationResource;

    private final ModelResource assemblyResource;

    private final ModelResource repositoryResource;

    /**
     * Entry event mapper.
     *
     * @param correspondenceResource
     *            correspondence model graph
     * @param repositoryResource
     *            repository model graph
     * @param assemblyResource
     *            assembly model graph
     * @param allocationResource
     *            allocation model graph
     */
    public EntryEventMapperStage(final ModelResource correspondenceResource, final ModelResource repositoryResource,
            final ModelResource assemblyResource, final ModelResource allocationResource) {
        this.correspondenceResource = correspondenceResource;
        this.repositoryResource = repositoryResource;
        this.assemblyResource = assemblyResource;
        this.allocationResource = allocationResource;
    }

    @Override
    protected void execute(final EntryCallEvent event) throws Exception {
        /** retrieve mapping. */
        // TODO add correct key names
        final ComponentEntry componentEntry = this.correspondenceResource.findObjectsByTypeAndName(ComponentEntry.class,
                CorrespondencePackage.Literals.COMPONENT_ENTRY, "implementationId", event.getClassSignature()).get(0);
        if (componentEntry != null) {
            final OperationEntry operationEntry = this.correspondenceResource
                    .findObjectsByTypeAndName(OperationEntry.class, CorrespondencePackage.Literals.OPERATION_ENTRY,
                            "implementationId", event.getOperationSignature())
                    .get(0);

            if (operationEntry != null) {
                final AllocationEntry allocationEntry = this.correspondenceResource.findObjectsByTypeAndName(
                        AllocationEntry.class, CorrespondencePackage.Literals.ALLOCATION_ENTRY, "implementationId",
                        event.getHostname()).get(0);
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
        final AllocationContext allocationContext = this.allocationResource.findObjectByTypeAndId(
                AllocationContext.class, AllocationPackage.Literals.ALLOCATION_CONTEXT,
                this.allocationResource.getInternalId(allocationEntry.getAllocation()));
        final OperationSignature operationSignature = this.repositoryResource.findAndLockObjectById(
                OperationSignature.class, RepositoryPackage.Literals.OPERATION_SIGNATURE,
                this.repositoryResource.getInternalId(operationEntry.getOperation()));
        final RepositoryComponent component = this.repositoryResource.findObjectByTypeAndId(RepositoryComponent.class,
                RepositoryPackage.Literals.REPOSITORY_COMPONENT,
                this.repositoryResource.getInternalId(componentEntry.getComponent()));

        /** assembly is inferred from allocation. */
        final AssemblyContext assemblyContext = this.assemblyResource.findObjectByTypeAndId(AssemblyContext.class,
                CompositionPackage.Literals.ASSEMBLY_CONTEXT,
                this.assemblyResource.getInternalId(allocationContext.getAssemblyContext_AllocationContext()));

        /** assemble event. */
        final PCMEntryCallEvent mappedEvent = new PCMEntryCallEvent(event.getEntryTime(), event.getExitTime(),
                component, operationSignature, assemblyContext, allocationContext);

        this.outputPort.send(mappedEvent);
    }

    public OutputPort<PCMEntryCallEvent> getOutputPort() {
        return this.outputPort;
    }

}
