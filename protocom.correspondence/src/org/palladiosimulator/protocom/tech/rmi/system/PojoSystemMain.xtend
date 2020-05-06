package org.palladiosimulator.protocom.tech.rmi.system

import org.palladiosimulator.protocom.lang.java.impl.JMethod
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.pcm.usagemodel.UsageScenario
import org.palladiosimulator.protocom.lang.java.util.PcmCalls
import org.palladiosimulator.protocom.tech.rmi.PojoClass

/**
 * @author Thomas Zolynski, Sebastian Lehrig
 */
class PojoSystemMain extends PojoClass<UsageScenario> {

	new(UsageScenario pcmEntity) {
		super(pcmEntity)
	}
	
	override superClass() {
		"org.palladiosimulator.protocom.framework.java.se.AbstractMain"
	}
		
	override packageName() {
		"ProtoComBootstrap"
	}
	
	override compilationUnitName() {
		"Main"
	}
	
	override methods() {
		#[
			new JMethod()
				.withName("initialiseThreads")
				.withParameters("de.uka.ipd.sdq.sensorframework.entities.Experiment exp, de.uka.ipd.sdq.sensorframework.entities.ExperimentRun expRun")
				.withImplementation('''
					int count = 1;
					if (runProps.hasOption("c")) {
						count = (Integer) runProps.getOptionObject("c");
					}
					for (int i = 0; i < count; i++) {
						threads.add(new «JavaNames::fqn(pcmEntity)»Thread(exp, expRun,
								runProps));
					}
				'''),
				
			new JMethod()
				.withName("setupResources")
				.withImplementation('''
					ResourceEnvironment.setUpResources(runProps.getOptionValue('p'),
					runProps.getOptionValue('H'), runProps.getOptionValue('s'),
					getAccuracy());
				'''),
				
			new JMethod()
				.withName("initialiseSystems")
				.withImplementation('''
					«FOR system : PcmCalls::getSystemsFromCalls(PcmCalls::querySystemCalls(pcmEntity.scenarioBehaviour_UsageScenario))»
						«JavaNames::fqn(system)».main(runProps.getOptionValue('R'), runProps.getOptionValue('O'));
					«ENDFOR»
					'''),
				
//				«FOREACH this.usageScenario_UsageModel.querySystemCalls().getSystemsFromCalls() AS us»
//					«us.fqn()».main(runProps.getOptionValue('R'), runProps.getOptionValue('O'));
//				«ENDFOREACH»
				
				
			new JMethod()
				.withName("initAllocationStorage")
				.withImplementation('''AllocationStorage.initSingleton(new AllocationStorage());'''),
				
			new JMethod()
				.withName("getSystems")
				.withReturnType("String[][]")
				.withImplementation('''
					String[][] systems = {
						{ 
						«FOR system : PcmCalls::getSystemsFromCalls(PcmCalls::querySystemCalls(pcmEntity.scenarioBehaviour_UsageScenario))»
							"«JavaNames::fqn(system)»", "«JavaNames::javaName(system)»"
						«ENDFOR»
						}
					};
					return systems;'''),
					
			new JMethod()
				.withName("main")
				.withParameters("String[] args")
				.withImplementation('''new Main().run(args);''')
				.withStaticModifier
				
			
		]
	}
	
	override filePath() {
	"/src/ProtoComBootstrap/Main.java"
	}
	
	
}