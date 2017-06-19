package org.iobserve.analysis.model;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

import de.uka.ipd.sdq.pcm.designdecision.DecisionSpace;
import de.uka.ipd.sdq.pcm.designdecision.DegreeOfFreedomInstance;
import de.uka.ipd.sdq.pcm.designdecision.designdecisionFactory;
import de.uka.ipd.sdq.pcm.designdecision.specific.AllocationDegree;
import de.uka.ipd.sdq.pcm.designdecision.specific.ResourceContainerReplicationDegree;
import de.uka.ipd.sdq.pcm.designdecision.specific.specificFactory;

/**
 * Builder for design decision models.
 *
 * @author Tobias Pöppke
 *
 */
public class DesignDecisionModelBuilder {

	/**
	 * Saves the decision space with the specified file name in the given
	 * folder.
	 *
	 * @param designDecisionFolder
	 *            the folder to save the model
	 * @param fileName
	 *            the file name for the saved model
	 * @param decisionSpace
	 *            the decision space to save
	 * @throws IOException
	 *             if the decision space could not be saved
	 */
	public static void saveDecisionSpace(final URI designDecisionFolder, final String fileName, final DecisionSpace decisionSpace)
			throws IOException {
		ResourceSet resSet = new ResourceSetImpl();
		Resource resource = resSet.createResource(designDecisionFolder.appendSegment(fileName).appendFileExtension("designdecision"));

		resource.getContents().add(decisionSpace);
		resource.save(Collections.EMPTY_MAP);
	}

	/**
	 * Creates a new decision space with the specified name.
	 *
	 * @param name
	 *            the name of the decision space
	 * @return the new decision space
	 */
	public static DecisionSpace createDecisionSpace(final String name) {
		DecisionSpace decisionSpace = designdecisionFactory.eINSTANCE.createDecisionSpace();
		decisionSpace.setId(EcoreUtil.generateUUID());
		decisionSpace.setName(name);
		return decisionSpace;
	}

	/**
	 * Creates a new allocation degree in the specified decision space with the
	 * given name, associated allocation context and the given resource
	 * containers as the possible choices for this degree.
	 *
	 * @param decisionSpace
	 *            the decision space for creating the allocation degree
	 * @param name
	 *            the name of the allocation degree
	 * @param changedContext
	 *            the allocation context that is change by this degree
	 * @param availableResourceContainers
	 *            the available resource container choices for this degree
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
	 * Removes a degree of freedom in the specified decision space with the
	 * given name.
	 *
	 * @param decisionSpace
	 *            the decision space for creating the allocation degree
	 * @param name
	 *            the name of the allocation degree
	 */
	public static boolean deleteDegreeOfFreedom(final DecisionSpace decisionSpace, final String name) {
		return decisionSpace.getDegreesOfFreedom().removeIf(s -> s.getEntityName().equals(name));
	}

	/**
	 * Removes a degree of freedom in the specified decision space with the
	 * given name.
	 *
	 * @param decisionSpace
	 *            the decision space for creating the allocation degree
	 * @param name
	 *            the name of the allocation degree
	 */
	public static boolean deleteAllocationDegree(final DecisionSpace decisionSpace, final String allocationID) {
		AllocationDegree allocDegree = null;
		for (DegreeOfFreedomInstance dof : decisionSpace.getDegreesOfFreedom()) {
			if (dof instanceof AllocationDegree) {
				AllocationDegree currentAllocDegree = (AllocationDegree) dof;
				EObject primaryChanged = currentAllocDegree.getPrimaryChanged();
				if (primaryChanged instanceof AllocationContext) {
					AllocationContext currentAllocContext = (AllocationContext) primaryChanged;
					if (currentAllocContext.getId().equals(allocationID)) {
						allocDegree = currentAllocDegree;
						break;
					}
				}
			}
		}
		return decisionSpace.getDegreesOfFreedom().remove(allocDegree);
	}

	/**
	 * Creates a new resource container replication degree.
	 *
	 * The degree's name is set to the given name and the number of replicas is
	 * bounded by the to and from number of replicas parameters. Both bounds are
	 * included and all steps in between as well.
	 *
	 * @param decisionSpace
	 *            the decision space in which to create the replication degree
	 * @param name
	 *            the name of this replication degree
	 * @param replicatedContainer
	 *            the container that is to be replicated
	 * @param fromNrOfReplicas
	 *            the lower bound on the number of replicas
	 * @param toNrOfReplicas
	 *            the upper bound on the number of replicas
	 * @return the new replication degree
	 */
	public static ResourceContainerReplicationDegree createReplicationDegree(final DecisionSpace decisionSpace, final String name,
			final ResourceContainer replicatedContainer, final int fromNrOfReplicas, final int toNrOfReplicas) {
		ResourceContainerReplicationDegree degree = specificFactory.eINSTANCE.createResourceContainerReplicationDegree();
		degree.setEntityName(name);
		degree.setFrom(fromNrOfReplicas);
		degree.setTo(toNrOfReplicas);
		degree.setLowerBoundIncluded(true);
		degree.setUpperBoundIncluded(true);
		degree.setNumberOfSteps(0);
		degree.setPrimaryChanged(replicatedContainer);
		decisionSpace.getDegreesOfFreedom().add(degree);
		return degree;
	}

}
