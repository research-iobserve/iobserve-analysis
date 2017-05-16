package org.iobserve.analysis.privacyanalysis;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.RepositoryModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.analysis.privacy.graph.ComponentEdge;
import org.iobserve.analysis.privacy.graph.ComponentNode;
import org.iobserve.analysis.privacy.graph.DeploymentNode;
import org.iobserve.analysis.privacy.graph.PrivacyAnalysisModel;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.compositionprivacy.AssemblyConnectorPrivacy;
import org.palladiosimulator.pcm.compositionprivacy.DataPrivacyLvl;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.Connector;
import org.palladiosimulator.pcm.repository.CompositeComponent;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironmentprivacy.ResourceContainerPrivacy;

import groovy.ui.Console;
import teetime.stage.basic.AbstractTransformation;

/**
 * This class extracts the required information from the pcm models and re-assembles them to a PrivacyAnalysisModel for privacy analysis purposes.
 * 
 * @author Philipp Weimann
 */
public class GraphCreation extends AbstractTransformation<URI, PrivacyAnalysisModel> {

	private InitializeModelProviders modelProviders;

	private Map<String, AssemblyContext> assemblyContexts;
	private Map<String, DataPrivacyLvl> assemblyContextPrivacyLvl;
	private Map<String, ResourceContainerPrivacy> resourceContainers;
	private Map<String, String> ac2rcMap;
	private Map<String, AssemblyConnectorPrivacy> assemblyConnectors;
	
	private PrivacyAnalysisModel graph;

	
	public GraphCreation() {
	}
	
	private void init(URI initialModelLocation)
	{
		this.modelProviders = new InitializeModelProviders(new File(initialModelLocation.toFileString()));
		
		this.assemblyContexts = new HashMap<String, AssemblyContext>();
		this.assemblyContextPrivacyLvl = new HashMap<String, DataPrivacyLvl>();
		this.assemblyConnectors = new HashMap<String, AssemblyConnectorPrivacy>();
		this.resourceContainers = new HashMap<String, ResourceContainerPrivacy>();
		this.ac2rcMap = new HashMap<String, String>();
	}

	@Override
	protected void execute(URI element) throws Exception {
		this.init(element);

		this.extractAssemblyContexts(this.modelProviders.getSystemModelProvider());
		this.extractAssemblyConnectors(this.modelProviders.getSystemModelProvider());
		this.extractResourceContainers(this.modelProviders.getResourceEnvironmentModelProvider());
		this.adaptPrivacyLvl();
		this.extractAllocations(this.modelProviders.getAllocationModelProvider());
		
		PrivacyAnalysisModel model = this.graph = this.createModelGraph(element);
		outputPort.send(model);
	}

	/*
	 * Extract Information Helpers
	 */
	private void extractAssemblyContexts(SystemModelProvider sysModelProv) {
		org.palladiosimulator.pcm.system.System sysModel = sysModelProv.getModel();
		EList<AssemblyContext> assemblyContexts = sysModel.getAssemblyContexts__ComposedStructure();

		for (AssemblyContext assemblyContext : assemblyContexts) {
			this.assemblyContexts.put(assemblyContext.getId(), assemblyContext);
		}
	}

	private void extractAssemblyConnectors(SystemModelProvider sysModelProv) {
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

		for (AssemblyConnectorPrivacy acp : acps) {
			DataPrivacyLvl assemblyConnectorPrivacyLvl = acp.getPrivacyLevel();

			String providedAC_ID = acp.getProvidingAssemblyContext_AssemblyConnector().getId();
			String requiredAC_ID = acp.getRequiringAssemblyContext_AssemblyConnector().getId();

			this.updatePrivacyLvl(acp, assemblyConnectorPrivacyLvl, providedAC_ID);
			this.updatePrivacyLvl(acp, assemblyConnectorPrivacyLvl, requiredAC_ID);
		}
	}

	private void updatePrivacyLvl(AssemblyConnectorPrivacy acp, DataPrivacyLvl assemblyConnectorPrivacyLvl, String assemblyContext_ID) {
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

	private void extractResourceContainers(ResourceEnvironmentModelProvider resEnvModelProv) {
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

	private void extractAllocations(AllocationModelProvider allocationModelProv) {
		Allocation allocation = allocationModelProv.getModel();
		EList<AllocationContext> allocationContexts = allocation.getAllocationContexts_Allocation();

		for (AllocationContext allocationContext : allocationContexts) {
			ResourceContainer resContainer = allocationContext.getResourceContainer_AllocationContext();
			AssemblyContext assemblyContext = allocationContext.getAssemblyContext_AllocationContext();

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

			if (correctIDs)
				this.ac2rcMap.put(assemblyContext.getId(), resContainer.getId());
		}
	}

	/*
	 * Build Graph Helpers
	 */
	private PrivacyAnalysisModel createModelGraph(URI pcmModelUri) {
		HashMap<String, DeploymentNode> servers = new HashMap<String, DeploymentNode>();
		HashMap<String, ComponentNode> components = new HashMap<String, ComponentNode>();
		
		//Build Servers Nodes
		for (ResourceContainerPrivacy resContainer : this.resourceContainers.values())
		{
			DeploymentNode server = new DeploymentNode(resContainer.getId(), resContainer.getEntityName(), resContainer.getGeolocation());
			servers.put(resContainer.getId(), server);
		}
		

		//Build Component Nodes
		for (AssemblyContext ac : this.assemblyContexts.values()) {
			
			DeploymentNode hostServer = servers.get(this.ac2rcMap.get(ac.getId()));
			DataPrivacyLvl acPrivacyLvl = this.assemblyContextPrivacyLvl.get(ac.getId());
			
			ComponentNode component = new ComponentNode(ac.getId(), ac.getEntityName(), acPrivacyLvl, hostServer);
			hostServer.addComponent(component);
			
			components.put(ac.getId(), component);
		}
		
		//Set Edges
		for (AssemblyConnectorPrivacy acp : this.assemblyConnectors.values())
		{
			String provAC_ID = acp.getProvidingAssemblyContext_AssemblyConnector().getId();
			String reqAC_ID = acp.getRequiringAssemblyContext_AssemblyConnector().getId();
			
			ComponentNode provNode = components.get(provAC_ID);
			ComponentNode reqNode = components.get(reqAC_ID);
			
			ComponentEdge edge = new ComponentEdge(acp.getId(), acp.getEntityName(), provNode, reqNode, acp.getPrivacyLevel());
			
			provNode.addCommunicationEdge(edge);
			reqNode.addCommunicationEdge(edge);
		}
		
		
		return new PrivacyAnalysisModel(servers.values(), components.values(), this.modelProviders, pcmModelUri);
	}

}
