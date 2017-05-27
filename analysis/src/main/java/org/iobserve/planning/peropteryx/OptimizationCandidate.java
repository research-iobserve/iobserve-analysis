package org.iobserve.planning.peropteryx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.IModelRepository;
import org.iobserve.planning.changegroup.Action;
import org.iobserve.planning.core.optimization.IOptimizationCandidate;
import org.iobserve.planning.core.optimization.QualityMetric;
import org.iobserve.planning.utils.AssemblyGroupHelper;
import org.iobserve.planning.utils.AssemblyGroupHelper.AllocationGroup;
import org.iobserve.planning.utils.AssemblyGroupHelper.AssemblyGroup;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceContainerCloud;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

import de.uka.ipd.sdq.pcm.designdecision.Candidate;
import de.uka.ipd.sdq.pcm.designdecision.Choice;
import de.uka.ipd.sdq.pcm.designdecision.DegreeOfFreedomInstance;
import de.uka.ipd.sdq.pcm.designdecision.specific.AllocationDegree;
import de.uka.ipd.sdq.pcm.designdecision.specific.ResourceContainerReplicationDegree;

public class OptimizationCandidate implements IOptimizationCandidate {
	private static final Logger LOG = LogManager.getLogger();

	private final Map<String, QualityMetric> metrics = new LinkedHashMap<>();
	private final IModelRepository candidateRepository;
	private final IModelRepository originalModelRepository;
	private final Candidate candidate;
	private final Map<String, Choice> allocationChoices = new LinkedHashMap<>();
	private final Map<String, Choice> replicationChoices = new LinkedHashMap<>();

	private boolean valid = false;

	public OptimizationCandidate(IModelRepository candidateRepository, IModelRepository originalModelRepository,
			Candidate candidate) {
		this.candidateRepository = candidateRepository;
		this.originalModelRepository = originalModelRepository;
		this.candidate = candidate;

		this.sanitizeChoices();
	}

	private void sanitizeChoices() {
		Set<String> allocatedContainersSet = new HashSet<>();

		for (Choice choice : this.candidate.getChoices()) {
			Object containerChoice = choice.getValue();
			DegreeOfFreedomInstance dofInstance = choice.getDegreeOfFreedomInstance();
			if ((containerChoice instanceof ResourceContainer) && (dofInstance instanceof AllocationDegree)) {
				ResourceContainer container = (ResourceContainer) containerChoice;
				AllocationContext allocation = (AllocationContext) ((AllocationDegree) dofInstance).getPrimaryChanged();
				allocatedContainersSet.add(container.getEntityName());
				this.allocationChoices.put(allocation.getEntityName(), choice);
			}
		}

		for (Choice choice : this.candidate.getChoices()) {
			DegreeOfFreedomInstance dofInstance = choice.getDegreeOfFreedomInstance();
			Object containerChoice = dofInstance.getPrimaryChanged();
			if ((dofInstance instanceof ResourceContainerReplicationDegree)
					&& (containerChoice instanceof ResourceContainer)) {
				ResourceContainer container = (ResourceContainer) containerChoice;
				if (allocatedContainersSet.contains(container.getEntityName())) {
					this.replicationChoices.put(container.getEntityName(), choice);
				}
			}
		}

		this.candidate.getChoices().clear();
		this.candidate.getChoices().addAll(this.allocationChoices.values());
		this.candidate.getChoices().addAll(this.replicationChoices.values());
	}

	@Override
	public double getCostsPerHour() {
		return this.getQualityMetric("cost").getMetricValue().getValueDouble();
	}

	@Override
	public QualityMetric getQualityMetric(String name) {
		return this.metrics.get(name);
	}

	@Override
	public Collection<QualityMetric> getQualityMetrics() {
		return this.metrics.values();
	}

	public List<Choice> getChoices() {
		return this.candidate.getChoices();
	}

	public Map<String, QualityMetric> getMetricsMap() {
		return this.metrics;
	}

	@Override
	public String toString() {
		StringBuilder representation = new StringBuilder();
		representation.append("Candidate: [");
		representation.append("Model: ");
		representation.append(this.candidateRepository.getModelName());
		representation.append(", Choices: [");

		for (Choice choice : this.candidate.getChoices()) {
			representation.append(choice.getDegreeOfFreedomInstance().getEntityName());
			representation.append(" = ");
			Object value = choice.getValue();
			representation
					.append(value instanceof ResourceContainer ? ((ResourceContainer) value).getEntityName() : value);
			representation.append("; ");
		}
		representation.append("]]");
		return representation.toString();
	}

