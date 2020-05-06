package org.palladiosimulator.protocom.traverse.jse.usage

import org.palladiosimulator.protocom.traverse.framework.usage.XUsageScenario
import org.palladiosimulator.protocom.lang.java.impl.JClass
import org.palladiosimulator.protocom.tech.rmi.usage.PojoUsageScenario
import org.palladiosimulator.protocom.tech.rmi.usage.PojoUsageClosedScenarioThread
import org.palladiosimulator.protocom.tech.rmi.system.PojoSystemMain

class JseUsageScenario extends XUsageScenario {
	
	override generate() {
		
		generatedFiles.add(injector.getInstance(typeof(JClass)).createFor(new PojoUsageScenario(entity)))
		
		generatedFiles.add(injector.getInstance(typeof(JClass)).createFor(new PojoUsageClosedScenarioThread(entity)))
		
		// Main class for execution.
		generatedFiles.add(injector.getInstance(typeof(JClass)).createFor(new PojoSystemMain(entity)));
	}
	
}