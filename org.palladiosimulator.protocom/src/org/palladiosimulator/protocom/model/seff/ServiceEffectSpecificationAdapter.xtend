package org.palladiosimulator.protocom.model.seff

import org.palladiosimulator.pcm.seff.ServiceEffectSpecification
import org.palladiosimulator.protocom.model.ModelAdapter
import org.palladiosimulator.pcm.seff.ResourceDemandingBehaviour
import org.palladiosimulator.protocom.model.repository.SignatureAdapter
import org.palladiosimulator.pcm.seff.StartAction

/**
 * Adapter class for PCM ServiceEffectSpecification entities.
 * @author Christian Klaussner
 */
class ServiceEffectSpecificationAdapter extends ModelAdapter<ServiceEffectSpecification> {
	new(ServiceEffectSpecification entity) {
		super(entity)
	}
	
	/**
	 * Gets the signature.
	 * @return an adapter for the signature
	 */
	def getSignature() {
		new SignatureAdapter(entity.describedService__SEFF)
	}
	
	def getStart() {
		val steps = (entity as ResourceDemandingBehaviour).steps_Behaviour
		new StartActionAdapter(steps.get(0) as StartAction)
	}
}
