package org.iobserve.analysis.model;

import java.util.HashMap;
import java.util.Map;

import org.palladiosimulator.pcm.core.entity.Entity;
import org.palladiosimulator.pcm.resourcetype.CommunicationLinkResourceType;
import org.palladiosimulator.pcm.resourcetype.ProcessingResourceType;
import org.palladiosimulator.pcm.resourcetype.ResourceRepository;
import org.palladiosimulator.pcm.resourcetype.ResourceType;
import org.palladiosimulator.pcm.resourcetype.SchedulingPolicy;

public class PalladioResourceRepositoryImpl implements PalladioResourceRepository {

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

	public PalladioResourceRepositoryImpl(final ResourceRepository repository) {
		this.repository = repository;
	}

	@Override
	public ProcessingResourceType cpu() {
		return (ProcessingResourceType) this.getResourceTypeByName(CPU);
	}

	@Override
	public ProcessingResourceType hdd() {
		return (ProcessingResourceType) this.getResourceTypeByName(HDD);
	}

	@Override
	public ProcessingResourceType delay() {
		return (ProcessingResourceType) this.getResourceTypeByName(DELAY);
	}

	@Override
	public CommunicationLinkResourceType lan() {
		return (CommunicationLinkResourceType) this.getResourceTypeByName(LAN);
	}

	@Override
	public SchedulingPolicy policyDelay() {
		return this.getSchedulingPolicyByName(POLICY_DELAY);
	}

	@Override
	public SchedulingPolicy policyProcessorSharing() {
		return this.getSchedulingPolicyByName(POLICY_PROCESSOR_SHARING);
	}

	@Override
	public SchedulingPolicy policyFCFS() {
		return this.getSchedulingPolicyByName(POLICY_FCFS);
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
