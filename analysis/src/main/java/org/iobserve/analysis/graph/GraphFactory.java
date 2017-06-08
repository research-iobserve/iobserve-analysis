package org.iobserve.analysis.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelProvider;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.compositionprivacy.AssemblyConnectorPrivacy;
import org.palladiosimulator.pcm.compositionprivacy.DataPrivacyLvl;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.Connector;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironmentprivacy.ResourceContainerPrivacy;

public class GraphFactory {
	private static final Logger LOG = LogManager.getLogger();

	private InitializeModelProviders modelProvider;

	private Map<String, AssemblyContext> assemblyContexts;
	private Map<String, DataPrivacyLvl> assemblyContextPrivacyLvl;
	private Map<String, ResourceContainerPrivacy> resourceContainers;
	private Map<String, String> ac2rcMap;
	private Map<String, AssemblyConnectorPrivacy> assemblyConnectors;
	private Map<String, String> assemblyID2allocID;

	/**
	 * Empty Constructor
	 */
	public GraphFactory() {
	}

	public ModelGraph buildGraph(final InitializeModelProviders modelProvider) throws Exception {
		this.init(modelProvider);

		this.extractAssemblyContexts(this.modelProvider.getSystemModelProvider());
		this.extractAssemblyConnectors(this.modelProvider.getSystemModelProvider());
		this.extractResourceContainers(this.modelProvider.getResourceEnvironmentModelProvider());
		this.adaptPrivacyLvl();
		this.extractAllocations(this.modelProvider.getAllocationModelProvider());

		return this.createModelGraph();
	}

	/*
	 * Prepare all data structures.
	 */
	private void init(final InitializeModelProviders modelProvider) {
		this.modelProvider = modelProvider;

		this.assemblyContexts = new HashMap<String, AssemblyContext>();
		this.assemblyContextPrivacyLvl = new HashMap<String, DataPrivacyLvl>();
		this.assemblyConnectors = new HashMap<String, AssemblyConnectorPrivacy>();
		this.resourceContainers = new HashMap<String, ResourceContainerPrivacy>();
		this.ac2rcMap = new HashMap<String, String>();
		this.assemblyID2allocID = new HashMap<String, String>();
	}

	/*
	 * Extract Information Helpers
	 */
	private void extractAssemblyContexts(final SystemModelProvider sysModelProv) {
		org.palladiosimulator.pcm.system.System sysModel = sysModelProv.getModel();
		EList<AssemblyContext> assemblyContexts = sysModel.getAssemblyContexts__ComposedStructure();
		Set<String> acs = new HashSet<String>();

		for (AssemblyContext assemblyContext : assemblyContexts) {
			this.assemblyContexts.put(assemblyContext.getId(), assemblyContext);
			acs.add(assemblyContext.getId());
		}

		LOG.info("Individual Assembly Contexts found in System Model: " + acs.size());
	}

	private void extractAssemblyConnectors(final SystemModelProvider sysModelProv) {
		org.palladiosimulator.pcm.system.System sysModel = sysModelProv.getModel();
		EList<Connector> connectors = sysModel.getConnectors__ComposedStructure();

		for (Connector connector : connectors) {
			if (connector instanceof AssemblyConnectorPrivacy) {
				AssemblyConnectorPrivacy acp = (AssemblyConnectorPrivacy) connector;
				this.assemblyConnectors.put(connector.getId(), acp);
			}
		}
	}

	private void adaptPrivacyLvl() {
		Collection<AssemblyConnectorPrivacy> acps = this.assemblyConnectors.values();

		Set<String> acs = new HashSet<String>();

		for (AssemblyConnectorPrivacy acp : acps) {
			DataPrivacyLvl assemblyConnectorPrivacyLvl = acp.getPrivacyLevel();

			String providedAC_ID = acp.getProvidingAssemblyContext_AssemblyConnector().getId();
			String requiredAC_ID = acp.getRequiringAssemblyContext_AssemblyConnector().getId();

			this.updatePrivacyLvl(acp, assemblyConnectorPrivacyLvl, providedAC_ID);
			this.updatePrivacyLvl(acp, assemblyConnectorPrivacyLvl, requiredAC_ID);

			acs.add(requiredAC_ID);
			acs.add(providedAC_ID);
		}

		LOG.info("Individual Assembly Contexts found in Assembly Connectors: " + acs.size());
	}

