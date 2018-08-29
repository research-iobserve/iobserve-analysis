package org.palladiosimulator.protocom.tech.rmi.allocation

import org.palladiosimulator.pcm.allocation.Allocation
import org.palladiosimulator.protocom.lang.java.impl.JMethod
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.tech.rmi.PojoClass

/**
 * @author Thomas Zolynski, Sebastian Lehrig
 */
class PojoAllocationStorage extends PojoClass<Allocation> {
	
	new(Allocation pcmEntity) {
		super(pcmEntity)
	}
	
	override superClass() {
		"org.palladiosimulator.protocom.framework.java.se.AbstractAllocationStorage"
	}
	
	override packageName() {
		"ProtoComBootstrap"
	}
	
	override compilationUnitName() {
		"AllocationStorage"
	}
	
	override methods() {
		#[
			new JMethod()
				.withName("initContainerTemplate")
				.withImplementation('''
					String container;
					String containerId;
					Class<?> component;
					String assemblyContext;
					
					// TODO This filters out Event Stuff. Maybe enable that in some future version.
					«FOR context : pcmEntity.allocationContexts_Allocation.filter[i | i.assemblyContext_AllocationContext != null]»
						containerId = "«context.resourceContainer_AllocationContext.id»";
						container = "«context.resourceContainer_AllocationContext.entityName»";
						component = «JavaNames::fqn(context.assemblyContext_AllocationContext.encapsulatedComponent__AssemblyContext)».class;
						assemblyContext = "«context.assemblyContext_AllocationContext.id»";
						saveContainerComponent(containerId, container, component, assemblyContext);
					«ENDFOR»
					''')		
		]
	}

	override filePath() {
		"/src/ProtoComBootstrap/AllocationStorage.java"
	}
	
	
	
}