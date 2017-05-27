package org.iobserve.analysis.model;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

import de.uka.ipd.sdq.pcm.designdecision.DecisionSpace;
import de.uka.ipd.sdq.pcm.designdecision.designdecisionFactory;
import de.uka.ipd.sdq.pcm.designdecision.specific.AllocationDegree;
import de.uka.ipd.sdq.pcm.designdecision.specific.ResourceContainerReplicationDegree;
import de.uka.ipd.sdq.pcm.designdecision.specific.specificFactory;

/**
 * TODO
 *
 * @author Tobias Pöppke
 *
 */
public class DesignDecisionModelBuilder {

	/**
	 * TODO
	 *
	 * @param designDecisionFolder
	 * @param fileName
	 * @param decisionSpace
	 * @throws IOException
	 */
	public static void saveDecisionSpace(final URI designDecisionFolder, final String fileName,
			final DecisionSpace decisionSpace) throws IOException {
		ResourceSet resSet = new ResourceSetImpl();
		Resource resource = resSet
				.createResource(designDecisionFolder.appendSegment(fileName).appendFileExtension("designdecision"));

		resource.getContents().add(decisionSpace);
		resource.save(Collections.EMPTY_MAP);
	}

	/**
	 * TODO
	 *
	 * @param designDecisionFile
	 * @param name
	 * @return
	 */
	public static DecisionSpace createDecisionSpace(final String name) {
		DecisionSpace decisionSpace = designdecisionFactory.eINSTANCE.createDecisionSpace();
		decisionSpace.setId(EcoreUtil.generateUUID());
		decisionSpace.setName(name);
		return decisionSpace;
	}

	/**
	 * TODO
	 *
	 * @param decisionSpace
	 * @param name
	 * @param changedContext
	 * @param availableResourceContainers
	 * @return
	 */
	public static AllocationDegree createAllocationDegree(final DecisionSpace decisionSpace, final String name,
			final AllocationContext changedContext, final Collection<ResourceContainer> availableResourceContainers) {
		AllocationDegree allocationDegree = specificFactory.eINSTANCE.createAllocationDegree();
		allocationDegree.setEntityName(name);
		allocationDegree.setPrimaryChanged(changedContext);
		allocationDegree.getClassDesignOptions().addAll(availableResourceContainers);
		allocationDegree.getChangeableElements().addAll(availableResourceContainers);
		decisionSpace.getDegreesOfFreedom().add(allocationDegree);
		return allocationDegree;
	}

	/**
	 * TODO
	 *
	 * @param decisionSpace
	 * @param name
	 * @param replicatedContainer
	 * @param fromNrOfReplicas
	 * @param toNrOfReplicas
	 * @param allocationContexts
	 * @return
	 */
	public static ResourceContainerReplicationDegree createReplicationDegree(final DecisionSpace decisionSpace,
			final String name, final ResourceContainer replicatedContainer, final int fromNrOfReplicas,
			final int toNrOfReplicas) {
		ResourceContainerReplicationDegree degree = specificFactory.eINSTANCE
				.createResourceContainerReplicationDegree();
		degree.setEntityName(name);
		degree.setFrom(fromNrOfReplicas);
		degree.setTo(toNrOfReplicas);
		degree.setLowerBoundIncluded(true);
		degree.setUpperBoundIncluded(true);
		degree.setNumberOfSteps(0);
		degree.setPrimaryChanged(replicatedContainer);
		// degree.getChangeableElements().addAll(allocationContexts);
		decisionSpace.getDegreesOfFreedom().add(degree);
		return degree;
	}

}
