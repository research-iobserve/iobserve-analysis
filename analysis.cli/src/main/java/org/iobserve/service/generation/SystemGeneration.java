package org.iobserve.service.generation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.gradle.internal.impldep.com.esotericsoftware.minlog.Log;
import org.palladiosimulator.pcm.compositionprivacy.AssemblyConnectorPrivacy;
import org.palladiosimulator.pcm.compositionprivacy.CompositionPrivacyFactory;
import org.palladiosimulator.pcm.compositionprivacy.DataPrivacyLvl;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.CompositionFactory;
import org.palladiosimulator.pcm.core.composition.Connector;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationRequiredRole;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.repository.RequiredRole;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.system.impl.SystemFactoryImpl;

/**
 * Generates a PCM Privacy compliant SystemModel
 * 
 * @author Philipp Weimann
 * @author Robert Heinrich
 */
public class SystemGeneration {

	private static Logger LOG = LogManager.getLogger(SystemGeneration.class);

	private static final CompositionFactory COMPOSITION_FACTORY = CompositionFactory.eINSTANCE;
	private static final CompositionPrivacyFactory COMPOSITION_PRIVACY_FACTORY = CompositionPrivacyFactory.eINSTANCE;

	private Repository repo;
	private RepositoryComponent[] components;
	private System system;

	private HashMap<String, List<AssemblyContext>> openRequiredInterfaces;
	private HashMap<String, List<AssemblyContext>> openProvidedInterfaces;
	private HashMap<String, List<AssemblyContext>> connectedProvidedInterfaces;
	private Set<AssemblyContext> unconnectedAssemblyContextes;

	/**
	 * 
	 * @param repo
	 */
	public SystemGeneration(Repository repo) {
		super();
		this.repo = repo;
		this.components = repo.getComponents__Repository().toArray(new RepositoryComponent[repo.getComponents__Repository().size()]);

		initInterfaceMaps();

		this.system = SystemFactoryImpl.init().createSystem();
		this.system.setEntityName(this.repo.getEntityName());
	}

	/**
	 * 
	 * @param repo
	 * @param system
	 * @param openRequiredInterfaces
	 * @param openProvidedInterfaces
	 */
	public SystemGeneration(Repository repo, System system, HashMap<String, List<AssemblyContext>> openRequiredInterfaces,
			HashMap<String, List<AssemblyContext>> openProvidedInterfaces) {
		this.repo = repo;
		this.components = repo.getComponents__Repository().toArray(new RepositoryComponent[repo.getComponents__Repository().size()]);

		this.initInterfaceMaps();

		this.system = system;
		this.fillInterfaceMaps(openRequiredInterfaces, openProvidedInterfaces);
	}

	private void initInterfaceMaps() {
		this.openRequiredInterfaces = new HashMap<String, List<AssemblyContext>>();
		this.openProvidedInterfaces = new HashMap<String, List<AssemblyContext>>();
		this.connectedProvidedInterfaces = new HashMap<String, List<AssemblyContext>>();
		this.unconnectedAssemblyContextes = new HashSet<AssemblyContext>();

		for (RepositoryComponent component : this.components) {
			for (ProvidedRole provRole : component.getProvidedRoles_InterfaceProvidingEntity()) {
				if (!(provRole instanceof OperationProvidedRole))
					continue;

				OperationProvidedRole provInterface = (OperationProvidedRole) provRole;
				String interfaceID = provInterface.getProvidedInterface__OperationProvidedRole().getId();
				if (!this.openProvidedInterfaces.containsKey(interfaceID)) {
					this.openProvidedInterfaces.put(interfaceID, new LinkedList<AssemblyContext>());
					this.openRequiredInterfaces.put(interfaceID, new LinkedList<AssemblyContext>());
					this.connectedProvidedInterfaces.put(interfaceID, new LinkedList<AssemblyContext>());
				}
			}

			for (RequiredRole requRole : component.getRequiredRoles_InterfaceRequiringEntity()) {
				if (!(requRole instanceof OperationRequiredRole))
					continue;

				OperationRequiredRole reqInterface = (OperationRequiredRole) requRole;
				String interfaceID = reqInterface.getRequiredInterface__OperationRequiredRole().getId();
				if (!this.openProvidedInterfaces.containsKey(interfaceID)) {
					this.openProvidedInterfaces.put(interfaceID, new LinkedList<AssemblyContext>());
					this.openRequiredInterfaces.put(interfaceID, new LinkedList<AssemblyContext>());
					this.connectedProvidedInterfaces.put(interfaceID, new LinkedList<AssemblyContext>());
				}
			}
		}
	}

