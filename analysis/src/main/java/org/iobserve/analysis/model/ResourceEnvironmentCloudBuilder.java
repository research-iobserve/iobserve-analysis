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
package org.iobserve.analysis.model;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceContainerCloud;
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceenvironmentcloudFactory;
import org.palladiosimulator.pcm.core.CoreFactory;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.resourceenvironment.CommunicationLinkResourceSpecification;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ProcessingResourceSpecification;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;

/**
 * Model builder for cloud resource environment models.
 *
 * @author Tobias Pöppke
 *
 */
public class ResourceEnvironmentCloudBuilder {

    // 100 ms latency is a conservative value
    private static final double DEFAULT_LATENCY = 100;

    // Cloud providers normally have fast internet connections, so 100 Mbps as
    // default throughput should be ok
    private static final double DEFAULT_THROUGHPUT = 100000000;

    // Default failure probability is 10%
    private static final double DEFAULT_FAILURE_PROB = 0.1;

    /**
     * Creates a new processing resource specification for the given resource container.
     *
     * There is no check if the container already has a processing resource.
     *
     * @param nrOfCores
     *            the number of cores the processing resource should have
     * @param processingRate
     *            the processing rate of the resource
     * @param container
     *            the container in which to create the processing resource.
     * @return
     */
    public static ProcessingResourceSpecification createProcessingResource(final int nrOfCores,
            final double processingRate, final ResourceContainer container) {
        final ProcessingResourceSpecification processor = ResourceenvironmentFactory.eINSTANCE
                .createProcessingResourceSpecification();
        final PCMRandomVariable pcmProcessingRate = CoreFactory.eINSTANCE.createPCMRandomVariable();
        pcmProcessingRate.setSpecification(Double.toString(processingRate));
        processor.setActiveResourceType_ActiveResourceSpecification(
                PalladioResourceRepository.INSTANCE.resources().cpu());
        processor.setId(EcoreUtil.generateUUID());
        processor.setNumberOfReplicas(nrOfCores);
        processor.setMTTF(0.0);
        processor.setMTTR(0.0);
        processor.setProcessingRate_ProcessingResourceSpecification(pcmProcessingRate);
        processor.setRequiredByContainer(true);
        processor.setResourceContainer_ProcessingResourceSpecification(container);
        processor.setSchedulingPolicy(PalladioResourceRepository.INSTANCE.resources().policyProcessorSharing());
        return processor;
    }

    /**
     * Create a {@link ResourceContainerCloud} with the given name, without checking if it already
     * exists.
     *
     * @param model
     *            resource environment model
     * @param name
     *            name of the new container
     * @return builder
     */
    public static ResourceContainerCloud createResourceContainer(final ResourceEnvironment model, final String name) {
        final ResourceContainerCloud resContainer = ResourceenvironmentcloudFactory.eINSTANCE
                .createResourceContainerCloud();
        resContainer.setEntityName(name);
        model.getResourceContainer_ResourceEnvironment().add(resContainer);
        return resContainer;
    }

    /**
     * Creates a linking resource with the given name.
     *
     * @param model
     *            resource environment model
     * @param spec
     *            specification of the linking resource, default values apply if this is null
     * @param name
     *            name for the new linking resource
     * @return link instance, already added to the model
     */
    public static LinkingResource createLinkingResource(final ResourceEnvironment model,
            final CommunicationLinkResourceSpecification spec, final String name) {
        final LinkingResource link = ResourceenvironmentFactory.eINSTANCE.createLinkingResource();

        if (spec == null) {
            final CommunicationLinkResourceSpecification defaultSpec = ResourceEnvironmentCloudBuilder
                    .createLinkingResourceSpecification(model, ResourceEnvironmentCloudBuilder.DEFAULT_THROUGHPUT,
                            ResourceEnvironmentCloudBuilder.DEFAULT_FAILURE_PROB,
                            ResourceEnvironmentCloudBuilder.DEFAULT_LATENCY);
            link.setCommunicationLinkResourceSpecifications_LinkingResource(defaultSpec);
        } else {
            link.setCommunicationLinkResourceSpecifications_LinkingResource(spec);
        }

        link.setEntityName(name);
        link.setResourceEnvironment_LinkingResource(model);
        return link;
    }

    /**
     * Creates a communication link resource specification with the given properties.
     *
     * @param model
     *            resource environment model
     * @param throughput
     *            throughput of the link specification
     * @param failureProbability
     *            failure probability of the link specification
     * @param latency
     *            latency of the link specification
     * @return
     */
    public static CommunicationLinkResourceSpecification createLinkingResourceSpecification(
            final ResourceEnvironment model, final double throughput, final double failureProbability,
            final double latency) {
        final CommunicationLinkResourceSpecification spec = ResourceenvironmentFactory.eINSTANCE
                .createCommunicationLinkResourceSpecification();
        spec.setCommunicationLinkResourceType_CommunicationLinkResourceSpecification(
                PalladioResourceRepository.INSTANCE.resources().lan());
        spec.setFailureProbability(failureProbability);

        final PCMRandomVariable throughputPCM = CoreFactory.eINSTANCE.createPCMRandomVariable();
        throughputPCM.setSpecification(Double.toString(throughput));
        spec.setThroughput_CommunicationLinkResourceSpecification(throughputPCM);

        final PCMRandomVariable latencyPCM = CoreFactory.eINSTANCE.createPCMRandomVariable();
        latencyPCM.setSpecification(Double.toString(latency));
        spec.setLatency_CommunicationLinkResourceSpecification(latencyPCM);

        return spec;
    }
}
