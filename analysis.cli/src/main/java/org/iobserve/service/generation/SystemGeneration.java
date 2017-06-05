package org.iobserve.service.generation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.eclipse.emf.common.util.EList;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.CompositionFactory;
import org.palladiosimulator.pcm.core.composition.impl.AssemblyContextImpl;
import org.palladiosimulator.pcm.core.composition.impl.CompositionFactoryImpl;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationRequiredRole;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.repository.RequiredRole;
import org.palladiosimulator.pcm.system.*;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.system.impl.SystemFactoryImpl;

import com.google.common.base.Optional;
import com.sun.jna.Native.ffi_callback;

public class SystemGeneration {

	private static final CompositionFactory COMPOSITION_FACTORY = CompositionFactory.eINSTANCE;

	private Repository repo;
	private RepositoryComponent[] components;
	private System system;

	private HashMap<String, List<AssemblyContext>> openRequiredInterfaces;
	private HashMap<String, List<AssemblyContext>> openProvidedInterfaces;
	private HashMap<String, List<AssemblyContext>> connectedProvidedInterfaces;

	public SystemGeneration(Repository repo) {
		super();
		this.repo = repo;
		this.components = repo.getComponents__Repository().toArray(new RepositoryComponent[repo.getComponents__Repository().size()]);

		initInterfaceMaps();

		this.system = SystemFactoryImpl.init().createSystem();
		this.system.setEntityName(this.repo.getEntityName());
	}

	private void initInterfaceMaps() {
		this.openRequiredInterfaces = new HashMap<String, List<AssemblyContext>>();
		this.openProvidedInterfaces = new HashMap<String, List<AssemblyContext>>();
		this.connectedProvidedInterfaces = new HashMap<String, List<AssemblyContext>>();

		for (RepositoryComponent component : this.components) {
			for (ProvidedRole provRole : component.getProvidedRoles_InterfaceProvidingEntity()) {
				if (!(provRole instanceof OperationProvidedRole))
					continue;

				String interfaceID = provRole.getProvidingEntity_ProvidedRole().getId();
				if (!openRequiredInterfaces.containsKey(interfaceID)) {
					this.openProvidedInterfaces.put(interfaceID, new LinkedList<AssemblyContext>());
					this.openProvidedInterfaces.put(interfaceID, new LinkedList<AssemblyContext>());
					this.connectedProvidedInterfaces.put(interfaceID, new LinkedList<AssemblyContext>());
				}
			}
			for (RequiredRole requRole : component.getRequiredRoles_InterfaceRequiringEntity()) {
				if (!(requRole instanceof OperationRequiredRole))
					continue;

				String interfaceID = requRole.getRequiringEntity_RequiredRole().getId();
				if (!openRequiredInterfaces.containsKey(interfaceID)) {
					this.openProvidedInterfaces.put(interfaceID, new LinkedList<AssemblyContext>());
					this.openProvidedInterfaces.put(interfaceID, new LinkedList<AssemblyContext>());
					this.connectedProvidedInterfaces.put(interfaceID, new LinkedList<AssemblyContext>());
				}
			}
		}
	};

