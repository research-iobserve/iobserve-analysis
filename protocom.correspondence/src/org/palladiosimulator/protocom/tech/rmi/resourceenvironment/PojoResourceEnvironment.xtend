package org.palladiosimulator.protocom.tech.rmi.resourceenvironment

import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment
import org.palladiosimulator.protocom.lang.java.impl.JMethod
import org.palladiosimulator.protocom.tech.ConceptMapping
import org.palladiosimulator.protocom.lang.java.IJClass

/**
 * Creates a configuration file for the ResourceEnvironment.
 * 
 * FIXME: ResourceEnvironment is not an Entity. Therefore we cannot reuse PojoClass at this moment...
 * 
 * @author Thomas Zolynski
 */
class PojoResourceEnvironment extends ConceptMapping<ResourceEnvironment> implements IJClass {
	
	new(ResourceEnvironment pcmEntity) {
		super(pcmEntity)
	}
	
	override superClass() {
		"org.palladiosimulator.protocom.framework.java.se.AbstractResourceEnvironment"
	}
	
	override packageName() {
		"ProtoComBootstrap"
	}
	
	override compilationUnitName() {
		"ResourceEnvironment"
	}
	
	override methods() {
		#[
			new JMethod()
				.withName("setUpResources")
				.withParameters("String cpuStrategy, String hddStrategy, String calibrationPath, org.palladiosimulator.protocom.resourcestrategies.activeresource.DegreeOfAccuracyEnum accuracy")
				.withStaticModifier
				.withImplementation('''
					String idContainer = org.palladiosimulator.protocom.framework.java.se.AbstractAllocationStorage.getActiveContainer();
					«FOR container : pcmEntity.resourceContainer_ResourceEnvironment SEPARATOR " else"»
					// Active resources of container «container.entityName»
					if (idContainer.equals("«container.id»")) {
						«FOR processingResource : container.activeResourceSpecifications_ResourceContainer»
							«IF processingResource.activeResourceType_ActiveResourceSpecification.entityName.toLowerCase().contains("cpu")»
							setUpCPU(cpuStrategy, calibrationPath, accuracy, "«processingResource.processingRate_ProcessingResourceSpecification.specification»");	
							«ELSEIF processingResource.activeResourceType_ActiveResourceSpecification.entityName.toLowerCase().contains("hdd")»
							setUpHDD(hddStrategy, calibrationPath, accuracy, "«processingResource.processingRate_ProcessingResourceSpecification.specification»");	
							«ENDIF»
						«ENDFOR»
					}
					«ENDFOR»
				''')
		]
	}
	
	override filePath() {
		"/src/ProtoComBootstrap/ResourceEnvironment.java"
	}
	
	override constructors() {
		// in juno, "#[]" instead of "newLinkedList" worked
		newLinkedList
	}
	
	override interfaces() {
		newLinkedList
	}
	
	override fields() {
		newLinkedList
	}

	override projectName() {
	}
	
	override annotations() {
	}
	
}