/***************************************************************************
 * Copyright (C) 2014 iObserve Project (https://www.iobserve-devops.net)
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

import java.util.Optional;

import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.allocation.AllocationFactory;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 *
 */
public final class AllocationModelBuilder {

	/**
	 *
	 * @param modelProvider
	 */
	private AllocationModelBuilder() {
	}

	/**
	 * Add an {@link AllocationContext} for the given {@link ResourceContainer}
	 * and {@link AssemblyContext} if they are absent to this model. No check
	 * for duplication is done!
	 *
	 * @param model
	 *            allocation model
	 * @param resContainer
	 *            container
	 * @param asmCtx
	 *            assembly context
	 */
	public static AllocationContext addAllocationContext(final Allocation model, final ResourceContainer resContainer, final AssemblyContext asmCtx) {
		final AllocationFactory factory = AllocationFactory.eINSTANCE;
		final AllocationContext allocationCtx = factory.createAllocationContext();
		allocationCtx.setEntityName(asmCtx.getEntityName() + " @ " + resContainer.getEntityName());
		allocationCtx.setAssemblyContext_AllocationContext(asmCtx);
		allocationCtx.setResourceContainer_AllocationContext(resContainer);
		model.getAllocationContexts_Allocation().add(allocationCtx);
		return allocationCtx;
	}

	/**
	 * Create an {@link AllocationContext} for the given
	 * {@link ResourceContainer} and {@link AssemblyContext} if they are absent
	 * to this model. Check is done via
	 * {@link ResourceContainer#getEntityName()} and
	 * {@link AssemblyContext#getEntityName()}.
	 *
	 * @param model
	 *            the allocation model
	 * @param resContainer
	 *            container
	 * @param asmCtx
	 *            assembly context.
	 */
	public static boolean addAllocationContextIfAbsent(final Allocation model, final ResourceContainer resContainer, final AssemblyContext asmCtx) {
		boolean added = false;

		AllocationContext allocCon = AllocationModelProvider.getAllocationContext(model, asmCtx, resContainer);
		if (allocCon == null) {
			AllocationModelBuilder.addAllocationContext(model, resContainer, asmCtx);
			added = true;
		}
		return added;
	}

	/**
	 * Remove an {@link AllocationContext} from the given
	 * {@link ResourceContainer} and {@link AssemblyContext} if they are
	 * contained in the model. Identification is done via
	 * {@link ResourceContainer#getEntityName()} and
	 * {@link AssemblyContext#getEntityName()}.
	 *
	 * @param model
	 *            the allocation model
	 * @param resContainer
	 *            container
	 * @param asmCtx
	 *            assembly context
	 */
	public static boolean removeAllocationContext(final Allocation model, final ResourceContainer resContainer, final AssemblyContext asmCtx) {
		boolean removed = false;

		AllocationContext allocContext = AllocationModelProvider.getAllocationContext(model, asmCtx, resContainer);
		if (allocContext != null) {
			removed = model.getAllocationContexts_Allocation().remove(allocContext);
		}
		return removed;
	}

}