	private void fillInterfaceMaps(HashMap<String, List<AssemblyContext>> openRequiredInterfaces,
			HashMap<String, List<AssemblyContext>> openProvidedInterfaces) {
		for (String key : openRequiredInterfaces.keySet()) {
			this.openRequiredInterfaces.replace(key, openRequiredInterfaces.get(key));
		}

		for (String key : openProvidedInterfaces.keySet()) {
			this.openProvidedInterfaces.replace(key, openProvidedInterfaces.get(key));
		}

		for (Connector connector : this.system.getConnectors__ComposedStructure()) {
			if (connector instanceof AssemblyConnector) {
				AssemblyConnector assemblyConnector = (AssemblyConnector) connector;
				String interfaceID = assemblyConnector.getProvidedRole_AssemblyConnector().getProvidedInterface__OperationProvidedRole().getId();
				this.connectedProvidedInterfaces.get(interfaceID).add(assemblyConnector.getProvidingAssemblyContext_AssemblyConnector());
			}
		}
	}

	/**
	 * Generates a new System Model
	 * 
	 * @param assemblyContextCount
	 *            the amount of Assembly Contexts to generate
	 * @return the System Model
	 */
	public System generateSystemModel(int assemblyContextCount) {

		for (int i = 0; i < assemblyContextCount; i++) {
			String namePrefix = Integer.toString(i);
			this.generateNewAC(namePrefix);
		}

		this.finalizeModelGeneration();

		return this.system;
	}

	/**
	 * Generates a certain amount of new Assembly Contexts into the existing
	 * System Model
	 * 
	 * @param assemblyContextCount
	 *            the amount of Assembly Contexts
	 * @param prefix
	 *            the special prefix
	 * @return the new Assembly Contexts
	 */
	public List<AssemblyContext> addAssemblyContexts(int assemblyContextCount, String prefix) {

		List<AssemblyContext> newACs = new ArrayList<AssemblyContext>();
		for (int i = 0; i < assemblyContextCount; i++) {
			String namePrefix = Integer.toString(i) + "_" + prefix;
			AssemblyContext newAC = this.generateNewAC(namePrefix);
			newACs.add(newAC);

			LOG.info("CREATING: \tAssemblyContext: \t" + newAC.getId());
		}

		this.finalizeModelGeneration();

		return newACs;
	}

	/*
	 * Geneartes a new AC
	 */
	private AssemblyContext generateNewAC(String namePrefix) {
		int randomInt = ThreadLocalRandom.current().nextInt(components.length);
		RepositoryComponent randComponent = this.components[randomInt];

		AssemblyContext newAC = COMPOSITION_PRIVACY_FACTORY.createAssemblyContextPrivacy();
		newAC.setEntityName(namePrefix + "_AC_" + randComponent.getEntityName());
		newAC.setEncapsulatedComponent__AssemblyContext(randComponent);
		newAC.setParentStructure__AssemblyContext(system);

		this.system.getAssemblyContexts__ComposedStructure().add(newAC);
		this.unconnectedAssemblyContextes.add(newAC);

		EList<ProvidedRole> provRoles = randComponent.getProvidedRoles_InterfaceProvidingEntity();
		this.connectProvidedInterfaces(newAC, provRoles);

		EList<RequiredRole> requRoles = randComponent.getRequiredRoles_InterfaceRequiringEntity();
		this.connectRequiredInterfaces(newAC, requRoles);

		return newAC;
	}

	/*
	 * Connects open required interfaces to existing providing interfaces
	 */
	private void finalizeModelGeneration() {
		Set<String> interfaceIDs = this.openRequiredInterfaces.keySet();

		// Iterate over all not connected required interfaces
		for (String interfaceID : interfaceIDs) {
			if (this.openRequiredInterfaces.get(interfaceID).size() == 0)
				continue;

			List<AssemblyContext> openReqACs = this.openRequiredInterfaces.get(interfaceID);
			for (AssemblyContext requAC : openReqACs) {

				AssemblyContext provAC = null;
				if (this.openProvidedInterfaces.get(interfaceID).size() > 0)
					// First search the not connected providing interfaces
					provAC = this.getAndRemoveRandomAC(this.openProvidedInterfaces.get(interfaceID), requAC);
				if (provAC == null && this.connectedProvidedInterfaces.get(interfaceID).size() > 0)
					// Second search the connected providing interfaces
					provAC = this.getAndRemoveRandomAC(this.connectedProvidedInterfaces.get(interfaceID), requAC);

				if (provAC != null) {
					// Create assembly connector and add to connected providing
					// interfaces
					this.createAssemblyConnector(requAC, provAC, interfaceID);
					this.connectedProvidedInterfaces.get(interfaceID).add(provAC);
				} else {
					throw new RuntimeException("A required interface has no matching provided interface!");
				}
			}
		}

		int unconnectedAssemblyContextCount = this.unconnectedAssemblyContextes.size();
		LOG.info(String.format("There are \t%d\t unconnected AssemblyContexts, deleting and retrying!", unconnectedAssemblyContextCount));
		if (unconnectedAssemblyContextCount > 0) {
			for (AssemblyContext ac : this.unconnectedAssemblyContextes) {
				this.removeAssemblyContext(ac);
			}
			this.unconnectedAssemblyContextes.clear();
			this.addAssemblyContexts(unconnectedAssemblyContextCount, "");
		}

	}

