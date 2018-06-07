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

import org.eclipse.emf.ecore.EPackage;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationPackage;

/**
 * Model provider to provide {@link Allocation} model.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 * @author Reiner Jung - refactoring & api change
 *
 */
public final class AllocationModelHandler extends AbstractModelHandler<Allocation> {

    public static final String SUFFIX = "allocation";

    /**
     * Create an uninitialized empty provider.
     */
    public AllocationModelHandler() {
        super();
    }

    @Override
    protected EPackage getPackage() {
        return AllocationPackage.eINSTANCE;
    }

    @Override
    protected String getSuffix() {
        return AllocationModelHandler.SUFFIX;
    }

}
