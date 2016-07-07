/***************************************************************************
 * Copyright 2015 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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
package org.iobserve.analysis.model;

import java.util.ArrayList;
import java.util.List;

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
import org.palladiosimulator.pcm.seff.ResourceDemandingSEFF;
import org.palladiosimulator.pcm.seff.ServiceEffectSpecification;

/**
 * Model provider to provide {@link Repository} model.
 * @author Robert Heinrich
 * @author Alessandro Giusa
 *
 */
public final class RepositoryModelProvider 
	extends AbstractModelProvider<Repository> {

	/**list of operation interfaces.*/
	private List<OperationInterface> operationInterfaces;
	/**list of basic components.*/
	private List<BasicComponent> basicComponents;

	/**
	 * Create model provider to provide {@link Repository} model.
	 * @param thePlatform platform
	 * @param uriUsageModel uri to the model
	 */
	RepositoryModelProvider(final ModelProviderPlatform thePlatform,
			final URI uriUsageModel) {
		super(thePlatform, uriUsageModel);
		this.loadAllBasicComponents();
		this.loadAllOperationInterfaces();
	}

	/**
	 * Load all basic components.
	 */
	private void loadAllBasicComponents() {
		this.basicComponents = new ArrayList<BasicComponent>();
		for (final RepositoryComponent nextRepoCmp 
				: this.getModel().getComponents__Repository()) {
			if (nextRepoCmp instanceof BasicComponent) {
				final BasicComponent basicCmp = (BasicComponent) nextRepoCmp;
				this.basicComponents.add(basicCmp);
			}
		}
	}

	/**
	 * Load all operation interfaces.
	 */
	private void loadAllOperationInterfaces() {
		this.operationInterfaces = new ArrayList<OperationInterface>();
		for (final Interface nextInterface 
				: this.getModel().getInterfaces__Repository()) {
			if (nextInterface instanceof OperationInterface) {
				final OperationInterface opInf =
						(OperationInterface) nextInterface;
				this.operationInterfaces.add(opInf);
			}
		}
	}

	@Override
	public EPackage getPackage() {
		return RepositoryPackage.eINSTANCE;
	}

	/**
	 * @return list of basic components
	 */
	public List<BasicComponent> getBasicComponents() {
		return this.basicComponents;
	}

	/**
	 * @return list of all operation interfaces
	 */
	public List<OperationInterface> getOperationInterfaces() {
		return this.operationInterfaces;
	}

	/**
	 * Get the {@link OperationSignature} with the given signature. The 
	 * comparison is done by the {@link OperationSignature#getEntityName()}.
	 * @param operationSig operation signature
	 * @return operation signature instance or null if no operation
	 * signature with the given entity name could be found
	 */
	public OperationSignature getOperationSignature(final String operationSig) {
		for (final OperationInterface nextOpInter : this.operationInterfaces) {
			for (final OperationSignature nextOpSig : nextOpInter
					.getSignatures__OperationInterface()) {
				if (nextOpSig.getEntityName().equalsIgnoreCase(operationSig)) {
					return nextOpSig;
				}
			}
		}
		return null;
	}

	/**
	 * Get the {@link BasicComponent} with the given operation signature.
	 * @param operationSig operation signature
	 * @return basic component instance or null if no basic component with 
	 * the given signature could be found
	 */
	public BasicComponent getBasicComponent(final String operationSig) {
		for (final BasicComponent nextBasicCmp : this.basicComponents) {
			for (final ServiceEffectSpecification nextSerEffSpec 
					: nextBasicCmp
					.getServiceEffectSpecifications__BasicComponent()) {
				if (nextSerEffSpec instanceof ResourceDemandingSEFF) {
					final ResourceDemandingSEFF rdSeff =
							(ResourceDemandingSEFF) nextSerEffSpec;
					if (operationSig.equalsIgnoreCase(rdSeff.getId())) {
						return nextBasicCmp;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Get the {@link OperationProvidedRole} by the given operation 
	 * interface and basic component.
	 * @param operationInterface operation interface. 
	 * @param basicComp basic component
	 * @return operation provide role instance or null if none available by
	 * the given operation interface and basic component
	 * 
	 * @see {@link #getOperationInterfaces()}
	 * @see #getBasicComponent(String)
	 */
	public OperationProvidedRole getOperationProvidedRole(
			final OperationInterface operationInterface,
			final BasicComponent basicComp) {
		for (final ProvidedRole nextProvidedRole 
				: basicComp.getProvidedRoles_InterfaceProvidingEntity()) {
			if (nextProvidedRole instanceof OperationProvidedRole) {
				final OperationProvidedRole operationProvidedRole = 
						(OperationProvidedRole) nextProvidedRole;
				final String idProvidedRole = operationProvidedRole
						.getProvidedInterface__OperationProvidedRole().getId();
				final String idOpIf = operationInterface.getId();
				if (idOpIf.equals(idProvidedRole)) {
					return operationProvidedRole;
				}
			}
		}
		return null;
	}
}
