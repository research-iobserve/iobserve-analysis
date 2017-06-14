package org.iobserve.service.generation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.Connector;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationRequiredRole;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.repository.RequiredRole;
import org.palladiosimulator.pcm.system.System;

/**
 * Modifies a PCM SystemModel
 * 
 * @author Philipp Weimann
 * @author Robert Heinrich
 */
public class SystemModification {

	private static final Logger LOG = LogManager.getLogger(ModelGeneration.class);

	private Repository repositoryModel;
	private RepositoryComponent[] repositoryComponents;
	private System systemModel;

	HashMap<String, List<RepositoryComponent>> duplicateRepositoryComponents;

	private HashMap<String, List<AssemblyContext>> openRequiredInterfaces = new HashMap<String, List<AssemblyContext>>();
	private HashMap<String, List<AssemblyContext>> openProvidedInterfaces = new HashMap<String, List<AssemblyContext>>();

	public SystemModification(System systemModel, Repository repositoryModel) {
		this.systemModel = systemModel;
		this.repositoryModel = repositoryModel;
		int componentsCount = repositoryModel.getComponents__Repository().size();
		this.repositoryComponents = repositoryModel.getComponents__Repository().toArray(new RepositoryComponent[componentsCount]);

		this.duplicateRepositoryComponents = new HashMap<String, List<RepositoryComponent>>();

		this.initDuplicateRepo(repositoryComponents);
	}

	/*
	 * Calculates all duplicated Repositories.
	 */
	private void initDuplicateRepo(RepositoryComponent[] comps) {
		LOG.info("Calculating euql Repository Components ...");

		HashMap<String, RepositoryComponent> existingInterfaceSig = new HashMap<String, RepositoryComponent>();

		for (RepositoryComponent comp : comps) {
			// Calc Sig
			String interfaceSignature = this.caluclateInterfaceSignature(comp);

			if (existingInterfaceSig.containsKey(interfaceSignature)) {
				// Sig already exists
				if (duplicateRepositoryComponents.containsKey(interfaceSignature)) {
					// Already 2+ equal sig exist
					duplicateRepositoryComponents.get(interfaceSignature).add(comp);
				} else {
					// First duplicate with this sig found
					List<RepositoryComponent> duplicates = new ArrayList<RepositoryComponent>();
					duplicates.add(existingInterfaceSig.get(interfaceSignature));
					duplicates.add(comp);
					duplicateRepositoryComponents.put(interfaceSignature, duplicates);
				}
			} else {
				// No equal sig found
				existingInterfaceSig.put(interfaceSignature, comp);
			}
		}

		LOG.info(String.format("Duplicate Interfacestructures found:\t%d", this.duplicateRepositoryComponents.keySet().size()));
	}

	/**
	 * Modifies the system model with a certain amount of deallocations
	 * 
	 * @param deallocations
	 *            the amount of deallocations
	 * @return the deallocated Assembly Contexts
	 */
	public List<AssemblyContext> modifySystem_Deallocations(int deallocations) {
		List<AssemblyContext> deallocatedAssemblyContexts = new LinkedList<AssemblyContext>();

		EList<AssemblyContext> assemblyContexts = this.systemModel.getAssemblyContexts__ComposedStructure();
		List<AssemblyConnector> assemblyConnectors = new ArrayList<AssemblyConnector>();
		for (Connector connector : this.systemModel.getConnectors__ComposedStructure()) {
			if (connector instanceof AssemblyConnector) {
				assemblyConnectors.add((AssemblyConnector) connector);
			}
		}

		for (int i = 0; i < deallocations && assemblyContexts.size() > 0; i++) {
			int randomIndex = ThreadLocalRandom.current().nextInt(assemblyContexts.size());
			AssemblyContext assemblyContext = assemblyContexts.remove(randomIndex);
			LOG.info("REMOVING: \tAssemblyContext: \t" + assemblyContext.getId());

			for (AssemblyConnector assemblyConnector : assemblyConnectors) {

				boolean removeAC = false;

				if (assemblyConnector.getProvidingAssemblyContext_AssemblyConnector() == assemblyContext) {

					String interfaceID = assemblyConnector.getRequiredRole_AssemblyConnector().getRequiredInterface__OperationRequiredRole().getId();
					if (!this.openRequiredInterfaces.containsKey(interfaceID)) {
						this.openRequiredInterfaces.put(interfaceID, new ArrayList<AssemblyContext>());
					}
					// Add open required AC
					this.openRequiredInterfaces.get(interfaceID).add(assemblyConnector.getRequiringAssemblyContext_AssemblyConnector());
					removeAC = true;

				} else if (assemblyConnector.getRequiringAssemblyContext_AssemblyConnector() == assemblyContext) {

					String interfaceID = assemblyConnector.getProvidedRole_AssemblyConnector().getProvidedInterface__OperationProvidedRole().getId();
					if (!this.openProvidedInterfaces.containsKey(interfaceID)) {
						this.openProvidedInterfaces.put(interfaceID, new ArrayList<AssemblyContext>());
					}
					// Add open providing AC
					this.openProvidedInterfaces.get(interfaceID).add(assemblyConnector.getProvidingAssemblyContext_AssemblyConnector());
					removeAC = true;
				}

				if (removeAC)
					this.systemModel.getConnectors__ComposedStructure().remove(assemblyConnector);

			}

			deallocatedAssemblyContexts.add(assemblyContext);
		}

		for (AssemblyContext ac : deallocatedAssemblyContexts)
			this.removeInterfaceMaps(ac);

		return deallocatedAssemblyContexts;
	}

