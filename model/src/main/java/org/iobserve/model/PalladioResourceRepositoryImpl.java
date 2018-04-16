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
package org.iobserve.model;

import java.util.HashMap;
import java.util.Map;

import org.palladiosimulator.pcm.core.entity.Entity;
import org.palladiosimulator.pcm.resourcetype.CommunicationLinkResourceType;
import org.palladiosimulator.pcm.resourcetype.ProcessingResourceType;
import org.palladiosimulator.pcm.resourcetype.ResourceRepository;
import org.palladiosimulator.pcm.resourcetype.ResourceType;
import org.palladiosimulator.pcm.resourcetype.SchedulingPolicy;

/**
 * Convenience class to access palladio resource repository components.
 *
 * @author Fabian Keller
 *
 */
public class PalladioResourceRepositoryImpl implements IPalladioResourceRepository {

    private static final String CPU_RESOURCE = "CPU";
    private static final String LAN_RESOURCE = "LAN";
    private static final String HDD_RESOURCE = "HDD";
    private static final String DELAY_RESOURCE = "DELAY";

    private static final String POLICY_DELAY = "Delay";
    private static final String POLICY_FCFS = "First-Come-First-Serve";
    private static final String POLICY_PROCESSOR_SHARING = "Processor Sharing";

    private final ResourceRepository repository;

    private final Map<String, Entity> resourceTypes = new HashMap<>();
    private final Map<String, SchedulingPolicy> schedulingPolicies = new HashMap<>();

    /**
     * Create an Palladio resource repository.
     *
     * @param repository
     *            the resource repository
     */
    public PalladioResourceRepositoryImpl(final ResourceRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProcessingResourceType cpu() throws ModelHandlingErrorException {
        return (ProcessingResourceType) this.getResourceTypeByName(PalladioResourceRepositoryImpl.CPU_RESOURCE);
    }

    @Override
    public ProcessingResourceType hdd() throws ModelHandlingErrorException {
        return (ProcessingResourceType) this.getResourceTypeByName(PalladioResourceRepositoryImpl.HDD_RESOURCE);
    }

    @Override
    public ProcessingResourceType delay() throws ModelHandlingErrorException {
        return (ProcessingResourceType) this.getResourceTypeByName(PalladioResourceRepositoryImpl.DELAY_RESOURCE);
    }

    @Override
    public CommunicationLinkResourceType lan() throws ModelHandlingErrorException {
        return (CommunicationLinkResourceType) this.getResourceTypeByName(PalladioResourceRepositoryImpl.LAN_RESOURCE);
    }

    @Override
    public SchedulingPolicy policyDelay() throws ModelHandlingErrorException {
        return this.getSchedulingPolicyByName(PalladioResourceRepositoryImpl.POLICY_DELAY);
    }

    @Override
    public SchedulingPolicy policyProcessorSharing() throws ModelHandlingErrorException {
        return this.getSchedulingPolicyByName(PalladioResourceRepositoryImpl.POLICY_PROCESSOR_SHARING);
    }

    @Override
    public SchedulingPolicy policyFCFS() throws ModelHandlingErrorException {
        return this.getSchedulingPolicyByName(PalladioResourceRepositoryImpl.POLICY_FCFS);
    }

    private Entity getResourceTypeByName(final String entityName) throws ModelHandlingErrorException {
        if (!this.resourceTypes.containsKey(entityName)) {
            for (final ResourceType type : this.repository.getAvailableResourceTypes_ResourceRepository()) {
                if (type.getEntityName().equals(entityName)) {
                    this.resourceTypes.put(entityName, type);
                    break;
                }
            }
            if (!this.resourceTypes.containsKey(entityName)) {
                throw new ModelHandlingErrorException(String.format("Could not find resource type '%s'", entityName));
            }
        }
        return this.resourceTypes.get(entityName);
    }

    private SchedulingPolicy getSchedulingPolicyByName(final String policyName) throws ModelHandlingErrorException {
        if (!this.schedulingPolicies.containsKey(policyName)) {
            for (final SchedulingPolicy type : this.repository.getSchedulingPolicies__ResourceRepository()) {
                if (type.getEntityName().equals(policyName)) {
                    this.schedulingPolicies.put(policyName, type);
                    break;
                }
            }
            if (!this.schedulingPolicies.containsKey(policyName)) {
                throw new ModelHandlingErrorException(
                        String.format("Could not find scheduling policy '%s'", policyName));
            }
        }
        return this.schedulingPolicies.get(policyName);
    }
}
