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
package org.iobserve.analysis.model;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.allocation.AllocationPackage;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * Model provider to provide {@link Allocation} model.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 *
 */
public final class AllocationModelProvider extends AbstractModelProvider<Allocation> {

	/**
	 * Create model provider to provide {@link Allocation} model.
	 *
	 * @param uriModelInstance
	 *            uri to the model
	 */
	public AllocationModelProvider(final URI uriModelInstance) {
		super(uriModelInstance);
		this.loadModel();
	}

	public AllocationModelProvider() {
		super();
	}

	/**
	 * Return the assembly context representing the given input.
	 * 
	 * @param assemblyContext
	 *            the containing assembly context
	 * @param resourceContainer
	 *            the containing resource container
	 * @return the allocation context or NULL if not existent
	 */
	public AllocationContext getAllocationContext(AssemblyContext assemblyContext, ResourceContainer resourceContainer) {
		return AllocationModelProvider.getAllocationContext(this.getModel(), assemblyContext, resourceContainer);
	}

	
	/**
	 * Return the assembly context representing the given input.
	 * 
	 * @param assemblyContext
	 *            the containing assembly context
	 * @return the allocation context or NULL if not existent
	 */
	public AllocationContext getAllocationContext(AssemblyContext assemblyContext) {
		return AllocationModelProvider.getAllocationContext(super.getModel(), assemblyContext);
	}
	
	
	/**
	 * Return the assembly context representing the given input.
	 * 
	 * @param model
	 *            the allocation model
	 * @param assemblyContext
	 *            the containing assembly context
	 * @return the allocation context or NULL if not existent
	 */
	public static AllocationContext getAllocationContext(Allocation model, AssemblyContext assemblyContext) {
		AllocationContext requestedAllocCon = null;
		String assemblyContextID = assemblyContext.getId();

		for (AllocationContext allocCon : model.getAllocationContexts_Allocation()) {
			String currentAssemblyConID = allocCon.getAssemblyContext_AllocationContext().getId();
			if (currentAssemblyConID.equals(assemblyContextID)) {
				requestedAllocCon = allocCon;
				break;
			}
		}

		return requestedAllocCon;
	}

	/**
	 * Return the assembly context representing the given input.
	 * 
	 * @param assemblyContext
	 *            the containing assembly context
	 * @param resourceContainer
	 *            the containing resource container
	 * @return the allocation context or NULL if not existent
	 */
	public static AllocationContext getAllocationContext(Allocation model, AssemblyContext assemblyContext, ResourceContainer resourceContainer) {
		AllocationContext requestedAllocCon = null;

		String assemblyContextID = assemblyContext.getId();
		String resourceContainerID = resourceContainer.getId();

		for (AllocationContext allocCon : model.getAllocationContexts_Allocation()) {
			String currentAssemblyConID = allocCon.getAssemblyContext_AllocationContext().getId();
			if (currentAssemblyConID.equals(assemblyContextID)) {
				String currentResContainerID = allocCon.getResourceContainer_AllocationContext().getId();
				if (currentResContainerID.equals(resourceContainerID)) {
					requestedAllocCon = allocCon;
					break;
				}
			}
		}

		return requestedAllocCon;
	}

	/**
	 * Reset model.
	 */
	@Override
	public void resetModel() {
		final Allocation model = this.getModel();
		model.getAllocationContexts_Allocation().clear();
	}

	@Override
	public EPackage getPackage() {
		return AllocationPackage.eINSTANCE;
	}
}