	public System generateSystemModel(int assemblyContextCount) {

		for (int i = 0; i < assemblyContextCount; i++) {

			int randomInt = ThreadLocalRandom.current().nextInt(components.length - 1);
			RepositoryComponent randComponent = this.components[randomInt];

			AssemblyContext newAC = COMPOSITION_FACTORY.createAssemblyContext();
			newAC.setEntityName(Integer.toString(i) + "_AC_" + randComponent.getEntityName());
			newAC.setEncapsulatedComponent__AssemblyContext(randComponent);
			newAC.setParentStructure__AssemblyContext(system);
			this.system.getAssemblyContexts__ComposedStructure().add(newAC);

			EList<ProvidedRole> provRoles = randComponent.getProvidedRoles_InterfaceProvidingEntity();
			this.connectProvidedInterfaces(newAC, provRoles);

			EList<RequiredRole> requRoles = randComponent.getRequiredRoles_InterfaceRequiringEntity();
			this.connectRequiredInterfaces(newAC, requRoles);
		}

		this.finalizeModelGeneration();

		return this.system;
	}

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

	}

	/*
	 * This method tires to connect all providing interfaces to non connected
	 * required interfaces!
	 * 
	 * If this is not possible, it will be added to non connected providing
	 * interfaces for later connecting!
	 */
	private void connectProvidedInterfaces(AssemblyContext newAC, EList<ProvidedRole> provRoles) {
		for (ProvidedRole provRole : provRoles) {
			// Connect all providing interfaces!
			if (!(provRole instanceof OperationProvidedRole))
				continue;

			boolean connected = false;
			String interfaceID = provRole.getProvidingEntity_ProvidedRole().getId();
			if (this.openRequiredInterfaces.get(interfaceID).size() > 0) {
				// Currently not connected, but required interfaces
				List<AssemblyContext> openRequiredAC = this.openRequiredInterfaces.get(interfaceID);

				AssemblyContext requiringAC = this.getAndRemoveRandomAC(openRequiredAC, newAC);
				if (requiringAC != null) {
					AssemblyConnector connector = this.createAssemblyConnector(requiringAC, newAC, interfaceID);
					this.system.getConnectors__ComposedStructure().add(connector);
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
	}

	/*
	 * This method tries to connect all required interfaces to the open provided
	 * interfaces.
	 * 
	 * If this is not possible the interface will be added to the open required
	 * interfaces for later connecting.
	 */
	private void connectRequiredInterfaces(AssemblyContext newAC, EList<RequiredRole> requRoles) {
		for (RequiredRole requRole : requRoles) {
			// Connect all providing interfaces!
			if (!(requRole instanceof OperationRequiredRole))
				continue;

			boolean connected = false;
			String interfaceID = requRole.getRequiringEntity_RequiredRole().getId();
			if (this.openProvidedInterfaces.get(interfaceID).size() > 0) {
				// Open provided interface is available
				List<AssemblyContext> openProvidingAC = this.openProvidedInterfaces.get(interfaceID);

				AssemblyContext providingAC = this.getAndRemoveRandomAC(openProvidingAC, newAC);
				if (providingAC != null) {
					AssemblyConnector connector = this.createAssemblyConnector(newAC, providingAC, interfaceID);
					this.system.getConnectors__ComposedStructure().add(connector);
					
					this.connectedProvidedInterfaces.get(interfaceID).add(providingAC);
					connected = true;
				}
			}

			if (!connected)
				// Add to open, but required interfaces to connect later!
				this.openRequiredInterfaces.get(interfaceID).add(newAC);

		}
	}

	/*
	 * This method creates an assembly connector between the given
	 * AssemblyContextes for the given interface.
	 */
	private AssemblyConnector createAssemblyConnector(AssemblyContext requiringAC, AssemblyContext providingAC, String interfaceID) {
		AssemblyConnector connector = COMPOSITION_FACTORY.createAssemblyConnector();
		connector.setEntityName(requiringAC.getEntityName() + " -> " + providingAC.getEntityName());

		java.util.Optional<ProvidedRole> providedRole = providingAC.getEncapsulatedComponent__AssemblyContext()
				.getProvidedRoles_InterfaceProvidingEntity().stream().filter(s -> s.getProvidingEntity_ProvidedRole().getId().equals(interfaceID))
				.findFirst();
		java.util.Optional<RequiredRole> requiredRole = requiringAC.getEncapsulatedComponent__AssemblyContext()
				.getRequiredRoles_InterfaceRequiringEntity().stream().filter(s -> s.getRequiringEntity_RequiredRole().getId().equals(interfaceID))
				.findFirst();

		connector.setProvidingAssemblyContext_AssemblyConnector(providingAC);
		connector.setRequiringAssemblyContext_AssemblyConnector(requiringAC);

		connector.setProvidedRole_AssemblyConnector((OperationProvidedRole) providedRole.get());
		connector.setRequiredRole_AssemblyConnector((OperationRequiredRole) requiredRole.get());

		return connector;
	}

	/*
	 * This method returns a random AssemblyContext in the given container, but
	 * never the given forbidden object ref! It returns null if this is not
	 * possible!
	 */
	private AssemblyContext getAndRemoveRandomAC(List<AssemblyContext> container, Object forbiddenRef) {
		AssemblyContext ac = null;
		if (container.size() == 1) {
			ac = container.get(0);
			if (ac == forbiddenRef)
				ac = null;
			else
				container.remove(0);
		} else {
			boolean acFound = false;
			while (!acFound) {
				int randIndex = ThreadLocalRandom.current().nextInt(container.size());
				ac = container.get(randIndex);
				if (ac != forbiddenRef) {
					container.remove(randIndex);
					acFound = true;
				}
			}
		}

		// if (ac == null)
		// throw new IllegalArgumentException("There was no legal
		// AssemblyContext in the container!");
		return ac;
	}

}