	private void updatePrivacyLvl(final AssemblyConnectorPrivacy acp, final DataPrivacyLvl assemblyConnectorPrivacyLvl, final String assemblyContext_ID) {
		// Check whether the AssemblyContext was found while extracting
		AssemblyContext assemblyContext = this.assemblyContexts.get(assemblyContext_ID);
		if (assemblyContext == null) {
			System.err.printf("The provided AssemblyContext (ID: %s) form the AssemblyConnectorPrivacy (ID: %s) "
					+ "was not found during the AssemblyContextExtraction", assemblyContext_ID, acp.getId());

			this.assemblyContexts.put(assemblyContext_ID, acp.getProvidingAssemblyContext_AssemblyConnector());
		}

		// Do the actual job and update the privacy lvl
		DataPrivacyLvl currentDataLevelPrivacy = this.assemblyContextPrivacyLvl.get(assemblyContext_ID);

		if (currentDataLevelPrivacy != null) {
			currentDataLevelPrivacy = DataPrivacyLvl.get(Math.min(assemblyConnectorPrivacyLvl.getValue(), currentDataLevelPrivacy.getValue()));
		} else {
			currentDataLevelPrivacy = assemblyConnectorPrivacyLvl;
		}

		this.assemblyContextPrivacyLvl.put(assemblyContext_ID, currentDataLevelPrivacy);
	}

	private void extractResourceContainers(final ResourceEnvironmentModelProvider resEnvModelProv) {
		ResourceEnvironment resEnvModel = resEnvModelProv.getModel();
		EList<ResourceContainer> resourceContainers = resEnvModel.getResourceContainer_ResourceEnvironment();
		for (ResourceContainer resourceContainer : resourceContainers) {
			if (resourceContainer instanceof ResourceContainerPrivacy) {
				this.resourceContainers.put(resourceContainer.getId(), (ResourceContainerPrivacy) resourceContainer);
			} else {
				System.err.printf("A ResourceContainer (ID: %s) was found which has no privacy extention\n", resourceContainer.getId());
			}
		}
	}

	private void extractAllocations(final AllocationModelProvider allocationModelProv) {
		Allocation allocation = allocationModelProv.getModel();
		EList<AllocationContext> allocationContexts = allocation.getAllocationContexts_Allocation();

		for (AllocationContext allocationContext : allocationContexts) {
			ResourceContainer resContainer = allocationContext.getResourceContainer_AllocationContext();
			AssemblyContext assemblyContext = allocationContext.getAssemblyContext_AllocationContext();

			this.assemblyID2allocID.put(assemblyContext.getId(), allocationContext.getId());

			boolean correctIDs = true;
			String resContainerID = resContainer.getId();
			if (!this.resourceContainers.containsKey(resContainerID)) {
				System.err.printf("A unknown ResourceContainer (ID: %s) was found during allocation context analysis.\n", resContainer.getId());
				correctIDs = false;
			}

			String assemblyContextID = assemblyContext.getId();
			if (!this.assemblyContexts.containsKey(assemblyContextID)) {
				System.err.printf("An unknown AssemblyContext (ID: %s) was found during allocation context analysis.\n", assemblyContext.getId());
				correctIDs = false;
			}

			if (correctIDs) {
				this.ac2rcMap.put(assemblyContext.getId(), resContainer.getId());
			}
		}
	}

	/*
	 * Build Graph Helpers
	 */
	private ModelGraph createModelGraph() {
		HashMap<String, DeploymentNode> servers = new HashMap<String, DeploymentNode>();
		HashMap<String, ComponentNode> components = new HashMap<String, ComponentNode>();

		// Build Servers Nodes
		for (ResourceContainerPrivacy resContainer : this.resourceContainers.values()) {
			DeploymentNode server = new DeploymentNode(resContainer.getId(), resContainer.getEntityName(), resContainer.getGeolocation());
			servers.put(resContainer.getId(), server);
		}

		// Build Component Nodes
		for (AssemblyContext ac : this.assemblyContexts.values()) {

			DeploymentNode hostServer = servers.get(this.ac2rcMap.get(ac.getId()));
			DataPrivacyLvl acPrivacyLvl = this.assemblyContextPrivacyLvl.get(ac.getId());

			ComponentNode component = new ComponentNode(ac.getId(), ac.getEntityName(), acPrivacyLvl, hostServer,
					ac.getEncapsulatedComponent__AssemblyContext().getId(), this.assemblyID2allocID.get(ac.getId()));
			hostServer.addComponent(component);

			components.put(ac.getId(), component);
		}

		// Set Edges
		for (AssemblyConnectorPrivacy acp : this.assemblyConnectors.values()) {
			String provAC_ID = acp.getProvidingAssemblyContext_AssemblyConnector().getId();
			String reqAC_ID = acp.getRequiringAssemblyContext_AssemblyConnector().getId();

			ComponentNode provNode = components.get(provAC_ID);
			ComponentNode reqNode = components.get(reqAC_ID);

			ComponentEdge edge = new ComponentEdge(acp.getId(), acp.getEntityName(), provNode, reqNode, acp.getPrivacyLevel());

			provNode.addCommunicationEdge(edge);
			reqNode.addCommunicationEdge(edge);
		}

		return new ModelGraph(servers.values(), components.values(), this.modelProvider);
	}
}