	/*
	 * This method tires to connect all providing interfaces to non connected
	 * required interfaces!
	 * 
	 * If this is not possible, it will be added to non connected providing
	 * interfaces for later connecting!
	 */
	private int connectProvidedInterfaces(AssemblyContext newAC, EList<ProvidedRole> provRoles) {
		int connectionsCreated = 0;

		for (ProvidedRole provRole : provRoles) {
			// Connect all providing interfaces!
			if (!(provRole instanceof OperationProvidedRole))
				continue;

			boolean connected = false;
			OperationProvidedRole provInterface = (OperationProvidedRole) provRole;
			String interfaceID = provInterface.getProvidedInterface__OperationProvidedRole().getId();
			if (this.openRequiredInterfaces.get(interfaceID).size() > 0) {
				// Currently not connected, but required interfaces
				List<AssemblyContext> openRequiredAC = this.openRequiredInterfaces.get(interfaceID);

				AssemblyContext requiringAC = this.getAndRemoveRandomAC(openRequiredAC, newAC);
				if (requiringAC != null) {
					this.createAssemblyConnector(requiringAC, newAC, interfaceID);

					connectionsCreated++;
					connected = true;
				}
			}

			if (connected)
				// Matching required interface found!
				this.connectedProvidedInterfaces.get(interfaceID).add(newAC);
			else
				// No matching required interface yet!
				this.openProvidedInterfaces.get(interfaceID).add(newAC);

		}
		return connectionsCreated;
	}

	/*
	 * This method tries to connect all required interfaces to the open provided
	 * interfaces.
	 * 
	 * If this is not possible the interface will be added to the open required
	 * interfaces for later connecting.
	 */
	private int connectRequiredInterfaces(AssemblyContext newAC, EList<RequiredRole> requRoles) {
		int connectionsCreated = 0;

		for (RequiredRole requRole : requRoles) {
			// Connect all providing interfaces!
			if (!(requRole instanceof OperationRequiredRole))
				continue;

			boolean connected = false;
			OperationRequiredRole reqInterface = (OperationRequiredRole) requRole;
			String interfaceID = reqInterface.getRequiredInterface__OperationRequiredRole().getId();
			if (this.openProvidedInterfaces.get(interfaceID).size() > 0) {
				// Open provided interface is available
				List<AssemblyContext> openProvidingAC = this.openProvidedInterfaces.get(interfaceID);

				AssemblyContext providingAC = this.getAndRemoveRandomAC(openProvidingAC, newAC);
				if (providingAC != null) {
					this.createAssemblyConnector(newAC, providingAC, interfaceID);
					this.connectedProvidedInterfaces.get(interfaceID).add(providingAC);

					connectionsCreated++;
					connected = true;
				}
			}

			if (!connected) {
				// Add to open, but required interfaces to connect later!
				List<AssemblyContext> acs = this.openRequiredInterfaces.get(interfaceID);
				acs.add(newAC);
			}
		}

		return connectionsCreated;
	}

