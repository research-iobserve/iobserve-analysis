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
package org.palladiosimulator.protocom.correspondencemodel;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.CompositeComponent;
import org.palladiosimulator.pcm.repository.Interface;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationRequiredRole;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.repository.RepositoryPackage;
import org.palladiosimulator.pcm.repository.RequiredRole;

/**
 * Model provider to provide {@link Repository} model.
 *
 * @author Robert Heinrich
 * @author Nicolas Boltz
 * @author Alessandro Giusa
 *
 */
public final class RepositoryModelProvider extends AbstractModelProvider<Repository> {

	private Set<String> setOfIds;
    

    /**
     * Create model provider to provide {@link Repository} model.
     *
     * @param uriUsageModel
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
    	this.setOfIds = new HashSet<>();
    	final Repository repository = this.getModel();
    	
        for (final RepositoryComponent nextRepoCmp : repository.getComponents__Repository()) {
            if (nextRepoCmp instanceof BasicComponent) {
                final BasicComponent basicCmp = (BasicComponent) nextRepoCmp;
                this.setOfIds.add(basicCmp.getId());
                
                for(final ProvidedRole providedRole : basicCmp.getProvidedRoles_InterfaceProvidingEntity()) {
                	if(providedRole instanceof OperationProvidedRole) {
                		final OperationProvidedRole opProvRole = (OperationProvidedRole) providedRole;
                		final OperationInterface opInterface = opProvRole.getProvidedInterface__OperationProvidedRole();
                		this.setOfIds.add(opProvRole.getId());
                		this.setOfIds.add(opInterface.getId());
                	}
                }
                
                for(final RequiredRole requiredRole : basicCmp.getRequiredRoles_InterfaceRequiringEntity()) {
                	if(requiredRole instanceof OperationRequiredRole) {
                		final OperationRequiredRole opRequRole = (OperationRequiredRole) requiredRole;
                		final OperationInterface opInterface = opRequRole.getRequiredInterface__OperationRequiredRole();
                		this.setOfIds.add(opRequRole.getId());
                		this.setOfIds.add(opInterface.getId());
                	}
                }
            } else if (nextRepoCmp instanceof CompositeComponent) {
            	final CompositeComponent compCmp = (CompositeComponent)nextRepoCmp;
               
            	for(final ProvidedRole providedRole : compCmp.getProvidedRoles_InterfaceProvidingEntity()) {
                	if(providedRole instanceof OperationProvidedRole) {
                		final OperationProvidedRole opProvRole = (OperationProvidedRole) providedRole;
                		final OperationInterface opInterface = opProvRole.getProvidedInterface__OperationProvidedRole();
                		this.setOfIds.add(opProvRole.getId());
                		this.setOfIds.add(opInterface.getId());
                	}
                }
                
                for(final RequiredRole requiredRole : compCmp.getRequiredRoles_InterfaceRequiringEntity()) {
                	if(requiredRole instanceof OperationRequiredRole) {
                		final OperationRequiredRole opRequRole = (OperationRequiredRole) requiredRole;
                		final OperationInterface opInterface = opRequRole.getRequiredInterface__OperationRequiredRole();
                		this.setOfIds.add(opRequRole.getId());
                		this.setOfIds.add(opInterface.getId());
                	}
                }
            }
        }
        
    	// loading OperationInterfaces and OperationSignatures in dedicated maps
        for (final Interface nextInterface : this.getModel().getInterfaces__Repository()) {
            if (nextInterface instanceof OperationInterface) {
                final OperationInterface opInf = (OperationInterface) nextInterface;
                this.setOfIds.add(opInf.getId());
                
                for (final OperationSignature opSig : opInf.getSignatures__OperationInterface()) {
                	this.setOfIds.add(opSig.getId());
                }
            }
        }
    }

    @Override
    protected EPackage getPackage() {
        return RepositoryPackage.eINSTANCE;
    }

    /**
     * 
     * @param id
     * @return true if the the given id belongs to one of {@link BasicComponent}, {@link ProvidedRole}, {@link RequiredRole}, {@link OperationSignature}, {@link OperationInterface}.
     */
    public boolean hasEntityWithId(final String id) {
    	return this.setOfIds.contains(id);
    }
}
