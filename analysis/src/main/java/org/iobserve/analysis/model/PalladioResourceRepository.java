package org.iobserve.analysis.model;

import org.palladiosimulator.pcm.resourcetype.CommunicationLinkResourceType;
import org.palladiosimulator.pcm.resourcetype.ProcessingResourceType;
import org.palladiosimulator.pcm.resourcetype.ResourceRepository;
import org.palladiosimulator.pcm.resourcetype.SchedulingPolicy;

/**
 * Convenient access to Palladio's {@link ResourceRepository}
 *
 * @author Fabian Keller
 */
public interface PalladioResourceRepository {

	ProcessingResourceType cpu();

	ProcessingResourceType hdd();

	ProcessingResourceType delay();

	CommunicationLinkResourceType lan();

	SchedulingPolicy policyProcessorSharing();

	SchedulingPolicy policyFCFS();

	SchedulingPolicy policyDelay();

	/**
	 * Hello stranger,
	 * <p>
	 * until there is a proper DI framework built into this project, we'll need
	 * to rely on this singleton to not have to pass around the
	 * {@link PalladioResourceRepository} to all builders. Unfortunately, some
	 * deeply nested builders require the resource repository and bubbling up
	 * the dependency leads to more technical debt than this singleton.
	 * <p>
	 * Hit <kbd>Alt+F7</kbd> to find the builders requiring the
	 * {@link PalladioResourceRepository}.
	 * <p>
	 * Have a nice day, anyways!
	 */
	public enum INSTANCE {
		;

		private static PalladioResourceRepository resources;

		/**
		 * Technical Debt - to be replaced by DI framework
		 */
		public static void initResources(final PalladioResourceRepository instance) {
			resources = instance;
		}

		/**
		 * Technical Debt - to be replaced by DI framework
		 */
		public static PalladioResourceRepository resources() {
			if (resources == null) {
				throw new RuntimeException("Palladio resource repository singleton has not been initialized!");
			}
			return resources;
		}
	}
}
