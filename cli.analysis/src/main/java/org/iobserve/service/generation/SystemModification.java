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
package org.iobserve.service.generation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.EList;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.Connector;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationRequiredRole;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.repository.RequiredRole;
import org.palladiosimulator.pcm.system.System;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ToDo .
 *
 * @author unknown
 *
 */
public class SystemModification {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelGeneration.class);

    private final Repository repositoryModel;
    private final RepositoryComponent[] repositoryComponents;
    private final System systemModel;

    private final HashMap<String, List<RepositoryComponent>> duplicateRepositoryComponents;

    private final HashMap<String, List<AssemblyContext>> openRequiredInterfaces = new HashMap<>();
    private final HashMap<String, List<AssemblyContext>> openProvidedInterfaces = new HashMap<>();

    public SystemModification(final System systemModel, final Repository repositoryModel) {
        this.systemModel = systemModel;
        this.repositoryModel = repositoryModel;
        final int componentsCount = repositoryModel.getComponents__Repository().size();
        this.repositoryComponents = repositoryModel.getComponents__Repository()
                .toArray(new RepositoryComponent[componentsCount]);

        this.duplicateRepositoryComponents = new HashMap<>();

        this.initDuplicateRepo(this.repositoryComponents);
    }

    /*
     * Calculates all duplicated Repositories.
     */
    private void initDuplicateRepo(final RepositoryComponent[] comps) {
        SystemModification.LOGGER.info("Calculating euql Repository Components ...");

        final HashMap<String, RepositoryComponent> existingInterfaceSig = new HashMap<>();

        for (final RepositoryComponent comp : comps) {
            // Calc Sig
            final String interfaceSignature = this.caluclateInterfaceSignature(comp);

            if (existingInterfaceSig.containsKey(interfaceSignature)) {
                // Sig already exists
                if (this.duplicateRepositoryComponents.containsKey(interfaceSignature)) {
                    // Already 2+ equal sig exist
                    this.duplicateRepositoryComponents.get(interfaceSignature).add(comp);
                } else {
                    // First duplicate with this sig found
                    final List<RepositoryComponent> duplicates = new ArrayList<>();
                    duplicates.add(existingInterfaceSig.get(interfaceSignature));
                    duplicates.add(comp);
                    this.duplicateRepositoryComponents.put(interfaceSignature, duplicates);
                }
            } else {
                // No equal sig found
                existingInterfaceSig.put(interfaceSignature, comp);
            }
        }

        SystemModification.LOGGER.info("Duplicate Interfacestructures found:\t {}",
                this.duplicateRepositoryComponents.keySet().size());
    }

    public List<AssemblyContext> modifySystemDeallocations(final int deallocations) {
        final List<AssemblyContext> deallocatedAssemblyContexts = new LinkedList<>();

        final EList<AssemblyContext> assemblyContexts = this.systemModel.getAssemblyContexts__ComposedStructure();
        final List<AssemblyConnector> assemblyConnectors = new ArrayList<>();
        for (final Connector connector : this.systemModel.getConnectors__ComposedStructure()) {
            if (connector instanceof AssemblyConnector) {
                assemblyConnectors.add((AssemblyConnector) connector);
            }
        }

        for (int i = 0; i < deallocations && assemblyContexts.size() > 0; i++) {
            final int randomIndex = ThreadLocalRandom.current().nextInt(assemblyContexts.size());
            final AssemblyContext assemblyContext = assemblyContexts.remove(randomIndex);
            SystemModification.LOGGER.info("REMOVING: \tAssemblyContext: \t{}", assemblyContext.getId());

            for (final AssemblyConnector assemblyConnector : assemblyConnectors) {

                boolean removeAC = false;

                if (assemblyConnector.getProvidingAssemblyContext_AssemblyConnector() == assemblyContext) {

                    final String interfaceID = assemblyConnector.getRequiredRole_AssemblyConnector()
                            .getRequiredInterface__OperationRequiredRole().getId();
                    if (!this.openRequiredInterfaces.containsKey(interfaceID)) {
                        this.openRequiredInterfaces.put(interfaceID, new ArrayList<AssemblyContext>());
                    }
                    // Add open required AC
                    this.openRequiredInterfaces.get(interfaceID)
                            .add(assemblyConnector.getRequiringAssemblyContext_AssemblyConnector());
                    removeAC = true;

                } else if (assemblyConnector.getRequiringAssemblyContext_AssemblyConnector() == assemblyContext) {

                    final String interfaceID = assemblyConnector.getProvidedRole_AssemblyConnector()
                            .getProvidedInterface__OperationProvidedRole().getId();
                    if (!this.openProvidedInterfaces.containsKey(interfaceID)) {
                        this.openProvidedInterfaces.put(interfaceID, new ArrayList<AssemblyContext>());
                    }
                    // Add open providing AC
                    this.openProvidedInterfaces.get(interfaceID)
                            .add(assemblyConnector.getProvidingAssemblyContext_AssemblyConnector());
                    removeAC = true;
                }

                if (removeAC) {
                    this.systemModel.getConnectors__ComposedStructure().remove(assemblyConnector);
                }

            }

            deallocatedAssemblyContexts.add(assemblyContext);
        }

        for (final AssemblyContext ac : deallocatedAssemblyContexts) {
            this.removeInterfaceMaps(ac);
        }

        return deallocatedAssemblyContexts;
    }

    public int modifySystemChangeComp(final int changes) {
        int madeChanges = 0;

        // Copy the assembly context list
        final EList<AssemblyContext> assemblyContextsList = this.systemModel.getAssemblyContexts__ComposedStructure();
        final List<AssemblyContext> assemblyContexts = assemblyContextsList.stream().collect(Collectors.toList());

        // Randomize the list!
        Collections.shuffle(assemblyContexts);

        // Iterate over random list
        for (final AssemblyContext assemblyContext : assemblyContexts) {

            final String interfaceSignature = this
                    .caluclateInterfaceSignature(assemblyContext.getEncapsulatedComponent__AssemblyContext());

            if (this.duplicateRepositoryComponents.containsKey(interfaceSignature)) {
                // Duplicate to containing comp is available!
                final List<RepositoryComponent> equalComponents = this.duplicateRepositoryComponents
                        .get(interfaceSignature);

                // Find non-self replacement
                RepositoryComponent newRepoComp = null;
                for (int j = 0; newRepoComp != null && j < equalComponents.size() * 10; j++) {
                    final int randomIndex = ThreadLocalRandom.current().nextInt(equalComponents.size());
                    final RepositoryComponent candidate = equalComponents.get(randomIndex);
                    if (!assemblyContext.getEncapsulatedComponent__AssemblyContext().getId()
                            .equals(candidate.getId())) {
                        newRepoComp = candidate;
                    }
                }

                if (newRepoComp != null) {
                    // Exchange containing comp
                    assemblyContext.setEncapsulatedComponent__AssemblyContext(newRepoComp);
                    madeChanges++;
                    if (madeChanges == changes) {
                        break;
                    }
                }
            }
        }

        return madeChanges;
    }

    public List<AssemblyContext> modifySystemAllocate(final int allocations) {
        List<AssemblyContext> newACs = null;

        final SystemGeneration sysGen = new SystemGeneration(this.repositoryModel, this.systemModel,
                this.openRequiredInterfaces, this.openProvidedInterfaces);
        newACs = sysGen.addAssemblyContexts(allocations, "alloc");

        return newACs;
    }

    /*
     * Calculates the inteface signuature of a component.
     */
    private String caluclateInterfaceSignature(final RepositoryComponent comp) {

        final StringBuilder sb = new StringBuilder();

        // Calculate Providing Signature
        final List<OperationProvidedRole> provInterfaces = new ArrayList<>();
        for (final ProvidedRole provInterface : comp.getProvidedRoles_InterfaceProvidingEntity()) {
            if (provInterface instanceof OperationProvidedRole) {
                provInterfaces.add((OperationProvidedRole) provInterface);
            }
        }
        // Order Interfaces
        Collections.sort(provInterfaces,
                (final OperationProvidedRole a1, final OperationProvidedRole a2) -> a1
                        .getProvidedInterface__OperationProvidedRole().getId()
                        .compareTo(a2.getProvidedInterface__OperationProvidedRole().getId()));

        // Build sig part from Prefix + Interface ID
        for (final OperationProvidedRole provInterface : provInterfaces) {
            sb.append(";p_" + provInterface.getProvidedInterface__OperationProvidedRole().getId());
        }

        // Calculate Requiring Signature
        // Get Operation Interfaces
        final List<OperationRequiredRole> reqInterfaces = new ArrayList<>();
        for (final RequiredRole reqInterface : comp.getRequiredRoles_InterfaceRequiringEntity()) {
            if (reqInterface instanceof OperationRequiredRole) {
                reqInterfaces.add((OperationRequiredRole) reqInterface);
            }
        }
        // Order Interfaces
        Collections.sort(reqInterfaces,
                (final OperationRequiredRole a1, final OperationRequiredRole a2) -> a1
                        .getRequiredInterface__OperationRequiredRole().getId()
                        .compareTo(a2.getRequiredInterface__OperationRequiredRole().getId()));

        // Build sig part from Prefix + Interface ID
        for (final OperationRequiredRole reqInterface : reqInterfaces) {
            sb.append(";r_" + reqInterface.getRequiredInterface__OperationRequiredRole().getId());
        }

        return sb.toString();
    }

    private void removeInterfaceMaps(final AssemblyContext assemblyContext) {
        for (final String key : this.openProvidedInterfaces.keySet()) {
            this.openProvidedInterfaces.get(key).remove(assemblyContext);
        }

        for (final String key : this.openRequiredInterfaces.keySet()) {
            this.openRequiredInterfaces.get(key).remove(assemblyContext);
        }
    }

}
