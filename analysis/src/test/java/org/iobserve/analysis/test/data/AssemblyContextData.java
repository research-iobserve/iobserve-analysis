/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.test.data;

import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.CompositionFactory;

/**
 * @author Reiner Jung
 *
 */
public class AssemblyContextData {
    public static final String ASSEMBLY_CONTEXT_NAME = CorrespondenceModelData.PCM_ENTITY_NAME + "_"
            + ImplementationLevelData.SERVICE;
    public static final AssemblyContext ASSEMBLY_CONTEXT = AssemblyContextData.createAssemblyContext();

    private static AssemblyContext createAssemblyContext() {
        final AssemblyContext assemblyContext = CompositionFactory.eINSTANCE.createAssemblyContext();
        assemblyContext.setEntityName(AssemblyContextData.ASSEMBLY_CONTEXT_NAME);
        assemblyContext.setId("_assemblycontext_test_id");

        return assemblyContext;
    }
}