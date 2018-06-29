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
package org.iobserve.model.test.data;

import org.palladiosimulator.pcm.resourceenvironment.CommunicationLinkResourceSpecification;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ProcessingResourceSpecification;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;
import org.palladiosimulator.pcm.resourcetype.CommunicationLinkResourceType;
import org.palladiosimulator.pcm.resourcetype.ProcessingResourceType;
import org.palladiosimulator.pcm.resourcetype.ResourcetypeFactory;

/**
 * @author Reiner Jung
 *
 */
public final class ResourceEnvironmentDataFactory {

    public static final String QUERY_CONTAINER_1 = "user0815's MacBook";
    public static final String QUERY_CONTAINER_2 = "user0816's ThinkPad";
    public static final String QUERY_CONTAINER_3 = "user0817's Mainframe";

    public static final String BUSINESS_ORDER_CONTAINER = "businessOrderServer";
    public static final String PRIVATE_ORDER_CONTAINER = "privateOrderServer";
    public static final String PAYMENT_CONTAINER = "paymentServer";

    public static final String LAN_1 = "lan-1";

    private ResourceEnvironmentDataFactory() {
        // private empty constructor for factory
    }

    /**
     * Create a new resource environment model.
     *
     * @return returns the model.
     */
    public static ResourceEnvironment createResourceEnvironment() {
        final ResourceEnvironment resourceEnvironment = ResourceenvironmentFactory.eINSTANCE
                .createResourceEnvironment();
        resourceEnvironment.setEntityName("defaultResourceEnvironment");

        // Resource container types
        final ProcessingResourceType client1Type = ResourceEnvironmentDataFactory
                .createProcessionResourceType("MacBook");
        final ProcessingResourceType client2Type = ResourceEnvironmentDataFactory
                .createProcessionResourceType("ThinkPad");
        final ProcessingResourceType client3Type = ResourceEnvironmentDataFactory
                .createProcessionResourceType("Mainframe");
        final ProcessingResourceType orderServerType = ResourceEnvironmentDataFactory
                .createProcessionResourceType("Cisco Business Server");
        final ProcessingResourceType paymentServerType = ResourceEnvironmentDataFactory
                .createProcessionResourceType("Lenovo Security Server");

        // Processing resource specifications
        final ProcessingResourceSpecification client1Specification = ResourceEnvironmentDataFactory
                .createProcessingResourceSpecification(client1Type);
        final ProcessingResourceSpecification client2Specification = ResourceEnvironmentDataFactory
                .createProcessingResourceSpecification(client2Type);
        final ProcessingResourceSpecification client3Specification = ResourceEnvironmentDataFactory
                .createProcessingResourceSpecification(client3Type);
        final ProcessingResourceSpecification businessOrderServerSpecification = ResourceEnvironmentDataFactory
                .createProcessingResourceSpecification(orderServerType);
        final ProcessingResourceSpecification privateOrderServerSpecification = ResourceEnvironmentDataFactory
                .createProcessingResourceSpecification(orderServerType);
        final ProcessingResourceSpecification paymentServerSpecification = ResourceEnvironmentDataFactory
                .createProcessingResourceSpecification(paymentServerType);

        // Resource container
        final ResourceContainer client1 = ResourceEnvironmentDataFactory
                .createResourceContainer(ResourceEnvironmentDataFactory.QUERY_CONTAINER_1, client1Specification);
        final ResourceContainer client2 = ResourceEnvironmentDataFactory
                .createResourceContainer(ResourceEnvironmentDataFactory.QUERY_CONTAINER_2, client2Specification);
        final ResourceContainer client3 = ResourceEnvironmentDataFactory
                .createResourceContainer(ResourceEnvironmentDataFactory.QUERY_CONTAINER_3, client3Specification);
        final ResourceContainer businessOrderServer = ResourceEnvironmentDataFactory.createResourceContainer(
                ResourceEnvironmentDataFactory.BUSINESS_ORDER_CONTAINER, businessOrderServerSpecification);
        final ResourceContainer privateOrderServer = ResourceEnvironmentDataFactory.createResourceContainer(
                ResourceEnvironmentDataFactory.PRIVATE_ORDER_CONTAINER, privateOrderServerSpecification);
        final ResourceContainer paymentServer = ResourceEnvironmentDataFactory
                .createResourceContainer(ResourceEnvironmentDataFactory.PAYMENT_CONTAINER, paymentServerSpecification);

        resourceEnvironment.getResourceContainer_ResourceEnvironment().add(client1);
        resourceEnvironment.getResourceContainer_ResourceEnvironment().add(client2);
        resourceEnvironment.getResourceContainer_ResourceEnvironment().add(client3);
        resourceEnvironment.getResourceContainer_ResourceEnvironment().add(businessOrderServer);
        resourceEnvironment.getResourceContainer_ResourceEnvironment().add(privateOrderServer);
        resourceEnvironment.getResourceContainer_ResourceEnvironment().add(paymentServer);

        // linking

        // Linking resource type
        final CommunicationLinkResourceType lan1Type = ResourcetypeFactory.eINSTANCE
                .createCommunicationLinkResourceType();
        lan1Type.setEntityName("Cat.7 LAN");

        // Linking resource specification
        final CommunicationLinkResourceSpecification lan1Specification = ResourceenvironmentFactory.eINSTANCE
                .createCommunicationLinkResourceSpecification();
        lan1Specification.setCommunicationLinkResourceType_CommunicationLinkResourceSpecification(lan1Type);
        lan1Specification.setFailureProbability(0.01);

        // linking resource
        final LinkingResource lan1 = ResourceenvironmentFactory.eINSTANCE.createLinkingResource();

        // Linking resource
        lan1.setEntityName(ResourceEnvironmentDataFactory.LAN_1);
        lan1.setResourceEnvironment_LinkingResource(resourceEnvironment);
        lan1.setCommunicationLinkResourceSpecifications_LinkingResource(lan1Specification);
        lan1.getConnectedResourceContainers_LinkingResource().add(businessOrderServer);
        lan1.getConnectedResourceContainers_LinkingResource().add(paymentServer);

        resourceEnvironment.getLinkingResources__ResourceEnvironment().add(lan1);

        return resourceEnvironment;
    }

