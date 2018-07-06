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
package org.iobserve.model.test.provider.neo4j;

import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.iobserve.model.test.data.AllocationDataFactory;
import org.iobserve.model.test.data.RepositoryModelDataFactory;
import org.iobserve.model.test.data.ResourceEnvironmentDataFactory;
import org.iobserve.model.test.data.SystemDataFactory;
import org.iobserve.model.test.data.UsageModelDataFactory;
import org.junit.Before;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

/**
 * @author Reiner Jung
 *
 * @param <T>
 *            model root type
 *
 */
public abstract class AbstractModelProviderTest<T extends EObject> {

    protected final Neo4jEqualityHelper equalityHelper = new Neo4jEqualityHelper();

    protected Repository repository = RepositoryModelDataFactory.createBookstoreRepositoryModel();
    protected System system = SystemDataFactory.createSystem(this.repository);
    protected ResourceEnvironment resourceEnvironment = ResourceEnvironmentDataFactory.createResourceEnvironment();
    protected Allocation allocation = AllocationDataFactory.createAllocation(this.system, this.resourceEnvironment);
    protected UsageModel usageModel = UsageModelDataFactory.createUsageModel();

    protected String prefix;
    protected T testModel;
    protected EFactory factory;
    protected Class<T> clazz;

    /**
     * Clears the graph db for the next test.
     */
    @Before
    abstract void setUp();

}
