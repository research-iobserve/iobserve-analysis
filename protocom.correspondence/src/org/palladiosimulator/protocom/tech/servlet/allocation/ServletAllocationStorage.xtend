package org.palladiosimulator.protocom.tech.servlet.allocation

import org.palladiosimulator.pcm.allocation.Allocation
import org.palladiosimulator.protocom.lang.java.impl.JMethod
import org.palladiosimulator.protocom.model.allocation.AllocationAdapter
import org.palladiosimulator.protocom.tech.servlet.ServletClass

class ServletAllocationStorage extends ServletClass<Allocation> {
	protected val frameworkBase = "org.palladiosimulator.protocom.framework.java.ee"
	private val AllocationAdapter entity
	
	new(AllocationAdapter entity, Allocation pcmEntity) {
		super(pcmEntity)
		this.entity = entity
	}
	
	override superClass() {
	}
	
	override packageName() {
		"main"
	}
	
	override compilationUnitName() {
		"ComponentAllocation"
	}
	
	override methods() {
		var contexts = entity.allocationContexts
		var i = 0
		
		#[
			new JMethod()
				.withVisibilityModifier("public")
				.withStaticModifier
				.withName("init")
				.withParameters('''«frameworkBase».prototype.PrototypeBridge bridge''')
				.withImplementation('''
					«frameworkBase».prototype.PrototypeBridge.Allocation[] allocations = new «frameworkBase».prototype.PrototypeBridge.Allocation[«contexts.length»];
					
					«FOR context : contexts»
						allocations[«i++»] = bridge.new Allocation("«context.resourceContainer.id»", «context.assemblyContext.encapsulatedComponent.classFqn».class, "«context.assemblyContext.id»");
					«ENDFOR»
					
					bridge.setAllocations(allocations);
				''')
		]
	}
	
	override filePath() {
		"/src/main/ComponentAllocation.java"
	}	
}