	@Override
	public List<Action> getChangeActions() {
		List<Action> changeActions = new ArrayList<>();

		LOG.info("Transforming candidate into change actions...");
		AllocationModelProvider allocationProvider = this.originalModelRepository.getAllocationModelProvider();

		Map<String, AssemblyGroup> assemblyGroupMap = AssemblyGroupHelper
				.extractAssemblyContextGroups(allocationProvider.getModel());

		for (Entry<String, AssemblyGroup> assemblyGroupEntry : assemblyGroupMap.entrySet()) {
			String assemblyGroupName = assemblyGroupEntry.getKey();
			AssemblyGroup assemblyGroup = assemblyGroupEntry.getValue();

			Map<String, AllocationGroup> allocationGroupMap = assemblyGroup.getAllocationContextGroups();

			for (Entry<String, AllocationGroup> allocationGroupEntry : allocationGroupMap.entrySet()) {
				String instanceType = allocationGroupEntry.getKey();
				AllocationGroup allocationGroup = allocationGroupEntry.getValue();

				Choice allocationChoice = this.allocationChoices
						.get(AssemblyGroupHelper.getAllocationGroupName(assemblyGroupName, instanceType));

				if (allocationChoice == null) {
					// Deallocation case
					LOG.info(String.format("Deallocation case found for assembly group '%s'.", assemblyGroupName));
					continue;
				}

				ResourceContainer containerChoice = (ResourceContainer) allocationChoice.getValue();

				Choice replicationChoice = this.replicationChoices.get(containerChoice.getEntityName());

				if ((replicationChoice == null) || !(containerChoice instanceof ResourceContainerCloud)) {
					// Not a cloud container, therefore no replication possible
					LOG.info(String.format("No cloud container replication possible for allocation group '%s'",
							assemblyGroupName));
					continue;
				}

				int nrOfChoiceReplicas = (int) replicationChoice.getValue();
				int nrOfCurrentReplicas = allocationGroup.getAllocationContexts().size();

				if (nrOfChoiceReplicas == nrOfCurrentReplicas) {
					// no action needed
					LOG.info(
							String.format("No action needed for allocation group '%s', number of replicas stays at %d.",
									assemblyGroupName, nrOfChoiceReplicas));
				} else if (nrOfChoiceReplicas > nrOfCurrentReplicas) {
					// Replication action needed
					LOG.info(String.format(
							"Replication action needed for allocation group '%s', scale number of replicas up from %d to %d.",
							assemblyGroupName, nrOfCurrentReplicas, nrOfChoiceReplicas));
				} else {
					// Dereplication action needed
					LOG.info(String.format(
							"Replication action needed for allocation group '%s', scale number of replicas down from %d to %d.",
							assemblyGroupName, nrOfCurrentReplicas, nrOfChoiceReplicas));
				}

				this.allocationChoices.remove(assemblyGroupName);
				this.replicationChoices.remove(containerChoice.getEntityName());
			}
		}

		// Remaining allocation choices represent new allocations
		LOG.info("Checking remaining allocations...");
		for (Entry<String, Choice> allocationChoiceEntry : this.allocationChoices.entrySet()) {
			String allocationGroupName = allocationChoiceEntry.getKey();
			Choice allocationChoice = allocationChoiceEntry.getValue();

			if (allocationChoice.getValue() instanceof ResourceContainerCloud) {
				ResourceContainerCloud cloudContainer = (ResourceContainerCloud) allocationChoice.getValue();

				Choice replicationChoice = this.replicationChoices.get(cloudContainer.getEntityName());

				if (replicationChoice == null) {
					LOG.info(String.format("No cloud container replication possible for allocation group '%s'",
							allocationGroupName));
					continue;
				}

				int nrOfReplicas = (int) replicationChoice.getValue();
				LOG.info(String.format(
						"Allocation action needed for allocation group '%s', "
								+ "allocating %d replicas of instance type '%s'.",
						allocationGroupName, nrOfReplicas, cloudContainer.getInstanceType()));
			}
		}

		return changeActions;
	}

	public boolean isValid() {
		return this.valid;
	}
}
