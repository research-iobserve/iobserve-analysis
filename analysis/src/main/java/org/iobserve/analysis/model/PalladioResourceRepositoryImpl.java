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

    private final ResourceRepository repository;

    private static final String CPU = "CPU";
    private static final String LAN = "LAN";
    private static final String HDD = "HDD";
    private static final String DELAY = "DELAY";

    private static final String POLICY_DELAY = "Delay";
    private static final String POLICY_FCFS = "First-Come-First-Serve";
    private static final String POLICY_PROCESSOR_SHARING = "Processor Sharing";

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
    public ProcessingResourceType cpu() {
        return (ProcessingResourceType) this.getResourceTypeByName(PalladioResourceRepositoryImpl.CPU);
    }

    @Override
    public ProcessingResourceType hdd() {
        return (ProcessingResourceType) this.getResourceTypeByName(PalladioResourceRepositoryImpl.HDD);
    }

    @Override
    public ProcessingResourceType delay() {
        return (ProcessingResourceType) this.getResourceTypeByName(PalladioResourceRepositoryImpl.DELAY);
    }

    @Override
    public CommunicationLinkResourceType lan() {
        return (CommunicationLinkResourceType) this.getResourceTypeByName(PalladioResourceRepositoryImpl.LAN);
    }

    @Override
    public SchedulingPolicy policyDelay() {
        return this.getSchedulingPolicyByName(PalladioResourceRepositoryImpl.POLICY_DELAY);
    }

    @Override
    public SchedulingPolicy policyProcessorSharing() {
        return this.getSchedulingPolicyByName(PalladioResourceRepositoryImpl.POLICY_PROCESSOR_SHARING);
    }

    @Override
    public SchedulingPolicy policyFCFS() {
        return this.getSchedulingPolicyByName(PalladioResourceRepositoryImpl.POLICY_FCFS);
    }

    private Entity getResourceTypeByName(final String entityName) {
        if (!this.resourceTypes.containsKey(entityName)) {
            for (final ResourceType type : this.repository.getAvailableResourceTypes_ResourceRepository()) {
                if (type.getEntityName().equals(entityName)) {
                    this.resourceTypes.put(entityName, type);
                    break;
                }
            }
            if (!this.resourceTypes.containsKey(entityName)) {
                throw new RuntimeException(String.format("Could not find resource type '%s'", entityName));
            }
        }
        return this.resourceTypes.get(entityName);
    }

    private SchedulingPolicy getSchedulingPolicyByName(final String policyName) {
        if (!this.schedulingPolicies.containsKey(policyName)) {
            for (final SchedulingPolicy type : this.repository.getSchedulingPolicies__ResourceRepository()) {
                if (type.getEntityName().equals(policyName)) {
                    this.schedulingPolicies.put(policyName, type);
                    break;
                }
            }
            if (!this.schedulingPolicies.containsKey(policyName)) {
                throw new RuntimeException(String.format("Could not find scheduling policy '%s'", policyName));
            }
        }
        return this.schedulingPolicies.get(policyName);
    }
}