	/**
	 * Generates a given amount of repository exchanges
	 * 
	 * @param changes
	 *            the amount of repository exchanges
	 * @return the amount of changes actually made
	 */
	public int modifySystem_ChangeComp(int changes) {
		int madeChanges = 0;

		// Copy the assembly context list
		EList<AssemblyContext> assemblyContextsList = this.systemModel.getAssemblyContexts__ComposedStructure();
		List<AssemblyContext> assemblyContexts = assemblyContextsList.stream().collect(Collectors.toList());

		// Randomize the list!
		Collections.shuffle(assemblyContexts);

		// Iterate over random list
		for (AssemblyContext assemblyContext : assemblyContexts) {

			String interfaceSignature = this.caluclateInterfaceSignature(assemblyContext.getEncapsulatedComponent__AssemblyContext());

			if (this.duplicateRepositoryComponents.containsKey(interfaceSignature)) {
				// Duplicate to containing comp is available!
				List<RepositoryComponent> equalComponents = this.duplicateRepositoryComponents.get(interfaceSignature);

				// Find non-self replacement
				RepositoryComponent newRepoComp = null;
				for (int j = 0; newRepoComp != null && j < equalComponents.size() * 10; j++) {
					int randomIndex = ThreadLocalRandom.current().nextInt(equalComponents.size());
					RepositoryComponent candidate = equalComponents.get(randomIndex);
					if (!assemblyContext.getEncapsulatedComponent__AssemblyContext().getId().equals(candidate.getId()))
						newRepoComp = candidate;
				}

				if (newRepoComp != null) {
					// Exchange containing comp
					assemblyContext.setEncapsulatedComponent__AssemblyContext(newRepoComp);
					madeChanges++;
					if (madeChanges == changes)
						break;
				}
			}
		}

		return madeChanges;
	}

	/**
	 * Generates a certain amount of Assembly Context allocations
	 * 
	 * @param allocations
	 *            the amount of allocations
	 * @return the allocated Assembly Contexts
	 */
	public List<AssemblyContext> modifySystem_Allocate(int allocations) {
		List<AssemblyContext> newACs = null;

		SystemGeneration sysGen = new SystemGeneration(this.repositoryModel, this.systemModel, openRequiredInterfaces, openProvidedInterfaces);
		newACs = sysGen.addAssemblyContexts(allocations, "alloc");

		return newACs;
	}

	/*
	 * Calculates the inteface signuature of a component.
	 */
	private String caluclateInterfaceSignature(RepositoryComponent comp) {

		StringBuilder sb = new StringBuilder();

		// Calculate Providing Signature
		List<OperationProvidedRole> provInterfaces = new ArrayList<OperationProvidedRole>();
		for (ProvidedRole provInterface : comp.getProvidedRoles_InterfaceProvidingEntity()) {
			if (provInterface instanceof OperationProvidedRole)
				provInterfaces.add((OperationProvidedRole) provInterface);
		}
		// Order Interfaces
		Collections.sort(provInterfaces, (OperationProvidedRole a1, OperationProvidedRole a2) -> a1.getProvidedInterface__OperationProvidedRole()
				.getId().compareTo(a2.getProvidedInterface__OperationProvidedRole().getId()));

		// Build sig part from Prefix + Interface ID
		for (OperationProvidedRole provInterface : provInterfaces) {
			sb.append(";p_" + provInterface.getProvidedInterface__OperationProvidedRole().getId());
		}

		// Calculate Requiring Signature
		// Get Operation Interfaces
		List<OperationRequiredRole> reqInterfaces = new ArrayList<OperationRequiredRole>();
		for (RequiredRole reqInterface : comp.getRequiredRoles_InterfaceRequiringEntity()) {
			if (reqInterface instanceof OperationRequiredRole)
				reqInterfaces.add((OperationRequiredRole) reqInterface);
		}
		// Order Interfaces
		Collections.sort(reqInterfaces, (OperationRequiredRole a1, OperationRequiredRole a2) -> a1.getRequiredInterface__OperationRequiredRole()
				.getId().compareTo(a2.getRequiredInterface__OperationRequiredRole().getId()));

		// Build sig part from Prefix + Interface ID
		for (OperationRequiredRole reqInterface : reqInterfaces) {
			sb.append(";r_" + reqInterface.getRequiredInterface__OperationRequiredRole().getId());
		}

		return sb.toString();
	}

	/*
	 * Removes a given Interface from the support infrastructure
	 */
	private void removeInterfaceMaps(AssemblyContext assemblyContext) {
		for (String key : this.openProvidedInterfaces.keySet()) {
			this.openProvidedInterfaces.get(key).remove(assemblyContext);
		}

		for (String key : this.openRequiredInterfaces.keySet()) {
			this.openRequiredInterfaces.get(key).remove(assemblyContext);
		}
	}

}