	/*
	 * This method creates an assembly connector between the given
	 * AssemblyContextes for the given interface.
	 */
	private AssemblyConnector createAssemblyConnector(AssemblyContext requiringAC, AssemblyContext providingAC, String interfaceID) {
		AssemblyConnectorPrivacy connector = COMPOSITION_PRIVACY_FACTORY.createAssemblyConnectorPrivacy();
		connector.setEntityName(requiringAC.getEntityName() + " -> " + providingAC.getEntityName());

		java.util.Optional<ProvidedRole> providedRole = providingAC.getEncapsulatedComponent__AssemblyContext()
				.getProvidedRoles_InterfaceProvidingEntity().stream().filter(s -> matchingInterface(s, interfaceID)).findFirst();
		java.util.Optional<RequiredRole> requiredRole = requiringAC.getEncapsulatedComponent__AssemblyContext()
				.getRequiredRoles_InterfaceRequiringEntity().stream().filter(s -> matchingInterface(s, interfaceID)).findFirst();

		connector.setProvidingAssemblyContext_AssemblyConnector(providingAC);
		connector.setRequiringAssemblyContext_AssemblyConnector(requiringAC);

		connector.setProvidedRole_AssemblyConnector((OperationProvidedRole) providedRole.get());
		connector.setRequiredRole_AssemblyConnector((OperationRequiredRole) requiredRole.get());

		float randFloat = ThreadLocalRandom.current().nextFloat();
		if (randFloat < 0.15) {
			connector.setPrivacyLevel(DataPrivacyLvl.PERSONAL);
		} else if (randFloat < 0.50) {
			connector.setPrivacyLevel(DataPrivacyLvl.DEPERSONALIZED);
		} else {
			connector.setPrivacyLevel(DataPrivacyLvl.ANONYMIZED);
		}

		this.system.getConnectors__ComposedStructure().add(connector);

		this.unconnectedAssemblyContextes.remove(requiringAC);
		this.unconnectedAssemblyContextes.remove(providingAC);

		return connector;
	}

	/*
	 * Checks if a matching interface exists
	 */
	private boolean matchingInterface(ProvidedRole providedRole, String interfaceID) {
		boolean equalInterface = false;
		if (providedRole instanceof OperationProvidedRole) {
			OperationProvidedRole provInterface = (OperationProvidedRole) providedRole;
			equalInterface = provInterface.getProvidedInterface__OperationProvidedRole().getId().equals(interfaceID);
		}
		return equalInterface;
	}

	/*
	 * Checks if a matching interface exists
	 */
	private boolean matchingInterface(RequiredRole requRole, String interfaceID) {
		boolean equalInterface = false;
		if (requRole instanceof OperationRequiredRole) {
			OperationRequiredRole preqInterface = (OperationRequiredRole) requRole;
			equalInterface = preqInterface.getRequiredInterface__OperationRequiredRole().getId().equals(interfaceID);
		}
		return equalInterface;
	}

	/*
	 * This method returns a random AssemblyContext in the given container, but
	 * never the given forbidden object ref! It returns null if this is not
	 * possible!
	 */
	private AssemblyContext getAndRemoveRandomAC(List<AssemblyContext> container, Object forbiddenRef) {
		AssemblyContext ac = null;

		for (int i = 0; ac == null && i < (container.size() * 10); i++) {

			int randIndex = ThreadLocalRandom.current().nextInt(container.size());
			AssemblyContext randomAC = container.get(randIndex);

			if (randomAC != forbiddenRef) {
				container.remove(randIndex);
				ac = randomAC;
			}
		}

		// if (ac == null)
		// throw new IllegalArgumentException("There was no legal
		// AssemblyContext in the container!");
		return ac;
	}

	private void removeAssemblyContext(AssemblyContext assemblyContext) {
		boolean removed = this.system.getAssemblyContexts__ComposedStructure().remove(assemblyContext);
		if (removed) {
			for (ProvidedRole provRole : assemblyContext.getEncapsulatedComponent__AssemblyContext().getProvidedRoles_InterfaceProvidingEntity()) {
				if (!(provRole instanceof OperationProvidedRole))
					continue;

				OperationProvidedRole provInterface = (OperationProvidedRole) provRole;
				String interfaceID = provInterface.getProvidedInterface__OperationProvidedRole().getId();
				boolean success = this.openProvidedInterfaces.get(interfaceID).remove(assemblyContext);
				removed = removed && success;

			}

			for (RequiredRole requRole : assemblyContext.getEncapsulatedComponent__AssemblyContext().getRequiredRoles_InterfaceRequiringEntity()) {
				// Connect all providing interfaces!
				if (!(requRole instanceof OperationRequiredRole))
					continue;

				OperationRequiredRole reqInterface = (OperationRequiredRole) requRole;
				String interfaceID = reqInterface.getRequiredInterface__OperationRequiredRole().getId();
				boolean success = this.openRequiredInterfaces.get(interfaceID).remove(assemblyContext);
				removed = removed && success;
			}
		}

		if (!removed)
			Log.error("Something went wrong during the removal of AssemblyContext: " + assemblyContext.getId());
	}

}
