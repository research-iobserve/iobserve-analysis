package org.iobserve.service.generation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.iobserve.analysis.graph.GraphFactory;
import org.iobserve.analysis.graph.ModelCollection;
import org.iobserve.analysis.graph.ModelGraph;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.compositionprivacy.AssemblyConnectorPrivacy;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.Connector;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationRequiredRole;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironmentprivacy.ResourceContainerPrivacy;

public class ModelDif {

	public List<String> difModels(ModelCollection initModels, ModelCollection difModels) {
		List<String> dif = new ArrayList<String>();

		GraphFactory graphCreator = new GraphFactory();
		ModelGraph initGraph = null;
		ModelGraph difGraph = null;
		try {
			initGraph = graphCreator.buildGraph(initModels);
			difGraph = graphCreator.buildGraph(difModels);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// boolean equalGraphs = initGraph.equals(difGraph);
		//
		// if (!equalGraphs) {
		List<String> resEnvDifs = calcResEnvDifs(initModels.getResourceEnvironmentModel(), difModels.getResourceEnvironmentModel());
		dif.addAll(resEnvDifs);
		List<String> allocEnvDifs = calcAllocDifs(initModels.getAllocationModel(), difModels.getAllocationModel());
		dif.addAll(allocEnvDifs);
		List<String> sysAContextes = calcAssemblyDifs_Contexts(initModels.getSystemModel(), difModels.getSystemModel());
		dif.addAll(sysAContextes);
		List<String> sysAConnectors = calcAssemblyDifs_Connectors(initModels.getSystemModel(), difModels.getSystemModel());
		dif.addAll(sysAConnectors);
		// }

		return dif;
	}

	///////////////////// ResEnv ////////////////////////
	private HashMap<String, ResourceContainer> initResCon;

	public List<String> calcResEnvDifs(ResourceEnvironment initResEnv, ResourceEnvironment difResEnv) {
		List<String> resEnvDifs = new ArrayList<String>();

		this.initResCon = new HashMap<String, ResourceContainer>();
		for (ResourceContainer resContainer : initResEnv.getResourceContainer_ResourceEnvironment()) {
			this.initResCon.put(resContainer.getId(), resContainer);
		}

		for (ResourceContainer difResContainer : difResEnv.getResourceContainer_ResourceEnvironment()) {
			if (!this.initResCon.containsKey(difResContainer.getId())) {
				resEnvDifs.add(String.format("ResourceContainer %s does not exist in INIT model!", difResContainer.getId()));
				continue;
			}

			ResourceContainer initResContainer = this.initResCon.get(difResContainer.getId());
			if (initResContainer instanceof ResourceContainerPrivacy && difResContainer instanceof ResourceContainerPrivacy) {
				ResourceContainerPrivacy initRCP = (ResourceContainerPrivacy) initResContainer;
				ResourceContainerPrivacy difRCP = (ResourceContainerPrivacy) difResContainer;

				if (initRCP.getGeolocation() != difRCP.getGeolocation()) {
					resEnvDifs.add(String.format("Unmatchig Geo-locations for ResoruceContainer %s: INIT (%d) vs DIF (%d)!", initRCP.getId(),
							initRCP.getGeolocation(), difRCP.getGeolocation()));
				}
			} else {
				if (!(initResContainer instanceof ResourceContainerPrivacy))
					resEnvDifs.add(String.format("ResourceContainer (%s: %s) INIT is not of type ResoruceContainerPRIVACY!", initResContainer.getId(),
							initResContainer.getEntityName()));
				if (!(difResContainer instanceof ResourceContainerPrivacy))
					resEnvDifs.add(String.format("ResourceContainer (%s: %s) DIF is not of type ResoruceContainerPRIVACY!", difResContainer.getId(),
							initResContainer.getEntityName()));
			}
			boolean removed = initResCon.remove(initResContainer.getId(), initResContainer);
		}

		for (String key : initResCon.keySet()) {
			resEnvDifs.add(String.format("ResourceContainer %s does not exist in DIF model!", key));
		}

		return resEnvDifs;
	}

	///////////////////// Allocation ////////////////////////
	private HashMap<String, AllocationContext> initAllocs;

	public List<String> calcAllocDifs(Allocation initAlloc, Allocation difAlloc) {
		List<String> resEnvDifs = new ArrayList<String>();

		this.initAllocs = new HashMap<String, AllocationContext>();
		for (AllocationContext initAC : initAlloc.getAllocationContexts_Allocation()) {
			this.initAllocs.put(initAC.getId(), initAC);
		}

		for (AllocationContext difAC : difAlloc.getAllocationContexts_Allocation()) {
			if (!this.initAllocs.containsKey(difAC.getId())) {
				resEnvDifs.add(String.format("AllocationContext %s does not exist in INIT model!", difAC.getId()));
				continue;
			}

			AllocationContext initAC = this.initAllocs.get(difAC.getId());

			if (!(initAC.getResourceContainer_AllocationContext().getId().equals(difAC.getResourceContainer_AllocationContext().getId()))) {
				ResourceContainer initAC_RC = initAC.getResourceContainer_AllocationContext();
				ResourceContainer difAC_RC = difAC.getResourceContainer_AllocationContext();
				resEnvDifs.add(String.format("Unmatchig ResourceContainer for AllocationContext %s: INIT (%s: %s) vs DIF (%s: %s)!", initAC.getId(),
						initAC_RC.getId(), initAC_RC.getEntityName(), difAC_RC.getId(), difAC_RC.getEntityName()));
			}
			if (!(initAC.getAssemblyContext_AllocationContext().getId().equals(difAC.getAssemblyContext_AllocationContext().getId()))) {
				AssemblyContext initAC_AC = initAC.getAssemblyContext_AllocationContext();
				AssemblyContext difAC_AC = difAC.getAssemblyContext_AllocationContext();
				resEnvDifs.add(String.format("Unmatchig AssemblyContexts for AllocationContext %s: INIT (%s: %s) vs DIF (%s: %s)!", initAC.getId(),
						initAC_AC.getId(), initAC_AC.getEntityName(), difAC_AC.getId(), difAC_AC.getEntityName()));
			}

			initAllocs.remove(initAC.getId(), initAC);
		}

		for (String key : initAllocs.keySet()) {
			resEnvDifs.add(String.format("AllocationContext %s does not exist in DIF model!", key));
		}

		return resEnvDifs;
	}

	///////////////////// AssemblyContexts ////////////////////////
	private HashMap<String, AssemblyContext> initAssemblys;

	public List<String> calcAssemblyDifs_Contexts(org.palladiosimulator.pcm.system.System initSys, org.palladiosimulator.pcm.system.System difSys) {
		List<String> resEnvDifs = new ArrayList<String>();

		this.initAssemblys = new HashMap<String, AssemblyContext>();
		for (AssemblyContext initAC : initSys.getAssemblyContexts__ComposedStructure()) {
			this.initAssemblys.put(initAC.getId(), initAC);
		}

		for (AssemblyContext difAC : difSys.getAssemblyContexts__ComposedStructure()) {
			if (!this.initAssemblys.containsKey(difAC.getId())) {
				resEnvDifs.add(String.format("AssemblyContext %s does not exist in INIT model!", difAC.getId()));
				continue;
			}

			AssemblyContext initAC = this.initAssemblys.get(difAC.getId());

			if (!(initAC.getEncapsulatedComponent__AssemblyContext().getId().equals(difAC.getEncapsulatedComponent__AssemblyContext().getId()))) {
				RepositoryComponent initAC_EC = initAC.getEncapsulatedComponent__AssemblyContext();
				RepositoryComponent difAC_EC = difAC.getEncapsulatedComponent__AssemblyContext();
				resEnvDifs.add(String.format("Unmatchig Component for AssemblyContext %s: INIT (%s: %s) vs DIF (%s: %s)!", initAC.getId(),
						initAC_EC.getId(), initAC_EC.getEntityName(), difAC_EC.getId(), difAC_EC.getEntityName()));
			}

			initAssemblys.remove(initAC.getId(), initAC);
		}

		for (String key : initAssemblys.keySet()) {
			resEnvDifs.add(String.format("AssemblyContext %s does not exist in DIF model!", key));
		}

		return resEnvDifs;
	}

	///////////////////// AssemblyConnectors ////////////////////////
	private HashMap<String, AssemblyConnector> initConnectors;

	public List<String> calcAssemblyDifs_Connectors(org.palladiosimulator.pcm.system.System initSys, org.palladiosimulator.pcm.system.System difSys) {
		List<String> resEnvDifs = new ArrayList<String>();

		this.initConnectors = new HashMap<String, AssemblyConnector>();
		for (Connector initAC : initSys.getConnectors__ComposedStructure()) {
			if (initAC instanceof AssemblyConnector)
				this.initConnectors.put(initAC.getId(), (AssemblyConnector) initAC);
		}

		for (Connector difAC : difSys.getConnectors__ComposedStructure()) {
			if (!(difAC instanceof AssemblyConnector))
				continue;

			if (!this.initConnectors.containsKey(difAC.getId())) {
				resEnvDifs.add(String.format("AssemblyConnector %s does not exist in INIT model!", difAC.getId()));
				continue;
			}

			AssemblyConnector initAC = this.initConnectors.get(difAC.getId());

			if (initAC instanceof AssemblyConnectorPrivacy && difAC instanceof AssemblyConnectorPrivacy) {
				AssemblyConnectorPrivacy initACP = (AssemblyConnectorPrivacy) initAC;
				AssemblyConnectorPrivacy difACP = (AssemblyConnectorPrivacy) difAC;

				if (initACP.getPrivacyLevel() != difACP.getPrivacyLevel()) {
					resEnvDifs.add(String.format("Unmatchig Privacy-Levels for AssemblyConnector %s: INIT (%s) vs DIF (%s)!", initACP.getId(),
							initACP.getPrivacyLevel().toString(), difACP.getPrivacyLevel().toString()));
				}

				if (!(initACP.getProvidingAssemblyContext_AssemblyConnector().getId()
						.equals(difACP.getProvidingAssemblyContext_AssemblyConnector().getId()))) {
					AssemblyContext initAC_PAC = initACP.getProvidingAssemblyContext_AssemblyConnector();
					AssemblyContext difAC_PAC = difACP.getProvidingAssemblyContext_AssemblyConnector();
					resEnvDifs.add(String.format("Unmatchig Providing ACs for AssemblyConnector %s: INIT (%s: %s) vs DIF (%s: %s)!", initAC.getId(),
							initAC_PAC.getId(), initAC_PAC.getEntityName(), difAC_PAC.getId(), difAC_PAC.getEntityName()));
				}

				if (!(initACP.getRequiringAssemblyContext_AssemblyConnector().getId()
						.equals(difACP.getRequiringAssemblyContext_AssemblyConnector().getId()))) {
					AssemblyContext initAC_PAC = initACP.getRequiringAssemblyContext_AssemblyConnector();
					AssemblyContext difAC_PAC = difACP.getRequiringAssemblyContext_AssemblyConnector();
					resEnvDifs.add(String.format("Unmatchig Requiring ACs for AssemblyConnector %s: INIT (%s: %s) vs DIF (%s: %s)!", initAC.getId(),
							initAC_PAC.getId(), initAC_PAC.getEntityName(), difAC_PAC.getId(), difAC_PAC.getEntityName()));
				}

				if (!(initACP.getProvidedRole_AssemblyConnector().getId().equals(difACP.getProvidedRole_AssemblyConnector().getId()))) {
					OperationProvidedRole initAC_PR = initAC.getProvidedRole_AssemblyConnector();
					OperationProvidedRole difAC_PR = ((AssemblyConnector) difAC).getProvidedRole_AssemblyConnector();
					resEnvDifs.add(String.format("Unmatchig ProvidingRole for AssemblyConnector %s: INIT (%s: %s) vs DIF (%s: %s)!", initAC.getId(),
							initAC_PR.getId(), initAC_PR.getEntityName(), difAC_PR.getId(), difAC_PR.getEntityName()));
				}

				if (!(initACP.getRequiredRole_AssemblyConnector().getId().equals(difACP.getRequiredRole_AssemblyConnector().getId()))) {
					OperationRequiredRole initAC_PR = initAC.getRequiredRole_AssemblyConnector();
					OperationRequiredRole difAC_PR = ((AssemblyConnector) difAC).getRequiredRole_AssemblyConnector();
					resEnvDifs.add(String.format("Unmatchig RequiringRole for AssemblyConnector %s: INIT (%s: %s) vs DIF (%s: %s)!", initAC.getId(),
							initAC_PR.getId(), initAC_PR.getEntityName(), difAC_PR.getId(), difAC_PR.getEntityName()));
				}

			} else {
				if (!(initAC instanceof ResourceContainerPrivacy))
					resEnvDifs.add(String.format("AssemblyConnector (%s: %s) INIT is not of type AssemblyConnectorPRIVACY!", initAC.getId(),
							initAC.getEntityName()));
				if (!(difAC instanceof ResourceContainerPrivacy))
					resEnvDifs.add(String.format("AssemblyConnector (%s: %s) INIT is not of type AssemblyConnectorPRIVACY!", difAC.getId(),
							difAC.getEntityName()));
			}

			initConnectors.remove(initAC.getId(), initAC);
		}

		for (String key : initConnectors.keySet()) {
			resEnvDifs.add(String.format("AssemblyConnector %s does not exist in DIF model!", key));
		}

		return resEnvDifs;
	}
}
