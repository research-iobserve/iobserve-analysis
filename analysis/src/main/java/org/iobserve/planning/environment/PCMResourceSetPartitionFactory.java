package org.iobserve.planning.environment;

import org.eclipse.emf.common.util.URI;
import org.palladiosimulator.analyzer.workflow.blackboard.PCMResourceSetPartition;
import org.palladiosimulator.analyzer.workflow.configurations.AbstractPCMWorkflowRunConfiguration;

/**
 * Creates a {@link PCMResourceSetPartition} where Palladio default models are
 * already loaded.
 *
 * @author Fabian Keller
 */
public interface PCMResourceSetPartitionFactory {

	/**
	 * Creates a {@link PCMResourceSetPartition} that can be used in headless
	 * mode.
	 */
	PCMResourceSetPartition create();

	/**
	 * {@link PCMResourceSetPartition}s do not by default hold references to the
	 * default models shipped with Palladio. They are somehow automagically
	 * registered when loading the model. However, we need to load them manually
	 * and this class shows how this can be done. While this is not nice and
	 * future proof, it works for the moment. This should be replaced by some
	 * proper loading in the near future.
	 */
	class DefaultFactory implements PCMResourceSetPartitionFactory {

		private static final String[] toLoad = { "pathmap://PCM_MODELS/Palladio.resourcetype" };

		@Override
		public PCMResourceSetPartition create() {
			PCMResourceSetPartition rsp = new PCMResourceSetPartition();
			// final ResourceSet rs = new ResourceSetImpl();
			rsp.getResourceSet().setURIConverter(PalladioEclipseEnvironment.INSTANCE.getUriConverter());
			rsp.initialiseResourceSetEPackages(AbstractPCMWorkflowRunConfiguration.PCM_EPACKAGES);

			// load default models
			for (final String model : toLoad) {
				rsp.loadModel(URI.createURI(model));
			}

			rsp.resolveAllProxies();
			return rsp;
		}
	}
}
