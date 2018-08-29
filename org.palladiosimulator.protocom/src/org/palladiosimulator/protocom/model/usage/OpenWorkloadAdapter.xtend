package org.palladiosimulator.protocom.model.usage

import org.palladiosimulator.pcm.usagemodel.OpenWorkload

/**
 * Adapter class for PCM OpenWorkload entities.
 * @author Christian Klaussner
 */
class OpenWorkloadAdapter extends WorkloadAdapter<OpenWorkload> {
	new(OpenWorkload entity) {
		super(entity)
	}
}
