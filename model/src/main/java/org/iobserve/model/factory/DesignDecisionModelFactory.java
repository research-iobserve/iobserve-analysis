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
import java.util.Collection;
import java.util.Collections;

import de.uka.ipd.sdq.pcm.designdecision.DecisionSpace;
import de.uka.ipd.sdq.pcm.designdecision.designdecisionFactory;
import de.uka.ipd.sdq.pcm.designdecision.specific.AllocationDegree;
import de.uka.ipd.sdq.pcm.designdecision.specific.ResourceContainerReplicationDegree;
import de.uka.ipd.sdq.pcm.designdecision.specific.specificFactory;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * Builder for design decision models.
 *
 * @author Tobias Pöppke
 *
 */
public class DesignDecisionModelFactory {

    /**
     * Saves the decision space with the specified file name in the given folder.
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
    public static void saveDecisionSpace(final URI designDecisionFolder, final String fileName,
            final DecisionSpace decisionSpace) throws IOException {
        final ResourceSet resSet = new ResourceSetImpl();
        final Resource resource = resSet
                .createResource(designDecisionFolder.appendSegment(fileName).appendFileExtension("designdecision"));

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
        final DecisionSpace decisionSpace = designdecisionFactory.eINSTANCE.createDecisionSpace();
        decisionSpace.setId(EcoreUtil.generateUUID());
        decisionSpace.setName(name);
        return decisionSpace;
    }

    /**
     * Creates a new allocation degree in the specified decision space with the given name,
     * associated allocation context and the given resource containers as the possible choices for
     * this degree.
     *
     * @param decisionSpace
     *            the decision space for creating the allocation degree
     * @param name
     *            the name of the allocation degree
     * @param changedContext
     *            the allocation context that is change by this degree
     * @param availableResourceContainers
     *            the available resource container choices for this degree
     * @return an allocation degree
     */
    public static AllocationDegree createAllocationDegree(final DecisionSpace decisionSpace, final String name,
            final AllocationContext changedContext, final Collection<ResourceContainer> availableResourceContainers) {
        final AllocationDegree allocationDegree = specificFactory.eINSTANCE.createAllocationDegree();
        allocationDegree.setEntityName(name);
        allocationDegree.setPrimaryChanged(changedContext);
        allocationDegree.getClassDesignOptions().addAll(availableResourceContainers);
        allocationDegree.getChangeableElements().addAll(availableResourceContainers);
        decisionSpace.getDegreesOfFreedom().add(allocationDegree);
        return allocationDegree;
    }

    /**
     * Creates a new resource container replication degree.
     *
     * The degree's name is set to the given name and the number of replicas is bounded by the to
     * and from number of replicas parameters. Both bounds are included and all steps in between as
     * well.
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
    public static ResourceContainerReplicationDegree createReplicationDegree(final DecisionSpace decisionSpace,
            final String name, final ResourceContainer replicatedContainer, final int fromNrOfReplicas,
            final int toNrOfReplicas) {
        final ResourceContainerReplicationDegree degree = specificFactory.eINSTANCE
                .createResourceContainerReplicationDegree();
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
