package org.palladiosimulator.protocom.tech.iiop.repository

import org.palladiosimulator.pcm.allocation.AllocationContext
import org.palladiosimulator.pcm.core.composition.AssemblyConnector
import java.util.HashMap
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.tech.iiop.JavaEEIIOPDescriptor
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI

class JavaEEIIOPGlassfishEjbDescriptor extends JavaEEIIOPDescriptor<AllocationContext> {
	private val allocation = pcmEntity.allocation_AllocationContext
	private var HashMap<AssemblyConnector, String> assemblyConnectorIPHashMap = newHashMap

	new(AllocationContext pcmEntity) {
		super(pcmEntity)
	}

	override ejbName() {
		JavaNames::javaName(pcmEntity.assemblyContext_AllocationContext.encapsulatedComponent__AssemblyContext)
	}

	override ejbRefName() {
	}

	override jndiName() {
	}

	override filePath() {
		JavaNames::fqnJavaEEDescriptorPath(
			pcmEntity.assemblyContext_AllocationContext.encapsulatedComponent__AssemblyContext) + "glassfish-ejb-jar.xml"
	}

	override projectName() {
		JavaNames::fqnJavaEEDescriptorProjectName(
			pcmEntity.assemblyContext_AllocationContext.encapsulatedComponent__AssemblyContext)
	}

	override requiredComponentsAndResourceContainerIPAddress() {
		val basicComponentAssemblyConnectors = allocation.system_Allocation.connectors__ComposedStructure.filter(
			typeof(AssemblyConnector)).filter[
			it.requiredRole_AssemblyConnector.requiringEntity_RequiredRole.equals(
				pcmEntity.assemblyContext_AllocationContext.encapsulatedComponent__AssemblyContext)]

		for (connector : basicComponentAssemblyConnectors) {
			var requiredEntityAllocationContext = allocation.allocationContexts_Allocation.filter[
				it.assemblyContext_AllocationContext.encapsulatedComponent__AssemblyContext.equals(
					connector.providedRole_AssemblyConnector.providingEntity_ProvidedRole)]
			for (allocationContext : requiredEntityAllocationContext) {
				var resourceContainer = allocationContext.resourceContainer_AllocationContext
				var resourceContainerAppliedStereotypes = StereotypeAPI.getStereotypeApplications(resourceContainer, "IIOP")
				
				if(resourceContainerAppliedStereotypes !== null){
					for (stereotypeApplication : resourceContainerAppliedStereotypes) {
						var ipValue = stereotypeApplication.eGet(stereotypeApplication.extension.source.getTaggedValue("IpAddress")).toString
						assemblyConnectorIPHashMap.put(connector, ipValue)
					}
				}
				else{
					assemblyConnectorIPHashMap.put(connector, "localhost")
				}
			}
		}

		return assemblyConnectorIPHashMap

	}
}