    public static ProcessingResourceSpecification createProcessingResourceSpecification(
            final ProcessingResourceType type) {
        final ProcessingResourceSpecification specification = ResourceenvironmentFactory.eINSTANCE
                .createProcessingResourceSpecification();
        specification.setActiveResourceType_ActiveResourceSpecification(type);
        specification.setMTTF(42);
        specification.setMTTR(42);

        return specification;
    }

    private static ProcessingResourceType createProcessionResourceType(final String name) {
        final ProcessingResourceType type = ResourcetypeFactory.eINSTANCE.createProcessingResourceType();

        type.setEntityName(name);

        return type;
    }

    public static ResourceContainer createResourceContainer(final String name,
            final ProcessingResourceSpecification specification) {
        /** optional test resource container with value */
        final ResourceContainer container = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();

        container.setEntityName(name);
        container.getActiveResourceSpecifications_ResourceContainer().add(specification);

        return container;
    }

    public static ResourceContainer findContainer(final ResourceEnvironment resourceEnvironment,
            final String containerName) {
        for (final ResourceContainer container : resourceEnvironment.getResourceContainer_ResourceEnvironment()) {
            if (container.getEntityName().equals(containerName)) {
                return container;
            }
        }
        return null;
    }

    public static ResourceContainer cloneContainer(final ResourceEnvironment resourceEnvironment,
            final String existingContainerName, final String newContainerName) {
        final ResourceContainer existingContainer = ResourceEnvironmentDataFactory.findContainer(resourceEnvironment,
                existingContainerName);
        final ProcessingResourceSpecification specification = existingContainer
                .getActiveResourceSpecifications_ResourceContainer().get(0);

        return ResourceEnvironmentDataFactory.createResourceContainer(newContainerName, specification);
    }

    public static LinkingResource findLinkingResource(final ResourceEnvironment resourceEnvironment,
            final String linkName) {
        for (final LinkingResource linkingResource : resourceEnvironment.getLinkingResources__ResourceEnvironment()) {
            if (linkingResource.getEntityName().equals(linkName)) {
                return linkingResource;
            }
        }

        return null;
    }
}
