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
package org.iobserve.model.factory;

import java.io.IOException;
import java.util.Collections;

import de.uka.ipd.sdq.pcm.cost.CostRepository;
import de.uka.ipd.sdq.pcm.cost.FixedProcessingResourceCost;
import de.uka.ipd.sdq.pcm.cost.costFactory;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.palladiosimulator.pcm.resourceenvironment.ProcessingResourceSpecification;

/**
 * Model builder for cost models.
 *
 * @author Tobias Pöppke
 *
 */
public final class CostModelFactory {

    private CostModelFactory() {
        // private constructor, utility class
    }

    /**
     * Save the given cost repository with the specified file name in the given folder.
     *
     * @param costModelFolder
     *            the folder to save the model
     * @param fileName
     *            the file name of the saved model
     * @param repository
     *            the cost repository to save
     * @throws IOException
     *             if the repository could not be saved
     */
    public static void saveCostRepository(final URI costModelFolder, final String fileName,
            final CostRepository repository) throws IOException {
        final ResourceSet resSet = new ResourceSetImpl();
        final Resource resource = resSet
                .createResource(costModelFolder.appendSegment(fileName).appendFileExtension("cost"));

        resource.getContents().add(repository);
        resource.save(Collections.EMPTY_MAP);
    }

    /**
     * Create a new cost repository with the given interest rate and the specified time period.
     *
     * @param interest
     *            the interest rate
     * @param timePeriodYears
     *            the time period in years
     * @return the new cost repository
     */
    public static CostRepository createCostRepository(final double interest, final int timePeriodYears) {
        final CostRepository repository = costFactory.eINSTANCE.createCostRepository();
        repository.setInterest(interest);
        repository.setTimePeriodYears(timePeriodYears);
        return repository;
    }

    /**
     * Create a new fixed processing resource cost element in the specified cost repository.
     *
     * @param repository
     *            the repository where to create the cost element
     * @param initialCost
     *            the initial costs of the processing resource
     * @param operatingCost
     *            the operating costs of the processing resource
     * @param processingResource
     *            the processing resource for which to add the costs
     * @return the new fixed processing resource cost element
     */
    public static FixedProcessingResourceCost createFixedProcessingResourceCost(final CostRepository repository,
            final double initialCost, final double operatingCost,
            final ProcessingResourceSpecification processingResource) {
        final FixedProcessingResourceCost processingCost = costFactory.eINSTANCE.createFixedProcessingResourceCost();
        processingCost.setFixedInitialCost(initialCost);
        processingCost.setFixedOperatingCost(operatingCost);
        processingCost.setProcessingresourcespecification(processingResource);
        repository.getCost().add(processingCost);
        return processingCost;
    }
}
