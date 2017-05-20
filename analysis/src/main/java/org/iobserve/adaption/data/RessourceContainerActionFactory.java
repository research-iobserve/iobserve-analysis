package org.iobserve.adaption.data;

import org.iobserve.planning.systemadaptation.AssemblyContextAction;
import org.iobserve.planning.systemadaptation.ResourceContainerAction;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

public class RessourceContainerActionFactory extends ActionFactory {

	private static ResourceContainerAction setSourceResourceContainer(ResourceContainerAction action, String resourceContainerID)
	{
		ResourceEnvironment resEnvModel = ActionFactory.runtimeModels.getResourceEnvironmentModelProvider().getModel();
		ResourceContainer resourceContainer = ActionFactory.getResourceContainer(resourceContainerID, resEnvModel);
		action.setSourceResourceContainer(resourceContainer);
		return action;
	}
}
