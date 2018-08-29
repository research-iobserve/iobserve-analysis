package org.palladiosimulator.protocom.model.usage

import org.palladiosimulator.pcm.usagemodel.Stop

/**
 * Adapter class for PCM Stop user actions.
 * @author Christian Klaussner
 */
class StopAdapter extends UserActionAdapter<Stop> {
	new(Stop entity) {
		super(entity)
	}	
}
