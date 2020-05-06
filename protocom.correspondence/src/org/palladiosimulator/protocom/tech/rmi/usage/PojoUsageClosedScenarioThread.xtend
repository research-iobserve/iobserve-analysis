package org.palladiosimulator.protocom.tech.rmi.usage

import org.palladiosimulator.pcm.usagemodel.UsageScenario
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.lang.java.impl.JMethod
import org.palladiosimulator.protocom.tech.rmi.PojoClass

/**
 * @author Thomas Zolynski, Sebastian Lehrig
 */
class PojoUsageClosedScenarioThread extends PojoClass<UsageScenario> {
	
	new(UsageScenario pcmEntity) {
		super(pcmEntity)
	}
	
	override superClass() {
		'''org.palladiosimulator.protocom.framework.java.se.usage.AbstractClosedScenarioThread'''
	}
	
	override constructors() {
		#[
			new JMethod()
				.withParameters('''de.uka.ipd.sdq.sensorframework.entities.Experiment exp, de.uka.ipd.sdq.sensorframework.entities.ExperimentRun expRun, org.palladiosimulator.protocom.framework.java.se.utils.RunProperties runProps''')
				.withImplementation('''super(exp, expRun, "Response Time of «pcmEntity.entityName»", runProps);''')
		]
	}
	
	override compilationUnitName() {
		JavaNames::javaName(pcmEntity) + "Thread"
	}
	
	override methods() {
		#[
			new JMethod()
				.withName("getScenarioRunner")
				.withParameters("org.palladiosimulator.protocom.framework.java.se.utils.RunProperties runProps")
				.withReturnType("Runnable")
				.withImplementation('''
					if (runProps.hasOption('R')) {
						org.palladiosimulator.protocom.framework.java.se.registry.RmiRegistry.setRemoteAddress(runProps.getOptionValue('R'));
					}
					else {
						org.palladiosimulator.protocom.framework.java.se.registry.RmiRegistry.setRemoteAddress(org.palladiosimulator.protocom.framework.java.se.registry.RmiRegistry.LOCALHOST);
					}
					return new «JavaNames::fqn(pcmEntity)»();
				''')
		]
	}
	
	override filePath() {
		"/src/" + JavaNames::fqnToDirectoryPath(JavaNames::implementationPackage(pcmEntity)) + "/" + JavaNames::javaName(pcmEntity) + "Thread.java"
	}
	
}