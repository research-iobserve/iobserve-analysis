package org.iobserve.analysis.model;

import java.io.IOException;
import java.util.Collections;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.palladiosimulator.pcm.resourceenvironment.ProcessingResourceSpecification;

import de.uka.ipd.sdq.pcm.cost.CostRepository;
import de.uka.ipd.sdq.pcm.cost.FixedProcessingResourceCost;
import de.uka.ipd.sdq.pcm.cost.costFactory;

/**
 * Model builder for cost models.
 *
 * @author Tobias Pöppke
 *
 */
public class CostModelBuilder {

	/**
	 * Save the given cost repository with the specified file name in the given
	 * folder.
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
		ResourceSet resSet = new ResourceSetImpl();
		Resource resource = resSet.createResource(costModelFolder.appendSegment(fileName).appendFileExtension("cost"));

		resource.getContents().add(repository);
		resource.save(Collections.EMPTY_MAP);
	}

	/**
	 * Create a new cost repository with the given interest rate and the
	 * specified time period.
	 *
	 * @param interest
	 *            the interest rate
	 * @param timePeriodYears
	 *            the time period in years
	 * @return the new cost repository
	 */
	public static CostRepository createCostRepository(final double interest, final int timePeriodYears) {
		CostRepository repository = costFactory.eINSTANCE.createCostRepository();
		repository.setInterest(interest);
		repository.setTimePeriodYears(timePeriodYears);
		return repository;
	}

	/**
	 * Create a new fixed processing resource cost element in the specified cost
	 * repository.
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
		FixedProcessingResourceCost processingCost = costFactory.eINSTANCE.createFixedProcessingResourceCost();
		processingCost.setFixedInitialCost(initialCost);
		processingCost.setFixedOperatingCost(operatingCost);
		processingCost.setProcessingresourcespecification(processingResource);
		repository.getCost().add(processingCost);
		return processingCost;
	}
}
