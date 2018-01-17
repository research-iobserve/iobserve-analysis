/***************************************************************************
 * Copyright (C) 2015 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.model.provider.file;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.Interface;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.repository.RepositoryPackage;

/**
 * Model provider to provide {@link Repository} model.
 *
 * @author Robert Heinrich
 * @author Nicolas Boltz
 * @author Alessandro Giusa
 *
 * @deprecated since 0.0.2 we should use the new neo4j generic provider
 *             {@link org.iobserve.model.provider.neo4j.ModelProvider}
 */
@Deprecated
public final class RepositoryModelProvider extends AbstractModelProvider<Repository> {

    /** map of operation interfaces mapped by id. */
    private Map<String, OperationInterface> operationInterfaceMap;
    /** map of operation signatures mapped by id. */
    private Map<String, OperationSignature> operationSignatureMap;
    /** map between operation interface id and the provided interface id that implements it. */
    private Map<String, String> opInfToProvInfMap;
    /** map of operation provided roles mapped by id. */
    private Map<String, OperationProvidedRole> opProvidedRoleMap;

    /**
     * Create model provider to provide {@link Repository} model.
     *
     * @param uriRepositoryModel
     *            uri to the model
     */
    public RepositoryModelProvider(final URI uriRepositoryModel) {
        super(uriRepositoryModel);
        this.loadData();
    }

    @Override
    public void resetModel() {
        // nothing to do
    }

    /**
     * Loading and initializing the maps for data access.
     */
    private void loadData() {
        this.opInfToProvInfMap = new HashMap<>();
        this.opProvidedRoleMap = new HashMap<>();
        this.operationInterfaceMap = new HashMap<>();
        this.operationSignatureMap = new HashMap<>();

        // loading OperationProvidedRoles and OperationInterfaces in dedicated maps
        for (final RepositoryComponent nextRepoCmp : this.getModel().getComponents__Repository()) {
            if (nextRepoCmp instanceof BasicComponent) {
                final BasicComponent basicCmp = (BasicComponent) nextRepoCmp;

                for (final ProvidedRole providedRole : basicCmp.getProvidedRoles_InterfaceProvidingEntity()) {
                    if (providedRole instanceof OperationProvidedRole) {
                        final OperationProvidedRole opProvRole = (OperationProvidedRole) providedRole;
                        final OperationInterface opInterface = opProvRole.getProvidedInterface__OperationProvidedRole();
                        this.opInfToProvInfMap.put(opInterface.getId(), opProvRole.getId());
                        this.opProvidedRoleMap.put(opProvRole.getId(), opProvRole);
                    }
                }
            }
        }

        // loading OperationInterfaces and OperationSignatures in dedicated maps
        for (final Interface nextInterface : this.getModel().getInterfaces__Repository()) {
            if (nextInterface instanceof OperationInterface) {
                final OperationInterface opInf = (OperationInterface) nextInterface;
                this.operationInterfaceMap.put(opInf.getId(), opInf);

                for (final OperationSignature opSig : opInf.getSignatures__OperationInterface()) {
                    this.operationSignatureMap.put(opSig.getId(), opSig);
                }
            }
        }
    }

    @Override
    protected EPackage getPackage() {
        return RepositoryPackage.eINSTANCE;
    }

    /**
     * Get the {@link OperationSignature} with the given signature. The comparison is done by the
     * {@link OperationSignature#getEntityName()}.
     *
     * @param operationID
     *            operation id
     * @return operation signature instance or null if no operation signature with the given id
     *         could be found
     */
    public OperationSignature getOperationSignature(final String operationID) {
        return this.operationSignatureMap.get(operationID);
    }

    /**
     * Get the {@link OperationProvidedRole} by the given operation interface and basic component.
     *
     * @param operationInterface
     *            operation interface.
     * @return operation provide role instance or null if none available by the given operation
     *         interface
     */
    public OperationProvidedRole getOperationProvidedRole(final OperationInterface operationInterface) {
        final String provRoleId = this.opInfToProvInfMap.get(operationInterface.getId());
        return this.opProvidedRoleMap.get(provRoleId);
    }
}
