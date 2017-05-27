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

public class CostModelBuilder {

	public static void saveCostRepository(final URI costModelFolder, final String fileName,
			final CostRepository repository) throws IOException {
		ResourceSet resSet = new ResourceSetImpl();
		Resource resource = resSet.createResource(costModelFolder.appendSegment(fileName).appendFileExtension("cost"));

		resource.getContents().add(repository);
		resource.save(Collections.EMPTY_MAP);
	}

	public static CostRepository createCostRepository(final double interest, final int timePeriodYears) {
		CostRepository repository = costFactory.eINSTANCE.createCostRepository();
		repository.setInterest(interest);
		repository.setTimePeriodYears(timePeriodYears);
		return repository;
	}

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
