package org.iobserve.analysis.privacyanalysis;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.model.RepositoryModelProvider;
import org.iobserve.analysis.model.SystemModelProvider;
import org.palladiosimulator.pcm.compositionprivacy.AssemblyConnectorPrivacy;
import org.palladiosimulator.pcm.compositionprivacy.DataPrivacyLvl;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.Connector;
import org.palladiosimulator.pcm.repository.CompositeComponent;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;

import teetime.stage.basic.AbstractTransformation;

public class GraphCreation extends AbstractTransformation<URI, SystemPrivacyGraph> {

	private InitializeModelProviders modelProviders;
	private Map<String, AssemblyContext> assemblyContexts;
	private Map<String, DataPrivacyLvl> assemblyContextPrivacyLvl;

	private Map<String, AssemblyConnectorPrivacy> assemblyConnectors;

	
	
	public GraphCreation() {
		// assemblyContexts = new HashMap<String, AssemblyContext>();
	}

	
	
	@Override
	protected void execute(URI element) throws Exception {
		// TODO Auto-generated method stub
	}

	private void extractCompositeComponents(SystemModelProvider sysModelProv) {
		org.palladiosimulator.pcm.system.System sysModel = sysModelProv.getModel();
		EList<AssemblyContext> assemblyContexts = sysModel.getAssemblyContexts__ComposedStructure();

		for (AssemblyContext assemblyContext : assemblyContexts) {
			this.assemblyContexts.put(assemblyContext.getId(), assemblyContext);
		}
	}

	private void extractAssemblyContext(SystemModelProvider sysModelProv) {
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

			AssemblyContext provAC = this.assemblyContexts.get(providedAC_ID);

			if (provAC != null) {
				DataPrivacyLvl providedDataLevelPrivacy = this.assemblyContextPrivacyLvl.get(providedAC_ID);
				DataPrivacyLvl newDLP = null;
				if (providedDataLevelPrivacy != null) {
					newDLP = DataPrivacyLvl.get(Math.min(assemblyConnectorPrivacyLvl.getValue(), providedDataLevelPrivacy.getValue()));
				} else {
					newDLP = assemblyConnectorPrivacyLvl;
				}
				this.assemblyContextPrivacyLvl.put(providedAC_ID, newDLP);
				
			} else {
				System.err.printf(
						"The provided AssemblyContext (ID: %s) form the AssemblyConnectorPrivacy (ID: %s) was not found during the AssemblyContextExtraction",
						providedAC_ID, acp.getId());
				
				this.assemblyContexts.put(providedAC_ID, acp.getProvidingAssemblyContext_AssemblyConnector());
				this.assemblyContextPrivacyLvl.put(providedAC_ID, assemblyConnectorPrivacyLvl);
			}
		}
	}

}
